package com.game.domain.bag;

import com.game.data.ToolCfg;
import com.game.domain.player.Player;
import com.game.sdk.utils.ErrorCode;

/**
 * Created by lucky on 2019/3/13.
 * 宝箱
 */
public class TreasureItem extends Item {
    @Override
    public String useItem(Player player) {

        ToolCfg config = getConf();
        player.getPlayerDataObject().addBuffer(config);
        return ErrorCode.OK;
    }
}
