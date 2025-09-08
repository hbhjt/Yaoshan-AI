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
public class Recommendation {
    private Long recId;           // 推荐记录ID
    private Long userId;          // 用户ID
    private Long profileId;       // 健康画像ID
    private Long recipeId;        // 药膳/奶茶ID
    private LocalDateTime recommendTime; // 推荐时间
}