<template>
  <view class="housework-container">
    <view class="loading" v-if="loading">加载中...</view>
    <view class="error" v-if="error">{{ error }}</view>
    
    <view class="page-header" v-if="!loading && !error">
      <text class="title">{{ (textConfig.houseworkTexts && textConfig.houseworkTexts.pageTitle) || '家政服务' }}</text>
    </view>
    
    <view class="housework-list" v-if="!loading && !error">
      <view v-if="houseworkTasks.length === 0" class="no-tasks">
        <text>{{ (textConfig.houseworkTexts && textConfig.houseworkTexts.noTasks) || '暂无家政服务任务' }}</text>
      </view>
      
      <view class="housework-item" v-for="task in houseworkTasks" :key="task.id" @click="viewTaskDetails(task)">
        <view class="task-header">
          <text class="task-title">{{ task.taskType }}</text>
          <text class="task-status" :class="{
            'pending': task.status === 'pending',
            'accepted': task.status === 'accepted',
            'completed': task.status === 'completed',
            'rejected': task.status === 'rejected'
          }">
            {{ getStatusText(task.status) }}
          </text>
        </view>
        <view class="task-info">
          <text class="user-name">用户：{{ task.userName }}</text>
          <text class="task-time">服务时间：{{ task.time }}</text>
        </view>
        <view class="task-address">
          <text class="address-label">服务地址：</text>
          <text class="address-detail">{{ task.address }}</text>
        </view>
        <view class="task-description">
          <text class="desc-label">服务需求：</text>
          <text class="desc-detail">{{ task.description }}</text>
        </view>
        <view class="task-estimated">
          <text class="estimated-label">预计时长：</text>
          <text class="estimated-detail">{{ task.estimatedHours }}小时</text>
        </view>
        <view class="task-actions" v-if="task.status === 'pending'">
          <button class="accept-btn" @click.stop="acceptTask(task)">
            {{ (textConfig.houseworkTexts && textConfig.houseworkTexts.acceptBtn) || '接单' }}
          </button>
          <button class="reject-btn" @click.stop="rejectTask(task)">
            {{ (textConfig.houseworkTexts && textConfig.houseworkTexts.rejectBtn) || '拒单' }}
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      houseworkTasks: [],
      textConfig: {},
      loading: false,
      error: ''
    };
  },
  onLoad() {
    this.loadTextConfig();
    this.loadHouseworkTasks();
  },
  methods: {
    async loadTextConfig() {
      try {
        const response = await uni.request({
          url: 'http://localhost:8080/api/volunteer/text-config',
          method: 'GET'
        });
        
        if (response[1].statusCode === 200 && response[1].data.code === 200) {
          this.textConfig = response[1].data.data || {};
        }
      } catch (error) {
        console.error('加载文字配置失败:', error);
        // 设置默认文字配置
        this.textConfig = {
          houseworkTexts: {
            pageTitle: '家政服务',
            taskList: '任务列表',
            noTasks: '暂无家政服务任务',
            acceptBtn: '接单',
            rejectBtn: '拒单',
            acceptSuccess: '接单成功',
            rejectSuccess: '拒单成功',
            taskDetails: '任务详情'
          }
        };
      }
    },
    
    async loadHouseworkTasks() {
      this.loading = true;
      this.error = '';
      
      try {
        const token = uni.getStorageSync('token');
        const response = await uni.request({
          url: 'http://localhost:8080/api/volunteer/housework-tasks',
          method: 'GET',
          header: {
            'Authorization': token ? `Bearer ${token}` : ''
          }
        });
        
        if (response[1].statusCode === 200 && response[1].data.code === 200) {
          this.houseworkTasks = response[1].data.data || [];
        } else {
          throw new Error(response[1].data.msg || '加载任务列表失败');
        }
      } catch (error) {
        console.error('加载家政服务任务失败:', error);
        this.error = '加载任务列表失败，请稍后重试';
        
        // 设置默认数据
        this.houseworkTasks = [
          {
            id: 'T001',
            status: 'pending',
            userName: '张奶奶',
            taskType: '家庭清洁',
            address: '北京市朝阳区XX街道XX小区3号楼1单元101室',
            time: '2024-01-15 14:00',
            description: '需要打扫客厅和厨房，大约2小时',
            estimatedHours: 2
          },
          {
            id: 'T002',
            status: 'pending',
            userName: '李爷爷',
            taskType: '衣物整理',
            address: '北京市朝阳区XX街道XX小区5号楼2单元202室',
            time: '2024-01-16 10:00',
            description: '帮助整理衣柜和收纳衣物',
            estimatedHours: 1.5
          },
          {
            id: 'T003',
            status: 'pending',
            userName: '王奶奶',
            taskType: '厨房清洁',
            address: '北京市朝阳区XX街道XX小区2号楼3单元303室',
            time: '2024-01-17 15:30',
            description: '深度清洁厨房，包括油烟机和灶台',
            estimatedHours: 2.5
          }
        ];
      } finally {
        this.loading = false;
      }
    },
    
    viewTaskDetails(task) {
      uni.showModal({
        title: (this.textConfig.houseworkTexts && this.textConfig.houseworkTexts.taskDetails) || '任务详情',
        content: `任务类型：${task.taskType}\n用户：${task.userName}\n地址：${task.address}\n时间：${task.time}\n需求：${task.description}\n预计时长：${task.estimatedHours}小时`,
        showCancel: false
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
    
    async acceptTask(task) {
      uni.showModal({
        title: '确认接单',
        content: '确定要接受这个家政服务任务吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const token = uni.getStorageSync('token');
              const response = await uni.request({
                url: `http://localhost:8080/api/volunteer/housework-tasks/${task.id}/accept`,
                method: 'POST',
                header: {
                  'Authorization': token ? `Bearer ${token}` : ''
                }
              });
              
              if (response[1].statusCode === 200 && response[1].data.code === 200) {
                task.status = 'accepted';
                uni.showToast({
                  title: (this.textConfig.houseworkTexts && this.textConfig.houseworkTexts.acceptSuccess) || '接单成功',
                  icon: 'success'
                });
              } else {
                throw new Error(response[1].data.msg || '接单失败');
              }
            } catch (error) {
              console.error('接单失败:', error);
              uni.showToast({
                title: '接单失败，请稍后重试',
                icon: 'error'
              });
            }
          }
        }
      });
    },
    
    async rejectTask(task) {
      uni.showModal({
        title: '确认拒绝',
        content: '确定要拒绝这个家政服务任务吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const token = uni.getStorageSync('token');
              const response = await uni.request({
                url: `http://localhost:8080/api/volunteer/housework-tasks/${task.id}/reject`,
                method: 'POST',
                header: {
                  'Authorization': token ? `Bearer ${token}` : ''
                }
              });
              
              if (response[1].statusCode === 200 && response[1].data.code === 200) {
                task.status = 'rejected';
                uni.showToast({
                  title: (this.textConfig.houseworkTexts && this.textConfig.houseworkTexts.rejectSuccess) || '拒单成功',
                  icon: 'success'
                });
              } else {
                throw new Error(response[1].data.msg || '拒单失败');
              }
            } catch (error) {
              console.error('拒单失败:', error);
              uni.showToast({
                title: '拒单失败，请稍后重试',
                icon: 'error'
              });
            }
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

.loading, .error {
  text-align: center;
  padding: 40rpx;
  font-size: 32rpx;
  color: #666;
}

.error {
  color: #ff4444;
}

.page-header {
  text-align: center;
  margin-bottom: 30rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.housework-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.no-tasks {
  text-align: center;
  padding: 60rpx 20rpx;
  font-size: 32rpx;
  color: #999;
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

.task-address, .task-description, .task-estimated {
  margin-bottom: 15rpx;
}

.address-label, .desc-label, .estimated-label {
  font-size: 30rpx;
  color: #666;
  margin-right: 10rpx;
}

.address-detail, .desc-detail, .estimated-detail {
  font-size: 30rpx;
  color: #333;
}

.task-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 20rpx;
}

.accept-btn, .reject-btn {
  flex: 1;
  height: 70rpx;
  font-size: 30rpx;
  border-radius: 10rpx;
}

.accept-btn {
  background-color: #007AFF;
  color: white;
  border: none;
}

.reject-btn {
  background-color: #ff4444;
  color: white;
  border: none;
}
</style>