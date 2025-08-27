package com.mavis.digg_agent.controller;

import com.mavis.digg_agent.entity.param.DockerImagesParam;
import com.mavis.digg_agent.logic.DockerImagesLogic;
import com.mavis.digg_agent.utils.RestResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * DockerImagesController
 * 描述: Docker镜像控制器
 * @author Mavis郭逸轩
 * @since 2025/8/27 9:56
 */
@RestController
@RequestMapping("docker")
public class DockerImagesController {
    @Resource
    private DockerImagesLogic dockerImagesLogic;

    @RequestMapping(value = "pull", method = {RequestMethod.GET,RequestMethod.POST})
    public RestResult pullDockerImages(@RequestBody DockerImagesParam param){
        return dockerImagesLogic.pullDockerImages(param);
    }
}
