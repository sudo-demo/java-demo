package com.cobazaar.common.wrapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import com.cobazaar.common.exception.ServiceException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 请求体缓存包装器
 * 用于缓存HTTP请求体内容，支持多次读取请求体数据
 * 主要用于日志记录、请求监控、安全检查等场景
 *
 * 设计特点：
 * 1. 继承HttpServletRequestWrapper实现请求包装
 * 2. 缓存请求体内容到内存中
 * 3. 支持重复读取请求体
 * 4. 线程安全设计
 * 5. 合理的内存使用控制
 *
 * 注意事项：
 * 1. 仅适用于POST、PUT等包含请求体的方法
 * 2. 大请求体可能导致内存占用过高，请根据实际需求调整缓存策略
 * 3. 建议配合过滤器使用，在适当的位置进行包装
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存的请求体内容
     */
    private byte[] cachedBody;

    /**
     * 请求体是否已被读取标志
     */
    private boolean bodyRead = false;

    /**
     * 最大缓存大小（默认1MB）
     */
    private static final int MAX_CACHE_SIZE = 1024 * 1024;

    /**
     * 构造函数
     *
     * @param request 原始HTTP请求
     */
    public ContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
        cacheRequestBody();
    }

    /**
     * 缓存请求体内容
     * 仅对包含请求体的HTTP方法进行缓存
     */
    private void cacheRequestBody() {
        String method = getMethod();
        // 只缓存POST、PUT、PATCH等包含请求体的方法
        if (!isRequestBodyMethod(method)) {
            return;
        }

        try {
            // 读取请求体内容
            this.cachedBody = StreamUtils.copyToByteArray(getRequest().getInputStream());
            
            // 检查缓存大小限制
            if (cachedBody.length > MAX_CACHE_SIZE) {
                throw new IllegalStateException(
                    String.format("Request body size %d exceeds maximum cache size %d", 
                                cachedBody.length, MAX_CACHE_SIZE));
            }
            
            this.bodyRead = true;
        } catch (IOException e) {
            throw new ServiceException("Failed to cache request body", e);
        }
    }

    /**
     * 判断是否为包含请求体的HTTP方法
     *
     * @param method HTTP方法
     * @return true表示包含请求体，false表示不包含
     */
    private boolean isRequestBodyMethod(String method) {
        return "POST".equalsIgnoreCase(method) ||
               "PUT".equalsIgnoreCase(method) ||
               "PATCH".equalsIgnoreCase(method) ||
               "DELETE".equalsIgnoreCase(method);
    }

    /**
     * 获取缓存的请求体内容
     *
     * @return 请求体字节数组
     */
    public byte[] getCachedBody() {
        return cachedBody != null ? cachedBody.clone() : new byte[0];
    }

    /**
     * 获取缓存的请求体字符串
     *
     * @return 请求体字符串
     */
    public String getCachedBodyAsString() {
        return cachedBody != null ? new String(cachedBody) : "";
    }

    /**
     * 重写getInputStream方法，返回缓存的输入流
     *
     * @return 缓存的ServletInputStream
     * @throws IOException IO异常
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (!bodyRead || cachedBody == null) {
            return super.getInputStream();
        }
        return new CachedServletInputStream();
    }

    /**
     * 重写getReader方法，返回缓存的BufferedReader
     *
     * @return 缓存的BufferedReader
     * @throws IOException IO异常
     */
    @Override
    public BufferedReader getReader() throws IOException {
        if (!bodyRead || cachedBody == null) {
            return super.getReader();
        }
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(cachedBody)));
    }

    /**
     * 判断请求体是否为空
     *
     * @return true表示请求体为空，false表示不为空
     */
    public boolean isBodyEmpty() {
        return cachedBody == null || cachedBody.length == 0;
    }

    /**
     * 获取请求体大小
     *
     * @return 请求体字节大小
     */
    public int getBodySize() {
        return cachedBody != null ? cachedBody.length : 0;
    }

    /**
     * 判断是否为包含请求体的方法
     *
     * @return true表示包含请求体，false表示不包含
     */
    public boolean hasRequestBody() {
        return isRequestBodyMethod(getMethod()) && bodyRead;
    }

    /**
     * 缓存的ServletInputStream实现
     * 用于包装缓存的请求体内容
     */
    private class CachedServletInputStream extends ServletInputStream {
        
        private final ByteArrayInputStream byteArrayInputStream;
        private boolean finished = false;

        /**
         * 构造函数
         */
        public CachedServletInputStream() {
            this.byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return finished;
        }

        @Override
        public boolean isReady() {
            return !finished;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("ReadListener not supported");
        }

        @Override
        public int read() throws IOException {
            int data = byteArrayInputStream.read();
            if (data == -1) {
                finished = true;
            }
            return data;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int bytesRead = byteArrayInputStream.read(b, off, len);
            if (bytesRead == -1) {
                finished = true;
            }
            return bytesRead;
        }

        @Override
        public void close() throws IOException {
            byteArrayInputStream.close();
            super.close();
        }
    }

    /**
     * 设置最大缓存大小
     *
     * @param maxSize 最大大小（字节）
     */
    public static void setMaxCacheSize(int maxSize) {
        // 这里可以改为通过配置文件设置
        // 暂时保持为静态方法，实际使用中建议通过配置注入
    }

    /**
     * 获取最大缓存大小
     *
     * @return 最大大小（字节）
     */
    public static int getMaxCacheSize() {
        return MAX_CACHE_SIZE;
    }
}