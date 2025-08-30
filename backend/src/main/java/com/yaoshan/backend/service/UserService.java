package com.yaoshan.backend.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yaoshan.backend.constant.MessageConstant;
import com.yaoshan.backend.exception.LoginFailedException;
import com.yaoshan.backend.mapper.UserMapper;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.pojo.UserLoginDTO;
import com.yaoshan.backend.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.url}")
    private String wechatUrl;
    @Autowired
    private UserMapper userMapper;

    public User findByOpenid(String openid) {
        return userMapper.findByOpenid(openid);
    }

    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    /**
     * 查询全部用户信息
     * @return
     */
    public List<User> findAll() {

        return userMapper.findAll();
    }

    @Transactional
    public User findOrCreateByOpenid(String openid) {
        User user = userMapper.findByOpenid(openid);
        if (user != null) return user;

        User newUser = new User();
        newUser.setOpenid(openid);
        newUser.setNickname("微信用户");
        newUser.setCreatedTime(LocalDateTime.now());
        newUser.setUpdatedTime(LocalDateTime.now());
        userMapper.insertUser(newUser);
        return newUser;
    }

    @Transactional
    public void updateUser(User user) {
        user.setUpdatedTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    /**
     * 用户注册
     * @param user
     */
    public void login(User user) {
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        userMapper.insertUser(user);
    }

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxlogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空，如果为空表示登录失败，抛出异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断是否为新用户
        User user = userMapper.findByOpenid(openid);

        //如果是新用户，自动完成注册
        if(user == null) {
            user = User.builder() // 将构建的对象赋值给 user
                    .openid(openid)
                    .nickname("微信用户") // 建议补充默认昵称，与 findOrCreateByOpenid 方法保持一致
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .build();
            userMapper.insertUser(user);
        }


        //返回这个用户对象
        return user;
    }

    /**
     *  调用微信接口服务，获得当前微信用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        //调用微信接口服务，获得当前微信用户的openid
        Map<String,String> map = new HashMap<>();
        map.put("appid",appid);
        map.put("secret",secret);
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(wechatUrl, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}