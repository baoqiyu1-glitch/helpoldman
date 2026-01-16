<template>
  <view class="cooking-container">
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

    <view class="cooking-form">
      <view class="form-item">
        <text class="label">服务类型</text>
        <picker mode="selector" :range="serviceTypes" v-model="orderForm.serviceTypeIndex" @change="onServiceTypeChange">
          <view class="picker">
            {{ serviceTypes[orderForm.serviceTypeIndex] }}
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">服务时间</text>
        <picker mode="date" v-model="orderForm.serviceTime" @change="onDateChange">
          <view class="picker">
            {{ orderForm.serviceTime || '请选择服务日期' }}
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">服务频率</text>
        <picker mode="selector" :range="frequencies" v-model="orderForm.frequencyIndex" @change="onFrequencyChange">
          <view class="picker">
            {{ frequencies[orderForm.frequencyIndex] }}
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">服务时长（天）</text>
        <input type="number" v-model="orderForm.duration" placeholder="请输入服务天数" />
      </view>
      
      <view class="form-item">
        <text class="label">服务地址</text>
        <input type="text" v-model="orderForm.address" placeholder="请输入服务地址" />
      </view>
      
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="orderForm.phone" placeholder="请输入联系电话" />
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
        <text>总计：¥{{ calculateTotalPrice() }}</text>
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
      serviceTypes: ['只做饭', '做饭+送饭', '包月做饭'],
      frequencies: ['单次', '每日一次', '每周三次', '每周五次'],
      orderForm: {
        elderId: '',
        serviceType: '只做饭',
        serviceTime: '',
        frequency: '单次',
        duration: 1,
        address: '',
        phone: '',
        dishRequirements: '',
        specialRequirements: ''
      }
    }
  },
  onLoad() {
    this.loadElders()
    // 初始化默认日期为今天
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    this.orderForm.serviceTime = `${year}-${month}-${day}`
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
    
    selectElder(elder) {
      this.selectedElder = elder
      this.orderForm.elderId = elder.id
      // 设置默认地址和电话
      this.orderForm.address = elder.address || ''
      this.orderForm.phone = elder.phone || ''
    },
    
    onDateChange(e) {
      this.orderForm.serviceTime = e.detail.value
    },
    
    onServiceTypeChange(e) {
      this.orderForm.serviceType = this.serviceTypes[e.detail.value]
    },
    
    onFrequencyChange(e) {
      this.orderForm.frequency = this.frequencies[e.detail.value]
    },
    
    calculateTotalPrice() {
      const basePrice = this.getBasePrice(this.orderForm.serviceType)
      const frequencyMultiplier = this.getFrequencyMultiplier(this.orderForm.frequency)
      return basePrice * frequencyMultiplier * (this.orderForm.duration || 1)
    },
    
    getBasePrice(serviceType) {
      const prices = {
        '只做饭': 80,
        '做饭+送饭': 100,
        '包月做饭': 2000
      }
      return prices[serviceType] || 80
    },
    
    getFrequencyMultiplier(frequency) {
      const multipliers = {
        '单次': 1,
        '每日一次': 30,
        '每周三次': 12,
        '每周五次': 20
      }
      return multipliers[frequency] || 1
    },
    
    async submitApplication() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const applicationData = {
        ...this.orderForm,
        totalAmount: this.calculateTotalPrice()
      }
      
      try {
        uni.showLoading({ title: '提交申请中...' })
        const res = await this.$request.post('/family/apply-cooking', applicationData)
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
      return this.orderForm.elderId && 
             this.orderForm.serviceTime && 
             this.orderForm.duration && 
             this.orderForm.address && 
             this.orderForm.phone
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
  background-color: #007AFF;
  color: #fff;
}
</style>