package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.TreasureBoxReq;
import com.game.service.TreasureService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class TreasureBoxHandler {
    @Autowired
    private TreasureService treasureService;

    @Command(cmd = Cmd.GET_TREASURE_BOXS, description = "获取宝箱列表")
    public Result getTreasureBoxs(String openId, String targetOpenId) throws Exception {
        Result result = treasureService.getTreasureBoxs(targetOpenId);
        return result;
    }

    @Command(cmd = Cmd.TREASURE_BOX_SPEEDUP, description = "宝箱加速")
    public Result adsSpeedup(String openId, TreasureBoxReq req) throws Exception {
        Result result = treasureService.adsSpeedup(req.getTargetOpenId(), req.getId());
        return result;
    }

    @Command(cmd = Cmd.TREASURE_BOX_REWARD, description = "宝箱领奖")
    public Result getTreasureBoxReward(String openId, TreasureBoxReq req) throws Exception {
        Result result = treasureService.getTreasureBoxReward(openId, req.getTargetOpenId(), req.getId());
        return result;
    }

    @Command(cmd = Cmd.TREASURE_BOX_OPEN, description = "打开宝箱倒计时")
    public Result openTreasureBox(String openId, TreasureBoxReq req) throws Exception {
        Result result = treasureService.openTreasureBox(req.getTargetOpenId(), req.getId());
        return result;
    }
}
