<template>
  <view class="container">
    <view class="login-form">
      <view class="logo">
        <image src="/static/logo.png" mode="aspectFit"></image>
      </view>
      <view class="title">助老助残系统</view>
      <view class="form-item">
        <input type="text" v-model="username" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <input type="password" v-model="password" placeholder="请输入密码" />
      </view>
      <button class="btn-primary" @click="login">登录</button>
      <view class="register-link">
        <text>还没有账号？</text>
        <navigator url="/pages/auth/register">立即注册</navigator>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    async login() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }

      try {
        const response = await this.$request.post('/auth/login', {
          username: this.username,
          password: this.password
        })
        
        // 修复：正确解析响应结构
        console.log('登录接口完整响应:', response)
        
        if (response && response.code === 200) {
          // 保存token和用户信息
          this.$request.setToken(response.data.token)
          uni.setStorageSync('userInfo', response.data.user)
          
          // 调试信息：打印用户类型
          console.log('登录用户信息:', response.data.user)
          console.log('用户类型:', response.data.user.userType)
          
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          
          // 根据用户类型跳转到不同页面
          this.redirectByUserType(response.data.user.userType)
        } else {
          // 处理登录失败的情况
          uni.showToast({
            title: response.message || '登录失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('登录失败', error)
        uni.showToast({
          title: '登录失败，请检查用户名和密码',
          icon: 'none'
        })
      }
    },
    
    redirectByUserType(userType) {
      console.log('根据用户类型跳转:', userType)
      
      // 根据用户类型跳转到不同页面
      switch(userType) {
        case 'volunteer':
          // 志愿者跳转到志愿者首页（使用reLaunch关闭所有页面并打开新页面）
          uni.reLaunch({
            url: '/pages/volunteer/index'
          })
          break
        case 'elder':
          // 老人用户跳转到老人首页（使用tabBar页面）
          uni.switchTab({
            url: '/pages/elder/personal'
          })
          break
        case 'family':
          // 家人用户跳转到家人首页（使用reLaunch关闭所有页面并打开新页面）
          uni.reLaunch({
            url: '/pages/family/index'
          })
          break
        case 'admin':
          // 管理员跳转到管理员首页（需要创建）
          uni.switchTab({
            url: '/pages/common/index'
          })
          break
        default:
          // 默认跳转到通用首页
          uni.switchTab({
            url: '/pages/common/index'
          })
      }
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  width: 90%;
  max-width: 600rpx;
  background-color: #fff;
  padding: 60rpx;
  border-radius: 20rpx;
  box-shadow: 0 5rpx 20rpx rgba(0, 0, 0, 0.1);
}

.logo {
  width: 160rpx;
  height: 160rpx;
  margin: 0 auto 40rpx;
}

.logo image {
  width: 100%;
  height: 100%;
}

.title {
  font-size: 48rpx;
  font-weight: bold;
  text-align: center;
  margin-bottom: 60rpx;
  color: #333;
}

.form-item {
  margin-bottom: 40rpx;
}

.form-item input {
  width: 100%;
  padding: 24rpx;
  border: 2rpx solid #eee;
  border-radius: 10rpx;
  font-size: 32rpx;
}

.btn-primary {
  background-color: #007AFF;
  color: #fff;
  border-radius: 10rpx;
  padding: 24rpx;
  font-size: 36rpx;
  margin-bottom: 30rpx;
}

.register-link {
  text-align: center;
  font-size: 28rpx;
  color: #666;
}

.register-link navigator {
  color: #007AFF;
  margin-left: 10rpx;
}
</style>