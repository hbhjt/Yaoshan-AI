package com.yaoshan.backend.common;


import lombok.Data;

@Data
public class Result<T> {
    private Integer code;   // 状态码
    private String message; // 提示信息
    private T data;         // 返回数据

    // 成功返回
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    // 成功但无数据
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败返回
    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(message);
        r.setData(null);
        return r;
    }

    // 自定义状态码
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(null);
        return r;
    }
}
