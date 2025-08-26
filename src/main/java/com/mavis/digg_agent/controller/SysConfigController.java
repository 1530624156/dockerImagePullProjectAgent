package com.mavis.digg_agent.controller;

import com.mavis.digg_agent.entity.param.SysConfigParam;
import com.mavis.digg_agent.logic.SysConfigLogic;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * SystemController
 * 描述: 系统配置控制层
 * @author Mavis郭逸轩
 * @since 2025/8/26 21:17
 */
@RestController
@RequestMapping("/system")
public class SysConfigController {

    @Resource
    private SysConfigLogic sysConfigLogic;

    @PostMapping("/test")
    public RestResult testSystem(@RequestBody SysConfigParam param){
        return sysConfigLogic.testSystem(param);
    }
}
