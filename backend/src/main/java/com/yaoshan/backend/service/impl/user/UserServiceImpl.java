package com.yaoshan.backend.service.impl.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoshan.backend.dto.UserRegisterParam;
import com.yaoshan.backend.exception.BusinessException;
import com.yaoshan.backend.mapper.UserMapper;
import com.yaoshan.backend.pojo.User;
import com.yaoshan.backend.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User loginByPhone(String phone, String password) {
        // 1. 根据手机号查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = baseMapper.selectOne(queryWrapper);

        // 2. 校验用户是否存在
        if (user == null) {
            throw new BusinessException("手机号未注册");
        }

        // 3. 明文验证密码（仅临时方案，生产环境必须加密！）
        if (!password.equals(user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 4. 脱敏返回（移除密码）
        user.setPassword(null);
        return user;
    }
    @Override
    public User register(UserRegisterParam registerParam) {
        // 1. 校验手机号是否已注册（数据库有uk_phone唯一索引，也可通过代码提前校验）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", registerParam.getPhone());
        User existUser = baseMapper.selectOne(queryWrapper);
        if (existUser != null) {
            throw new BusinessException("手机号已注册");
        }

        // 2. 转换注册参数为User对象
        User user = new User();
        user.setPhone(registerParam.getPhone());
        user.setPassword(registerParam.getPassword()); // 暂时明文存储（生产需加密）
        user.setNickname(registerParam.getNickname());
        // 其他默认值：头像为空、体质未设置、信息未完善（has_complete默认为0）

        // 3. 保存用户到数据库（userId由MyBatis-Plus自动生成UUID）
        boolean saveSuccess = baseMapper.insert(user) > 0;
        if (!saveSuccess) {
            throw new BusinessException("注册失败，请重试");
        }

        // 4. 脱敏返回（移除密码）
        user.setPassword(null);
        return user;
    }
}