package com.helpoldman.service;

import java.util.List;
import java.util.Map;

public interface VolunteerService {
    
    /**
     * 获取志愿者首页数据
     */
    Map<String, Object> getVolunteerDashboard(Long userId);
    
    /**
     * 获取志愿者服务统计
     */
    Map<String, Object> getVolunteerStats(Long userId);
    
    /**
     * 获取志愿者任务列表
     */
    Map<String, Object> getVolunteerTasks(Long userId, Integer page, Integer size);
    
    /**
     * 获取志愿者服务类型
     */
    List<Map<String, Object>> getVolunteerServiceTypes();
    
    /**
     * 获取志愿者页面文字配置
     */
    Map<String, Object> getVolunteerTextConfig();
    
    /**
     * 获取志愿者个人资料
     */
    Map<String, Object> getVolunteerProfile(Long userId);
    
    /**
     * 更新志愿者个人资料
     */
    boolean updateVolunteerProfile(Long userId, Map<String, Object> profileData);
    
    /**
     * 获取志愿者代购订单列表
     */
    Map<String, Object> getShoppingOrders(Long userId, Integer page, Integer size);
    
    /**
     * 接受代购订单
     */
    boolean acceptShoppingOrder(Long userId, String orderId);
    
    /**
     * 拒绝代购订单
     */
    boolean rejectShoppingOrder(Long userId, String orderId);
    
    /**
     * 获取家政服务任务列表
     */
    List<Map<String, Object>> getHouseworkTasks(Long userId);
    
    /**
     * 接受家政服务任务
     */
    boolean acceptHouseworkTask(Long userId, String taskId);
    
    /**
     * 拒绝家政服务任务
     */
    boolean rejectHouseworkTask(Long userId, String taskId);
    
    /**
     * 获取培训课程列表
     */
    List<Map<String, Object>> getTrainingCourses(Long userId);
    
    /**
     * 报名培训课程
     */
    boolean registerTrainingCourse(Long userId, String courseId);
    
    /**
     * 获取申请服务信息
     */
    Map<String, Object> getApplyInfo(Long userId);
    
    /**
     * 提交申请服务
     */
    boolean submitApply(Long userId, Map<String, Object> applyData);

    /**
     * 获取技能列表
     */
    List<Map<String, Object>> getSkills();
    
    /**
     * 完成订单
     */
    boolean completeOrder(Long userId, String orderId);
    
    /**
     * 获取志愿者已接任务
     */
    List<Map<String, Object>> getMyTasks(Long userId);
    
    /**
     * 获取订单详情
     */
    Map<String, Object> getOrderDetails(String orderId);
}