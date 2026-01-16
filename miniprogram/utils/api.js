const request = require('./requset')

// 护工服务API
const nursingService = {
    // 申请护工服务
    applyService(data) {
        return request.post('/nursing-service/apply', data)
    },

    // 获取我的护工服务申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/nursing-service/my?page=${page}&size=${size}`)
    },

    // 获取所有护工服务申请（管理员）
    getAllApplications(page = 1, size = 10) {
        return request.get(`/nursing-service/all?page=${page}&size=${size}`)
    },

    // 分配护工
    assignNurse(applicationId, nurseId) {
        return request.put(`/nursing-service/assign/${applicationId}`, { nurseId })
    }
}

// 无障碍改造API
const barrierFreeService = {
    // 提交无障碍改造申请
    applyRenovation(data) {
        return request.post('/renovations', data)
    },

    // 获取我的改造申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/renovations/my?page=${page}&size=${size}`)
    },

    // 获取改造类型
    getRenovationTypes() {
        return request.get('/service-types?category=RENOVATION')
    }
}

// 送饭服务API
const mealDeliveryService = {
    // 申请送饭服务
    applyService(data) {
        return request.post('/meal-delivery/apply', data)
    },

    // 获取我的申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/meal-delivery/my?page=${page}&size=${size}`)
    },

    // 获取所有申请（管理员）
    getAllApplications(page = 1, size = 10) {
        return request.get(`/meal-delivery/all?page=${page}&size=${size}`)
    },

    // 更新申请状态
    updateStatus(applicationId, status) {
        return request.put(`/meal-delivery/status/${applicationId}`, { status })
    }
}

// 补贴申请API
const subsidyService = {
    // 提交补贴申请
    applySubsidy(data) {
        return request.post('/subsidy-application/apply', data)
    },

    // 获取我的申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/subsidy-application/my?page=${page}&size=${size}`)
    },

    // 获取所有申请（管理员）
    getAllApplications(page = 1, size = 10) {
        return request.get(`/subsidy-application/all?page=${page}&size=${size}`)
    },

    // 更新申请状态
    updateStatus(applicationId, status) {
        return request.put(`/subsidy-application/status/${applicationId}`, { status })
    }
}

// 捐赠管理API
const donationService = {
    // 提交捐赠申请
    applyDonation(data) {
        return request.post('/donation/apply', data)
    },

    // 获取我的捐赠记录
    getMyDonations(page = 1, size = 10) {
        return request.get(`/donation/my?page=${page}&size=${size}`)
    },

    // 获取所有捐赠记录（管理员）
    getAllDonations(page = 1, size = 10) {
        return request.get(`/donation/all?page=${page}&size=${size}`)
    }
}

// 社区互助API
const communityService = {
    // 发布互助信息
    publishHelp(data) {
        return request.post('/community-help', data)
    },

    // 获取我的互助信息
    getMyHelps(page = 1, size = 10) {
        return request.get(`/community-help/my?page=${page}&size=${size}`)
    },

    // 获取所有互助信息
    getAllHelps(page = 1, size = 10) {
        return request.get(`/community-help/requests?page=${page}&size=${size}`)
    },

    // 获取帮助提供列表
    getHelpOffers(page = 1, size = 10) {
        return request.get(`/community-help/offers?page=${page}&size=${size}`)
    },

    // 响应互助请求
    respondHelp(helpId) {
        return request.put(`/community-help/${helpId}/help`)
    }
}

// 紧急报警API
const emergencyService = {
    // 提交紧急报警
    submitAlert(data) {
        return request.post('/emergency-alerts', data)
    },

    // 获取我的报警记录
    getMyAlerts(page = 1, size = 10) {
        return request.get(`/emergency-alerts/my?page=${page}&size=${size}`)
    },

    // 获取所有报警记录（管理员）
    getAllAlerts(page = 1, size = 10) {
        return request.get(`/emergency-alerts/all?page=${page}&size=${size}`)
    },

    // 获取报警统计
    getAlertStats() {
        return request.get('/emergency-alerts/stats')
    }
}

