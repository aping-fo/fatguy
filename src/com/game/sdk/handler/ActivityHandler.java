package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.service.ActivityService;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class ActivityHandler {
    @Autowired
    private ActivityService activityService;

    @Command(cmd = Cmd.GET_ACTIVITY_INFO, description = "获取活动信息")
    public Result getActivityInfo(String openId) throws Exception {
        return activityService.getActivityInfo(openId);
    }

    @Command(cmd = Cmd.EXCHANGE_ACTIVITY, description = "活动兑换")
    public Result exchangeActivity(String openId) throws Exception {
        return activityService.exchangeActivity(openId);
    }

}
