package com.game.domain.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.sdk.proto.vo.FighterAnswerVO;
import com.game.sdk.proto.vo.FighterVO;

/**
 * Created by lucky on 2018/10/11.
 */
public class Fighter {
    private String openId;
    private String nickName;
    private String iconUrl;
    private int roomId;
    private boolean robot = false;
    private boolean offline = false;

    private int level;
    private int score;
    //大师赛
    private int round = 0; //第几回合

    //吃鸡模式
    private int chooseAnswer = 0;   //选中的答案

    @JsonIgnore
    public volatile boolean matchFlag; //匹配标识
    //匹配次数
    @JsonIgnore
    public int count; //匹配次数
    //取消标记
    @JsonIgnore
    public volatile boolean exitFlag; //退出标志
    @JsonIgnore
    public volatile boolean victory = false; //是否胜利
    @JsonIgnore
    public boolean fighting = false; //是否答题中

    public Fighter(String openId, String nickName,String iconUrl) {
        this.openId = openId;
        this.nickName = nickName;
        this.iconUrl=iconUrl;
    }

    public Fighter() {
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isRobot() {
        return robot;
    }

    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    public boolean isOffline() { return offline; }

    public void setOffline(boolean offline) { this.offline = offline; }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getChooseAnswer() {
        return chooseAnswer;
    }

    public void setChooseAnswer(int chooseAnswer) {
        this.chooseAnswer = chooseAnswer;
    }

    public void clean() {
        roomId = 0;
        matchFlag = false;
        fighting = false;
    }

    public void matchSuccess() {
        matchFlag = true;
        fighting = true;
    }

    public FighterVO toProto(){
        FighterVO result = new FighterVO();

        result.setOpenId(openId);
        result.setNickName(nickName);
        result.setIconUrl(iconUrl);
        result.setRobot(robot);
        return result;
    }

    public FighterAnswerVO toAnswerProto(){
        FighterAnswerVO result = new FighterAnswerVO();
        result.setOpenId(openId);
        result.setAnswer(chooseAnswer);

        return result;
    }
}
