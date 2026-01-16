<template>
  <view class="barrier-free-container">
    <!-- 申请表单 -->
    <view class="apply-form" v-if="showForm">
      <view class="form-item">
        <text class="label">改造类型</text>
        <picker @change="onTypeChange" :value="typeIndex" :range="typeOptions">
          <view class="picker">{{ typeOptions[typeIndex] || '请选择改造类型' }}</view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">详细描述</text>
        <textarea v-model="applyData.description" placeholder="请详细描述需要改造的内容和需求" style="height: 150rpx;"></textarea>
      </view>
      
      <view class="form-item">
        <text class="label">期望改造时间</text>
        <picker mode="date" :value="applyData.expectedTime" @change="onDateChange">
          <view class="picker">{{ applyData.expectedTime || '请选择期望时间' }}</view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">预算金额（元）</text>
        <input type="number" v-model="applyData.budget" placeholder="请输入预算金额" />
      </view>
      
      <view class="form-item">
        <text class="label">地址信息</text>
        <input type="text" v-model="applyData.address" placeholder="请输入详细地址" />
      </view>
      
      <view class="form-actions">
        <button class="btn-cancel" @click="showForm = false">取消</button>
        <button class="btn-submit" @click="submitApplication">提交申请</button>
      </view>
    </view>
    
    <!-- 申请记录 -->
    <view class="my-applications" v-else>
      <view class="section-header">
        <text class="title">我的无障碍改造申请</text>
        <button class="btn-new" @click="showForm = true">+ 新申请</button>
      </view>
      
      <view class="application-list">
        <view class="application-item" v-for="item in applications" :key="item.id">
          <view class="application-header">
            <text class="status" :class="item.statusClass">{{ getStatusText(item.status) }}</text>
            <text class="date">{{ formatDate(item.createTime) }}</text>
          </view>
          <text class="type">改造类型：{{ getTypeText(item.renovationType) }}</text>
          <text class="description">{{ item.description }}</text>
          <text class="budget">预算：{{ item.budget }}元</text>
          <text class="address">地址：{{ item.address }}</text>
        </view>
        
        <view class="load-more" v-if="hasMore" @click="loadMore">
          <text>加载更多</text>
        </view>
        
        <view class="empty" v-if="applications.length === 0">
          <text>暂无申请记录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { barrierFreeService } from '../../utils/api'

export default {
  data() {
    return {
      showForm: false,
      typeIndex: 0,
      typeOptions: ['卫生间改造', '厨房改造', '卧室改造', '通道改造', '其他'],
      applyData: {
        renovationType: '',
        description: '',
        expectedTime: '',
        budget: '',
        address: ''
      },
      applications: [],
      currentPage: 1,
      pageSize: 10,
      hasMore: true,
      loading: false
    }
  },
  
  onLoad() {
    this.loadApplications()
  },
  
  computed: {
    // 添加计算属性来处理状态类名
    processedApplications() {
      return this.applications.map(item => ({
        ...item,
        statusClass: this.getStatusClass(item.status)
      }))
    }
  },
  
  methods: {
    onTypeChange(e) {
      this.typeIndex = e.detail.value
      this.applyData.renovationType = this.typeOptions[this.typeIndex]
    },
    
    onDateChange(e) {
      this.applyData.expectedTime = e.detail.value
    },
    
    async submitApplication() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      try {
        uni.showLoading({ title: '提交中...' })
        await barrierFreeService.applyRenovation(this.applyData)
        uni.showToast({ title: '申请提交成功' })
        this.showForm = false
        this.resetForm()
        this.loadApplications()
      } catch (error) {
        console.error('提交申请失败:', error)
      } finally {
        uni.hideLoading()
      }
    },
    
    validateForm() {
      return this.applyData.renovationType && 
             this.applyData.description && 
             this.applyData.expectedTime && 
             this.applyData.budget && 
             this.applyData.address
    },
    
    resetForm() {
      this.applyData = {
        renovationType: '',
        description: '',
        expectedTime: '',
        budget: '',
        address: ''
      }
      this.typeIndex = 0
    },
    
    async loadApplications() {
      if (this.loading) return
      
      this.loading = true
      try {
        const result = await barrierFreeService.getMyApplications(this.currentPage, this.pageSize)
        // 处理应用数据，添加状态类名
        const processedRecords = (result.records || []).map(item => ({
          ...item,
          statusClass: this.getStatusClass(item.status)
        }))
        
        if (this.currentPage === 1) {
          this.applications = processedRecords
        } else {
          this.applications = [...this.applications, ...processedRecords]
        }
        this.hasMore = result.current < result.pages
      } catch (error) {
        console.error('加载申请记录失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.currentPage++
        this.loadApplications()
      }
    },
    
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待审核',
        'APPROVED': '已批准',
        'REJECTED': '已驳回',
        'IN_PROGRESS': '进行中',
        'COMPLETED': '已完成'
      }
      return statusMap[status] || '未知状态'
    },
    
    getStatusClass(status) {
      const classMap = {
        'PENDING': 'status-pending',
        'APPROVED': 'status-approved',
        'REJECTED': 'status-rejected',
        'IN_PROGRESS': 'status-processing',
        'COMPLETED': 'status-completed'
      }
      return classMap[status] || 'status-pending'
    },
    
    getTypeText(type) {
      return type || '未知类型'
    },
    
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleDateString()
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
  margin-bottom: 15rpx;
}

textarea, input, .picker {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
}

.form-actions {
  display: flex;
  gap: 20rpx;
}

.btn-cancel, .btn-submit {
  flex: 1;
  height: 80rpx;
  border-radius: 10rpx;
  font-size: 32rpx;
}

.btn-cancel {
  background-color: #999;
  color: #fff;
}

.btn-submit {
  background-color: #007AFF;
  color: #fff;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
}

.btn-new {
  background-color: #007AFF;
  color: #fff;
  font-size: 28rpx;
  padding: 10rpx 20rpx;
  border-radius: 20rpx;
}

.application-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.application-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.application-item:last-child {
  border-bottom: none;
}

.application-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.status {
  font-size: 28rpx;
  font-weight: bold;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
}

.status-pending {
  background-color: #FFF3CD;
  color: #856404;
}

.status-approved {
  background-color: #D4EDDA;
  color: #155724;
}

.status-rejected {
  background-color: #F8D7DA;
  color: #721C24;
}

.status-processing {
  background-color: #D1ECF1;
  color: #0C5460;
}

.status-completed {
  background-color: #E2E3E5;
  color: #383D41;
}

.date {
  font-size: 26rpx;
  color: #999;
}

.type, .description, .budget, .address {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 5rpx;
  line-height: 40rpx;
}

.description {
  color: #333;
  margin: 10rpx 0;
}

.load-more {
  text-align: center;
  padding: 30rpx;
  color: #007AFF;
}

.empty {
  text-align: center;
  padding: 60rpx;
  color: #999;
  font-size: 30rpx;
}
</style>