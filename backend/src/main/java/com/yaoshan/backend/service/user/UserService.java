package com.yaoshan.backend.service.user;

import com.yaoshan.backend.entity.User;

public interface UserService {
    User register(User user);
    User login(String username, String password);
}