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

    @Select("select * from t_user where user_id = #{userId}")
    User findById(@Param("userId") Long userId);

    @Insert("INSERT INTO t_user (openid, phone, nickname, avatar_url,created_time) " +
            "VALUES (#{openid}, #{phone}, #{nickname}, #{avatarUrl},NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "userId") // 正确：匹配 User 类的 userId 字段
    int insertUser(User user);

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