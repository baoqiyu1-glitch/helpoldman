package com.helpoldman.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helpoldman.entity.ServiceOrder;
import com.helpoldman.utils.Result;

import java.util.List;

public interface ServiceOrderService extends IService<ServiceOrder> {
    
    /**
     * 根据用户ID获取服务订单列表
     */
    List<ServiceOrder> getOrdersByUserId(Long userId);
    
    /**
     * 分配订单给志愿者
     */
    Result assignOrder(Long orderId, Long volunteerId);
    
    /**
     * 完成订单
     */
    Result completeOrder(Long orderId, String feedback);
    
    /**
     * 根据状态筛选订单
     */
    List<ServiceOrder> getOrdersByStatus(String status);
}