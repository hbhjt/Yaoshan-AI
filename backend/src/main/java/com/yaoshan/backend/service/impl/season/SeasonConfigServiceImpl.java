package com.yaoshan.backend.service.impl.season;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoshan.backend.mapper.SeasonConfigMapper;
import com.yaoshan.backend.pojo.SeasonConfig;
import com.yaoshan.backend.service.season.SeasonConfigService;
import org.springframework.stereotype.Service;

@Service
public class SeasonConfigServiceImpl extends ServiceImpl<SeasonConfigMapper, SeasonConfig> implements SeasonConfigService {}