package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helpoldman.entity.NursingService;
import com.helpoldman.service.NursingServiceService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NursingServiceController {
    
    @Autowired
    private NursingServiceService nursingService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 申请护工服务
     */
    @PostMapping("/nursing-services")
    public Result applyNursingService(@RequestHeader("Authorization") String token,
                                     @RequestBody NursingService nursingService) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            nursingService.setUserId(userId);
            nursingService.setStatus("PENDING");
            this.nursingService.save(nursingService);  // 修复：使用this.nursingService来区分
            return Result.success("护工服务申请提交成功");
        } catch (Exception e) {
            return Result.error("申请提交失败");
        }
    }

    /**
     * 获取我的护工服务申请
     */
    @GetMapping("/nursing-services/my")
    public Result getMyNursingServices(@RequestHeader("Authorization") String token,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            Page<NursingService> pageInfo = new Page<>(page, size);
            QueryWrapper<NursingService> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
            
            Page<NursingService> resultPage = nursingService.page(pageInfo, wrapper);
            
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
     * 获取所有护工服务申请（管理员）
     */
    @GetMapping("/nursing-services")
    public Result getAllNursingServices(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        Page<NursingService> pageInfo = new Page<>(page, size);
        QueryWrapper<NursingService> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        
        Page<NursingService> resultPage = nursingService.page(pageInfo, wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("size", resultPage.getSize());
        result.put("current", resultPage.getCurrent());
        result.put("pages", resultPage.getPages());
        
        return Result.success(result);
    }

    /**
     * 分配护工
     */
    @PutMapping("/nursing-services/{id}/assign")
    public Result assignNurse(@PathVariable Long id, 
                             @RequestBody Map<String, Long> assignMap) {
        try {
            NursingService service = nursingService.getById(id);
            if (service != null) {
                service.setNurseId(assignMap.get("nurseId"));
                service.setStatus("ASSIGNED");
                nursingService.updateById(service);
                return Result.success("护工分配成功");
            }
            return Result.error("服务记录不存在");
        } catch (Exception e) {
            return Result.error("分配失败");
        }
    }
}