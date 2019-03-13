package com.game.domain.quest;

import com.game.sdk.proto.vo.QuestVO;
import com.game.util.TimerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jdk.nashorn.internal.runtime.Debug;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChickenRoom{
    public enum RoomStatus {
        Waiting,
        Ready,
        StartQuestion,
        StartAnswer,
        End
    }

    private int id;
    private long startTime;
    private int mode;

    private RoomStatus status;
    private long updateTime;   //倒计时时间

    private List<Fighter> roles = Lists.newArrayList();
    private QuestVO currentQuest;  //当前抢答题



    private QuestVO lastQuset; //上一个抢答题
    private List<QuestVO> currentQuestions = Lists.newArrayList(); //已经出的题目
    private int lastQuestionAnswer;         //上次答案

    private ScheduledFuture scheduler;


    public static final long UpdateTimerInterval = 1;
    public static final long WaitingTime = 10;              //等待时间
    public static final long ReadyTime = 3;              //准备开始
    public static final long AnswerTime = 10;               //回答时间
    public static final long AnswerAniTime = 5;               //答对动画时间差

    public ChickenRoom(int id) {
        this.id = id;
        this.startTime = System.currentTimeMillis();
        this.mode = 0;
        this.status = RoomStatus.Waiting;
        this.updateTime = 0;
        this.lastQuestionAnswer=0;
    }

    public ScheduledFuture getScheduler() {
        return scheduler;
    }

    public void setScheduler(ScheduledFuture scheduler) {
        this.scheduler = scheduler;
    }

    public void cancel() throws Throwable {
        this.scheduler.cancel(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void update() {
        updateTime += UpdateTimerInterval;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public List<Fighter> getRoles() {
        return roles;
    }

    public void setRoles(List<Fighter> roles) {
        this.roles = roles;
    }

    public QuestVO getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(QuestVO currentQuest) {
        this.lastQuset=this.currentQuest;
        this.currentQuest = currentQuest;
    }

    public int getLastQuestionAnswer() {
        return lastQuestionAnswer;
    }

    public void setLastQuestionAnswer(int lastQuestionAnswer) {
        this.lastQuestionAnswer = lastQuestionAnswer;
    }

    public List<QuestVO> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(List<QuestVO> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }

    public boolean isWaiting(){ return status == RoomStatus.Waiting; }

    public boolean isStart() {
        return status == RoomStatus.StartQuestion || status == RoomStatus.StartAnswer;
    }

    public boolean isEnd() {
        return status == RoomStatus.End;
    }

    public void setStartAnswer() {
        status = RoomStatus.StartAnswer;
        //答对动画时间差，暂时定位3秒
        if(updateTime == AnswerTime)
        {
            updateTime=-AnswerAniTime;
        }
        else
        {
            updateTime = 0;
        }

    }
    public void setReady()
    {
        status=RoomStatus.Ready;
        updateTime = 0;
    }
    public QuestVO getLastQuset() {
        return lastQuset;
    }

    public void setLastQuset(QuestVO lastQuset) {
        this.lastQuset = lastQuset;
    }
}
