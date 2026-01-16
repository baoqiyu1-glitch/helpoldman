package com.helpoldman.service.impl;

import com.helpoldman.service.VolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;

import com.helpoldman.entity.ServiceOrder;
import com.helpoldman.mapper.ServiceOrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.helpoldman.entity.User;
import com.helpoldman.service.UserService;
import com.helpoldman.entity.Product;
import com.helpoldman.mapper.ProductMapper;

@Service
public class VolunteerServiceImpl implements VolunteerService {
    
    @Autowired
    private ServiceOrderMapper serviceOrderMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductMapper productMapper;
    
    // 添加一个内存存储来保存申请信息
    private final Map<Long, Map<String, Object>> volunteerApplications = new ConcurrentHashMap<>();
    
    @Override
    public Map<String, Object> getVolunteerDashboard(Long userId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        // 获取用户基本信息
        Map<String, Object> userInfo = getVolunteerProfile(userId);
        dashboard.put("userInfo", userInfo);
        
        // 获取服务统计
        Map<String, Object> stats = getVolunteerStats(userId);
        dashboard.put("stats", stats);
        
        // 获取快速服务入口
        List<Map<String, Object>> quickServices = getVolunteerServiceTypes();
        dashboard.put("quickServices", quickServices);
        
        // 获取最近任务
        List<Map<String, Object>> recentTasks = new ArrayList<>();
        
        // 查询志愿者已接受的所有服务订单（包括代购和家政服务）
        QueryWrapper<ServiceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("service_type_id", Arrays.asList(1, 2)) // 代购(1)和家政(2)服务
                   .eq("volunteer_id", userId) // 当前志愿者已接受的订单
                   .in("status", Arrays.asList("ASSIGNED", "IN_PROGRESS", "COMPLETED")) // 已分配、进行中、已完成
                   .orderByDesc("create_time")
                   .last("LIMIT 5"); // 只获取最近5条任务
        
        List<ServiceOrder> orders = serviceOrderMapper.selectList(queryWrapper);
        
        for (ServiceOrder order : orders) {
            Map<String, Object> task = new HashMap<>();
            task.put("id", order.getOrderNo());
            task.put("title", order.getTitle());
            task.put("type", getServiceTypeCode(order.getServiceTypeId()));
            task.put("status", getTaskStatus(order.getStatus()));
            task.put("date", formatDate(order.getCreateTime()));
            task.put("description", order.getContent());
            
            // 如果是代购订单，添加商品信息
            if (order.getServiceTypeId() == 1) {
                task.put("totalPrice", calculateTotalPrice(order));
                task.put("products", parseProducts(order.getContent()));
            }
            
            recentTasks.add(task);
        }
        
        dashboard.put("tasks", recentTasks);
        
        return dashboard;
    }

