package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.User;
import com.helpoldman.mapper.UserMapper;
import com.helpoldman.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Override
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
               .eq("password", password)
               .eq("status", "ACTIVE");
        return baseMapper.selectOne(wrapper);
    }
    
    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        User existUser = baseMapper.selectOne(wrapper);
        if (existUser != null) {
            return false;
        }
        
        user.setStatus("ACTIVE");
        return save(user);
    }
    
    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return baseMapper.selectOne(wrapper);
    }
    
    @Override
    public User getUserByPhone(String phone) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        return baseMapper.selectOne(wrapper);
    }
}