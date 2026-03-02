package com.cobazaar.gateway.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;

/**
 * 网关统一响应结果封装类
 * 用于网关内部异常处理，返回统一的JSON格式响应
 */
@Slf4j
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public Result(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success");
    }
    
    /**
     * 成功响应带数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    
    /**
     * 成功响应带消息和数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> Result<T> fail() {
        return new Result<>(500, "failed");
    }
    
    /**
     * 失败响应带消息
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message);
    }
    
    /**
     * 失败响应带状态码和消息
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message);
    }
    
    /**
     * 错误响应（别名方法，保持向后兼容）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return fail(code, message);
    }
    
    /**
     * 错误响应（默认错误码）
     */
    public static <T> Result<T> error(String message) {
        return fail(500, message);
    }
}
