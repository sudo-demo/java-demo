package com.cobazaar.system.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码服务接口
 * 提供验证码生成和验证功能
 */
public interface CaptchaService {

    /**
     * 生成验证码图片并输出到响应流
     * @param response HTTP响应对象
     * @param uuid 验证码唯一标识
     * @throws IOException IO异常
     */
    void generateCaptcha(HttpServletResponse response, String uuid) throws IOException;

    /**
     * 验证验证码
     * @param uuid 验证码唯一标识
     * @param code 验证码值
     * @return 是否验证通过
     */
    boolean validateCaptcha(String uuid, String code);

    /**
     * 移除验证码
     * @param uuid 验证码唯一标识
     */
    void removeCaptcha(String uuid);
}
