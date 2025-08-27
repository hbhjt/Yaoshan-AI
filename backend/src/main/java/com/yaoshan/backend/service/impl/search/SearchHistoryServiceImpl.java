package com.yaoshan.backend.service.impl.search;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoshan.backend.mapper.SearchHistoryMapper;
import com.yaoshan.backend.pojo.SearchHistory;
import com.yaoshan.backend.service.search.SearchHistoryService;
import org.springframework.stereotype.Service;

@Service
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements SearchHistoryService {}