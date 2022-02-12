package com.chat.util;

/**
 * 消息类型
 */
public class Util {
    public static final String USER_PROPERTIES = "user.properties"; // 用户信息文件
    public static final String USERNAME = "username"; // 用户名参数
    public static final String STATE = "state"; // 验证的状态参数

    public static final int ONLINE = 1; // 上线
    public static final int OUTLINE = 2; // 下线
    public static final int ON_LIST = 3; // 在线列表
    public static final int NORMAL_MSG = 4; // 普通消息
    public static final int PUBLIC_MSG = 5; // 公共消息

    public static final int LOG_SUCCEED = 6; // 登录成功
    public static final int LOG_FAIL = 7; // 登录失败
    public static final int REG_SUCCEED = 8; // 注册成功
    public static final int REG_FAIL = 9; // 注册失败
    public static final int LOG_EXIST = 10; // 已经被登录
    public static final int ERROR = 20; // 出错
}
