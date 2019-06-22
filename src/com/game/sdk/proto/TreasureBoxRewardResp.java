package com.game.sdk.proto;

import com.game.sdk.proto.vo.ToolVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lucky on 2019/3/14.
 */
public class TreasureBoxRewardResp {
    private int step;
    private int ticket;
    private List<ToolVO> tools = Lists.newArrayList();
    private List<ToolVO> activityItems = Lists.newArrayList();

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public List<ToolVO> getTools() {
        return tools;
    }

    public void setTools(List<ToolVO> tools) {
        this.tools = tools;
    }

    public List<ToolVO> getActivityItems() {
        return activityItems;
    }

    public void setActivityItems(List<ToolVO> activityItems) {
        this.activityItems = activityItems;
    }
}
