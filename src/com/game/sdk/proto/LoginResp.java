package com.game.sdk.proto;

import com.game.sdk.proto.vo.BufferVO;

import java.util.ArrayList;
import java.util.List;

public class LoginResp {
    private String openId;
    private int stealTimes;
    private boolean isFirstLogin;
    private boolean secondDayLogin;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getStealTimes() {
        return stealTimes;
    }

    public void setStealTimes(int stealTimes) {
        this.stealTimes = stealTimes;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public boolean isSecondDayLogin() {
        return secondDayLogin;
    }

    public void setSecondDayLogin(boolean secondDayLogin) {
        this.secondDayLogin = secondDayLogin;
    }
}
