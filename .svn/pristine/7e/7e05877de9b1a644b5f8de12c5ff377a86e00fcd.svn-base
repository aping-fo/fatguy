package com.game.domain.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.domain.quest.HistoryRace;
import com.game.util.JsonUtils;
import com.google.common.collect.Lists;

import java.util.List;

public class Player {
    private String openId;
    private String nickName;
    private int level;
    private int step;
    private int totalStep;
    private String iconUrl;

    private int todayTransStep;

    //创建时间
    private long createTime;
    //登录时间
    private long loginTime;
    //群ID
    private String groupId;

    private long signTime;

    @JsonIgnore
    private int lastGameStepChange = 0; //上局步数变化
    @JsonIgnore
    private int lastQuestionAnswer = 0;  //上一局游戏答案
    @JsonIgnore
    private boolean lastGameWin;         //上一盘结果


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getTotalStep() {
        return totalStep;
    }

    public void setTotalStep(int totalStep) {
        this.totalStep = totalStep;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public int getLastGameStepChange() {
        return lastGameStepChange;
    }

    public void setLastGameStepChange(int lastGameStepChange) {
        this.lastGameStepChange = lastGameStepChange;
    }

    public int getLastQuestionAnswer() {
        return lastQuestionAnswer;
    }

    public void setLastQuestionAnswer(int lastQuestionAnswer) {
        this.lastQuestionAnswer = lastQuestionAnswer;
    }

    public boolean isLastGameWin() {
        return lastGameWin;
    }

    public void setLastGameWin(boolean lastGameWin) {
        this.lastGameWin = lastGameWin;
    }

    public int getTodayTransStep() {
        return todayTransStep;
    }

    public void setTodayTransStep(int todayTransStep) {
        this.todayTransStep = todayTransStep;
    }
}
