package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.TrainingRegistration;
import com.helpoldman.mapper.TrainingRegistrationMapper;
import com.helpoldman.service.TrainingRegistrationService;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrainingRegistrationServiceImpl extends ServiceImpl<TrainingRegistrationMapper, TrainingRegistration> implements TrainingRegistrationService {

    @Override
    public Result registerCourse(Long userId, Long courseId) {
        // 检查是否已报名
        if (isUserRegistered(userId, courseId)) {
            return Result.error("您已报名该课程");
        }
        
        TrainingRegistration registration = new TrainingRegistration();
        registration.setUserId(userId);
        registration.setCourseId(courseId);
        registration.setStatus("REGISTERED");
        registration.setRegistrationTime(new Date());
        
        boolean success = this.save(registration);
        return success ? Result.success("报名成功") : Result.error("报名失败");
    }

    @Override
    public Result cancelRegistration(Long userId, Long courseId) {
        QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("course_id", courseId)
               .eq("status", "REGISTERED");
        
        TrainingRegistration registration = this.getOne(wrapper);
        if (registration == null) {
            return Result.error("未找到报名记录");
        }
        
        registration.setStatus("CANCELLED");
        boolean success = this.updateById(registration);
        return success ? Result.success("取消报名成功") : Result.error("取消报名失败");
    }

    @Override
    public boolean isUserRegistered(Long userId, Long courseId) {
        QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("course_id", courseId)
               .eq("status", "REGISTERED");
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean isUserRegisteredForCourse(Long userId, Long courseId) {
        return isUserRegistered(userId, courseId);
    }

    @Override
    public List<TrainingRegistration> getRegisteredCoursesByUserId(Long userId) {
        QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("status", "REGISTERED");
        return this.list(wrapper);
    }

    @Override
    public int getRegisteredCourseCountByUserId(Long userId) {
        QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("status", "REGISTERED");
        return (int) this.count(wrapper);
    }

    @Override
    public List<TrainingRegistration> getUpcomingRegisteredCoursesByUserId(Long userId) {
        QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("status", "REGISTERED");
        return this.list(wrapper);
    }

    @Override
    public List<TrainingRegistration> getCompletedRegisteredCoursesByUserId(Long userId) {
        QueryWrapper<TrainingRegistration> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("status", "COMPLETED");
        return this.list(wrapper);
    }
}