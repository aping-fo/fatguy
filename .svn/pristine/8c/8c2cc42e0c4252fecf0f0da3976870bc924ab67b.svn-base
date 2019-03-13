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

    @Command(cmd = Cmd.GROUP_INFO, description = "组织查询")
    public Result getGroupInfo(String openId) throws Exception {
        Result result = playerService.getGroupInfo(openId);
        return result;
    }

    @Command(cmd = Cmd.GROUP_RECORD, description = "组织记录查询")
    public Result getGroupRecord(String openId) throws Exception {
        Result result = playerService.getGroupRecord(openId);
        return result;
    }

    @Command(cmd = Cmd.GROUP_ADD_MEMBER, description = "组织成员加入")
    public Result groupAddMember(String openId, GroupMemberAddReq req) throws Exception {
        Result result = playerService.groupMemberAdd(openId, req.getId());
        return result;
    }

    @Command(cmd = Cmd.GROUP_REMOVE_MEMBER, description = "组织移除成员")
    public Result groupRemoveMember(String openId, GroupMemberRemoveReq req) throws Exception {
        Result result = playerService.groupMemberRemove(openId, req.getMemberOpenId());
        return result;
    }

    @Command(cmd = Cmd.GROUP_CHANGENAME, description = "修改组织名字")
    public Result groupChangeName(String openId, GroupChangeNameReq req) throws Exception {
        Result result = playerService.groupChangeName(openId, req.getName());
        return result;
    }

    @Command(cmd = Cmd.GROUP_CHANGEICON, description = "修改组织头像")
    public Result groupChangeIcon(String openId, GroupChangeIconReq req) throws Exception {
        Result result = playerService.groupChangeIcon(openId, req.getIconUrl());
        return result;
    }


    @Command(cmd = Cmd.GROUP_RANK, description = "排行榜")
    public Result getGroupRank(String openId) throws Exception {
        Result result = playerService.getGroupRank(openId);
        return result;
    }

    @Command(cmd = Cmd.GROUP_EXIT, description = "退出")
    public Result groupMemberExit(String openId) throws Exception {
        Result result = playerService.groupMemberExit(openId);
        return result;
    }

    @Command(cmd = Cmd.GROUP_DIS, description = "解散")
    public Result dismissGroup(String openId) throws Exception {
        Result result = playerService.dismissGroup(openId, true);
        return result;
    }

    @Command(cmd = Cmd.GROUP_ENCOURAGE, description = "鼓励")
    public Result encourageGroupMemeber(String openId, String otherNickName) throws Exception {
        Result result = playerService.encourageGroupMemeber(openId, otherNickName);
        return result;
    }

    @Command(cmd = Cmd.GROUP_WARN, description = "警告")
    public Result warnGroupMemeber(String openId, String otherNickName) throws Exception {
        Result result = playerService.warnGroupMemeber(openId, otherNickName);
        return result;
    }

    @Command(cmd = Cmd.SIGN, description = "签到")
    public Result sign(String openId) throws Exception {
        Result result = playerService.sign(openId);
        return result;
    }

    @Command(cmd = Cmd.GET_RUN_DATA, description = "跑步")
    public Result getRunData(String openId,RunDataReq req) throws Exception {
        Result result = playerService.getRunData(openId,req.getEncryptedData(),req.getIv());
        return result;
    }

    @Command(cmd = Cmd.TRANSFER_RUN_DATA, description = "跑步")
    public Result transferRunData(String openId, TransferStepReq req) throws Exception {
        Result result = playerService.transStep(openId, req.getStep(), req.getType());
        return result;
    }

    @Command(cmd = Cmd.GIVE_RUN_DATA, description = "给予步数")
    public Result giveRunData(String openId, GiveRunDataReq req) throws Exception {
        Result result = playerService.giveStep(openId, req.getGiveOpenId(), req.getStep());
        return result;
    }

    @Command(cmd = Cmd.GET_GIVE_RUN_DATA, description = "查询给予步数")
    public Result giveRunData(String openId) throws Exception {
        Result result = playerService.getGiveStep(openId);
        return result;
    }

    @Command(cmd = Cmd.TRANSFER_GIVE_RUN_DATA, description = "兑换给予步数")
    public Result transferGiveRunData(String openId, String giveOpenId) throws Exception {
        Result result = playerService.transferGiveRunData(openId, giveOpenId);
        return result;
    }

    @Command(cmd = Cmd.GET_GROUPS_INFO_NEARBY, description = "获取附近组信息")
    public Result getGroupsInfoNearBy(String openId) throws Exception {
        Result result = playerService.getGroupsInfoNearBy(openId);
        return result;
    }
}
