package com.game.sdk.proto;

/**
 * Created by lucky on 2019/3/14.
 */
public class TreasureBoxReq {
    private String targetOpenId;
    private int id;

    public String getTargetOpenId() {
        return targetOpenId;
    }

    public void setTargetOpenId(String targetOpenId) {
        this.targetOpenId = targetOpenId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
