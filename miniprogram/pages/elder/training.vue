<template>
  <view class="training-container">
    <view class="training-list">
      <view class="training-item" v-for="training in trainingList" :key="training.id">
        <image class="training-image" :src="training.image" mode="aspectFill"></image>
        <view class="training-info">
          <text class="training-title">{{ training.name }}</text>
          <text class="training-desc">{{ training.description }}</text>
          <view class="training-meta">
            <text class="time">时长: {{ training.duration }}小时</text>
            <text class="price">费用: {{ training.price }}元</text>
          </view>
          <button class="apply-btn" @click="applyTraining(training)">立即报名</button>
        </view>
      </view>
    </view>
    
    <view class="apply-form" v-if="selectedTraining">
      <view class="form-item">
        <text class="label">培训课程</text>
        <text class="value">{{ selectedTraining.name }}</text>
      </view>
      <view class="form-item">
        <text class="label">报名人数</text>
        <input type="number" v-model="applyForm.participantCount" placeholder="请输入报名人数" />
      </view>
      <view class="form-item">
        <text class="label">联系方式</text>
        <input type="tel" v-model="applyForm.contactPhone" placeholder="请输入联系电话" />
      </view>
      <view class="form-item">
        <text class="label">联系地址</text>
        <input type="text" v-model="applyForm.address" placeholder="请输入详细地址" />
      </view>
      <view class="form-item">
        <text class="label">预约时间</text>
        <picker mode="date" v-model="applyForm.appointmentTime" @change="onDateChange">
          <view class="picker">
            {{ applyForm.appointmentTime }}
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">备注信息</text>
        <textarea v-model="applyForm.serviceContent" placeholder="请输入其他需求或备注" style="height: 150rpx;"></textarea>
      </view>
      
      <view class="total-price">
        <text>总计：¥{{ selectedTraining.price * (applyForm.participantCount || 1) }}</text>
      </view>
      
      <button type="primary" @click="submitApplication">提交报名</button>
    </view>
  </view>
</template>

<script>
import { trainingService } from '../../utils/api'

export default {
  data() {
    return {
      trainingList: [],
      selectedTraining: null,
      applyForm: {
        participantCount: 1,
        contactPhone: '',
        address: '',
        appointmentTime: '',
        serviceContent: ''
      }
    }
  },
  onLoad() {
    this.loadTrainingList()
    // 初始化默认日期为今天
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    this.applyForm.appointmentTime = `${year}-${month}-${day}`
  },
  methods: {
    async loadTrainingList() {
      try {
        uni.showLoading({ title: '加载中...' })
        const res = await trainingService.getServiceTypes()
        if (res.code === 200) {
          this.trainingList = res.data
        } else {
          uni.showToast({ title: '加载培训列表失败', icon: 'none' })
        }
      } catch (error) {
        console.error('加载培训列表失败:', error)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    applyTraining(training) {
      this.selectedTraining = training
    },
    onDateChange(e) {
      this.applyForm.appointmentTime = e.detail.value
    },
    async submitApplication() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const applyData = {
        serviceTypeId: this.selectedTraining.id,
        serviceContent: this.applyForm.serviceContent,
        appointmentTime: this.applyForm.appointmentTime,
        address: this.applyForm.address,
        contactPhone: this.applyForm.contactPhone
      }
      
      try {
        uni.showLoading({ title: '提交中...' })
        const res = await trainingService.applyService(applyData)
        if (res.code === 200) {
          uni.showToast({ title: '报名成功' })
          this.resetForm()
        } else {
          uni.showToast({ title: res.message || '报名失败', icon: 'none' })
        }
      } catch (error) {
        console.error('报名失败:', error)
        uni.showToast({ title: '报名失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    validateForm() {
      return this.selectedTraining && 
             this.applyForm.contactPhone && 
             this.applyForm.address && 
             this.applyForm.appointmentTime
    },
    resetForm() {
      this.selectedTraining = null
      this.applyForm = {
        participantCount: 1,
        contactPhone: '',
        address: '',
        appointmentTime: '',
        serviceContent: ''
      }
      // 重新设置默认日期
      const today = new Date()
      const year = today.getFullYear()
      const month = String(today.getMonth() + 1).padStart(2, '0')
      const day = String(today.getDate()).padStart(2, '0')
      this.applyForm.appointmentTime = `${year}-${month}-${day}`
    }
  }
}
</script>

<style scoped>
.training-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.training-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.training-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.training-item:last-child {
  border-bottom: none;
}

.training-image {
  width: 100%;
  height: 300rpx;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.training-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.training-desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 20rpx;
  line-height: 45rpx;
}

.training-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.time, .price {
  font-size: 28rpx;
  color: #007AFF;
  font-weight: bold;
}

.apply-btn {
  width: 100%;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 30rpx;
}

.apply-form {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 20rpx;
  margin-top: 20rpx;
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