package com.example.utils.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @ClassName MailUtils
 * @Author yupanpan
 * @Date 2020/6/18 10:37
 */
public class MailUtils {

    private static JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    static {
        javaMailSender.setHost(MailProperties.getServiceHost());
        javaMailSender.setPort(Integer.parseInt(MailProperties.getPort()));
        javaMailSender.setUsername(MailProperties.getSender());
        javaMailSender.setPassword(MailProperties.getAuthCode());
        Properties mp = new Properties();
        mp.setProperty("mail.host", MailProperties.getServiceHost());
        mp.setProperty("mail.transport.protocol", MailProperties.getProtocol());
        mp.setProperty("mail.smtp.port", MailProperties.getPort());
        mp.setProperty("mail.smtp.socketFactory.port", MailProperties.getPort());
        // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确  
        mp.setProperty(" mail.smtp.auth ", "true");
        mp.setProperty("mail.smtp.starttls.enable", "true");
        //使用ssl协议来保证连接安全
        mp.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mp.setProperty(" mail.smtp.timeout ", "25000");
        javaMailSender.setJavaMailProperties(mp);
    }

    /**
     *  * 发送简单邮件
     *  * @param accounts  被发邮件的用户数组
     *  * @param info      邮件内容信息
     *  * @param title     邮件主题
     * <p>
     */

    public static void sendSimpleMail(String info, String title,String... accounts) {
        //创建简单邮件对象
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(accounts);
        //设置邮件的发送者
        mailMessage.setFrom(MailProperties.getSender());
        mailMessage.setSubject(title);
        mailMessage.setText(info);
        javaMailSender.send(mailMessage);

    }
}
