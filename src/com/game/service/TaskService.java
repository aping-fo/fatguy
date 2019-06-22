package com.game.service;

import com.game.domain.player.Player;
import com.game.domain.task.Task;
import com.game.domain.task.TaskConsts;
import com.game.sdk.net.Result;
import com.game.sdk.utils.ErrorCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TreasureService treasureService;

    public Result getTaskList(String openId){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(1);
        List<Map<String, Object>> tasks = Lists.newArrayList();

        for(Task task : player.getPlayerDataObject().getTasks().values()){
            Map<String, Object> taskData = Maps.newHashMapWithExpectedSize(4);

            taskData.put("id", task.getId());
            taskData.put("num", task.getNum());
            taskData.put("totalNum", task.getTotalNum());
            taskData.put("status", task.getStatus());

            tasks.add(taskData);
        }

        resp.put("taskList", tasks);

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result getTaskReward(String openId, int taskId){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        Task task = player.getPlayerDataObject().getTasks().get(taskId);
        if(task == null){
            return Result.valueOf(ErrorCode.TASK_NOT_EXIST);
        }

        if(task.getStatus() == TaskConsts.TASK_STATUS_UNFINISH){
            return Result.valueOf(ErrorCode.TASK_UNFINISH);
        }

        if(task.getStatus() == TaskConsts.TASK_STATUS_CLOSE){
            return Result.valueOf(ErrorCode.TASK_CLOSE);
        }

        treasureService.addTreasureBox(openId, task.getReward());

        if(task.getResetType() == TaskConsts.TASK_RESETTYPE_IMMEDIATE || (task.getResetType() == TaskConsts.TASK_RESETTYPE_LIMIT  && task.getResetTimes() < task.getMaxResetTimes())){
            task.reset();
        }
        else
        {
            task.setStatus(TaskConsts.TASK_STATUS_CLOSE);
        }
        playerService.saveOrUpdate(player,false);
        return Result.valueOf(ErrorCode.OK);
    }

    public Result finishTask(String openId, int taskId, int num, Integer... params){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        Task task = player.getPlayerDataObject().getTasks().get(taskId);
        if(task == null){
            return Result.valueOf(ErrorCode.TASK_NOT_EXIST);
        }

        if(task.getStatus() == TaskConsts.TASK_STATUS_FINISHED){
            return Result.valueOf(ErrorCode.TASK_FINISHED);
        }

        if(task.getStatus() == TaskConsts.TASK_STATUS_CLOSE){
            return Result.valueOf(ErrorCode.TASK_CLOSE);
        }

        if(!this.checkCanFinishTask(task, params)){
            return Result.valueOf(ErrorCode.TASK_DONT_REACH_REQUIRE);
        }

        task.addNum(num);

        if(task.getResetType() == TaskConsts.TASK_RESETTYPE_DAILY && task.getStatus() == TaskConsts.TASK_STATUS_FINISHED){
            //检查全部每日任务是否完成
            Task dailyTask = player.getPlayerDataObject().getTasks().get(TaskConsts.TASK_TYPE_DAILY_TASK);
            if(dailyTask.getStatus() == TaskConsts.TASK_STATUS_UNFINISH){
                dailyTask.addNum(1);
            }
        }

        return Result.valueOf(ErrorCode.OK);
    }

    private boolean checkCanFinishTask(Task task, Integer[] params){
        if(task.getType() == TaskConsts.TASK_TYPE_LOGIN){
            if(params[0] < task.getParams()[0] || params[0] > task.getParams()[1]){
                return false;
            }
        }

        return true;
    }
}
