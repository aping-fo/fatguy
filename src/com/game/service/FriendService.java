package com.game.service;

import com.game.data.TrendCfg;
import com.game.domain.ServerData;
import com.game.domain.friend.Friend;
import com.game.domain.player.Player;
import com.game.domain.player.TrendsConsts;
import com.game.domain.task.TaskConsts;
import com.game.sdk.net.Result;
import com.game.sdk.proto.vo.FriendVO;
import com.game.sdk.utils.ErrorCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lucky on 2019/3/16.
 */
@Service
public class FriendService {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TreasureService treasureService;

    /**
     * 如果量大，需要分页获取
     * 获取好友列表
     *
     * @param openid
     * @return
     */
    public Result getFriends(String openid) {
        Player player = playerService.getPlayer(openid);
        List<String> friendList = player.getPlayerDataObject().getFriendList();

        List<FriendVO> friendVOS = Lists.newArrayListWithCapacity(friendList.size());
        for (String fOpenId : friendList) {
            Player fPlayer = playerService.getPlayer(fOpenId);

            if(fPlayer != null){
                treasureService.updatePlayerTreasure(fOpenId);

                FriendVO vo = new FriendVO();
                vo.setOpenId(fOpenId);
                vo.setNickName(fPlayer.getNickName());
                vo.setAvatarUrl(fPlayer.getAvatarUrl());
                vo.setStealTimes(fPlayer.getStealTimes());
                vo.setAward(fPlayer.getPlayerDataObject().getTreasureWarehouse().isAward());

                friendVOS.add(vo);
            }
        }

        Map<String, Object> resp = Maps.newHashMap();
        resp.put("friends", friendVOS);
        return Result.valueOf(ErrorCode.OK, resp);
    }


    /**
     * 删除好友
     *
     * @param openid
     * @param targetOpenid
     * @return
     */
    public Result delFriends(String openid, String targetOpenid) {
        Player player = playerService.getPlayer(openid);
        Player targetPlayer = playerService.getPlayer(targetOpenid);

        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        if(openid.equals(targetOpenid)){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        if (targetPlayer == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST);
        }

        //相互删除
        List<String> friendList = player.getPlayerDataObject().getFriendList();

        if(!friendList.contains(targetOpenid)){
            return Result.valueOf(ErrorCode.NOT_FRIEND);
        }

        friendList.remove(targetOpenid);
        friendList = targetPlayer.getPlayerDataObject().getFriendList();
        friendList.remove(openid);

        return Result.valueOf(ErrorCode.OK);
    }

    /**
     * 删除好友
     *
     * @param openid
     * @param targetOpenid
     * @return
     */
    public Result addFriends(String openid, String targetOpenid) {
        Player player = playerService.getPlayer(openid);
        List<String> friendList = player.getPlayerDataObject().getFriendList();
        Player targetPlayer = playerService.getPlayer(targetOpenid);

        if(openid.equals(targetOpenid)){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        if (targetPlayer == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST);
        }

        if(player.getPlayerDataObject().getFriendRequestList().contains(targetOpenid)){
            player.getPlayerDataObject().getFriendRequestList().remove(targetOpenid);
        }
        if(targetPlayer.getPlayerDataObject().getFriendRequestList().contains(openid)){
            targetPlayer.getPlayerDataObject().getFriendRequestList().remove(openid);
        }

        if(friendList.contains(targetOpenid)){
            return Result.valueOf(ErrorCode.FRIEND_ALREADY_IS);
        }
        friendList.add(targetOpenid);

        //相互添加
        friendList = targetPlayer.getPlayerDataObject().getFriendList();
        if(friendList.contains(openid)){
            return Result.valueOf(ErrorCode.FRIEND_ALREADY_IS);
        }
        friendList.add(openid);

        taskService.finishTask(openid, TaskConsts.TASK_TYPE_ADD_FRIEND, 1);
        taskService.finishTask(targetOpenid, TaskConsts.TASK_TYPE_ADD_FRIEND, 1);

        playerService.saveOrUpdate(player, false);
        playerService.saveOrUpdate(targetPlayer, false);

        playerService.addInformation(openid, targetOpenid, TrendsConsts.ADD_FRIEND);
        playerService.addInformation(targetOpenid, openid, TrendsConsts.ADD_FRIEND);

        return Result.valueOf(ErrorCode.OK);
    }

    public Result addFriendRequest(String openId, String targetOpenId){
        Player targetPlayer = playerService.getPlayer(targetOpenId);
        if(targetPlayer == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        if(targetPlayer.getPlayerDataObject().getFriendList().contains(openId)){
            return Result.valueOf(ErrorCode.FRIEND_ALREADY_IS);
        }

        if(!targetPlayer.getPlayerDataObject().getFriendRequestList().contains(openId)){
            targetPlayer.getPlayerDataObject().getFriendRequestList().add(openId);
        }

        return Result.valueOf(ErrorCode.OK);
    }

    public Result getAddFriendRequestList(String openId){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        List<String> friendRequestList = player.getPlayerDataObject().getFriendRequestList();
        List<FriendVO> friendVOS = Lists.newArrayListWithCapacity(friendRequestList.size());

        for (String fOpenId : friendRequestList) {
            Player fPlayer = playerService.getPlayer(fOpenId);

            if(fPlayer != null){
                FriendVO vo = new FriendVO();
                vo.setOpenId(fOpenId);
                vo.setNickName(fPlayer.getNickName());
                vo.setAvatarUrl(fPlayer.getAvatarUrl());
                vo.setStealTimes(fPlayer.getStealTimes());

                friendVOS.add(vo);
            }
        }

        Map<String, Object> resp = Maps.newHashMap();
        resp.put("requestFriends", friendVOS);
        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result denyAddFriend(String openId, String targetOpenId) {
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        Player targetPlayer = playerService.getPlayer(targetOpenId);
        if(targetPlayer == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        if(!player.getPlayerDataObject().getFriendRequestList().contains(targetOpenId)){
            return Result.valueOf(ErrorCode.OK);
        }

        player.getPlayerDataObject().getFriendRequestList().remove(targetOpenId);

        playerService.addInformation(targetOpenId, openId, TrendsConsts.FRIEND_REQUEST_DENY);

        return Result.valueOf(ErrorCode.OK);
    }
}
