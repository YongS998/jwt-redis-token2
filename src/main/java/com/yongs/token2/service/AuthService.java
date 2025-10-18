package com.yongs.token2.service;

import com.yongs.token2.exception.BusinessException;
import com.yongs.token2.mapper.UserMapper;
import com.yongs.token2.pojo.Result;
import com.yongs.token2.pojo.bo.CustomUserDetails;
import com.yongs.token2.pojo.dto.LoginDTO;
import com.yongs.token2.pojo.entity.User;
import com.yongs.token2.utils.JwtUtil;
import com.yongs.token2.utils.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/17 23:08
 */
@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private RefreshTokenVersionService refreshTokenVersionService;
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    public Result login(LoginDTO loginDTO, HttpServletResponse response) {
        //1.构建认证token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword());

        try {
            //2.调用认证方法
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            if (authenticate == null){
                throw new BusinessException("用户名或密码错误");
            }

            //3.获取UserDetails
            CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
            //3.1提取username
            String username = userDetails.getUsername();

            //4.生成token
            //4.0作废所有旧Refresh Token，版本号自增
            refreshTokenVersionService.incrementRefreshVersion(username);
            //4.1生成access token
            String accessToken = jwtUtil.generateAccessToken(username);
            //4.2生成refresh token
            String refreshToken = jwtUtil.generateRefreshToken(username);

            //5.构建Map
            Map<String,Object> map = new HashMap<>();
            map.put("accessToken",accessToken);

            //6.标记在线状态
            onlineUserService.makeUserOnline(username);

            //7.refreshToken写入HttpOnly Cookie
            ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true) //js无法读取
                    .secure(false) //生成环境必需，https
                    .path("/auth/refresh") //只在刷新接口发送
                    .maxAge(Duration.ofDays(7)) //7天
                    .sameSite("Strict") //放 csrf
                    .build();

            response.setHeader("Set-Cookie",cookie.toString());

            return Result.success(map);
        } catch (AuthenticationException e) {
            throw new BusinessException("用户名或密码错误");
        }
    }

    public Result register(LoginDTO registerDTO) {

        User userDb = userMapper.findByUsername(registerDTO.getUsername());
        if (userDb != null){
            throw new BusinessException("用户名已存在...");
        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO,user);

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setStatus(1);

        int insert = userMapper.insert(user);
        if (insert > 0){
            return Result.success();
        }else {
            return Result.error("注册失败！");
        }
    }

    public Result refresh(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())){
                     refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null){
            return Result.error("refresh token 不存在，请重新登录");
        }

        String accessToken = jwtUtil.refreshAccessToken(refreshToken);

        return Result.success(accessToken);
    }

    public Result logout(HttpServletRequest request) {
        String username = securityUtil.getCurrentUsername();

        refreshTokenVersionService.incrementRefreshVersion(username);
        onlineUserService.makeUserOffline(username);

        SecurityContextHolder.clearContext();
        return Result.success();
    }
}
