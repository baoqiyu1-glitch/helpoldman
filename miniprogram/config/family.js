// 家属端配置
export const familyConfig = {
    // 服务类型配置
    serviceTypes: {
        DONATION: 1, // 爱心捐赠
        LEARNING: 2, // 我要学习
        NURSING: 3, // 申请护工
        MEAL_DELIVERY: 4, // 送饭服务
        SHOPPING: 5, // 代购物资
        HOUSEWORK: 6, // 协助家务
        COOKING: 7 // 做饭送饭
    },

    // 服务状态
    serviceStatus: {
        PENDING: 'pending',
        IN_PROGRESS: 'in-progress',
        COMPLETED: 'completed',
        CANCELLED: 'cancelled'
    },

    // 默认配置
    defaultSettings: {
        pageSize: 10,
        maxElders: 5
    }
}