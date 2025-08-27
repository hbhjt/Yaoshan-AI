package com.yaoshan.backend.service.impl.user;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoshan.backend.mapper.UserMapper;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {}