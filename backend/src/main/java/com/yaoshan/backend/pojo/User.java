package com.yaoshan.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Long userId;               // 用户ID
    private String openid;             // 微信OpenID
    private String phone;              // 手机号
    private String password;           // 密码
    private String nickname;           // 昵称
    private String avatarUrl;          // 头像URL
    private String physiqueTags;       // 体质标签（如阴虚/阳虚）
    private String dietaryRestrictions;// 饮食禁忌
    private String tastePreferences;   // 口味偏好
    private LocalDateTime createdTime; // 创建时间
    private LocalDateTime updatedTime; // 更新时间
}
