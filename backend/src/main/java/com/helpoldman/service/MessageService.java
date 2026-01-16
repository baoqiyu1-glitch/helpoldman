package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.Message;
import com.helpoldman.utils.Result;

import java.util.List;

public interface MessageService extends IService<Message> {
    // 获取用户消息列表
    List<Message> getUserMessages(Long userId);
    
    // 标记消息为已读
    boolean markAsRead(Long messageId);
    
    // 批量标记消息为已读
    boolean markAllAsRead(Long userId);
    
    // 获取未读消息数量
    int getUnreadCount(Long userId);
}
