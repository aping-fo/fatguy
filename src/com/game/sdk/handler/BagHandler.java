package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.UseItemReq;
import com.game.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class BagHandler {
    @Autowired
    private BagService bagService;

    @Command(cmd = Cmd.USE_ITEM, description = "使用道具")
    public Result useItem(String openId, UseItemReq req) throws Exception {
        Result result = bagService.useItem(openId, req.getItemId(), req.getCount(),req.getOpenid());
        return result;
    }

    @Command(cmd = Cmd.GET_BAG, description = "请求背包数据")
    public Result getBag(String openId) throws Exception {
        Result result = bagService.getBag(openId);
        return result;
    }
}
