package com.cobazaar.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Jackson全局配置类
 * 统一处理时间格式
 *
 * @author cobazaar
 */
@Configuration
public class JacksonConfig {

    /**
     * 日期格式（从配置文件读取，默认：yyyy-MM-dd HH:mm:ss）
     */
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    /**
     * 时区（从配置文件读取，默认：GMT+8）
     */
    @Value("${spring.jackson.time-zone:GMT+8}")
    private String timeZone;

    /**
     * 配置ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 配置时间格式
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        
        // 配置LocalDateTime的序列化和反序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        
        objectMapper.registerModule(javaTimeModule);
        
        // 配置时区
        try {
            objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        } catch (Exception e) {
            // 如果时区配置错误，使用默认时区
            objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        }
        
        return objectMapper;
    }
}
