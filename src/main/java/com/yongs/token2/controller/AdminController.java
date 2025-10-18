package com.yongs.token2.controller;

import com.yongs.token2.pojo.Result;
import com.yongs.token2.service.OnlineUserService;
import com.yongs.token2.service.RefreshTokenVersionService;
import com.yongs.token2.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 功能：管理员功能
 * 作者：YongS
 * 日期：2025/10/18 16:32
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RefreshTokenVersionService refreshTokenVersionService;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 查看在线用户
     */
    @GetMapping("/online-users")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Result getOnlineUsers(){
        Set<String> onlineUsers = onlineUserService.getOnlineUsers();
        return Result.success(onlineUsers);
    }

    /**
     * 踢人下线
     */
    @PostMapping("/kick-out/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result kickOutUser(@PathVariable("username") String username){
        if (!onlineUserService.isUserOnline(username)){
            return Result.error("用户未登录，无法踢下线");
        }

        String currentUsername = securityUtil.getCurrentUsername();
        if (currentUsername.equals(username)){
            return Result.error("不能踢自己下线");
        }

        //作废 refresh Token
        refreshTokenVersionService.incrementRefreshVersion(username);
        onlineUserService.makeUserOffline(username);

        return Result.success();
    }
}
