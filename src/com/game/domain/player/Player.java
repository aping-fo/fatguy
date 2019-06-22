package com.game.domain.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.sdk.proto.vo.PlayerRankVO;

import java.util.concurrent.locks.ReentrantLock;

public class Player {
    /**
     * 玩家锁
     */
    @JsonIgnore
    public final ReentrantLock SelfLock = new ReentrantLock();

    private String openId;
    private String nickName;
    private String avatarUrl;
    //创建时间
    private long createTime;

    //偷取次数
    private int stealTimes;

    //登录时间
    private long loginTime;
    private String playerData;

    private PlayerData playerDataObject = new PlayerData();

    public int getStealTimes() {
        return stealTimes;
    }

    public void setStealTimes(int stealTimes) {
        this.stealTimes = stealTimes;
    }

    public String getPlayerData() {
        return playerData;
    }

    public void setPlayerData(String playerData) {
        this.playerData = playerData;
    }

    public PlayerData getPlayerDataObject() {
        return playerDataObject;
    }

    public void setPlayerDataObject(PlayerData playerDataObject) {
        this.playerDataObject = playerDataObject;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
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

    public PlayerRank toPlayerRank(){
        PlayerRank rank = new PlayerRank();
        rank.setOpenId(openId);
        rank.setNickName(nickName);
        rank.setAvatarUrl(avatarUrl);
        rank.setStealTimes(stealTimes);

        return rank;
    }
}
