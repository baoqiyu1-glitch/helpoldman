package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.News;
import com.helpoldman.mapper.NewsMapper;
import com.helpoldman.service.NewsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Override
    public List<News> getPublishedNews() {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED")
                   .orderByDesc("publish_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<News> getNewsByType(String newsType) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED")
                   .eq("news_type", newsType)
                   .orderByDesc("publish_time");
        return baseMapper.selectList(queryWrapper);
    }
}