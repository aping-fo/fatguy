package com.game.domain.bag;

import com.game.data.ToolCfg;
import com.game.domain.player.Player;
import com.game.sdk.utils.ErrorCode;

/**
 * Created by lucky on 2019/3/13.
 * 肥仔水
 */
public class FatWaterItem extends Item {
    @Override
    public String useItem(Player player) {
        if (checkUsdGuradItem(player)) {
            return ErrorCode.ITEM_GUARDING;
        }
        ToolCfg config = getConf();
        player.getPlayerDataObject().addBuffer(config);

        return ErrorCode.OK;

    }
}
