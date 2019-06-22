package com.game.sdk.utils;

/**
 * Created by lucky on 2018/10/11.
 */
public class ErrorCode {
    //成功
    public static final String OK = "200";

    //服务器内部错误
    public static final String SERVER_INTERNAL_ERROR = "500";

    //参数错误
    public static final String PARAM_ERROR = "501";

    //签名错误
    public static final String SIGN_ERROR = "502";

    //找不到对应的处理接口
    public static final String EXEC_ERROR = "503";

    //角色不存在
    public static final String ROLE_NOT_EXIST = "504";
    //版本错误
    public static final String VERSION_ERROR = "505";
    //赠送达到上限
    public static final String GIVE_LIMIT = "507";

    //已经是好友
    public static final String FRIEND_ALREADY_IS = "600";
    //不是好友
    public static final String NOT_FRIEND = "601";

    //邮件或附件不存在
    public static final String MAIL_OR_ATTACHMENT_NOT_EXIST = "700";

    //健康币不足
    public static final String MALL_STEP_NOT_ENOUGH = "900";
    //最大人数参与
    public static final String MALL_MAX_COUNT = "901";
    //不是中奖人
    public static final String MALL_NOT_REWARDER = "902";
    //重复填写
    public static final String MALL_ALREADY_CONSUME = "903";
    //重复签到
    public static final String REPEAT_CHECK_IN = "1000";

    ////道具相关
    public static final String ITEM_NOT_EXIST = "1500";
    //保护罩中
    public static final String ITEM_GUARDING = "1501";
    //BOX 不存在
    public static final String BOX_NOT_EXIST = "1502";
    //未到领奖时间
    public static final String BOX_CANNOT_REWARD = "1503";
    //有宝箱正在开启
    public static final String BOX_ALREADY_REWARD = "1504";
    //宝箱还没打开
    public static final String BOX_NOT_OPEN = "1505";
    //有宝箱正在开启
    public static final String BOX_ALREADY_OPEN = "1506";
    //宝箱不能打开
    public static final String BOX_CANT_OPEN = "1507";
    //道具不足
    public static final String ITEM_NOT_ENOUGH = "1510";

    //奖劵不足
    public static final String TICKET_NOT_ENOUGH = "1600";
    public static final String LUCK_DRAW_TIME = "1601";

    //任务不存在
    public static final String TASK_NOT_EXIST = "1700";
    //任务没完成
    public static final String TASK_UNFINISH = "1701";
    //任务已经完成
    public static final String TASK_FINISHED = "1702";
    //任务已经领奖关闭
    public static final String TASK_CLOSE = "1703";
    //任务没达到要求
    public static final String TASK_DONT_REACH_REQUIRE = "1704";

}
