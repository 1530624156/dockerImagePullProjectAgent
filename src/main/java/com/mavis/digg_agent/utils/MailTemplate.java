package com.mavis.digg_agent.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 邮件模板工具类
 *
 * @author Mavis郭逸轩
 * @since 2026/5/8
 */
public class MailTemplate {

    private static final String IMAGE_READY_SUBJECT = "Docker镜像拉取完成通知";

    /**
     * 镜像拉取成功通知邮件HTML模板
     * @param imageName    镜像名称
     * @param imageTag     镜像标签
     * @param downloadUrl  下载地址
     * @param expTime      过期时间
     * @return HTML内容
     */
    public static String imageReadyTemplate(String imageName, String imageTag, String downloadUrl, Date expTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expTimeStr = sdf.format(expTime);
        return "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;background:#f4f4f7;'>"
                + "<div style='max-width:600px;margin:40px auto;background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.1);'>"
                // header
                + "<div style='background:#1a73e8;padding:24px 32px;'>"
                + "<h2 style='color:#ffffff;margin:0;font-size:20px;'>Docker 镜像拉取完成</h2>"
                + "</div>"
                // body
                + "<div style='padding:32px;'>"
                + "<p style='color:#333;font-size:15px;line-height:1.6;'>您好，您请求的 Docker 镜像已经拉取并打包完成，可以进行下载。</p>"
                + "<table style='width:100%;border-collapse:collapse;margin:20px 0;'>"
                + "<tr><td style='padding:10px 12px;background:#f8f9fa;color:#666;font-size:14px;width:100px;'>镜像名称</td>"
                + "<td style='padding:10px 12px;color:#333;font-size:14px;font-weight:600;'>" + imageName + "</td></tr>"
                + "<tr><td style='padding:10px 12px;background:#f8f9fa;color:#666;font-size:14px;'>镜像标签</td>"
                + "<td style='padding:10px 12px;color:#333;font-size:14px;font-weight:600;'>" + imageTag + "</td></tr>"
                + "<tr><td style='padding:10px 12px;background:#f8f9fa;color:#666;font-size:14px;'>下载地址</td>"
                + "<td style='padding:10px 12px;font-size:14px;'><a href='" + downloadUrl + "' style='color:#1a73e8;text-decoration:none;word-break:break-all;'>" + downloadUrl + "</a></td></tr>"
                + "<tr><td style='padding:10px 12px;background:#f8f9fa;color:#666;font-size:14px;'>过期时间</td>"
                + "<td style='padding:10px 12px;color:#e53935;font-size:14px;font-weight:600;'>" + expTimeStr + "</td></tr>"
                + "</table>"
                + "<div style='background:#fff8e1;border-left:4px solid #ffc107;padding:12px 16px;margin:20px 0;border-radius:0 4px 4px 0;'>"
                + "<p style='color:#795548;font-size:13px;margin:0;'>请注意在过期时间之前下载，过期后文件将被自动清理。</p>"
                + "<p style='color:#795548;font-size:13px;margin:0;'>下载地址可能被风控，可复制后粘贴到浏览器地址。</p>"
                + "</div>"
                + "</div>"
                // footer
                + "<div style='padding:16px 32px;background:#f8f9fa;text-align:center;'>"
                + "<p style='color:#999;font-size:12px;margin:0;'>此邮件由系统自动发送，请勿回复</p>"
                + "</div>"
                + "</div></body></html>";
    }

}
