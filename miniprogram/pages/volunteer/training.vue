<template>
  <view class="training-container">
    <view class="loading" v-if="loading">加载中...</view>
    <view class="error" v-if="error">{{ error }}</view>
    
    <view class="page-header" v-if="!loading && !error">
      <text class="title">{{ (textConfig.trainingTexts && textConfig.trainingTexts.pageTitle) || '培训学习' }}</text>
    </view>
    
    <view class="training-list" v-if="!loading && !error">
      <view v-if="trainingCourses.length === 0" class="no-courses">
        <text>{{ (textConfig.trainingTexts && textConfig.trainingTexts.noCourses) || '暂无培训课程' }}</text>
      </view>
      
      <view class="training-item" v-for="course in trainingCourses" :key="course.id" @click="viewCourseDetails(course)">
        <view class="course-header">
          <text class="course-title">{{ course.title }}</text>
          <text class="course-status" :class="{
            'ongoing': getCourseStatus(course) === 'ongoing',
            'upcoming': getCourseStatus(course) === 'upcoming',
            'completed': getCourseStatus(course) === 'completed'
          }">
            {{ getStatusText(getCourseStatus(course)) }}
          </text>
        </view>
        <view class="course-info">
          <text class="course-desc">{{ course.description }}</text>
        </view>
        <view class="course-meta">
          <text class="time">{{ (textConfig.trainingTexts && textConfig.trainingTexts.time) || '时间' }}：{{ course.time }}</text>
          <text class="location">{{ (textConfig.trainingTexts && textConfig.trainingTexts.location) || '地点' }}：{{ course.location }}</text>
        </view>
        <view class="course-details">
          <text class="instructor">{{ (textConfig.trainingTexts && textConfig.trainingTexts.instructor) || '讲师' }}：{{ course.instructor }}</text>
          <text class="duration">{{ (textConfig.trainingTexts && textConfig.trainingTexts.duration) || '时长' }}：{{ course.duration }}分钟</text>
        </view>
        <view class="course-capacity">
          <text class="capacity">{{ (textConfig.trainingTexts && textConfig.trainingTexts.capacity) || '容量' }}：{{ course.capacity }}人</text>
          <text class="registered">{{ (textConfig.trainingTexts && textConfig.trainingTexts.registeredCount) || '已报名' }}：{{ course.registeredCount || 0 }}人</text>
        </view>
        <view class="course-actions">
          <button class="register-btn" @click.stop="registerCourse(course)" v-if="getCourseStatus(course) === 'ongoing'">
            {{ (textConfig.trainingTexts && textConfig.trainingTexts.registerBtn) || '报名' }}
          </button>
          <text class="status-text" :class="{
            'ongoing': getCourseStatus(course) === 'ongoing',
            'upcoming': getCourseStatus(course) === 'upcoming',
            'completed': getCourseStatus(course) === 'completed'
          }" v-else>
            {{ getStatusText(getCourseStatus(course)) }}
          </text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      trainingCourses: [],
      textConfig: {},
      loading: false,
      error: ''
    };
  },
  onLoad() {
    this.loadTextConfig();
    this.loadTrainingCourses();
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
          trainingTexts: {
            pageTitle: '培训学习',
            courseList: '课程列表',
            noCourses: '暂无培训课程',
            registerBtn: '报名',
            registerSuccess: '报名成功',
            courseDetails: '课程详情',
            instructor: '讲师',
            duration: '时长',
            capacity: '容量',
            registeredCount: '已报名',
            time: '时间',
            location: '地点'
          }
        };
      }
    },
    
    async loadTrainingCourses() {
      this.loading = true;
      this.error = '';
      
      try {
        const token = uni.getStorageSync('token');
        const response = await uni.request({
          url: 'http://localhost:8080/api/volunteer/training-courses',
          method: 'GET',
          header: {
            'Authorization': token ? `Bearer ${token}` : ''
          }
        });
        
        if (response[1].statusCode === 200 && response[1].data.code === 200) {
          this.trainingCourses = response[1].data.data || [];
        } else {
          throw new Error(response[1].data.msg || '加载课程列表失败');
        }
      } catch (error) {
        console.error('加载培训课程失败:', error);
        this.error = '加载课程列表失败，请稍后重试';
        
        // 设置默认数据
        this.trainingCourses = [
          {
            id: 'C001',
            title: '老年护理基础知识',
            description: '学习老年人日常护理的基本知识和技巧',
            time: '2024-01-20 09:00',
            location: '线上直播',
            instructor: '张医生',
            duration: 120,
            capacity: 50,
            registeredCount: 0
          },
          {
            id: 'C002',
            title: '心理健康疏导',
            description: '了解老年人心理特点，学习心理疏导方法',
            time: '2024-01-22 14:00',
            location: '线下培训',
            instructor: '李心理咨询师',
            duration: 90,
            capacity: 30,
            registeredCount: 0
          },
          {
            id: 'C003',
            title: '急救技能培训',
            description: '掌握基本的急救知识和操作技能',
            time: '2024-01-25 10:00',
            location: '线下实操',
            instructor: '王护士长',
            duration: 180,
            capacity: 20,
            registeredCount: 0
          }
        ];
      } finally {
        this.loading = false;
      }
    },
    
    getCourseStatus(course) {
      // 根据课程时间判断状态
      const now = new Date();
      const courseTime = new Date(course.time);
      
      if (courseTime > now) {
        return 'ongoing';
      } else {
        return 'completed';
      }
    },
    
    viewCourseDetails(course) {
      uni.showModal({
        title: (this.textConfig.trainingTexts && this.textConfig.trainingTexts.courseDetails) || '课程详情',
        content: `课程名称：${course.title}\n课程描述：${course.description}\n时间：${course.time}\n地点：${course.location}\n讲师：${course.instructor}\n时长：${course.duration}分钟\n容量：${course.capacity}人\n已报名：${course.registeredCount || 0}人`,
        showCancel: false
      });
    },
    
    getStatusText(status) {
      const statusMap = {
        ongoing: '可报名',
        upcoming: '即将开始',
        completed: '已结束'
      };
      return statusMap[status] || status;
    },
    
    async registerCourse(course) {
      uni.showModal({
        title: '报名确认',
        content: `确定要报名参加"${course.title}"吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const token = uni.getStorageSync('token');
              const response = await uni.request({
                url: `http://localhost:8080/api/volunteer/training-courses/${course.id}/register`,
                method: 'POST',
                header: {
                  'Authorization': token ? `Bearer ${token}` : ''
                }
              });
              
              if (response[1].statusCode === 200 && response[1].data.code === 200) {
                course.registeredCount = (course.registeredCount || 0) + 1;
                uni.showToast({
                  title: (this.textConfig.trainingTexts && this.textConfig.trainingTexts.registerSuccess) || '报名成功',
                  icon: 'success'
                });
              } else {
                throw new Error(response[1].data.msg || '报名失败');
              }
            } catch (error) {
              console.error('报名失败:', error);
              uni.showToast({
                title: '报名失败，请稍后重试',
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
.training-container {
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

.training-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.no-courses {
  text-align: center;
  padding: 60rpx 20rpx;
  font-size: 32rpx;
  color: #999;
}

.training-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.training-item:last-child {
  border-bottom: none;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.course-title {
  font-size: 34rpx;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.course-status {
  font-size: 28rpx;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
}

.course-status.ongoing {
  background-color: #cce7ff;
  color: #007AFF;
}

.course-status.upcoming {
  background-color: #e6f7ff;
  color: #52c41a;
}

.course-status.completed {
  background-color: #f5f5f5;
  color: #999;
}

.course-info {
  margin-bottom: 15rpx;
}

.course-desc {
  font-size: 30rpx;
  color: #666;
  line-height: 1.5;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10rpx;
  font-size: 28rpx;
  color: #999;
}

.course-details {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10rpx;
  font-size: 28rpx;
  color: #666;
}

.course-capacity {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
  font-size: 28rpx;
  color: #666;
}

.course-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.register-btn {
  width: 200rpx;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 10rpx;
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
  color: #52c41a;
}

.status-text.completed {
  background-color: #f5f5f5;
  color: #999;
}
</style>