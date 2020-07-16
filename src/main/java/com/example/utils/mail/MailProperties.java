package com.example.utils.mail;

import com.example.utils.logger.Log4jKit;

import java.io.IOException;
import java.util.Properties;

/**
 * @ClassName MailProperties
 * @Author yupanpan
 * @Date 2020/6/18 11:35
 */
public class MailProperties {

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(MailProperties.class.getClassLoader().getResourceAsStream("mail/mail.properties"));
        } catch (IOException e) {
            Log4jKit.error("初始化邮件配置信息失败", e);
        }
    }

    public static Properties getProperties(){
        return properties;
    }
    public static String getServiceHost(){
        return properties.getProperty("SERVICE_HOST");
    }
    public static String getPort(){
        return properties.getProperty("PORT");
    }
    public static String getProtocol(){
        return properties.getProperty("PROTOCOL");
    }
    public static String getAuthCode(){
        return properties.getProperty("AUTH_CODE");
    }
    public static String getSender(){
        return properties.getProperty("SENDER");
    }
}
