package com.roker.springbootminio.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class R implements Serializable {

    private int code;

    private String message;

    private Object data;

    private R() {
    }

    private R(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static R success() {
        return success("操作成功");
    }


    public static R success(String message,Object data) {
        return new R(200,message,data);
    }
    public static R success(Object data) {
        return new R(200,"操作成功",data);
    }

    public static R error() {
        return new R(500,"操作失败",null);
    }
    public static R error(String message) {
        return new R(500,message,null);
    }
}
