package com.cobazaar.system.service.impl;

import com.cobazaar.system.service.CaptchaService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

/**
 * 验证码服务实现类
 * 实现验证码生成和验证功能
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private DefaultKaptcha kaptcha;

    /**
     * 验证码过期时间（秒）
     */
    private static final int CAPTCHA_EXPIRE_TIME = 300;

    /**
     * 验证码前缀
     */
    private static final String CAPTCHA_PREFIX = "captcha:";

    /**
     * 内存存储
     */
    private static final Map<String, String> CAPTCHA_STORAGE = new ConcurrentHashMap<>();

    /**
     * 生成验证码图片并输出到响应流
     * @param response HTTP响应对象
     * @param uuid 验证码唯一标识
     * @throws IOException IO异常
     */
    @Override
    public void generateCaptcha(HttpServletResponse response, String uuid) throws IOException {
        // 生成验证码文字
        String code = kaptcha.createText();
        
        // 存储验证码
        String key = CAPTCHA_PREFIX + uuid;
        // 使用内存存储
        CAPTCHA_STORAGE.put(key, code);
        // 这里简化处理，实际项目中应该添加过期时间管理
        
        // 生成验证码图片
        BufferedImage image = kaptcha.createImage(code);
        
        // 设置响应头
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // 输出图片到响应流
        try (OutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            outputStream.flush();
        }
    }

    /**
     * 验证验证码
     * @param uuid 验证码唯一标识
     * @param code 验证码值
     * @return 是否验证通过
     */
    @Override
    public boolean validateCaptcha(String uuid, String code) {
        if (uuid == null || code == null) {
            return false;
        }
        
        // 获取验证码
        String key = CAPTCHA_PREFIX + uuid;
        String storedCode = CAPTCHA_STORAGE.get(key);
        
        if (storedCode == null) {
            return false;
        }
        
        // 验证验证码
        boolean isValid = storedCode.equalsIgnoreCase(code);
        
        // 验证通过后移除验证码
        if (isValid) {
            CAPTCHA_STORAGE.remove(key);
        }
        
        return isValid;
    }

    /**
     * 移除验证码
     * @param uuid 验证码唯一标识
     */
    @Override
    public void removeCaptcha(String uuid) {
        if (uuid != null) {
            String key = CAPTCHA_PREFIX + uuid;
            CAPTCHA_STORAGE.remove(key);
        }
    }
}
