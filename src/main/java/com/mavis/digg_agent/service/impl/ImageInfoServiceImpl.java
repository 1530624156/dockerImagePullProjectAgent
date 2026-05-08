package com.mavis.digg_agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mavis.digg_agent.entity.po.ImageInfo;
import com.mavis.digg_agent.entity.po.SysConfig;
import com.mavis.digg_agent.mapper.ImageInfoMapper;
import com.mavis.digg_agent.mapper.SysConfigMapper;
import com.mavis.digg_agent.service.ImageInfoService;
import com.mavis.digg_agent.service.SysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ImageInfoService
 *
 * @author Mavis郭逸轩
 * @since 2026/5/8 15:41
 */
@Service
public class ImageInfoServiceImpl extends ServiceImpl<ImageInfoMapper, ImageInfo>
        implements ImageInfoService {

    @Resource
    private ImageInfoMapper imageInfoMapper;

    @Override
    public int removeImageBackCount(LambdaQueryWrapper lqw) {
        return imageInfoMapper.delete(lqw);
    }
}
