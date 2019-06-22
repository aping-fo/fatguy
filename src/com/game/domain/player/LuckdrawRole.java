package com.game.domain.player;

/**
 * Created by lucky on 2019/3/28.
 */
public class LuckdrawRole {
    private String openid;
    private String awardNo;
    private String avatarUrl;
    private String nickName;
    private int attendTimes;
    private double probability;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAwardNo() {
        return awardNo;
    }

    public void setAwardNo(String awardNo) {
        this.awardNo = awardNo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getAttendTimes() {
        return attendTimes;
    }

    public void setAttendTimes(int attendTimes) {
        this.attendTimes = attendTimes;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
