package com.yaoshan.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;



@Data
@TableName("ai_recipe")
public class AiRecipe {

    @TableId(value = "recipe_id", type = IdType.ASSIGN_UUID)
    private String recipeId;

    private String userId;
    private String generateType; // herbal_meal / herbal_milk_tea
    private String recipeName;
    private String ingredients;  // JSON [{"name":"银耳","amount":"15g","effect":"滋阴润燥"}]
    private String makeSteps;    // JSON ["步骤1","步骤2"]
    private String adaptEfficacy;
    private String tabooTips;
    private String season;
    private String extraDemand;
    private String cover;
    private LocalDateTime generateTime;
    private Boolean isDeleted;
}