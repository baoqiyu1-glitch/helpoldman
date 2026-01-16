package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helpoldman.entity.MealDelivery;
import com.helpoldman.service.MealDeliveryService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MealDeliveryController {
    
    @Autowired
    private MealDeliveryService mealDeliveryService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 申请送饭服务
     */
    @PostMapping("/meal-deliveries")
    public Result applyMealDelivery(@RequestHeader("Authorization") String token,
                                   @RequestBody MealDelivery mealDelivery) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            mealDelivery.setUserId(userId);
            mealDelivery.setStatus("PENDING");
            mealDeliveryService.save(mealDelivery);
            return Result.success("送饭服务申请提交成功");
        } catch (Exception e) {
            return Result.error("申请提交失败");
        }
    }

    /**
     * 获取我的送饭服务申请
     */
    @GetMapping("/meal-deliveries/my")
    public Result getMyMealDeliveries(@RequestHeader("Authorization") String token,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            Page<MealDelivery> pageInfo = new Page<>(page, size);
            QueryWrapper<MealDelivery> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
            
            Page<MealDelivery> resultPage = mealDeliveryService.page(pageInfo, wrapper);
            
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
     * 获取所有送饭服务申请（管理员）
     */
    @GetMapping("/meal-deliveries")
    public Result getAllMealDeliveries(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        Page<MealDelivery> pageInfo = new Page<>(page, size);
        QueryWrapper<MealDelivery> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        
        Page<MealDelivery> resultPage = mealDeliveryService.page(pageInfo, wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("size", resultPage.getSize());
        result.put("current", resultPage.getCurrent());
        result.put("pages", resultPage.getPages());
        
        return Result.success(result);
    }

    /**
     * 更新送饭服务状态
     */
    @PutMapping("/meal-deliveries/{id}/status")
    public Result updateMealDeliveryStatus(@PathVariable Long id, 
                                          @RequestBody Map<String, String> statusMap) {
        try {
            MealDelivery delivery = mealDeliveryService.getById(id);
            if (delivery != null) {
                delivery.setStatus(statusMap.get("status"));
                mealDeliveryService.updateById(delivery);
                return Result.success("状态更新成功");
            }
            return Result.error("服务记录不存在");
        } catch (Exception e) {
            return Result.error("状态更新失败");
        }
    }
}