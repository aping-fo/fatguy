package com.game.sdk.net;

/**
 * Created by lucky on 2018/10/11.
 */
public class Cmd {
    //版本号比较
    public static final int CHECK_VERSION = 1000;
    //请求openid
    public static final int GET_OPENID = 1001;
    //请求创建角色
    public static final int CREATE_ROLE = 1002;
    //登陆
    public static final int LOGIN = 1003;
    //请求角色详细数据
    public static final int GET_ROLE_DETAIL = 1005;
    //拜访
    public static final int VISIT = 1006;
    //生成奖券球
    public static final int GENERATE_TICKETBALL = 1007;
    //收获奖券球
    public static final int GAIN_TICKETBALL = 1008;
    //给予奖券
    public static final int GIVE_TICKET = 1009;
    //查询奖券球
    public static final int GET_TICKETBALLS = 1010;
    //取消buff
    public static final int CANCEL_BUFF = 1011;
    //获取动态
    public static final int GET_TRENDS = 1012;
    //获取可访问人头像信息
    public static final int GET_VISITABLE_ROLES_INFO = 1013;

    //世界排行榜
    public static final int RANK_WORLD = 2001;

    //获取公告
    public static final int GET_BROADCAST = 6001;

    //重载配置
    public static final int ADMIN_RELOAD_CFG = 10001;

    //邮件
    public static final int GET_MAIL_LIST = 6400;
    //获取邮件附件
    public static final int GET_MAIL_ATTACHMENT = 6401;

    //背包道具
    public static final int USE_ITEM = 6500;
    //获取背包信息
    public static final int GET_BAG = 6501;
    //获取宝箱列表
    public static final int GET_TREASURE_BOXS = 6510;
    //宝箱加速
    public static final int TREASURE_BOX_SPEEDUP = 6511;
    //宝箱领奖
    public static final int TREASURE_BOX_REWARD = 6512;
    //打开宝箱倒计时
    public static final int TREASURE_BOX_OPEN = 6513;

    /////friend
    public static final int FRIEND_LIST = 6610;
    public static final int FRIEND_ADD = 6611;
    public static final int FRIEND_DEL = 6612;
    public static final int FRIEND_ADD_REQUEST = 6613;
    public static final int FRIEND_ADD_REQUEST_LIST = 6614;
    public static final int FRIEND_ADD_DENY = 6615;

    //do  luckdraw
    public static final int LUCK_DRAW_RECORD = 6710;
    public static final int LUCK_DRAW_ATTEND = 6711;
    public static final int LUCK_DRAW_INFO = 6712;
    public static final int GET_ATTEND_LUCKDRAW_ROLES = 6713;

    //任务系统
    //获取任务列表
    public static final int GET_TASK_LIST = 7000;
    //领取任务奖励
    public static final int GET_TASK_REWARD = 7001;
    //完成某任务
    public static final int FINISH_TASK = 7002;

    //活动
    //获取活动信息
    public static final int GET_ACTIVITY_INFO = 8001;
    //活动兑换
    public static final int EXCHANGE_ACTIVITY = 8002;
}
