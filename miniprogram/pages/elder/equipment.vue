<template>
  <view class="equipment-container">
    <view class="service-list">
      <view class="service-item" v-for="item in equipmentList" :key="item.id" @click="selectEquipment(item)">
        <image class="service-image" :src="item.image" mode="aspectFill"></image>
        <view class="service-info">
          <text class="service-name">{{ item.name }}</text>
          <text class="service-desc">{{ item.description }}</text>
        </view>
      </view>
    </view>
    
    <view class="apply-form" v-if="selectedEquipment">
      <view class="form-item">
        <text class="label">申请设备</text>
        <text class="value">{{ selectedEquipment.name }}</text>
      </view>
      <view class="form-item">
        <text class="label">申请原因</text>
        <textarea v-model="applyForm.reason" placeholder="请详细描述您的需求" style="height: 200rpx;"></textarea>
      </view>
      <view class="form-item">
        <text class="label">联系地址</text>
        <input type="text" v-model="applyForm.address" placeholder="请输入详细地址" />
      </view>
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="applyForm.phone" placeholder="请输入联系电话" />
      </view>
      
      <button type="primary" @click="submitApply">提交申请</button>
    </view>
  </view>
</template>

<script>
import request from '../../utils/requset.js'

export default {
  data() {
    return {
      equipmentList: [
        { id: 1, name: '轮椅', description: '手动轮椅，适合行动不便老人', image: '../../static/images/wheelchair.png' },
        { id: 2, name: '拐杖', description: '铝合金拐杖，轻便耐用', image: '../../static/images/crutch.png' },
        { id: 3, name: '助行器', description: '四脚助行器，稳定性好', image: '../../static/images/walker.png' },
        { id: 4, name: '助听器', description: '数字助听器，提高听力', image: '../../static/images/hearing-aid.png' },
        { id: 5, name: '护理床', description: '多功能护理床，方便照顾', image: '../../static/images/nursing-bed.png' }
      ],
      selectedEquipment: null,
      applyForm: {
        reason: '',
        address: '',
        phone: ''
      }
    }
  },
  methods: {
    selectEquipment(equipment) {
      this.selectedEquipment = equipment
    },
    submitApply() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const applyData = {
        equipmentId: this.selectedEquipment.id,
        equipmentName: this.selectedEquipment.name,
        reason: this.applyForm.reason,
        address: this.applyForm.address,
        phone: this.applyForm.phone
      }
      
      uni.showLoading({ title: '提交中...' })
      // 这里替换为实际API调用
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({ title: '申请提交成功' })
        this.resetForm()
      }, 1000)
    },
    validateForm() {
      return this.selectedEquipment && this.applyForm.reason && this.applyForm.address && this.applyForm.phone
    },
    resetForm() {
      this.selectedEquipment = null
      this.applyForm = {
        reason: '',
        address: '',
        phone: ''
      }
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

input, textarea {
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
}
</style>