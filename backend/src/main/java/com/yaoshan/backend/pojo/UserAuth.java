package com.yaoshan.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    private Long authId;          // 凭证ID
    private Long userId;          // 用户ID
    private Integer authType;     // 登录方式：1-微信，2-手机号
    private String identity;      // openid 或 账号
    private String credential;    // 加密后的密码（微信登录可空）
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}