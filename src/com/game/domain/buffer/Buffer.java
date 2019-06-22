package com.game.domain.buffer;

import com.game.sdk.proto.vo.BufferVO;
import com.game.util.TimeUtil;

/**
 * Created by lucky on 2019/3/13.
 */
public class Buffer {
    public static final int BUFF_TYPE_RUNMACHINE = 1;   //跑步机
    public static final int BUFF_TYPE_FATWATER = 2;    //喝肥宅水
    public static final int BUFF_TYPE_CLOTH_HANDSOME = 10;   //文化衫——帅
    public static final int BUFF_TYPE_CLOTH_UGLY = 11;    //文化衫——丑
    public static final int BUFF_TYPE_BOARD_STEAL = 20;   //文化路牌—偷
    public static final int BUFF_TYPE_BOARD_FAT = 21;    //文化路牌—拒
    private int id;
    private int type;
    private int group;

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

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
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
        return this.getLeftCdTime() <= 0;
    }

    public int getPassCDTime(){
        if(cdStart == 0){
            return 0;
        }

        return Math.min(TimeUtil.getCurrentSeconds() - cdStart, cdtime);
    }

    //剩余CD
    public int getLeftCdTime(){
        if(cdStart == 0){
            return cdtime;
        }
        return cdtime - (TimeUtil.getCurrentSeconds() - cdStart);
    }

    public BufferVO toProto() {
        BufferVO vo = new BufferVO();
        vo.setId(id);
        vo.setType(type);
        vo.setGroup(group);
        vo.setRemainTime(this.getLeftCdTime());
        return vo;
    }
}
