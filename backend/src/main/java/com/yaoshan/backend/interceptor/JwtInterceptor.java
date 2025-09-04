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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
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
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器对象
     * @return true：Token有效，继续执行；false：Token无效，终止请求
     * @throws Exception 处理异常
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 1. 跳过静态资源和非Controller方法（如Swagger文档等）
        if (!(handler instanceof HandlerMethod)) {
            return true; // 非Controller方法直接放行
        }
        
        // 2. 从请求头中获取Token（键名是配置的jwt.token-name，如"authentication"）
        String token = request.getHeader(jwtConfig.getTokenName());
        log.info("拦截到请求：{}，Token：{}", request.getRequestURI(), token);

        // 3. 判断Token是否存在（前端可能传"Bearer Token"格式，需处理）
        if (token == null || token.trim().isEmpty()) {
            return sendErrorResponse(response, 401, "未登录，请先获取Token");
        }

        // 处理"Bearer Token"格式（前端可能在Token前加"Bearer "前缀，需截取）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim(); // 截取"Bearer "后的实际Token，并去除首尾空格
            // 检查截取后的Token是否为空
            if (token.isEmpty()) {
                return sendErrorResponse(response, 401, "Token格式错误，Bearer后缺少Token值");
            }
        }

        // 4. 解析Token，验证有效性
        Claims claims = null;
        try {
            // 调用JWT工具类解析Token（需传入配置的secretKey，已在JwtUtil中通过JwtConfig获取）
            claims = jwtUtil.parseJWT(token);
        } catch (JwtException e) {
            // 捕获Token解析异常（过期、签名错误、格式错误等）
            log.error("Token解析失败：{}", e.getMessage());
            // 对异常信息进行友好处理，避免暴露过多技术细节
            String friendlyMessage = "Token无效或已过期，请重新登录";
            return sendErrorResponse(response, 401, friendlyMessage);
        } catch (Exception e) {
            // 捕获其他可能的异常
            log.error("Token验证过程发生异常：{}", e.getMessage(), e);
            return sendErrorResponse(response, 500, "系统内部错误，请稍后重试");
        }

        // 5. 解析Token中的userId，存入HttpServletRequest的属性中
        try {
            // 注意：生成Token时claims存的是"userId"（Long类型），解析时需对应
            Long userId = claims.get("userId", Long.class);
            if (userId == null || userId <= 0) {
                return sendErrorResponse(response, 401, "Token中用户信息无效");
            }

            // 将userId存入request属性，后续Controller可通过request.getAttribute("userId")获取
            request.setAttribute("userId", userId);
            log.info("Token验证通过，解析userId：{}", userId);
        } catch (Exception e) {
            log.error("解析Token中userId失败：{}", e.getMessage());
            return sendErrorResponse(response, 401, "Token中用户信息格式错误");
        }

        // 6. Token有效，继续执行（放行到Controller）
        return true;
    }

    /**
     * 私有工具方法：Token无效时，返回JSON格式的错误响应（避免前端拿到404/500页面）
     * @param response 响应对象
     * @param code 状态码（401：未授权）
     * @param message 错误信息
     * @return false：终止请求
     * @throws IOException IO异常
     */
    private boolean sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        // 防止中文乱码
        response.setCharacterEncoding("UTF-8");
        // 设置响应格式为JSON
        response.setContentType("application/json;charset=UTF-8");
        // 设置状态码（401：未授权）
        response.setStatus(code);

        // 构建JSON响应（与Result类格式保持一致，使用转义字符确保JSON格式正确）
        String safeMessage = escapeJsonString(message); // 对消息进行转义，避免JSON注入
        String json = "{\"code\":" + code + ",\"message\":\"" + safeMessage + "\",\"data\":null}";

        // 写入响应体
        PrintWriter writer = response.getWriter();
        try {
            writer.write(json);
            writer.flush();
        } finally {
            // 确保资源关闭
            if (writer != null) {
                writer.close();
            }
        }

        // 返回false，终止请求（不再进入Controller）
        return false;
    }
    
    /**
     * 对JSON字符串中的特殊字符进行转义，防止JSON注入
     * @param input 输入字符串
     * @return 转义后的安全字符串
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        // 替换JSON中的特殊字符
        return input
                .replace("\\", "\\\\")  // 反斜杠
                .replace("\"", "\\\"")  // 双引号
                .replace("\b", "\\b")    // 退格
                .replace("\f", "\\f")    // 换页
                .replace("\n", "\\n")    // 换行
                .replace("\r", "\\r")    // 回车
                .replace("\t", "\\t");    // 制表符
    }
}
