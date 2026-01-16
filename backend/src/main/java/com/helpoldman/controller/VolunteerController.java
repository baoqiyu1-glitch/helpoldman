package com.helpoldman.controller;

import com.helpoldman.entity.User;
import com.helpoldman.service.UserService;
import com.helpoldman.service.VolunteerService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/volunteer")
public class VolunteerController {
    
    @Autowired
    private VolunteerService volunteerService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取志愿者首页数据
     */
     @GetMapping("/dashboard")
    public Result getVolunteerDashboard(@RequestHeader("Authorization") String token) {
        // 从token中获取用户ID
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        User user = userService.getById(userId);
        if (user == null || !"VOLUNTEER".equalsIgnoreCase(user.getUserType())) {
            return Result.error("用户不是志愿者");
        }
        
        Map<String, Object> dashboardData = volunteerService.getVolunteerDashboard(userId);
        return Result.success(dashboardData);
    }

    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(String token) {
        // 假设token格式为 "Bearer xxxxxx"
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            return jwtUtil.getUserIdFromToken(jwtToken);
        }
        return null;
    }

    /**
     * 获取志愿者个人资料
     */
    @GetMapping("/profile")
    public Result getVolunteerProfile(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Map<String, Object> profile = volunteerService.getVolunteerProfile(userId);
        return profile != null ? Result.success(profile) : Result.error("获取个人资料失败");
    }

    /**
     * 更新志愿者个人资料
     */
    @PutMapping("/profile")
    public Result updateVolunteerProfile(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> profileData) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.updateVolunteerProfile(userId, profileData);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 获取志愿者文字配置
     */
    @GetMapping("/text-config")
    public Result getVolunteerTextConfig() {
        Map<String, Object> textConfig = volunteerService.getVolunteerTextConfig();
        return Result.success(textConfig);
    }

    /**
     * 获取志愿者代购订单列表
     */
    @GetMapping("/shopping-orders")
    public Result getShoppingOrders(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Map<String, Object> orders = volunteerService.getShoppingOrders(userId, page, size);
        return Result.success(orders);
    }

    /**
     * 接受代购订单
     */
    @PostMapping("/shopping-orders/{orderId}/accept")
    public Result acceptShoppingOrder(@RequestHeader("Authorization") String token, @PathVariable String orderId) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.acceptShoppingOrder(userId, orderId);
        return success ? Result.success("接单成功") : Result.error("接单失败");
    }

    /**
     * 拒绝代购订单
     */
    @PostMapping("/shopping-orders/{orderId}/reject")
    public Result rejectShoppingOrder(@RequestHeader("Authorization") String token, @PathVariable String orderId) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.rejectShoppingOrder(userId, orderId);
        return success ? Result.success("拒单成功") : Result.error("拒单失败");
    }

    /**
     * 获取家政服务任务列表
     */
    @GetMapping("/housework-tasks")
    public Result getHouseworkTasks(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<Map<String, Object>> tasks = volunteerService.getHouseworkTasks(userId);
        return Result.success(tasks);
    }

    /**
     * 接受家政服务任务
     */
    @PostMapping("/housework-tasks/{taskId}/accept")
    public Result acceptHouseworkTask(@RequestHeader("Authorization") String token, @PathVariable String taskId) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.acceptHouseworkTask(userId, taskId);
        return success ? Result.success("接单成功") : Result.error("接单失败");
    }

    /**
     * 拒绝家政服务任务
     */
    @PostMapping("/housework-tasks/{taskId}/reject")
    public Result rejectHouseworkTask(@RequestHeader("Authorization") String token, @PathVariable String taskId) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.rejectHouseworkTask(userId, taskId);
        return success ? Result.success("拒单成功") : Result.error("拒单失败");
    }

    /**
     * 获取培训课程列表
     */
    @GetMapping("/training-courses")
    public Result getTrainingCourses(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<Map<String, Object>> courses = volunteerService.getTrainingCourses(userId);
        return Result.success(courses);
    }

    /**
     * 报名培训课程
     */
    @PostMapping("/training-courses/{courseId}/register")
    public Result registerTrainingCourse(@RequestHeader("Authorization") String token, @PathVariable String courseId) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.registerTrainingCourse(userId, courseId);
        return success ? Result.success("报名成功") : Result.error("报名失败");
    }

    /**
     * 获取申请服务信息
     */
    @GetMapping("/apply-info")
    public Result getApplyInfo(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Map<String, Object> applyInfo = volunteerService.getApplyInfo(userId);
        return Result.success(applyInfo);
    }

    /**
     * 提交申请服务
     */
    @PostMapping("/apply")
    public Result submitApply(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> applyData) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.submitApply(userId, applyData);
        return success ? Result.success("申请提交成功") : Result.error("申请提交失败");
    }

    /**
     * 获取技能列表
     */
    @GetMapping("/skills")
    public Result getSkills() {
        List<Map<String, Object>> skills = volunteerService.getSkills();
        return Result.success(skills);
    }

    /**
     * 完成订单
     */
    @PostMapping("/orders/{orderId}/complete")
    public Result completeOrder(@RequestHeader("Authorization") String token, @PathVariable String orderId) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        boolean success = volunteerService.completeOrder(userId, orderId);
        return success ? Result.success("订单完成成功") : Result.error("订单完成失败");
    }

    /**
     * 获取志愿者已接任务
     */
    @GetMapping("/my-tasks")
    public Result getMyTasks(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        List<Map<String, Object>> tasks = volunteerService.getMyTasks(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("tasks", tasks);
        return Result.success(result);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/order-details")
    public Result getOrderDetails(@RequestParam String id) {
        Map<String, Object> orderDetails = volunteerService.getOrderDetails(id);
        return orderDetails != null ? Result.success(orderDetails) : Result.error("获取订单详情失败");
    }

    /**
     * 检查用户是否为志愿者
     */
    private boolean isVolunteer(User user) {
        if (user == null || user.getUserType() == null) {
            return false;
        }
        String userType = user.getUserType().toUpperCase();
        return "VOLUNTEER".equals(userType);
    }
}