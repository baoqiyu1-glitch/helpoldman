<template>
  <view class="services-container">
    <view class="header">
      <text class="title">服务列表</text>
    </view>
    
    <view class="services-list">
      <view class="service-section" v-for="category in serviceCategories" :key="category">
        <text class="section-title">{{ getCategoryName(category) }}</text>
        
        <view class="service-item" 
              v-for="service in getServicesByCategory(category)" 
              :key="service.id"
              @click="goToService(service.code)">
          <view class="service-icon">{{ service.icon }}</view>
          <view class="service-info">
            <text class="service-name">{{ service.name }}</text>
            <text class="service-desc">{{ service.description }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import request from '../../utils/requset' // 注意：文件名是requset.js（拼写错误）

export default {
  data() {
    return {
      serviceTypes: [],
      serviceCategories: []
    }
  },
  onLoad() {
    this.getServiceTypes()
  },
  methods: {
    // 获取服务类型列表
    async getServiceTypes() {
      try {
        const result = await request.get('/service-types')
        this.serviceTypes = result || []
        
        // 提取所有唯一的分类
        const categories = [...new Set(this.serviceTypes.map(service => service.category))]
        this.serviceCategories = categories
      } catch (error) {
        console.error('获取服务类型失败:', error)
        uni.showToast({
          title: '获取服务类型失败',
          icon: 'none'
        })
      }
    },
    
    // 根据分类获取服务
    getServicesByCategory(category) {
      return this.serviceTypes.filter(service => service.category === category)
    },
    
    // 获取分类名称
    getCategoryName(category) {
      const categoryMap = {
        'LIFE': '生活服务',
        'HEALTH': '医疗健康',
        'EDUCATION': '学习培训'
      }
      return categoryMap[category] || category
    },
    
    // 根据服务类型跳转到相应页面
    goToService(serviceCode) {
      // 从本地存储获取用户信息
      const userInfo = uni.getStorageSync('userInfo')
      // 获取用户类型，如果没有获取到则默认使用'elder'
      const userType = userInfo ? userInfo.userType : 'elder'
      
      // 根据用户类型和服务代码跳转到相应服务页面
      const serviceMap = {
        'elder': {
          'SHOPPING': '/pages/elder/shopping',
          'HOUSEWORK': '/pages/elder/housework',
          'EMERGENCY': '/pages/elder/emergency-alert',
          'TRAINING': '/pages/elder/training'
        },
        'volunteer': {
          'SHOPPING': '/pages/volunteer/shopping',
          'HOUSEWORK': '/pages/volunteer/housework',
          'TRAINING': '/pages/volunteer/training'
        },
        'family': {
          'SHOPPING': '/pages/family/shopping',
          'HOUSEWORK': '/pages/family/housework',
          'COOKING': '/pages/family/cooking',
          'NURSE': '/pages/family/nurse',
          'LEARN': '/pages/family/learn'
        }
      }
      
      const url = serviceMap[userType] && serviceMap[userType][serviceCode]
      if (url) {
        uni.navigateTo({ url })
      } else {
        uni.showToast({
          title: '该服务暂不可用',
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style scoped>
.services-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  padding: 30rpx;
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
}

.services-list {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 20rpx;
}

.service-section {
  margin-bottom: 40rpx;
}

.service-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 34rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  padding-left: 20rpx;
  color: #333;
  border-left: 4rpx solid #007AFF;
}

.service-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #f9f9f9;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.service-item:last-child {
  margin-bottom: 0;
}

.service-icon {
  font-size: 60rpx;
  margin-right: 30rpx;
}

.service-info {
  flex: 1;
}

.service-name {
  font-size: 32rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
  color: #333;
}

.service-desc {
  font-size: 28rpx;
  color: #666;
}
</style>