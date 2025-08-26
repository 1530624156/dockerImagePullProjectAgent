package com.mavis.digg_agent.logic;

import com.mavis.digg_agent.service.SysConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BaseLogic {
    @Resource
    private SysConfigService sysConfigService;

    /**
     * 获取系统参数
     * @param key 系统参数key
     * @return 系统参数value
     */
    public String getSysValue(String key) {
        return sysConfigService.getConfigValue(key);
    }


}
