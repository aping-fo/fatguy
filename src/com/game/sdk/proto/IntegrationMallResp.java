package com.game.sdk.proto;

import com.game.data.MallCfg;
import com.google.common.collect.Lists;

import java.util.List;

public class IntegrationMallResp {
    /**
     * 积分兑换列表
     */
    private List<MallCfg> mallItemList = Lists.newArrayList();

    public List<MallCfg> getMallItems() {
        return mallItemList;
    }

    public void setMallItems(List<MallCfg> mallItemList) {
        this.mallItemList = mallItemList;
    }
}
