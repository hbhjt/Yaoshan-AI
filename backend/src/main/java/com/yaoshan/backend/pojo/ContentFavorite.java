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
public class ContentFavorite {
    private Long favId;           // 收藏ID
    private Long userId;          // 用户ID
    private Long contentId;       // 内容ID
    private LocalDateTime createdTime; // 收藏时间
}