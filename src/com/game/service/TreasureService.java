package com.game.service;

import com.game.dao.PlayerDAO;
import com.game.data.ToolCfg;
import com.game.data.TreasureCfg;
import com.game.domain.bag.ItemConsts;
import com.game.domain.mail.MailConsts;
import com.game.domain.player.Player;
import com.game.domain.player.TrendsConsts;
import com.game.domain.task.TaskConsts;
import com.game.domain.treasure.Treasure;
import com.game.domain.treasure.TreasureConsts;
import com.game.domain.treasure.TreasureWarehouse;
import com.game.sdk.net.Result;
import com.game.sdk.proto.TreasureBoxRewardResp;
import com.game.sdk.proto.vo.ToolVO;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.RandomUtil;
import com.game.util.TimeUtil;
import com.game.util.TimerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by lucky on 2019/3/13.
 * 背包道具管理
 */
@Service
public class TreasureService extends AbstractService  {
    private static Logger logger = Logger.getLogger(TreasureService.class);
    private static final int AUTO_OPEN_BOX_INTERVAL = 600;  //10分钟

    @Autowired
    private PlayerService playerService;
    @Autowired
    private BagService bagService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ActivityService activityService;

    @Override
    public void onStart(){
        //1分钟更新玩家宝箱信息
//        timerService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    updateAllPlayerTreasure();
//                } catch (Exception e) {
//                    logger.error("match schedule error", e);
//                }
//            }
//        }, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * 获取宝箱列表
     *
     * @param openid
     * @return
     */
    public Result getTreasureBoxs(String openid) {
        Player player = playerService.getPlayer(openid);

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> boxs = Lists.newArrayList();
        TreasureWarehouse warehouse = player.getPlayerDataObject().getTreasureWarehouse();
        warehouse.setAward(false);

        for (Treasure box : warehouse.getTreasureMap().values()) {
            Map<String, Object> boxData = Maps.newHashMapWithExpectedSize(5);
            int remainTime = this.getTreasureBoxAwardRemainTime(box);
            if(remainTime == 0){
                box.setAwardFlag(true);
                warehouse.setAward(true);
            }
            boxData.put("id", box.getId());
            boxData.put("configId", box.getConfigId());
            boxData.put("cdTime", remainTime);
            boxData.put("isAward", box.isAwardFlag());
            boxData.put("isOpen", box.getOpenTime() != 0);

            boxs.add(boxData);
        }
        resp.put("boxs", boxs);

        playerService.saveOrUpdate(player, false);

        return Result.valueOf(ErrorCode.OK, resp);
    }


    /**
     * 广告加速
     *
     * @param openid
     * @param boxid
     * @return
     */
    public Result adsSpeedup(String openid, int boxid) {
        Player player = playerService.getPlayer(openid);
        TreasureWarehouse warehouse = player.getPlayerDataObject().getTreasureWarehouse();
        Treasure treasure = warehouse.getTreasureMap().get(boxid);

        if (treasure == null) {
            return Result.valueOf(ErrorCode.BOX_NOT_EXIST);
        }

        if(treasure.isAwardFlag()){
            return Result.valueOf(ErrorCode.BOX_ALREADY_REWARD);
        }

        TreasureCfg config = ConfigData.getConfig(TreasureCfg.class, treasure.getConfigId());
        treasure.setSpeedupTime(Math.min(treasure.getCdTime(), treasure.getSpeedupTime() + config.adsTime));

        int remainTime = this.getTreasureBoxAwardRemainTime(treasure);
        if(remainTime == 0){
            treasure.setAwardFlag(true);
            warehouse.setAward(true);
        }

        playerService.saveOrUpdate(player, false);

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(4);
        resp.put("id", treasure.getId());
        resp.put("configId", treasure.getConfigId());
        resp.put("cdTime", remainTime);
        resp.put("isAward", treasure.isAwardFlag());

        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 打开领奖
     *
     * @param openid
     * @param boxid
     * @return
     */
    public Result getTreasureBoxReward(String openid, String targetId, int boxid) {
        Player player = playerService.getPlayer(openid);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        Player targetPlayer = playerService.getPlayer(targetId);
        if(targetPlayer == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        TreasureWarehouse warehouse = targetPlayer.getPlayerDataObject().getTreasureWarehouse();
        Treasure treasure = warehouse.getTreasureMap().get(boxid);

        if (treasure == null) {
            return Result.valueOf(ErrorCode.BOX_NOT_EXIST);
        }

        int curTime = TimeUtil.getCurrentSeconds();
        int remainTime = this.getTreasureBoxAwardRemainTime(treasure);
        if(remainTime == 0){
            treasure.setAwardFlag(true);
        }

        if(!treasure.isAwardFlag()){
            return Result.valueOf(ErrorCode.BOX_CANNOT_REWARD);
        }

        //集字items
        Map<Integer, Integer> items = Maps.newHashMap();

        TreasureCfg config = ConfigData.getConfig(TreasureCfg.class, treasure.getConfigId());
        TreasureBoxRewardResp resp = new TreasureBoxRewardResp();
        for (int[] reward : config.rewards) {
            int type = reward[0];

            if (type == 2) {
                int min = reward[1];
                int max = reward[2];
                int ticket = RandomUtil.nextGaussian(min, max);
                player.getPlayerDataObject().setTicket(player.getPlayerDataObject().getTicket() + ticket);
                resp.setTicket(ticket);
            }else if (type == 3) {//随机道具
                if(treasure.isNewPlayerTreasure()){
                    int count = 1;

                    bagService.addItem(openid, ItemConsts.ITEM_TYPE_RUN, count);

                    ToolVO toolVo = new ToolVO();
                    toolVo.setConfigId(ItemConsts.ITEM_TYPE_RUN);
                    toolVo.setCount(count);
                    resp.getTools().add(toolVo);
                }else{
                    int count = reward[1];
                    List<ToolCfg> toolCfgs = ConfigData.toolCfgList;
                    Map<Integer, Integer> map = Maps.newHashMapWithExpectedSize(toolCfgs.size());

                    for(int i = 0; i < count; i++){
                        ToolCfg cfg = toolCfgs.get(RandomUtil.randInt(toolCfgs.size()));
                        int num = map.getOrDefault(cfg.id, 0) + 1;

                        map.put(cfg.id, num);
                    }

                    for(Integer cfgId : map.keySet()){
                        bagService.addItem(openid, cfgId, map.get(cfgId));

                        ToolVO toolVo = new ToolVO();
                        toolVo.setConfigId(cfgId);
                        toolVo.setCount(map.get(cfgId));
                        resp.getTools().add(toolVo);
                    }
                }
            }else if(type == 4){//集字
                int itemId = reward[1];
                int ratio = reward[2];

                if(RandomUtil.randInt(100) < ratio){
                    //成功获得
                    items.put(itemId, 1);
                }
            }
        }

        activityService.addItems(openid, items);
        for(Integer cfgId : items.keySet()){
            ToolVO toolVo = new ToolVO();
            toolVo.setConfigId(cfgId);
            toolVo.setCount(items.get(cfgId));
            resp.getActivityItems().add(toolVo);
        }

        //奖励完删除
        warehouse.getTreasureMap().remove(boxid);

        //更新上次开宝箱时间
        warehouse.setLastOpenTreasureTime(curTime);

        taskService.finishTask(openid, TaskConsts.TASK_TYPE_GET_TREASUREBOX_REWARD, 1);

        if(!openid.equals(targetId)){
            //偷取
            playerService.addInformation(targetId, openid, TrendsConsts.STEAL_TREASUREBOX);

            targetPlayer.getPlayerDataObject().getMailBox().addMail(MailConsts.MAILTYPE_STEAL_TREASUREBOX, player.getNickName(), TimeUtil.dateFormatter.format(new Date()), player.getAvatarUrl());

            player.setStealTimes(player.getStealTimes() + 1);

            playerService.saveOrUpdate(targetPlayer, false);
            playerService.saveOrUpdate(player, true);
        }else {
            playerService.saveOrUpdate(player, false);
        }

        return Result.valueOf(ErrorCode.OK, resp);
    }

    /**
     * 打开宝箱倒计时
     *
     * @param openid
     * @param boxid
     * @return
     */
    public Result openTreasureBox(String openid, int boxid) {
        Player player = playerService.getPlayer(openid);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        TreasureWarehouse warehouse = player.getPlayerDataObject().getTreasureWarehouse();
        if(warehouse.getOpenTreasure() != null){
            return Result.valueOf(ErrorCode.BOX_ALREADY_OPEN);
        }

        Treasure treasure = warehouse.getTreasureMap().get(boxid);
        if (treasure == null) {
            return Result.valueOf(ErrorCode.BOX_NOT_EXIST);
        }

        if(treasure.isAwardFlag()){
            return Result.valueOf(ErrorCode.BOX_CANT_OPEN);
        }

        int lastOpenTime = warehouse.getLastOpenTreasureTime();
        treasure.setOpenTime(lastOpenTime);

        playerService.saveOrUpdate(player, false);

        return Result.valueOf(ErrorCode.OK);
    }

    public void addTreasureBox(String openId, int configId){
        Player player = playerService.getPlayer(openId);

        if(player == null){
            return;
        }

        TreasureWarehouse warehouse = player.getPlayerDataObject().getTreasureWarehouse();

        warehouse.addTreasureBox(configId, 1);
    }


    public void addNewPlayerTreasureBox(String openId){
        Player player = playerService.getPlayer(openId);

        if(player == null){
            return;
        }

        TreasureWarehouse warehouse = player.getPlayerDataObject().getTreasureWarehouse();
        Treasure treasure = warehouse.addTreasureBox(TreasureConsts.TREASURE_TYPE_COPPER, 1);

        treasure.setCdTime(1);
        treasure.setNewPlayerTreasure(true);
    }

    //更新所有玩家宝箱信息
    public void updatePlayerTreasure(String openId){
        Player player = playerService.getPlayer(openId);
        TreasureWarehouse warehouse = player.getPlayerDataObject().getTreasureWarehouse();

        warehouse.setAward(false);

        for (Treasure box : warehouse.getTreasureMap().values()) {
            int remainTime = this.getTreasureBoxAwardRemainTime(box);
            if(remainTime == 0){
                box.setAwardFlag(true);
                warehouse.setAward(true);
            }
        }

        playerService.saveOrUpdate(player, false);
    }

    //获取宝箱领奖剩余时间
    private int getTreasureBoxAwardRemainTime(Treasure treasure){
        int curTime = TimeUtil.getCurrentSeconds();
        int remainTime = treasure.getCdTime() - treasure.getSpeedupTime();

        if(treasure.getOpenTime() != 0){
            remainTime = Math.max(0, remainTime - (curTime - treasure.getOpenTime()));
        }
        return remainTime;
    }
}
