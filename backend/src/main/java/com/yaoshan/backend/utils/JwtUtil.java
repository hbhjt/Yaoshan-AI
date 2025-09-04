package com.yaoshan.backend.utils;

import com.yaoshan.backend.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类：生成Token、解析Token、验证Token有效性
 * 依赖JwtConfig获取密钥和配置参数，与拦截器、JwtConfig完全配套
 */
@Component // 交给Spring容器管理，便于注入到拦截器和Controller中
public class JwtUtil {

    // 注入JWT配置类（包含密钥、过期时间等核心参数）
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 生成JWT令牌
     * @param claims 自定义负载（如存储userId、role等信息）
     * @return 生成的JWT令牌字符串
     */
    public String createJWT(Map<String, Object> claims) {
        // 1. 参数校验
        if (jwtConfig == null || jwtConfig.getSecretKey() == null) {
            throw new IllegalStateException("JWT配置未正确初始化，请检查JwtConfig");
        }
        
        // 处理空claims参数
        claims = claims != null ? claims : Collections.emptyMap();
        
        // 2. 计算Token过期时间（当前时间 + 配置的过期毫秒数）
        long expirationTime = System.currentTimeMillis() + jwtConfig.getExpiration();
        Date expirationDate = new Date(expirationTime);

        // 3. 构建JWT令牌
        JwtBuilder builder = Jwts.builder()
                // 添加自定义负载（如userId）
                .addClaims(claims)
                // 设置过期时间
                .setExpiration(expirationDate)
                // 使用HS256算法和配置的密钥签名（确保与JwtConfig中的密钥一致）
                .signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS256);

        // 4. 压缩为字符串并返回
        return builder.compact();
    }

    /**
     * 解析JWT令牌，获取负载信息
     * @param token 待解析的JWT令牌
     * @return 令牌中的负载（Claims对象，可获取自定义信息如userId）
     * @throws JwtException 当Token无效（过期、签名错误、格式错误等）时抛出
     * @throws IllegalArgumentException 当Token为空或无效格式时抛出
     */
    public Claims parseJWT(String token) throws JwtException, IllegalArgumentException {
        // 1. 参数校验
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT令牌不能为空");
        }
        
        if (jwtConfig == null || jwtConfig.getSecretKey() == null) {
            throw new IllegalStateException("JWT配置未正确初始化，请检查JwtConfig");
        }
        
        // 2. 使用配置的密钥验证签名并解析Token
        return Jwts.parserBuilder()
                // 设置签名密钥（必须与生成Token时的密钥一致，否则验证失败）
                .setSigningKey(jwtConfig.getSecretKey())
                // 构建解析器
                .build()
                // 解析Token并获取负载（若Token无效，会在此处抛出JwtException）
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从Token中提取指定的自定义参数（如userId）
     * @param token JWT令牌
     * @param key 自定义参数的键（如"userId"）
     * @param clazz 参数类型（如Long.class）
     * @return 提取的参数值
     * @throws JwtException Token无效时抛出
     * @throws IllegalArgumentException 当Token或key为空时抛出
     * @throws ClassCastException 当参数类型不匹配时抛出
     */
    public <T> T getClaimFromToken(String token, String key, Class<T> clazz) throws JwtException, IllegalArgumentException, ClassCastException {
        // 参数校验
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("参数键名不能为空");
        }
        
        if (clazz == null) {
            throw new IllegalArgumentException("参数类型不能为空");
        }
        
        // 解析Token并获取指定参数
        Claims claims = parseJWT(token);
        return claims.get(key, clazz);
    }
    
    /**
     * 验证Token是否有效
     * @param token 待验证的JWT令牌
     * @return true表示Token有效，false表示Token无效
     */
    public boolean validateToken(String token) {
        try {
            parseJWT(token);
            return true;
        } catch (Exception e) {
            // Token无效（过期、签名错误、格式错误等）
            return false;
        }
    }
}
