<template>
  <view class="nurse-container">
    <!-- 老人选择 -->
    <view class="elder-selection" v-if="elders.length > 0">
      <text class="section-title">选择服务对象</text>
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

    <!-- 护工服务申请表单 -->
    <view class="service-form">
      <text class="section-title">护工服务申请</text>
      
      <view class="form-item">
        <text class="label">服务类型</text>
        <picker @change="onServiceTypeChange" :value="serviceTypeIndex" :range="nurseTypes" range-key="name">
          <view class="picker">{{ serviceData.serviceType || '请选择服务类型' }}</view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">服务时间</text>
        <picker mode="date" @change="onDateChange">
          <view class="picker">{{ serviceData.serviceTime || '请选择服务日期' }}</view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">服务时长</text>
        <picker @change="onDurationChange" :value="durationIndex" :range="durationOptions">
          <view class="picker">{{ serviceData.duration || '请选择服务时长' }}</view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">特殊要求</text>
        <textarea v-model="serviceData.specialRequirements" placeholder="请输入特殊要求（可选）" style="height: 120rpx;"></textarea>
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
      nurseTypes: [],
      serviceTypeIndex: 0,
      durationIndex: 0,
      durationOptions: ['1小时', '2小时', '4小时', '8小时', '全天'],
      serviceData: {
        elderId: '',
        serviceType: '',
        serviceTime: '',
        duration: '',
        specialRequirements: ''
      }
    }
  },
  onLoad() {
    this.loadElders()
    this.loadNurseTypes()
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
      this.serviceData.elderId = elder.id
    },
    
    async loadNurseTypes() {
      try {
        const res = await this.$request.get('/nursing/types')
        if (res.code === 200) {
          this.nurseTypes = res.data
        }
      } catch (error) {
        console.error('加载护工类型失败:', error)
      }
    },
    
    onServiceTypeChange(e) {
      const index = e.detail.value
      this.serviceTypeIndex = index
      this.serviceData.serviceType = this.nurseTypes[index].name
    },
    
    onDateChange(e) {
      this.serviceData.serviceTime = e.detail.value
    },
    
    onDurationChange(e) {
      const index = e.detail.value
      this.durationIndex = index
      this.serviceData.duration = this.durationOptions[index]
    },
    
    async submitApplication() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      if (!this.selectedElder) {
        uni.showToast({ title: '请选择服务对象', icon: 'none' })
        return
      }
      
      try {
        uni.showLoading({ title: '提交中...' })
        // 使用家属端专用护工服务接口
        const res = await this.$request.post('/family/apply-nursing', this.serviceData)
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({ title: '申请提交成功' })
          this.resetForm()
        } else {
          uni.showToast({ title: res.message || '申请失败', icon: 'none' })
        }
      } catch (error) {
        uni.hideLoading()
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
      }
    },
    
    validateForm() {
      return this.serviceData.serviceType && this.serviceData.serviceTime && this.serviceData.duration
    },
    
    resetForm() {
      this.serviceData = {
        elderId: this.selectedElder ? this.selectedElder.id : '',
        serviceType: '',
        serviceTime: '',
        duration: '',
        specialRequirements: ''
      }
      this.serviceTypeIndex = 0
      this.durationIndex = 0
    }
  }
}
</script>

<style scoped>
.nurse-container {
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

.service-form {
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
  margin-bottom: 15rpx;
}

.picker {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
}

textarea {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
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