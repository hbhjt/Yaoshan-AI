package com.yaoshan.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yaoshan.backend.constant.MessageConstant;
import com.yaoshan.backend.exception.LoginFailedException;
import com.yaoshan.backend.mapper.UserMapper;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.pojo.UserLoginDTO;
import com.yaoshan.backend.service.UserService;
import com.yaoshan.backend.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.url}")
    private String wechatUrl;
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    public User findByOpenid(String openid) {

        return userMapper.findByOpenid(openid);
    }

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
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
            String defaultNickname = "微信用户_" + openid.substring(0, 6);
            String defaultAvatar = "https://images.icon-icons.com/1488/PNG/512/5368-wechat_102582.png"; // 替换为你的默认头像地址
            user = User.builder() // 将构建的对象赋值给 user
                    .openid(openid)
                    .nickname(defaultNickname)
                    .avatarUrl(defaultAvatar)
                    .createdTime(LocalDateTime.now())
                    .updatedTime(LocalDateTime.now())
                    .build();
            userMapper.insertUser(user);
            log.info("新用户自动注册：userId={}, openid={}", user.getUserId(), openid); // 优化：打印注册日志
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
        try {
            //调用微信接口服务，获得当前微信用户的openid
            Map<String,String> map = new HashMap<>();
            map.put("appid",appid);
            map.put("secret",secret);
            map.put("js_code",code);
            map.put("grant_type","authorization_code");
            String json = HttpClientUtil.doGet(wechatUrl, map);
            log.info("微信接口请求参数：{}，响应：{}", map, json);

            JSONObject jsonObject = JSON.parseObject(json);

            // 优化3：判断微信接口是否返回错误
            if (jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") != 0) {
                String errMsg = jsonObject.getString("errmsg");
                log.error("微信接口错误：errcode={}, errmsg={}", jsonObject.getInteger("errcode"), errMsg);
                return null;
            }

            return jsonObject.getString("openid");
        } catch (Exception e) {
            log.error("调用微信接口失败（code={}）：", code, e); // 优化4：打印code便于定位
            return null;
        }

    }
}