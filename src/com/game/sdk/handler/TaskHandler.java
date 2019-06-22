package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.FinishTaskReq;
import com.game.sdk.proto.GetTaskRewardReq;
import com.game.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class TaskHandler {
    @Autowired
    private TaskService taskService;

    @Command(cmd = Cmd.GET_TASK_LIST, description = "获取任务列表")
    public Result getTaskList(String openId) throws Exception {
        Result result = taskService.getTaskList(openId);
        return result;
    }

    @Command(cmd = Cmd.GET_TASK_REWARD, description = "获取任务列表")
    public Result getTaskReward(String openId, GetTaskRewardReq req) throws Exception {
        Result result = taskService.getTaskReward(openId, req.getTaskId());
        return result;
    }

    @Command(cmd = Cmd.FINISH_TASK, description = "完成任务")
    public Result finishTask(String openId, FinishTaskReq req) throws Exception {
        Result result = taskService.finishTask(openId, req.getTaskId(), req.getNum());
        return result;
    }
}
