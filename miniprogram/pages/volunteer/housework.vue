<template>
  <view class="housework-container">
    <view class="housework-list">
      <view class="housework-item" v-for="task in houseworkTasks" :key="task.id" @click="viewTaskDetails(task)">
        <view class="task-header">
          <text class="task-title">{{ task.title }}</text>
          <text class="task-status" :class="task.status">
            {{ getStatusText(task.status) }}
          </text>
        </view>
        <view class="task-info">
          <text class="user-name">用户：{{ task.userName }}</text>
          <text class="task-date">服务日期：{{ task.date }}</text>
        </view>
        <view class="task-time">
          <text class="time-label">服务时间：</text>
          <text class="time-detail">{{ task.startTime }} - {{ task.endTime }}</text>
        </view>
        <view class="task-address">
          <text class="address-label">服务地址：</text>
          <text class="address-detail">{{ task.address }}</text>
        </view>
        <view class="task-description">
          <text class="desc-label">服务需求：</text>
          <text class="desc-detail">{{ task.description }}</text>
        </view>
        <view class="task-actions" v-if="task.status === 'pending'">
          <button class="accept-btn" @click.stop="acceptTask(task)">接受任务</button>
          <button class="reject-btn" @click.stop="rejectTask(task)">拒绝任务</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      houseworkTasks: [
        {
          id: 'H20240101001',
          title: '日常打扫',
          status: 'pending',
          userName: '张奶奶',
          date: '2024-01-02',
          startTime: '09:00',
          endTime: '11:00',
          address: '北京市朝阳区XX街道XX小区3号楼1单元101室',
          description: '需要打扫客厅、卧室和厨房，清理地面和家具表面'
        },
        {
          id: 'H20240101002',
          title: '洗衣做饭',
          status: 'pending',
          userName: '李爷爷',
          date: '2024-01-02',
          startTime: '14:00',
          endTime: '16:00',
          address: '北京市朝阳区XX街道XX小区5号楼2单元202室',
          description: '需要清洗衣物和准备晚餐'
        },
        {
          id: 'H20240101003',
          title: '整理收纳',
          status: 'accepted',
          userName: '王奶奶',
          date: '2024-01-03',
          startTime: '10:00',
          endTime: '12:00',
          address: '北京市朝阳区XX街道XX小区2号楼3单元303室',
          description: '需要整理衣柜和书架，将物品分类收纳'
        }
      ]
    };
  },
  methods: {
    viewTaskDetails(task) {
      uni.navigateTo({
        url: `/pages/volunteer/task-details?id=${task.id}`
      });
    },
    getStatusText(status) {
      const statusMap = {
        pending: '待接单',
        accepted: '已接单',
        completed: '已完成',
        rejected: '已拒绝'
      };
      return statusMap[status] || status;
    },
    acceptTask(task) {
      uni.showModal({
        title: '确认接单',
        content: '确定要接受这个家务服务任务吗？',
        success: (res) => {
          if (res.confirm) {
            task.status = 'accepted';
            uni.showToast({
              title: '接单成功',
              icon: 'success'
            });
          }
        }
      });
    },
    rejectTask(task) {
      uni.showModal({
        title: '确认拒绝',
        content: '确定要拒绝这个家务服务任务吗？',
        success: (res) => {
          if (res.confirm) {
            task.status = 'rejected';
            uni.showToast({
              title: '拒绝成功',
              icon: 'success'
            });
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.housework-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.housework-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.housework-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.housework-item:last-child {
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
  color: #333;
}

.task-status {
  font-size: 28rpx;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
}

.task-status.pending {
  background-color: #fff2cc;
  color: #ff8c00;
}

.task-status.accepted {
  background-color: #cce7ff;
  color: #007AFF;
}

.task-status.completed {
  background-color: #d4edda;
  color: #28a745;
}

.task-status.rejected {
  background-color: #f8d7da;
  color: #dc3545;
}

.task-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
  font-size: 30rpx;
  color: #666;
}

.task-time, .task-address, .task-description {
  margin-bottom: 15rpx;
}

.time-label, .address-label, .desc-label {
  font-size: 30rpx;
  color: #666;
}

.time-detail, .address-detail, .desc-detail {
  font-size: 30rpx;
  color: #333;
  line-height: 1.5;
}

.task-actions {
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}

.accept-btn {
  width: 150rpx;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}

.reject-btn {
  width: 150rpx;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #FF4444;
  color: #fff;
}
</style>
