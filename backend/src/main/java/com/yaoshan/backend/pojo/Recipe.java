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
public class Recipe {
    private Long id;              // 主键
    private Integer type;         // 0=药膳,1=奶茶
    private String name;          // 名称
    private String intro;         // 简介
    private String taboo;         // 忌口
    private String effect;        // 功效/主治
    private String suitableTime;  // 适宜饮用时间
    private String method;        // 做法
    private String tags;          // JSON字符串，功能标签
    private LocalDateTime createTime; // 创建时间
}