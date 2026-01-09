package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.EmergencyAlert;
import com.helpoldman.entity.User;
import com.helpoldman.service.EmergencyAlertService;
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
public class EmergencyController {
    
    @Autowired
    private EmergencyAlertService emergencyAlertService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 提交紧急报警
     */
    @PostMapping("/emergency-alerts")
    public Result submitEmergencyAlert(@RequestHeader("Authorization") String token,
                                      @RequestBody Map<String, Object> params) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            String alertType = (String) params.get("alertType");
            String description = (String) params.get("description");
            String location = (String) params.get("location");
            
            EmergencyAlert alert = new EmergencyAlert();
            alert.setUserId(user.getId());
            alert.setAlertType(alertType);
            alert.setDescription(description);
            alert.setLocation(location);
            alert.setAlertTime(new Date());
            alert.setStatus("PENDING");
            
            emergencyAlertService.save(alert);
            return Result.success("紧急报警提交成功");
        } catch (Exception e) {
            return Result.error("报警提交失败");
        }
    }

    /**
     * 获取我的紧急报警记录
     */
    @GetMapping("/emergency-alerts/my")
    public Result getMyEmergencyAlerts(@RequestHeader("Authorization") String token) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            List<EmergencyAlert> alerts = emergencyAlertService.list(
                new QueryWrapper<EmergencyAlert>()
                    .eq("user_id", user.getId())
                    .orderByDesc("alert_time")
            );
            return Result.success(alerts);
        } catch (Exception e) {
            return Result.error("获取报警记录失败");
        }
    }

    /**
     * 获取所有紧急报警（管理员/社区人员）
     */
    @GetMapping("/emergency-alerts")
    public Result getAllEmergencyAlerts(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String status) {
        QueryWrapper<EmergencyAlert> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("alert_time");
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        // 修复：手动实现分页逻辑
        List<EmergencyAlert> alertList = emergencyAlertService.list(wrapper);
        int start = (page - 1) * size;
        int end = Math.min(start + size, alertList.size());
        List<EmergencyAlert> pagedList = alertList.subList(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pagedList);
        result.put("total", alertList.size());
        result.put("size", size);
        result.put("current", page);
        result.put("pages", (int) Math.ceil((double) alertList.size() / size));
        
        return Result.success(result);
    }

    /**
     * 处理紧急报警
     */
    @PutMapping("/emergency-alerts/{alertId}/handle")
    public Result handleEmergencyAlert(@PathVariable Long alertId,
                                      @RequestHeader("Authorization") String token,
                                      @RequestBody Map<String, Object> params) {
        try {
            // 修复：使用正确的JWT解析方法
            Long handlerId = jwtUtil.getUserIdFromToken(token);
            User handler = userService.getById(handlerId);
            
            EmergencyAlert alert = emergencyAlertService.getById(alertId);
            if (alert == null) {
                return Result.error("报警记录不存在");
            }
            
            String action = (String) params.get("action");
            String remark = (String) params.get("remark");
            
            alert.setStatus("HANDLED");
            alert.setHandlerId(handler.getId());
            alert.setHandleTime(new Date());
            alert.setResult(remark);
            
            emergencyAlertService.updateById(alert);
            return Result.success("报警处理完成");
        } catch (Exception e) {
            return Result.error("处理报警失败");
        }
    }

    /**
     * 获取报警统计
     */
    @GetMapping("/emergency-alerts/stats")
    public Result getEmergencyStats() {
        List<EmergencyAlert> alerts = emergencyAlertService.list();
        long pendingCount = alerts.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
        long handledCount = alerts.stream().filter(a -> "HANDLED".equals(a.getStatus())).count();
        
        return Result.success(new HashMap<String, Long>() {{
            put("pending", pendingCount);
            put("handled", handledCount);
            put("total", (long) alerts.size());
        }});
    }
}