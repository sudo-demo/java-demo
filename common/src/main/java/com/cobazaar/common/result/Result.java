package com.cobazaar.common.result;

import com.cobazaar.common.enums.ResponseEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;

/**
 * 统一响应结果封装类
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
        return new Result<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage());
    }
    
    /**
     * 成功响应带数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功响应带消息和数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResponseEnum.SUCCESS.getCode(), message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> Result<T> fail() {
        return new Result<>(ResponseEnum.FAIL.getCode(), ResponseEnum.FAIL.getMessage());
    }
    
    /**
     * 失败响应带消息
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResponseEnum.FAIL.getCode(), message);
    }
    
    /**
     * 失败响应带状态码和消息
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message);
    }
    
    /**
     * 根据ResponseEnum创建失败响应
     */
    public static <T> Result<T> fail(ResponseEnum responseEnum) {
        return new Result<>(responseEnum.getCode(), responseEnum.getMessage());
    }
    
    /**
     * 根据ResponseEnum创建失败响应（带数据）
     */
    public static <T> Result<T> fail(ResponseEnum responseEnum, T data) {
        return new Result<>(responseEnum.getCode(), responseEnum.getMessage(), data);
    }
    
    /**
     * 参数错误响应
     */
    public static <T> Result<T> paramError() {
        return fail(ResponseEnum.PARAM_ERROR);
    }
    
    /**
     * 参数错误响应（带自定义消息）
     */
    public static <T> Result<T> paramError(String message) {
        return fail(ResponseEnum.PARAM_ERROR.getCode(), message);
    }
    
    /**
     * 未授权响应
     */
    public static <T> Result<T> unauthorized() {
        return fail(ResponseEnum.UNAUTHORIZED);
    }
    
    /**
     * 业务异常响应
     */
    public static <T> Result<T> businessError(String message) {
        return new Result<>(ResponseEnum.BUSINESS_ERROR.getCode(), message);
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
        return fail(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(), message);
    }
    
    /**
     * 将Result对象转换为JSON字符串
     * @param result Result对象
     * @return JSON字符串
     */
    public static String toJson(Result<?> result) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.error("Result转JSON失败", e);
            // 返回简化的错误信息
            return "{\"code\":1006,\"message\":\"JSON序列化失败\",\"timestamp\":" + 
                   System.currentTimeMillis() + "}";
        }
    }
}