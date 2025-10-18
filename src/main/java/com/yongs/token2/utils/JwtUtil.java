package com.yongs.token2.utils;

import com.yongs.token2.exception.BusinessException;
import com.yongs.token2.properties.JwtProperties;
import com.yongs.token2.service.RefreshTokenVersionService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/17 22:23
 */
@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    @Autowired
    private RefreshTokenVersionService refreshTokenVersionService;

    /**
     * 注入配置及SecretKey构造
     * @param jwtProperties
     */
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        //Base64编码
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成accessToken
     */
    public String generateAccessToken(String username){
        Map<String,Object> claims = new HashMap<>();
        claims.put("type","access");
        return buildToken(
                claims,
                username,
                jwtProperties.getAccessTokenExpiration()
        );
    }

    /**
     * 生成refreshToken方法
     */
    public String generateRefreshToken(String username){
        Long version = refreshTokenVersionService.getUserRefreshVersion(username);
        Map<String,Object> claims = new HashMap<>();
        claims.put("type","refresh");
        claims.put("ver",version);
        return buildToken(
                claims,
                username,
                jwtProperties.getRefreshTokenExpiration()
        );
    }

    /**
     * 从令牌中提取过期时间
     */
    public Date getExpirationFromToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 从令牌中提取用户名
     */
    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从令牌中提取token类型
     */
    public String getTypeFromToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.get("type", String.class);
    }

    /**
     * 校验令牌是否过期
     */
    public boolean isTokenExpired(String token){
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    /**
     * 校验令牌结构是否完整
     */
    public boolean validateTokenStructure(String token){
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 校验AccessToken
     */
    public boolean validateAccessToken(String token){
        if (validateTokenStructure(token) && isTokenExpired(token)){
            return false;
        }

        String type = getTypeFromToken(token);
        return "access".equals(type);
    }

    /**
     * 校验RefreshToken
     */
    public boolean validateRefreshToken(String token){
        if (validateTokenStructure(token) && isTokenExpired(token)){
            return false;
        }

        String type = getTypeFromToken(token);
        if (!"refresh".equals(type)) return false;

        String username = getUsernameFromToken(token);
        Long tokenVersion = getClaimsFromToken(token).get("ver", Long.class);
        Long currentVersion = refreshTokenVersionService.getUserRefreshVersion(username);

        return tokenVersion!=null && tokenVersion.equals(currentVersion);
    }

    /**
     * 使用RefreshToken生成新的AccessToken
     */
    public String refreshAccessToken(String token){
        if (!validateRefreshToken(token)){
            throw new BusinessException("Refresh Token 无效！");
        }

        String username = getUsernameFromToken(token);

        Map<String,Object> claims = new HashMap<>();
        claims.put("type","access");

        return buildToken(
                claims,
                username,
                jwtProperties.getAccessTokenExpiration()
        );
    }

    /**
     * 解析令牌
     */
    private Claims getClaimsFromToken(String token){
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BusinessException("Token已过期");
        } catch (SignatureException e) {
            throw new BusinessException("Token签名无效");
        } catch (MalformedJwtException e) {
            throw new BusinessException("Token格式错误");
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException("Token解析失败: " + e.getMessage());
        }
    }

    /**
     * 构建通用token方法
     */
    private String buildToken(Map<String,Object> claims,
                             String subject,
                             long expiration){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
