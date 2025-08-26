package com.mavis.digg_agent.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: SimpleBlog
 * @description: Rest api请求结果统一封装对象
 * @author: <a href="http:mavis01.top:88">Mavis郭逸轩</a>
 * @create: 2024-02-20 10:42
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class RestResult<T> {
    public static final Boolean OPT_SUCCESS_STATUS = true;
    public static final Boolean OPT_ERROR_STATUS = false;

    private Boolean status;

    private String msg;

    private Long count;

    private T data;

    private int code;

    public RestResult(Boolean status, String msg, Long count, T data) {
        this.status = status;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public RestResult(String msg, Long count, T data, int code) {
        this.msg = msg;
        this.count = count;
        this.data = data;
        this.code = code;
    }

    public static <T> RestResult<T> success(T t,Long count,String msg,int code) {
        return new RestResult<>(OPT_SUCCESS_STATUS, msg, count, t, code);
    }
    public static <T> RestResult<T> success(T t) {
        return new RestResult<>(OPT_SUCCESS_STATUS, "ok", 0L, t);
    }

    public static <T> RestResult<T> success() {
        return new RestResult<>(OPT_SUCCESS_STATUS, "ok", 0L, null);
    }

    public static <T> RestResult<T> success(T t, Long count) {
        return new RestResult<>(OPT_SUCCESS_STATUS, "ok", count, t);
    }

    public static <T> RestResult<T> successPage(T t, Long count) {
        return new RestResult<>(OPT_SUCCESS_STATUS, "ok", count, t);
    }

    public static <T> RestResult<T> fail(String errorCode, String errorMessage) {
        return new RestResult<>(OPT_ERROR_STATUS, errorMessage, 0L, null);
    }

    public static <T> RestResult<T> fail(String errorMessage) {
        return new RestResult<>(OPT_ERROR_STATUS, errorMessage, 0L, null);
    }

    public static <T> RestResult<T> fail(EnumBase errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> RestResult<T> failList(EnumBase errorCode, T t) {
        return new RestResult<>(OPT_ERROR_STATUS, errorCode.getMessage(), 0L, t);
    }

    public boolean isStatus() {
        return OPT_SUCCESS_STATUS.equals(this.status);
    }

    public static <T> RestResult<T> neutrality(boolean b) {
        if (b){
            return success();
        }else {
            return fail("操作失败");
        }
    }
}