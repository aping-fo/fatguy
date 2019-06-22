package com.game.service;

import com.game.dao.ServerDataDAO;
import com.game.domain.ServerData;
import com.game.domain.player.Trend;
import com.game.util.JsonUtils;
import com.game.util.TimeUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by lucky on 2019/3/16.
 */
@Service
public class ServerDataService extends AbstractService {

    @Autowired
    private ServerDataDAO serverDataDAO;
    private ServerData serverData;
    private ScheduledExecutorService schedule;

    @Autowired
    private LuckdrawService luckdrawService;

    private final int TRENDS_MAX_COUNT = 50;

    @Override
    public void onStart() {
        byte[] data = serverDataDAO.queryServerData();
        if (data != null) {
            String json = new String(data, UTF_8);
            serverData = JsonUtils.string2Object(json, ServerData.class);
        }

        if(serverData == null){
            serverData = new ServerData();
            saveServerData();
        }

        schedule = Executors.newScheduledThreadPool(1);
        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                saveServerData();
            }
        }, 5, 5, TimeUnit.MINUTES);

        //抽奖信息初始化
        luckdrawService.init();
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void saveServerData() {
        String json = JsonUtils.object2String(serverData);
        serverDataDAO.saveOrUpdate(888, json.getBytes(UTF_8));
    }
    /**
     * 增加动态
     */
    public void addTrends(String openId, String nickName, String avatarUrl, int cfgId, Object... params){
        Trend trend = null;
        List<Trend> trends = serverData.getTrends();
        if (trends.size() > TRENDS_MAX_COUNT) {
            trend = trends.remove(TRENDS_MAX_COUNT);
        }

        if(trend == null){
            trend = new Trend();
        }
        trend.setOpenId(openId);
        trend.setNickName(nickName);
        trend.setAvatarUrl(avatarUrl);
        trend.setCfgId(cfgId);
        trend.setTime(TimeUtil.getCurrentSeconds());
        trend.setParams(Lists.newArrayList(params));
        trends.add(0, trend);
    }
}
