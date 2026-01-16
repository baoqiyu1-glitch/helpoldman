<template>
  <view class="profile-container">
    <view class="profile-header">
      <image class="avatar" :src="userInfo.avatarUrl || '../../static/images/avatar.png'" mode="aspectFill"></image>
      <view class="user-info">
        <text class="username">{{ userInfo.realName || userInfo.username }}</text>
        <text class="user-type">老人用户</text>
      </view>
    </view>
    
    <view class="profile-content">
      <view class="info-item">
        <text class="label">姓名</text>
        <text class="value">{{ userInfo.realName || userInfo.username }}</text>
      </view>
      <view class="info-item">
        <text class="label">年龄</text>
        <text class="value">{{ userInfo.age ? userInfo.age + '岁' : '' }}</text>
      </view>
      <view class="info-item">
        <text class="label">性别</text>
        <text class="value">{{ userInfo.gender }}</text>
      </view>
      <view class="info-item">
        <text class="label">联系电话</text>
        <text class="value">{{ userInfo.phone }}</text>
      </view>
      <view class="info-item">
        <text class="label">家庭地址</text>
        <text class="value">{{ userInfo.address }}</text>
      </view>
      <view class="info-item">
        <text class="label">紧急联系人</text>
        <text class="value">{{ userInfo.emergencyContact }}</text>
      </view>
      <view class="info-item">
        <text class="label">紧急联系电话</text>
        <text class="value">{{ userInfo.emergencyPhone }}</text>
      </view>
    </view>
    
    <view class="action-btn">
      <button type="primary" @click="editProfile">编辑档案</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {
        username: '',
        realName: '',
        age: '',
        gender: '',
        phone: '',
        address: '',
        emergencyContact: '',
        emergencyPhone: '',
        avatarUrl: ''
      }
    }
  },
  onLoad() {
    this.loadProfile()
  },
  methods: {
    // 加载个人档案数据
    async loadProfile() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        if (userInfo) {
          this.userInfo = userInfo
          
          // 从接口获取最新用户信息
          const result = await this.$request.get('/auth/current')
          if (result) {
            this.userInfo = result
            // 更新本地存储
            uni.setStorageSync('userInfo', result)
          }
        }
      } catch (error) {
        console.error('获取个人信息失败:', error)
        uni.showToast({
          title: '获取个人信息失败',
          icon: 'none'
        })
      }
    },
    editProfile() {
      // 跳转到编辑档案页面
      uni.navigateTo({
        url: '/pages/elder/personal'
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

.user-type {
  font-size: 28rpx;
  color: #666;
}

.profile-content {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 0 30rpx;
  margin-bottom: 30rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #eee;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  font-size: 32rpx;
  color: #333;
}

.value {
  font-size: 32rpx;
  color: #666;
  text-align: right;
}

.action-btn {
  padding: 0 20rpx;
}

.action-btn button {
  width: 100%;
  height: 90rpx;
  font-size: 34rpx;
  border-radius: 10rpx;
}
</style>