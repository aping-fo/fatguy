package com.game.sdk.proto.vo;

public class VisitablePlayerVO {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private boolean isAward;

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

    public boolean isAward() {
        return isAward;
    }

    public void setAward(boolean award) {
        isAward = award;
    }
}
