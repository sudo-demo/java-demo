package com.cobazaar.common.wrapper;

import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 可重复读取请求体的包装器
 * 解决Servlet InputStream只能读取一次的问题
 * 
 * @author cobazaar
 */
public final class RepeatableReadRequestWrapper extends ContentCachingRequestWrapper {

    private final byte[] body;

    /**
     * 构造函数
     * @param request 原始HTTP请求
     */
    public RepeatableReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedServletInputStream(this.body);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 获取请求体内容
     * @return 请求体字符串
     */
    public String getBodyContent() {
        return new String(this.body);
    }

    /**
     * 缓存的Servlet输入流实现
     */
    private static class CachedServletInputStream extends ServletInputStream {
        
        private final ByteArrayInputStream buffer;

        public CachedServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() throws IOException {
            return buffer.read();
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("不支持异步读取");
        }
    }
}