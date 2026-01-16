<template>
  <view class="message-container">
    <view class="header">
      <text class="title">消息中心</text>
    </view>
    
    <view class="message-list">
      <view class="message-item" v-for="(message, index) in messages" :key="index">
        <view class="message-icon">{{ message.icon }}</view>
        <view class="message-content">
          <view class="message-header">
            <text class="message-title">{{ message.title }}</text>
            <text class="message-time">{{ message.time }}</text>
          </view>
          <text class="message-desc">{{ message.desc }}</text>
        </view>
        <view class="message-badge" v-if="message.unread">{{ message.unread }}</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      messages: []
    }
  },
  onLoad() {
    this.loadMessages()
  },
  methods: {
    async loadMessages() {
      try {
        const result = await this.$request.get('/messages')
        // 将后端数据映射到前端格式
        this.messages = this.mapMessages(result || [])
      } catch (error) {
        console.error('获取消息列表失败:', error)
        uni.showToast({
          title: '获取消息列表失败',
          icon: 'none'
        })
        
        // 失败时使用默认消息
        this.useDefaultMessages()
      }
    },
    
    // 将后端消息数据映射到前端格式
    mapMessages(backendMessages) {
      return backendMessages.map(message => {
        // 根据消息类型设置图标
        let icon = "📢" // 默认系统通知图标
        switch(message.type) {
          case 'VOLUNTEER':
            icon = "👥"
            break
          case 'TRAINING':
            icon = "📅"
            break
          case 'SYSTEM':
          default:
            icon = "📢"
            break
        }
        
        // 格式化时间
        const time = this.formatTime(message.createTime)
        
        return {
          id: message.id,
          icon: icon,
          title: message.title,
          desc: message.description || message.content,
          time: time,
          unread: message.isRead ? 0 : 1
        }
      })
    },
    
    // 格式化时间
    formatTime(timestamp) {
      if (!timestamp) return '未知时间'
      
      const date = new Date(timestamp)
      const now = new Date()
      const diff = now - date
      
      // 计算时间差
      const minutes = Math.floor(diff / 60000)
      const hours = Math.floor(diff / 3600000)
      const days = Math.floor(diff / 86400000)
      
      if (minutes < 1) return '刚刚'
      if (minutes < 60) return `${minutes}分钟前`
      if (hours < 24) return `${hours}小时前`
      if (days < 7) return `${days}天前`
      
      // 超过一周显示具体日期
      return `${date.getMonth() + 1}月${date.getDate()}日`
    },
    
    // 使用默认消息（当接口调用失败时）
    useDefaultMessages() {
      this.messages = [
        {
          icon: "📢",
          title: "系统通知",
          desc: "您的服务申请已通过审核",
          time: "10分钟前",
          unread: 1
        },
        {
          icon: "👥",
          title: "志愿者消息",
          desc: "李志愿者已接受您的帮助请求",
          time: "1小时前",
          unread: 1
        },
        {
          icon: "📅",
          title: "培训提醒",
          desc: "明天下午2点有智能手机培训",
          time: "昨天",
          unread: 0
        },
        {
          icon: "📢",
          title: "系统通知",
          desc: "平台将于明天凌晨2-4点进行维护",
          time: "2天前",
          unread: 0
        }
      ]
    },
    
    readMessage(index) {
      // 标记消息为已读
      this.messages[index].unread = 0
      // 调用接口更新消息状态
      try {
        this.$request.put(`/messages/${this.messages[index].id}/read`)
      } catch (error) {
        console.error('更新消息状态失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.message-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  padding: 30rpx;
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
}

.message-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.message-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.message-item:last-child {
  border-bottom: none;
}

.message-icon {
  font-size: 50rpx;
  margin-right: 30rpx;
}

.message-content {
  flex: 1;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.message-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.message-time {
  font-size: 26rpx;
  color: #999;
}

.message-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
}

.message-badge {
  background-color: #FF3B30;
  color: #fff;
  width: 40rpx;
  height: 40rpx;
  border-radius: 20rpx;
  text-align: center;
  line-height: 40rpx;
  font-size: 24rpx;
}
</style>