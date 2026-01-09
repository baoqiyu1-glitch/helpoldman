<template>
  <view class="barrier-free-container">
    <view class="intro-section">
      <text class="title">无障碍改造服务</text>
      <text class="desc">为行动不便的老人提供居家无障碍改造服务，包括坡道安装、扶手加装、卫生间改造等。</text>
    </view>
    
    <view class="apply-form">
      <view class="form-item">
        <text class="label">改造类型</text>
        <picker mode="selector" :range="transformTypes" v-model="applyForm.typeIndex" @change="onTypeChange">
          <view class="picker">
            {{ transformTypes[applyForm.typeIndex] }}
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">改造地址</text>
        <input type="text" v-model="applyForm.address" placeholder="请输入详细地址" />
      </view>
      <view class="form-item">
        <text class="label">联系电话</text>
        <input type="tel" v-model="applyForm.phone" placeholder="请输入联系电话" />
      </view>
      <view class="form-item">
        <text class="label">改造需求描述</text>
        <textarea v-model="applyForm.description" placeholder="请详细描述您的改造需求" style="height: 200rpx;"></textarea>
      </view>
      <view class="form-item">
        <text class="label">上传现场照片</text>
        <view class="upload-section">
          <view class="upload-item" v-for="(image, index) in applyForm.images" :key="index">
            <image :src="image" mode="aspectFill"></image>
            <view class="delete-btn" @click="deleteImage(index)">×</view>
          </view>
          <view class="upload-btn" @click="uploadImage" v-if="applyForm.images.length < 3">
            <text>+</text>
          </view>
        </view>
      </view>
      
      <button type="primary" @click="submitApply">提交申请</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      transformTypes: ['坡道安装', '扶手加装', '卫生间改造', '厨房改造', '其他'],
      applyForm: {
        typeIndex: 0,
        address: '',
        phone: '',
        description: '',
        images: []
      }
    }
  },
  methods: {
    onTypeChange(e) {
      this.applyForm.typeIndex = e.detail.value
    },
    uploadImage() {
      uni.chooseImage({
        count: 3 - this.applyForm.images.length,
        success: (res) => {
          this.applyForm.images = [...this.applyForm.images, ...res.tempFilePaths]
        }
      })
    },
    deleteImage(index) {
      this.applyForm.images.splice(index, 1)
    },
    submitApply() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
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
      return this.applyForm.address && this.applyForm.phone && this.applyForm.description
    },
    resetForm() {
      this.applyForm = {
        typeIndex: 0,
        address: '',
        phone: '',
        description: '',
        images: []
      }
    }
  }
}
</script>

<style scoped>
.barrier-free-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.intro-section {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  text-align: center;
}

.desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  line-height: 45rpx;
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

.picker, input, textarea {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
}

.upload-section {
  display: flex;
  flex-wrap: wrap;
}

.upload-item {
  position: relative;
  width: 200rpx;
  height: 200rpx;
  margin-right: 20rpx;
  margin-bottom: 20rpx;
}

.upload-item image {
  width: 100%;
  height: 100%;
  border-radius: 10rpx;
}

.delete-btn {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
  width: 40rpx;
  height: 40rpx;
  background-color: #FF4444;
  color: #fff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 40rpx;
  line-height: 40rpx;
}

.upload-btn {
  width: 200rpx;
  height: 200rpx;
  border: 2rpx dashed #ddd;
  border-radius: 10rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 80rpx;
  color: #999;
}

button {
  width: 100%;
  height: 90rpx;
  font-size: 34rpx;
  border-radius: 10rpx;
}
</style>