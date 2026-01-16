package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helpoldman.entity.BarrierFreeRenovation;
import com.helpoldman.service.BarrierFreeRenovationService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BarrierFreeRenovationController {
    
    @Autowired
    private BarrierFreeRenovationService renovationService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 提交无障碍改造申请
     */
    @PostMapping("/renovations")
    public Result submitRenovation(@RequestHeader("Authorization") String token,
                                  @RequestBody BarrierFreeRenovation renovation) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            renovation.setUserId(userId);
            renovation.setStatus("PENDING");
            renovationService.save(renovation);
            return Result.success("改造申请提交成功");
        } catch (Exception e) {
            return Result.error("申请提交失败");
        }
    }

    /**
     * 获取我的改造申请
     */
    @GetMapping("/renovations/my")
    public Result getMyRenovations(@RequestHeader("Authorization") String token,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            Page<BarrierFreeRenovation> pageInfo = new Page<>(page, size);
            QueryWrapper<BarrierFreeRenovation> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
            
            Page<BarrierFreeRenovation> resultPage = renovationService.page(pageInfo, wrapper);
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", resultPage.getRecords());
            result.put("total", resultPage.getTotal());
            result.put("size", resultPage.getSize());
            result.put("current", resultPage.getCurrent());
            result.put("pages", resultPage.getPages());
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取申请记录失败");
        }
    }

    /**
     * 获取所有改造申请（管理员）
     */
    @GetMapping("/renovations")
    public Result getAllRenovations(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        Page<BarrierFreeRenovation> pageInfo = new Page<>(page, size);
        QueryWrapper<BarrierFreeRenovation> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        
        Page<BarrierFreeRenovation> resultPage = renovationService.page(pageInfo, wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("size", resultPage.getSize());
        result.put("current", resultPage.getCurrent());
        result.put("pages", resultPage.getPages());
        
        return Result.success(result);
    }

    /**
     * 更新改造申请状态
     */
    @PutMapping("/renovations/{id}/status")
    public Result updateRenovationStatus(@PathVariable Long id, 
                                        @RequestBody Map<String, String> statusMap) {
        try {
            BarrierFreeRenovation renovation = renovationService.getById(id);
            if (renovation != null) {
                renovation.setStatus(statusMap.get("status"));
                renovationService.updateById(renovation);
                return Result.success("状态更新成功");
            }
            return Result.error("申请记录不存在");
        } catch (Exception e) {
            return Result.error("状态更新失败");
        }
    }
}