package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.TrainingCourse;
import com.helpoldman.utils.Result;

import java.util.List;

public interface TrainingCourseService extends IService<TrainingCourse> {
    
    /**
     * 用户报名培训课程
     */
    Result registerCourse(Long userId, Long courseId);
    
    /**
     * 获取热门培训课程
     */
    List<TrainingCourse> getPopularCourses();
    
    /**
     * 获取用户已报名的课程列表
     */
    List<TrainingCourse> getRegisteredCourses(Long userId);
    
    /**
     * 获取用户已报名的课程数量
     */
    Integer getRegisteredCourseCount(Long userId);
    
    /**
     * 获取用户即将参加的课程
     */
    List<TrainingCourse> getUpcomingRegisteredCourses(Long userId);
    
    /**
     * 获取用户已完成的课程
     */
    List<TrainingCourse> getCompletedRegisteredCourses(Long userId);
    
    /**
     * 检查用户是否已报名某课程
     */
    boolean isUserRegisteredForCourse(Long userId, Long courseId);
    
    /**
     * 更新课程状态
     */
    Result updateCourseStatus(Long courseId, String status);
}