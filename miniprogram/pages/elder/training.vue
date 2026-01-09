<template>
  <view class="training-container">
    <view class="training-list">
      <view class="training-item" v-for="training in trainingList" :key="training.id" @click="selectTraining(training)">
        <image class="training-image" :src="training.image" mode="aspectFill"></image>
        <view class="training-info">
          <text class="training-title">{{ training.title }}</text>
          <text class="training-desc">{{ training.description }}</text>
          <view class="training-meta">
            <text class="time">{{ training.time }}</text>
            <text class="location">{{ training.location }}</text>
          </view>
          <button class="apply-btn" @click.stop="applyTraining(training)">立即报名</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      trainingList: [
        { 
          id: 1, 
          title: '智能手机使用培训', 
          description: '学习智能手机基本操作，包括打电话、发微信、看视频等', 
          time: '每周一、三、五 上午9:00-11:00', 
          location: '社区活动中心',
          image: '../../static/images/smartphone.png'
        },
        { 
          id: 2, 
          title: '健康养生知识讲座', 
          description: '学习健康饮食、合理运动、常见疾病预防等养生知识', 
          time: '每周二、四 下午2:00-4:00', 
          location: '社区卫生服务站',
          image: '../../static/images/health.png'
        },
        { 
          id: 3, 
          title: '手工制作课程', 
          description: '学习编织、剪纸、绘画等手工技能，丰富晚年生活', 
          time: '每周六 上午9:30-11:30', 
          location: '社区文化站',
          image: '../../static/images/handcraft.png'
        }
      ]
    }
  },
  methods: {
    selectTraining(training) {
      // 查看培训详情
      uni.navigateTo({
        url: `/pages/elder/training-detail?id=${training.id}`
      })
    },
    applyTraining(training) {
      uni.showModal({
        title: '报名确认',
        content: `确定要报名参加"${training.title}"吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '报名中...' })
            // 这里替换为实际API调用
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({ title: '报名成功' })
            }, 1000)
          }
        }
      })
    }
  }
}
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
  height: 300rpx;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.training-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.training-desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 20rpx;
  line-height: 45rpx;
}

.training-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.time, .location {
  font-size: 28rpx;
  color: #999;
}

.apply-btn {
  width: 100%;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}
</style>