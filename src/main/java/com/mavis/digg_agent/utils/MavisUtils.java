package com.mavis.digg_agent.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;

/**
 * MavisUtils
 *
 * @author Mavis郭逸轩
 * @since 2024/5/8 15:39
 */
@Component
public class MavisUtils {

    /**
     * MD5加密方法
     */
    public String encryprMD5(String originalString){
        try {
            // 创建一个MessageDigest实例，用于MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 更新摘要以包含指定的字节
            md.update(originalString.getBytes());
            // 完成哈希计算，并返回字节数组
            byte[] digest = md.digest();
            // 将字节数组转换为十六进制字符串表示形式
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            // 输出结果
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算文件的MD5值
     * @param file
     * @return
     * @throws Exception
     */
    public String calculateMD5(MultipartFile file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream inputStream = file.getInputStream();
             DigestInputStream dis = new DigestInputStream(inputStream, md)) {
            // 读取整个文件以确保所有数据被处理
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) { /* Do nothing, just read */ }
        }

        // 完成MD5计算
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes
     * @return
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 获取随机UUID
     * @return
     */
    public String randomUUID(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-","");
        return uuid;
    }

    /**
     * 生成随机文件名称
     * @return
     */
    public String randomFileName(){
        String nowTime = MyDate.getNowTime();
        String uuid = randomUUID();
        uuid = uuid.substring(7, 12);
        String fileName = nowTime + uuid;
        fileName = fileName.replace(" ","");
        fileName = fileName.replace("-","");
        fileName = fileName.replace(":","");
        return fileName;
    }

    /**
     * 校验html是否成功
     * @param html
     * @return
     */
    public static boolean checkRestResultHtml(String html){
        JSONObject jsonObject = JSONObject.parseObject(html);
        return "success".equals(jsonObject.getString("status").toLowerCase(Locale.ROOT));
    }
}
