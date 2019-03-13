package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.*;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class PlayerHandler {

    @Autowired
    private PlayerService playerService;

    @Command(cmd = Cmd.GET_OPENID, description = "请求OPENID")
    public Result getOpenID(String openId, OpenIDReq req) throws Exception {
        Result result = playerService.getOpenID(openId, req.getCode());
        return result;
    }


    @Command(cmd = Cmd.CREATE_ROLE, description = "请求创角色")
    public Result createRole(String openId, CreateRoleReq req) throws Exception {
        Result result = playerService.createPlayer(openId, req.getNickName(), req.getIconUrl());
        return result;
    }

    //初始登录时候
    @Command(cmd = Cmd.GET_ROLE, description = "请求角色信息")
    public Result getRole(String openId) throws Exception {
        Result result = playerService.getRole(openId);
        return result;
    }

    @Command(cmd = Cmd.UPDATE_ROLE, description = "更新角色信息")
    public Result updateRoleInfo(String openId, UpdateRoleReq req) throws Exception {
        Result result = playerService.updateRole(openId, req.getNickName(), req.getIconUrl());
        return result;
    }

    @Command(cmd = Cmd.GET_ROLE_DETAIL, description = "获取角色详细数据")
    public Result getRoleDetailInfo(String openId, String roleOpenId) throws Exception {
        Result result = playerService.getRoleDetailInfo(roleOpenId);
        return result;
    }

    @Command(cmd = Cmd.GET_RUN_DATA, description = "跑步")
    public Result getRunData(String openId,RunDataReq req) throws Exception {
        Result result = playerService.getRunData(openId,req.getEncryptedData(),req.getIv());
        return result;
    }

    @Command(cmd = Cmd.GIVE_RUN_DATA, description = "给予步数")
    public Result giveRunData(String openId, GiveRunDataReq req) throws Exception {
        Result result = playerService.giveStep(openId, req.getGiveOpenId());
        return result;
    }

    @Command(cmd = Cmd.GAIN_STEPBALL, description = "收获步数球")
    public Result gainStepBall(String openId, GainStepBallReq req) throws Exception {
        Result result = playerService.gainStepBall(openId, req.getId(), req.getType());
        return result;
    }

    @Command(cmd = Cmd.GET_STEPBALLS, description = "查询步数球")
    public Result getStepList(String openId) throws Exception {
        Result result = playerService.getStepBalls(openId);
        return result;
    }
//    @Command(cmd = Cmd.TRANSFER_GIVE_RUN_DATA, description = "兑换给予步数")
//    public Result transferGiveRunData(String openId, String giveOpenId) throws Exception {
//        Result result = playerService.transferGiveRunData(openId, giveOpenId);
//        return result;
//    }
//
//    @Command(cmd = Cmd.GET_GROUPS_INFO_NEARBY, description = "获取附近组信息")
//    public Result getGroupsInfoNearBy(String openId) throws Exception {
//        Result result = playerService.getGroupsInfoNearBy(openId);
//        return result;
//    }
}
