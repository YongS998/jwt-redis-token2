package com.yongs.token2.service;

import com.yongs.token2.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/18 15:53
 */
@Service
public class RefreshTokenVersionService {

    private static final String REFRESH_VERSION_KEY = "user:refresh:version:";
    private static final Long VERSION_TTL_DAYS = 30*3600*24L;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取用户的token版本号
     */
    public Long getUserRefreshVersion(String username){
        String key = REFRESH_VERSION_KEY + username;
        String version = redisUtil.get(key);
        return version == null?0:Long.parseLong(version);
    }

    /**
     * 版本号+1，用户注销/管理员踢下线
     * 使得Refresh Token过期
     */
    public void incrementRefreshVersion(String username){
        String key = REFRESH_VERSION_KEY+username;
        redisUtil.increment(key);
        redisUtil.expire(key,VERSION_TTL_DAYS);
    }
}
