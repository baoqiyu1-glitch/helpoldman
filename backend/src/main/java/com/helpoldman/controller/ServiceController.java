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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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
     * 获取家务服务类型（家属端专用）
     */
    @GetMapping("/housework/services")
    public Result getHouseworkServices() {
        List<Map<String, Object>> services = new ArrayList<>();
        
        // 使用HashMap替代Map.of()，兼容Java 8
        Map<String, Object> service1 = new HashMap<>();
        service1.put("id", 1);
        service1.put("name", "日常打扫");
        service1.put("description", "包括客厅、卧室、厨房等区域的日常清洁");
        service1.put("price", 50.0);
        service1.put("duration", "2小时");
        service1.put("category", "HOUSEWORK");
        services.add(service1);
        
        Map<String, Object> service2 = new HashMap<>();
        service2.put("id", 2);
        service2.put("name", "洗衣做饭");
        service2.put("description", "帮助清洗衣物和准备简单餐食");
        service2.put("price", 80.0);
        service2.put("duration", "3小时");
        service2.put("category", "HOUSEWORK");
        services.add(service2);
        
        Map<String, Object> service3 = new HashMap<>();
        service3.put("id", 3);
        service3.put("name", "物品整理");
        service3.put("description", "帮助整理衣柜、书架等物品");
        service3.put("price", 60.0);
        service3.put("duration", "2小时");
        service3.put("category", "HOUSEWORK");
        services.add(service3);
        
        Map<String, Object> service4 = new HashMap<>();
        service4.put("id", 4);
        service4.put("name", "深度清洁");
        service4.put("description", "包括窗户、卫生间、厨房的深度清洁");
        service4.put("price", 120.0);
        service4.put("duration", "4小时");
        service4.put("category", "HOUSEWORK");
        services.add(service4);
        
        Map<String, Object> service5 = new HashMap<>();
        service5.put("id", 5);
        service5.put("name", "陪护服务");
        service5.put("description", "陪伴老人聊天、散步等");
        service5.put("price", 40.0);
        service5.put("duration", "1小时");
        service5.put("category", "HOUSEWORK");
        services.add(service5);
        
        return Result.success(services);
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
    public Result applyService(@RequestBody ServiceApplyDTO applyDTO, HttpServletRequest request) {
        try {
            // 获取用户ID
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error(401, "用户未登录");
            }
            
            // 创建服务订单
            ServiceOrder order = new ServiceOrder();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            order.setServiceTypeId(applyDTO.getServiceTypeId());
            
            // 修复字段映射：使用前端发送的字段名
            order.setTitle("代购需求");  // 设置默认标题
            order.setContent(applyDTO.getServiceContent());  // 使用serviceContent
            order.setAddress(applyDTO.getAddress());
            order.setContactPhone(applyDTO.getContactPhone());
            order.setServiceTime(applyDTO.getAppointmentTime());  // 使用appointmentTime
            order.setStatus("PENDING");
            order.setPriority("MEDIUM");
            
            // 保存订单
            boolean saveResult = serviceOrderService.save(order);
            
            if (saveResult) {
                System.out.println("服务申请保存成功，订单号：" + order.getOrderNo());
                System.out.println("订单ID：" + order.getId());
                
                // 返回成功响应
                return Result.success("服务申请提交成功");
            } else {
                System.out.println("服务申请保存失败");
                return Result.error("服务申请失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("服务申请异常：" + e.getMessage());
            return Result.error("系统异常，请稍后重试");
        }
    }

    /**
     * 家属申请家务服务（专用接口）- 只保留一个版本
     */
    @PostMapping("/family/housework-apply")
    public Result applyHouseworkService(@RequestBody Map<String, Object> serviceData, 
                                       HttpServletRequest request) {
        try {
            // 获取用户ID
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error(401, "用户未登录");
            }
            
            // 创建服务订单
            ServiceOrder order = new ServiceOrder();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            order.setServiceTypeId(2L); // 家务服务类型ID
            
            // 设置订单信息
            order.setTitle("家务服务申请 - " + serviceData.get("serviceType"));
            order.setContent((String) serviceData.get("specialRequirements"));
            order.setAddress((String) serviceData.get("address"));
            order.setContactPhone((String) serviceData.get("phone"));
            // 修复：删除不存在的setServiceTime调用，使用serviceTime字段（Date类型）
            // order.setServiceTime((String) serviceData.get("serviceTime")); // 删除此行
            order.setStatus("PENDING");
            order.setPriority("MEDIUM");
            
            // 删除不存在的字段设置：elderId、duration和totalAmount
            // if (serviceData.containsKey("elderId")) {
            //     order.setElderId(Long.valueOf(serviceData.get("elderId").toString())); // 删除此行
            // }
            
            // 保存订单
            boolean saveResult = serviceOrderService.save(order);
            
            if (saveResult) {
                System.out.println("家务服务申请保存成功，订单号：" + order.getOrderNo());
                return Result.success("家务服务申请提交成功");
            } else {
                return Result.error("申请失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统异常，请稍后重试");
        }
    }

    /**
     * 家属申请送饭服务（专用接口）
     */
    @PostMapping("/family/apply-cooking")
    public Result applyCookingService(@RequestBody Map<String, Object> cookingData, 
                                      HttpServletRequest request) {
        try {
            // 获取用户ID
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error(401, "用户未登录");
            }
            
            // 创建做饭送饭服务订单
            ServiceOrder order = new ServiceOrder();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            order.setServiceTypeId(4L); // 做饭送饭服务类型ID
            
            // 设置订单信息
            order.setTitle("做饭送饭服务申请 - " + cookingData.get("serviceType"));
            order.setContent("菜品要求：" + cookingData.get("dishRequirements") + 
                           "；特殊要求：" + cookingData.get("specialRequirements"));
            order.setAddress((String) cookingData.get("address"));
            order.setContactPhone((String) cookingData.get("phone"));
            // 修复：删除不存在的setServiceTime调用
            // order.setServiceTime((String) cookingData.get("serviceTime")); // 删除此行
            order.setStatus("PENDING");
            order.setPriority("MEDIUM");
            
            // 删除不存在的字段设置：elderId、duration、frequency和totalAmount
            // if (cookingData.containsKey("elderId")) {
            //     order.setElderId(Long.valueOf(cookingData.get("elderId").toString())); // 删除此行
            // }
            
            // 保存订单
            boolean saveResult = serviceOrderService.save(order);
            
            if (saveResult) {
                System.out.println("做饭送饭服务申请保存成功，订单号：" + order.getOrderNo());
                return Result.success("做饭送饭服务申请提交成功");
            } else {
                return Result.error("申请失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统异常，请稍后重试");
        }
    }

    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        return "SO" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    /**
     * 获取我的服务订单
     */
    @GetMapping("/service-orders/my")
    public Result getMyServiceOrders(@RequestHeader("Authorization") String token,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        try {
            // 修复：使用私有方法处理Bearer前缀
            Long userId = getUserIdFromToken(token);
            
            // 使用MyBatis Plus分页插件
            Page<ServiceOrder> pageInfo = new Page<>(page, size);
            QueryWrapper<ServiceOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
            
            Page<ServiceOrder> resultPage = serviceOrderService.page(pageInfo, wrapper);
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", resultPage.getRecords());
            result.put("total", resultPage.getTotal());
            result.put("size", resultPage.getSize());
            result.put("current", resultPage.getCurrent());
            result.put("pages", resultPage.getPages());
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取订单失败");
        }
    }
    
    /**
     * 私有方法：从HttpServletRequest中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return getUserIdFromToken(token);
    }
    
    /**
     * 私有方法：从token字符串中获取用户ID - 只保留一个版本
     */
    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
    
    /**
     * 获取学习课程列表（家属端专用）- 只保留一个版本
     */
    @GetMapping("/learning/courses")
    public Result getLearningCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        
        // 使用HashMap替代Map.of()，兼容Java 8
        Map<String, Object> course1 = new HashMap<>();
        course1.put("id", 1);
        course1.put("title", "老人护理基础知识");
        course1.put("description", "学习老人日常护理的基本技能和注意事项");
        course1.put("teacher", "李护士");
        course1.put("duration", "45分钟");
        course1.put("image", "/static/images/nursing.png");
        course1.put("category", "HEALTH_CARE");
        courses.add(course1);
        
        Map<String, Object> course2 = new HashMap<>();
        course2.put("id", 2);
        course2.put("title", "常见疾病应急处理");
        course2.put("description", "学习高血压、糖尿病等常见疾病的应急处理方法");
        course2.put("teacher", "王医生");
        course2.put("duration", "60分钟");
        course2.put("image", "/static/images/emergency.png");
        course2.put("category", "HEALTH_CARE");
        courses.add(course2);
        
        Map<String, Object> course3 = new HashMap<>();
        course3.put("id", 3);
        course3.put("title", "心理疏导技巧");
        course3.put("description", "学习如何与老人进行有效沟通和心理疏导");
        course3.put("teacher", "张心理师");
        course3.put("duration", "50分钟");
        course3.put("image", "/static/images/psychology.png");
        course3.put("category", "PSYCHOLOGY");
        courses.add(course3);
        
        Map<String, Object> course4 = new HashMap<>();
        course4.put("id", 4);
        course4.put("title", "智能设备使用教学");
        course4.put("description", "教授老人使用智能手机、平板等智能设备");
        course4.put("teacher", "陈老师");
        course4.put("duration", "40分钟");
        course4.put("image", "/static/images/technology.png");
        course4.put("category", "TECHNOLOGY");
        courses.add(course4);
        
        Map<String, Object> course5 = new HashMap<>();
        course5.put("id", 5);
        course5.put("title", "康复训练指导");
        course5.put("description", "学习老人康复训练的基本方法和注意事项");
        course5.put("teacher", "刘康复师");
        course5.put("duration", "55分钟");
        course5.put("image", "/static/images/rehabilitation.png");
        course5.put("category", "REHABILITATION");
        courses.add(course5);
        
        return Result.success(courses);
    }

    /**
     * 获取护工服务类型（家属端专用）
     */
    @GetMapping("/nursing/types")
    public Result getNursingServiceTypes() {
        List<Map<String, Object>> serviceTypes = new ArrayList<>();
        
        // 使用HashMap替代Map.of()，兼容Java 8
        Map<String, Object> service1 = new HashMap<>();
        service1.put("id", 1);
        service1.put("name", "日常护理");
        service1.put("description", "协助老人日常起居、个人卫生等基本护理服务");
        service1.put("price", 150);
        serviceTypes.add(service1);
        
        Map<String, Object> service2 = new HashMap<>();
        service2.put("id", 2);
        service2.put("name", "专业护理");
        service2.put("description", "针对特殊疾病或身体状况的专业护理服务");
        service2.put("price", 200);
        serviceTypes.add(service2);
        
        Map<String, Object> service3 = new HashMap<>();
        service3.put("id", 3);
        service3.put("name", "康复护理");
        service3.put("description", "术后康复、功能恢复等专业康复护理服务");
        service3.put("price", 180);
        serviceTypes.add(service3);
        
        Map<String, Object> service4 = new HashMap<>();
        service4.put("id", 4);
        service4.put("name", "夜间护理");
        service4.put("description", "夜间陪护、安全监护等夜间护理服务");
        service4.put("price", 120);
        serviceTypes.add(service4);
        
        return Result.success(serviceTypes);
    }

    /**
     * 家属申请学习服务（专用接口）
     */
    @PostMapping("/family/apply-learning")
    public Result applyLearningService(@RequestBody Map<String, Object> learningData, 
                                       HttpServletRequest request) {
        try {
            // 获取用户ID
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error(401, "用户未登录");
            }
            
            // 创建学习服务订单
            ServiceOrder order = new ServiceOrder();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            order.setServiceTypeId(6L); // 学习服务类型ID
            
            // 设置订单信息
            order.setTitle("学习服务申请 - " + learningData.get("courseName"));
            order.setContent((String) learningData.get("applyReason"));
            order.setStatus("PENDING");
            order.setPriority("LOW");
            
            // 删除不存在的字段设置：elderId和courseId
            // if (learningData.containsKey("elderId")) {
            //     order.setElderId(Long.valueOf(learningData.get("elderId").toString())); // 删除此行
            // }
            // if (learningData.containsKey("courseId")) {
            //     order.setCourseId(Long.valueOf(learningData.get("courseId").toString())); // 删除此行
            // }
            
            // 保存订单
            boolean saveResult = serviceOrderService.save(order);
            
            if (saveResult) {
                System.out.println("学习服务申请保存成功，订单号：" + order.getOrderNo());
                return Result.success("学习服务申请提交成功");
            } else {
                return Result.error("申请失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统异常，请稍后重试");
        }
    }

    // ========== 新增接口：支持前端请求的路径 ==========
    
    /**
     * 家属申请护工服务（兼容前端路径）
     */
    @PostMapping("/family/apply-nursing")
    public Result applyNursingService(@RequestBody Map<String, Object> nursingData, 
                                      HttpServletRequest request) {
        try {
            // 获取用户ID
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error(401, "用户未登录");
            }
            
            // 创建护工服务订单
            ServiceOrder order = new ServiceOrder();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            order.setServiceTypeId(3L); // 护工服务类型ID
            
            // 设置订单信息
            order.setTitle("护工服务申请 - " + nursingData.get("serviceType"));
            order.setContent((String) nursingData.get("specialRequirements"));
            order.setAddress((String) nursingData.get("address"));
            order.setContactPhone((String) nursingData.get("phone"));
            order.setStatus("PENDING");
            order.setPriority("HIGH");
            
            // 保存订单
            boolean saveResult = serviceOrderService.save(order);
            
            if (saveResult) {
                System.out.println("护工服务申请保存成功，订单号：" + order.getOrderNo());
                return Result.success("护工服务申请提交成功");
            } else {
                return Result.error("申请失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统异常，请稍后重试");
        }
    }

    /**
     * 家属申请送饭服务（兼容前端路径）
     */
    @PostMapping("/family/apply-meal")
    public Result applyMealService(@RequestBody Map<String, Object> mealData, 
                                   HttpServletRequest request) {
        try {
            // 获取用户ID
            Long userId = getUserIdFromToken(request);
            if (userId == null) {
                return Result.error(401, "用户未登录");
            }
            
            // 创建送饭服务订单
            ServiceOrder order = new ServiceOrder();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            order.setServiceTypeId(4L); // 送饭服务类型ID
            
            // 设置订单信息
            order.setTitle("送饭服务申请 - " + mealData.get("mealType"));
            order.setContent("餐食类型：" + mealData.get("mealType") + 
                           "；特殊要求：" + mealData.get("specialRequirements"));
            order.setAddress((String) mealData.get("address"));
            order.setContactPhone((String) mealData.get("phone"));
            order.setStatus("PENDING");
            order.setPriority("MEDIUM");
            
            // 保存订单
            boolean saveResult = serviceOrderService.save(order);
            
            if (saveResult) {
                System.out.println("送饭服务申请保存成功，订单号：" + order.getOrderNo());
                return Result.success("送饭服务申请提交成功");
            } else {
                return Result.error("申请失败，请稍后重试");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统异常，请稍后重试");
        }
    }

    /**
     * 获取商品分类（兼容前端路径）
     */
    @GetMapping("/product/categories")
    public Result getProductCategories() {
        // 返回模拟的商品分类数据
        List<Map<String, Object>> categories = new ArrayList<>();
        
        Map<String, Object> category1 = new HashMap<>();
        category1.put("id", 1);
        category1.put("name", "食品");
        category1.put("code", "FOOD");
        category1.put("description", "各类食品和饮料");
        categories.add(category1);
        
        Map<String, Object> category2 = new HashMap<>();
        category2.put("id", 2);
        category2.put("name", "日用品");
        category2.put("code", "DAILY_USE");
        category2.put("description", "日常生活用品");
        categories.add(category2);
        
        Map<String, Object> category3 = new HashMap<>();
        category3.put("id", 3);
        category3.put("name", "药品");
        category3.put("code", "MEDICINE");
        category3.put("description", "常用药品和保健品");
        categories.add(category3);
        
        Map<String, Object> category4 = new HashMap<>();
        category4.put("id", 4);
        category4.put("name", "其他");
        category4.put("code", "OTHER");
        category4.put("description", "其他商品");
        categories.add(category4);
        
        return Result.success(categories);
    }
}