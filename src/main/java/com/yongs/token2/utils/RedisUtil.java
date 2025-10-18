package com.yongs.token2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/17 23:42
 */

// RedisUtil.java
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置字符串值，并指定过期时间（秒）
     */
    public void set(String key, String value, long expireSeconds) {
        stringRedisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 设置字符串值，无过期时间
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取字符串值
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除 key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 设置过期时间（秒）
     */
    public void expire(String key, long expireSeconds) {
        stringRedisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 获取剩余过期时间（秒），-1 表示永不过期，-2 表示 key 不存在
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    // ============ Hash 操作 ============
    /**
     * 写入 Hash 字段
     */
    public void hashSet(String key, String hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取 Hash 字段
     */
    public String hashGet(String key, String hashKey) {
        Object value = stringRedisTemplate.opsForHash().get(key, hashKey);
        return value == null ? null : (String) value;
    }

    /**
     * 删除 Hash 字段
     */
    public void hashDelete(String key, String hashKey) {
        stringRedisTemplate.opsForHash().delete(key, hashKey);
    }

    // ============ Set 操作 ============
    /**
     * 向 Set 添加成员
     */
    public void setAdd(String key, String... values) {
        stringRedisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取 Set 所有成员
     */
    public Set<String> setMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 判断 Set 是否包含某成员
     */
    public boolean setIsMember(String key, String value) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 移除 Set 成员
     */
    public void setRemove(String key, String value) {
        stringRedisTemplate.opsForSet().remove(key, value);
    }

    // ============ Increment / Decrement ============
    /**
     * 自增（用于版本号、计数器）
     */
    public Long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增指定步长
     */
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自减
     */
    public Long decrement(String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }
}