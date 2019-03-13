package com.game.domain.mall;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lucky on 2018/12/20.
 */
public class Mall {
    private int id;
    private ConcurrentHashMap<String, Integer> playerSteps = new ConcurrentHashMap<>();
    private int code = 10000;
    private String openid; //获奖者
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Mall(int id) {
        this.id = id;
    }

    public Mall() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConcurrentHashMap<String, Integer> getPlayerSteps() {
        return playerSteps;
    }

    public void setPlayerSteps(ConcurrentHashMap<String, Integer> playerSteps) {
        this.playerSteps = playerSteps;
    }

    public synchronized int genCode() {
        return code++;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public synchronized boolean add(String openid, int code, int max) {
        if (playerSteps.size() >= max) {
            return false;
        }
        if(playerSteps.get(openid) != null)
        {
            return false;
        }
        playerSteps.put(openid, code++);
        return true;
    }
}
