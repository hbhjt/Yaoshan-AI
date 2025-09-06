package com.yaoshan.backend.mapper;

import com.yaoshan.backend.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from t_user where openid = #{openid}")
    User findByOpenid(@Param("openid") String openid);

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    @Select("select * from t_user where phone = #{phone}")
    User findByPhone(@Param("phone") String phone);

    /**
     * 根据用户ID查询用户
     * @param userId
     * @return
     */
    @Select("select * from t_user where user_id = #{userId}")
    User findById(@Param("userId") Long userId);

    /**
     * 插入用户
     * @param user
     * @return
     */
    @Insert("INSERT INTO t_user (openid, phone, nickname, avatar_url, password, created_time) " +       
            "VALUES (#{openid}, #{phone}, #{nickname}, #{avatarUrl}, #{password}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "userId") // 正确：匹配 User 类的 userId 字段
    int insertUser(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Update("    UPDATE t_user\n" +
            "    SET phone = #{phone},\n" +
            "        nickname = #{nickname},\n" +
            "        avatar_url = #{avatarUrl},\n" +
            "        physique_tags = #{physiqueTags},\n" +
            "        dietary_restrictions = #{dietaryRestrictions},\n" +
            "        taste_preferences = #{tastePreferences},\n" +
            "        updated_time = NOW()\n" +
            "    WHERE openid = #{openid}")
    int updateUser(User user);

    /**
     * 查询全部用户信息
     * @return
     */
    @Select("select * from t_user")
    List<User> findAll();
}