// 家政服务API（保留这个完整版本，删除下面的简化版本）
const houseworkService = {
    // 获取服务类型
    getServiceTypes() {
        return request.get('/service-types?type=HOUSEWORK')
    },

    // 申请家政服务
    applyService(data) {
        return request.post('/service-orders', {
            ...data,
            serviceTypeId: 2 // 家政服务类型ID（正确）
        })
    },

    // 获取我的家政服务申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/service-orders/my?serviceType=HOUSEWORK&page=${page}&size=${size}`)
    }
}

// 设备租赁API
const equipmentService = {
    // 获取服务类型
    getServiceTypes() {
        return request.get('/service-types?type=EQUIPMENT')
    },

    // 申请设备租赁
    applyService(data) {
        return request.post('/service-orders', {
            ...data,
            serviceTypeId: 3 // 设备租赁类型ID（正确）
        })
    },

    // 获取我的设备租赁申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/service-orders/my?serviceType=EQUIPMENT&page=${page}&size=${size}`)
    }
}

// 培训服务API
const trainingService = {
    // 获取服务类型
    getServiceTypes() {
        return request.get('/service-types?type=TRAINING')
    },

    // 申请培训服务
    applyService(data) {
        return request.post('/service-orders', {
            ...data,
            serviceTypeId: 5 // 修正：培训服务类型ID应为5（之前是4）
        })
    },

    // 获取我的培训服务申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/service-orders/my?serviceType=TRAINING&page=${page}&size=${size}`)
    }
}

// 代购服务API
const shoppingService = {
    // 申请代购服务
    applyService(data) {
        return request.post('/service-orders', {
            serviceTypeId: 1, // 代购服务类型ID（正确）
            serviceContent: data.content || data.description,
            appointmentTime: data.appointmentTime || new Date(),
            address: data.address,
            contactPhone: data.contactPhone
        })
    },

    // 获取我的代购服务申请
    getMyApplications(page = 1, size = 10) {
        return request.get(`/service-orders/my?serviceType=PURCHASE&page=${page}&size=${size}`)
    }
}

// 删除重复的家政服务API定义（第220-230行的简化版本）

// 商品相关API
const productApi = {
    // 获取所有商品分类
    getCategories() {
        return request.get('/product-categories')
    },

    // 获取商品列表
    getProducts(categoryId = null) {
        const params = categoryId ? `?categoryId=${categoryId}` : ''
        return request.get(`/products${params}`)
    },

    // 根据分类获取商品
    getProductsByCategory(categoryId) {
        return request.get(`/products/category/${categoryId}`)
    },

    // 搜索商品
    searchProducts(keyword) {
        return request.get(`/products/search?keyword=${encodeURIComponent(keyword)}`)
    }
}

// 通用服务API
const serviceApi = {
    // 获取所有服务类型
    getServiceTypes() {
        return request.get('/service-types')
    },

    // 获取用户信息
    getUserInfo() {
        return request.get('/auth/current')
    },

    // 获取通知公告
    getNotifications(page = 1, size = 10) {
        return request.get(`/notifications?page=${page}&size=${size}`)
    }
}

// 在api.js中增加家属端服务
const familyService = {
    // 获取家属信息
    getFamilyInfo() {
        return request.get('/family/info')
    },

    // 获取家属照顾的老人
    getFamilyElders() {
        return request.get('/family/elders')
    },

    // 家属申请服务
    applyService(serviceData) {
        return request.post('/family/apply-service', serviceData)
    },

    // 获取服务记录
    getServiceRecords(params) {
        return request.get('/family/service-records', { params })
    }
}

// 导出家属服务
export {
    familyService
}

module.exports = {
    nursingService,
    barrierFreeService,
    mealDeliveryService,
    subsidyService,
    donationService,
    communityService,
    emergencyService,
    houseworkService,
    equipmentService,
    trainingService,
    shoppingService,
    productApi,
    serviceApi
}