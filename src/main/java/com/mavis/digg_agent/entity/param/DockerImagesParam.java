package com.mavis.digg_agent.entity.param;

import lombok.Data;

/**
 * DockerImagesParam
 * 描述: Docker镜像入参
 * @author Mavis郭逸轩
 * @since 2025/8/27 10:00
 */
@Data
public class DockerImagesParam {
    private String imageName;
    private String imageTag;
}
