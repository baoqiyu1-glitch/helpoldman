<template>
  <view class="housework-container">
    <!-- 老人选择 -->
    <view class="elder-selection" v-if="elders.length > 0">
      <text class="section-title">选择老人</text>
      <view class="elder-list">
        <view class="elder-item" 
              v-for="elder in elders" 
              :key="elder.id"
              :class="{ active: selectedElder && selectedElder.id === elder.id }"
              @click="selectElder(elder)">
          <text class="elder-name">{{ elder.name }}</text>
          <text class="elder-relation">{{ elder.relation }}</text>
        </view>
      </view>
    </view>

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
        <picker mode="date" v-model="serviceData.serviceTime" @change="onDateChange">
          <view class="picker">
            {{ serviceData.serviceTime || '请选择服务日期' }}
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">服务时长（小时）</text>
        <input type="number" v-model="serviceData.duration" placeholder="请输入服务时长" />
      </view>
      
      <view class="form-item">
        <text class="label">服务地址</text>
        <input type="text" v-model="serviceData.address" placeholder="请输入服务地址" />
      </view>
      
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="serviceData.phone" placeholder="请输入联系电话" />
      </view>
      
      <view class="form-item">
        <text class="label">特殊要求</text>
        <textarea v-model="serviceData.specialRequirements" placeholder="请输入特殊要求（可选）" style="height: 120rpx;"></textarea>
      </view>
      
      <view class="total-price">
        <text>总计：¥{{ selectedService.price * (serviceData.duration || 1) }}</text>
      </view>
      
      <button type="primary" @click="submitApplication">提交申请</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      elders: [],
      selectedElder: null,
      services: [],
      selectedService: null,
      serviceData: {
        elderId: '',
        serviceType: '',
        serviceTime: '',
        duration: 1,
        address: '',
        phone: '',
        specialRequirements: ''
      }
    }
  },
  onLoad() {
    this.loadElders()
    this.loadServices()
    // 初始化默认日期为今天
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    this.serviceData.serviceTime = `${year}-${month}-${day}`
  },
  methods: {
    async loadElders() {
      try {
        const res = await this.$request.get('/family/elders')
        if (res.code === 200) {
          this.elders = res.data
          if (this.elders.length > 0) {
            this.selectElder(this.elders[0])
          }
        }
      } catch (error) {
        console.error('加载老人列表失败:', error)
      }
    },
    
    async loadServices() {
      try {
        const res = await this.$request.get('/housework/services')
        if (res.code === 200) {
          this.services = res.data
        }
      } catch (error) {
        console.error('加载家务服务列表失败:', error)
      }
    },
    
    selectElder(elder) {
      this.selectedElder = elder
      this.serviceData.elderId = elder.id
      // 设置默认地址和电话
      this.serviceData.address = elder.address || ''
      this.serviceData.phone = elder.phone || ''
    },
    
    selectService(service) {
      this.selectedService = service
      this.serviceData.serviceType = service.name
    },
    
    onDateChange(e) {
      this.serviceData.serviceTime = e.detail.value
    },
    
    async submitApplication() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const applicationData = {
        ...this.serviceData,
        serviceId: this.selectedService.id,
        totalAmount: this.selectedService.price * (this.serviceData.duration || 1)
      }
      
      try {
        uni.showLoading({ title: '提交申请中...' })
        const res = await this.$request.post('/family/housework-apply', applicationData)
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({ title: '申请提交成功' })
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({ title: res.message || '申请失败', icon: 'none' })
        }
      } catch (error) {
        uni.hideLoading()
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
      }
    },
    
    validateForm() {
      return this.serviceData.elderId && 
             this.serviceData.serviceType && 
             this.serviceData.serviceTime && 
             this.serviceData.duration && 
             this.serviceData.address && 
             this.serviceData.phone
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

.section-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
}

.elder-selection {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.elder-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.elder-item {
  padding: 20rpx 30rpx;
  border: 2rpx solid #ddd;
  border-radius: 10rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 150rpx;
}

.elder-item.active {
  border-color: #007AFF;
  background-color: #f0f8ff;
}

.elder-name {
  font-size: 28rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.elder-relation {
  font-size: 24rpx;
  color: #666;
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
  background-color: #007AFF;
  color: #fff;
}
</style>