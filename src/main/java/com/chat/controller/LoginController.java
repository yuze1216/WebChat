package com.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:yuze
 * @description:跳转登录首页
 * @data:2021/11/14
 */
@Controller
@RequestMapping("/")
public class LoginController {
    @RequestMapping("/")
    public String index() {
        return "login";
    }
}
