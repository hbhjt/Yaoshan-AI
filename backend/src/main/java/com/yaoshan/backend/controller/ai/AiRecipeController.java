package com.yaoshan.backend.controller.ai;

import com.yaoshan.backend.common.Result;
import com.yaoshan.backend.pojo.AiRecipe;
import com.yaoshan.backend.service.ai.AiRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class AiRecipeController {  // 修改为public访问修饰符
    private final AiRecipeService service;

    @PostMapping
    public Result<Boolean> create(@RequestBody AiRecipe entity) {  // 返回类型改为Result<Boolean>
        return Result.success(service.save(entity));  // 用Result.success()包装结果
    }

    @GetMapping
    public Result<List<AiRecipe>> getAll() {  // 返回类型改为Result<List<AiRecipe>>
        return Result.success(service.list());  // 用Result.success()包装结果
    }
}
