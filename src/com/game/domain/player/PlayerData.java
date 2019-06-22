package com.game.domain.player;

import com.game.data.ToolCfg;
import com.game.domain.activity.ActivityData;
import com.game.domain.bag.Bag;
import com.game.domain.buffer.Buffer;
import com.game.domain.mail.MailBox;
import com.game.domain.task.Task;
import com.game.domain.treasure.TreasureWarehouse;
import com.game.sdk.proto.vo.TicketBallVO;
import com.game.util.TimeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * Created by lucky on 2019/3/12.
 * 玩家相关数据相关
 */
public class PlayerData {
    private final int TRENDS_MAX_COUNT = 30;

    /**
     * 抽奖劵
     */
    private float ticket;

    /**
     *
     */
    private Map<Integer, List<TicketBallVO>> ticketBallMap = Maps.newHashMap();

    /**
     * 好友来访记录
     */
    private List<PlayerVisitor> playerVisitorRecords = Lists.newArrayList();

    /**
     * 好友赠送记录
     */
    private Map<String, Float> playerGiveRecords = Maps.newHashMap();

    /**
     * 背包
     */
    private Bag bag = new Bag();

    /**
     * 邮箱
     */
    private MailBox mailBox = new MailBox();

    /**
     * buffer
     */
    private LinkedList<Buffer> bufferList = Lists.newLinkedList();

    /**
     * 宝箱仓库
     */
    private TreasureWarehouse treasureWarehouse = new TreasureWarehouse();

    /**
     * 上次计算卡路里的时间
     */
    private int lastCalculateTime;

    /**
     * 好友列表
     */
    private List<String> friendList = Lists.newArrayList();

    /**
     * 好友请求列表
     */
    private List<String> friendRequestList = Lists.newArrayList();

    /**
     * 动态列表
     */
    private List<Trend> trends = Lists.newArrayList();

    private Map<String,List<String>> luckdrawRecords = new HashMap<>();

    /**
     * 任务列表
     */
    private Map<Integer, Task> tasks = Maps.newHashMap();

    private ActivityData activityData;

    public PlayerData() {
        ticketBallMap.put(TicketConsts.TICKET_TYPE_NORMAL, Lists.newArrayList());
        ticketBallMap.put(TicketConsts.TICKET_TYPE_GIVE, Lists.newArrayList());
        ticketBallMap.put(TicketConsts.TICKET_TYPE_TASK, Lists.newArrayList());
    }

    public ActivityData getActivityData() {
        return activityData;
    }

    public void setActivityData(ActivityData activityData) {
        this.activityData = activityData;
    }

    public Map<String, List<String>> getLuckdrawRecords() {
        return luckdrawRecords;
    }
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public void setLuckdrawRecords(Map<String, List<String>> luckdrawRecords) {
        this.luckdrawRecords = luckdrawRecords;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public List<Trend> getTrends() {
        return trends;
    }

    public void setTrends(List<Trend> trends) {
        this.trends = trends;
    }

    public List<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }

    public List<String> getFriendRequestList() {
        return friendRequestList;
    }

    public void setFriendRequestList(List<String> friendRequestList) {
        this.friendRequestList = friendRequestList;
    }

    public TreasureWarehouse getTreasureWarehouse() {
        return treasureWarehouse;
    }

    public void setTreasureWarehouse(TreasureWarehouse treasureWarehouse) {
        this.treasureWarehouse = treasureWarehouse;
    }

    public LinkedList<Buffer> getBufferList() {
        return bufferList;
    }

    public void setBufferList(LinkedList<Buffer> bufferList) {
        this.bufferList = bufferList;
    }

    public int getLastCalculateTime() {
        return lastCalculateTime;
    }

    public void setLastCalculateTime(int lastCalculateTime) {
        this.lastCalculateTime = lastCalculateTime;
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
    }

    public Map<Integer, List<TicketBallVO>> getTicketBallMap() {
        return ticketBallMap;
    }

    public void setTicketBallMap(Map<Integer, List<TicketBallVO>> ticketBallMap) {
        this.ticketBallMap = ticketBallMap;
    }

    public Map<String, Float> getPlayerGiveRecords() {
        return playerGiveRecords;
    }

    public void setPlayerGiveRecords(Map<String, Float> playerGiveRecords) {
        this.playerGiveRecords = playerGiveRecords;
    }

    public List<PlayerVisitor> getPlayerVisitorRecords() {
        return playerVisitorRecords;
    }

    public void setPlayerVisitorRecords(List<PlayerVisitor> playerVisitorRecords) {
        this.playerVisitorRecords = playerVisitorRecords;
    }

    public float getTicket() {
        return ticket;
    }

    public void setTicket(float ticket) {
        this.ticket = ticket;
    }

    /**
     * 增加动态
     */
    public void addTrends(String openId, String nickName, String avatarUrl, int cfgId, Object[] params){
        Trend trend = null;
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

    /**
     * 增加一个buffer
     * @param cfg
     */
    public void addBuffer(ToolCfg cfg) {
        if(cfg.during <= 0){
            return;
        }

        Buffer buffer = this.getBufferByType(cfg.type);

        if (buffer == null) {
            buffer = new Buffer();
            buffer.setCdStart(TimeUtil.getCurrentSeconds());
            buffer.setCdtime(cfg.during);
            buffer.setId(cfg.id);
            buffer.setType(cfg.type);
            buffer.setGroup(cfg.group);

            Buffer effectiveBuff = this.getEffectiveBuffer(cfg.group);
            if(cfg.type == Buffer.BUFF_TYPE_FATWATER){
                if(effectiveBuff != null){
                    effectiveBuff.setCdStart(0);
                }
                this.bufferList.addFirst(buffer);
            }else{
                if(effectiveBuff != null){
                    buffer.setCdStart(0);
                }
                this.bufferList.addLast(buffer);
            }
        }else{
            buffer.setCdtime(buffer.getCdtime() + cfg.during);
        }
    }

    public void clearAndUpdateBufferCD() {
        int now = TimeUtil.getCurrentSeconds();
        List<Buffer> removeBuffers = Lists.newArrayList();

        //clear
        for (Buffer buffer : bufferList) {
            if (buffer.checkTimeout()) {
                removeBuffers.add(buffer);
            }
        }

        bufferList.removeAll(removeBuffers);

        //update
        for(Buffer buffer: bufferList){
            if(buffer == this.getEffectiveBuffer(buffer.getGroup())){
                if(buffer.getCdStart() != 0){
                    buffer.setCdtime(buffer.getCdtime() - (now - buffer.getCdStart()));
                }
                buffer.setCdStart(now);
            }
        }
    }

    public void clearBufferCD(int id){
        for (Buffer buffer : bufferList) {
            if (buffer.getId() == id) {
                bufferList.remove(buffer);
                break;
            }
        }
    }

    public Buffer getEffectiveBuffer(int group){
        for(Buffer buffer : bufferList){
            if(buffer.getGroup() == group){
                return buffer;
            }
        }

        return null;
    }

    private Buffer getBufferByType(int type) {
        for(Buffer buffer : bufferList){
            if(buffer.getType() == type){
                return buffer;
            }
        }
        return null;
    }
}
