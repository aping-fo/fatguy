package com.game.sdk.proto;

public class GainStepBallReq {
    private String id;
    private String targetOpenId;
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetOpenId() {
        return targetOpenId;
    }

    public void setTargetOpenId(String targetOpenId) {
        this.targetOpenId = targetOpenId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
