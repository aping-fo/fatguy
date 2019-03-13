package com.game.domain.player;

import com.game.data.ToolCfg;
import com.game.domain.bag.Bag;
import com.game.domain.buffer.Buffer;
import com.game.sdk.proto.vo.StepBallVO;
import com.game.util.TimeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lucky on 2019/3/12.
 * 玩家相关数据相关
 */
public class PlayerData {

    private String iconUrl;

    private float step;

    private int todayTransStep;
    /**
     * 抽奖劵
     */
    private int ticket;

    /**
     *
     */
    private Map<StepType, List<StepBallVO>> stepBallMap = Maps.newHashMap();

    /**
     * 好友来访记录
     */
    private List<PlayerVisiter> playerVisiters = new ArrayList<>();

    /**
     * 背包
     */
    private Bag bag = new Bag();

    private Map<Integer, Buffer> bufferMap = Maps.newHashMap();
    /**
     * 上次计算卡路里的时间
     */
    private long lastCalculateTime;

    public PlayerData() {
        stepBallMap.put(StepType.Normal, Lists.newArrayList());
        stepBallMap.put(StepType.Give, Lists.newArrayList());
    }


    public Map<Integer, Buffer> getBufferMap() {
        return bufferMap;
    }

    public void setBufferMap(Map<Integer, Buffer> bufferMap) {
        this.bufferMap = bufferMap;
    }

    public long getLastCalculateTime() {
        return lastCalculateTime;
    }

    public void setLastCalculateTime(long lastCalculateTime) {
        this.lastCalculateTime = lastCalculateTime;
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public Map<StepType, List<StepBallVO>> getStepBallMap() {
        return stepBallMap;
    }

    public void setStepBallMap(Map<StepType, List<StepBallVO>> stepBallMap) {
        this.stepBallMap = stepBallMap;
    }

    public List<PlayerVisiter> getPlayerVisiters() {
        return playerVisiters;
    }

    public void setPlayerVisiters(List<PlayerVisiter> friendVisiters) {
        this.playerVisiters = friendVisiters;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        this.step = step;
    }

    public int getTodayTransStep() {
        return todayTransStep;
    }

    public void setTodayTransStep(int todayTransStep) {
        this.todayTransStep = todayTransStep;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }


    /**
     * 增加一个buffer
     *
     * @param config
     * @return
     */
    public Buffer addBuffer(ToolCfg config) {
        Buffer buffer = bufferMap.get(config.id);
        if (buffer == null) {
            buffer = new Buffer();
            buffer.setCdStart(TimeUtil.getCurrentSeconds());
            buffer.setCdtime(config.during);
            buffer.setId(config.id);

            bufferMap.put(config.id, buffer);
        } else {
            if (buffer.checkTimeout()) {
                buffer.setCdStart(TimeUtil.getCurrentSeconds());
                buffer.setCdtime(config.during);
            } else {
                buffer.setCdtime(buffer.getCdtime() + config.during);
            }

        }
        return buffer;
    }
}
