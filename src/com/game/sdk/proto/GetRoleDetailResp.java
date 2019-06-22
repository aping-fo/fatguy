package com.game.sdk.proto;

import com.game.sdk.proto.vo.BufferVO;

import java.util.ArrayList;
import java.util.List;

public class GetRoleDetailResp {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private int stealTimes;
    private float ticket;

    private List<BufferVO> buffs = new ArrayList<>();

    public List<BufferVO> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<BufferVO> buffs) {
        this.buffs = buffs;
    }

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

    public float getTicket() {
        return ticket;
    }

    public void setTicket(float ticket) {
        this.ticket = ticket;
    }
}
