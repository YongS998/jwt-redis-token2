package com.yongs.token2.controller;

import com.yongs.token2.pojo.Result;
import com.yongs.token2.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能：用户自身接口
 * 作者：YongS
 * 日期：2025/10/18 14:49
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/perms")
    public Result getAuthorities(){
        List<String> currentUserAuthorities = securityUtil.getCurrentUserAuthorities();
        return Result.success(currentUserAuthorities);
    }

}
