package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helpoldman.entity.TrainingCourse;
import com.helpoldman.entity.TrainingRegistration;
import com.helpoldman.entity.User;
import com.helpoldman.service.TrainingCourseService;
import com.helpoldman.service.TrainingRegistrationService;
import com.helpoldman.service.UserService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TrainingController {
    
    @Autowired
    private TrainingCourseService trainingCourseService;
    
    @Autowired
    private TrainingRegistrationService trainingRegistrationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // ========== 培训课程管理 ==========
    
    /**
     * 获取培训课程列表
     */
    @GetMapping("/training-courses")
    public Result getTrainingCourses(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String category,
                                    @RequestParam(required = false) String status) {
        QueryWrapper<TrainingCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("course_date", "start_time");
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        // 修复：使用服务类的list方法获取课程列表
        List<TrainingCourse> courseList = trainingCourseService.list(wrapper);
        int start = (page - 1) * size;
        int end = Math.min(start + size, courseList.size());
        List<TrainingCourse> pagedList = courseList.subList(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pagedList);
        result.put("total", courseList.size());
        result.put("size", size);
        result.put("current", page);
        result.put("pages", (int) Math.ceil((double) courseList.size() / size));
        
        return Result.success(result);
    }

    /**
     * 获取课程详情
     */
    @GetMapping("/training-courses/{id}")
    public Result getCourseById(@PathVariable Long id) {
        // 修复：使用服务类的getById方法获取课程详情
        TrainingCourse course = trainingCourseService.getById(id);
        if (course == null) {
            return Result.error("课程不存在");
        }
        return Result.success(course);
    }

    /**
     * 创建培训课程
     */
    @PostMapping("/training-courses")
    public Result createCourse(@RequestBody TrainingCourse course) {
        course.setCurrentParticipants(0);
        course.setStatus("UPCOMING");
        // 修复：使用服务类的save方法创建课程
        trainingCourseService.save(course);
        return Result.success("创建课程成功");
    }

    /**
     * 更新培训课程
     */
    @PutMapping("/training-courses/{id}")
    public Result updateCourse(@PathVariable Long id, @RequestBody TrainingCourse course) {
        course.setId(id);
        // 修复：使用服务类的updateById方法更新课程
        trainingCourseService.updateById(course);
        return Result.success("更新课程成功");
    }

    // ========== 培训报名管理 ==========
    
    /**
     * 报名培训课程
     */
    @PostMapping("/training-registrations")
    public Result registerCourse(@RequestHeader("Authorization") String token,
                                @RequestBody Map<String, Long> params) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            Long courseId = params.get("courseId");
            
            TrainingCourse course = trainingCourseService.getById(courseId);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            if (course.getCurrentParticipants() >= course.getMaxParticipants()) {
                return Result.error("课程人数已满");
            }
            
            // 检查是否已报名
            QueryWrapper<TrainingRegistration> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("user_id", user.getId()).eq("course_id", courseId);
            TrainingRegistration existing = trainingRegistrationService.getOne(checkWrapper);
            if (existing != null) {
                return Result.error("您已报名该课程");
            }
            
            TrainingRegistration registration = new TrainingRegistration();
            registration.setUserId(user.getId());
            registration.setCourseId(courseId);
            registration.setStatus("REGISTERED");
            registration.setRegistrationTime(new Date());
            
            trainingRegistrationService.save(registration);
            
            // 更新课程报名人数
            course.setCurrentParticipants(course.getCurrentParticipants() + 1);
            trainingCourseService.updateById(course);
            
            return Result.success("报名成功");
        } catch (Exception e) {
            return Result.error("报名失败");
        }
    }

    /**
     * 取消报名
     */
    @DeleteMapping("/training-registrations/{registrationId}")
    public Result cancelRegistration(@PathVariable Long registrationId) {
        // 修复：使用服务类的getById方法获取报名记录
        TrainingRegistration registration = trainingRegistrationService.getById(registrationId);
        if (registration == null) {
            return Result.error("报名记录不存在");
        }
        
        TrainingCourse course = trainingCourseService.getById(registration.getCourseId());
        if (course != null) {
            course.setCurrentParticipants(course.getCurrentParticipants() - 1);
            trainingCourseService.updateById(course);
        }
        
        // 修复：使用服务类的removeById方法删除报名记录
        trainingRegistrationService.removeById(registrationId);
        return Result.success("取消报名成功");
    }

    /**
     * 获取我的报名记录
     */
    @GetMapping("/training-registrations/my")
    public Result getMyRegistrations(@RequestHeader("Authorization") String token) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getId()).orderByDesc("registration_time");
            // 修复：使用服务类的list方法获取报名记录
            List<TrainingRegistration> registrations = trainingRegistrationService.list(wrapper);
            return Result.success(registrations);
        } catch (Exception e) {
            return Result.error("获取报名记录失败");
        }
    }

    /**
     * 获取课程的报名人员列表
     */
    @GetMapping("/training-courses/{courseId}/registrations")
    public Result getCourseRegistrations(@PathVariable Long courseId) {
        QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId).orderByDesc("registration_time");
        // 修复：使用服务类的list方法获取报名人员列表
        List<TrainingRegistration> registrations = trainingRegistrationService.list(wrapper);
        return Result.success(registrations);
    }
}