package com.game.domain.activity;

import com.game.data.CollectionCfg;
import com.game.sdk.proto.vo.ActivityVO;
import com.game.util.ConfigData;
import com.google.common.collect.Maps;
import sun.security.krb5.Config;

import java.util.List;
import java.util.Map;

public class ActivityData {
    private int id;
    private Map<Integer, Integer> infos = Maps.newHashMap();
    private Map<Integer, Integer> items = Maps.newHashMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Integer> getInfos() {
        return infos;
    }

    public void setInfos(Map<Integer, Integer> infos) {
        this.infos = infos;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }

    public ActivityVO toProto(){
        ActivityVO vo = new ActivityVO();

        vo.setId(id);
        vo.setItems(items);

        return vo;
    }
}
