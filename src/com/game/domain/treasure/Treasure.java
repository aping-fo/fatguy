package com.game.domain.treasure;

import com.game.data.TreasureCfg;
import com.game.util.ConfigData;
import com.game.util.TimeUtil;

/**
 * Created by lucky on 2019/3/14.
 */
public class Treasure {
    /**
     * id
     */
    private int id;
    /**
     * configId
     */
    private int configId;
    /**
     * 开启计时开始时间
     */
    private int openTime;
    /**
     * 冷却时间
     */
    private int cdTime;
    /**
     *  加速时间
     */
    private int speedupTime;
    /**
     * 是否领奖
     */
    private boolean awardFlag;

    /**
     * 是否新手宝箱
     */
    private boolean newPlayerTreasure;

    public Treasure() {
    }

    public Treasure(int id, int configId) {
        this.id = id;
        this.configId = configId;
        this.openTime = 0;
        this.awardFlag = false;
        this.cdTime = ConfigData.getConfig(TreasureCfg.class, configId).cooldown;
        this.speedupTime = 0;
        this.newPlayerTreasure = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCdTime() {
        return cdTime;
    }

    public void setCdTime(int cdTime) {
        this.cdTime = cdTime;
    }

    public int getSpeedupTime() {
        return speedupTime;
    }

    public void setSpeedupTime(int speedupTime) {
        this.speedupTime = speedupTime;
    }

    public boolean isAwardFlag() {
        return awardFlag;
    }

    public void setAwardFlag(boolean awardFlag) {
        this.awardFlag = awardFlag;
    }

    public boolean isNewPlayerTreasure() {
        return newPlayerTreasure;
    }

    public void setNewPlayerTreasure(boolean newPlayerTreasure) {
        this.newPlayerTreasure = newPlayerTreasure;
    }
}
