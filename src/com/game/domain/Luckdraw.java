package com.game.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.domain.player.LuckdrawRole;
import com.google.common.collect.Maps;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lucky on 2019/3/28.
 */
public class Luckdraw {
    /**
     * 本期ID
     */
    private String id;
    /**
     * 中奖者
     */
    private String luckPlayerOpenid;

    //中奖者昵称
    private String nickName;
    //中奖者头像
    private String avatarUrl;

    /**
     * 中奖编号
     */
    private String awardNo;

    private float money;

    /**
     * 时间
     */
    private int time;

    /**
     * 抽奖号 --- openid
     */
    private ConcurrentHashMap<String, String> luckdrawMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, LuckdrawRole> roleMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, LuckdrawRole> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(ConcurrentHashMap<String, LuckdrawRole> roleMap) {
        this.roleMap = roleMap;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLuckPlayerOpenid() {
        return luckPlayerOpenid;
    }

    public void setLuckPlayerOpenid(String luckPlayerOpenid) {
        this.luckPlayerOpenid = luckPlayerOpenid;
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

    public String getAwardNo() {
        return awardNo;
    }

    public void setAwardNo(String awardNo) {
        this.awardNo = awardNo;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ConcurrentHashMap<String, String> getLuckdrawMap() {
        return luckdrawMap;
    }

    public void setLuckdrawMap(ConcurrentHashMap<String, String> luckdrawMap) {
        this.luckdrawMap = luckdrawMap;
    }

    @JsonIgnore
    DecimalFormat df = new DecimalFormat("00000");

    public String genNumber() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        String preNO = year + "" + month + "" + day;
        int num = luckdrawMap.size();
        String postNo = df.format(num);

        return preNO + postNo;
    }

    public Map<String, Object> toProto(){
        Map<String, Object> r = Maps.newHashMapWithExpectedSize(6);
        r.put("id", id);
        r.put("nickName",nickName);
        r.put("avatarUrl", avatarUrl);
        r.put("money", money);
        r.put("awardNo", awardNo);
        r.put("time", time);
        return r;
    }
}
