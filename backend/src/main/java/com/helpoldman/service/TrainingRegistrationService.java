package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.TrainingRegistration;
import com.helpoldman.utils.Result;
import java.util.List;

public interface TrainingRegistrationService extends IService<TrainingRegistration> {
    
    /**
     * 用户报名培训课程
     */
    Result registerCourse(Long userId, Long courseId);
    
    /**
     * 取消报名
     */
    Result cancelRegistration(Long userId, Long courseId);
    
    /**
     * 检查用户是否已报名
     */
    boolean isUserRegistered(Long userId, Long courseId);
    
    /**
     * 检查用户是否已报名课程（别名方法）
     */
    boolean isUserRegisteredForCourse(Long userId, Long courseId);
    
    /**
     * 根据用户ID获取已报名课程列表
     */
    List<TrainingRegistration> getRegisteredCoursesByUserId(Long userId);
    
    /**
     * 根据用户ID获取已报名课程数量
     */
    int getRegisteredCourseCountByUserId(Long userId);
    
    /**
     * 根据用户ID获取即将开始的已报名课程
     */
    List<TrainingRegistration> getUpcomingRegisteredCoursesByUserId(Long userId);
    
    /**
     * 根据用户ID获取已完成的已报名课程
     */
    List<TrainingRegistration> getCompletedRegisteredCoursesByUserId(Long userId);
}