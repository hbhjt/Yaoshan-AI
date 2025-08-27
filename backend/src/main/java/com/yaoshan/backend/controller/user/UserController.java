package com.yaoshan.backend.controller.user;


import com.yaoshan.backend.common.Result;
import com.yaoshan.backend.dto.UserLoginParam;
import com.yaoshan.backend.dto.UserRegisterParam;
import com.yaoshan.backend.exception.BusinessException;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Result<User> register(@Validated @RequestBody UserRegisterParam registerParam) {
        try {
            User user = userService.register(registerParam);
            return Result.success(user);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
    // 手机号登录接口
    @PostMapping("/login/phone")
    public Result<User> loginByPhone(@Validated @RequestBody UserLoginParam loginParam) {
        try {
            User user = userService.loginByPhone(loginParam.getPhone(), loginParam.getPassword());
            return Result.success(user);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody User user) {
        return Result.success(userService.save(user));
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable String id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping
    public Result<List<User>> getAll() {
        return Result.success(userService.list());
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.success(userService.removeById(id));
    }
}