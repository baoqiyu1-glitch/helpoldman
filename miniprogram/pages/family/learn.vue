<template>
  <view class="learn-container">
    <!-- 老人选择 -->
    <view class="elder-selection" v-if="elders.length > 0">
      <text class="section-title">选择老人</text>
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
          <button class="study-btn" @click.stop="applyLearning(course)">申请学习</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      elders: [],
      selectedElder: null,
      courseList: []
    }
  },
  onLoad() {
    this.loadElders()
    this.loadCourses()
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
    
    async loadCourses() {
      try {
        const res = await this.$request.get('/learning/courses')
        if (res.code === 200) {
          this.courseList = res.data
        }
      } catch (error) {
        console.error('加载课程列表失败:', error)
      }
    },
    
    selectElder(elder) {
      this.selectedElder = elder
    },
    
    selectCourse(course) {
      // 查看课程详情
      uni.navigateTo({
        url: `/pages/family/course-detail?id=${course.id}`
      })
    },
    
    async applyLearning(course) {
      if (!this.selectedElder) {
        uni.showToast({ title: '请先选择老人', icon: 'none' })
        return
      }
      
      const applicationData = {
        elderId: this.selectedElder.id,
        courseId: course.id,
        courseName: course.title,
        applyReason: `为${this.selectedElder.name}申请学习${course.title}课程`
      }
      
      try {
        uni.showLoading({ title: '提交申请中...' })
        const res = await this.$request.post('/family/apply-learning', applicationData)
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({ title: '学习申请提交成功' })
        } else {
          uni.showToast({ title: res.message || '申请失败', icon: 'none' })
        }
      } catch (error) {
        uni.hideLoading()
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
      }
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