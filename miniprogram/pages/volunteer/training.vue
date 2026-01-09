<template>
  <view class="training-container">
    <view class="training-list">
      <view class="training-item" v-for="training in trainingCourses" :key="training.id" @click="viewTrainingDetails(training)">
        <image class="training-image" :src="training.image" mode="aspectFill"></image>
        <view class="training-info">
          <text class="training-title">{{ training.title }}</text>
          <text class="training-desc">{{ training.description }}</text>
          <view class="training-meta">
            <text class="time">{{ training.time }}</text>
            <text class="location">{{ training.location }}</text>
          </view>
          <text class="trainer">培训老师：{{ training.trainer }}</text>
          <view class="training-actions">
            <button class="apply-btn" @click.stop="applyTraining(training)" v-if="training.status === 'ongoing'">
              报名参加
            </button>
            <text class="status-text" :class="training.status" v-else>
              {{ getStatusText(training.status) }}
            </text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      trainingCourses: [
        {
          id: 1,
          title: '老年人沟通技巧培训',
          description: '学习与老年人有效沟通的技巧，了解老年人心理特点，提升服务质量',
          time: '每周一、三 下午2:00-4:00',
          location: '社区活动中心',
          trainer: '王老师',
          status: 'ongoing',
          image: '../../static/images/communication.png'
        },
        {
          id: 2,
          title: '急救技能培训',
          description: '学习基本急救知识和技能，包括心肺复苏、止血包扎等，提高应急处理能力',
          time: '每周五 上午9:00-11:00',
          location: '社区卫生服务站',
          trainer: '李医生',
          status: 'ongoing',
          image: '../../static/images/first-aid.png'
        },
        {
          id: 3,
          title: '无障碍服务技能培训',
          description: '学习如何为行动不便的老年人提供无障碍服务，包括辅助器具使用、安全护理等',
          time: '每周六 下午2:00-4:00',
          location: '社区文化站',
          trainer: '张老师',
          status: 'upcoming',
          image: '../../static/images/barrier-free.png'
        },
        {
          id: 4,
          title: '心理健康辅导培训',
          description: '学习基本的心理健康辅导知识，帮助老年人缓解孤独、焦虑等心理问题',
          time: '已结束',
          location: '社区心理咨询室',
          trainer: '刘老师',
          status: 'completed',
          image: '../../static/images/mental-health.png'
        }
      ]
    };
  },
  methods: {
    viewTrainingDetails(training) {
      uni.navigateTo({
        url: `/pages/volunteer/training-details?id=${training.id}`
      });
    },
    getStatusText(status) {
      const statusMap = {
        ongoing: '进行中',
        upcoming: '即将开始',
        completed: '已结束'
      };
      return statusMap[status] || status;
    },
    applyTraining(training) {
      uni.showModal({
        title: '报名确认',
        content: `确定要报名参加"${training.title}"吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '报名中...'
            });
            setTimeout(() => {
              uni.hideLoading();
              uni.showToast({
                title: '报名成功',
                icon: 'success'
              });
            }, 1000);
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.training-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.training-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.training-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.training-item:last-child {
  border-bottom: none;
}

.training-image {
  width: 100%;
  height: 250rpx;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.training-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
  color: #333;
}

.training-desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 20rpx;
  line-height: 1.5;
}

.training-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
  font-size: 28rpx;
  color: #999;
}

.trainer {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 20rpx;
}

.training-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.apply-btn {
  width: 200rpx;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}

.status-text {
  font-size: 30rpx;
  padding: 10rpx 20rpx;
  border-radius: 20rpx;
}

.status-text.ongoing {
  background-color: #cce7ff;
  color: #007AFF;
}

.status-text.upcoming {
  background-color: #e6f7ff;
  color: #00bfff;
}

.status-text.completed {
  background-color: #d4edda;
  color: #28a745;
}
</style>
