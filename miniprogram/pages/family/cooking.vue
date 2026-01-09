<template>
  <view class="cooking-container">
    <view class="cooking-form">
      <view class="form-item">
        <text class="label">服务地址</text>
        <input type="text" v-model="orderForm.address" placeholder="请输入详细服务地址" />
      </view>
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="orderForm.phone" placeholder="请输入联系电话" />
      </view>
      <view class="form-item">
        <text class="label">服务时间</text>
        <picker mode="date" v-model="orderForm.date" @change="onDateChange">
          <view class="picker">
            {{ orderForm.date }}
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">服务类型</text>
        <picker mode="selector" :range="serviceTypes" v-model="orderForm.serviceTypeIndex" @change="onServiceTypeChange">
          <view class="picker">
            {{ serviceTypes[orderForm.serviceTypeIndex] }}
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">菜品要求</text>
        <textarea v-model="orderForm.dishRequirements" placeholder="请输入菜品要求（如家常菜、川菜、粤菜等）" style="height: 120rpx;"></textarea>
      </view>
      <view class="form-item">
        <text class="label">特殊要求</text>
        <textarea v-model="orderForm.specialRequirements" placeholder="请输入特殊饮食要求（如低盐、过敏食材等）" style="height: 120rpx;"></textarea>
      </view>
      
      <view class="total-price">
        <text>总计：¥{{ totalPrice }}</text>
      </view>
      
      <button type="primary" @click="submitOrder">提交订单</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      serviceTypes: ['只做饭', '做饭+送饭', '包月做饭'],
      orderForm: {
        address: '',
        phone: '',
        date: '',
        serviceTypeIndex: 0,
        dishRequirements: '',
        specialRequirements: ''
      },
      servicePrices: {
        0: 80, // 只做饭
        1: 100, // 做饭+送饭
        2: 2000 // 包月做饭
      }
    }
  },
  computed: {
    totalPrice() {
      return this.servicePrices[this.orderForm.serviceTypeIndex]
    }
  },
  onLoad() {
    // 初始化默认日期
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    
    this.orderForm.date = `${year}-${month}-${day}`
  },
  methods: {
    onDateChange(e) {
      this.orderForm.date = e.detail.value
    },
    onServiceTypeChange(e) {
      this.orderForm.serviceTypeIndex = e.detail.value
    },
    submitOrder() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const orderData = {
        address: this.orderForm.address,
        phone: this.orderForm.phone,
        date: this.orderForm.date,
        serviceType: this.serviceTypes[this.orderForm.serviceTypeIndex],
        dishRequirements: this.orderForm.dishRequirements,
        specialRequirements: this.orderForm.specialRequirements,
        price: this.totalPrice
      }
      
      uni.showLoading({ title: '提交中...' })
      // 这里替换为实际API调用
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({ title: '订单提交成功' })
        this.resetForm()
      }, 1000)
    },
    validateForm() {
      return this.orderForm.address && this.orderForm.phone
    },
    resetForm() {
      this.orderForm = {
        address: '',
        phone: '',
        date: this.orderForm.date, // 保留选择的日期
        serviceTypeIndex: 0,
        dishRequirements: '',
        specialRequirements: ''
      }
    }
  }
}
</script>

<style scoped>
.cooking-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.cooking-form {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 20rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 10rpx;
}

.picker, input, textarea {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
}

.total-price {
  text-align: right;
  font-size: 36rpx;
  font-weight: bold;
  color: #FF4444;
  margin-bottom: 30rpx;
  padding: 20rpx 0;
}

button {
  width: 100%;
  height: 90rpx;
  font-size: 34rpx;
  border-radius: 10rpx;
}
</style>