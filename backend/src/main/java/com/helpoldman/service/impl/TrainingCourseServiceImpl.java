package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.TrainingCourse;
import com.helpoldman.entity.TrainingRegistration;
import com.helpoldman.mapper.TrainingCourseMapper;
import com.helpoldman.service.TrainingCourseService;
import com.helpoldman.service.TrainingRegistrationService;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingCourseServiceImpl extends ServiceImpl<TrainingCourseMapper, TrainingCourse> implements TrainingCourseService {

    @Autowired
    private TrainingRegistrationService trainingRegistrationService;

    @Override
    public Result registerCourse(Long userId, Long courseId) {
        try {
            TrainingCourse course = this.getById(courseId);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            if (course.getCurrentParticipants() >= course.getMaxParticipants()) {
                return Result.error("课程已满员");
            }
            
            // 检查用户是否已报名
            boolean isRegistered = trainingRegistrationService.isUserRegisteredForCourse(userId, courseId);
            if (isRegistered) {
                return Result.error("您已报名该课程");
            }
            
            // 创建报名记录
            TrainingRegistration registration = new TrainingRegistration();
            registration.setUserId(userId);
            registration.setCourseId(courseId);
            registration.setRegistrationTime(new Date());
            registration.setStatus("REGISTERED");
            trainingRegistrationService.save(registration);
            
            // 更新课程报名人数
            course.setCurrentParticipants(course.getCurrentParticipants() + 1);
            this.updateById(course);
            
            return Result.success("报名成功");
        } catch (Exception e) {
            return Result.error("报名失败：" + e.getMessage());
        }
    }

    @Override
    public List<TrainingCourse> getPopularCourses() {
        QueryWrapper<TrainingCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("current_participants")
               .last("LIMIT 5");
        return this.list(wrapper);
    }

    @Override
    public List<TrainingCourse> getRegisteredCourses(Long userId) {
        List<TrainingRegistration> registrations = trainingRegistrationService.getRegisteredCoursesByUserId(userId);
        return registrations.stream()
                .map(registration -> this.getById(registration.getCourseId()))
                .filter(course -> course != null)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getRegisteredCourseCount(Long userId) {
        return trainingRegistrationService.getRegisteredCourseCountByUserId(userId);
    }

    @Override
    public List<TrainingCourse> getUpcomingRegisteredCourses(Long userId) {
        List<TrainingRegistration> registrations = trainingRegistrationService.getUpcomingRegisteredCoursesByUserId(userId);
        return registrations.stream()
                .map(registration -> this.getById(registration.getCourseId()))
                .filter(course -> course != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingCourse> getCompletedRegisteredCourses(Long userId) {
        List<TrainingRegistration> registrations = trainingRegistrationService.getCompletedRegisteredCoursesByUserId(userId);
        return registrations.stream()
                .map(registration -> this.getById(registration.getCourseId()))
                .filter(course -> course != null)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isUserRegisteredForCourse(Long userId, Long courseId) {
        return trainingRegistrationService.isUserRegisteredForCourse(userId, courseId);
    }

    @Override
    public Result updateCourseStatus(Long courseId, String status) {
        try {
            TrainingCourse course = this.getById(courseId);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            course.setStatus(status);
            this.updateById(course);
            return Result.success("课程状态更新成功");
        } catch (Exception e) {
            return Result.error("课程状态更新失败：" + e.getMessage());
        }
    }
}