-- 助老助残系统完整数据库设计
CREATE DATABASE IF NOT EXISTS helpoldman DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE helpoldman;

-- 用户表（老人、家属、志愿者、社区人员）
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(20) COMMENT '手机号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    age INT COMMENT '年龄',
    gender ENUM('MALE', 'FEMALE') COMMENT '性别',
    health_status VARCHAR(200) COMMENT '健康状况',
    user_type ENUM('OLD_MAN', 'FAMILY', 'VOLUNTEER', 'COMMUNITY_STAFF', 'ADMIN') NOT NULL COMMENT '用户类型',
    address VARCHAR(200) COMMENT '地址',
    id_card VARCHAR(20) COMMENT '身份证号',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    avatar_url VARCHAR(200) COMMENT '头像URL',
    service_hours INT DEFAULT 0 COMMENT '服务时长(志愿者)',
    status ENUM('ACTIVE', 'INACTIVE', 'PENDING') DEFAULT 'ACTIVE' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

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

-- 用户表
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
    address TEXT COMMENT '地址',
    avatar VARCHAR(500) COMMENT '头像',
    id_card VARCHAR(20) COMMENT '身份证号',
    emergency_contact VARCHAR(100) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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

-- 课程报名表
CREATE TABLE course_registrations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status ENUM('REGISTERED', 'ATTENDED', 'ABSENT') DEFAULT 'REGISTERED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES training_courses(id)
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

-- 插入基础数据
INSERT INTO service_types (name, code, description, sort_order) VALUES
('代购生活物资', 'PURCHASE', '帮助购买日常生活用品', 1),
('上门协助家务', 'HOUSEWORK', '帮助打扫卫生、整理家务', 2),
('辅助器具申请', 'EQUIPMENT', '申请轮椅、助行器等辅助器具', 3),
('无障碍改造', 'RENOVATION', '居家无障碍设施改造', 4),
('技能培训', 'TRAINING', '智能手机使用、养生技巧等培训', 5),
('申请护工', 'NURSING', '专业护工上门服务', 6),
('送饭服务', 'MEAL_DELIVERY', '为老人送餐服务', 7),
('社区互助', 'COMMUNITY_HELP', '邻里互助服务', 8),
('紧急报警', 'EMERGENCY', '紧急情况报警', 9);

INSERT INTO equipment (name, type, quantity, available_quantity, description) VALUES
('轮椅', 'MOBILITY', 10, 10, '轻便型轮椅，适合室内使用'),
('助行器', 'MOBILITY', 15, 15, '四脚助行器，稳定性好'),
('拐杖', 'MOBILITY', 20, 20, '铝合金材质，可调节高度'),
('助听器', 'HEARING', 8, 8, '数字助听器，清晰度高'),
('老花镜', 'VISION', 30, 30, '多种度数可选');

INSERT INTO users (username, password, real_name, phone, age, gender, user_type, address) VALUES
('admin', '123456', '系统管理员', '13800138000', 35, 'MALE', 'ADMIN', '系统'),
('oldman001', '123456', '张大爷', '13800138001', 75, 'MALE', 'OLD_MAN', '北京市朝阳区xx小区1号楼101'),
('family001', '123456', '李女士', '13800138002', 45, 'FEMALE', 'FAMILY', '北京市朝阳区xx小区1号楼101'),
('volunteer001', '123456', '王志愿者', '13800138003', 28, 'MALE', 'VOLUNTEER', '北京市朝阳区xx小区2号楼201'),
('staff001', '123456', '社区工作人员', '13800138004', 32, 'FEMALE', 'COMMUNITY_STAFF', '社区服务中心');