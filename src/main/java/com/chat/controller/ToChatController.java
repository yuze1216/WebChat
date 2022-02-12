package com.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author:yuze
 * @description:登陆成功跳转到聊天页面
 * @data:2021/11/20
 */
@Controller
@RequestMapping("/chat")
public class ToChatController {
    @Autowired
    private HttpSession httpSession;
     //进入聊天页面
    @RequestMapping("/tochat/{username}")
    public ModelAndView chat(@PathVariable String username) {
        if(httpSession.getAttribute("username") == null){
            ModelAndView modelAndView = new ModelAndView("loginController");
            modelAndView.addObject("username",username);
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("chat");
        return modelAndView;
    }
}
