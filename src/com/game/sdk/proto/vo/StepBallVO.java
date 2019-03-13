package com.game.sdk.proto.vo;

public class StepBallVO {
    private String id;
    private int type;
    private int step;
    private GiveStepVO param;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public GiveStepVO getParam() {
        return param;
    }

    public void setParam(GiveStepVO param) {
        this.param = param;
    }
}
