package com.game.service;

import com.game.dao.PlayerDAO;
import com.game.data.AwardCfg;
import com.game.data.RoleInfoCfg;
import com.game.domain.Luckdraw;
import com.game.domain.ServerData;
import com.game.domain.mail.MailConsts;
import com.game.domain.player.LuckdrawRole;
import com.game.domain.player.Player;
import com.game.domain.player.PlayerData;
import com.game.domain.player.TrendsConsts;
import com.game.domain.task.TaskConsts;
import com.game.sdk.net.Result;
import com.game.sdk.proto.vo.LuckdrawAttenderVO;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.RandomUtil;
import com.game.util.TimeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by lucky on 2019/3/16.
 */
@Service
public class LuckdrawService extends AbstractService {
    private static Logger logger = Logger.getLogger(LuckdrawService.class);
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ServerDataService serverDataService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private PlayerDAO playerDAO;

    private static final float MONEY_ORIGINAL = 48.f;

    public void init(){
        String curLuckdrawKey = serverDataService.getServerData().getCurLuckdrawKey();
        if(true || curLuckdrawKey.equals("")){
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            int month = now.getMonthValue();
            int day = now.getDayOfMonth();

            curLuckdrawKey = year + "" + month + "" + day;
            serverDataService.getServerData().setCurLuckdrawKey(curLuckdrawKey);
            serverDataService.getServerData().setLuckdrawRewardTime(TimeUtil.getTimeNow());
        }
//
//        Luckdraw currentDraw = this.getCurrentLuckdraw();
//
//        for(LuckdrawRole role : currentDraw.getRoleMap().values()){
//            int attendCount = 0;
//            for(String openId: currentDraw.getLuckdrawMap().values()){
//                if(openId.equals(role.getOpenid())){
//                    attendCount++;
//                }
//            }
//
//            role.setAttendTimes(attendCount);
//        }

//        this.forceDoLuckdraw();

//        serverDataService.saveServerData();
    }

    /**
     * 获取当前抽奖数据
     *
     * @return
     */
    private synchronized Luckdraw getCurrentLuckdraw() {
        ServerData serverData = serverDataService.getServerData();
        String key = serverData.getCurLuckdrawKey();
        Luckdraw luckdraw = serverData.getLuckdraw().get(key);

        if (luckdraw == null) {
            luckdraw = new Luckdraw();
            luckdraw.setId(key);
            luckdraw.setMoney(MONEY_ORIGINAL);
            serverData.getLuckdraw().put(key, luckdraw);
        }
        return luckdraw;
    }

