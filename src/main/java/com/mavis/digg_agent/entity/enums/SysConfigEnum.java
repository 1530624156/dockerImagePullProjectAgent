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
    PASSWORD("sys.password"),
    SYS_TAR_PATH("sys.tarPath"),
    SYS_DOWNLOAD_PATH("sys.downloadPath"),
    SYS_TASK_MAXCOUNT("sys.task.maxCount");


    private String value;
}
