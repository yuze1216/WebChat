package com.chat.config;

import com.chat.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
/**
 * @author:yuze
 * @description:读取配置文件中的用户信息
 * @data:2021/11/14
 */
@Configuration
public class CustomConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomConfig.class);
    public static final Properties USERS = new Properties(); // 加载Properties文件
    public static FileWriter fwUser = null; // 用户文件输出流

    // 在Spring初始化时加载所需数据
    @PostConstruct
    public void postConstruct() {
        try {
            // 使用class文件所在目录，能够在打包后也能找到文件
            ClassPathResource cprUser = new ClassPathResource(Util.USER_PROPERTIES);
            // 获取文件是否存在，否则创建
            File userFile = new File(cprUser.getPath());
            if (!userFile.exists()) {
                userFile.createNewFile();
            }
            Reader fr = new FileReader(Util.USER_PROPERTIES);
            fwUser = new FileWriter(Util.USER_PROPERTIES, true);
            USERS.load(fr); // 加载文件映射成键值对
            fr.close();
        } catch (IOException e) {
            LOGGER.error("An error occurred on the server!", e);
        }
    }

    // 为了初始化带有ServerEndpoint注解的类
    @Bean
    public ServerEndpointExporter getServerEndpoint() {
        return new ServerEndpointExporter();
    }
}
