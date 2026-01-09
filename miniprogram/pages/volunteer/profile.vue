<template>
  <view class="profile-container">
    <view class="profile-header">
      <image class="avatar" src="../../static/images/volunteer-avatar.png" mode="aspectFill"></image>
      <view class="user-info">
        <text class="username">志愿者：{{ userInfo.name }}</text>
        <text class="user-status" :class="{ active: userInfo.status === 'active' }">
          {{ userInfo.status === 'active' ? '已认证' : '待认证' }}
        </text>
      </view>
    </view>
    
    <view class="profile-content">
      <view class="info-section">
        <text class="section-title">基本信息</text>
        
        <view class="info-item">
          <text class="label">姓名</text>
          <text class="value">{{ userInfo.name }}</text>
        </view>
        
        <view class="info-item">
          <text class="label">性别</text>
          <text class="value">{{ userInfo.gender }}</text>
        </view>
        
        <view class="info-item">
          <text class="label">年龄</text>
          <text class="value">{{ userInfo.age }}岁</text>
        </view>
        
        <view class="info-item">
          <text class="label">联系电话</text>
          <text class="value">{{ userInfo.phone }}</text>
        </view>
        
        <view class="info-item">
          <text class="label">邮箱</text>
          <text class="value">{{ userInfo.email }}</text>
        </view>
        
        <view class="info-item">
          <text class="label">居住地址</text>
          <text class="value">{{ userInfo.address }}</text>
        </view>
      </view>
      
      <view class="info-section">
        <text class="section-title">服务信息</text>
        
        <view class="info-item">
          <text class="label">擅长领域</text>
          <text class="value">{{ userInfo.skills.join('、') }}</text>
        </view>
        
        <view class="info-item">
          <text class="label">可服务时间</text>
          <text class="value">{{ userInfo.availableTime }}</text>
        </view>
        
        <view class="info-item">
          <text class="label">服务次数</text>
          <text class="value">{{ userInfo.serviceCount }}</text>
        </view>
        
        <view class="info-item">
          <text class="label">服务时长</text>
          <text class="value">{{ userInfo.serviceHours }}小时</text>
        </view>
        
        <view class="info-item">
          <text class="label">用户评价</text>
          <text class="value">{{ userInfo.rating }}分</text>
        </view>
      </view>
    </view>
    
    <button class="edit-btn" @click="editProfile">编辑资料</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {
        name: '李四',
        gender: '男',
        age: 28,
        phone: '13900139000',
        email: 'lisi@example.com',
        address: '北京市朝阳区XX街道XX小区',
        skills: ['代购', '家务', '教学'],
        availableTime: '周一至周五晚上，周末全天',
        serviceCount: 12,
        serviceHours: 36,
        rating: 4.8,
        status: 'active'
      }
    }
  },
  methods: {
    editProfile() {
      // 跳转到编辑资料页面
      uni.navigateTo({
        url: '/pages/volunteer/edit-profile'
      })
    }
  }
}
</script>

<style scoped>
.profile-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.profile-header {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.avatar {
  width: 150rpx;
  height: 150rpx;
  border-radius: 50%;
  margin-right: 30rpx;
}

.user-info {
  flex: 1;
}

.username {
  font-size: 36rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
}

.user-status {
  font-size: 28rpx;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  background-color: #f0f0f0;
  color: #999;
}

.user-status.active {
  background-color: #e6f7ff;
  color: #007AFF;
}

.profile-content {
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 30rpx;
  overflow: hidden;
}

.info-section {
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.info-section:last-child {
  border-bottom: none;
}

.section-title {
  font-size: 34rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  display: block;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  font-size: 32rpx;
  color: #666;
  min-width: 120rpx;
}

.value {
  font-size: 32rpx;
  color: #333;
  flex: 1;
  text-align: right;
  line-height: 1.5;
}

.edit-btn {
  width: 100%;
  height: 90rpx;
  font-size: 34rpx;
  border-radius: 10rpx;
}
</style>
