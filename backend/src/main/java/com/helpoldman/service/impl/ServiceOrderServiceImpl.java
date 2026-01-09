package com.helpoldman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helpoldman.entity.ServiceOrder;
import com.helpoldman.mapper.ServiceOrderMapper;
import com.helpoldman.service.ServiceOrderService;
import com.helpoldman.utils.Result;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ServiceOrderServiceImpl extends ServiceImpl<ServiceOrderMapper, ServiceOrder> implements ServiceOrderService {

    @Override
    public List<ServiceOrder> getOrdersByUserId(Long userId) {
        QueryWrapper<ServiceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public Result assignOrder(Long orderId, Long volunteerId) {
        ServiceOrder order = this.getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        order.setVolunteerId(volunteerId);
        order.setStatus("ASSIGNED");
        order.setAssignTime(new Date());
        
        boolean success = this.updateById(order);
        return success ? Result.success("分配成功") : Result.error("分配失败");
    }

    @Override
    public Result completeOrder(Long orderId, String feedback) {
        ServiceOrder order = this.getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        order.setStatus("COMPLETED");
        order.setCompleteTime(new Date());
        order.setFeedback(feedback);
        
        boolean success = this.updateById(order);
        return success ? Result.success("订单完成") : Result.error("订单完成失败");
    }

    @Override
    public List<ServiceOrder> getOrdersByStatus(String status) {
        QueryWrapper<ServiceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status)
                   .orderByDesc("create_time");
        return this.list(queryWrapper);
    }
}