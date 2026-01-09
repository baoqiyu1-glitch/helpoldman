<template>
  <view class="meal-container">
    <view class="meal-form">
      <view class="form-item">
        <text class="label">送餐地址</text>
        <input type="text" v-model="orderForm.address" placeholder="请输入详细送餐地址" />
      </view>
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="orderForm.phone" placeholder="请输入联系电话" />
      </view>
      <view class="form-item">
        <text class="label">送餐时间</text>
        <picker mode="date" v-model="orderForm.date" @change="onDateChange">
          <view class="picker">
            日期：{{ orderForm.date }}
          </view>
        </picker>
        <picker mode="time" v-model="orderForm.time" @change="onTimeChange">
          <view class="picker">
            时间：{{ orderForm.time }}
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">餐食类型</text>
        <picker mode="selector" :range="mealTypes" v-model="orderForm.mealTypeIndex" @change="onMealTypeChange">
          <view class="picker">
            {{ mealTypes[orderForm.mealTypeIndex] }}
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">特殊要求</text>
        <textarea v-model="orderForm.requirements" placeholder="请输入特殊饮食要求（如低盐、糖尿病餐等）" style="height: 120rpx;"></textarea>
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
      mealTypes: ['营养餐', '糖尿病餐', '低盐餐', '低脂餐', '普通餐'],
      orderForm: {
        address: '',
        phone: '',
        date: '',
        time: '',
        mealTypeIndex: 0,
        requirements: ''
      },
      mealPrices: {
        0: 25, // 营养餐
        1: 30, // 糖尿病餐
        2: 25, // 低盐餐
        3: 25, // 低脂餐
        4: 20  // 普通餐
      }
    }
  },
  computed: {
    totalPrice() {
      return this.mealPrices[this.orderForm.mealTypeIndex]
    }
  },
  onLoad() {
    // 初始化默认日期和时间
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    const hour = String(today.getHours()).padStart(2, '0')
    const minute = String(today.getMinutes()).padStart(2, '0')
    
    this.orderForm.date = `${year}-${month}-${day}`
    this.orderForm.time = `${hour}:${minute}`
  },
  methods: {
    onDateChange(e) {
      this.orderForm.date = e.detail.value
    },
    onTimeChange(e) {
      this.orderForm.time = e.detail.value
    },
    onMealTypeChange(e) {
      this.orderForm.mealTypeIndex = e.detail.value
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
        time: this.orderForm.time,
        mealType: this.mealTypes[this.orderForm.mealTypeIndex],
        requirements: this.orderForm.requirements,
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
        time: this.orderForm.time, // 保留选择的时间
        mealTypeIndex: 0,
        requirements: ''
      }
    }
  }
}
</script>

<style scoped>
.meal-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.meal-form {
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
  margin-bottom: 15rpx;
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