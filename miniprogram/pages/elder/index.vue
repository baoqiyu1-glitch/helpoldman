<template>
  <view class="elder-index">
    <!-- 顶部用户信息 -->
    <view class="user-info">
      <view class="avatar-section">
        <image class="avatar" :src="userInfo.avatar || '/static/images/default-avatar.png'" mode="aspectFill"></image>
        <view class="user-details">
          <text class="username">{{ userInfo.name || '老年用户' }}</text>
          <text class="welcome">欢迎使用助老服务平台</text>
        </view>
      </view>
    </view>

    <!-- 服务功能网格 -->
    <view class="service-grid">
      <view class="section-title">常用服务</view>
      <view class="grid-container">
        <!-- 代购生活物资 -->
        <view class="service-item" @click="goToService('shopping')">
          <view class="service-icon">🛒</view>
          <text class="service-name">代购生活物资</text>
          <text class="service-desc">志愿者帮您代购日常用品</text>
        </view>

        <!-- 上门协助家务 -->
        <view class="service-item" @click="goToService('housework')">
          <view class="service-icon">🏠</view>
          <text class="service-name">上门协助家务</text>
          <text class="service-desc">打扫卫生、洗衣做饭等</text>
        </view>

        <!-- 辅助器具申请 -->
        <view class="service-item" @click="goToService('equipment')">
          <view class="service-icon">🦽</view>
          <text class="service-name">辅助器具申请</text>
          <text class="service-desc">轮椅、助行器等设备</text>
        </view>

        <!-- 无障碍改造 -->
        <view class="service-item" @click="goToService('barrier-free')">
          <view class="service-icon">♿</view>
          <text class="service-name">无障碍改造</text>
          <text class="service-desc">居家环境适老化改造</text>
        </view>

        <!-- 培训学习 -->
        <view class="service-item" @click="goToService('training')">
          <view class="service-icon">📚</view>
          <text class="service-name">培训学习</text>
          <text class="service-desc">智能手机使用等培训</text>
        </view>

        <!-- 社区互助 -->
        <view class="service-item" @click="goToService('community')">
          <view class="service-icon">👥</view>
          <text class="service-name">社区互助</text>
          <text class="service-desc">邻里互助、活动参与</text>
        </view>

        <!-- 紧急报警 -->
        <view class="service-item emergency" @click="goToService('emergency-alert')">
          <view class="service-icon">🚨</view>
          <text class="service-name">紧急报警</text>
          <text class="service-desc">一键求助，快速响应</text>
        </view>

        <!-- 个人中心 -->
        <view class="service-item" @click="goToService('personal')">
          <view class="service-icon">👤</view>
          <text class="service-name">个人中心</text>
          <text class="service-desc">档案管理、设置等</text>
        </view>
      </view>
    </view>

    <!-- 快速操作 -->
    <view class="quick-actions">
      <view class="section-title">快速操作</view>
      <view class="action-buttons">
        <button class="action-btn primary" @click="goToService('shopping')">发布代购需求</button>
        <button class="action-btn" @click="goToService('housework')">申请家政服务</button>
      </view>
    </view>

    <!-- 通知公告 -->
    <view class="notifications">
      <view class="section-title">最新通知</view>
      <view class="notification-list">
        <view class="notification-item" v-for="notification in notifications" :key="notification.id">
          <text class="notification-title">{{ notification.title }}</text>
          <text class="notification-time">{{ notification.createTime }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { serviceApi } from '../../utils/api'

export default {
  data() {
    return {
      userInfo: {
        name: '',
        avatar: ''
      },
      notifications: []
    }
  },
  onLoad() {
    this.loadUserInfo()
    this.loadNotifications()
  },
  onShow() {
    // 页面显示时刷新用户信息
    this.loadUserInfo()
  },
  methods: {
    async loadUserInfo() {
      try {
        // 从本地存储获取用户信息
        const userInfo = uni.getStorageSync('userInfo')
        if (userInfo) {
          this.userInfo = userInfo
        } else {
          // 如果本地存储没有，尝试从API获取
          await this.getUserInfoFromAPI()
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    },
    
    async getUserInfoFromAPI() {
      try {
        // 这里需要添加获取用户信息的API调用
        // 暂时使用本地存储中的信息
        const token = uni.getStorageSync('token')
        if (token) {
          // 如果有token，可以调用API获取用户信息
          // const res = await this.$request.get('/user/profile')
          // if (res.code === 200) {
          //   this.userInfo = res.data
          //   uni.setStorageSync('userInfo', res.data)
          // }
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    },
    
    async loadNotifications() {
      try {
        // 这里需要添加获取通知公告的API调用
        // 暂时使用模拟数据
        this.notifications = [
          { id: 1, title: '平台服务优化通知', createTime: '2024-01-15' },
          { id: 2, title: '社区活动预告', createTime: '2024-01-10' },
          { id: 3, title: '春节服务安排', createTime: '2024-01-05' }
        ]
        
        // 实际API调用示例：
        // const res = await this.$request.get('/notifications')
        // if (res.code === 200) {
        //   this.notifications = res.data
        // }
      } catch (error) {
        console.error('加载通知失败:', error)
      }
    },
    
    goToService(serviceType) {
      const routes = {
        'shopping': '/pages/elder/shopping',
        'housework': '/pages/elder/housework',
        'equipment': '/pages/elder/equipment',
        'barrier-free': '/pages/elder/barrier-free',
        'training': '/pages/elder/training',
        'community': '/pages/elder/community',
        'emergency-alert': '/pages/elder/emergency-alert',
        'personal': '/pages/elder/personal',
        'profile': '/pages/elder/profile'
      }
      
      if (routes[serviceType]) {
        uni.navigateTo({ url: routes[serviceType] })
      } else {
        uni.showToast({ title: '功能开发中', icon: 'none' })
      }
    }
  }
}
</script>

<style scoped>
.elder-index {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20rpx;
}

/* 用户信息区域 */
.user-info {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 30rpx;
  color: white;
}

.avatar-section {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
  margin-right: 30rpx;
}

.user-details {
  flex: 1;
}

.username {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.welcome {
  display: block;
  font-size: 28rpx;
  opacity: 0.9;
}

/* 服务功能网格 */
.service-grid {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
  color: #333;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.service-item {
  background-color: #f8f9fa;
  border-radius: 15rpx;
  padding: 30rpx 20rpx;
  text-align: center;
  transition: all 0.3s ease;
}

.service-item:active {
  background-color: #e9ecef;
  transform: scale(0.98);
}

.service-item.emergency {
  background-color: #fff5f5;
  border: 2rpx solid #ff4444;
}

.service-icon {
  font-size: 60rpx;
  margin-bottom: 15rpx;
  display: block;
}

.service-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.service-desc {
  display: block;
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

/* 快速操作 */
.quick-actions {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.action-buttons {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
  background-color: #f8f9fa;
  color: #333;
}

.action-btn.primary {
  background-color: #007AFF;
  color: white;
}

/* 通知公告 */
.notifications {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
}

.notification-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-title {
  font-size: 28rpx;
  color: #333;
  flex: 1;
}

.notification-time {
  font-size: 24rpx;
  color: #999;
}
</style>