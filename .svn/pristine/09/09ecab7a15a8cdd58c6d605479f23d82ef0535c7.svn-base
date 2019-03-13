package com.game.domain.bag;

import com.game.data.ToolCfg;
import com.game.domain.buffer.Buffer;
import com.game.domain.player.Player;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;

import java.util.Map;

/**
 * Created by lucky on 2019/3/12.
 */
public class Item {
    private int id;
    //配置ID
    private int configId;

    private int count;
    /**
     * 创建时间
     */
    private long createTime;


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }


    /**
     * 道具使用
     *
     * @param player
     * @return 错误码信息
     */
    public String useItem(Player player) {

        return ErrorCode.OK;
    }

    public boolean checkUsdGuradItem(Player player) {
        Map<Integer, Buffer> effectMap = player.getPlayerDataObject().getBufferMap();
        Buffer buffer = effectMap.get(ItemConsts.GUARD_ITEM_ID);

        int passTime = (int) (System.currentTimeMillis() / 1000) - buffer.getCdStart();

        if (passTime >= buffer.getCdtime()) {
            effectMap.remove(buffer.getId());
            return false;
        }

        return true;
    }

    public ToolCfg getConf() {
        ToolCfg config = ConfigData.getConfig(ToolCfg.class, configId);
        return config;
    }

}
