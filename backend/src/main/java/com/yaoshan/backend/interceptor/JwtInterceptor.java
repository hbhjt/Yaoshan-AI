package com.yaoshan.backend.interceptor;

import com.yaoshan.backend.config.JwtConfig;
import com.yaoshan.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT拦截器：验证Token有效性，解析userId存入请求属性
 */
@Component // 交给Spring容器管理，否则无法注入依赖
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    // 注入JWT配置（获取token-name：请求头中Token的键名，如"authentication"）
    @Autowired
    private JwtConfig jwtConfig;

    // 注入JWT工具类（用于解析Token）
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 请求到达Controller前执行：验证Token
     * @return true：Token有效，继续执行；false：Token无效，终止请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取Token（键名是配置的jwt.token-name，如"authentication"）
        String token = request.getHeader(jwtConfig.getTokenName());
        log.info("拦截到请求：{}，Token：{}", request.getRequestURI(), token);

        // 2. 判断Token是否存在（前端可能传"Bearer Token"格式，需处理）
        if (token == null || token.trim().isEmpty()) {
            return sendErrorResponse(response, 401, "未登录，请先获取Token");
        }

        // 处理"Bearer Token"格式（前端可能在Token前加"Bearer "前缀，需截取）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // 截取"Bearer "后的实际Token（注意空格）
        }

        // 3. 解析Token，验证有效性
        Claims claims = null;
        try {
            // 调用JWT工具类解析Token（需传入配置的secretKey，已在JwtUtil中通过JwtConfig获取）
            claims = jwtUtil.parseJWT(token);
        } catch (JwtException e) {
            // 捕获Token解析异常（过期、签名错误、格式错误等）
            log.error("Token解析失败：{}", e.getMessage());
            return sendErrorResponse(response, 401, e.getMessage());
        }

        // 4. 解析Token中的userId，存入HttpServletRequest的属性中
        // 注意：生成Token时claims存的是"userId"（Long类型），解析时需对应
        Long userId = claims.get("userId", Long.class);
        if (userId == null) {
            return sendErrorResponse(response, 401, "Token中未包含userId");
        }

        // 将userId存入request属性，后续Controller可通过request.getAttribute("userId")获取
        request.setAttribute("userId", userId);
        log.info("Token验证通过，解析userId：{}", userId);

        // 5. Token有效，继续执行（放行到Controller）
        return true;
    }

    /**
     * 私有工具方法：Token无效时，返回JSON格式的错误响应（避免前端拿到404/500页面）
     * @param response 响应对象
     * @param code 状态码（401：未授权）
     * @param message 错误信息
     * @return false：终止请求
     */
    private boolean sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        // 设置响应格式为JSON
        response.setContentType("application/json;charset=UTF-8");
        // 设置状态码（401：未授权）
        response.setStatus(code);

        // 构建JSON响应（与你的Result类格式保持一致，便于前端统一处理）
        String json = "{\"code\":" + code + ",\"message\":\"" + message + "\",\"data\":null}";

        // 写入响应体
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();

        // 返回false，终止请求（不再进入Controller）
        return false;
    }
}
