package com.yaoshan.backend.service.impl.ai;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoshan.backend.mapper.AiRecipeMapper;
import com.yaoshan.backend.pojo.AiRecipe;
import com.yaoshan.backend.service.ai.AiRecipeService;
import org.springframework.stereotype.Service;

@Service
public class AiRecipeServiceImpl extends ServiceImpl<AiRecipeMapper, AiRecipe> implements AiRecipeService {}
