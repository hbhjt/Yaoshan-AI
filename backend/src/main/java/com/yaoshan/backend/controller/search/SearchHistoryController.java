package com.yaoshan.backend.controller.search;


import com.yaoshan.backend.common.Result;
import com.yaoshan.backend.pojo.SearchHistory;
import com.yaoshan.backend.service.search.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchHistoryController {  // 修改为public访问修饰符
    private final SearchHistoryService service;

    @PostMapping
    public Result<Boolean> create(@RequestBody SearchHistory entity) {  // 返回类型改为Result<Boolean>
        return Result.success(service.save(entity));  // 用Result.success()包装结果
    }

    @GetMapping
    public Result<List<SearchHistory>> getAll() {  // 返回类型改为Result<List<SearchHistory>>
        return Result.success(service.list());  // 用Result.success()包装结果
    }
}
