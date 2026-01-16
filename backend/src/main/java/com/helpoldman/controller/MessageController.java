package com.helpoldman.controller;

import com.helpoldman.entity.Message;
import com.helpoldman.service.MessageService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户的消息列表
     */
    @GetMapping("/messages")
    public Result getMessages(@RequestHeader("Authorization") String token) {
        try {
            // 检查token是否为空
            if (token == null || token.trim().isEmpty()) {
                return Result.error("未提供认证令牌");
            }
            
            // 解析JWT获取用户ID
            System.out.println("接收到的token: " + token);
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            System.out.println("处理后的JWT token: " + jwtToken);
            
            Long userId = jwtUtil.getUserIdFromToken(jwtToken);
            System.out.println("解析出的用户ID: " + userId);
            
            if (userId == null) {
                return Result.error("用户ID解析失败");
            }
            
            List<Message> messages = messageService.getUserMessages(userId);
            System.out.println("查询到的消息数量: " + messages.size());
            
            return Result.success(messages);
        } catch (Exception e) {
            System.out.println("获取消息列表异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取消息列表失败: " + e.getMessage());
        }
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/messages/{id}/read")
    public Result markAsRead(@PathVariable Long id) {
        try {
            System.out.println("标记消息为已读 - 消息ID: " + id);
            boolean success = messageService.markAsRead(id);
            return success ? Result.success("标记成功") : Result.error("标记失败");
        } catch (Exception e) {
            System.err.println("标记消息为已读异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("标记消息失败: " + e.getMessage());
        }
    }

    /**
     * 标记所有消息为已读
     */
    @PutMapping("/messages/read-all")
    public Result markAllAsRead(@RequestHeader("Authorization") String token) {
        try {
            // 解析JWT获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            boolean success = messageService.markAllAsRead(userId);
            return success ? Result.success("全部标记成功") : Result.error("标记失败");
        } catch (Exception e) {
            return Result.error("标记所有消息失败: " + e.getMessage());
        }
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/messages/unread-count")
    public Result getUnreadCount(@RequestHeader("Authorization") String token) {
        try {
            // 解析JWT获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            int count = messageService.getUnreadCount(userId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("获取未读消息数量失败: " + e.getMessage());
        }
    }
}