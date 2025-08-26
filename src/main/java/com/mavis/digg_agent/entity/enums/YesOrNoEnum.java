package com.mavis.digg_agent.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNoEnum {
    YES("1",1),
    NO("0",0);

    private String value;

    private Integer code;
}