    /**
     * 参与抽奖
     *
     * @param openid
     * @return
     */
    public Result attendLuckdraw(String openid, int ticket) {
        Player player = playerService.getPlayer(openid);
        PlayerData data = player.getPlayerDataObject();
        if (data.getTicket() < 1) {
            return Result.valueOf(ErrorCode.TICKET_NOT_ENOUGH);
        }

        AwardCfg config = ConfigData.getConfig(AwardCfg.class, 888);

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int min = now.getMinute();

        int openHour2 = config.awardTime[1][0];
        int openmin2 = config.awardTime[1][1];

        if (hour >= openHour2 && min >= openmin2) {
            return Result.valueOf(ErrorCode.LUCK_DRAW_TIME);
        }

        ticket = Math.min(Math.min(ticket, (int)data.getTicket()), 10);
        data.setTicket(data.getTicket() - ticket);

        Luckdraw currentLuckdraw = getCurrentLuckdraw();
//        List<String> luckdrawNOs = data.getLuckdrawRecords().get(currentLuckdraw.getId());
//        if (luckdrawNOs == null) {
//            //第一次参加
//            luckdrawNOs = Lists.newArrayList();
//            data.getLuckdrawRecords().put(currentLuckdraw.getId(), luckdrawNOs);
//
//            //增加奖金
//            currentLuckdraw.setMoney(currentLuckdraw.getMoney() + config.bonus);
//        }

        LuckdrawRole role = currentLuckdraw.getRoleMap().getOrDefault(openid, null);
        if(role == null){
            //第一次参加
            role = new LuckdrawRole();
            role.setOpenid(openid);
            role.setAvatarUrl(player.getAvatarUrl());
            role.setNickName(player.getNickName());
            currentLuckdraw.getRoleMap().put(openid, role);

            //增加奖金
            currentLuckdraw.setMoney(currentLuckdraw.getMoney() + config.bonus);
        }

        List<String> nos = Lists.newArrayList();
        String no = "";
        double probability = 0;
        int attendTimes = role.getAttendTimes();

        synchronized (this) {
            for(int i = ticket; i > 0; i--){
                no = currentLuckdraw.genNumber();
                nos.add(no);
                currentLuckdraw.getLuckdrawMap().put(no, openid);

                attendTimes++;

                int totalAttendTimes = currentLuckdraw.getLuckdrawMap().size();

                probability = Math.floor((float) attendTimes / totalAttendTimes * 10000) / 10000;
                data.getMailBox().addMail(MailConsts.MAILTYPE_LOTTERY_ATTEND, no, String.format("%.2f", probability * 100) + "%");
            }
        }

        role.setAttendTimes(attendTimes);

        this.updateRolesProbability();

        //动态
        serverDataService.addTrends(openid, player.getNickName(), player.getAvatarUrl(), TrendsConsts.LOTTERY_ATTEND, attendTimes);

        //完成任务
        taskService.finishTask(openid, TaskConsts.TASK_TYPE_LOTTERY, 1);

        int attenderCount = currentLuckdraw.getRoleMap().size();
        float money = currentLuckdraw.getMoney();

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(6);
        resp.put("nos", nos);
        resp.put("ticket",data.getTicket());
        resp.put("money", money);
        resp.put("attenderCount", attenderCount);
        resp.put("attendTimes", attendTimes);
        resp.put("probability", probability);

        if(attenderCount == 100 || attenderCount % 500 == 0){
            List<String> playerOpenIds = playerDAO.queryAllPlayersOpenId();
            for(String openId : playerOpenIds){
                Player p = playerService.getPlayer(openId);
                if(p != null){
                    p.getPlayerDataObject().getMailBox().addMail(MailConsts.MAILTYPE_LOTTERY_TOTAL_ATTENDER, String.valueOf(attenderCount), String.valueOf(money));
                }
            }
        }

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result getAttendPlayers(String openid, int pageIdx, int pageSize) {
        Map<String, Object> resp = Maps.newHashMap();
        Luckdraw currentLuckdraw = getCurrentLuckdraw();

        if (currentLuckdraw == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        List<LuckdrawRole> list = Lists.newArrayList(currentLuckdraw.getRoleMap().values());
        if (list.isEmpty()) {
            resp.put("result", Lists.newArrayListWithCapacity(0));
            return Result.valueOf(ErrorCode.OK, resp);
        }

        list.sort(new Comparator<LuckdrawRole>() {
            @Override
            public int compare(LuckdrawRole o1, LuckdrawRole o2) {
                return o2.getAttendTimes() - o1.getAttendTimes();
            }
        });

        int beginIdx = pageIdx * pageSize;

        if(beginIdx >= list.size()){
            resp.put("result", Lists.newArrayListWithCapacity(0));
            return Result.valueOf(ErrorCode.OK, resp);
        }

        int endIdx = beginIdx + pageSize;
        if (endIdx > list.size()) {
            endIdx = list.size();
        }

        List<LuckdrawRole> luckdrawRoles = list.subList(beginIdx, endIdx);
        List<LuckdrawAttenderVO> result = Lists.newArrayList();

        for(int i = 0; i < luckdrawRoles.size(); i++){
            LuckdrawAttenderVO vo = new LuckdrawAttenderVO();
            LuckdrawRole role = luckdrawRoles.get(i);

            vo.setAvatarUrl(role.getAvatarUrl());

            if(i + beginIdx < 3){
                vo.setNickName(role.getNickName());
                vo.setTickets(role.getAttendTimes());
                vo.setProbability(role.getProbability());
            }
            result.add(vo);
        }

        resp.put("result", result);
        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 获取信息
     *
     * @param openid
     * @return
     */
    public Result getLuckdrawInfo(String openid) {
        Player player = playerService.getPlayer(openid);
        if (player == null) {
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        PlayerData data = player.getPlayerDataObject();
        Luckdraw currentLuckdraw = getCurrentLuckdraw();
        Map<String, Object> resp = Maps.newHashMap();

        resp.put("money", currentLuckdraw.getMoney());
        resp.put("attenderCount", currentLuckdraw.getRoleMap().size());
        resp.put("lastOpenRewardTime", serverDataService.getServerData().getLuckdrawRewardTime());

        //没开奖，显示该玩家的参与券和中奖概率
        LuckdrawRole role = currentLuckdraw.getRoleMap().getOrDefault(openid, null);
        int attendTimes = role == null ? 0 : role.getAttendTimes();
        resp.put("attendTimes", attendTimes);

        if (attendTimes > 0) {
            //中奖概率
            int total = currentLuckdraw.getLuckdrawMap().size();
            resp.put("probability", Math.floor((float) attendTimes / total * 10000) / 10000);
        }else{
            resp.put("probability", 0);
        }

        if (currentLuckdraw.getAwardNo() != null) {
            //开奖，显示中奖号码,中奖玩家名和本玩家是否中奖
            Player awardPlayer = playerService.getPlayer(currentLuckdraw.getLuckPlayerOpenid());

            resp.put("awardNo", currentLuckdraw.getAwardNo());
            resp.put("awardName", awardPlayer.getNickName());
            resp.put("rewardFlag", openid.equals(currentLuckdraw.getLuckPlayerOpenid()));
        }
        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 获取抽奖结果列表
     *
     * @param openid
     * @return
     */
    public Result getLuckdrawHistory(String openid) {
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> history = Lists.newArrayList();

        ServerData serverData = serverDataService.getServerData();
        for (Luckdraw luckdraw : serverData.getLuckdraw().values()) {
            if (luckdraw.getLuckPlayerOpenid() != null) {
                history.add(luckdraw.toProto());
            }
        }

        resp.put("result", history);

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public void forceDoLuckdraw(){
        try {
            logger.info("luck draw >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            ServerData serverData = serverDataService.getServerData();
//            long lastRewardTime = serverData.getLuckdrawRewardTime();
//            long now = TimeUtil.getTimeNow();
//
//            if(lastRewardTime != 0 && (now - lastRewardTime) < 2 * TimeUtil.ONE_DAY - 10000){
//                return;
//            }
            long millSec = LocalDateTime.now().minusDays(1).withHour(22).withMinute(30).withSecond(0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            serverData.setLuckdrawRewardTime(millSec);

            Luckdraw currentLuckdraw = getCurrentLuckdraw();
            List<String> awardnos = new ArrayList<>(currentLuckdraw.getLuckdrawMap().keySet());
            if (awardnos.size() == 0) {
                serverDataService.saveServerData();
                return;
            }

            int luckIdx = RandomUtil.randInt(awardnos.size());
            String luckno = awardnos.get(luckIdx);
            String luckopenId = currentLuckdraw.getLuckdrawMap().get(luckno);
            LuckdrawRole role = currentLuckdraw.getRoleMap().get(luckopenId);

            currentLuckdraw.setAwardNo(luckno);
            currentLuckdraw.setLuckPlayerOpenid(role.getOpenid());
            currentLuckdraw.setNickName(role.getNickName());
            currentLuckdraw.setAvatarUrl(role.getAvatarUrl());
            currentLuckdraw.setTime(TimeUtil.getCurrentSeconds());
            currentLuckdraw.getLuckdrawMap().clear();
            currentLuckdraw.getRoleMap().clear();

            //mail
            List<String> playerOpenIds = playerDAO.queryAllPlayersOpenId();
            for(String openId : playerOpenIds){
                Player player = playerService.getPlayer(openId);
                if(player != null){
                    if(openId.equals(role.getOpenid())){
                        player.getPlayerDataObject().getMailBox().addMail(MailConsts.MAILTYPE_LOTTERY_WINNER);
                    }else{
                        player.getPlayerDataObject().getMailBox().addMail(MailConsts.MAILTYPE_LOTTERY_SHOWWINNER, luckno, role.getNickName());
                    }
                }
            }

            //动态
            serverDataService.addTrends(currentLuckdraw.getLuckPlayerOpenid(), currentLuckdraw.getNickName(), currentLuckdraw.getAvatarUrl(), TrendsConsts.LOTTERY_REWARD, currentLuckdraw.getMoney());

            //下一场
            LocalDateTime nowTime = LocalDateTime.now();
//            nowTime = nowTime.plusDays(1);
            int year = nowTime.getYear();
            int month = nowTime.getMonthValue();
            int day = nowTime.getDayOfMonth();

            String key = year + "" + month + "" + day;

            Luckdraw luckdraw = new Luckdraw();
            luckdraw.setId(key);
            luckdraw.setMoney(MONEY_ORIGINAL);
            serverData.getLuckdraw().put(key, luckdraw);
            serverData.setCurLuckdrawKey(key);

            logger.info("luck draw openid = " + role.getOpenid() + " no =" + luckno);

            serverDataService.saveServerData();
        } catch (Exception e) {
            logger.error("luck draw error", e);
        }
    }

    public void doLuckdraw() {
        try {
            logger.info("luck draw >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            ServerData serverData = serverDataService.getServerData();
            long lastRewardTime = serverData.getLuckdrawRewardTime();
            long now = TimeUtil.getTimeNow();

            if(lastRewardTime != 0 && (now - lastRewardTime) < 2 * TimeUtil.ONE_DAY - 10000){
                return;
            }
            serverData.setLuckdrawRewardTime(now);

            Luckdraw currentLuckdraw = getCurrentLuckdraw();
            List<String> awardnos = new ArrayList<>(currentLuckdraw.getLuckdrawMap().keySet());
            if (awardnos.size() == 0) {
                serverDataService.saveServerData();
                return;
            }

            int luckIdx = RandomUtil.randInt(awardnos.size());
            String luckno = awardnos.get(luckIdx);
            String luckopenId = currentLuckdraw.getLuckdrawMap().get(luckno);
            LuckdrawRole role = currentLuckdraw.getRoleMap().get(luckopenId);

            currentLuckdraw.setAwardNo(luckno);
            currentLuckdraw.setLuckPlayerOpenid(role.getOpenid());
            currentLuckdraw.setNickName(role.getNickName());
            currentLuckdraw.setAvatarUrl(role.getAvatarUrl());
            currentLuckdraw.setTime(TimeUtil.getCurrentSeconds());
            currentLuckdraw.getLuckdrawMap().clear();
            currentLuckdraw.getRoleMap().clear();

            //mail
            List<String> playerOpenIds = playerDAO.queryAllPlayersOpenId();
            for(String openId : playerOpenIds){
                Player player = playerService.getPlayer(openId);
                if(player != null){
                    if(openId.equals(role.getOpenid())){
                        player.getPlayerDataObject().getMailBox().addMail(MailConsts.MAILTYPE_LOTTERY_WINNER);
                    }else{
                        player.getPlayerDataObject().getMailBox().addMail(MailConsts.MAILTYPE_LOTTERY_SHOWWINNER, luckno, role.getNickName());
                    }
                }
            }

            //动态
            serverDataService.addTrends(currentLuckdraw.getLuckPlayerOpenid(), currentLuckdraw.getNickName(), currentLuckdraw.getAvatarUrl(), TrendsConsts.LOTTERY_REWARD, currentLuckdraw.getMoney());

            //下一场
            LocalDateTime nowTime = LocalDateTime.now();
            nowTime = nowTime.plusDays(1);
            int year = nowTime.getYear();
            int month = nowTime.getMonthValue();
            int day = nowTime.getDayOfMonth();

            String key = year + "" + month + "" + day;

            Luckdraw luckdraw = new Luckdraw();
            luckdraw.setId(key);
            luckdraw.setMoney(MONEY_ORIGINAL);
            serverData.getLuckdraw().put(key, luckdraw);
            serverData.setCurLuckdrawKey(key);

            logger.info("luck draw openid = " + role.getOpenid() + " no =" + luckno);

            serverDataService.saveServerData();
        } catch (Exception e) {
            logger.error("luck draw error", e);
        }
    }

    private void updateRolesProbability(){
        Luckdraw currentLuckdraw = this.getCurrentLuckdraw();
        if(currentLuckdraw == null){
            return;
        }

        int totalAttendTimes = currentLuckdraw.getLuckdrawMap().size();
//        logger.warn("totalTimes: " + totalAttendTimes);

        for(LuckdrawRole role : currentLuckdraw.getRoleMap().values()){
            int attendTimes = role.getAttendTimes();
            double probability = Math.floor((float) attendTimes / totalAttendTimes * 10000) / 10000;

//            logger.warn("attendTimes: " + attendTimes + ", probability: " + probability);
            role.setProbability(probability);

        }
    }
}
