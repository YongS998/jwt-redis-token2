package com.yongs.token2.filter;

import com.yongs.token2.exception.BusinessException;
import com.yongs.token2.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/18 13:41
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final List<String> whiteList = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/public/**"
    );
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //白名单
        String requestURI = request.getRequestURI();
        if (isWhitelisted(requestURI)){
            filterChain.doFilter(request,response);
            return;
        }

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String AccessToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")){
            AccessToken = requestHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(AccessToken);
            } catch (Exception e) {
                throw new BusinessException("JWT token 解析失败!");
            }
        }

        //校验令牌
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateAccessToken(AccessToken)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //安全审计
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }

    /**
     * 校验路径白名单
     */
    private boolean isWhitelisted(String requestURI){
        return whiteList.stream().anyMatch(pattern -> pathMatcher.match(pattern,requestURI));
    }
}
