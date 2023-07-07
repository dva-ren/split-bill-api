package com.dvaren.bill.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum StatusCodeEnum {

    SUCCESS(200,"请求成功"),
    FAILED(400,"请求失败"),
    SERVER_ERROR(500, "服务器错误"),
    ARGUMENTS_ERROR(401,"参数错误"),
    PERMISSION_DENIED(405, "权限不足");

    private final int code;
    private final String msg;

    StatusCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
