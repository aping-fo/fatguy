package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.*;
import com.game.service.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class QuestHandler {

    @Autowired
    private ChickenQuestService chickenQuestService;

    @Command(cmd = Cmd.CHICKEN_START_MATCH, description = "吃鸡模式开始匹配")
    public Result chickenStartMatch(String openId, ChickenCreateRoomReq req) throws Exception {
        return chickenQuestService.startMatch(openId, req.getMode());
    }

    @Command(cmd = Cmd.CHICKEN_END_MATCH, description = "吃鸡模式取消匹配")
    public Result chickenEndMatch(String openId) throws Exception {
        return chickenQuestService.endMatch(openId);
    }

    @Command(cmd = Cmd.CHICKEN_CREATE_ROOM, description = "吃鸡模式新建房间")
    public Result chickenCreateRoom(String openId, ChickenCreateRoomReq req) throws Exception {
        return chickenQuestService.createRoom(openId, req.getMode());
    }

    @Command(cmd = Cmd.CHICKEN_JOIN_ROOM, description = "吃鸡模式加入房间")
    public Result chickenJoinRoom(String openId, ChickenJoinRoomReq req) throws Exception {
        return chickenQuestService.joinRoom(openId, req.getRoomId());
    }

    @Command(cmd = Cmd.CHICKEN_GET_ROOM_STATUS, description = "吃鸡模式查询房间状态")
    public Result chickenGetRoomStatus(String openId) throws Exception {
        return chickenQuestService.getRoomStatus(openId);
    }

    @Command(cmd = Cmd.CHICKEN_EXIT_ROOM, description = "吃鸡模式退出房间")
    public Result chickenExitRoom(String openId) throws Exception {
        return chickenQuestService.exitRoom(openId);
    }

    @Command(cmd = Cmd.CHICKEN_GET_QUESTION, description = "请求题目")
    public Result chickenGetQuestion(String openId) throws Exception {
        return chickenQuestService.getQuestion(openId);
    }

    @Command(cmd = Cmd.CHICKEN_GET_PLAYING_SITUATION, description = "请求赛场动态（轮询)")
    public Result chickenGetPlayingSituation(String openId) throws Exception {
        return chickenQuestService.getPlayingSituation(openId);
    }

    @Command(cmd = Cmd.CHICKEN_SUBMIT_ANSWER, description = "提交答案")
    public Result chickenSubmitAnswer(String openId, ChickenSubmitAnswerReq req) throws Exception {
        return chickenQuestService.submitAnswer(openId, req);
    }

    @Command(cmd = Cmd.CHICKEN_GET_RESULT, description = "请求结果（答案）")
    public Result chickenGetResult(String openId) throws Exception {
        return chickenQuestService.chickenGetResult(openId);
    }

    @Command(cmd = Cmd.CHICEKN_SET_GAME_START, description = "立即开始")
    public Result startNow(String openId) throws Exception {
        return chickenQuestService.SetGameStart(openId);
    }
}
