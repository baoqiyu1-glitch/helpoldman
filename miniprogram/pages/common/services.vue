<template>
  <view class="services-container">
    <view class="header">
      <text class="title">服务列表</text>
    </view>
    
    <view class="services-list">
      <view class="service-section">
        <text class="section-title">生活服务</text>
        
        <view class="service-item" @click="goToService('shopping')">
          <view class="service-icon">🛒</view>
          <view class="service-info">
            <text class="service-name">代购生活物资</text>
            <text class="service-desc">帮您购买日常生活用品</text>
          </view>
        </view>
        
        <view class="service-item" @click="goToService('housework')">
          <view class="service-icon">🧹</view>
          <view class="service-info">
            <text class="service-name">上门协助家务</text>
            <text class="service-desc">清洁、整理等家务服务</text>
          </view>
        </view>
        
        <view class="service-item" @click="goToService('cooking')">
          <view class="service-icon">🍳</view>
          <view class="service-info">
            <text class="service-name">做饭送饭</text>
            <text class="service-desc">提供家常便饭服务</text>
          </view>
        </view>
      </view>
      
      <view class="service-section">
        <text class="section-title">医疗健康</text>
        
        <view class="service-item" @click="goToService('emergency')">
          <view class="service-icon">🚨</view>
          <view class="service-info">
            <text class="service-name">紧急报警</text>
            <text class="service-desc">一键呼叫紧急救援</text>
          </view>
        </view>
        
        <view class="service-item" @click="goToService('nurse')">
          <view class="service-icon">👩‍⚕️</view>
          <view class="service-info">
            <text class="service-name">护工服务</text>
            <text class="service-desc">专业护理人员服务</text>
          </view>
        </view>
      </view>
      
      <view class="service-section">
        <text class="section-title">学习培训</text>
        
        <view class="service-item" @click="goToService('training')">
          <view class="service-icon">📚</view>
          <view class="service-info">
            <text class="service-name">技能培训</text>
            <text class="service-desc">智能手机、健康知识等培训</text>
          </view>
        </view>
        
        <view class="service-item" @click="goToService('learn')">
          <view class="service-icon">🎓</view>
          <view class="service-info">
            <text class="service-name">我要学习</text>
            <text class="service-desc">各种兴趣爱好学习</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    goToService(serviceType) {
      // 根据用户类型跳转到相应服务页面
      const userType = 'elder' // 假设当前用户是老人
      const serviceMap = {
        shopping: '/pages/elder/shopping',
        housework: '/pages/elder/housework',
        cooking: '/pages/family/cooking',
        emergency: '/pages/elder/emergency-alert',
        nurse: '/pages/family/nurse',
        training: '/pages/elder/training',
        learn: '/pages/family/learn'
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
