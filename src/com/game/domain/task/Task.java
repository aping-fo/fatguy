package com.game.domain.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.data.TaskCfg;
import com.game.util.ConfigData;

public class Task {
    private int id;     //配置表ID
    private int type;       //任务类型
    private int resetType;  //重置类型
    private int resetTimes; //重置次数
    private int maxResetTimes; //最大重置次数
    private int status;     //状态
    private int num;    //完成度
    private int reward; //奖励
    private int[] params;   //参数

    public Task(){

    }

    public Task(int id){
        this.id = id;

        TaskCfg cfg = ConfigData.getConfig(TaskCfg.class, id);
        this.resetTimes = 0;
        this.status = TaskConsts.TASK_STATUS_UNFINISH;
        this.num = 0;
        this.setCfg(cfg);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResetType() {
        return resetType;
    }

    public void setResetType(int resetType) {
        this.resetType = resetType;
    }

    public int getResetTimes() {
        return resetTimes;
    }

    public void setResetTimes(int resetTimes) {
        this.resetTimes = resetTimes;
    }

    public int getMaxResetTimes() {
        return maxResetTimes;
    }

    public void setMaxResetTimes(int maxResetTimes) {
        this.maxResetTimes = maxResetTimes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int[] getParams() {
        return params;
    }

    public void setParams(int[] params) {
        this.params = params;
    }

    public int getTotalNum(){
        return this.params[this.params.length - 1];
    }

    public void setCfg(TaskCfg cfg){
        this.type  = cfg.type;
        this.resetType = cfg.resetType;
        this.reward = cfg.reward;
        this.params = cfg.params;
        this.maxResetTimes = cfg.resetTimes;
    }

    public void addNum(int num){
        this.num += num;

        if(this.num >= this.getTotalNum()){
            this.status = TaskConsts.TASK_STATUS_FINISHED;
        }
    }

    public void reset(){
        this.status = TaskConsts.TASK_STATUS_UNFINISH;
        this.num = 0;
        this.resetTimes++;
    }
}
