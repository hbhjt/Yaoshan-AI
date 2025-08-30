package com.yaoshan.backend.config;


import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;


@Configuration
public class JwtConfig {

    // 从 application.properties 中读取 JWT 密钥
    @Value("${jwt.secret}")
    private String secret;

    // 从 application.properties 中读取 Token 过期时间（毫秒）
    @Value("${jwt.expiration}")
    private long expiration;

    // 签名密钥（JWT 加密需要，由 secret 生成）
    private SecretKey secretKey;

    // 初始化方法：在对象创建后生成 secretKey，并验证 secret 有效性
    @PostConstruct
    public void init() {
        // 验证 secret 长度：HS256 算法要求密钥至少 256 位（32 个字符）
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT 密钥长度不足！至少需要 32 个字符（256 位）");
        }
        // 生成 SecretKey（基于 HMAC-SHA 算法）
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 获取签名密钥（供 JWT 生成和验证使用）
    public SecretKey getSecretKey() {
        return secretKey;
    }

    // 获取 Token 过期时间（毫秒）
    public long getExpiration() {
        return expiration;
    }
}