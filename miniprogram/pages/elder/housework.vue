<template>
  <view class="housework-container">
    <view class="service-list">
      <view class="service-item" v-for="service in services" :key="service.id" @click="selectService(service)">
        <text class="service-name">{{ service.name }}</text>
        <text class="service-price">{{ service.price }}元/次</text>
      </view>
    </view>
    
    <view class="order-form" v-if="selectedService">
      <view class="form-item">
        <text class="label">服务类型</text>
        <text class="value">{{ selectedService.name }}</text>
      </view>
      <view class="form-item">
        <text class="label">服务时间</text>
        <picker mode="date" v-model="order.serviceTime" @change="onDateChange">
          <view class="picker">
            {{ order.serviceTime }}
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">服务地址</text>
        <input type="text" v-model="order.address" placeholder="请输入服务地址" />
      </view>
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="order.contactPhone" placeholder="请输入联系电话" />
      </view>
      <view class="form-item">
        <text class="label">服务内容</text>
        <textarea v-model="order.serviceContent" placeholder="请输入服务需求详情" style="height: 150rpx;"></textarea>
      </view>
      
      <view class="total-price">
        <text>总计：¥{{ selectedService.price }}</text>
      </view>
      
      <button type="primary" @click="submitOrder">提交订单</button>
    </view>
  </view>
</template>

<script>
import { houseworkService } from '../../utils/api'

export default {
  data() {
    return {
      services: [],
      selectedService: null,
      order: {
        serviceTime: '',
        address: '',
        contactPhone: '',
        serviceContent: ''
      }
    }
  },
  onLoad() {
    this.loadServices()
    // 初始化默认日期为今天
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    this.order.serviceTime = `${year}-${month}-${day}`
  },
  methods: {
    async loadServices() {
      try {
        uni.showLoading({ title: '加载中...' })
        const res = await houseworkService.getServiceTypes()
        if (res.code === 200) {
          this.services = res.data
        } else {
          uni.showToast({ title: '加载服务类型失败', icon: 'none' })
        }
      } catch (error) {
        console.error('加载服务类型失败:', error)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    selectService(service) {
      this.selectedService = service
    },
    onDateChange(e) {
      this.order.serviceTime = e.detail.value
    },
    async submitOrder() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const orderData = {
        serviceTypeId: this.selectedService.id,
        serviceContent: this.order.serviceContent,
        appointmentTime: this.order.serviceTime,
        address: this.order.address,
        contactPhone: this.order.contactPhone
      }
      
      try {
        uni.showLoading({ title: '提交中...' })
        const res = await houseworkService.applyService(orderData)
        if (res.code === 200) {
          uni.showToast({ title: '订单提交成功' })
          this.resetForm()
        } else {
          uni.showToast({ title: res.message || '提交失败', icon: 'none' })
        }
      } catch (error) {
        console.error('提交订单失败:', error)
        uni.showToast({ title: '提交失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    validateForm() {
      return this.order.serviceTime && this.order.address && this.order.contactPhone && this.order.serviceContent
    },
    resetForm() {
      this.selectedService = null
      this.order = {
        serviceTime: '',
        address: '',
        contactPhone: '',
        serviceContent: ''
      }
      // 重新设置默认日期
      const today = new Date()
      const year = today.getFullYear()
      const month = String(today.getMonth() + 1).padStart(2, '0')
      const day = String(today.getDate()).padStart(2, '0')
      this.order.serviceTime = `${year}-${month}-${day}`
    }
  }
}
</script>

<style scoped>
.housework-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.service-list {
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.service-item {
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.service-item:last-child {
  border-bottom: none;
}

.service-name {
  font-size: 32rpx;
  font-weight: bold;
}

.service-price {
  font-size: 28rpx;
  color: #007AFF;
}

.order-form {
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

.value {
  display: block;
  font-size: 32rpx;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 5rpx;
}

input, textarea, .picker {
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