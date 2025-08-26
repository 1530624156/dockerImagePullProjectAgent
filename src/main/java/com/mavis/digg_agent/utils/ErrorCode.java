package com.mavis.digg_agent.utils;

/**
 * ErrorCode
 *
 * @author Mavis郭逸轩
 * @since 2024/5/6 10:23
 */
public enum ErrorCode implements EnumBase {

    /**
     * 上传文件不存在
     */
    FILE_NOT_EXIST("file_not_exist", "上传文件不存在"),

    ;

    private final String code;

    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

