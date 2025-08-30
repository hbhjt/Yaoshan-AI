package com.yaoshan.backend.controller;

import com.yaoshan.backend.pojo.Result;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.pojo.UserLoginDTO;
import com.yaoshan.backend.pojo.UserLoginVO;
import com.yaoshan.backend.service.UserService;
import com.yaoshan.backend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.token-name}")
    private String tokenName;

    /**
     * 用户注册
     * @return
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody User user){
        log.info("用户注册:{}",user);

        //调用service注册用户信息
        userService.login(user);

        return Result.success();
    }

    /**
     * 用户微信登录
     * @return
     */
    @PostMapping("/user/login")
    public Result<UserLoginVO> wxlogin(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录:{}",userLoginDTO.getCode());

        //调用微信登录
        User user = userService.wxlogin(userLoginDTO);

        //调用jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getUserId());
        String token = JwtUtil.createJWT(secret, expiration, claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getUserId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    // 获取当前用户信息（依赖JWT Filter提前解析 userId）
    @GetMapping("/profile")
    public User getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("未登录或Token无效");
        }
        return userService.findById(userId);
    }

    // 更新用户偏好（体质标签 / 饮食禁忌 / 口味偏好）
    @PutMapping("/preferences")
    public Map<String, Object> updatePreferences(HttpServletRequest request,
                                                 @RequestBody User update) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("未登录或Token无效");
        }
        update.setUserId(userId);
        userService.updateUser(update);
        return Map.of("status", 200, "message", "用户偏好更新成功");
    }

    // 管理员或测试用：获取所有用户
    @GetMapping("/list")
    public Result findAllUsers() {
        log.info("查询全部用户信息");
        //调用service查询用户信息
        List<User> userList = userService.findAll();
        return Result.success(userList);
    }
}