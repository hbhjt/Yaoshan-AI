package com.yaoshan.backend.controller;

import com.yaoshan.backend.exception.LoginFailedException;
import com.yaoshan.backend.pojo.Result;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.pojo.UserLoginDTO;
import com.yaoshan.backend.pojo.UserLoginVO;
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

/**
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //初始化日历对象
    private static Logger log = LoggerFactory.getLogger(UserController.class);
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
     * 用户注册
     * @return
     */
    @PostMapping("/register")
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
    @PostMapping("/wx/login")
    public Result<UserLoginVO> wxlogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信用户登录:{}", userLoginDTO.getCode());
        try {
            // 1. 调用Service获取用户信息
            User user = userService.wxlogin(userLoginDTO);

            // 2. 生成JWT令牌（只传claims，其他参数由JwtUtil从JwtConfig中获取）
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            String token = jwtUtil.createJWT(claims); // 修正：用注入的jwtUtil实例调用，只传claims

            // 3. 构建返回VO
            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(user.getUserId())
                    .openid(user.getOpenid())
                    .token(token)
                    .build();
            return Result.success(userLoginVO);
        } catch (LoginFailedException e) {
            log.error("微信登录失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("微信登录异常：", e);
            return Result.error("系统繁忙，请稍后再试");
        }
    }

    //TODO 获取当前用户信息（依赖JWT Filter提前解析 userId）
//    @GetMapping("/profile")
//    public User getProfile(HttpServletRequest request) {
//        Long userId = (Long) request.getAttribute("userId");
//        if (userId == null) {
//            throw new RuntimeException("未登录或Token无效");
//        }
//        return userService.findById(userId);
//    }


    // TODO 更新用户偏好（体质标签 / 饮食禁忌 / 口味偏好）
//    @PutMapping("/preferences")
//    public Map<String, Object> updatePreferences(HttpServletRequest request,
//                                                 @RequestBody User update) {
//        Long userId = (Long) request.getAttribute("userId");
//        if (userId == null) {
//            throw new RuntimeException("未登录或Token无效");
//        }
//        update.setUserId(userId);
//        userService.updateUser(update);
//        return Map.of("status", 200, "message", "用户偏好更新成功");
//    }

    // 管理员或测试用：获取所有用户
    @GetMapping("/list")
    public Result findAllUsers() {
        log.info("查询全部用户信息");
        //调用service查询用户信息
        List<User> userList = userService.findAll();
        return Result.success(userList);
    }
}