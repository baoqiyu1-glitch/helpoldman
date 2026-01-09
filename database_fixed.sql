-- 助老助残系统完整数据库设计 - 修复版
CREATE DATABASE IF NOT EXISTS helpoldman DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE helpoldman;

-- 服务类型表
CREATE TABLE IF NOT EXISTS service_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '服务类型名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '服务类型代码',
    description TEXT COMMENT '服务描述',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    status VARCHAR(20) DEFAULT 'ENABLED' COMMENT '状态：ENABLED-启用, DISABLED-禁用',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    icon VARCHAR(50) COMMENT '图标',
    color VARCHAR(20) COMMENT '颜色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 用户表（统一版本）
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(100) COMMENT '真实姓名',
    user_type VARCHAR(20) NOT NULL COMMENT '用户类型：ADMIN-管理员, ELDER-老人, VOLUNTEER-志愿者, FAMILY-家属',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-激活, INACTIVE-未激活, DISABLED-禁用',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    age INT COMMENT '年龄',
    gender VARCHAR(10) COMMENT '性别',
    health_status VARCHAR(200) COMMENT '健康状况',
    address TEXT COMMENT '地址',
    avatar VARCHAR(500) COMMENT '头像',
    id_card VARCHAR(20) COMMENT '身份证号',
    emergency_contact VARCHAR(100) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    service_hours INT DEFAULT 0 COMMENT '服务时长(志愿者)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 服务订单表
CREATE TABLE service_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    service_type_id BIGINT NOT NULL COMMENT '服务类型ID',
    title VARCHAR(200) NOT NULL COMMENT '订单标题',
    content TEXT COMMENT '服务内容',
    address VARCHAR(200) COMMENT '服务地址',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    service_time DATETIME COMMENT '期望服务时间',
    status ENUM('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM' COMMENT '优先级',
    remarks TEXT COMMENT '备注',
    volunteer_id BIGINT COMMENT '接单志愿者ID',
    assign_time DATETIME COMMENT '分配时间',
    accept_time DATETIME COMMENT '接单时间',
    complete_time DATETIME COMMENT '完成时间',
    rating INT COMMENT '评分(1-5)',
    feedback TEXT COMMENT '反馈',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (service_type_id) REFERENCES service_types(id),
    FOREIGN KEY (volunteer_id) REFERENCES users(id)
);

-- 辅助器具表
CREATE TABLE equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '器具名称',
    type VARCHAR(50) COMMENT '类型',
    model VARCHAR(100) COMMENT '型号',
    quantity INT DEFAULT 0 COMMENT '总数量',
    available_quantity INT DEFAULT 0 COMMENT '可用数量',
    description TEXT COMMENT '描述',
    image_url VARCHAR(200) COMMENT '图片URL',
    status ENUM('AVAILABLE', 'UNAVAILABLE') DEFAULT 'AVAILABLE',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 器具申请记录表
CREATE TABLE equipment_applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    apply_reason TEXT COMMENT '申请理由',
    expected_duration INT COMMENT '预计使用时长(天)',
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'RETURNED') DEFAULT 'PENDING',
    approve_time DATETIME COMMENT '审批时间',
    return_time DATETIME COMMENT '归还时间',
    remarks TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);

-- 培训课程表
CREATE TABLE IF NOT EXISTS training_courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '课程标题',
    description TEXT COMMENT '课程描述',
    instructor VARCHAR(100) NOT NULL COMMENT '讲师',
    course_time DATETIME NOT NULL COMMENT '课程时间',
    location VARCHAR(200) NOT NULL COMMENT '地点',
    max_participants INT DEFAULT 50 COMMENT '最大参与人数',
    current_participants INT DEFAULT 0 COMMENT '当前参与人数',
    image_url VARCHAR(500) COMMENT '课程图片',
    status VARCHAR(20) DEFAULT 'UPCOMING' COMMENT '状态：UPCOMING-即将开始, ONGOING-进行中, COMPLETED-已完成',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 培训报名表
