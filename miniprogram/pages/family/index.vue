<template>
  <view class="family-index">
    <view class="header">
      <view class="user-info">
        <image class="avatar" :src="userInfo.avatarUrl || '/static/default-avatar.png'" mode="aspectFill"></image>
        <view class="user-details">
          <text class="username">{{ userInfo.username || '家人用户' }}</text>
          <text class="role">家人用户</text>
        </view>
      </view>
      <view class="stats">
        <view class="stat-item">
          <text class="stat-number">{{ caredElders }}</text>
          <text class="stat-label">照顾老人</text>
        </view>
        <view class="stat-item">
          <text class="stat-number">{{ completedServices }}</text>
          <text class="stat-label">完成服务</text>
        </view>
      </view>
    </view>
    
    <view class="quick-actions">
      <view class="section-title">快速服务</view>
      <view class="action-grid">
        <view class="action-item" @click="navigateTo('donation')">
          <image class="action-icon" src="/static/icons/donation.png" mode="aspectFit"></image>
          <text class="action-text">爱心捐赠</text>
        </view>
        <view class="action-item" @click="navigateTo('learn')">
          <image class="action-icon" src="/static/icons/learn.png" mode="aspectFit"></image>
          <text class="action-text">我要学习</text>
        </view>
        <view class="action-item" @click="navigateTo('nurse')">
          <image class="action-icon" src="/static/icons/nurse.png" mode="aspectFit"></image>
          <text class="action-text">申请护工</text>
        </view>
        <view class="action-item" @click="navigateTo('meal')">
          <image class="action-icon" src="/static/icons/meal.png" mode="aspectFit"></image>
          <text class="action-text">送饭服务</text>
        </view>
        <view class="action-item" @click="navigateTo('shopping')">
          <image class="action-icon" src="/static/icons/shopping.png" mode="aspectFit"></image>
          <text class="action-text">代购物资</text>
        </view>
        <view class="action-item" @click="navigateTo('housework')">
          <image class="action-icon" src="/static/icons/housework.png" mode="aspectFit"></image>
          <text class="action-text">协助家务</text>
        </view>
        <view class="action-item" @click="navigateTo('cooking')">
          <image class="action-icon" src="/static/icons/cooking.png" mode="aspectFit"></image>
          <text class="action-text">做饭送饭</text>
        </view>
      </view>
    </view>
    
    <view class="recent-services">
      <view class="section-title">最近服务</view>
      <view class="service-list">
        <view class="service-item" v-for="service in recentServices" :key="service.id">
          <view class="service-info">
            <text class="service-title">{{ service.title }}</text>
            <text class="service-time">{{ service.time }}</text>
          </view>
          <view class="service-status" :class="service.status">{{ service.statusText }}</view>
        </view>
      </view>
    </view>
    
    <!-- 自定义底部导航 -->
    <view class="custom-tabbar">
      <view class="tab-item" :class="{ active: currentTab === 'index' }" @click="switchTab('index')">
        <image class="tab-icon" src="/static/icons/home.png" mode="aspectFit"></image>
        <text class="tab-text">首页</text>
      </view>
      <view class="tab-item" :class="{ active: currentTab === 'services' }" @click="switchTab('services')">
        <image class="tab-icon" src="/static/icons/service.png" mode="aspectFit"></image>
        <text class="tab-text">服务</text>
      </view>
      <view class="tab-item" :class="{ active: currentTab === 'message' }" @click="switchTab('message')">
        <image class="tab-icon" src="/static/icons/message.png" mode="aspectFit"></image>
        <text class="tab-text">消息</text>
      </view>
      <view class="tab-item" :class="{ active: currentTab === 'profile' }" @click="switchTab('profile')">
        <image class="tab-icon" src="/static/icons/profile.png" mode="aspectFit"></image>
        <text class="tab-text">我的</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {},
      caredElders: 0,
      completedServices: 0,
      recentServices: [],
      serviceStats: {},
      currentTab: 'index'
    }
  },
  onLoad() {
    this.loadUserInfo()
    this.loadFamilyData()
  },
  methods: {
    loadUserInfo() {
      const userInfo = uni.getStorageSync('userInfo')
      if (userInfo) {
        this.userInfo = userInfo
        console.log('家人用户信息:', userInfo)
      }
    },
    
    async loadFamilyData() {
      try {
        // 获取照顾的老人数量
        const eldersRes = await this.$request.get('/family/cared-elders')
        if (eldersRes.code === 200) {
          this.caredElders = eldersRes.data.count
        }
        
        // 获取完成的服务数量
        const servicesRes = await this.$request.get('/family/completed-services')
        if (servicesRes.code === 200) {
          this.completedServices = servicesRes.data.count
        }
        
        // 获取最近服务记录
        const recentRes = await this.$request.get('/family/recent-services')
        if (recentRes.code === 200) {
          this.recentServices = recentRes.data
        }
        
        // 获取服务统计
        const statsRes = await this.$request.get('/family/service-stats')
        if (statsRes.code === 200) {
          this.serviceStats = statsRes.data
        }
      } catch (error) {
        console.error('加载家属数据失败:', error)
      }
    },
    
    navigateTo(page) {
      uni.navigateTo({
        url: `/pages/family/${page}`
      })
    },
    
    switchTab(tab) {
      this.currentTab = tab
      switch(tab) {
        case 'index':
          break
        case 'services':
          uni.navigateTo({
            url: '/pages/common/services'
          })
          break
        case 'message':
          uni.navigateTo({
            url: '/pages/common/message'
          })
          break
        case 'profile':
          uni.navigateTo({
            url: '/pages/family/profile'
          })
          break
      }
    }
  }
}
</script>

<style scoped>
.family-index {
  padding: 30rpx 30rpx 130rpx 30rpx; /* 增加底部内边距，避免内容被底部导航遮挡 */
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  background: linear-gradient(135deg, #34C759, #5AC8FA);
  border-radius: 20rpx;
  padding: 40rpx;
  color: white;
  margin-bottom: 30rpx;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.role {
  font-size: 28rpx;
  opacity: 0.9;
}

.stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 48rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.stat-label {
  font-size: 24rpx;
  opacity: 0.9;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
}

.quick-actions {
  background-color: white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx;
  background-color: #f8f8f8;
  border-radius: 15rpx;
}

.action-icon {
  width: 60rpx;
  height: 60rpx;
  margin-bottom: 15rpx;
}

.action-text {
  font-size: 24rpx;
  text-align: center;
}

.recent-services {
  background-color: white;
  border-radius: 20rpx;
  padding: 30rpx;
}

.service-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.service-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: #f8f8f8;
  border-radius: 10rpx;
}

.service-info {
  display: flex;
  flex-direction: column;
}

.service-title {
  font-size: 28rpx;
  font-weight: bold;
  margin-bottom: 5rpx;
}

.service-time {
  font-size: 24rpx;
  color: #666;
}

.service-status {
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
}

.service-status.completed {
  background-color: #34C759;
  color: white;
}

.service-status.in-progress {
  background-color: #FF9500;
  color: white;
}

/* 自定义底部导航样式 */
.custom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #FFFFFF;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1rpx solid #eeeeee;
  z-index: 999;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
}

.tab-item.active {
  color: #34C759;
}

.tab-icon {
  width: 40rpx;
  height: 40rpx;
  margin-bottom: 8rpx;
}

.tab-text {
  font-size: 20rpx;
}
</style>