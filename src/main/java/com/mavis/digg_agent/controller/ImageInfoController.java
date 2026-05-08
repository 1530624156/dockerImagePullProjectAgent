package com.mavis.digg_agent.controller;

import com.mavis.digg_agent.logic.ImageInfoLogic;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ImagesInfoController
 *
 * @author Mavis郭逸轩
 * @since 2026/5/8 17:13
 */
@RestController
@RequestMapping("image")
public class ImageInfoController {
    @Resource
    private ImageInfoLogic imageInfoLogic;

    @RequestMapping("list")
    public RestResult getAllImageInfo(){
        return imageInfoLogic.getAllImageInfo();
    }

    /**
     * 删除过期镜像 每天凌晨0点执行
     * @return 删除结果
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @RequestMapping("removeExpired")
    public RestResult removeExpiredImage() {
        return imageInfoLogic.removeExpiredImage();
    }
}
