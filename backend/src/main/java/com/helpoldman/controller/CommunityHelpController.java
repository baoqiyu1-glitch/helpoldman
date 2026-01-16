package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helpoldman.entity.CommunityHelp;
import com.helpoldman.entity.User;
import com.helpoldman.service.CommunityHelpService;
import com.helpoldman.service.UserService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommunityHelpController {
    
    @Autowired
    private CommunityHelpService communityHelpService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 发布求助/帮助信息
     */
    @PostMapping("/community-help")
    public Result publishHelp(@RequestHeader("Authorization") String token,
                             @RequestBody CommunityHelp communityHelp) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            communityHelp.setUserId(userId);
            communityHelp.setStatus("PENDING");
            communityHelpService.save(communityHelp);
            return Result.success("发布成功");
        } catch (Exception e) {
            return Result.error("发布失败");
        }
    }

    /**
     * 获取求助列表
     */
    @GetMapping("/community-help/requests")
    public Result getHelpRequests(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size) {
        Page<CommunityHelp> pageInfo = new Page<>(page, size);
        QueryWrapper<CommunityHelp> wrapper = new QueryWrapper<>();
        wrapper.eq("help_type", "REQUEST")
               .eq("status", "PENDING")
               .orderByDesc("create_time");
        
        Page<CommunityHelp> resultPage = communityHelpService.page(pageInfo, wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("size", resultPage.getSize());
        result.put("current", resultPage.getCurrent());
        result.put("pages", resultPage.getPages());
        
        return Result.success(result);
    }

    /**
     * 获取帮助提供列表
     */
    @GetMapping("/community-help/offers")
    public Result getHelpOffers(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        Page<CommunityHelp> pageInfo = new Page<>(page, size);
        QueryWrapper<CommunityHelp> wrapper = new QueryWrapper<>();
        wrapper.eq("help_type", "OFFER")
               .eq("status", "PENDING")
               .orderByDesc("create_time");
        
        Page<CommunityHelp> resultPage = communityHelpService.page(pageInfo, wrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("size", resultPage.getSize());
        result.put("current", resultPage.getCurrent());
        result.put("pages", resultPage.getPages());
        
        return Result.success(result);
    }

    /**
     * 获取我的互助信息
     */
    @GetMapping("/community-help/my")
    public Result getMyHelps(@RequestHeader("Authorization") String token,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            Page<CommunityHelp> pageInfo = new Page<>(page, size);
            QueryWrapper<CommunityHelp> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
            
            Page<CommunityHelp> resultPage = communityHelpService.page(pageInfo, wrapper);
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", resultPage.getRecords());
            result.put("total", resultPage.getTotal());
            result.put("size", resultPage.getSize());
            result.put("current", resultPage.getCurrent());
            result.put("pages", resultPage.getPages());
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取失败");
        }
    }

    /**
     * 提供帮助
     */
    @PutMapping("/community-help/{id}/help")
    public Result offerHelp(@RequestHeader("Authorization") String token,
                           @PathVariable Long id) {
        try {
            Long helperId = jwtUtil.getUserIdFromToken(token);
            CommunityHelp communityHelp = communityHelpService.getById(id);
            if (communityHelp != null) {
                communityHelp.setHelperId(helperId);
                communityHelp.setStatus("IN_PROGRESS");
                communityHelpService.updateById(communityHelp);
                return Result.success("帮助成功");
            }
            return Result.error("求助信息不存在");
        } catch (Exception e) {
            return Result.error("帮助失败");
        }
    }
}