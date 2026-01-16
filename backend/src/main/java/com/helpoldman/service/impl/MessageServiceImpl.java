package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.Message;
import com.helpoldman.mapper.MessageMapper;
import com.helpoldman.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    
    @Override
    public List<Message> getUserMessages(Long userId) {
        System.out.println("查询用户ID为 " + userId + " 的消息");
        
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("deleted", 0)
               .orderByDesc("create_time");
        
        List<Message> messages = this.list(wrapper);
        System.out.println("数据库查询结果数量: " + messages.size());
        
        return messages;
    }
    
    @Override
    public boolean markAsRead(Long messageId) {
        try {
            Message message = new Message();
            message.setId(messageId);
            message.setIsRead(true);
            System.out.println("标记消息为已读 - 消息ID: " + messageId);
            boolean result = updateById(message);
            System.out.println("更新结果: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("标记消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean markAllAsRead(Long userId) {
        try {
            Message message = new Message();
            message.setIsRead(true);
            
            QueryWrapper<Message> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .eq("is_read", false)
                   .eq("deleted", 0);
            
            System.out.println("标记用户 " + userId + " 的所有消息为已读");
            boolean result = update(message, wrapper);
            System.out.println("批量更新结果: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("标记所有消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int getUnreadCount(Long userId) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("is_read", false)
               .eq("deleted", 0);
        return (int) count(wrapper);
    }
}