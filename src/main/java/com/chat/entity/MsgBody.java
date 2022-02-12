package com.chat.entity;
import com.chat.util.Util;
import lombok.Data;
/**
 * @author:yuze
 * @description:消息实体类
 * @data:2021/11/13
 */
@Data
public class MsgBody {

    public int msgType = Util.NORMAL_MSG; // 消息类型，默认为普通消息
    public String username ; // 用户名称
    public String content; // 消息内容
    public String userList; // 用户列表
    public String from; // 发送用户
    public String to; // 接收用户
    public String date; // 发送时间

}
