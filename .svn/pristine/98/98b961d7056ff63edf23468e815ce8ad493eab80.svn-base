package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Result;
import com.game.service.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import com.game.sdk.net.Cmd;

@Handler
public class BroadcastHandler {
    @Autowired
    private BroadcastService service;

    @Command(cmd = Cmd.GET_BROADCAST, description = "获取广播信息")
    public Result getBroadcast(String openId){
        return service.getBroadcast();
    }
}
