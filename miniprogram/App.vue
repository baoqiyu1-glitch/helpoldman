<template>
  <div>
    <router-view />
  </div>
</template>

<script>
export default {
  onLaunch() {
    // 初始化登录状态
    const token = uni.getStorageSync('token')
    if (token) {
      this.$request.setToken(token)
    }
  },
  onShow() {
    console.log('App Show')
  },
  onHide() {
    console.log('App Hide')
  }
}
</script>

<style>
/* 全局样式 */
* {
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f5f5f5;
}

.container {
  padding: 20rpx;
}

.card {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.btn-primary {
  background-color: #007AFF;
  color: #fff;
  border-radius: 50rpx;
  padding: 20rpx;
  font-size: 32rpx;
}
</style>

// 在pages数组中增加家属端页面路由
pages: [
  // ... 其他页面
  {
    "path": "pages/family/index",
    "style": {
      "navigationBarTitleText": "家属首页"
    }
  },
  {
    "path": "pages/family/donation",
    "style": {
      "navigationBarTitleText": "爱心捐赠"
    }
  },
  // ... 其他家属端页面
]