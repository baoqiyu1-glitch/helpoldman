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


-- 在数据库中执行以下SQL创建消息表
CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(100) NOT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `type` varchar(20) NOT NULL COMMENT '消息类型',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `icon` varchar(20) DEFAULT NULL COMMENT '消息图标',
  `desc` varchar(200) DEFAULT NULL COMMENT '消息简短描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';
-- 在现有数据库文件末尾添加缺失的表结构

-- 社区互助表
CREATE TABLE community_help (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '发布者ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description TEXT COMMENT '详细描述',
    help_type ENUM('REQUEST', 'OFFER') DEFAULT 'REQUEST' COMMENT '类型：REQUEST-求助, OFFER-提供帮助',
    skill VARCHAR(100) COMMENT '技能类型',
    available_time VARCHAR(200) COMMENT '可提供帮助时间',
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING' COMMENT '状态',
    helper_id BIGINT COMMENT '帮助者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (helper_id) REFERENCES users(id)
);

-- 无障碍改造申请表
CREATE TABLE barrier_free_renovation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '申请人ID',
    renovation_type VARCHAR(100) NOT NULL COMMENT '改造类型',
    address VARCHAR(200) NOT NULL COMMENT '改造地址',
    phone VARCHAR(20) COMMENT '联系电话',
    description TEXT COMMENT '改造需求描述',
    status ENUM('PENDING', 'APPROVED', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING' COMMENT '状态',
    images VARCHAR(500) COMMENT '现场照片URL，多个用逗号分隔',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 护工服务表
CREATE TABLE nursing_services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '申请人ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    service_type VARCHAR(100) NOT NULL COMMENT '服务类型',
    service_time DATETIME NOT NULL COMMENT '服务时间',
    address VARCHAR(200) NOT NULL COMMENT '服务地址',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    requirements TEXT COMMENT '特殊要求',
    status ENUM('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING' COMMENT '状态',
    nurse_id BIGINT COMMENT '护工ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (elder_id) REFERENCES users(id),
    FOREIGN KEY (nurse_id) REFERENCES users(id)
);

-- 送饭服务表
CREATE TABLE meal_delivery (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '申请人ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    meal_type VARCHAR(50) NOT NULL COMMENT '餐食类型',
    delivery_time DATETIME NOT NULL COMMENT '送餐时间',
    address VARCHAR(200) NOT NULL COMMENT '送餐地址',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    special_requirements TEXT COMMENT '特殊要求',
    status ENUM('PENDING', 'PREPARING', 'DELIVERING', 'COMPLETED') DEFAULT 'PENDING' COMMENT '状态',
    deliverer_id BIGINT COMMENT '送餐员ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (elder_id) REFERENCES users(id),
    FOREIGN KEY (deliverer_id) REFERENCES users(id)
);

-- 补助申请表
CREATE TABLE subsidy_applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '申请人ID',
    subsidy_type VARCHAR(100) NOT NULL COMMENT '补助类型',
    applied_amount DECIMAL(10,2) NOT NULL COMMENT '申请金额',
    application_reason TEXT NOT NULL COMMENT '申请理由',
    supporting_documents VARCHAR(500) COMMENT '证明材料URL',
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING' COMMENT '状态',
    review_comments TEXT COMMENT '审核意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 新闻表
CREATE TABLE IF NOT EXISTS news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '新闻标题',
    content TEXT COMMENT '新闻内容',
    author VARCHAR(100) DEFAULT '系统管理员' COMMENT '发布者',
    news_type VARCHAR(50) DEFAULT 'SYSTEM' COMMENT '新闻类型：SYSTEM-系统通知, ACTIVITY-活动通知, UPDATE-更新通知',
    image_url VARCHAR(500) COMMENT '新闻图片',
    status VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT '状态：DRAFT-草稿, PUBLISHED-已发布',
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入测试新闻数据
INSERT INTO news (title, content, news_type, image_url) VALUES
('新的志愿者已加入平台', '热烈欢迎10名新志愿者加入我们的助老助残平台，他们将为大家提供更优质的服务。', 'SYSTEM', '/static/images/news1.jpg'),
('系统更新维护通知', '平台将于明天凌晨2-4点进行系统维护，期间可能无法正常访问，敬请谅解。', 'UPDATE', '/static/images/news2.jpg'),
('社区活动通知', '本周六下午2点在社区活动中心举办老年人智能手机使用培训，欢迎参加。', 'ACTIVITY', '/static/images/news3.jpg'),
('服务优化公告', '我们对代购服务进行了优化，现在支持更多商品种类和更快的配送服务。', 'SYSTEM', '/static/images/news4.jpg');


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

-- 插入一些测试数据
INSERT INTO `messages` (`user_id`, `title`, `content`, `type`, `is_read`, `icon`, `desc`) VALUES
(1, '系统通知', '您的服务申请已通过审核', 'SYSTEM', 0, '📢', '您的服务申请已通过审核'),
(1, '志愿者消息', '李志愿者已接受您的帮助请求', 'VOLUNTEER', 0, '👥', '李志愿者已接受您的帮助请求'),
(1, '培训提醒', '明天下午2点有智能手机培训', 'TRAINING', 1, '📅', '明天下午2点有智能手机培训'),
(1, '系统通知', '平台将于明天凌晨2-4点进行维护', 'SYSTEM', 1, '📢', '平台将于明天凌晨2-4点进行维护');

-- 商品分类表
CREATE TABLE product_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '分类代码',
    description TEXT COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-激活, INACTIVE-未激活',
    icon VARCHAR(200) COMMENT '图标URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 商品表
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '商品代码',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    image_url VARCHAR(500) COMMENT '商品图片URL',
    stock INT DEFAULT 0 COMMENT '库存数量',
    unit VARCHAR(20) DEFAULT '个' COMMENT '单位',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-激活, INACTIVE-未激活',
    brand VARCHAR(100) COMMENT '品牌',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES product_categories(id)
);

-- 插入商品分类基础数据
INSERT INTO product_categories (name, code, description, sort_order, icon) VALUES
('食品', 'FOOD', '各类食品和饮料', 1, '/static/images/food.png'),
('日用品', 'DAILY_USE', '日常生活用品', 2, '/static/images/daily.png'),
('药品', 'MEDICINE', '常用药品和保健品', 3, '/static/images/medicine.png'),
('其他', 'OTHER', '其他商品', 4, '/static/images/other.png');

-- 插入商品基础数据
INSERT INTO products (name, code, description, price, category_id, image_url, stock, unit, brand) VALUES
('大米', 'RICE', '优质东北大米5kg装', 68.00, 1, '/static/images/rice.png', 100, '袋', '金龙鱼'),
('面粉', 'FLOUR', '高筋面粉5kg装', 30.00, 1, '/static/images/flour.png', 80, '袋', '五得利'),
('食用油', 'OIL', '纯正花生油5L装', 80.00, 1, '/static/images/oil.png', 60, '桶', '鲁花'),
('洗发水', 'SHAMPOO', '去屑止痒洗发水500ml', 45.00, 2, '/static/images/shampoo.png', 120, '瓶', '海飞丝'),
('牙膏', 'TOOTHPASTE', '防蛀美白牙膏180g', 15.00, 2, '/static/images/toothpaste.png', 200, '支', '佳洁士'),
('感冒药', 'COLD_MEDICINE', '感冒清热颗粒', 25.00, 3, '/static/images/cold-medicine.png', 50, '盒', '同仁堂'),
('血压计', 'BLOOD_PRESSURE_MONITOR', '电子血压计', 199.00, 4, '/static/images/blood-pressure.png', 30, '台', '欧姆龙');