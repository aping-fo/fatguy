package com.game.sdk.proto.vo;

import org.omg.CORBA.INTERNAL;

import java.util.Map;

public class ActivityVO {
    private int id;
    private Map<Integer, Integer> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }
}
