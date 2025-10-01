package com.yaoshan.backend.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户个人资料视图对象
 * 用于返回用户非敏感信息给前端
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileVO implements Serializable {
    private Long userId;          // 用户唯一ID
    private String nickname;      // 用户昵称
    private String avatarUrl;     // 用户头像URL
    private String phone;         // 手机号（可根据需要决定是否返回）
    private Integer status;       // 用户状态 1-正常,0-禁用
    private String physiqueTags;  // 体质标签
    private String dietaryRestrictions; // 饮食禁忌
    private String tastePreferences; // 口味偏好
    private LocalDateTime createdTime; // 创建时间
    private LocalDateTime updatedTime; // 更新时间
}