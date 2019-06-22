package com.game.service;

import com.game.sdk.net.Result;
import com.game.sdk.utils.ErrorCode;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BroadcastService extends AbstractService {
    private final int CONTENT_SIZE = 30;
    private List<String> lists = Lists.newArrayListWithCapacity(CONTENT_SIZE);

    public Result getBroadcast(){
        if(lists == null){
            return Result.valueOf(ErrorCode.SERVER_INTERNAL_ERROR, "error");
        }

        return Result.valueOf(ErrorCode.OK, lists);
    }

    private void addEvent(String content){
        lists.add(0, content);
        if (lists.size() > CONTENT_SIZE) {
            lists.remove(CONTENT_SIZE);
        }
        lists.add(content);
    }
}