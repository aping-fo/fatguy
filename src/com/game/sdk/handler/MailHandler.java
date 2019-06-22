package com.game.sdk.handler;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Cmd;
import com.game.sdk.net.Result;
import com.game.sdk.proto.FinishTaskReq;
import com.game.sdk.proto.GetTaskRewardReq;
import com.game.service.MailService;
import com.game.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class MailHandler {
    @Autowired
    private MailService mailService;

    @Command(cmd = Cmd.GET_MAIL_LIST, description = "获取邮件列表")
    public Result getMailList(String openId) throws Exception {
        Result result = mailService.getMailList(openId);
        return result;
    }

    @Command(cmd = Cmd.GET_MAIL_ATTACHMENT, description = "领取邮件附件")
    public Result getMailAttachment(String openId, int id) throws Exception {
        Result result = mailService.getMailAttachment(openId, id);
        return result;
    }

}
