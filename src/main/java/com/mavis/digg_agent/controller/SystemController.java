package com.mavis.digg_agent.controller;

import com.mavis.digg_agent.utils.RestResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController
 *
 * @author Mavis郭逸轩
 * @since 2025/8/26 21:17
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @GetMapping("/test")
    public RestResult testSystem(){
        return RestResult.success("节点测试成功");
    }
}
