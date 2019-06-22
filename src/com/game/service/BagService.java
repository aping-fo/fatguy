package com.game.service;

import com.game.data.ToolCfg;
import com.game.domain.bag.Bag;
import com.game.domain.bag.Item;
import com.game.domain.mail.MailConsts;
import com.game.domain.player.Player;
import com.game.domain.player.TrendsConsts;
import com.game.domain.task.TaskConsts;
import com.game.sdk.net.Result;
import com.game.sdk.utils.ErrorCode;
import com.game.util.ConfigData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lucky on 2019/3/13.
 * 背包道具管理
 */
@Service
public class BagService {
    private static Logger logger = Logger.getLogger(BagService.class);

    @Autowired
    private PlayerService playerService;
    @Autowired
    private TaskService taskService;

    /**
     * 增加道具
     *
     * @param openid
     * @param itemId
     * @param count
     */
    public void addItem(String openid, int itemId, int count) {
        Player player = playerService.getPlayer(openid);
        Bag bag = player.getPlayerDataObject().getBag();
        ToolCfg config = ConfigData.getConfig(ToolCfg.class, itemId);

        bag.addItem(config, count);

        playerService.saveOrUpdate(player, false);
    }


    /**
     * 使用道具
     *
     * @param openid
     * @param id
     * @param count
     */
    public Result useItem(String openid, int id, int count, String targetOpenid) {
        Player player = playerService.getPlayer(openid);
        Bag bag = player.getPlayerDataObject().getBag();

        Item item = bag.getById(id);
        if (item == null) {
            return Result.valueOf(ErrorCode.ITEM_NOT_EXIST);
        }

        Player target = playerService.getPlayer(targetOpenid);
        if (!bag.checkAndRemoveCount(id, count)) {
            return Result.valueOf(ErrorCode.ITEM_NOT_ENOUGH);
        }

        //先update步数和卡路里，调整buff顺序再去使用道具
        playerService.updateTickets(targetOpenid, false);
        item.useItem(target);

        if(!target.getOpenId().equals(openid)){
            playerService.addInformation(target.getOpenId(), openid, TrendsConsts.USE_TOOLITEM, item.getConf().name);
            target.getPlayerDataObject().getMailBox().addMail(MailConsts.MAILTYPE_USE_TOOLITEM, player.getNickName(), item.getConf().name, player.getAvatarUrl());
        }

        taskService.finishTask(openid, TaskConsts.TASK_TYPE_USETOOL, 1);

        playerService.saveOrUpdate(target, false);

        return Result.valueOf(ErrorCode.OK);
    }

    /**
     * 根据配置获取道具列表
     *
     * @return
     */
    public List<Item> getItemByConfid(String openid, int confid) {
        Player player = playerService.getPlayer(openid);
        Bag bag = player.getPlayerDataObject().getBag();

        List<Item> items = Lists.newArrayList();
        for (Item item : bag.getBag().values()) {
            if (item.getConfigId() == confid) {
                items.add(item);
            }
        }

        return items;
    }


    /**
     * 获取背包信息
     *
     * @param openid
     * @return
     */
    public Result getBag(String openid) {
        Player player = playerService.getPlayer(openid);
        Bag bag = player.getPlayerDataObject().getBag();

        List<Item> items = Lists.newArrayList();
        for (Item item : bag.getBag().values()) {
            if (item.getCount() <= 0) {
                bag.getBag().remove(item.getId());
                continue;
            }
            items.add(item);
        }

        Map<String, Object> resp = Maps.newHashMap();
        resp.put("items", items);
        return Result.valueOf(ErrorCode.OK, resp);
    }
}
