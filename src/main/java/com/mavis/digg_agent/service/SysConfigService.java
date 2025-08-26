package com.mavis.digg_agent.service;

import com.mavis.digg_agent.entity.po.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 15306
* @description 针对表【sys_config】的数据库操作Service
* @createDate 2025-08-27 01:01:39
*/
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 获取配置值
     * @param configKey 配置键
     * @return 配置值
     */
    String getConfigValue(String configKey);
}
