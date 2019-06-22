package com.game.sdk.proto.vo;

/**
 * Created by lucky on 2019/3/16.
 */
public class FriendVO {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private int stealTimes;
    private boolean award;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openid) {
        this.openId = openid;
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

    public boolean isAward() {
        return award;
    }

    public void setAward(boolean award) {
        this.award = award;
    }
}
