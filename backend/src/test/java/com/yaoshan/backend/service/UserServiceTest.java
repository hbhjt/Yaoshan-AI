package com.yaoshan.backend.service;

import com.yaoshan.backend.exception.LoginFailedException;
import com.yaoshan.backend.exception.PasswordErrorException;
import com.yaoshan.backend.mapper.UserMapper;
import com.yaoshan.backend.pojo.DTO.NormalUserLoginDTO;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.pojo.DTO.UserRegisterDTO;
import com.yaoshan.backend.service.impl.UserServiceImpl;
import com.yaoshan.backend.utils.HttpClientUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpClientUtil httpClientUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.url}")
    private String wechatUrl;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试用户注册功能
    @Test
    void testRegister() {
        // 准备测试数据
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setPhone("13800138000");
        registerDTO.setPassword("password123");
        registerDTO.setConfirmPassword("password123");
        registerDTO.setNickname("测试用户");

        // 模拟mapper行为
        when(userMapper.findByPhone(registerDTO.getPhone())).thenReturn(null);
        when(userMapper.insertUser(any(User.class))).thenReturn(1);

        // 执行测试
        userService.register(registerDTO);

        // 验证结果
        verify(userMapper, times(1)).findByPhone(registerDTO.getPhone());
        verify(userMapper, times(1)).insertUser(any(User.class));
    }

    // 测试手机号已注册的情况
    @Test
    void testRegisterWithExistingPhone() {
        // 准备测试数据
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setPhone("13800138000");
        registerDTO.setPassword("password123");
        registerDTO.setConfirmPassword("password123");

        // 模拟mapper行为
        User existingUser = new User();
        existingUser.setPhone(registerDTO.getPhone());
        when(userMapper.findByPhone(registerDTO.getPhone())).thenReturn(existingUser);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> userService.register(registerDTO));
    }

    // 测试两次输入密码不一致的情况
    @Test
    void testRegisterWithDifferentPasswords() {
        // 准备测试数据
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setPhone("13800138000");
        registerDTO.setPassword("password123");
        registerDTO.setConfirmPassword("differentPassword");

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> userService.register(registerDTO));
    }

    // 测试普通用户登录功能
    @Test
    void testNormalLogin() {
        // 准备测试数据
        NormalUserLoginDTO loginDTO = new NormalUserLoginDTO();
        loginDTO.setPhone("13800138000");
        loginDTO.setPassword("password123");

        // 准备用户数据
        User user = new User();
        user.setPhone(loginDTO.getPhone());
        user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));

        // 模拟mapper行为
        when(userMapper.findByPhone(loginDTO.getPhone())).thenReturn(user);

        // 执行测试
        User result = userService.normalLogin(loginDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(loginDTO.getPhone(), result.getPhone());
    }

    // 测试登录时用户不存在的情况
    @Test
    void testNormalLoginWithNonExistingUser() {
        // 准备测试数据
        NormalUserLoginDTO loginDTO = new NormalUserLoginDTO();
        loginDTO.setPhone("13800138000");
        loginDTO.setPassword("password123");

        // 模拟mapper行为
        when(userMapper.findByPhone(loginDTO.getPhone())).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(LoginFailedException.class, () -> userService.normalLogin(loginDTO));
    }

    // 测试登录时密码错误的情况
    @Test
    void testNormalLoginWithWrongPassword() {
        // 准备测试数据
        NormalUserLoginDTO loginDTO = new NormalUserLoginDTO();
        loginDTO.setPhone("13800138000");
        loginDTO.setPassword("wrongPassword");

        // 准备用户数据
        User user = new User();
        user.setPhone(loginDTO.getPhone());
        user.setPassword(passwordEncoder.encode("correctPassword"));

        // 模拟mapper行为
        when(userMapper.findByPhone(loginDTO.getPhone())).thenReturn(user);

        // 执行测试并验证异常
        assertThrows(PasswordErrorException.class, () -> userService.normalLogin(loginDTO));
    }

    // 测试根据ID查询用户
    @Test
    void testFindById() {
        // 准备测试数据
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setNickname("测试用户");

        // 模拟mapper行为
        when(userMapper.findById(userId)).thenReturn(user);

        // 执行测试
        User result = userService.findById(userId);

        // 验证结果
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    // 测试更新用户信息
    @Test
    void testUpdateUser() {
        // 准备测试数据
        User user = new User();
        user.setUserId(1L);
        user.setNickname("更新后的昵称");
        user.setPhysiqueTags("阴虚体质");

        // 模拟mapper行为
        when(userMapper.updateUser(any(User.class))).thenReturn(1);

        // 执行测试
        userService.updateUser(user);

        // 验证结果
        verify(userMapper, times(1)).updateUser(any(User.class));
        assertNotNull(user.getUpdatedTime());
    }

    // 测试查询所有用户
    @Test
    void testFindAll() {
        // 准备测试数据
        User user1 = new User();
        user1.setUserId(1L);
        user1.setNickname("用户1");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setNickname("用户2");

        List<User> userList = List.of(user1, user2);

        // 模拟mapper行为
        when(userMapper.findAll()).thenReturn(userList);

        // 执行测试
        List<User> result = userService.findAll();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}