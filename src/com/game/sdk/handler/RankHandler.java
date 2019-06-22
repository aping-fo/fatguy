package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.OpenIDReq;
import com.game.service.FriendService;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class RankHandler {

    @Autowired
    private PlayerService playerService;

    @Command(cmd = Cmd.RANK_WORLD, description = "世界排行榜")
    public Result getWorldRank(String openId) throws Exception {
        Result result = playerService.getWorldRank(openId);
        return result;
    }
}
