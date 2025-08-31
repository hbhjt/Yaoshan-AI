package com.yaoshan.backend.utils;

import com.yaoshan.backend.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类：生成Token、解析Token
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
        // 1. 计算Token过期时间（当前时间 + 配置的过期毫秒数）
        long expirationTime = System.currentTimeMillis() + jwtConfig.getExpiration();
        Date expirationDate = new Date(expirationTime);

        // 2. 构建JWT令牌
        JwtBuilder builder = Jwts.builder()
                // 添加自定义负载（如userId）
                .addClaims(claims)
                // 设置过期时间
                .setExpiration(expirationDate)
                // 使用HS256算法和配置的密钥签名（确保与JwtConfig中的密钥一致）
                .signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS256);

        // 3. 压缩为字符串并返回
        return builder.compact();
    }

    /**
     * 解析JWT令牌，获取负载信息
     * @param token 待解析的JWT令牌
     * @return 令牌中的负载（Claims对象，可获取自定义信息如userId）
     * @throws JwtException 当Token无效（过期、签名错误、格式错误等）时抛出
     */
    public Claims parseJWT(String token) throws JwtException {
        // 1. 使用配置的密钥验证签名并解析Token
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
     */
    public <T> T getClaimFromToken(String token, String key, Class<T> clazz) throws JwtException {
        Claims claims = parseJWT(token);
        return claims.get(key, clazz);
    }
}
