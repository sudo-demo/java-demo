package com.cobazaar.common.utils;

import com.cobazaar.common.config.redis.RedisEnvConfig;
import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具类
 * 基于RedisTemplate操作Redis
 * 
 * @author cobazaar
 */
@Component
@Slf4j
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisEnvConfig redisEnvConfig;

    // =============================common============================

    /**
     * 获取带环境前缀的key
     * @param key 原始key
     * @return 带环境前缀的key
     */
    private String getKeyWithEnvPrefix(String key) {
        return redisEnvConfig.addPrefix(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        if (time > 0) {
            String fullKey = getKeyWithEnvPrefix(key);
            redisTemplate.expire(fullKey, time, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        String fullKey = getKeyWithEnvPrefix(key);
        return redisTemplate.getExpire(fullKey, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        String fullKey = getKeyWithEnvPrefix(key);
        return redisTemplate.hasKey(fullKey);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            Collection<String> keys = new ArrayList<>(key.length);
            for (String k : key) {
                keys.add(getKeyWithEnvPrefix(k));
            }
            redisTemplate.delete(keys);
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        if (key != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            return redisTemplate.opsForValue().get(fullKey);
        }
        return null;
    }

    /**
     * 普通缓存获取并转换为指定类型
     *
     * @param key 键
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型的泛型
     * @return 转换后的值
     */
    public <T> T get(String key, Class<T> clazz) {
        if (key != null && clazz != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            Object value = redisTemplate.opsForValue().get(fullKey);
            if (value != null) {
                // 使用hutool进行类型转换
                return Convert.convert(clazz, value);
            }
        }
        return null;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        if (key != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            redisTemplate.opsForValue().set(fullKey, value);
            return true;
        }
        return false;
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        if (key != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            if (time > 0) {
                redisTemplate.opsForValue().set(fullKey, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        }
        return false;
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public long incr(String key, long delta) {
        if (key != null && delta > 0) {
            String fullKey = getKeyWithEnvPrefix(key);
            Long result = redisTemplate.opsForValue().increment(fullKey, delta);
            return result != null ? result : -1;
        }
        return -1;
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    public long decr(String key, long delta) {
        if (key != null && delta > 0) {
            String fullKey = getKeyWithEnvPrefix(key);
            Long result = redisTemplate.opsForValue().decrement(fullKey, delta);
            return result != null ? result : -1;
        }
        return -1;
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     */
    public Object hget(String key, String item) {
        if (key != null && item != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            return redisTemplate.opsForHash().get(fullKey, item);
        }
        return null;
    }

    /**
     * HashGet并转换为指定类型
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @param clazz 返回类型的Class对象
     * @param <T> 返回类型的泛型
     * @return 转换后的值
     */
    public <T> T hget(String key, String item, Class<T> clazz) {
        if (key != null && item != null && clazz != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            Object value = redisTemplate.opsForHash().get(fullKey, item);
            if (value != null) {
                // 使用hutool进行类型转换
                return Convert.convert(clazz, value);
            }
        }
        return null;
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        if (key != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            return redisTemplate.opsForHash().entries(fullKey);
        }
        return null;
    }

    /**
     * 获取hashKey对应的所有键值并转换为指定类型的Map
     *
     * @param key 键
     * @param keyClazz 键类型的Class对象
     * @param valueClazz 值类型的Class对象
     * @param <K> 键类型的泛型
     * @param <V> 值类型的泛型
     * @return 转换后的键值对
     */
    public <K, V> Map<K, V> hmget(String key, Class<K> keyClazz, Class<V> valueClazz) {
        if (key != null && keyClazz != null && valueClazz != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(fullKey);
            if (!entries.isEmpty()) {
                Map<K, V> result = new HashMap<>(entries.size());
                for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                    Object entryKey = entry.getKey();
                    Object entryValue = entry.getValue();
                    if (entryKey != null && entryValue != null) {
                        // 使用hutool进行类型转换
                        K convertedKey = Convert.convert(keyClazz, entryKey);
                        V convertedValue = Convert.convert(valueClazz, entryValue);
                        result.put(convertedKey, convertedValue);
                    }
                }
                return result;
            }
        }
        return null;
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     */
    public boolean hmset(String key, Map<String, Object> map) {
        if (key != null && map != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            redisTemplate.opsForHash().putAll(fullKey, map);
            return true;
        }
        return false;
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        if (key != null && map != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            redisTemplate.opsForHash().putAll(fullKey, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        }
        return false;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        if (key != null && item != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            redisTemplate.opsForHash().put(fullKey, item, value);
            return true;
        }
        return false;
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        if (key != null && item != null && item.length > 0) {
            String fullKey = getKeyWithEnvPrefix(key);
            redisTemplate.opsForHash().delete(fullKey, item);
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        if (key != null && item != null) {
            String fullKey = getKeyWithEnvPrefix(key);
            return redisTemplate.opsForHash().hasKey(fullKey, item);
        }
        return false;
    }
}