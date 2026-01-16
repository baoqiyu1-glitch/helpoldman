<template>
  <view class="register-container">
    <view class="register-form">
      <text class="title">用户注册</text>
      
      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input" placeholder="请输入用户名" v-model="form.username" />
      </view>
      
      <view class="form-item">
        <text class="label">密码</text>
        <input class="input" type="password" placeholder="请输入密码" v-model="form.password" />
      </view>
      
      <view class="form-item">
        <text class="label">确认密码</text>
        <input class="input" type="password" placeholder="请确认密码" v-model="form.confirmPassword" />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input class="input" placeholder="请输入手机号" v-model="form.phone" />
      </view>
      
      <view class="form-item">
        <text class="label">用户类型</text>
        <view class="radio-group">
          <label class="radio-item">
            <radio value="elder" :checked="form.userType === 'elder'" @click="form.userType = 'elder'" />
            <text>老人用户</text>
          </label>
          <label class="radio-item">
            <radio value="volunteer" :checked="form.userType === 'volunteer'" @click="form.userType = 'volunteer'" />
            <text>志愿者</text>
          </label>
          <label class="radio-item">
            <radio value="family" :checked="form.userType === 'family'" @click="form.userType = 'family'" />
            <text>家人用户</text>
          </label>
        </view>
      </view>
      
      <button class="register-btn" @click="register">注册</button>
      
      <view class="login-link">
        <text>已有账号？</text>
        <text class="link" @click="goToLogin">立即登录</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        phone: '',
        userType: 'elder' // 默认选择老人用户
      }
    }
  },
  methods: {
    register() {
      // 验证表单
      if (!this.form.username) {
        uni.showToast({ title: '请输入用户名', icon: 'none' })
        return
      }
      if (!this.form.password) {
        uni.showToast({ title: '请输入密码', icon: 'none' })
        return
      }
      if (this.form.password !== this.form.confirmPassword) {
        uni.showToast({ title: '两次密码输入不一致', icon: 'none' })
        return
      }
      if (!this.form.userType) {
        uni.showToast({ title: '请选择用户类型', icon: 'none' })
        return
      }
      
      console.log('注册数据:', this.form) // 调试信息
      
      // 调用注册接口
      uni.request({
        url: 'http://localhost:8080/api/auth/register',
        method: 'POST',
        data: this.form,
        header: {
          'Content-Type': 'application/json'
        },
        success: (res) => {
          console.log('注册响应:', res.data)
          if (res.data.code === 200) {
            uni.showToast({ title: '注册成功' })
            setTimeout(() => {
              uni.navigateTo({ url: '/pages/auth/login' })
            }, 1500)
          } else {
            uni.showToast({ title: res.data.message || '注册失败', icon: 'none' })
          }
        },
        fail: (err) => {
          console.error('注册失败:', err)
          uni.showToast({ title: '网络错误，请重试', icon: 'none' })
        }
      })
    },
    goToLogin() {
      uni.navigateTo({ url: '/pages/auth/login' })
    }
  }
}
</script>

<style scoped>
.register-container {
  padding: 40rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.register-form {
  background-color: #fff;
  padding: 40rpx;
  border-radius: 10rpx;
}

.title {
  font-size: 40rpx;
  font-weight: bold;
  text-align: center;
  display: block;
  margin-bottom: 40rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  display: block;
  font-size: 32rpx;
  margin-bottom: 10rpx;
  color: #333;
}

.input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #eee;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 32rpx;
}

.radio-group {
  display: flex;
  gap: 40rpx;
  padding: 20rpx 0;
}

.radio-item {
  display: flex;
  align-items: center;
  gap: 10rpx;
  font-size: 32rpx;
}

.register-btn {
  width: 100%;
  height: 90rpx;
  font-size: 34rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 10rpx;
  margin: 40rpx 0;
}

.login-link {
  text-align: center;
  font-size: 30rpx;
  color: #666;
}

.link {
  color: #007AFF;
  margin-left: 10rpx;
}
</style>