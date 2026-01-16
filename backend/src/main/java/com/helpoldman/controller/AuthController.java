package com.helpoldman.controller;

import com.helpoldman.dto.LoginDTO;
import com.helpoldman.entity.User;
import com.helpoldman.entity.Message;
import com.helpoldman.service.UserService;
import com.helpoldman.service.MessageService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.getUserByUsername(loginDTO.getUsername());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        
        if (!"ACTIVE".equals(user.getStatus())) {
            return Result.error("账号已被禁用，请联系管理员");
        }
        
        // 修复：使用正确的JWT生成方法
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getUserType());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        
        return Result.success("登录成功", data);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            if (userService.getUserByUsername(user.getUsername()) != null) {
                return Result.error("用户名已存在");
            }
            
            // 设置默认值 - 修复：确保用户类型正确设置
            user.setStatus("ACTIVE");
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            
            // 调试信息：打印接收到的用户类型
            System.out.println("注册接收到的用户类型: " + user.getUserType());
            
            // 如果用户类型为空，默认为elder
            if (user.getUserType() == null || user.getUserType().trim().isEmpty()) {
                user.setUserType("elder"); // 默认用户类型
                System.out.println("用户类型为空，设置为默认值: elder");
            } else {
                System.out.println("用户类型已设置: " + user.getUserType());
            }
            
            // 加密密码
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // 保存用户
            boolean saved = userService.save(user);
            if (saved) {
                // 为新用户添加默认消息
                addDefaultMessages(user.getId());
                
                // 打印调试信息
                System.out.println("新用户注册成功 - ID: " + user.getId() + 
                                 ", 用户名: " + user.getUsername() + 
                                 ", 用户类型: " + user.getUserType());
                
                return Result.success("注册成功");
            } else {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败: " + e.getMessage());
        }
    }
    
    /**
     * 为新用户添加默认消息
     */
    private void addDefaultMessages(Long userId) {
        try {
            List<Message> defaultMessages = new ArrayList<>();
            
            // 默认消息1：欢迎消息
            Message msg1 = new Message();
            msg1.setUserId(userId);
            msg1.setTitle("欢迎使用助老系统");
            msg1.setContent("欢迎您使用助老系统，我们将为您提供贴心的服务");
            msg1.setType("SYSTEM");
            msg1.setIsRead(false); // 修复：使用Boolean类型，false表示未读
            msg1.setIcon("notification");
            msg1.setDescription("系统通知");
            msg1.setCreateTime(new Date());
            
            // 默认消息2：服务提醒
            Message msg2 = new Message();
            msg2.setUserId(userId);
            msg2.setTitle("服务提醒");
            msg2.setContent("您有新的服务申请待处理，请及时查看");
            msg2.setType("SERVICE");
            msg2.setIsRead(false); // 修复：使用Boolean类型，false表示未读
            msg2.setIcon("service");
            msg2.setDescription("服务通知");
            msg2.setCreateTime(new Date());
            
            defaultMessages.add(msg1);
            defaultMessages.add(msg2);
            
            // 保存默认消息到数据库
            messageService.saveBatch(defaultMessages);
            System.out.println("为用户ID " + userId + " 添加了默认消息");
        } catch (Exception e) {
            System.err.println("添加默认消息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public Result getCurrentUser(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                Long userId = jwtUtil.getUserIdFromToken(jwtToken);
                User user = userService.getById(userId);
                if (user != null) {
                    return Result.success(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.error("获取用户信息失败");
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public Result changePassword(@RequestHeader("Authorization") String token,
                                @RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                Long userId = jwtUtil.getUserIdFromToken(jwtToken);
                User user = userService.getById(userId);
                
                if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setUpdateTime(new Date());
                    userService.updateById(user);
                    return Result.success("密码修改成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.error("原密码错误或用户不存在");
    }
}