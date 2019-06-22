package com.game.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.domain.player.Trend;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lucky on 2018/10/17.
 * 全局数据相关
 */
public class ServerData {
    //全服数据缓存
    @JsonIgnore
    private ConcurrentHashMap<String, Float> roleCalorie = new ConcurrentHashMap<>();
    //抽奖数据
    private ConcurrentHashMap<String, Luckdraw> luckdraw = new ConcurrentHashMap<>();
    //全局动态数据
    private List<Trend> trends = Lists.newArrayList();
    //当前抽奖key
    private String curLuckdrawKey = "";
    //上次开奖时间戳
    private long luckdrawRewardTime = 0;

    public long getLuckdrawRewardTime() {
        return luckdrawRewardTime;
    }

    public void setLuckdrawRewardTime(long luckdrawRewardTime) {
        this.luckdrawRewardTime = luckdrawRewardTime;
    }

    public String getCurLuckdrawKey() {
        return curLuckdrawKey;
    }

    public void setCurLuckdrawKey(String curLuckdrawKey) {
        this.curLuckdrawKey = curLuckdrawKey;
    }

    public List<Trend> getTrends() {
        return trends;
    }

    public void setTrends(List<Trend> trends) {
        this.trends = trends;
    }

    public ConcurrentHashMap<String, Luckdraw> getLuckdraw() {
        return luckdraw;
    }

    public void setLuckdraw(ConcurrentHashMap<String, Luckdraw> luckdraw) {
        this.luckdraw = luckdraw;
    }

    public ConcurrentHashMap<String, Float> getRoleCalorie() {
        return roleCalorie;
    }

    public void setRoleCalorie(ConcurrentHashMap<String, Float> roleCalorie) {
        this.roleCalorie = roleCalorie;
    }
}
