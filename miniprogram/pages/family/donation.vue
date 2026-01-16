<template>
  <view class="donation-container">
    <!-- 老人选择 -->
    <view class="elder-selection" v-if="elders.length > 0">
      <text class="section-title">选择捐赠对象</text>
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

    <view class="donation-intro">
      <text class="title">爱心捐赠</text>
      <text class="desc">感谢您的爱心捐赠，您的每一份贡献都将帮助到需要帮助的老人。</text>
    </view>
    
    <view class="donation-form">
      <view class="form-item">
        <text class="label">捐赠金额</text>
        <view class="amount-options">
          <view class="amount-item" v-for="amount in amountOptions" :key="amount" @click="selectAmount(amount)">
            <text :class="{ active: selectedAmount === amount }">{{ amount }}元</text>
          </view>
        </view>
        <input type="number" v-model="customAmount" placeholder="其他金额" v-if="selectedAmount === 'custom'" />
      </view>
      
      <view class="form-item">
        <text class="label">捐赠人信息</text>
        <input type="text" v-model="donorInfo.name" placeholder="请输入姓名" />
        <input type="tel" v-model="donorInfo.phone" placeholder="请输入联系电话" />
      </view>
      
      <view class="form-item">
        <text class="label">留言</text>
        <textarea v-model="donorInfo.message" placeholder="请输入您的留言（可选）" style="height: 120rpx;"></textarea>
      </view>
      
      <button type="primary" @click="submitDonation">确认捐赠</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      elders: [],
      selectedElder: null,
      donationInfo: {},
      amountOptions: [],
      selectedAmount: '',
      customAmount: '',
      donorInfo: {
        name: '',
        phone: '',
        message: ''
      }
    }
  },
  onLoad() {
    this.loadElders()
    this.loadDonationInfo()
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
    },
    
    async loadDonationInfo() {
      try {
        const res = await this.$request.get('/donation/info')
        if (res.code === 200) {
          this.donationInfo = res.data
          this.amountOptions = res.data.amountOptions || ['50', '100', '200', '500', 'custom']
        }
      } catch (error) {
        console.error('加载捐赠信息失败:', error)
      }
    },
    
    selectAmount(amount) {
      this.selectedAmount = amount
      if (amount !== 'custom') {
        this.customAmount = ''
      }
    },
    
    async submitDonation() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      if (!this.selectedElder) {
        uni.showToast({ title: '请选择捐赠对象', icon: 'none' })
        return
      }
      
      const donationData = {
        amount: this.selectedAmount === 'custom' ? this.customAmount : this.selectedAmount,
        ...this.donorInfo,
        elderId: this.selectedElder.id
      }
      
      try {
        uni.showLoading({ title: '捐赠处理中...' })
        const res = await this.$request.post('/donation/submit', donationData)
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({ title: '捐赠成功，感谢您的爱心！' })
          this.resetForm()
        } else {
          uni.showToast({ title: res.message || '捐赠失败', icon: 'none' })
        }
      } catch (error) {
        uni.hideLoading()
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
      }
    },
    
    validateForm() {
      const amount = this.selectedAmount === 'custom' ? this.customAmount : this.selectedAmount
      return amount && this.donorInfo.name && this.donorInfo.phone
    },
    
    resetForm() {
      this.selectedAmount = ''
      this.customAmount = ''
      this.donorInfo = {
        name: '',
        phone: '',
        message: ''
      }
    }
  }
}
</script>

<style scoped>
.donation-container {
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

.donation-intro {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  text-align: center;
}

.title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #e74c3c;
}

.desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  line-height: 45rpx;
}

.donation-form {
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

.amount-options {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 20rpx;
}

.amount-item {
  width: 150rpx;
  height: 80rpx;
  border: 2rpx solid #ddd;
  border-radius: 10rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20rpx;
  margin-bottom: 20rpx;
}

.amount-item text {
  font-size: 32rpx;
  color: #333;
}

.amount-item text.active {
  color: #e74c3c;
  font-weight: bold;
}

.amount-item.active {
  border-color: #e74c3c;
}

input, textarea {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
  margin-bottom: 20rpx;
}

button {
  width: 100%;
  height: 90rpx;
  font-size: 34rpx;
  border-radius: 10rpx;
  background-color: #e74c3c;
  color: #fff;
}
</style>