<template>
  <view class="community-container">
    <!-- 发布互助信息 -->
    <view class="publish-form" v-if="showPublishForm">
      <view class="form-item">
        <text class="label">互助类型</text>
        <picker @change="onTypeChange" :value="typeIndex" :range="typeOptions">
          <view class="picker">{{ typeOptions[typeIndex] || '请选择互助类型' }}</view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">标题</text>
        <input type="text" v-model="publishData.title" placeholder="请输入互助标题" />
      </view>
      
      <view class="form-item">
        <text class="label">详细描述</text>
        <textarea v-model="publishData.content" placeholder="请详细描述需要帮助的内容" style="height: 120rpx;"></textarea>
      </view>
      
      <view class="form-item">
        <text class="label">期望帮助时间</text>
        <picker mode="date" :value="publishData.expectedTime" @change="onDateChange">
          <view class="picker">{{ publishData.expectedTime || '请选择期望时间' }}</view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">联系方式</text>
        <input type="text" v-model="publishData.contact" placeholder="请输入联系电话" />
      </view>
      
      <view class="form-actions">
        <button class="btn-cancel" @click="showPublishForm = false">取消</button>
        <button class="btn-submit" @click="publishHelp">发布</button>
      </view>
    </view>
    
    <!-- 互助信息列表 -->
    <view class="help-list" v-else>
      <view class="section-header">
        <text class="title">社区互助</text>
        <button class="btn-new" @click="showPublishForm = true">+ 发布求助</button>
      </view>
      
      <view class="help-items">
        <view class="help-item" v-for="item in helpList" :key="item.id">
          <view class="help-header">
            <text class="type">{{ getTypeText(item.helpType) }}</text>
            <text class="status" :class="item.statusClass">{{ getStatusText(item.status) }}</text>
          </view>
          <text class="title">{{ item.title }}</text>
          <text class="content">{{ item.content }}</text>
          <view class="help-footer">
            <text class="author">{{ item.createBy }}</text>
            <text class="time">{{ formatDate(item.createTime) }}</text>
            <button class="btn-respond" v-if="item.status === 'PENDING'" @click="respondHelp(item.id)">响应帮助</button>
          </view>
        </view>
        
        <view class="load-more" v-if="hasMore" @click="loadMore">
          <text>加载更多</text>
        </view>
        
        <view class="empty" v-if="helpList.length === 0">
          <text>暂无互助信息</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { communityService } from '../../utils/api'

export default {
  data() {
    return {
      showPublishForm: false,
      typeIndex: 0,
      typeOptions: ['生活照料', '医疗陪护', '心理疏导', '物品代购', '其他帮助'],
      publishData: {
        helpType: '',
        title: '',
        content: '',
        expectedTime: '',
        contact: ''
      },
      helpList: [],
      currentPage: 1,
      pageSize: 10,
      hasMore: true,
      loading: false
    }
  },
  
  onLoad() {
    this.loadHelpList()
  },
  
  methods: {
    onTypeChange(e) {
      this.typeIndex = e.detail.value
      this.publishData.helpType = this.typeOptions[this.typeIndex]
    },
    
    onDateChange(e) {
      this.publishData.expectedTime = e.detail.value
    },
    
    async publishHelp() {
      if (!this.validateForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      try {
        uni.showLoading({ title: '发布中...' })
        await communityService.publishHelp(this.publishData)
        uni.showToast({ title: '发布成功' })
        this.showPublishForm = false
        this.resetForm()
        this.loadHelpList()
      } catch (error) {
        console.error('发布失败:', error)
      } finally {
        uni.hideLoading()
      }
    },
    
    async respondHelp(helpId) {
      try {
        uni.showLoading({ title: '响应中...' })
        await communityService.respondHelp(helpId)
        uni.showToast({ title: '响应成功' })
        this.loadHelpList()
      } catch (error) {
        console.error('响应失败:', error)
      } finally {
        uni.hideLoading()
      }
    },
    
    validateForm() {
      return this.publishData.helpType && 
             this.publishData.title && 
             this.publishData.content && 
             this.publishData.expectedTime && 
             this.publishData.contact
    },
    
    resetForm() {
      this.publishData = {
        helpType: '',
        title: '',
        content: '',
        expectedTime: '',
        contact: ''
      }
      this.typeIndex = 0
    },
    
    async loadHelpList() {
      if (this.loading) return
      
      this.loading = true
      try {
        const result = await communityService.getAllHelps(this.currentPage, this.pageSize)
        // 处理互助信息数据，添加状态类名
        const processedRecords = (result.records || []).map(item => ({
          ...item,
          statusClass: this.getStatusClass(item.status)
        }))
        
        if (this.currentPage === 1) {
          this.helpList = processedRecords
        } else {
          this.helpList = [...this.helpList, ...processedRecords]
        }
        this.hasMore = result.current < result.pages
      } catch (error) {
        console.error('加载互助信息失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.currentPage++
        this.loadHelpList()
      }
    },
    
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待帮助',
        'RESPONDED': '已响应',
        'COMPLETED': '已完成'
      }
      return statusMap[status] || '未知状态'
    },
    
    getStatusClass(status) {
      const classMap = {
        'PENDING': 'status-pending',
        'RESPONDED': 'status-responded',
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
.community-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.publish-form {
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

.help-items {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.help-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.help-item:last-child {
  border-bottom: none;
}

.help-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.type {
  font-size: 28rpx;
  color: #007AFF;
  background-color: #E3F2FD;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
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

.status-responded {
  background-color: #D1ECF1;
  color: #0C5460;
}

.status-completed {
  background-color: #D4EDDA;
  color: #155724;
}

.title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.content {
  display: block;
  font-size: 30rpx;
  color: #666;
  line-height: 45rpx;
  margin-bottom: 15rpx;
}

.help-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.author, .time {
  font-size: 26rpx;
  color: #999;
}

.btn-respond {
  background-color: #28A745;
  color: #fff;
  font-size: 26rpx;
  padding: 8rpx 16rpx;
  border-radius: 15rpx;
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