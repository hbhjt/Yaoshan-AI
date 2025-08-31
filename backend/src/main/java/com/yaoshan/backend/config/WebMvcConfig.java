package com.yaoshan.backend.config;

import com.yaoshan.backend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置：注册拦截器，配置拦截规则
 */
@Configuration // 标识为配置类，Spring启动时自动加载
public class WebMvcConfig implements WebMvcConfigurer {

    // 注入JWT拦截器（已通过@Component交给Spring管理）
    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 注册拦截器，配置拦截路径和放行路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 1. 配置需要拦截的路径（所有需要登录的接口，用Ant风格路径匹配）
                .addPathPatterns("/user/profile/**")       // 个人信息接口
                .addPathPatterns("/user/preferences/**")  // 更新偏好接口
                .addPathPatterns("/user/order/**")        // 示例：订单相关接口（需登录）
                // 2. 配置不需要拦截的路径（登录接口、静态资源等）
                .excludePathPatterns("/user/login")        // 普通注册/登录接口
                .excludePathPatterns("/user/wx/login")     // 微信登录接口（登录前无Token，需放行）
                .excludePathPatterns("/user/list")         // 示例：管理员查询所有用户接口（若无需登录可放行，否则注释）
                .excludePathPatterns("/static/**")         // 静态资源（如前端JS/CSS，若有）
                .excludePathPatterns("/swagger-ui/**");    // 若用Swagger文档，放行Swagger路径
    }
}