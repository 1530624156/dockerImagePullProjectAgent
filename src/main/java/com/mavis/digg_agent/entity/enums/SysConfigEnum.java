package com.mavis.digg_agent.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysConfigEnum {
    SERVERNAME("sys.servername"),
    STATUS("sys.status"),
    USERNAME("sys.username"),
    PASSWORD("sys.password");

    private String value;
}
