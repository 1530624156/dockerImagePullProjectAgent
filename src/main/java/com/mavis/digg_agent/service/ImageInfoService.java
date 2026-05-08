package com.mavis.digg_agent.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mavis.digg_agent.entity.po.ImageInfo;

/**
 * ImageInfoService
 *
 * @author Mavis郭逸轩
 * @since 2026/5/8 15:41
 */
public interface ImageInfoService extends IService<ImageInfo> {

    int removeImageBackCount(LambdaQueryWrapper lqw);
}
