package com.game.service;

import com.game.sdk.net.Result;
import com.game.sdk.utils.ErrorCode;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BroadcastService extends AbstractService {
    private final int CONTENT_SIZE = 30;
    private final String ARRIVEEVENT_CONTENT_FORMAT = "%s刚刚到达了%s";
    private final String PRESENTEVENT_CONTENT_FORMAT = "%s的好友给他赠送了%s步";
    private final String ACTIVITY_JOIN_EVENT_CONTENT_FORMAT = "%s参与了%s活动";
    private final String ACTIVITY_FULL_EVENT_CONTENT_FORMAT = "%s活动已经满人，即奖开奖";
    private final String ACTIVITY_AWARDL_EVENT_CONTENT_FORMAT = "%s活动中奖者是%s";
    private List<String> lists = Lists.newArrayListWithCapacity(CONTENT_SIZE);

    public Result getBroadcast(){
        if(lists == null){
            return Result.valueOf(ErrorCode.SERVER_INTERNAL_ERROR, "error");
        }

        return Result.valueOf(ErrorCode.OK, lists);
    }

    public void addArriveEvent(String name, String place){
        this.addEvent(String.format(ARRIVEEVENT_CONTENT_FORMAT, name, place));
    }

    public void addPresentEvent(String name, int step){
        this.addEvent(String.format(PRESENTEVENT_CONTENT_FORMAT, name, step));
    }

    public void addActivityJoinEvent(String name, String actName){
        this.addEvent(String.format(ACTIVITY_JOIN_EVENT_CONTENT_FORMAT, name, actName));
    }

    public void addActivityFullEvent(String actName){
        this.addEvent(String.format(ACTIVITY_FULL_EVENT_CONTENT_FORMAT, actName));
    }

    public void addActivityAwardEvent(String actName,String name){
        this.addEvent(String.format(ACTIVITY_AWARDL_EVENT_CONTENT_FORMAT, actName,name));
    }

    private void addEvent(String content){
        lists.add(0, content);
        if (lists.size() > CONTENT_SIZE) {
            lists.remove(CONTENT_SIZE);
        }
        lists.add(content);
    }
}