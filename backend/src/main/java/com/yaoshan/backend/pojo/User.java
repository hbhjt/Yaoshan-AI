package com.yaoshan.backend.pojo;


import lombok.Data;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;

@Data
@TableName("user")
public class User {

    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    private String phone;
    private String password;
    private String nickname;
    private String avatar;
    private String constitution;
    private String flavorPrefer;
    private Boolean hasComplete;

    // JSON 字段，建议存 String，使用时再转 List<String>
    private String dietTaboo;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
