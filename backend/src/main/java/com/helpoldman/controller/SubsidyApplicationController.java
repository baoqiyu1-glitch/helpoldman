package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helpoldman.entity.SubsidyApplication;
import com.helpoldman.service.SubsidyApplicationService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SubsidyApplicationController {
    
    @Autowired
    private SubsidyApplicationService subsidyApplicationService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 提交补助申请
     */
    @PostMapping("/subsidy-applications")
    public Result submitSubsidyApplication(@RequestHeader("Authorization") String token,
                                          @RequestBody SubsidyApplication subsidyApplication) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            subsidyApplication.setUserId(userId);
            subsidyApplication.setStatus("PENDING");
            subsidyApplicationService.save(subsidyApplication);
            return Result.success("补助申请提交成功");
        } catch (Exception e) {
            return Result.error("申请提交失败");
        }
    }

    /**
     * 获取我的补助申请
     */
    @GetMapping("/subsidy-applications/my")
    public Result getMySubsidyApplications(@RequestHeader("Authorization") String token,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            Page<SubsidyApplication> pageInfo = new Page<>(page, size);
            QueryWrapper<SubsidyApplication> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
            
            Page<SubsidyApplication> resultPage = subsidyApplicationService.page(pageInfo, wrapper);
            
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
     * 获取所有补助申请（管理员）
     */
    @GetMapping("/subsidy-applications")
    public Result getAllSubsidyApplications(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size) {
        Page<SubsidyApplication> pageInfo = new Page<>(page, size);
        QueryWrapper<SubsidyApplication> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        
        Page<SubsidyApplication> resultPage = subsidyApplicationService.page(pageInfo, wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("size", resultPage.getSize());
        result.put("current", resultPage.getCurrent());
        result.put("pages", resultPage.getPages());
        
        return Result.success(result);
    }

    /**
     * 审核补助申请
     */
    @PutMapping("/subsidy-applications/{id}/review")
    public Result reviewSubsidyApplication(@PathVariable Long id, 
                                          @RequestBody Map<String, String> reviewMap) {
        try {
            SubsidyApplication application = subsidyApplicationService.getById(id);
            if (application != null) {
                application.setStatus(reviewMap.get("status"));
                application.setReviewComments(reviewMap.get("comments"));
                subsidyApplicationService.updateById(application);
                return Result.success("审核完成");
            }
            return Result.error("申请记录不存在");
        } catch (Exception e) {
            return Result.error("审核失败");
        }
    }
}