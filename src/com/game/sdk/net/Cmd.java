package com.game.sdk.net;

/**
 * Created by lucky on 2018/10/11.
 */
public class Cmd {
    //请求openid
    public static final int GET_OPENID = 1001;
    //请求创建角色
    public static final int CREATE_ROLE = 1002;
    //请求角色数据
    public static final int GET_ROLE = 1003;
    //更新角色信息
    public static final int UPDATE_ROLE = 1004;
    //请求角色详细数据
    public static final int GET_ROLE_DETAIL = 1005;
    //跑步数据
    public static final int GET_RUN_DATA = 1007;
    //给予步数
    public static final int GIVE_RUN_DATA = 1009;
    //收获步数球
    public static final int GAIN_STEPBALL = 1008;
    //查询步数球
    public static final int GET_STEPBALLS = 1010;
    //兑换给予步数
    public static final int TRANSFER_GIVE_RUN_DATA = 1011;
    //获取附近组信息
    public static final int GET_GROUPS_INFO_NEARBY = 4011;


    //积分商城物品列表
    public static final int IntegrationMall_ITEMS = 5000;
    //积分商城物品兑换
    public static final int IntegrationMall_CONSUME = 5001;
    //参与购买
    public static final int BUY = 5002;
    //获取得奖者信息
    public static final int GET_REWEARDER = 5003;
    //获取参与者人数
    public static final int GET_PARTICIPANT_COUNT = 5004;
    //获取公告
    public static final int GET_BROADCAST = 6001;

    //重载配置
    public static final int ADMIN_RELOAD_CFG = 10001;


    //背包道具
    public static final int USE_ITEM = 6500;
    //获取背包信息
    public static final int GET_BAG = 6501;
}