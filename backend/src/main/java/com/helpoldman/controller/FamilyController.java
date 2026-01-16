package com.helpoldman.controller;

import com.helpoldman.entity.ServiceOrder;
import com.helpoldman.service.ServiceOrderService;
import com.helpoldman.utils.JwtUtil;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/family")
public class FamilyController {
    
    @Autowired
    private ServiceOrderService serviceOrderService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 获取家属照顾的老人数量
     */
    @GetMapping("/cared-elders")
    public Result getCaredEldersCount(@RequestHeader("Authorization") String token) {
        // 实现获取家属照顾老人数量的逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("count", 2);
        return Result.success("获取成功", result);
    }
    
    /**
     * 获取家属完成的服务数量
     */
    @GetMapping("/completed-services")
    public Result getCompletedServicesCount(@RequestHeader("Authorization") String token) {
        // 实现获取完成服务数量的逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("count", 5);
        return Result.success("获取成功", result);
    }
    
    /**
     * 获取家属最近服务记录
     */
    @GetMapping("/recent-services")
    public Result getRecentServices(@RequestHeader("Authorization") String token) {
        // 实现获取最近服务记录的逻辑
        List<Map<String, Object>> services = new ArrayList<>();
        
        Map<String, Object> service1 = new HashMap<>();
        service1.put("id", 1);
        service1.put("title", "为父亲申请护工服务");
        service1.put("time", "2024-01-15");
        service1.put("status", "completed");
        service1.put("statusText", "已完成");
        services.add(service1);
        
        Map<String, Object> service2 = new HashMap<>();
        service2.put("id", 2);
        service2.put("title", "为母亲申请送饭服务");
        service2.put("time", "2024-01-14");
        service2.put("status", "in-progress");
        service2.put("statusText", "进行中");
        services.add(service2);
        
        return Result.success("获取成功", services);
    }
    
    /**
     * 获取家属服务统计
     */
    @GetMapping("/service-stats")
    public Result getServiceStats(@RequestHeader("Authorization") String token) {
        // 实现获取服务统计的逻辑
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalServices", 8);
        stats.put("pendingServices", 2);
        stats.put("completedServices", 5);
        stats.put("cancelledServices", 1);
        return Result.success("获取成功", stats);
    }
    
    /**
     * 家属为老人申请服务
     */
    @PostMapping("/apply-service")
    public Result applyService(@RequestBody Map<String, Object> serviceData, 
                              @RequestHeader("Authorization") String token) {
        try {
            // 获取用户ID
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                return Result.error(401, "用户未登录");
            }
            
            // 检查服务类型
            String serviceType = (String) serviceData.get("serviceType");
            if (serviceType == null) {
                return Result.error(400, "服务类型不能为空");
            }
            
            // 创建服务订单
            ServiceOrder order = new ServiceOrder();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            
            // 根据服务类型设置不同的serviceTypeId
            if ("shopping".equals(serviceType)) {
                order.setServiceTypeId(1L); // 代购服务类型ID
                order.setTitle("代购服务申请");
                
                // 构建代购内容
                StringBuilder contentBuilder = new StringBuilder();
                if (serviceData.containsKey("items")) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) serviceData.get("items");
                    contentBuilder.append("代购商品：");
                    for (Map<String, Object> item : items) {
                        contentBuilder.append(item.get("name"))
                                     .append("(")
                                     .append(item.get("description"))
                                     .append(") ");
                    }
                }
                
                if (serviceData.containsKey("totalAmount")) {
                    contentBuilder.append("；总金额：").append(serviceData.get("totalAmount")).append("元");
                }
                
                order.setContent(contentBuilder.toString());
            } else {
                // 其他服务类型的处理
                order.setServiceTypeId(1L); // 默认服务类型ID
                order.setTitle("服务申请");
                order.setContent((String) serviceData.getOrDefault("content", ""));
            }
            
            // 设置订单地址
            if (serviceData.containsKey("deliveryAddress")) {
                order.setAddress((String) serviceData.get("deliveryAddress"));
            } else if (serviceData.containsKey("address")) {
                order.setAddress((String) serviceData.get("address"));
            }
            
            // 设置订单状态
            order.setStatus("PENDING");
            order.setPriority("MEDIUM");
            
            // 保存订单
            boolean saveResult = serviceOrderService.save(order);
            
            if (saveResult) {
                System.out.println("服务申请保存成功，订单号：" + order.getOrderNo());
                return Result.success("申请成功");
            } else {
                return Result.error("申请失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统异常，请稍后重试");
        }
    }
    
    /**
     * 获取家属可服务的老人列表
     */
    @GetMapping("/elders")
    public Result getFamilyElders(@RequestHeader("Authorization") String token) {
        // 实现获取家属关联老人列表的逻辑
        List<Map<String, Object>> elders = new ArrayList<>();
        
        Map<String, Object> elder1 = new HashMap<>();
        elder1.put("id", 1);
        elder1.put("name", "张大爷");
        elder1.put("relation", "父亲");
        elder1.put("age", 75);
        elder1.put("healthStatus", "良好");
        elders.add(elder1);
        
        Map<String, Object> elder2 = new HashMap<>();
        elder2.put("id", 2);
        elder2.put("name", "李奶奶");
        elder2.put("relation", "母亲");
        elder2.put("age", 72);
        elder2.put("healthStatus", "一般");
        elders.add(elder2);
        
        return Result.success("获取成功", elders);
    }
    
    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        return "SO" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}