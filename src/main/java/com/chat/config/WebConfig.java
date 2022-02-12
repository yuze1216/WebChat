package com.chat.config;


import com.chat.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author:yuze
 * @description:注册拦截器demo
 * @data:2021/9/1
 */
@Configuration//表明这是配置类，并交给Spring管理
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/webSocket/**")
                // 哪些路径不拦截
                .excludePathPatterns("/test/**").excludePathPatterns("/chat/**");
    }

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }
}
