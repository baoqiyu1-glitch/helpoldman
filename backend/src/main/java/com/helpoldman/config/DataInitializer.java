package com.helpoldman.config;

import com.helpoldman.entity.ServiceType;
import com.helpoldman.entity.TrainingCourse;
import com.helpoldman.entity.User;
import com.helpoldman.service.ServiceTypeService;
import com.helpoldman.service.TrainingCourseService;
import com.helpoldman.service.UserService;
import com.helpoldman.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ServiceTypeService serviceTypeService;
    
    @Autowired
    private TrainingCourseService trainingCourseService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initAdminUser();
        initServiceTypes();
        initTrainingCourses();
        initVolunteerUsers();
    }
    
    private void initAdminUser() {
        long count = userService.count();
        if (count == 0) {
            // 创建管理员用户
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRealName("系统管理员");
            admin.setUserType("ADMIN");
            admin.setStatus("ACTIVE");
            admin.setPhone("13800000000");
            admin.setEmail("admin@helpoldman.com");
            userService.save(admin);
            
            // 创建测试志愿者
            User volunteer = new User();
            volunteer.setUsername("volunteer");
            volunteer.setPassword(passwordEncoder.encode("123456"));
            volunteer.setRealName("志愿者张三");
            volunteer.setUserType("VOLUNTEER");
            volunteer.setStatus("ACTIVE");
            volunteer.setPhone("13800000001");
            volunteer.setEmail("volunteer@helpoldman.com");
            userService.save(volunteer);
            
            // 创建测试老人用户
            User elder = new User();
            elder.setUsername("elder");
            elder.setPassword(passwordEncoder.encode("123456"));
            elder.setRealName("李大爷");
            elder.setUserType("ELDER");
            elder.setStatus("ACTIVE");
            elder.setPhone("13800000002");
            elder.setEmail("elder@helpoldman.com");
            elder.setAge(75);
            elder.setAddress("北京市朝阳区某某小区");
            userService.save(elder);
            
            System.out.println("初始化测试用户完成");
            System.out.println("管理员账号：admin/123456");
            System.out.println("志愿者账号：volunteer/123456");
            System.out.println("老人账号：elder/123456");
        }
    }
    
    private void initServiceTypes() {
        long count = serviceTypeService.count();
        if (count == 0) {
            List<ServiceType> serviceTypes = Arrays.asList(
                createServiceType("代购生活物资", "PURCHASE", "帮助购买日常生活用品", 1, "🛒", "#FF6B6B"),
                createServiceType("上门协助家务", "HOUSEWORK", "帮助打扫卫生、整理家务", 2, "🏠", "#4ECDC4"),
                createServiceType("辅助器具申请", "EQUIPMENT", "申请轮椅、助行器等辅助器具", 3, "♿", "#45B7D1"),
                createServiceType("无障碍改造", "RENOVATION", "居家无障碍设施改造", 4, "🔧", "#96CEB4"),
                createServiceType("技能培训", "TRAINING", "智能手机使用、养生技巧等培训", 5, "📚", "#FECA57"),
                createServiceType("申请护工", "NURSING", "专业护工上门服务", 6, "👨‍⚕️", "#FF9FF3"),
                createServiceType("送饭服务", "MEAL_DELIVERY", "为老人送餐服务", 7, "🍱", "#54A0FF"),
                createServiceType("社区互助", "COMMUNITY_HELP", "邻里互助服务", 8, "🤝", "#5F27CD"),
                createServiceType("紧急报警", "EMERGENCY", "紧急情况报警", 9, "🚨", "#FF6B6B")
            );
            serviceTypeService.saveBatch(serviceTypes);
            System.out.println("初始化服务类型数据完成");
        }
    }
    
    private void initTrainingCourses() {
        long count = trainingCourseService.count();
        if (count == 0) {
            // 创建一些示例培训课程
            TrainingCourse course1 = new TrainingCourse();
            course1.setTitle("智能手机使用培训");
            course1.setDescription("教授老年人如何使用智能手机，包括微信、支付宝等常用APP");
            course1.setInstructor("张老师");
            course1.setCourseTime(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7天后
            course1.setLocation("社区活动中心");
            course1.setMaxParticipants(30);
            course1.setCurrentParticipants(0);
            course1.setStatus("UPCOMING");
            trainingCourseService.save(course1);
            
            TrainingCourse course2 = new TrainingCourse();
            course2.setTitle("健康养生知识讲座");
            course2.setDescription("讲解老年人健康养生知识，包括饮食、运动、心理等方面");
            course2.setInstructor("王医生");
            course2.setCourseTime(new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000)); // 14天后
            course2.setLocation("社区卫生站");
            course2.setMaxParticipants(50);
            course2.setCurrentParticipants(0);
            course2.setStatus("UPCOMING");
            trainingCourseService.save(course2);
            
            System.out.println("初始化培训课程数据完成");
        }
    }
    
    private void initVolunteerUsers() {
        // 可以在这里初始化更多的志愿者用户
        // 实际项目中可以从Excel或CSV文件导入
    }
    
    private ServiceType createServiceType(String name, String code, String desc, int order, String icon, String color) {
        ServiceType type = new ServiceType();
        type.setName(name);
        type.setCode(code);
        type.setDescription(desc);
        type.setSortOrder(order);
        type.setStatus("ENABLED");
        type.setIcon(icon);
        type.setColor(color);
        return type;
    }
}