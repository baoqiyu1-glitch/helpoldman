<template>
  <view class="personal-container">
    <view class="menu-list">
      <view class="menu-item" @click="goToProfile">
        <text class="menu-icon">👤</text>
        <text class="menu-title">个人档案</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="goToSettings">
        <text class="menu-icon">⚙️</text>
        <text class="menu-title">系统设置</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="goToHelp">
        <text class="menu-icon">❓</text>
        <text class="menu-title">帮助中心</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="goToAbout">
        <text class="menu-icon">ℹ️</text>
        <text class="menu-title">关于我们</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="logout" style="color: #FF3B30;">
        <text class="menu-icon">🚪</text>
        <text class="menu-title">退出登录</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    goToProfile() {
      uni.navigateTo({ url: '/pages/elder/profile' })
    },
    
    goToSettings() {
      // 跳转到设置页面
      uni.showToast({ title: '设置功能开发中', icon: 'none' })
    },
    
    goToHelp() {
      // 跳转到帮助中心
      uni.showToast({ title: '帮助中心开发中', icon: 'none' })
    },
    
    goToAbout() {
      // 跳转到关于我们
      uni.showToast({ title: '关于我们开发中', icon: 'none' })
    },
    
    logout() {
      // 退出登录逻辑
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            // 清除token
            this.$request.removeToken()
            // 跳转到登录页面
            uni.redirectTo({ url: '/pages/auth/login' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.personal-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.menu-list {
  background-color: #fff;
  margin-top: 20rpx;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 48rpx;
  margin-right: 20rpx;
}

.menu-title {
  font-size: 34rpx;
  color: #333;
  flex: 1;
}

.arrow {
  font-size: 36rpx;
  color: #999;
}
</style>
