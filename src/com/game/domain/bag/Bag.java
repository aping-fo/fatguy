package com.game.domain.bag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.data.ToolCfg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lucky on 2019/3/12.
 */
public class Bag {

    private ConcurrentHashMap<Integer, Item> bag = new ConcurrentHashMap<>();
    @JsonIgnore
    private AtomicInteger idGen = new AtomicInteger();

    public ConcurrentHashMap<Integer, Item> getBag() {
        return bag;
    }

    public void setBag(ConcurrentHashMap<Integer, Item> bag) {
        this.bag = bag;
    }

    /**
     * 登录完加载一次
     */
    public void loadIdgen() {
        int maxId = 1000;
        for (int id : bag.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }

        idGen = new AtomicInteger(maxId);
    }


    public void addItem(ToolCfg cfg, int count) {
        //判断是否可叠加，有冷却时间的应该不可叠加
        boolean flag = cfg.maxStack != 0;
        if (flag) {
            Item item = bag.get(cfg.id);
            if (item == null) {
                item = createItem(cfg);
                bag.put(item.getId(), item);
            }
            item.setCount(item.getCount() + count);
        } else {
            for (int i = 0; i < count; i++) {
                Item item = createItem(cfg);
                item.setCount(1);
                bag.put(item.getId(), item);
            }
        }
    }

    private Item createItem(ToolCfg cfg) {
        Item item;
        switch (cfg.id) {
            case ItemConsts.RUN_ITEM_ID:
                item = new RunItem();
                break;
            case ItemConsts.FAT_WATER_ITEM_ID:
                item = new FatWaterItem();
                break;
            case ItemConsts.GUARD_ITEM_TYPE:
                item = new GuardItem();
                break;
            case ItemConsts.SOFA_ITEM_ID:
                item = new SofaLyItem();
                break;
            case ItemConsts.TREASURE_ITEM_ID:
                item = new TreasureItem();
                break;
            default:
                item = new Item();
                break;
        }

        item.setCreateTime(System.currentTimeMillis());
        item.setConfigId(cfg.id);
        item.setCount(0);
        item.setId(idGen.incrementAndGet());

        return item;
    }


    public boolean checkEnough(int id, int count) {
        Item item = bag.get(id);
        if (item == null) {
            return false;
        }

        if (item.getCount() < count) {
            return false;
        }
        return true;
    }

    public boolean checkAndRemoveCount(int id, int count) {
        Item item = bag.get(id);
        if (item == null) {
            return false;
        }

        if (item.getCount() < count) {
            return false;
        }

        item.setCount(item.getCount() - count);
        return true;
    }

    public Item getById(int id) {
        return bag.get(id);
    }

    public Item getByConfID(int confID) {
        for (Item item : bag.values()) {
            if (item.getConfigId() == confID) {
                return item;
            }
        }
        return null;
    }
}