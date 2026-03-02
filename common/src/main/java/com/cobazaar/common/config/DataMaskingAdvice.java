package com.cobazaar.common.config;

import com.cobazaar.common.annotation.DataMasking;
import com.cobazaar.common.utils.DataMaskingUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 全局数据脱敏处理
 * 对所有REST接口的响应数据自动脱敏
 */
@RestControllerAdvice
public class DataMaskingAdvice implements ResponseBodyAdvice<Object> {
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null) {
            maskData(body);
        }
        return body;
    }
    
    /**
     * 递归脱敏处理
     */
    private void maskData(Object obj) {
        maskData(obj, new HashSet<>());
    }
    
    /**
     * 递归脱敏处理（带循环引用检测）
     */
    private void maskData(Object obj, Set<Object> visited) {
        if (obj == null) {
            return;
        }
        
        // 检测循环引用
        if (!visited.add(obj)) {
            return;
        }
        
        // 处理集合类型
        if (obj instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) obj;
            for (Object item : collection) {
                maskData(item, visited);
            }
            return;
        }
        
        // 处理数组类型
        if (obj.getClass().isArray()) {
            // 处理对象数组
            if (obj instanceof Object[]) {
                Object[] array = (Object[]) obj;
                for (Object item : array) {
                    maskData(item, visited);
                }
            } else {
                // 处理基本类型数组（如int[], long[]等），基本类型数组不需要脱敏
            }
            return;
        }
        
        // 处理Map类型
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Object value : map.values()) {
                maskData(value, visited);
            }
            return;
        }
        
        // 处理基本类型和包装类
        if (isPrimitive(obj)) {
            return;
        }
        
        // 处理自定义对象
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            // 检查是否有脱敏注解
            if (field.isAnnotationPresent(DataMasking.class)) {
                DataMasking annotation = field.getAnnotation(DataMasking.class);
                DataMasking.MaskingType type = annotation.type();
                
                // 设置字段可访问
                field.setAccessible(true);
                
                try {
                    Object value = field.get(obj);
                    if (value != null && value instanceof String) {
                        String maskedValue = maskValue((String) value, type);
                        field.set(obj, maskedValue);
                    }
                } catch (IllegalAccessException e) {
                    // 忽略访问异常
                }
            }
            
            // 递归处理子对象
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value != null && !isPrimitive(value)) {
                    maskData(value, visited);
                }
            } catch (IllegalAccessException e) {
                // 忽略访问异常
            }
        }
    }
    
    /**
     * 根据类型进行脱敏
     */
    private String maskValue(String value, DataMasking.MaskingType type) {
        switch (type) {
            case MOBILE:
                return DataMaskingUtils.maskMobile(value);
            case ID_CARD:
                return DataMaskingUtils.maskIdCard(value);
            case EMAIL:
                return DataMaskingUtils.maskEmail(value);
            case BANK_CARD:
                return DataMaskingUtils.maskBankCard(value);
            case ADDRESS:
                return DataMaskingUtils.maskAddress(value);
            case NAME:
                return DataMaskingUtils.maskName(value);
            case PASSWORD:
                return DataMaskingUtils.maskPassword(value);
            default:
                return value;
        }
    }
    
    /**
     * 判断是否为基本类型或包装类
     */
    private boolean isPrimitive(Object obj) {
        return obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj instanceof Character;
    }
}