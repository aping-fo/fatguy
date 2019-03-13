package com.game.sdk.proto;

/**
 * Created by lucky on 2018/10/11.
 */
public class UpdateRoleReq {
    private String iconUrl;
    private String nickName;
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
}
