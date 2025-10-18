package com.yongs.token2.controller;

import com.yongs.token2.pojo.Result;
import com.yongs.token2.pojo.dto.LoginDTO;
import com.yongs.token2.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 功能：认证接口
 * 作者：YongS
 * 日期：2025/10/17 22:56
 */
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDTO loginDTO, HttpServletResponse response){
        return authService.login(loginDTO,response);
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Result register(@RequestBody LoginDTO registerDTO){
        return authService.register(registerDTO);
    }

    /**
     * 刷新Access Token接口
     */
    @GetMapping("/refresh")
    public Result refreshAccessToken(HttpServletRequest request){
        return authService.refresh(request);
    }

    /**
     * 注销接口
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        return authService.logout(request);
    }
}
