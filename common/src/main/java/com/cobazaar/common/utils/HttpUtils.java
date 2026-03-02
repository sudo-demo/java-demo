package com.cobazaar.common.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.cobazaar.common.exception.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP客户端工具类
 * 基于Hutool实现HTTP请求功能
 * 
 * @author cobazaar
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpUtils {

    /**
     * 默认超时时间（毫秒）
     */
    private static final int DEFAULT_TIMEOUT = 5000;

    /**
     * GET请求
     * @param url 请求URL
     * @return 响应内容
     */
    public static String get(String url) {
        return get(url, new HashMap<>(), DEFAULT_TIMEOUT);
    }

    /**
     * GET请求（带参数）
     * @param url 请求URL
     * @param params 请求参数
     * @return 响应内容
     */
    public static String get(String url, Map<String, Object> params) {
        return get(url, params, DEFAULT_TIMEOUT);
    }

    /**
     * GET请求（带参数和超时设置）
     * @param url 请求URL
     * @param params 请求参数
     * @param timeout 超时时间（毫秒）
     * @return 响应内容
     */
    public static String get(String url, Map<String, Object> params, int timeout) {
        Assert.hasText(url, "请求URL不能为空");
        
        try {
            HttpResponse response = HttpRequest.get(url)
                    .form(params)
                    .timeout(timeout)
                    .execute();
            
            log.debug("GET请求成功: {} statusCode: {}", url, response.getStatus());
            return response.body();
        } catch (Exception e) {
            log.error("GET请求失败: {}", url, e);
            throw new ServiceException("GET请求失败: " + url, e);
        }
    }

    /**
     * POST请求（表单数据）
     * @param url 请求URL
     * @param params 表单参数
     * @return 响应内容
     */
    public static String post(String url, Map<String, Object> params) {
        return post(url, params, DEFAULT_TIMEOUT);
    }

    /**
     * POST请求（表单数据，自定义超时）
     * @param url 请求URL
     * @param params 表单参数
     * @param timeout 超时时间（毫秒）
     * @return 响应内容
     */
    public static String post(String url, Map<String, Object> params, int timeout) {
        Assert.hasText(url, "请求URL不能为空");
        
        try {
            HttpResponse response = HttpRequest.post(url)
                    .form(params)
                    .timeout(timeout)
                    .execute();
            
            log.debug("POST请求成功: {} statusCode: {}", url, response.getStatus());
            return response.body();
        } catch (Exception e) {
            log.error("POST请求失败: {}", url, e);
            throw new ServiceException("POST请求失败: " + url, e);
        }
    }

    /**
     * POST请求（JSON数据）
     * @param url 请求URL
     * @param json JSON数据
     * @return 响应内容
     */
    public static String postJson(String url, String json) {
        return postJson(url, json, DEFAULT_TIMEOUT);
    }

    /**
     * POST请求（JSON数据，自定义超时）
     * @param url 请求URL
     * @param json JSON数据
     * @param timeout 超时时间（毫秒）
     * @return 响应内容
     */
    public static String postJson(String url, String json, int timeout) {
        Assert.hasText(url, "请求URL不能为空");
        Assert.hasText(json, "JSON数据不能为空");
        
        try {
            HttpResponse response = HttpRequest.post(url)
                    .body(json)
                    .contentType("application/json")
                    .timeout(timeout)
                    .execute();
            
            log.debug("POST JSON请求成功: {} statusCode: {}", url, response.getStatus());
            return response.body();
        } catch (Exception e) {
            log.error("POST JSON请求失败: {}", url, e);
            throw new ServiceException("POST JSON请求失败: " + url, e);
        }
    }

    /**
     * PUT请求
     * @param url 请求URL
     * @param json JSON数据
     * @return 响应内容
     */
    public static String put(String url, String json) {
        return put(url, json, DEFAULT_TIMEOUT);
    }

    /**
     * PUT请求（自定义超时）
     * @param url 请求URL
     * @param json JSON数据
     * @param timeout 超时时间（毫秒）
     * @return 响应内容
     */
    public static String put(String url, String json, int timeout) {
        Assert.hasText(url, "请求URL不能为空");
        Assert.hasText(json, "JSON数据不能为空");
        
        try {
            HttpResponse response = HttpRequest.put(url)
                    .body(json)
                    .contentType("application/json")
                    .timeout(timeout)
                    .execute();
            
            log.debug("PUT请求成功: {} statusCode: {}", url, response.getStatus());
            return response.body();
        } catch (Exception e) {
            log.error("PUT请求失败: {}", url, e);
            throw new ServiceException("PUT请求失败: " + url, e);
        }
    }

    /**
     * DELETE请求
     * @param url 请求URL
     * @return 响应内容
     */
    public static String delete(String url) {
        return delete(url, DEFAULT_TIMEOUT);
    }

    /**
     * DELETE请求（自定义超时）
     * @param url 请求URL
     * @param timeout 超时时间（毫秒）
     * @return 响应内容
     */
    public static String delete(String url, int timeout) {
        Assert.hasText(url, "请求URL不能为空");
        
        try {
            HttpResponse response = HttpRequest.delete(url)
                    .timeout(timeout)
                    .execute();
            
            log.debug("DELETE请求成功: {} statusCode: {}", url, response.getStatus());
            return response.body();
        } catch (Exception e) {
            log.error("DELETE请求失败: {}", url, e);
            throw new ServiceException("DELETE请求失败: " + url, e);
        }
    }

    /**
     * 带请求头的GET请求
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应内容
     */
    public static String getWithHeaders(String url, Map<String, String> headers) {
        return getWithHeaders(url, new HashMap<>(), headers, DEFAULT_TIMEOUT);
    }

    /**
     * 带请求头的GET请求（完整版）
     * @param url 请求URL
     * @param params 请求参数
     * @param headers 请求头
     * @param timeout 超时时间
     * @return 响应内容
     */
    public static String getWithHeaders(String url, Map<String, Object> params, 
                                      Map<String, String> headers, int timeout) {
        Assert.hasText(url, "请求URL不能为空");
        
        try {
            HttpRequest request = HttpRequest.get(url)
                    .form(params)
                    .timeout(timeout);
            
            // 添加请求头
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(request::header);
            }
            
            HttpResponse response = request.execute();
            log.debug("带请求头GET请求成功: {} statusCode: {}", url, response.getStatus());
            return response.body();
        } catch (Exception e) {
            log.error("带请求头GET请求失败: {}", url, e);
            throw new ServiceException("带请求头GET请求失败: " + url, e);
        }
    }

    /**
     * 带认证的请求
     * @param url 请求URL
     * @param token 认证令牌
     * @param method 请求方法
     * @return 响应内容
     */
    public static String authRequest(String url, String token, Method method) {
        Assert.hasText(url, "请求URL不能为空");
        Assert.hasText(token, "认证令牌不能为空");
        Assert.notNull(method, "请求方法不能为空");
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        
        switch (method) {
            case GET:
                return getWithHeaders(url, headers);
            case POST:
                return HttpRequest.post(url).addHeaders(headers).execute().body();
            case PUT:
                return HttpRequest.put(url).addHeaders(headers).execute().body();
            case DELETE:
                return HttpRequest.delete(url).addHeaders(headers).execute().body();
            default:
                throw new IllegalArgumentException("不支持的请求方法: " + method);
        }
    }
}