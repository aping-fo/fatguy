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
        Result result = playerService.createPlayer(openId, req.getNickName(), req.getAvatarUrl());
        return result;
    }

    //初始登录时候
    @Command(cmd = Cmd.LOGIN, description = "登陆")
    public Result login(String openId, LoginReq req) throws Exception {
        Result result = playerService.login(openId, req.getNickName(), req.getAvatarUrl());
        return result;
    }

    @Command(cmd = Cmd.GET_ROLE_DETAIL, description = "获取角色详细数据")
    public Result getRoleDetailInfo(String openId, String roleOpenId) throws Exception {
        Result result = playerService.getRoleDetailInfo(openId, roleOpenId);
        return result;
    }

    @Command(cmd = Cmd.VISIT, description = "拜访")
    public Result visit(String openId, String roleOpenId) throws Exception {
        Result result = playerService.visit(openId, roleOpenId);
        return result;
    }

    @Command(cmd = Cmd.GENERATE_TICKETBALL, description = "生成奖券球")
    public Result generateTicketBall(String openId, float ticket) throws Exception {
        Result result = playerService.generateTicketBallFromClient(openId, ticket);
        return result;
    }

    @Command(cmd = Cmd.GIVE_TICKET, description = "给予奖券")
    public Result giveTicket(String openId, GiveRunDataReq req) throws Exception {
        Result result = playerService.giveTicket(openId, req.getGiveOpenId());
        return result;
    }

    @Command(cmd = Cmd.GAIN_TICKETBALL, description = "收获奖券球")
    public Result gainTicketBall(String openId, GainStepBallReq req) throws Exception {
        Result result = playerService.gainTicketBall(openId, req.getTargetOpenId(), req.getId(), req.getType());
        return result;
    }

    @Command(cmd = Cmd.GET_TICKETBALLS, description = "查询奖券球")
    public Result getTicketList(String openId, String targetOpenId) throws Exception {
        Result result = playerService.getTicketBalls(targetOpenId);
        return result;
    }

    @Command(cmd = Cmd.CANCEL_BUFF, description = "取消buff")
    public Result cancelBuff(String openId, CancelBuffReq req) throws Exception {
        Result result = playerService.cancelBuff(openId, req.getId());
        return result;
    }

    @Command(cmd = Cmd.GET_TRENDS, description = "获取动态")
    public Result getTrends(String openId, String targetOpenId) throws Exception {
        Result result = playerService.getTrends(targetOpenId);
        return result;
    }

    @Command(cmd = Cmd.GET_VISITABLE_ROLES_INFO, description = "获取可访问人头像信息")
    public Result getVisitableRoleInfo(String openId) throws Exception {
        Result result = playerService.getVisitableRoleInfo(openId);
        return result;
    }
}
