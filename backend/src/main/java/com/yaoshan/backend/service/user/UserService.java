package com.yaoshan.backend.service.user;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoshan.backend.dto.UserRegisterParam;
import com.yaoshan.backend.pojo.User;

public interface UserService extends IService<User> {

    // 新增登录方法
    User loginByPhone(String phone, String password);
    // 新增注册方法
    User register(UserRegisterParam registerParam);
}