package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.BuyReq;
import com.game.sdk.proto.IntegrationMallConsumeReq;
import com.game.sdk.proto.ParticipantCountReq;
import com.game.service.MallService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class MallHandler {
    @Autowired
    private MallService mallService;

    @Command(cmd = Cmd.IntegrationMall_ITEMS, description = "请求积分商城物品")
    public Result get(String openId) throws Exception {
        Result result = mallService.getIntegrationMallitems(openId);
        return result;
    }

    @Command(cmd = Cmd.IntegrationMall_CONSUME, description = "积分商城兑换")
    public Result consume(String openId, IntegrationMallConsumeReq req) throws Exception {
        Result result = mallService.consume(openId, req.getName(), req.getPhone(), req.getAddress(), req.getItemId());
        return result;
    }

    @Command(cmd = Cmd.BUY, description = "参与购买")
    public Result buy(String openId, BuyReq req) throws Exception {
        Result result = mallService.buyItem(openId, req.getId());
        return result;
    }

    @Command(cmd = Cmd.GET_REWEARDER, description = "获取中奖者信息")
    public Result getReawrder(String openId) throws Exception {
        Result result = mallService.getRewarder(openId);
        return result;
    }

    @Command(cmd = Cmd.GET_PARTICIPANT_COUNT, description = "获取参与人数")
    public Result getParticipantCount(String openId, ParticipantCountReq req) throws Exception {
        Result result = mallService.getMallParticipantCount(openId, req.getId());
        return result;
    }
}
