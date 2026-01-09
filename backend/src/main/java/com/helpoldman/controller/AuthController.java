package com.helpoldman.controller;

import com.helpoldman.dto.LoginDTO;
import com.helpoldman.entity.User;
import com.helpoldman.service.UserService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
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
    public Result register(@Valid @RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        
        if (userService.getUserByPhone(user.getPhone()) != null) {
            return Result.error("手机号已注册");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("ACTIVE");
        userService.save(user);
        
        return Result.success("注册成功");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public Result getCurrentUser(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(jwtToken);
            User user = userService.getById(userId);
            if (user != null) {
                return Result.success(user);
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
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(jwtToken);
            User user = userService.getById(userId);
            
            if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userService.updateById(user);
                return Result.success("密码修改成功");
            }
        }
        return Result.error("原密码错误或用户不存在");
    }
}