    @Override
    public Map<String, Object> getVolunteerStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalHours", 128);
        stats.put("completedTasks", 45);
        stats.put("currentTasks", 3);
        stats.put("rating", 4.8);
        stats.put("monthlyHours", 32);
        stats.put("weeklyTasks", 5);
        return stats;
    }

    @Override
    public Map<String, Object> getVolunteerTasks(Long userId, Integer page, Integer size) {
        Map<String, Object> result = new HashMap<>();
        
        // 计算分页偏移量
        int offset = (page - 1) * size;
        
        // 查询志愿者已接受的所有服务订单（包括代购和家政服务）
        QueryWrapper<ServiceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("service_type_id", Arrays.asList(1, 2)) // 代购(1)和家政(2)服务
                   .eq("volunteer_id", userId) // 当前志愿者已接受的订单
                   .in("status", Arrays.asList("ASSIGNED", "IN_PROGRESS", "COMPLETED")) // 已分配、进行中、已完成
                   .orderByDesc("create_time");
        
        List<ServiceOrder> orders = serviceOrderMapper.selectList(queryWrapper);
        
        List<Map<String, Object>> tasks = new ArrayList<>();
        for (ServiceOrder order : orders) {
            Map<String, Object> task = new HashMap<>();
            task.put("id", order.getOrderNo());
            task.put("title", order.getTitle());
            task.put("type", getServiceTypeCode(order.getServiceTypeId()));
            task.put("status", getTaskStatus(order.getStatus()));
            task.put("date", formatDate(order.getCreateTime()));
            task.put("description", order.getContent());
            
            // 如果是代购订单，添加商品信息
            if (order.getServiceTypeId() == 1) {
                task.put("totalPrice", calculateTotalPrice(order));
            }
            
            tasks.add(task);
        }
        
        result.put("tasks", tasks);
        result.put("total", tasks.size());
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getVolunteerServiceTypes() {
        List<Map<String, Object>> serviceTypes = new ArrayList<>();
        
        // 添加代购服务
        Map<String, Object> shoppingService = new HashMap<>();
        shoppingService.put("id", 1);
        shoppingService.put("name", "代购物资");
        shoppingService.put("icon", "shopping-cart");
        shoppingService.put("color", "#FF6B6B");
        shoppingService.put("description", "帮助老人购买日常用品、药品等");
        serviceTypes.add(shoppingService);
        
        // 添加家政服务
        Map<String, Object> houseworkService = new HashMap<>();
        houseworkService.put("id", 2);
        houseworkService.put("name", "家政服务");
        houseworkService.put("icon", "home");
        houseworkService.put("color", "#4ECDC4");
        houseworkService.put("description", "帮助老人打扫卫生、整理家务等");
        serviceTypes.add(houseworkService);
        
        // 添加医疗服务
        Map<String, Object> medicalService = new HashMap<>();
        medicalService.put("id", 3);
        medicalService.put("name", "医疗服务");
        medicalService.put("icon", "hospital");
        medicalService.put("color", "#96CEB4");
        medicalService.put("description", "帮助老人测量血压、陪同就医等");
        serviceTypes.add(medicalService);
        
        // 添加送餐服务
        Map<String, Object> foodService = new HashMap<>();
        foodService.put("id", 4);
        foodService.put("name", "送餐服务");
        foodService.put("icon", "food");
        foodService.put("color", "#FFEAA7");
        foodService.put("description", "为老人提供餐饮配送服务");
        serviceTypes.add(foodService);
        
        // 添加培训服务
        Map<String, Object> trainingService = new HashMap<>();
        trainingService.put("id", 5);
        trainingService.put("name", "技能培训");
        trainingService.put("icon", "book");
        trainingService.put("color", "#DDA0DD");
        trainingService.put("description", "为老人提供智能手机使用、健康知识等培训");
        serviceTypes.add(trainingService);
        
        return serviceTypes;
    }

    @Override
    public Map<String, Object> getVolunteerTextConfig() {
        Map<String, Object> textConfig = new HashMap<>();
        textConfig.put("welcome", "欢迎回来，志愿者！");
        textConfig.put("quickServices", "快速服务");
        textConfig.put("recentTasks", "最近任务");
        textConfig.put("serviceStats", "服务统计");
        textConfig.put("totalHours", "总服务时长(小时)");
        textConfig.put("completedTasks", "已完成任务");
        textConfig.put("currentTasks", "当前任务");
        textConfig.put("rating", "用户评分");
        textConfig.put("monthlyHours", "本月服务时长");
        textConfig.put("weeklyTasks", "本周任务");
        return textConfig;
    }

    @Override
    public Map<String, Object> getVolunteerProfile(Long userId) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("name", "张三");
        profile.put("avatar", "https://example.com/avatar.jpg");
        profile.put("phone", "13800138000");
        profile.put("email", "zhangsan@example.com");
        profile.put("address", "北京市朝阳区");
        profile.put("joinDate", "2023-01-15");
        profile.put("totalHours", 128);
        profile.put("completedTasks", 45);
        profile.put("rating", 4.8);
        return profile;
    }

    @Override
    public boolean updateVolunteerProfile(Long userId, Map<String, Object> profileData) {
        // 这里应该实现更新志愿者个人资料的逻辑
        // 目前返回模拟数据
        return true;
    }

        @Override
    public Map<String, Object> getShoppingOrders(Long userId, Integer page, Integer size) {
        Map<String, Object> result = new HashMap<>();
        
        // 计算分页偏移量
        int offset = (page - 1) * size;
        
        // 查询志愿者可接受的代购订单（未分配或分配给当前志愿者的）
        QueryWrapper<ServiceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_type_id", 1) // 代购服务
                   .eq("status", "PENDING") // 待分配
                   .orderByDesc("create_time");
        
        List<ServiceOrder> orders = serviceOrderMapper.selectList(queryWrapper);
        
        List<Map<String, Object>> orderList = new ArrayList<>();
        for (ServiceOrder order : orders) {
            // 获取用户信息
            User user = getUserById(order.getUserId());
            String userName = user != null ? user.getRealName() : "未知用户";
            
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getOrderNo());
            orderMap.put("title", order.getTitle());
            orderMap.put("content", order.getContent());
            orderMap.put("createTime", order.getCreateTime());
            orderMap.put("time", formatDateTime(order.getCreateTime())); // 添加time字段，格式化为中文日期时间
            orderMap.put("address", order.getAddress());
            orderMap.put("status", order.getStatus());
            orderMap.put("userName", userName); // 添加userName字段
            
            // 计算总金额
            orderMap.put("totalPrice", calculateTotalPrice(order));
            
            orderList.add(orderMap);
        }
        
        result.put("orders", orderList);
        result.put("total", orderList.size());
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    @Override
    public boolean acceptShoppingOrder(Long userId, String orderId) {
        // 这里应该实现接受代购订单的逻辑
        // 目前返回模拟数据
        return true;
    }

    @Override
    public boolean rejectShoppingOrder(Long userId, String orderId) {
        // 这里应该实现拒绝代购订单的逻辑
        // 目前返回模拟数据
        return true;
    }

                @Override
    public List<Map<String, Object>> getHouseworkTasks(Long userId) {
        List<Map<String, Object>> tasks = new ArrayList<>();
        
        // 查询志愿者可接受的家政服务任务（未分配的）
        QueryWrapper<ServiceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_type_id", 2) // 家政服务
                   .eq("status", "PENDING") // 待分配
                   .orderByDesc("create_time");
        
        List<ServiceOrder> orders = serviceOrderMapper.selectList(queryWrapper);
        
        for (ServiceOrder order : orders) {
            // 获取用户信息
            User user = getUserById(order.getUserId());
            String userName = user != null ? user.getRealName() : "未知用户";
            
            Map<String, Object> task = new HashMap<>();
            task.put("id", order.getOrderNo());
            task.put("status", order.getStatus().toLowerCase()); // 转换为小写以匹配前端期望
            task.put("userName", userName); // 添加用户名
            task.put("taskType", order.getTitle()); // 使用title作为taskType
            task.put("address", order.getAddress());
            // 修改：使用formatDateTime方法格式化时间为中文格式
            task.put("time", formatDateTime(order.getServiceTime() != null ? order.getServiceTime() : order.getCreateTime()));
            task.put("description", order.getContent()); // 使用content作为description
            task.put("estimatedHours", 2.0); // 默认预计时长为2小时，实际项目中应该从数据库或其他方式获取
            
            tasks.add(task);
        }
        
        return tasks;
    }

    @Override
    public boolean acceptHouseworkTask(Long userId, String taskId) {
        // 这里应该实现接受家政服务任务的逻辑
        // 目前返回模拟数据
        return true;
    }

    @Override
    public boolean rejectHouseworkTask(Long userId, String taskId) {
        // 这里应该实现拒绝家政服务任务的逻辑
        // 目前返回模拟数据
        return true;
    }

    @Override
    public List<Map<String, Object>> getTrainingCourses(Long userId) {
        List<Map<String, Object>> courses = new ArrayList<>();
        
        // 添加培训课程列表
        courses.add(createTrainingCourse("1", "智能手机使用入门", "教授老年人如何使用智能手机进行基本操作", "2023-06-15 09:00", "社区活动室", "李老师", 2, 30));
        courses.add(createTrainingCourse("2", "健康饮食与营养搭配", "介绍适合老年人的健康饮食知识和营养搭配技巧", "2023-06-18 14:00", "社区会议室", "王医生", 1.5, 25));
        courses.add(createTrainingCourse("3", "常见疾病预防与护理", "讲解老年人常见疾病的预防和家庭护理方法", "2023-06-22 10:00", "社区医院", "张护士", 2, 20));
        courses.add(createTrainingCourse("4", "家庭急救知识培训", "教授老年人家庭急救的基本技能和注意事项", "2023-06-25 09:30", "社区活动室", "刘医生", 1.5, 35));
        
        return courses;
    }

    @Override
    public boolean registerTrainingCourse(Long userId, String courseId) {
        // 这里应该实现报名培训课程的逻辑
        // 目前返回模拟数据
        return true;
    }

    @Override
    public Map<String, Object> getApplyInfo(Long userId) {
        // 从内存中获取申请信息，如果不存在则返回默认值
        return volunteerApplications.getOrDefault(userId, new HashMap<>());
    }

    @Override
    public boolean submitApply(Long userId, Map<String, Object> applyData) {
        // 将申请信息保存到内存中
        volunteerApplications.put(userId, applyData);
        return true;
    }

    @Override
    public List<Map<String, Object>> getSkills() {
        List<Map<String, Object>> skills = new ArrayList<>();
        
        // 添加常用技能
        skills.add(createSkill("1", "老年人护理", "具备基本的老年人护理知识和技能"));
        skills.add(createSkill("2", "医疗护理", "具备专业的医疗护理背景"));
        skills.add(createSkill("3", "心理疏导", "具备心理咨询或疏导能力"));
        skills.add(createSkill("4", "家政服务", "擅长各种家政服务工作"));
        skills.add(createSkill("5", "烹饪技能", "擅长烹饪适合老年人的饮食"));
        skills.add(createSkill("6", "康复训练", "具备康复训练指导能力"));
        
        return skills;
    }
    
    @Override
    public boolean completeOrder(Long userId, String orderId) {
        try {
            ServiceOrder order = serviceOrderMapper.selectOne(new QueryWrapper<ServiceOrder>()
                .eq("order_no", orderId)
                .eq("volunteer_id", userId));
            
            if (order != null) {
                order.setStatus("COMPLETED");
                order.setCompleteTime(new Date());
                serviceOrderMapper.updateById(order);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

        @Override
    public List<Map<String, Object>> getMyTasks(Long userId) {
        List<ServiceOrder> orders = serviceOrderMapper.selectList(new QueryWrapper<ServiceOrder>()
            .eq("volunteer_id", userId)
            .in("status", Arrays.asList("ASSIGNED", "IN_PROGRESS", "COMPLETED"))
            .orderByDesc("create_time"));
        
        List<Map<String, Object>> tasks = new ArrayList<>();
        for (ServiceOrder order : orders) {
            Map<String, Object> task = new HashMap<>();
            task.put("id", order.getOrderNo());
            
            // 修复：添加空值检查，防止NullPointerException
            User user = getUserById(order.getUserId());
            task.put("userName", user != null ? user.getRealName() : "未知用户");
            
            task.put("title", order.getTitle());
            // 修复：使用getTaskStatus方法转换状态，确保ASSIGNED状态转换为in_progress
            task.put("status", getTaskStatus(order.getStatus()));
            task.put("date", order.getCreateTime().toString());
            task.put("address", order.getAddress());
            task.put("type", getServiceTypeCode(order.getServiceTypeId()));
            tasks.add(task);
        }
        return tasks;
    }
@Override
public Map<String, Object> getOrderDetails(String orderId) {
    ServiceOrder order = serviceOrderMapper.selectOne(new QueryWrapper<ServiceOrder>()
        .eq("order_no", orderId));
    
    if (order == null) {
        return null;
    }
    
    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("id", order.getOrderNo());
    orderMap.put("userName", getUserById(order.getUserId()).getRealName()); // 修改：使用getRealName()而不是getName()
    orderMap.put("time", order.getCreateTime().toString());
    orderMap.put("address", order.getAddress());
    orderMap.put("status", order.getStatus().toLowerCase());
    orderMap.put("title", order.getTitle());
    orderMap.put("content", order.getContent());
    
    // 如果是代购订单，添加商品信息
    if (order.getServiceTypeId() == 1) {
        // 使用parseProducts方法从订单内容中解析商品信息
        List<Map<String, Object>> productList = parseProducts(order.getContent());
        double totalPrice = calculateTotalPrice(order);
        
        orderMap.put("products", productList);
        orderMap.put("totalPrice", totalPrice);
    }
    
    return orderMap;
}
    
    // 添加获取用户信息的辅助方法
    private User getUserById(Long userId) {
        return userService.getById(userId);
    }
    
    private Map<String, Object> createTrainingCourse(String id, String title, String description, 
                                                   String time, String location, String instructor, 
                                                   double duration, int capacity) {
        Map<String, Object> course = new HashMap<>();
        course.put("id", id);
        course.put("title", title);
        course.put("description", description);
        course.put("time", time);
        course.put("location", location);
        course.put("instructor", instructor);
        course.put("duration", duration);
        course.put("capacity", capacity);
        course.put("registeredCount", 0);
        return course;
    }

    private Map<String, Object> createSkill(String id, String name, String description) {
        Map<String, Object> skill = new HashMap<>();
        skill.put("id", id);
        skill.put("name", name);
        skill.put("description", description);
        return skill;
    }

    private String getServiceTypeCode(Long serviceTypeId) {
        if (serviceTypeId == null) {
            return "unknown";
        }
        switch (serviceTypeId.intValue()) {
            case 1: return "shopping";
            case 2: return "housework";
            case 3: return "equipment";
            case 5: return "training"; // 培训服务在数据库中是5
            default: return "unknown";
        }
    }

    private String getTaskStatus(String status) {
        if (status == null) {
            return "unknown";
        }
        switch (status.toUpperCase()) {
            case "PENDING": return "pending";
            case "ASSIGNED": return "in_progress"; // 将ASSIGNED状态转换为in_progress，表示进行中
            case "IN_PROGRESS": return "in_progress";
            case "COMPLETED": return "completed";
            case "CANCELLED": return "cancelled";
            default: return "unknown";
        }
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    private double calculateTotalPrice(ServiceOrder order) {
        // 解析订单内容中的总金额
        if (order.getContent() != null && !order.getContent().isEmpty()) {
            try {
                // 查找"总金额："后面的数字
                String content = order.getContent();
                int startIndex = content.indexOf("总金额：");
                if (startIndex != -1) {
                    startIndex += 4; // 跳过"总金额："4个字符
                    int endIndex = content.indexOf("元", startIndex);
                    if (endIndex != -1) {
                        String priceStr = content.substring(startIndex, endIndex).trim();
                        return Double.parseDouble(priceStr);
                    }
                }
            } catch (Exception e) {
                System.err.println("解析订单总价失败: " + e.getMessage());
            }
        }
        return 0.0;
    }

    private List<Map<String, Object>> parseProducts(String content) {
        List<Map<String, Object>> products = new ArrayList<>();
        
        if (content == null || content.isEmpty()) {
            return products;
        }
        
        try {
            // 解析"代购商品："后面的商品信息
            int startIndex = content.indexOf("代购商品：");
            if (startIndex != -1) {
                startIndex += 5; // 跳过"代购商品："5个字符
                
                // 查找"总金额："的位置，作为商品信息的结束位置
                int endIndex = content.indexOf("；总金额：");
                if (endIndex == -1) {
                    endIndex = content.length();
                }
                
                String productsStr = content.substring(startIndex, endIndex).trim();
                
                // 按分号分割多个商品
                String[] productItems = productsStr.split("；");
                
                for (String productItem : productItems) {
                    if (productItem.trim().isEmpty()) {
                        continue;
                    }
                    
                    // 解析商品名称和描述
                    Map<String, Object> product = new HashMap<>();
                    
                    // 查找括号位置，分离商品名称和描述
                    int bracketIndex = productItem.indexOf("(");
                    if (bracketIndex != -1) {
                        // 有描述的情况
                        String name = productItem.substring(0, bracketIndex).trim();
                        String description = productItem.substring(bracketIndex + 1);
                        if (description.endsWith(")")) {
                            description = description.substring(0, description.length() - 1);
                        }
                        product.put("name", name);
                        product.put("description", description.trim());
                    } else {
                        // 只有商品名称，没有描述
                        product.put("name", productItem.trim());
                        product.put("description", "");
                    }
                    
                    // 默认数量为1
                    product.put("quantity", 1);
                    
                    // 从总金额中计算单价（如果有多个商品，平均分配）
                    // 这里简化处理，实际应该从订单内容中获取每个商品的价格
                    product.put("price", 0.0);
                    
                    products.add(product);
                }
                
                // 如果有总金额信息，分配给各个商品
                int totalAmountIndex = content.indexOf("总金额：");
                if (totalAmountIndex != -1 && !products.isEmpty()) {
                    int amountStartIndex = totalAmountIndex + 4;
                    int amountEndIndex = content.indexOf("元", amountStartIndex);
                    if (amountEndIndex != -1) {
                        String totalAmountStr = content.substring(amountStartIndex, amountEndIndex).trim();
                        try {
                            double totalAmount = Double.parseDouble(totalAmountStr);
                            // 如果只有一个商品，直接设置价格为总金额
                            if (products.size() == 1) {
                                products.get(0).put("price", totalAmount);
                            } else {
                                // 多个商品，平均分配价格（简化处理）
                                double avgPrice = totalAmount / products.size();
                                for (Map<String, Object> product : products) {
                                    product.put("price", avgPrice);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("解析总金额失败: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("解析商品列表失败: " + e.getMessage());
        }
        
        return products;
    }
}