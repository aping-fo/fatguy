package com.game.service;

import com.game.SysConfig;
import com.game.dao.LoginLogDAO;
import com.game.dao.PlayerDAO;
import com.game.data.TaskCfg;
import com.game.data.ToolCfg;
import com.game.domain.buffer.Buffer;
import com.game.domain.mail.MailConsts;
import com.game.domain.player.*;
import com.game.domain.task.Task;
import com.game.domain.task.TaskConsts;
import com.game.sdk.http.HttpClient;
import com.game.sdk.net.Result;
import com.game.sdk.proto.GetRoleDetailResp;
import com.game.sdk.proto.LoginResp;
import com.game.sdk.proto.OpenIDResp;
import com.game.sdk.proto.vo.GiveTicketVO;
import com.game.sdk.proto.vo.PlayerRankVO;
import com.game.sdk.proto.vo.TicketBallVO;
import com.game.sdk.proto.vo.VisitablePlayerVO;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.JsonUtils;
import com.game.util.TimeUtil;
import com.game.util.TimerService;
import com.google.common.cache.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class PlayerService extends AbstractService {
    private static Logger logger = Logger.getLogger(PlayerService.class);
    private Map<String, String> sessionMap = Maps.newHashMap();         //session保存，一段时间清空

    @Autowired
    private TimerService timerService;
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private LoginLogDAO loginLogDAO;
    @Autowired
    private ServerDataService serverDataService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TreasureService treasureService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private LuckdrawService luckdrawService;

    //排行榜
    private Map<String, PlayerRank> globalRankMap = new ConcurrentHashMap<>();
    private TreeMap<PlayerRank, String> globalRank = new TreeMap<>(new Comparator<PlayerRank>() {
        @Override
        public int compare(PlayerRank o1, PlayerRank o2) {
            if(o1.getOpenId().equals(o2.getOpenId())){
                return 0;
            }

            if(o2.getStealTimes() == o1.getStealTimes()){
                return 1;
            }
            return o2.getStealTimes() - o1.getStealTimes();
        }
    });

    static final int MAX_GIVE_TICKET = 50;
    static final float OFFLINE_TICKETS_PER_SECOND = 1.f/3600;
    static final float OFFLINE_RUNMACHINE_TICKETS_PER_SECOND = OFFLINE_TICKETS_PER_SECOND * 2;

    //TODO 可以优化为停服时统一保存
    private final LoadingCache<String, Player> players = CacheBuilder.newBuilder()
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .maximumSize(5000)
            .removalListener(new RemovalListener<String, Player>() {
                @Override
                public void onRemoval(RemovalNotification<String, Player> notification) {
                    saveOrUpdate(notification.getValue(), false);
                }
            })
            .build(new CacheLoader<String, Player>() {
                @Override
                public Player load(String openId) throws Exception {
                    logger.info("Cache loaded for " + openId);
                    Player player = playerDAO.queryPlayer(openId);
                    if(player == null){
                        return null;
                    }
                    PlayerData playerData = JsonUtils.string2Object(player.getPlayerData(), PlayerData.class);

                    //初始化任务列表
                    Collection<Object> taskCfgs = ConfigData.getConfigs(TaskCfg.class);
                    for(Object obj : taskCfgs){
                        TaskCfg taskCfg = (TaskCfg)obj;
                        Task task = playerData.getTasks().getOrDefault(taskCfg.id, null);

                        if(task == null){
                            task = new Task(taskCfg.id);
                            playerData.getTasks().put(task.getId(), task);
                        }

                        task.setCfg(taskCfg);
                    }
                    playerData.getTasks().remove(TaskConsts.TASK_TYPE_PIN_PROGRAM);

                    //初始化buff
                    Collection<Buffer> buffs = playerData.getBufferList();
                    for(Buffer buff : buffs){
                        ToolCfg cfg = ConfigData.getConfig(ToolCfg.class, buff.getId());
                        buff.setGroup(cfg.group);
                    }

                    player.setPlayerDataObject(playerData);

                    return player;
                }
            });


    @Override
    public void onStart() {
        //玩家排行榜
        List<Player> players = playerDAO.queryAllPlayer();
        for (Player player : players) {
            PlayerRank vo = player.toPlayerRank();
            globalRank.put(vo, player.getOpenId());
            globalRankMap.put(player.getOpenId(), vo);
        }
    }

    /**
     * 保存或更新
     *
     * @param player
     */
    public void saveOrUpdate(Player player, boolean updateRank) {
        if(updateRank) {
            //更新排行榜
            PlayerRank playerRank = globalRankMap.get(player.getOpenId());
            if (playerRank != null) {
                globalRank.remove(playerRank);
                playerRank = player.toPlayerRank();
                globalRank.put(playerRank, player.getOpenId());
                globalRankMap.put(player.getOpenId(), playerRank);
            }
        }
        player.setPlayerData(JsonUtils.object2String(player.getPlayerDataObject()));
        playerDAO.saveOrUpdate(player);
    }

    /**
     * 获取玩家信息
     *
     * @param openId
     * @return
     */
    public Player getPlayer(String openId) {
        try {
            return players.get(openId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建角色
     *
     * @param openId
     * @param nickName
     */
    public Result createPlayer(String openId, String nickName, String avatarUrl) {
        Player player = this.getPlayer(openId);
        if (player != null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "ok");
        }

        player = new Player();
        player.setNickName(nickName);
        player.setOpenId(openId);
        player.setStealTimes(0);
        player.setAvatarUrl(avatarUrl);
        player.setCreateTime(System.currentTimeMillis());
        player.setLoginTime(System.currentTimeMillis());

        //初始化任务列表
        Collection<Object> taskCfgs = ConfigData.getConfigs(TaskCfg.class);
        for(Object obj : taskCfgs){
            TaskCfg taskCfg = (TaskCfg)obj;
            Task task = new Task(taskCfg.id);

            player.getPlayerDataObject().getTasks().put(task.getId(), task);
        }

        //初始化券
        player.getPlayerDataObject().setTicket(1);
        player.getPlayerDataObject().setLastCalculateTime(TimeUtil.getCurrentSeconds());

        players.put(openId, player);

        //新人获得新手宝箱和新手步数，走引导流程
        treasureService.addNewPlayerTreasureBox(openId);
        this.generateTicketBall(openId, 0.1f, TicketConsts.TICKET_TYPE_NORMAL, null);

        saveOrUpdate(player, true);

        PlayerRank playerRank = player.toPlayerRank();
        globalRank.put(playerRank, player.getOpenId());
        globalRankMap.put(player.getOpenId(), playerRank);

        return Result.valueOf(ErrorCode.OK, "ok");
    }

    /**
     * 获取OPENID
     *
     * @param openId
     * @param code
     * @return
     * @throws Exception
     */
    public Result getOpenID(String openId, String code) throws Exception {
        String errorCode = ErrorCode.OK;
        OpenIDResp resp = new OpenIDResp();
        String session;
        if (!"".equals(code))//测试模式
        {
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + SysConfig.wxAppid + "&secret=" + SysConfig.wxAppSecret + "&grant_type=authorization_code&js_code=" + code;
            String json = HttpClient.sendGetRequest(url);
            Map<String, Object> result = JsonUtils.string2Map(json);
            openId = (String) result.get("openid");
            session = (String) result.get("session_key");
        } else {
            session = "test" + openId;
        }
        try {
            players.get(openId);
            resp.setHasRole(true);
        } catch (Exception e) {
            resp.setHasRole(false);
        }

        logger.warn("player openId: " + openId + ", session: " + session);

        sessionMap.put(openId, session);
        resp.setOpenId(openId);

        return Result.valueOf(errorCode, resp);
    }

    /**
     * 登陆
     *
     * @param openId
     */
    public Result login(String openId, String nickName, String avatarUrl) {
        String code = ErrorCode.OK;
        LoginResp resp = new LoginResp();
        Player player = players.getUnchecked(openId);
        if (player == null) {
            code = ErrorCode.ROLE_NOT_EXIST;
        } else {
            if (nickName != null && nickName.length() > 0) {
                player.setNickName(nickName);
            }
            if (avatarUrl != null && avatarUrl.length() > 0) {
                player.setAvatarUrl(avatarUrl);
            }

            PlayerData playerData = player.getPlayerDataObject();
            resp.setOpenId(player.getOpenId());
            resp.setStealTimes(player.getStealTimes());

            boolean isTodayFirstLogin = !TimeUtil.isSameDate(TimeUtil.getTimeNow(), player.getLoginTime());
            resp.setFirstLogin(isTodayFirstLogin);

            //第二天登陆
            boolean isSecondDayLogin = isTodayFirstLogin && TimeUtil.isSameDate(player.getLoginTime(), player.getCreateTime());
            resp.setSecondDayLogin(isSecondDayLogin);

            if (isTodayFirstLogin) {
                //每日重置
                loginLogDAO.insert(player.getOpenId(), new Date());

                this.resetDaily(player);
            }
            player.setLoginTime(System.currentTimeMillis());

            //登陆初始化道具ID,宝箱ID,邮箱ID起始位置
            playerData.getBag().loadIdgen();
            playerData.getTreasureWarehouse().loadIdgen();
            playerData.getMailBox().loadIdgen();

            //登陆任务
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            taskService.finishTask(openId, TaskConsts.TASK_TYPE_LOGIN, 1, hour);

            //活动
            activityService.checkAndResetActivityData(openId);

            this.updateTickets(openId, true);

            //更新下
            saveOrUpdate(player, false);
        }

        return Result.valueOf(code, resp);
    }

    public void doRankReward(){
        List<PlayerRank> list = new ArrayList<>(globalRank.keySet());
        //前100名
        int limit = Math.min(list.size(), 100);
        for(int i = 0; i < limit; i++){
            int mailType = 0;
            if(i == 0){
                mailType = MailConsts.MAILTYPE_RANK1;
            }else if(i == 1){
                mailType = MailConsts.MAILTYPE_RANK2;
            }else if(i == 2){
                mailType = MailConsts.MAILTYPE_RANK3;
            }else if(i >= 3 && i <= 9){
                mailType = MailConsts.MAILTYPE_RANK4_10;
            }else if(i >= 10 && i <= 49){
                mailType = MailConsts.MAILTYPE_RANK11_50;
            }else{
                mailType = MailConsts.MAILTYPE_RANK51_100;
            }

            if(mailType != 0){
                PlayerRank rank = list.get(i);
                Player p = this.getPlayer(rank.getOpenId());
                p.getPlayerDataObject().getMailBox().addMail(mailType, String.valueOf(i + 1));
            }
        }
    }

    /**
     * 获取用户详细数据
     *
     * @param openId
     */
    public Result getRoleDetailInfo(String visitorOpenId, String openId) {
        Player visitorPlayer = players.getUnchecked(visitorOpenId);
        if (visitorPlayer == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        Player player = players.getUnchecked(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        GetRoleDetailResp resp = new GetRoleDetailResp();
        this.updateTickets(openId, false);

        resp.setOpenId(player.getOpenId());
        resp.setNickName(player.getNickName());
        resp.setAvatarUrl(player.getAvatarUrl());
        resp.setStealTimes(player.getStealTimes());
        resp.setTicket(player.getPlayerDataObject().getTicket());

        //获取buffer信息
        PlayerData playerData = player.getPlayerDataObject();
        Collection<Buffer> allBuffer = playerData.getBufferList();
        for (Buffer buffer : allBuffer) {
            resp.getBuffs().add(buffer.toProto());
        }

        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 访问
     *
     * @param visitorOpenId
     * @param openId
     */
    public Result visit(String visitorOpenId, String openId) {
        Player visitorPlayer = players.getUnchecked(visitorOpenId);
        if (visitorPlayer == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        Player player = players.getUnchecked(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        if (!openId.equals(visitorOpenId)) {
            //访问动态
            this.addInformation(openId, visitorOpenId, TrendsConsts.VISIT, TimeUtil.timeFormatter.format(new Date()));
        }

        return Result.valueOf(ErrorCode.OK);
    }

    /**
     * 从客户端生成奖券球
     * @param openId
     * @param ticket
     * @return
     */
    public Result generateTicketBallFromClient(String openId, float ticket){
        Player player = getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        player.getPlayerDataObject().setLastCalculateTime(TimeUtil.getCurrentSeconds());

        this.generateTicketBall(openId, ticket, TicketConsts.TICKET_TYPE_NORMAL, null);

        return Result.valueOf(ErrorCode.OK);
    }

    public Result giveTicket(String openId, String giveOpenId) {
        Player player = getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        Player givePlayer = getPlayer(giveOpenId);
        if (givePlayer == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        float ticket = MAX_GIVE_TICKET;
        float giveTicketBefore = givePlayer.getPlayerDataObject().getPlayerGiveRecords().getOrDefault(openId, 0.f);
        if(giveTicketBefore >= MAX_GIVE_TICKET){
            return Result.valueOf(ErrorCode.GIVE_LIMIT, "error");
        }

        float giveTicket = Math.min(MAX_GIVE_TICKET - giveTicketBefore, ticket);

        if (giveTicket > 0) {
            givePlayer.getPlayerDataObject().getPlayerGiveRecords().put(openId, giveTicketBefore + giveTicket);

            GiveTicketVO vo = new GiveTicketVO();
            vo.setOpenId(player.getOpenId());
            vo.setNickName(player.getNickName());
            vo.setIconUrl(player.getAvatarUrl());

            this.generateTicketBall(giveOpenId, giveTicket, TicketConsts.TICKET_TYPE_GIVE, vo);
        }

        return Result.valueOf(ErrorCode.OK, giveTicket);
    }

    /**
     * 查询奖券球列表
     *
     * @param openId
     * @return
     */
    public Result getTicketBalls(String openId) {
        Player player = getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        List<TicketBallVO> ret = Lists.newArrayList();
        Map<Integer, List<TicketBallVO>> map = player.getPlayerDataObject().getTicketBallMap();

        if (map == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        for (List<TicketBallVO> list : map.values()) {
            ret.addAll(list);
        }

        return Result.valueOf(ErrorCode.OK, ret);
    }

    /**
     * 收获奖券球
     *
     * @param openId
     * @param id
     * @param type
     * @return
     */
    public Result gainTicketBall(String openId, String targetOpenId,  String id, int type) {
        Player player = getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        Player targetPlayer = getPlayer(targetOpenId);
        if (targetPlayer == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        List<TicketBallVO> list = targetPlayer.getPlayerDataObject().getTicketBallMap().get(type);
        TicketBallVO ticketVo = null;

        for (TicketBallVO vo : list) {
            if (vo.getId().equals(id)) {
                ticketVo = vo;
                break;
            }
        }

        if (ticketVo == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        list.remove(ticketVo);
        this.saveOrUpdate(targetPlayer, false);

        //收获奖券
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(2);
        float gainTicket = ticketVo.getTicket();

        if (gainTicket > 0) {
            player.getPlayerDataObject().setTicket(player.getPlayerDataObject().getTicket() + gainTicket);
            this.saveOrUpdate(player, false);

            resp.put("ticket", player.getPlayerDataObject().getTicket());
            resp.put("gainTicket", gainTicket);
        }

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result cancelBuff(String openId, int buffId){
        Player player = getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        this.updateTickets(openId, false);

        player.getPlayerDataObject().clearBufferCD(buffId);

        return Result.valueOf(ErrorCode.OK);
    }

    public Result getTrends(String openId){
        Player player = getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(2);
        resp.put("trends", player.getPlayerDataObject().getTrends());
        resp.put("globalTrends", serverDataService.getServerData().getTrends());

        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 获取可访问人信息,排行榜前10或者好友
     * @param openId
     * @return
     */
    public Result getVisitableRoleInfo(String openId){
        Player player = this.getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        List<PlayerRank> list = new ArrayList<>(globalRank.keySet());
        Map<String, VisitablePlayerVO> players = Maps.newHashMap();

        //前10名
        int limit = Math.min(list.size(), 10);
        for(int i = 0; i < limit; i++){
            PlayerRank rankVo = list.get(i);

            if(!players.containsKey(rankVo.getOpenId())){
                treasureService.updatePlayerTreasure(rankVo.getOpenId());

                Player p = this.getPlayer(rankVo.getOpenId());
                VisitablePlayerVO playerVo = new VisitablePlayerVO();

                playerVo.setOpenId(rankVo.getOpenId());
                playerVo.setNickName(rankVo.getNickName());
                playerVo.setAvatarUrl(rankVo.getAvatarUrl());
                playerVo.setAward(p.getPlayerDataObject().getTreasureWarehouse().isAward());
                players.put(rankVo.getOpenId(), playerVo);
            }
        }


        //好友
        List<String> friendList = player.getPlayerDataObject().getFriendList();
        for (String fOpenId : friendList) {
            if(!players.containsKey(fOpenId)){
                Player fPlayer = this.getPlayer(fOpenId);
                if(fPlayer != null){
                    treasureService.updatePlayerTreasure(fOpenId);

                    VisitablePlayerVO playerVo = new VisitablePlayerVO();

                    playerVo.setOpenId(fPlayer.getOpenId());
                    playerVo.setNickName(fPlayer.getNickName());
                    playerVo.setAvatarUrl(fPlayer.getAvatarUrl());
                    playerVo.setAward(fPlayer.getPlayerDataObject().getTreasureWarehouse().isAward());
                    players.put(fOpenId, playerVo);
                }
            }
        }

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        resp.put("list", players.values());

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public void updateTickets(String openId, boolean isForce) {
        Player player = getPlayer(openId);
        if (player == null) {
            return;
        }

        PlayerData playerData = player.getPlayerDataObject();
        int now = TimeUtil.getCurrentSeconds();
        int lastCalculateTime = playerData.getLastCalculateTime();

        if(!isForce && now - lastCalculateTime < 3600){
            //没满一小时不算。
            return;
        }

        playerData.setLastCalculateTime(now);

        float ticket = 0;

        for(Buffer buff : playerData.getBufferList()){
            if(buff.getCdStart() == 0){
                buff.setCdStart(lastCalculateTime);
            }

            //秒数
            int availableTime = buff.getPassCDTime();
            if(availableTime > 0){
                if(buff.getType() == Buffer.BUFF_TYPE_RUNMACHINE || buff.getType() == Buffer.BUFF_TYPE_FATWATER){
                    if(buff.getType() == Buffer.BUFF_TYPE_RUNMACHINE){
                        //跑步机
                        ticket += availableTime * OFFLINE_RUNMACHINE_TICKETS_PER_SECOND;
                    }
                    lastCalculateTime += availableTime;
                }
            }

            if(!buff.checkTimeout()){
                break;
            }
        }

        playerData.clearAndUpdateBufferCD();

        if(now - lastCalculateTime > 0){
            ticket += (now - lastCalculateTime) * OFFLINE_TICKETS_PER_SECOND;
        }

        //生成奖券
        this.generateTicketBall(openId, ticket, TicketConsts.TICKET_TYPE_NORMAL, null);

        saveOrUpdate(player, false);
    }

    /**
     * 世界排行榜
     * @param openId
     * @return
     */
    public Result getWorldRank(String openId){
        Player player = this.getPlayer(openId);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR, "error");
        }

        List<PlayerRankVO> playerRankVOs = Lists.newArrayList();
        List<PlayerRank> list = new ArrayList<>(globalRank.keySet());
        PlayerRankVO myRankVO = globalRankMap.get(openId).toPlayerRankVO();
        myRankVO.setRank(0);

        //前100名
        int limit = Math.min(list.size(), 100);
        for(int i = 0; i < limit; i++){
            PlayerRankVO vo = list.get(i).toPlayerRankVO();

            vo.setRank(i + 1);
            if(vo.getOpenId().equals(openId)){
                myRankVO.setRank(i + 1);
            }

            //前10名显示是否有宝箱
            if(i < 10){
                Player p = this.getPlayer(vo.getOpenId());
                treasureService.updatePlayerTreasure(vo.getOpenId());
                vo.setAward(p.getPlayerDataObject().getTreasureWarehouse().isAward());
            }

            playerRankVOs.add(vo);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("list", playerRankVOs);
        resp.put("me", myRankVO);
        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 增加动态 ,配置全局表 xxxx{0}xxxx{1}xxxx
     * @param ownerOpenId
     * @param openId
     * @param id
     * @param params
     */
    public void addInformation(String ownerOpenId, String openId, int id, Object... params) {
        Player owner = this.getPlayer(ownerOpenId);
        Player player = owner;
        if(!ownerOpenId.equals(openId)){
            player = this.getPlayer(openId);
        }

        owner.getPlayerDataObject().addTrends(openId, player.getNickName(), player.getAvatarUrl(), id, params);
    }

    /**
     * 生成奖券球
     *
     * @param openId
     * @param ticket
     * @param type
     * @param param
     */
    public float generateTicketBall(String openId, float ticket, int type, GiveTicketVO param) {
        Player player = players.getUnchecked(openId);

        if (ticket < 0.1) {
            return 0;
        }

        List<TicketBallVO> list = player.getPlayerDataObject().getTicketBallMap().get(type);
        boolean isFind = false;

        if (type == TicketConsts.TICKET_TYPE_GIVE) {
            for (TicketBallVO vo : list) {
                if (vo.getParam().getOpenId().equals(param.getOpenId())) {
                    vo.setTicket(vo.getTicket() + ticket);
                    isFind = true;
                    break;
                }
            }
        }

        if (!isFind) {
            TicketBallVO vo = new TicketBallVO();
            vo.setId(UUID.randomUUID().toString());
            vo.setType(type);
            vo.setTicket(ticket);
            vo.setParam(param);

            list.add(vo);
        }

        return ticket;
    }

    /**
     * 每日重置
     * @param player
     */
    private void resetDaily(Player player){
        PlayerData playerData = player.getPlayerDataObject();

        //任务
        for(Task task : playerData.getTasks().values()){
            if(task.getResetType() == TaskConsts.TASK_RESETTYPE_DAILY){
                task.reset();
            }
        }

        //给予奖券
        playerData.getPlayerGiveRecords().clear();;
    }
}
