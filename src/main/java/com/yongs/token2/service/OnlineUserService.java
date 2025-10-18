package com.yongs.token2.service;

import com.yongs.token2.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/18 16:05
 */
@Service
public class OnlineUserService {

    private static final String ONLINE_USER_SET = "online:users";

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 标记用户在线状态
     */
    public void makeUserOnline(String username){
        redisUtil.setAdd(ONLINE_USER_SET,username);
    }

    /**
     * 标记用户离线
     */
    public void makeUserOffline(String username){
        redisUtil.setRemove(ONLINE_USER_SET,username);
    }

    /**
     * 获取所有在线用户
     */
    public Set<String> getOnlineUsers(){
        return redisUtil.setMembers(ONLINE_USER_SET);
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(String username){
        return redisUtil.setIsMember(ONLINE_USER_SET,username);
    }
}
