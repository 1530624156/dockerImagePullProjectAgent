package com.mavis.digg_agent.utils;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.setting.dialect.Props;
import com.mavis.digg_agent.logic.SysConfigLogic;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

@Component
public class MailUtils {
    public static MailAccount account = new MailAccount();
    @Resource
    private SysConfigLogic sysConfigLogic;

    @PostConstruct
     public void init(){
        // 读取配置文件
         Props props = new Props("mail.config");
         Map<String, String> allConfig = sysConfigLogic.getAllConfigToMap();
         account = new MailAccount();
         // 邮件配置
         account.setFrom(allConfig.get("mail.from")); // 发件人邮箱
         account.setUser(allConfig.get("mail.user")); // 发件人用户名
         account.setPass(allConfig.get("mail.pass")); // 发件人密码
         account.setHost(props.getStr("mail.host")); // 设置邮件服务器地址
         account.setPort(props.getInt("mail.port")); // 设置邮件服务器端口
         account.setAuth(props.getBool("mail.auth")); // 是否需要身份认证
         account.setSslEnable(props.getBool("mail.sslEnable"));// 是否需要ssl认证
    }

    /**
     * 发送HTML邮件
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param html    HTML内容
     */
    public static void sendHtmlMail(String to, String subject, String html) {
        MailUtil.send(account, to, subject, html, true);
    }

    /**
     * 发送HTML邮件（多个收件人）
     * @param tos     收件人邮箱列表
     * @param subject 邮件主题
     * @param html    HTML内容
     */
    public static void sendHtmlMail(String[] tos, String subject, String html) {
        MailUtil.send(account, java.util.Arrays.asList(tos), subject, html, true);
    }
}
