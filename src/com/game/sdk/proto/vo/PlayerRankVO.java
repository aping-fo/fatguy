package com.game.sdk.proto.vo;

/**
 * Created by lucky on 2018/10/16.
 */
public class PlayerRankVO {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private int stealTimes;
    private int rank;
    private boolean award;

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getStealTimes() {
        return stealTimes;
    }

    public void setStealTimes(int stealTimes) {
        this.stealTimes = stealTimes;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isAward() {
        return award;
    }

    public void setAward(boolean award) {
        this.award = award;
    }
}
