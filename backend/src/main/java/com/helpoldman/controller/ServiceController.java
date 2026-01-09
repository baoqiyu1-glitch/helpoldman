package com.helpoldman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.ServiceOrder;
import com.helpoldman.entity.ServiceType;
import com.helpoldman.entity.User;
import com.helpoldman.dto.ServiceApplyDTO;
import com.helpoldman.service.ServiceOrderService;
import com.helpoldman.service.ServiceTypeService;
import com.helpoldman.service.UserService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ServiceController {
    
    @Autowired
    private ServiceTypeService serviceTypeService;
    
    @Autowired
    private ServiceOrderService serviceOrderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // ========== 服务类型管理 ==========
    
    /**
     * 获取所有服务类型
     */
    @GetMapping("/service-types")
    public Result getServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeService.list(
            new QueryWrapper<ServiceType>().eq("status", "ACTIVE")
        );
        return Result.success(serviceTypes);
    }

    /**
     * 根据分类获取服务类型
     */
    @GetMapping("/service-types/category/{category}")
    public Result getServiceTypesByCategory(@PathVariable String category) {
        List<ServiceType> serviceTypes = serviceTypeService.list(
            new QueryWrapper<ServiceType>()
                .eq("category", category)
                .eq("status", "ACTIVE")
        );
        return Result.success(serviceTypes);
    }

    /**
     * 创建服务类型
     */
    @PostMapping("/service-types")
    public Result createServiceType(@RequestBody ServiceType serviceType) {
        serviceType.setStatus("ACTIVE");
        serviceTypeService.save(serviceType);
        return Result.success("创建服务类型成功");
    }

    // ========== 服务订单管理 ==========
    
    /**
     * 申请服务（老人/家属）
     */
    @PostMapping("/service-orders")
    public Result applyService(@RequestHeader("Authorization") String token,
                              @RequestBody ServiceApplyDTO applyDTO) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            ServiceOrder order = new ServiceOrder();
            order.setUserId(user.getId());
            order.setServiceTypeId(applyDTO.getServiceTypeId());
            order.setContent(applyDTO.getServiceContent());
            order.setServiceTime(applyDTO.getAppointmentTime());
            order.setAddress(applyDTO.getAddress());
            order.setContactPhone(applyDTO.getContactPhone());
            order.setStatus("PENDING");
            
            serviceOrderService.save(order);
            return Result.success("服务申请提交成功");
        } catch (Exception e) {
            return Result.error("服务申请失败");
        }
    }

    /**
     * 获取我的服务订单
     */
    @GetMapping("/service-orders/my")
    public Result getMyServiceOrders(@RequestHeader("Authorization") String token,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        try {
            // 修复：使用正确的JWT解析方法
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            QueryWrapper<ServiceOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getId())
                   .orderByDesc("create_time");
            
            // 修复：手动实现分页逻辑
            List<ServiceOrder> orderList = serviceOrderService.list(wrapper);
            int start = (page - 1) * size;
            int end = Math.min(start + size, orderList.size());
            List<ServiceOrder> pagedList = orderList.subList(start, end);
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", pagedList);
            result.put("total", orderList.size());
            result.put("size", size);
            result.put("current", page);
            result.put("pages", (int) Math.ceil((double) orderList.size() / size));
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取订单失败");
        }
    }
}