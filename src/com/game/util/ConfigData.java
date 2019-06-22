package com.game.util;

import com.game.data.CollectionCfg;
import com.game.data.ToolCfg;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 一些不是唯一key的配置，可以在这里定义一些辅助函数操作
 */
public class ConfigData {
    public static List<ToolCfg> toolCfgList = Lists.newArrayList();
    public static Map<Integer, List<Integer>> collectionDatas = Maps.newHashMap();

    public static void init() {
        //初始化道具配置表
        for(Object obj : getConfigs(ToolCfg.class)){
            toolCfgList.add((ToolCfg)obj);
        }

        //初始化集字数据
        for(Object obj : getConfigs(CollectionCfg.class)){
            CollectionCfg cfg = (CollectionCfg)obj;
            List<Integer> list = collectionDatas.getOrDefault(cfg.collection, null);
            if(list == null){
                list = Lists.newArrayList();
                collectionDatas.put(cfg.collection, list);
            }

            list.add(cfg.id);
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