CREATE TABLE IF NOT EXISTS training_registrations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    status VARCHAR(20) DEFAULT 'REGISTERED' COMMENT '状态：REGISTERED-已报名, CANCELLED-已取消',
    registration_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_course (user_id, course_id)
);

-- 紧急报警表
CREATE TABLE emergency_alerts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    alert_type ENUM('MEDICAL', 'FALL', 'FIRE', 'OTHER') DEFAULT 'MEDICAL',
    location VARCHAR(200) COMMENT '报警位置',
    description TEXT COMMENT '情况描述',
    status ENUM('PENDING', 'PROCESSING', 'RESOLVED') DEFAULT 'PENDING',
    handler_id BIGINT COMMENT '处理人ID',
    handle_time DATETIME COMMENT '处理时间',
    result TEXT COMMENT '处理结果',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (handler_id) REFERENCES users(id)
);

-- 捐赠记录表
CREATE TABLE donations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    donor_id BIGINT NOT NULL COMMENT '捐赠人ID',
    donation_type ENUM('MONEY', 'GOODS', 'SERVICE') DEFAULT 'MONEY',
    amount DECIMAL(10,2) COMMENT '金额',
    goods_name VARCHAR(200) COMMENT '物品名称',
    goods_quantity INT COMMENT '物品数量',
    description TEXT COMMENT '描述',
    status ENUM('PENDING', 'RECEIVED', 'DISTRIBUTED') DEFAULT 'PENDING',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (donor_id) REFERENCES users(id)
);

-- 插入基础数据（使用英文避免编码问题）
INSERT INTO service_types (name, code, description, sort_order) VALUES
('Purchase', 'PURCHASE', 'Help purchase daily necessities', 1),
('Housework', 'HOUSEWORK', 'Help with cleaning and housekeeping', 2),
('Equipment', 'EQUIPMENT', 'Apply for wheelchairs, walkers, etc.', 3),
('Renovation', 'RENOVATION', 'Home accessibility modifications', 4),
('Training', 'TRAINING', 'Smartphone usage, health tips training', 5),
('Nursing', 'NURSING', 'Professional nursing services', 6),
('Meal Delivery', 'MEAL_DELIVERY', 'Meal delivery service for elderly', 7),
('Community Help', 'COMMUNITY_HELP', 'Neighborhood mutual assistance', 8),
('Emergency', 'EMERGENCY', 'Emergency alert service', 9);

INSERT INTO equipment (name, type, quantity, available_quantity, description) VALUES
('Wheelchair', 'MOBILITY', 10, 10, 'Lightweight wheelchair for indoor use'),
('Walker', 'MOBILITY', 15, 15, 'Four-legged walker with good stability'),
('Crutches', 'MOBILITY', 20, 20, 'Aluminum material, adjustable height'),
('Hearing Aid', 'HEARING', 8, 8, 'Digital hearing aid with high clarity'),
('Reading Glasses', 'VISION', 30, 30, 'Multiple prescriptions available');

INSERT INTO users (username, password, real_name, phone, age, gender, user_type, address, email) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV2UiK', 'System Admin', '13800138000', 35, 'MALE', 'ADMIN', 'System', 'admin@helpoldman.com'),
('oldman001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV2UiK', 'Zhang Daye', '13800138001', 75, 'MALE', 'ELDER', 'Beijing Chaoyang District', 'oldman001@helpoldman.com'),
('volunteer001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV2UiK', 'Wang Volunteer', '13800138002', 28, 'MALE', 'VOLUNTEER', 'Beijing Chaoyang District', 'volunteer001@helpoldman.com'),
('family001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV2UiK', 'Li Family', '13800138003', 45, 'FEMALE', 'FAMILY', 'Beijing Chaoyang District', 'family001@helpoldman.com');