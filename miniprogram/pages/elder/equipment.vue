<template>
  <view class="equipment-container">
    <view class="service-list">
      <view class="service-item" v-for="item in equipmentList" :key="item.id" @click="selectEquipment(item)">
        <image class="service-image" :src="item.image" mode="aspectFill"></image>
        <view class="service-info">
          <text class="service-name">{{ item.name }}</text>
          <text class="service-desc">{{ item.description }}</text>
          <text class="service-price">{{ item.price }}元/天</text>
        </view>
      </view>
    </view>
    
    <view class="apply-form" v-if="selectedEquipment">
      <view class="form-item">
        <text class="label">申请设备</text>
        <text class="value">{{ selectedEquipment.name }}</text>
      </view>
      <view class="form-item">
        <text class="label">租赁天数</text>
        <input type="number" v-model="applyForm.rentalDays" placeholder="请输入租赁天数" />
      </view>
      <view class="form-item">
        <text class="label">申请原因</text>
        <textarea v-model="applyForm.serviceContent" placeholder="请详细描述您的需求" style="height: 200rpx;"></textarea>
      </view>
      <view class="form-item">
        <text class="label">联系地址</text>
        <input type="text" v-model="applyForm.address" placeholder="请输入详细地址" />
      </view>
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="applyForm.contactPhone" placeholder="请输入联系电话" />
      </view>
      <view class="form-item">
        <text class="label">预约时间</text>
        <picker mode="date" v-model="applyForm.appointmentTime" @change="onDateChange">
          <view class="picker">
            {{ applyForm.appointmentTime }}
          </view>
        </picker>
      </view>
      
      <view class="total-price">
        <text>总计：¥{{ selectedEquipment.price * (applyForm.rentalDays || 1) }}</text>
      </view>
      
      <button type="primary" @click="submitApply">提交申请</button>
    </view>
  </view>
</template>

<script>
import { equipmentService } from '../../utils/api'

export default {
  data() {
    return {
      equipmentList: [],
      selectedEquipment: null,
      applyForm: {
        rentalDays: 1,
        serviceContent: '',
        address: '',
        contactPhone: '',
        appointmentTime: ''
      }
    }
  },
  onLoad() {
    this.loadEquipmentList()
    // 初始化默认日期为今天
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    this.applyForm.appointmentTime = `${year}-${month}-${day}`
  },
  methods: {
    async loadEquipmentList() {
      try {
        uni.showLoading({ title: '加载中...' })
        const res = await equipmentService.getServiceTypes()
        if (res.code === 200) {
          this.equipmentList = res.data
        } else {
          uni.showToast({ title: '加载设备列表失败', icon: 'none' })
        }
      } catch (error) {
        console.error('加载设备列表失败:', error)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    selectEquipment(equipment) {
      this.selectedEquipment = equipment
    },
    onDateChange(e) {
      this.applyForm.appointmentTime = e.detail.value
    },
    async submitApply() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const applyData = {
        serviceTypeId: this.selectedEquipment.id,
        serviceContent: this.applyForm.serviceContent,
        appointmentTime: this.applyForm.appointmentTime,
        address: this.applyForm.address,
        contactPhone: this.applyForm.contactPhone
      }
      
      try {
        uni.showLoading({ title: '提交中...' })
        const res = await equipmentService.applyService(applyData)
        if (res.code === 200) {
          uni.showToast({ title: '申请提交成功' })
          this.resetForm()
        } else {
          uni.showToast({ title: res.message || '提交失败', icon: 'none' })
        }
      } catch (error) {
        console.error('提交申请失败:', error)
        uni.showToast({ title: '提交失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    validateForm() {
      return this.selectedEquipment && 
             this.applyForm.serviceContent && 
             this.applyForm.address && 
             this.applyForm.contactPhone && 
             this.applyForm.appointmentTime
    },
    resetForm() {
      this.selectedEquipment = null
      this.applyForm = {
        rentalDays: 1,
        serviceContent: '',
        address: '',
        contactPhone: '',
        appointmentTime: ''
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
.equipment-container {
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
  display: flex;
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.service-item:last-child {
  border-bottom: none;
}

.service-image {
  width: 150rpx;
  height: 150rpx;
  border-radius: 10rpx;
  margin-right: 20rpx;
}

.service-info {
  flex: 1;
}

.service-name {
  display: block;
  font-size: 34rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.service-desc {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 5rpx;
}

.service-price {
  display: block;
  font-size: 28rpx;
  color: #007AFF;
  font-weight: bold;
}

.apply-form {
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