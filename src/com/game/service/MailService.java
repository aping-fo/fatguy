package com.game.service;

import com.game.domain.mail.Mail;
import com.game.domain.mail.MailBox;
import com.game.domain.player.Player;
import com.game.domain.player.PlayerData;
import com.game.sdk.net.Result;
import com.game.sdk.proto.vo.MailVO;
import com.game.sdk.utils.ErrorCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 邮件
 */
@Service
public class MailService extends AbstractService {
    @Autowired
    private PlayerService playerService;

    public Result getMailList(String openId){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        boolean hasNewMail = false;
        MailBox mailBox = player.getPlayerDataObject().getMailBox();
        List<MailVO> list = Lists.newArrayList();
        for(Mail mail : mailBox.getMap().values()){
            if(!mail.isRead()){
                hasNewMail = true;
            }
            list.add(mail.toProto());

            mail.setRead(true);
        }

        Map<String, Object> resp = Maps.newHashMapWithExpectedSize(2);
        resp.put("list", list);
        resp.put("hasNewMail", hasNewMail);

        return Result.valueOf(ErrorCode.OK, resp);
    }

    public Result getMailAttachment(String openId, int id){
        Player player = playerService.getPlayer(openId);
        if(player == null){
            return Result.valueOf(ErrorCode.PARAM_ERROR);
        }

        PlayerData playerData = player.getPlayerDataObject();
        MailBox mailBox = playerData.getMailBox();
        Mail mail = mailBox.getMap().getOrDefault(id, null);

        if(mail == null || mail.getRewards() == null || mail.isGetReward()){
            return Result.valueOf(ErrorCode.MAIL_OR_ATTACHMENT_NOT_EXIST);
        }

        Map<Integer, Integer> rewards = mail.getRewards();
        for(Integer treasureBoxCfgId : rewards.keySet()){
            playerData.getTreasureWarehouse().addTreasureBox(treasureBoxCfgId, rewards.get(treasureBoxCfgId));
        }
        mail.setGetReward(true);

        return Result.valueOf(ErrorCode.OK);
    }
}
