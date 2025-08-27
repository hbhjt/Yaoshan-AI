package com.yaoshan.backend.pojo;


import lombok.Data;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;


@Data
@TableName("search_history")
public class SearchHistory {

    @TableId(value = "history_id", type = IdType.ASSIGN_UUID)
    private String historyId;

    private String userId;
    private String keyword;
    private String contentTypes;
    private String filters; // JSON 格式字符串
    private String sortType; // relevance / hot / new
    private LocalDateTime searchTime;
    private Boolean isDeleted;
}