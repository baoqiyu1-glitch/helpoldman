package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.Equipment;
import com.helpoldman.entity.EquipmentApplication;
import com.helpoldman.entity.User;
import com.helpoldman.service.EquipmentApplicationService;
import com.helpoldman.service.EquipmentService;
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
public class EquipmentController {
    
    @Autowired
    private EquipmentService equipmentService;
    
    @Autowired
    private EquipmentApplicationService equipmentApplicationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // ========== 辅助器具管理 ==========
    
    /**
     * 获取辅助器具列表
     */
    @GetMapping("/equipment")
    public Result getEquipmentList(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(required = false) String category) {
        QueryWrapper<Equipment> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "AVAILABLE")
               .orderByDesc("create_time");
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        
        // 修复：手动实现分页逻辑
        List<Equipment> equipmentList = equipmentService.list(wrapper);
        int start = (page - 1) * size;
        int end = Math.min(start + size, equipmentList.size());
        List<Equipment> pagedList = equipmentList.subList(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pagedList);
        result.put("total", equipmentList.size());
        result.put("size", size);
        result.put("current", page);
        result.put("pages", (int) Math.ceil((double) equipmentList.size() / size));
        
        return Result.success(result);
    }

    /**
     * 获取器具详情
     */
    @GetMapping("/equipment/{id}")
    public Result getEquipmentById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getById(id);
        if (equipment == null) {
            return Result.error("器具不存在");
        }
        return Result.success(equipment);
    }

    /**
     * 申请辅助器具
     */
     @PostMapping("/equipment/applications")
    public Result applyEquipment(@RequestHeader("Authorization") String token,
                                @RequestBody Map<String, Object> params) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            Long equipmentId = Long.valueOf(params.get("equipmentId").toString());
            String applyReason = (String) params.get("applyReason");
            Integer expectedDuration = (Integer) params.get("expectedDuration");
            
            // 修复：使用服务类的applyEquipment方法
            boolean success = equipmentService.applyEquipment(user.getId(), equipmentId, applyReason);
            if (!success) {
                return Result.error("器具申请失败");
            }
            
            return Result.success("器具申请提交成功");
        } catch (Exception e) {
            return Result.error("申请失败");
        }
    }

    /**
     * 获取我的器具申请记录
     */
    @GetMapping("/equipment/applications/my")
    public Result getMyEquipmentApplications(@RequestHeader("Authorization") String token) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            List<EquipmentApplication> applications = equipmentApplicationService.list(
                new QueryWrapper<EquipmentApplication>()
                    .eq("user_id", user.getId())
                    .orderByDesc("apply_date")
            );
            return Result.success(applications);
        } catch (Exception e) {
            return Result.error("获取申请记录失败");
        }
    }

    /**
     * 获取所有器具申请（管理员）
     */
    @GetMapping("/equipment/applications")
    public Result getAllEquipmentApplications(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(required = false) String status) {
        QueryWrapper<EquipmentApplication> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("apply_date");
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        // 修复：手动实现分页逻辑
        List<EquipmentApplication> applicationList = equipmentApplicationService.list(wrapper);
        int start = (page - 1) * size;
        int end = Math.min(start + size, applicationList.size());
        List<EquipmentApplication> pagedList = applicationList.subList(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pagedList);
        result.put("total", applicationList.size());
        result.put("size", size);
        result.put("current", page);
        result.put("pages", (int) Math.ceil((double) applicationList.size() / size));
        
        return Result.success(result);
    }

    /**
     * 审批器具申请
     */
    @PutMapping("/equipment/applications/{applicationId}/approve")
    public Result approveApplication(@PathVariable Long applicationId,
                                    @RequestHeader("Authorization") String token,
                                    @RequestBody Map<String, Object> params) {
        try {
            // 修复：使用正确的JWT解析方法
            Long approverId = jwtUtil.getUserIdFromToken(token);
            User approver = userService.getById(approverId);
            
            EquipmentApplication application = equipmentApplicationService.getById(applicationId);
            if (application == null) {
                return Result.error("申请记录不存在");
            }
            
            Boolean approved = (Boolean) params.get("approved");
            String remark = (String) params.get("remark");
            
            if (approved) {
                Equipment equipment = equipmentService.getById(application.getEquipmentId());
                if (equipment.getAvailableQuantity() <= 0) {
                    return Result.error("器具库存不足，无法批准");
                }
                
                application.setStatus("APPROVED");
                // 修复：移除重复的申请调用，只更新状态
            } else {
                application.setStatus("REJECTED");
                // 如果拒绝申请，恢复器具数量
                Equipment equipment = equipmentService.getById(application.getEquipmentId());
                equipment.setAvailableQuantity(equipment.getAvailableQuantity() + 1);
                equipmentService.updateById(equipment);
            }
            application.setApproveTime(new Date());
            application.setApproverId(approver.getId());
            application.setRemarks(remark);
            
            equipmentApplicationService.updateById(application);
            
            return Result.success(approved ? "申请已批准" : "申请已拒绝");
        } catch (Exception e) {
            return Result.error("审批失败");
        }
    }
}