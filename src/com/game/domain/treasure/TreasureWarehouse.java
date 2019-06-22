package com.game.domain.treasure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.util.TimeUtil;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TreasureWarehouse {
    @JsonIgnore
    private AtomicInteger idGen = new AtomicInteger();
    @JsonIgnore
    private boolean isAward;

    /**
     * 宝箱
     */
    private Map<Integer, Treasure> treasureMap = Maps.newHashMap();

    /**
     * 上次开启宝箱的时间
     */
    private int lastOpenTreasureTime;


    /**
     * 登录完加载一次
     */
    public void loadIdgen() {
        int maxId = 1000;
        for (int id : treasureMap.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }

        idGen = new AtomicInteger(maxId + 1);
    }

    public Treasure addTreasureBox(int configId, int count){
        Treasure treasure = null;
        for(int i = 0; i < count; i++){
            treasure = new Treasure(idGen.getAndIncrement(), configId);
            this.treasureMap.put(treasure.getId(), treasure);

            if(this.treasureMap.size() == 1){
                this.setLastOpenTreasureTime(TimeUtil.getCurrentSeconds());
            }
        }

        return treasure;
    }

    public Treasure getOpenTreasure(){
        for(Integer key : treasureMap.keySet()){
            Treasure treasure = treasureMap.get(key);
            if(treasure.getOpenTime() != 0){
                return treasure;
            }
        }

        return null;
    }

    public boolean isAward() {
        return isAward;
    }

    public void setAward(boolean award) {
        isAward = award;
    }

    public Map<Integer, Treasure> getTreasureMap() {
        return treasureMap;
    }

    public void setTreasureMap(Map<Integer, Treasure> treasureMap) {
        this.treasureMap = treasureMap;
    }

    public int getLastOpenTreasureTime() {
        return lastOpenTreasureTime;
    }

    public void setLastOpenTreasureTime(int lastOpenTreasureTime) {
        this.lastOpenTreasureTime = lastOpenTreasureTime;
    }
}
