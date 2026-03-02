package com.cobazaar.common.exception;

import com.cobazaar.common.enums.ResponseEnum;
import com.cobazaar.common.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 * 统一处理微服务中的各种异常，返回标准化的异常响应
 *
 * @author cobazaar
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e, HttpServletRequest request) {
        log.warn("业务异常: URI={}, code={}, message={}",
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理服务层异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result<Void> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.warn("服务层异常: URI={}, code={}, message={}",
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（@Validated注解触发）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMsg.append(fieldError.getDefaultMessage()).append(";");
        }

        String errorMessage = errorMsg.toString();
        if (errorMessage.endsWith(";")) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
        }

        log.warn("参数校验异常: URI={}, message={}", request.getRequestURI(), errorMessage);
        return Result.paramError(errorMessage);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMsg.append(fieldError.getDefaultMessage()).append(";");
        }

        String errorMessage = errorMsg.toString();
        if (errorMessage.endsWith(";")) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
        }

        log.warn("参数绑定异常: URI={}, message={}", request.getRequestURI(), errorMessage);
        return Result.paramError(errorMessage);
    }

    /**
     * 处理约束违反异常（如@NotBlank等注解触发）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e,
            HttpServletRequest request) {
        StringBuilder errorMsg = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            errorMsg.append(violation.getMessage()).append(";");
        }

        String errorMessage = errorMsg.toString();
        if (errorMessage.endsWith(";")) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
        }

        log.warn("约束违反异常: URI={}, message={}", request.getRequestURI(), errorMessage);
        return Result.paramError(errorMessage);
    }

    /**
     * 处理JSON解析异常
     */
    @ExceptionHandler(JsonProcessingException.class)
    public Result<Void> handleJsonProcessingException(JsonProcessingException e,
            HttpServletRequest request) {
        log.warn("JSON解析异常: URI={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResponseEnum.PARAM_ERROR.getCode(), "JSON格式错误");
    }

    /**
     * 处理HTTP消息不可读异常（如请求体格式错误）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        log.warn("HTTP消息不可读异常: URI={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResponseEnum.PARAM_ERROR.getCode(), "请求数据格式错误");
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e,
            HttpServletRequest request) {
        log.warn("非法参数异常: URI={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.paramError(e.getMessage());
    }

    /**
     * 处理数组越界异常
     */
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public Result<Void> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e,
            HttpServletRequest request) {
        log.error("数组越界异常: URI={}", request.getRequestURI(), e);
        return Result.fail(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(), "数据访问越界");
    }

    /**
     * 处理类型转换异常
     */
    @ExceptionHandler(ClassCastException.class)
    public Result<Void> handleClassCastException(ClassCastException e,
            HttpServletRequest request) {
        log.error("类型转换异常: URI={}", request.getRequestURI(), e);
        return Result.fail(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(), "数据类型转换错误");
    }

    /**
     * 处理数学运算异常
     */
    @ExceptionHandler(ArithmeticException.class)
    public Result<Void> handleArithmeticException(ArithmeticException e,
            HttpServletRequest request) {
        log.error("数学运算异常: URI={}", request.getRequestURI(), e);
        return Result.fail(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(), "数学运算错误");
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e,
            HttpServletRequest request) {
        log.error("空指针异常: URI={}", request.getRequestURI(), e);
        return Result.fail(ResponseEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: URI={}", request.getRequestURI(), e);
        return Result.fail(ResponseEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: URI={}", request.getRequestURI(), e);
        return Result.fail(ResponseEnum.INTERNAL_SERVER_ERROR);
    }
}