package com.yaoshan.backend.constant;

/**
 * API路径常量类
 * 用于统一管理API路径，避免硬编码
 */
public class ApiPathConstant {
    /**
     * 拦截器相关路径 - 需要认证的路径
     */
    // 用户相关路径
    public static final String USER_PROFILE_PATH = "/user/profile/**";     // 个人信息相关接口
    public static final String USER_PREFERENCES_PATH = "/user/preferences/**";  // 用户偏好设置相关接口
    public static final String USER_ORDER_PATH = "/user/order/**";        // 订单相关接口
    
    /**
     * 拦截器相关路径 - 无需认证的路径（公开接口）
     */
    // 用户认证接口
    public static final String USER_LOGIN_PATH = "/user/login";            // 普通用户登录接口
    public static final String USER_WX_LOGIN_PATH = "/user/wx/login";     // 微信登录接口
    public static final String USER_REGISTER_PATH = "/user/register";      // 用户注册接口
    public static final String USER_LIST_PATH = "/user/list";             // 用户列表查询接口（管理员）
    
    // 静态资源和文档路径
    public static final String STATIC_RESOURCE_PATH = "/static/**";       // 静态资源路径
    public static final String SWAGGER_UI_PATH = "/swagger-ui/**";        // Swagger文档路径
    public static final String OPENAPI_DOC_PATH = "/v3/api-docs/**";      // OpenAPI文档路径
}