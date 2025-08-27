package com.mavis.digg_agent.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysConfigEnum {
    DOCKER_IP("docker.ip"),
    DOCKER_PORT("docker.port"),
    CLASH_SHELL_PATH("clash.shellPath"),
    SERVERNAME("sys.servername"),
    STATUS("sys.status"),
    USERNAME("sys.username"),
    PASSWORD("sys.password");


    private String value;
}
