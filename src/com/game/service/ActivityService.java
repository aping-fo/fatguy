package com.game.service;

import com.game.data.ActivityCfg;
import com.game.domain.activity.ActivityData;
import com.game.domain.mail.MailConsts;
import com.game.domain.player.Player;
import com.game.domain.player.PlayerData;
import com.game.sdk.net.Result;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.game.util.TimeUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {
    @Autowired
    private PlayerService playerService;

    private static int[] MONEYS = {0, 1, 10, 100, 1000};

    public Result getActivityInfo(String openId){
        Player player = playerService.getPlayer(openId);

        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        ActivityData activityData = player.getPlayerDataObject().getActivityData();
        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(2);

        if(activityData == null){
            return Result.valueOf(ErrorCode.OK, resp);
        }

        resp.put("info", activityData.toProto());
        resp.put("money", getMoney(activityData.getItems(), false));

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result exchangeActivity(String openId){
        Player player = playerService.getPlayer(openId);

        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        PlayerData playerData = player.getPlayerDataObject();
        int money = this.getMoney(playerData.getActivityData().getItems(), true);

        if(money > 0){
            playerData.getMailBox().addMail(MailConsts.MAILTYPE_COLLECTION_ACTIVITY, null, String.valueOf(money));
        }

        return Result.valueOf(ErrorCode.OK);
    }

    public void checkAndResetActivityData(String openId){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        //check过期
        PlayerData playerData = player.getPlayerDataObject();
        ActivityData activityData = playerData.getActivityData();
        if(activityData != null){
            ActivityCfg cfg = ConfigData.getConfig(ActivityCfg.class, activityData.getId());
            if(now.isBefore(LocalDateTime.parse(cfg.BeginTime, TimeUtil.dateTimeFormatter)) || now.isAfter(LocalDateTime.parse(cfg.EndTime, TimeUtil.dateTimeFormatter))){
                activityData = null;
            }
        }

        if(activityData == null){
            Collection<Object> activityCfgs = ConfigData.getConfigs(ActivityCfg.class);
            for(Object obj : activityCfgs){
                ActivityCfg cfg = (ActivityCfg)obj;
                if(now.isBefore(LocalDateTime.parse(cfg.BeginTime, TimeUtil.dateTimeFormatter)) || now.isAfter(LocalDateTime.parse(cfg.EndTime, TimeUtil.dateTimeFormatter))){
                    continue;
                }

                activityData = new ActivityData();
                activityData.setId(cfg.id);
                playerData.setActivityData(activityData);
                break;
            }
        }
    }

    public void addItems(String openId, Map<Integer, Integer> items){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return;
        }

        ActivityData activityData = player.getPlayerDataObject().getActivityData();
        if(activityData == null){
            return;
        }

        for(Integer itemId : items.keySet()){
            int count = items.get(itemId);
            count += activityData.getItems().getOrDefault(itemId, 0);

            activityData.getItems().put(itemId, count);
        }

        playerService.saveOrUpdate(player, false);
    }


    public int getMoney(Map<Integer, Integer> items, boolean needRemove){
        Map<Integer, Integer> collectionMap = Maps.newHashMap();
        int totalMoney = 0;

        for(Integer collectionId : ConfigData.collectionDatas.keySet()){
            List<Integer> list = ConfigData.collectionDatas.get(collectionId);
            int count = Integer.MAX_VALUE;
            for(Integer id : list){
                count = Math.min(count, items.getOrDefault(id, 0));
                if(count == 0){
                    break;
                }
            }

            collectionMap.put(collectionId, count);
        }

        for(int i = 4; i >= 1; i--){
            int collectionCount = 0;
            int countInCollection = Integer.MAX_VALUE;

            for(Integer collectionId : collectionMap.keySet()) {
                int count = collectionMap.get(collectionId);
                if(count > 0){
                    collectionCount++;
                    countInCollection = Math.min(count, countInCollection);
                }
            }

            if(collectionCount == i){
                //凑够字数
                totalMoney += MONEYS[collectionCount] * countInCollection;

                for(Integer collectionId : collectionMap.keySet()) {
                    if(needRemove && collectionMap.get(collectionId) > 0){
                        List<Integer> list = ConfigData.collectionDatas.get(collectionId);
                        for(Integer id : list){
                            int newCount = items.getOrDefault(id, 0) - countInCollection;
                            items.put(id, Math.max(0, newCount));
                        }
                    }
                    collectionMap.put(collectionId, Math.max(0, collectionMap.get(collectionId) - countInCollection));
                }
            }
        }
        return totalMoney;
    }
}
