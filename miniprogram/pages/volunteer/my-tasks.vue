<template>
  <view class="container">
    <view class="header">
      <text class="title">我的任务</text>
    </view>
    <view class="tabs">
      <view class="tab-item" :class="{ active: activeTab === 'current' }" @click="activeTab = 'current'">进行中</view>
      <view class="tab-item" :class="{ active: activeTab === 'completed' }" @click="activeTab = 'completed'">已完成</view>
    </view>
    <view class="tasks-list">
      <view v-for="task in filteredTasks" :key="task.id" class="task-item">
        <view class="task-header">
          <text class="task-title">{{ task.title }}</text>
          <text class="task-status" :class="{ 'status-completed': task.status === 'completed' }">
            {{ getStatusText(task.status) }}
          </text>
        </view>
        <view class="task-info">
          <text class="info-item">用户：{{ task.userName }}</text>
          <text class="info-item">时间：{{ task.date }}</text>
          <text class="info-item">地址：{{ task.address }}</text>
        </view>
        <view class="task-actions">
          <button class="detail-btn" @click="viewDetails(task)">查看详情</button>
          <button v-if="task.status === 'in_progress'" class="complete-btn" @click="completeTask(task)">完成任务</button>
        </view>
      </view>
      <view v-if="filteredTasks.length === 0" class="no-tasks">
        <text>暂无任务</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'current',
      tasks: [],
      textConfig: {}
    }
  },
  onLoad() {
    this.loadTasks()
    this.loadTextConfig()
  },
  computed: {
    filteredTasks() {
      if (this.activeTab === 'current') {
        return this.tasks.filter(task => task.status === 'in_progress' || task.status === 'assigned')
      } else {
        return this.tasks.filter(task => task.status === 'completed')
      }
    }
  },
  methods: {
    async loadTasks() {
      try {
        const res = await this.$request.get('/volunteer/my-tasks')
        if (res.code === 200) {
          this.tasks = res.data.tasks || []
        }
      } catch (error) {
        console.error('加载任务失败:', error)
      }
    },
    async loadTextConfig() {
      try {
        const res = await this.$request.get('/volunteer/text-config')
        if (res.code === 200) {
          this.textConfig = res.data
        }
      } catch (error) {
        console.error('加载文字配置失败:', error)
      }
    },
    getStatusText(status) {
      const statusMap = {
        assigned: '已分配',
        in_progress: '进行中',
        completed: '已完成'
      }
      return statusMap[status] || status
    },
    viewDetails(task) {
      uni.navigateTo({
        url: `/pages/volunteer/order-details?id=${task.id}&type=${task.type}`
      })
    },
    async completeTask(task) {
      uni.showModal({
        title: '确认完成',
        content: '确定要完成这个任务吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const res = await this.$request.post(`/volunteer/tasks/${task.id}/complete`)
              if (res.code === 200) {
                uni.showToast({ title: '任务完成', icon: 'success' })
                this.loadTasks() // 重新加载任务列表
              }
            } catch (error) {
              console.error('完成任务失败:', error)
              uni.showToast({ title: '操作失败', icon: 'none' })
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
  min-height: 100vh;
  background-color: #f5f5f5;
}
.header {
  padding: 20rpx 0;
  margin-bottom: 20rpx;
}
.title {
  font-size: 36rpx;
  font-weight: bold;
}
.tabs {
  display: flex;
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}
.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  font-size: 32rpx;
  color: #666;
}
.tab-item.active {
  color: #007AFF;
  border-bottom: 3rpx solid #007AFF;
}
.tasks-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}
.task-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}
.task-item:last-child {
  border-bottom: none;
}
.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}
.task-title {
  font-size: 34rpx;
  font-weight: bold;
}
.task-status {
  font-size: 28rpx;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  background-color: #e6f7ff;
  color: #007AFF;
}
.task-status.status-completed {
  background-color: #f6ffed;
  color: #52c41a;
}
.task-info {
  margin-bottom: 20rpx;
}
.info-item {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 10rpx;
}
.task-actions {
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}
.detail-btn, .complete-btn {
  width: 150rpx;
  height: 60rpx;
  font-size: 28rpx;
  border-radius: 10rpx;
}
.detail-btn {
  background-color: #fff;
  color: #007AFF;
  border: 1rpx solid #007AFF;
}
.complete-btn {
  background-color: #52c41a;
  color: #fff;
}
.no-tasks {
  padding: 40rpx;
  text-align: center;
  color: #999;
}
</style>
