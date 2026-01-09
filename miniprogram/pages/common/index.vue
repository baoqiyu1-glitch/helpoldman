<template>
  <view class="index-container">
    <view class="header">
      <text class="title">助老助残服务平台</text>
    </view>
    
    <view class="service-grid">
      <view class="service-item" @click="goToService('shopping')">
        <view class="service-icon">🛒</view>
        <text class="service-name">代购生活物资</text>
      </view>
      
      <view class="service-item" @click="goToService('housework')">
        <view class="service-icon">🧹</view>
        <text class="service-name">上门协助家务</text>
      </view>
      
      <view class="service-item" @click="goToService('emergency')">
        <view class="service-icon">🚨</view>
        <text class="service-name">紧急报警</text>
      </view>
      
      <view class="service-item" @click="goToService('training')">
        <view class="service-icon">📚</view>
        <text class="service-name">技能培训</text>
      </view>
    </view>
    
    <view class="news-section">
      <text class="section-title">最新通知</text>
      <view class="news-list">
        <view class="news-item" v-for="(item, index) in news" :key="index">
          <text class="news-title">{{ item.title }}</text>
          <text class="news-time">{{ item.time }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      news: [
        { title: '新的志愿者已加入平台', time: '2023-10-15' },
        { title: '本周将开展健康讲座', time: '2023-10-14' },
        { title: '系统更新维护通知', time: '2023-10-13' }
      ]
    }
  },
  methods: {
    goToService(serviceType) {
      // 根据用户类型跳转到相应服务页面
      const userType = 'elder' // 假设当前用户是老人
      const serviceMap = {
        shopping: '/pages/elder/shopping',
        housework: '/pages/elder/housework',
        emergency: '/pages/elder/emergency-alert',
        training: '/pages/elder/training'
      }
      
      const url = serviceMap[serviceType]
      if (url) {
        uni.navigateTo({ url })
      }
    }
  }
}
</script>

<style scoped>
.index-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  background-color: #007AFF;
  color: #fff;
  padding: 30rpx;
  border-radius: 10rpx;
  margin-bottom: 30rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  display: block;
  text-align: center;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.service-item {
  background-color: #fff;
  padding: 40rpx;
  border-radius: 10rpx;
  text-align: center;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.service-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.service-name {
  font-size: 32rpx;
  color: #333;
}

.news-section {
  background-color: #fff;
  padding: 30rpx;
  border-radius: 10rpx;
}

.section-title {
  font-size: 34rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  display: block;
}

.news-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.news-title {
  font-size: 32rpx;
  color: #333;
  flex: 1;
}

.news-time {
  font-size: 28rpx;
  color: #999;
}
</style>
