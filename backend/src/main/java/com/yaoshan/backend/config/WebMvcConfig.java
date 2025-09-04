package com.yaoshan.backend.config;

import com.yaoshan.backend.constant.ApiPathConstant;
import com.yaoshan.backend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类：注册拦截器，配置拦截规则和放行路径
 */
@Configuration // 标识为配置类，Spring启动时自动加载
public class WebMvcConfig implements WebMvcConfigurer {

    // 注入JWT拦截器（已通过@Component交给Spring管理）
    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 注册拦截器，配置拦截路径和放行路径
     * @param registry 拦截器注册表，用于添加拦截器和配置路径规则
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 注册JWT拦截器并配置路径规则
        registry.addInterceptor(jwtInterceptor)
                // 配置需要拦截的路径（所有需要登录认证的接口，用Ant风格路径匹配）
                .addPathPatterns(ApiPathConstant.USER_PROFILE_PATH)       // 个人信息相关接口
                .addPathPatterns(ApiPathConstant.USER_PREFERENCES_PATH)  // 用户偏好设置相关接口
                .addPathPatterns(ApiPathConstant.USER_ORDER_PATH)        // 订单相关接口
                // 配置不需要拦截的路径（公开接口、静态资源等）
                .excludePathPatterns(ApiPathConstant.USER_LOGIN_PATH)        // 普通用户登录接口
                .excludePathPatterns(ApiPathConstant.USER_WX_LOGIN_PATH)     // 微信登录接口
                .excludePathPatterns(ApiPathConstant.USER_REGISTER_PATH)     // 注册接口
                .excludePathPatterns(ApiPathConstant.USER_LIST_PATH)         // 用户列表查询接口（管理员）
                .excludePathPatterns(ApiPathConstant.STATIC_RESOURCE_PATH)   // 静态资源路径
                .excludePathPatterns(ApiPathConstant.SWAGGER_UI_PATH)        // Swagger文档路径
                .excludePathPatterns(ApiPathConstant.OPENAPI_DOC_PATH);      // OpenAPI文档路径
    }
}