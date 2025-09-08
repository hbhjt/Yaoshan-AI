package com.yaoshan.backend.service;


import com.yaoshan.backend.pojo.DTO.NormalUserLoginDTO;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.pojo.DTO.UserLoginDTO;
import com.yaoshan.backend.pojo.DTO.UserRegisterDTO;

import java.util.List;

public interface UserService {


    void login(User user);

    User wxlogin(UserLoginDTO userLoginDTO);

    User findById(Long userId);

    void updateUser(User update);

    List<User> findAll();

    User normalLogin(NormalUserLoginDTO loginDTO);

    /**
     * 用户注册
     * @param registerDTO 注册信息
     */
    void register(UserRegisterDTO registerDTO);
}