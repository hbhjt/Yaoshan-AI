package com.yaoshan.backend.controller;

import com.yaoshan.backend.exception.LoginFailedException;
import com.yaoshan.backend.exception.PasswordErrorException;
import com.yaoshan.backend.pojo.DTO.NormalUserLoginDTO;
import com.yaoshan.backend.pojo.Result;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.pojo.DTO.UserLoginDTO;
import com.yaoshan.backend.pojo.VO.UserLoginVO;
import com.yaoshan.backend.pojo.VO.UserProfileVO;
import com.yaoshan.backend.pojo.DTO.UserRegisterDTO;
import com.yaoshan.backend.service.UserService;
import com.yaoshan.backend.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户操作控制器
 * 提供用户注册、登录、信息获取及偏好设置等功能
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.token-name}")
    private String tokenName;

    /**
     * 用户注册接口
     * @param registerDTO 注册信息
     * @return 注册结果信息
     */
    @PostMapping("/register")
    public Result<String> userRegister(@RequestBody UserRegisterDTO registerDTO){
        log.info("用户注册:{}", registerDTO);
        try {
            //参数校验
            if (registerDTO.getPhone() == null || registerDTO.getPhone().isEmpty()) {
                return Result.error("手机号不能为空");
            }
            if (registerDTO.getPassword() == null || registerDTO.getPassword().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (registerDTO.getConfirmPassword() == null || registerDTO.getConfirmPassword().isEmpty()) {
                return Result.error("确认密码不能为空");
            }
            
            //调用service注册用户信息
            userService.register(registerDTO);
            return Result.success("注册成功");
        } catch (IllegalArgumentException e) {
            log.error("用户注册参数校验失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage(), e);
            return Result.error("注册失败，请稍后重试");
        }
    }

    /**
     * 微信登录接口
     * @param userLoginDTO 包含微信登录code的请求体
     * @return 登录结果，包含用户信息和JWT令牌
     */
    @PostMapping("/wx/login")
    public Result<UserLoginVO> wxlogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信用户登录请求");
        try {
            //参数校验
            if (userLoginDTO == null || userLoginDTO.getCode() == null || userLoginDTO.getCode().isEmpty()) {
                log.warn("微信登录失败：code为空");
                return Result.error("登录失败，请重试");
            }
            
            log.info("微信登录，code: {}", userLoginDTO.getCode());
            
            // 1. 调用Service获取用户信息
            User user = userService.wxlogin(userLoginDTO);
            
            if (user == null) {
                log.error("微信登录失败：获取用户信息失败");
                return Result.error("登录失败，请重试");
            }

            // 2. 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            String token = jwtUtil.createJWT(claims);

            // 3. 构建返回VO
            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(user.getUserId())
                    .openid(user.getOpenid())
                    .token(token)
                    .build();
            
            log.info("微信用户登录成功：userId={}", user.getUserId());
            return Result.success(userLoginVO);
        } catch (LoginFailedException e) {
            log.error("微信登录失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("微信登录异常：", e);
            return Result.error("系统繁忙，请稍后再试");
        }
    }

    /**
     * 基础登录接口
     * @param loginDTO 登录信息
     * @return 登录结果，包含用户信息和JWT令牌
     */
    @PostMapping("/normal/login")
    public Result<UserLoginVO> normalLogin(@RequestBody NormalUserLoginDTO loginDTO) {
        log.info("用户登录手机号={}", loginDTO.getPhone());
        try {
            //参数校验
            if (loginDTO.getPhone() == null || loginDTO.getPhone().isEmpty()) {
                return Result.error("手机号不能为空");
            }
            if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
                return Result.error("密码不能为空");
            }
            
            // 调用service层的普通登录方法
            User user = userService.normalLogin(loginDTO);
            
            // 判断用户是否为空
            if (user == null) {
                log.error("登录失败：用户不存在或登录信息有误");
                return Result.error("登录失败，请重试");
            }

            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            String token = jwtUtil.createJWT(claims);
            
            // 封装返回结果
            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(user.getUserId())
                    .token(token)
                    .openid(user.getOpenid())
                    .build();
            
            return Result.success(userLoginVO);
        } catch (LoginFailedException e) {
            log.error("登录失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (PasswordErrorException e) {
            log.error("密码错误：{}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常：", e);
            return Result.error("系统繁忙，请稍后再试");
        }
    }
    
    /**
     * 用户信息查询接口
     * 获取当前登录用户的详细信息
     */
    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            
            // 验证用户是否已登录
            if (userId == null) {
                log.warn("用户信息查询失败：未登录或Token无效");
                return Result.error("未登录或Token无效");
            }
            
            log.info("用户信息查询请求：userId={}", userId);
            
            // 查询用户信息
            User user = userService.findById(userId);
            
            if (user == null) {
                log.error("用户信息查询失败：用户不存在 userId={}", userId);
                return Result.error("用户不存在");
            }
            
            // 构建安全的用户信息VO，避免返回敏感信息
            UserProfileVO userProfileVO = UserProfileVO.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .avatarUrl(user.getAvatarUrl())
                    .phone(user.getPhone())
                    .status(user.getStatus())
                    .physiqueTags(user.getPhysiqueTags())
                    .dietaryRestrictions(user.getDietaryRestrictions())
                    .tastePreferences(user.getTastePreferences())
                    .createdTime(user.getCreatedTime())
                    .updatedTime(user.getUpdatedTime())
                    .build();
            
            log.info("用户信息查询成功：userId={}", userId);
            return Result.success(userProfileVO);
        } catch (Exception e) {
            log.error("用户信息查询异常：", e);
            return Result.error("系统繁忙，请稍后再试");
        }
    }

    /**
     * 更新用户偏好设置（体质标签 / 饮食禁忌 / 口味偏好）
     */
    @PutMapping("/preferences")
    public Result<String> updatePreferences(HttpServletRequest request, @RequestBody User update) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error("未登录或Token无效");
            }
            // 获取当前用户信息，确保只更新偏好相关字段
            User currentUser = userService.findById(userId);
            if (currentUser == null) {
                return Result.error("用户不存在");
            }
            
            // 只更新体质标签、饮食禁忌和口味偏好
            currentUser.setPhysiqueTags(update.getPhysiqueTags());
            currentUser.setDietaryRestrictions(update.getDietaryRestrictions());
            currentUser.setTastePreferences(update.getTastePreferences());
            
            userService.updateUser(currentUser);
            log.info("用户ID: {} 偏好设置更新成功", userId);
            return Result.success("用户偏好更新成功");
        } catch (Exception e) {
            log.error("用户偏好更新失败: {}", e.getMessage(), e);
            return Result.error("用户偏好更新失败，请稍后重试");
        }
    }

    // 管理员或测试用：获取所有用户
    @GetMapping("/list")
    public Result<List<User>> findAllUsers() {
        log.info("查询全部用户信息");
        //调用service查询用户信息
        List<User> userList = userService.findAll();
        log.info("成功查询到{}条用户数据", userList.size());
        return Result.success(userList);
    }
}