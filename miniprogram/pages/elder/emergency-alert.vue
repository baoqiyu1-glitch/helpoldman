<template>
  <view class="container">
    <view class="alert-container">
      <view class="alert-btn" @click="triggerAlert">
        <image src="/static/emergency.png" mode="aspectFit"></image>
        <text class="alert-text">紧急报警</text>
      </view>
      <view class="alert-info">
        <text class="tip">点击按钮后，系统将立即通知社区工作人员和您的紧急联系人</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      alertType: 'EMERGENCY',
      description: '需要紧急帮助',
      location: ''
    }
  },
  onLoad() {
    // 获取当前位置
    this.getCurrentLocation()
  },
  methods: {
    getCurrentLocation() {
      uni.getLocation({
        type: 'gcj02',
        success: (res) => {
          const { latitude, longitude } = res
          // 可以调用地图API获取详细地址
          this.location = `${latitude},${longitude}`
        },
        fail: (err) => {
          console.error('获取位置失败', err)
          uni.showToast({
            title: '获取位置失败，请手动设置',
            icon: 'none'
          })
        }
      })
    },
    async triggerAlert() {
      uni.showModal({
        title: '紧急报警',
        content: '确定要触发紧急报警吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await this.$request.post('/emergency-alerts', {
                alertType: this.alertType,
                description: this.description,
                location: this.location
              })
              
              uni.showToast({
                title: '报警已发出，正在等待救援',
                icon: 'success'
              })
            } catch (error) {
              console.error('报警失败', error)
              uni.showToast({
                title: '报警失败，请重试',
                icon: 'none'
              })
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx;
  background-color: #f5f5f5;
  height: 100vh;
}

.alert-container {
  width: 100%;
  max-width: 800rpx;
  background-color: #fff;
  border-radius: 20rpx;
  padding: 60rpx;
  box-shadow: 0 5rpx 20rpx rgba(0, 0, 0, 0.1);
  margin-top: 100rpx;
}

.alert-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 200rpx;
  height: 200rpx;
  background-color: #FF3B30;
  border-radius: 50%;
  margin: 0 auto 40rpx;
  box-shadow: 0 10rpx 30rpx rgba(255, 59, 48, 0.3);
}

.alert-btn image {
  width: 100rpx;
  height: 100rpx;
  margin-bottom: 10rpx;
}

.alert-text {
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}

.alert-info {
  text-align: center;
}

.tip {
  color: #666;
  font-size: 28rpx;
  line-height: 44rpx;
}
</style>
