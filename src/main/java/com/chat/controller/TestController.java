package com.chat.controller;

import com.chat.config.CustomConfig;
import com.chat.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author:yuze
 * @description:进行登录注册验证
 * @data:2021/11/14
 */
@Controller
@RequestMapping("/test")
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private HttpSession httpSession;
    /**
     * 用于记录已经登录的用户
     */
    public static final Map<String, String> USERS = new ConcurrentHashMap<>();

    // 登录注册的验证
    @RequestMapping("/testuser")
    @ResponseBody // 返回json字符串
    public Object test(@RequestParam String type, @RequestParam String username, @RequestParam String password) {
        Map<String, Object> testMsg = new HashMap<>(4);
        try {
            String pwd = CustomConfig.USERS.getProperty(username);
            if ("login".equals(type)) {
                if (password.equals(pwd)) {
                    if (ChatController.CLIENTS.get(username) != null) {
                        LOGGER.info("{} has been login!", username);
                        testMsg.put(Util.STATE, Util.LOG_EXIST);
                    } else {
                        // 将uuid作为用户访问非登录页面的验证
                        //String uuid = UUID.randomUUID().toString();
                        testMsg.put(Util.STATE, Util.LOG_SUCCEED);
                        //testMsg.put(Util.USERNAME, uuid);
                        USERS.put(username, username);
                        LOGGER.info("{} login success!", username);
                        httpSession.setAttribute("username",password);
                        if (httpSession.getAttribute("username")==null){
                            System.out.println("没设置上");
                        }
                    }
                } else {
                    testMsg.put(Util.STATE, Util.LOG_FAIL);
                    LOGGER.info("{} login failure!", username);
                }
            } else {
                if (pwd != null) {
                    testMsg.put(Util.STATE, Util.REG_FAIL);
                    LOGGER.info("{} has been register!", username);
                } else {
                    CustomConfig.USERS.setProperty(username, password);
                    try {
                        // 将新注册用户写入数据文件
                        CustomConfig.fwUser.write(username + "=" + password + System.lineSeparator());
                        CustomConfig.fwUser.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    testMsg.put(Util.STATE, Util.REG_SUCCEED);
                    LOGGER.info("{} register success!", username);
                }
            }
        } catch (Exception e) {
            testMsg.put(Util.STATE, Util.ERROR);
            testMsg.put("error", e.toString());
            LOGGER.error("An error occurred on the server!", e);
        }
        return testMsg;
    }
}

