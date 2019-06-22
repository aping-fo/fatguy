package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.GetLuckdrawPlayerReq;
import com.game.service.LuckdrawService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class LuckdrawHandler {
    @Autowired
    private LuckdrawService luckdrawService;

    @Command(cmd = Cmd.LUCK_DRAW_RECORD, description = "获取抽奖记录")
    public Result getLuckdrawHistory(String openId) throws Exception {
        Result result = luckdrawService.getLuckdrawHistory(openId);
        return result;
    }

    @Command(cmd = Cmd.LUCK_DRAW_ATTEND, description = "参与抽奖")
    public Result attendLuckdraw(String openid, int ticket) throws Exception {
        Result result = luckdrawService.attendLuckdraw(openid, ticket);
        return result;
    }

    @Command(cmd = Cmd.LUCK_DRAW_INFO, description = "当前抽奖")
    public Result getLuckdrawInfo(String openid) throws Exception {
        Result result = luckdrawService.getLuckdrawInfo(openid);
        return result;
    }

    @Command(cmd = Cmd.GET_ATTEND_LUCKDRAW_ROLES, description = "获取当前参与者信息")
    public Result getAttendPlayers(String openid, GetLuckdrawPlayerReq req) throws Exception {
        Result result = luckdrawService.getAttendPlayers(openid, req.getPageIdx(), req.getPageSize());
        return result;
    }
}
