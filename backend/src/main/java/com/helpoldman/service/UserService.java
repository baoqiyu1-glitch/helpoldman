package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
    boolean register(User user);
    User getUserByUsername(String username);
    User getUserByPhone(String phone);
}