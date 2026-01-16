package com.helpoldman.controller;

import com.helpoldman.entity.News;
import com.helpoldman.service.NewsService;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 获取新闻列表
     */
    @GetMapping
    public Result getNewsList() {
        try {
            List<News> newsList = newsService.getPublishedNews();
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error("获取新闻列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据类型获取新闻
     */
    @GetMapping("/type/{type}")
    public Result getNewsByType(@PathVariable String type) {
        try {
            List<News> newsList = newsService.getNewsByType(type);
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error("获取新闻失败: " + e.getMessage());
        }
    }

    /**
     * 获取新闻详情
     */
    @GetMapping("/{id}")
    public Result getNewsDetail(@PathVariable Long id) {
        try {
            News news = newsService.getById(id);
            if (news == null) {
                return Result.error("新闻不存在");
            }
            return Result.success(news);
        } catch (Exception e) {
            return Result.error("获取新闻详情失败: " + e.getMessage());
        }
    }
}