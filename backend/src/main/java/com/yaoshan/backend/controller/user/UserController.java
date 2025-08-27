package com.yaoshan.backend.controller.user;


import com.yaoshan.backend.common.Result;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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