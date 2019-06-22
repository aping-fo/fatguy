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
    public boolean useItem(Player player) {
        try {
            player.SelfLock.lock();
            ToolCfg config = getConf();
            player.getPlayerDataObject().addBuffer(config);
            return true;
        } finally {
            player.SelfLock.unlock();
        }
    }


    public ToolCfg getConf() {
        ToolCfg config = ConfigData.getConfig(ToolCfg.class, configId);
        return config;
    }

}
