package com.mavis.digg_agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mavis.digg_agent.entity.po.SysConfig;
import com.mavis.digg_agent.service.SysConfigService;
import com.mavis.digg_agent.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author 15306
* @description 针对表【sys_config】的数据库操作Service实现
* @createDate 2025-08-27 01:01:39
*/
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
    implements SysConfigService{

    @Override
    public String getConfigValue(String configKey) {
        SysConfig config = getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, configKey));
        return config == null ? null : config.getConfigValue();
    }
}




