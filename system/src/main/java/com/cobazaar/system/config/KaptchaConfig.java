package com.cobazaar.system.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置类
 * 配置Kaptcha验证码生成器
 */
@Configuration
public class KaptchaConfig {

    /**
     * 验证码生成器配置
     * @return 验证码生成器
     */
    @Bean
    public DefaultKaptcha kaptcha() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        
        // 验证码图片宽度
        properties.setProperty("kaptcha.image.width", "150");
        // 验证码图片高度
        properties.setProperty("kaptcha.image.height", "50");
        // 验证码字符集
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        // 验证码字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 验证码字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        // 验证码字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 验证码字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial, Courier");
        // 干扰线颜色
        properties.setProperty("kaptcha.noise.color", "black");
        // 干扰线实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        // 图片样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        // 文字渲染器
        properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");
        // 背景实现类
        properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");
        // 背景颜色渐变开始
        properties.setProperty("kaptcha.background.clear.from", "white");
        // 背景颜色渐变结束
        properties.setProperty("kaptcha.background.clear.to", "white");
        // 文字渲染器
        properties.setProperty("kaptcha.textproducer.char.space", "5");
        
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
