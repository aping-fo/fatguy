package com.game.util;


import com.game.data.GlobalConfig;
import com.game.data.GroupRecordCfg;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 一些不是唯一key的配置，可以在这里定义一些辅助函数操作
 */
public class ConfigData {


    public static GlobalConfig globalCfg;
    public static Map<Integer, List<GroupRecordCfg>> groupRecordsMap = Maps.newHashMap();

    public static void init() {
        globalCfg = GameData.getConfig(GlobalConfig.class, 1);

        Collection<Object> cfgs = GameData.getConfigs(GroupRecordCfg.class);
        for(Object obj : cfgs){
            GroupRecordCfg cfg = (GroupRecordCfg)obj;
            List<GroupRecordCfg> list = groupRecordsMap.getOrDefault(cfg.eventType, null);
            if(list == null){
                list = Lists.newArrayList();
                groupRecordsMap.put(cfg.eventType, list);
            }
            list.add(cfg);
        }
    }

    public static <T> T getConfig(Class<T> t, int id) {
        T cfg = GameData.getConfig(t, id);
        return cfg;
    }

    public static Collection<Object> getConfigs(Class<?> t) {
        return GameData.getConfigs(t);
    }
}
