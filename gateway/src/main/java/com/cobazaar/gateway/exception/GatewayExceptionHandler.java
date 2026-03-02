package com.cobazaar.gateway.exception;

import com.cobazaar.gateway.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 网关全局异常处理器
 * 处理Spring Cloud Gateway中的异常，返回统一的JSON格式响应
 *
 * @author cobazaar
 */
@Slf4j
@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private List<ViewResolver> viewResolvers;
    private List<HttpMessageWriter<?>> messageWriters;

    public GatewayExceptionHandler(ServerCodecConfigurer serverCodecConfigurer) {
        this.messageWriters = serverCodecConfigurer.getWriters();
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 设置响应头
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构建错误响应 - 统一返回HTTP 200
        Result<Void> result;

        if (ex instanceof NotFoundException) {
            // 服务未找到异常
            result = Result.error(1004, "服务未找到");
            log.warn("服务未找到: {}", ex.getMessage());
        } else if (ex instanceof ResponseStatusException) {
            // HTTP状态异常
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            HttpStatus httpStatus = responseStatusException.getStatus();
            result = Result.error(httpStatus.value(), httpStatus.getReasonPhrase());
            log.warn("HTTP状态异常: {}", ex.getMessage());
        } else {
            // 其他异常
            result = Result.error(1006, "网关内部错误");
            log.error("网关内部错误", ex);
        }

        // 统一设置HTTP状态码为200
        response.setStatusCode(HttpStatus.OK);

        // 返回JSON响应
        String jsonResponse = com.alibaba.fastjson.JSON.toJSONString(result);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(jsonResponse.getBytes())));
    }
}