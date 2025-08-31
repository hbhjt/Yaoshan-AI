package com.yaoshan.backend.config;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * JWT配置类：读取JWT参数、生成签名密钥、校验密钥有效性
 * 与JWT拦截器、JwtUtil完全配套
 */
@Configuration // 标识为Spring配置类，启动时自动加载
public class JwtConfig {

    /**
     * 从配置文件读取JWT签名密钥（对应 application.yml 中的 jwt.secret）
     * 要求：HS256算法需至少32个字符（256位），避免过短导致安全风险
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 从配置文件读取Token过期时间（对应 application.yml 中的 jwt.expiration）
     * 单位：毫秒（如 604800000 表示7天）
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 从配置文件读取Token在请求头中的键名（对应 application.yml 中的 jwt.token-name）
     * 如：前端请求头需传 "authentication: Bearer {token}"，则此值为 "authentication"
     */
    @Value("${jwt.token-name}")
    private String tokenName;

    /**
     * JWT签名密钥对象（由 secret 生成，供 JwtUtil 签名/解析Token使用）
     * JJWT 0.11.5+ 强制要求使用 SecretKey 对象，不能直接用字符串/字节数组
     */
    private SecretKey secretKey;

    /**
     * 初始化方法：项目启动时自动执行（@PostConstruct）
     * 1. 校验 secret 长度有效性
     * 2. 将 secret 转换为 SecretKey 对象
     */
    @PostConstruct
    public void init() {
        // 1. 校验密钥长度：HS256算法要求至少32个字符（256位），不足则抛异常（启动阶段暴露问题）
        if (secret == null || secret.trim().isEmpty() || secret.length() < 32) {
            throw new IllegalArgumentException(
                    "JWT签名密钥无效！HS256算法要求密钥至少32个字符（256位），当前密钥长度："
                            + (secret == null ? 0 : secret.length())
            );
        }

        // 2. 将字符串密钥转换为 SecretKey 对象（使用 UTF-8 编码避免乱码）
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes); // JJWT工具类生成合规的HS256密钥
    }

    // ------------------- 对外提供配置的getter方法（拦截器、JwtUtil需调用） -------------------
    /**
     * 获取JWT签名密钥对象（供 JwtUtil 生成/解析Token使用）
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /**
     * 获取Token过期时间（供 JwtUtil 生成Token时设置有效期）
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * 获取Token在请求头中的键名（供 JWT拦截器 读取请求头中的Token）
     */
    public String getTokenName() {
        return tokenName;
    }

    // 注意：不提供secret的getter方法！避免密钥泄露（仅内部通过init()转换为SecretKey即可）
}