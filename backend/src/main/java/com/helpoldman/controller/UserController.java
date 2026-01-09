package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helpoldman.entity.User;
import com.helpoldman.service.UserService;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取用户列表（分页）
     */
    @GetMapping
    public Result getUsers(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String userType,
                          @RequestParam(required = false) String keyword) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        
        if (userType != null && !userType.isEmpty()) {
            wrapper.eq("user_type", userType);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("username", keyword)
                    .or().like("real_name", keyword)
                    .or().like("phone", keyword));
        }
        
        wrapper.orderByDesc("create_time");
        
        // 修复：手动实现分页逻辑
        List<User> userList = userService.list(wrapper);
        int start = (page - 1) * size;
        int end = Math.min(start + size, userList.size());
        List<User> pagedList = userList.subList(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", pagedList);
        result.put("total", userList.size());
        result.put("size", size);
        result.put("current", page);
        result.put("pages", (int) Math.ceil((double) userList.size() / size));
        
        return Result.success(result);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null || user.getDeleted() == 1) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result createUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        
        if (userService.getUserByPhone(user.getPhone()) != null) {
            return Result.error("手机号已注册");
        }
        
        user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        user.setStatus("ACTIVE");
        userService.save(user);
        return Result.success("创建用户成功");
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null || existingUser.getDeleted() == 1) {
            return Result.error("用户不存在");
        }
        
        // 检查用户名是否重复（排除当前用户）
        User userByUsername = userService.getUserByUsername(user.getUsername());
        if (userByUsername != null && !userByUsername.getId().equals(id)) {
            return Result.error("用户名已存在");
        }
        
        // 检查手机号是否重复（排除当前用户）
        User userByPhone = userService.getUserByPhone(user.getPhone());
        if (userByPhone != null && !userByPhone.getId().equals(id)) {
            return Result.error("手机号已注册");
        }
        
        user.setId(id);
        userService.updateById(user);
        return Result.success("更新用户成功");
    }

    /**
     * 删除用户（软删除）
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null || user.getDeleted() == 1) {
            return Result.error("用户不存在");
        }
        
        user.setDeleted(1);
        userService.updateById(user);
        return Result.success("删除用户成功");
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    public Result resetPassword(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null || user.getDeleted() == 1) {
            return Result.error("用户不存在");
        }
        
        user.setPassword(passwordEncoder.encode("123456"));
        userService.updateById(user);
        return Result.success("密码重置成功");
    }

    /**
     * 根据用户类型统计
     */
    @GetMapping("/stats/by-type")
    public Result getUserStatsByType() {
        List<User> users = userService.list(new QueryWrapper<User>().eq("deleted", 0));
        long elderCount = users.stream().filter(u -> "ELDER".equals(u.getUserType())).count();
        long volunteerCount = users.stream().filter(u -> "VOLUNTEER".equals(u.getUserType())).count();
        long familyCount = users.stream().filter(u -> "FAMILY".equals(u.getUserType())).count();
        long communityCount = users.stream().filter(u -> "COMMUNITY".equals(u.getUserType())).count();
        
        return Result.success(new HashMap<String, Long>() {{
            put("elder", elderCount);
            put("volunteer", volunteerCount);
            put("family", familyCount);
            put("community", communityCount);
            put("total", (long) users.size());
        }});
    }
}