<template>
  <view class="personal-container">
    <!-- 服务功能快捷入口 -->
    <view class="quick-service-section">
      <view class="section-title">服务功能</view>
      <view class="service-buttons">
        <button class="service-btn" @click="goToService('shopping')">代购物资</button>
        <button class="service-btn" @click="goToService('housework')">家政服务</button>
        <button class="service-btn" @click="goToService('equipment')">器具申请</button>
      </view>
    </view>

    <view class="menu-list">
      <view class="menu-item" @click="goToProfile">
        <text class="menu-icon">👤</text>
        <text class="menu-title">个人档案</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="goToService('barrier-free')">
        <text class="menu-icon">♿</text>
        <text class="menu-title">无障碍改造</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="goToService('training')">
        <text class="menu-icon">📚</text>
        <text class="menu-title">培训学习</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="goToService('community')">
        <text class="menu-icon">👥</text>
        <text class="menu-title">社区互助</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @click="goToService('emergency-alert')">
        <text class="menu-icon">🚨</text>
        <text class="menu-title">紧急报警</text>
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
    goToService(serviceType) {
      const routes = {
        'shopping': '/pages/elder/shopping',
        'housework': '/pages/elder/housework',
        'equipment': '/pages/elder/equipment',
        'barrier-free': '/pages/elder/barrier-free',
        'training': '/pages/elder/training',
        'community': '/pages/elder/community',
        'emergency-alert': '/pages/elder/emergency-alert'
      }
      
      if (routes[serviceType]) {
        uni.navigateTo({ url: routes[serviceType] })
      } else {
        uni.showToast({ title: '功能开发中', icon: 'none' })
      }
    },
    
    goToProfile() {
      uni.navigateTo({ url: '/pages/elder/profile' })
    },
    
    goToSettings() {
      uni.showToast({ title: '设置功能开发中', icon: 'none' })
    },
    
    goToHelp() {
      uni.showToast({ title: '帮助中心开发中', icon: 'none' })
    },
    
    goToAbout() {
      uni.showToast({ title: '关于我们开发中', icon: 'none' })
    },
    
    logout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            this.$request.removeToken()
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

/* 服务功能快捷入口 */
.quick-service-section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
}

.service-buttons {
  display: flex;
  gap: 20rpx;
}

.service-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: white;
  border: none;
}

.menu-list {
  background-color: #fff;
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