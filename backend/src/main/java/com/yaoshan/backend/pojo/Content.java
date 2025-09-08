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
public class Content {
    private Long contentId;       // 内容ID
    private String title;         // 标题
    private String description;   // 简介
    private String contentType;   // 类型 article/video
    private String imageUrl;      // 图片URL
    private String videoUrl;      // 视频URL
    private Long authorId;        // 作者ID
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}