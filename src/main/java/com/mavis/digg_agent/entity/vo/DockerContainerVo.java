package com.mavis.digg_agent.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Docker容器对象vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DockerContainerVo {

    /**
     * 容器id
     */
    private String id;
    /**
     * 容器名称
     */
    private String name;
    /**
     * 镜像名
     */
    private String imageName;

    /**
     * 映射端口
     */
    private Map<Integer,Integer> ports;

    /**
     * 容器分配的ip
     */
    private String containerAddr;
}
