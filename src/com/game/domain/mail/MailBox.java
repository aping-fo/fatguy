package com.game.domain.mail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.data.MailCfg;
import com.game.domain.treasure.TreasureConsts;
import com.game.util.ConfigData;
import com.game.util.TimeUtil;
import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MailBox {
    private ConcurrentHashMap<Integer, Mail> map = new ConcurrentHashMap<>();
    @JsonIgnore
    private AtomicInteger idGen = new AtomicInteger();
    @JsonIgnore
    private static final int MAX_SIZE = 30;

    public ConcurrentHashMap<Integer, Mail> getMap() {
        return map;
    }

    public void setMap(ConcurrentHashMap<Integer, Mail> map) {
        this.map = map;
    }

    /**
     * 登录完加载一次
     */
    public void loadIdgen() {
        int maxId = 1000;
        for (int id : map.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }

        idGen = new AtomicInteger(maxId + 1);
    }

    public void addMail(int type, String ... params){
        int id = idGen.getAndIncrement();
        MailCfg cfg = ConfigData.getConfig(MailCfg.class, type);

        if(cfg == null){
            return;
        }

        Mail mail = new Mail();
        mail.setId(id);
        mail.setType(type);
        
        if(params.length >= 1){
            mail.setContent(MessageFormat.format(cfg.content, params[0]));
        }else{
            mail.setContent(cfg.content);
        }

        if(params.length >= 2){
            mail.setContent1(MessageFormat.format(cfg.content1, params[1]));
        }else{
            mail.setContent1(cfg.content1);
        }

        if(params.length >= 3){
            mail.setAvatarUrl(params[2]);
        }

        mail.setCreateTime(TimeUtil.getCurrentSeconds());
        mail.setRead(false);
        mail.setImportant(type != 1);

        //奖励
        mail.setGetReward(true);
        if(cfg.rewards != null && cfg.rewards.length > 0){
            Map<Integer, Integer> rewards = Maps.newHashMap();
            for(int i = 0; i < cfg.rewards.length; i++){
                rewards.put(cfg.rewards[i][0], cfg.rewards[i][1]);
            }
            mail.setRewards(rewards);
            mail.setGetReward(false);
        }
        map.put(id, mail);

        if(map.size() > MAX_SIZE){
            boolean isFind = false;
            int waitRemoveId = 0;

            for(Mail m : map.values()){
                if(!m.isImportant() || m.isRead()){
                    if(!m.isImportant() && m.isRead()){
                        isFind = true;
                        map.remove(m.getId());
                        break;
                    }
                    waitRemoveId = m.getId();
                }
            }
            if(!isFind && waitRemoveId != 0){
                map.remove(waitRemoveId);
            }
        }
    }
}
