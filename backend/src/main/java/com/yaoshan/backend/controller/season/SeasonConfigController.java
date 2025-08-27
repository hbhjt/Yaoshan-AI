package com.yaoshan.backend.controller.season;

import com.yaoshan.backend.common.Result;
import com.yaoshan.backend.pojo.SeasonConfig;
import com.yaoshan.backend.service.season.SeasonConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
public class SeasonConfigController {  // 修改为public访问修饰符
    private final SeasonConfigService service;

    @PostMapping
    public Result<Boolean> create(@RequestBody SeasonConfig entity) {
        return Result.success(service.save(entity));
    }

    @GetMapping
    public Result<List<SeasonConfig>> getAll() {
        return Result.success(service.list());
    }
}
