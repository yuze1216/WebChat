package com.chat.controller;

import com.chat.entity.MsgBody;
import com.chat.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author:yuze
 * @description:处理用户连接以及推送消息
 * @data:2021/11/22
 */
@Component
@ServerEndpoint("/webSocket/{username}")
public class ChatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private static final ObjectMapper OM = new ObjectMapper();

    /**
     * 以用户名为key，会话Session为值保存
     */
    public static final Map<String, Session> CLIENTS = new ConcurrentHashMap<>();

    /**
     * 建立连接
     */
    @OnOpen
    public void onOpen(@PathParam(Util.USERNAME) String username, Session session) {
        try {
            // 当用户未登录直接进行WebSocket通信就关闭会话
            if (TestController.USERS.get(username) == null) {
                session.close();
                return;
            }
            // 给所有人发送通知新上线用户
            MsgBody mb = new MsgBody();
            mb.setMsgType(Util.ONLINE);
            mb.setUsername(username);
            sendMessageAll(OM.writeValueAsString(mb));

            // 给新加入用户发当前在线列表
            mb.setMsgType(Util.ON_LIST);
            mb.setUserList(CLIENTS.keySet().toString());
            // 把新加入用户加入到列表中
            CLIENTS.put(username, session);
            sendMessageTo(OM.writeValueAsString(mb), username);
            LOGGER.info("New userId:{},username:{},now online number:{}", session.getId(), username, CLIENTS.size());
        } catch (Exception e) {
            LOGGER.error("An error occurred on the server!", e);
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        try {
            sendMessageTo(OM.writeValueAsString(Collections.singletonMap("static/error", error)),
                    session.getPathParameters().get(Util.USERNAME));
            LOGGER.error("An error occurred on the server!", error);
        } catch (Exception e) {
            LOGGER.error("An error occurred on the server!", e);
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session) {
        try {
            String username = session.getPathParameters().get(Util.USERNAME);
            MsgBody mb = new MsgBody();
            mb.setMsgType(Util.OUTLINE);
            mb.setUsername(username);

            // 移除容器中存储的用户数据以及发消息通知其他用户
            if (TestController.USERS.get(username) != null) {
                TestController.USERS.remove(username);
            }
            if (CLIENTS.get(username) != null) {
                CLIENTS.remove(username);
            }
            sendMessageAll(OM.writeValueAsString(mb));
            LOGGER.info("{} exit!Now online number:{}", username, CLIENTS.size());
        } catch (Exception e) {
            LOGGER.error("An error occurred on the server!", e);
        }
    }

    /**
     * 收到客户端的消息
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            String from = session.getPathParameters().get(Util.USERNAME);
            MsgBody mb = OM.readValue(message, MsgBody.class);
            LOGGER.info("Message from:" + from);
            String to = mb.getTo();
            mb.setFrom(from);
            if ("All".equals(to)) {
                mb.setMsgType(Util.PUBLIC_MSG);
                sendMessageAll(OM.writeValueAsString(mb));
            } else {
                sendMessageTo(OM.writeValueAsString(mb), to);
            }
            // 将聊天记录写入文件
            //String separator = System.lineSeparator();
            LOGGER.trace("{}{}{} to {}：{}", mb.getDate(), System.lineSeparator(), from, to, mb.getContent());
        } catch (Exception e) {
            LOGGER.error("An error occurred on the server!", e);
        }

    }

    // 推送消息给某个人
    public void sendMessageTo(String message, String to) {
        Session session = CLIENTS.get(to);
        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }

    // 推送消息给所有人
    public void sendMessageAll(String message) {
        for (Session session : CLIENTS.values()) {
            session.getAsyncRemote().sendText(message);
        }
    }

}