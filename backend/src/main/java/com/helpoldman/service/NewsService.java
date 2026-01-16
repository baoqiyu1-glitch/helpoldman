package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.News;
import java.util.List;

public interface NewsService extends IService<News> {
    
    /**
     * 获取已发布的新闻列表
     */
    List<News> getPublishedNews();
    
    /**
     * 根据类型获取新闻列表
     */
    List<News> getNewsByType(String newsType);
}