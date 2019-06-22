package com.game.domain.player;

import com.game.sdk.proto.vo.PlayerRankVO;

public class PlayerRank {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private int stealTimes;

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

    public PlayerRankVO toPlayerRankVO(){
        PlayerRankVO rank = new PlayerRankVO();
        rank.setOpenId(openId);
        rank.setNickName(nickName);
        rank.setAvatarUrl(avatarUrl);
        rank.setStealTimes(stealTimes);

        return rank;
    }
}
