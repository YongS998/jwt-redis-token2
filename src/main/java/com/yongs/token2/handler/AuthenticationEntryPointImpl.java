package com.yongs.token2.handler;

import cn.hutool.json.JSONUtil;
import com.yongs.token2.pojo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/18 15:00
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        Result error = Result.error(401, "未登录...");
        String jsonStr = JSONUtil.toJsonStr(error);
        writer.write(jsonStr);
        writer.flush();
        writer.close();
    }
}
