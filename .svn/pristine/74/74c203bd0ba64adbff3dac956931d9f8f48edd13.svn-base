package com.game.domain.buffer;

import com.game.util.TimeUtil;

/**
 * Created by lucky on 2019/3/13.
 */
public class Buffer {
    private int id;
    private int type;

    /**
     * 总buffer作用时间
     */
    private int cdtime;

    /**
     * buffer作用开始时间
     */
    private int cdStart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCdtime() {
        return cdtime;
    }

    public void setCdtime(int cdtime) {
        this.cdtime = cdtime;
    }

    public int getCdStart() {
        return cdStart;
    }

    public void setCdStart(int cdStart) {
        this.cdStart = cdStart;
    }

    public boolean checkTimeout() {
        int passtime = TimeUtil.getCurrentSeconds() - cdStart;
        return passtime >= cdtime;
    }
}
