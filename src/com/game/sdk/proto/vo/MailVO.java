package com.game.sdk.proto.vo;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MailVO {
    private int id;
    private int type;
    private int createTime;
    private String content;
    private String content1;
    private String avatarUrl;
    private Collection<Integer> rewards;
    private boolean read;
    private boolean getReward;

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

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Collection<Integer> getRewards() {
        return rewards;
    }

    public void setRewards(Collection<Integer> rewards) {
        this.rewards = rewards;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isGetReward() {
        return getReward;
    }

    public void setGetReward(boolean getReward) {
        this.getReward = getReward;
    }
}
