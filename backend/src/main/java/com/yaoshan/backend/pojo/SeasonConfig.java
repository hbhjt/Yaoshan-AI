package com.yaoshan.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("season_config")
public class SeasonConfig {

    @TableId(value = "season_id", type = IdType.ASSIGN_UUID)
    private String seasonId;

    private String seasonName;
    private String season; // spring / summer / autumn / winter
    private String solarTerm;
    private String healthTips;
    private String suitableIngredients; // JSON 字符串 ["薏米","冬瓜"]
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}