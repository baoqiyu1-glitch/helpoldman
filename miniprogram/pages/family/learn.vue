<template>
  <view class="learn-container">
    <view class="course-list">
      <view class="course-item" v-for="course in courseList" :key="course.id" @click="selectCourse(course)">
        <image class="course-image" :src="course.image" mode="aspectFill"></image>
        <view class="course-info">
          <text class="course-title">{{ course.title }}</text>
          <text class="course-desc">{{ course.description }}</text>
          <view class="course-meta">
            <text class="teacher">讲师：{{ course.teacher }}</text>
            <text class="duration">时长：{{ course.duration }}</text>
          </view>
          <button class="study-btn" @click.stop="studyCourse(course)">开始学习</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      courseList: [
        { 
          id: 1, 
          title: '老人护理基础知识', 
          description: '学习老人日常护理的基本技能和注意事项', 
          teacher: '李护士', 
          duration: '45分钟',
          image: '../../static/images/nursing.png'
        },
        { 
          id: 2, 
          title: '常见疾病应急处理', 
          description: '学习高血压、糖尿病等常见疾病的应急处理方法', 
          teacher: '王医生', 
          duration: '60分钟',
          image: '../../static/images/emergency.png'
        },
        { 
          id: 3, 
          title: '心理疏导技巧', 
          description: '学习如何与老人进行有效沟通和心理疏导', 
          teacher: '张心理师', 
          duration: '50分钟',
          image: '../../static/images/psychology.png'
        }
      ]
    }
  },
  methods: {
    selectCourse(course) {
      // 查看课程详情
      uni.navigateTo({
        url: `/pages/family/course-detail?id=${course.id}`
      })
    },
    studyCourse(course) {
      // 开始学习
      uni.navigateTo({
        url: `/pages/family/study?id=${course.id}`
      })
    }
  }
}
</script>

<style scoped>
.learn-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.course-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.course-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.course-item:last-child {
  border-bottom: none;
}

.course-image {
  width: 100%;
  height: 300rpx;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.course-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.course-desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 20rpx;
  line-height: 45rpx;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.teacher, .duration {
  font-size: 28rpx;
  color: #999;
}

.study-btn {
  width: 100%;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}
</style>