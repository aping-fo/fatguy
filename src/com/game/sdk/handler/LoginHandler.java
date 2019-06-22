package com.game.sdk.handler;

import com.game.SysConfig;
import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.http.HttpClient;
import com.game.sdk.net.Result;
import com.game.sdk.proto.OpenIDReq;
import com.game.sdk.net.Cmd;
import com.game.sdk.utils.ErrorCode;

/**
 * Created by lucky on 2018/10/11.
 */
@Handler
public class LoginHandler {
    @Command(cmd = Cmd.CHECK_VERSION, description = "检查版本")
    public Result checkVersion(String openId, String version) throws Exception {

        if(!version.equals(SysConfig.version)){
            return Result.valueOf(ErrorCode.VERSION_ERROR);
        }

        return Result.valueOf(ErrorCode.OK);
    }

}
