package com.game.domain.mail;

import com.game.sdk.proto.vo.MailVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class Mail {
    private int id;
    private int type;
    private int createTime;
    private String content;
    private String content1;
    private String avatarUrl = "";
    private Map<Integer, Integer> rewards = Maps.newHashMap();
    private boolean isRead;
    private boolean important;
    private boolean getReward = true;

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

    public Map<Integer, Integer> getRewards() {
        return rewards;
    }

    public void setRewards(Map<Integer, Integer> rewards) {
        this.rewards = rewards;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isGetReward() {
        return getReward;
    }

    public void setGetReward(boolean getReward) {
        this.getReward = getReward;
    }

    public MailVO toProto(){
        MailVO vo = new MailVO();
        vo.setId(id);
        vo.setType(type);
        vo.setContent(content);
        vo.setContent1(content1);
        vo.setAvatarUrl(avatarUrl);
        vo.setCreateTime(createTime);
        vo.setRewards(rewards.values());
        vo.setRead(isRead);
        vo.setGetReward(getReward);

        return vo;
    }
}
