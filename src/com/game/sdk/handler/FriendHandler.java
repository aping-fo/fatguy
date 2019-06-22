package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.FriendOptReq;
import com.game.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class FriendHandler {
    @Autowired
    private FriendService friendService;

    @Command(cmd = Cmd.FRIEND_LIST, description = "获取好友列表")
    public Result getFriends(String openId) throws Exception {
        Result result = friendService.getFriends(openId);
        return result;
    }

    @Command(cmd = Cmd.FRIEND_ADD, description = "添加好友")
    public Result addFriends(String openId, FriendOptReq req) throws Exception {
        Result result = friendService.addFriends(openId, req.getOpenid());
        return result;
    }

    @Command(cmd = Cmd.FRIEND_DEL, description = "删除好友")
    public Result delFriends(String openId, FriendOptReq req) throws Exception {
        Result result = friendService.delFriends(openId, req.getOpenid());
        return result;
    }

    @Command(cmd = Cmd.FRIEND_ADD_REQUEST, description = "加好友请求")
    public Result addFriendRequest(String openId, FriendOptReq req) throws Exception {
        Result result = friendService.addFriendRequest(openId, req.getOpenid());
        return result;
    }

    @Command(cmd = Cmd.FRIEND_ADD_REQUEST_LIST, description = "加好友请求列表")
    public Result getAddFriendRequestList(String openId) throws Exception {
        Result result = friendService.getAddFriendRequestList(openId);
        return result;
    }

    @Command(cmd = Cmd.FRIEND_ADD_DENY, description = "拒绝加好友")
    public Result denyAddFriend(String openId, FriendOptReq req) throws Exception {
        Result result = friendService.denyAddFriend(openId, req.getOpenid());
        return result;
    }
}
