package com.game.domain.player;

/**
 * Created by lucky on 2019/3/12.
 */
public class PlayerVisitor {
    private String openId;
    private String nickName;

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
}