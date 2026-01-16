<template>
  <view class="volunteer-container">
    <!-- 加载状态 -->
    <view v-if="loading" class="loading-container">
      <view class="loading-spinner"></view>
      <text class="loading-text">加载中...</text>
    </view>
    
    <!-- 错误状态 -->
    <view v-else-if="error" class="error-container">
      <image class="error-icon" src="/static/icons/error.png" mode="aspectFit"></image>
      <text class="error-text">加载失败，请检查网络连接</text>
      <button class="retry-button" @click="retryLoad">重试</button>
    </view>
    
    <!-- 正常内容 -->
    <view v-else>
      <!-- 用户信息区域 -->
      <view class="user-info-section">
        <view class="user-avatar">
          <image :src="dashboardData.userInfo.avatar || '/static/images/avatar-default.png'" mode="aspectFill"></image>
        </view>
        <view class="user-details">
          <text class="username">{{ dashboardData.userInfo.username || '志愿者' }}</text>
          <text class="user-role">{{ textConfig.roleText || '志愿者' }}</text>
        </view>
      </view>

      <!-- 服务统计区域 -->
      <view class="stats-section">
        <view class="stat-item">
          <text class="stat-value">{{ dashboardData.stats.totalHours || 0 }}</text>
          <text class="stat-label">{{ textConfig.serviceHoursText || '服务时长' }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ dashboardData.stats.completedTasks || 0 }}</text>
          <text class="stat-label">{{ textConfig.completedTasksText || '完成任务' }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ dashboardData.stats.currentTasks || 0 }}</text>
          <text class="stat-label">{{ textConfig.currentTasksText || '进行中' }}</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ dashboardData.stats.rating || 0 }}</text>
          <text class="stat-label">{{ textConfig.ratingText || '评分' }}</text>
        </view>
      </view>

      <!-- 快速服务入口 -->
      <!-- <view class="quick-services-section">
        <text class="section-title">{{ textConfig.quickServicesText || '快速服务' }}</text>
        
        <view v-if="dashboardData.quickServices && dashboardData.quickServices.length > 0" class="services-grid">
                    <view v-for="(service, index) in dashboardData.quickServices" :key="index" 
                class="service-item" @click="navigateToService(service.code)">
            <image :src="service.icon" mode="aspectFit" class="service-icon"></image>
            <text class="service-name">{{ service.name }}</text>
          </view>
        </view>
        <view v-else class="no-services">
          <text>暂无服务数据</text>
        </view>
      </view> -->
      <view class="quick-services-section">
  <text class="section-title">{{ textConfig.quickServicesText || '快速服务' }}</text>
  <view class="services-grid">
    <view class="service-item" @click="navigateToService('shopping')">
      <image src="/static/images/shopping.png" mode="aspectFit" class="service-icon"></image>
      <text class="service-name">代购物资</text>
    </view>
    <view class="service-item" @click="navigateToService('housework')">
      <image src="/static/images/housework.png" mode="aspectFit" class="service-icon"></image>
      <text class="service-name">协助家务</text>
    </view>
    <view class="service-item" @click="navigateToService('my-tasks')">
      <image src="/static/images/tasks.png" mode="aspectFit" class="service-icon"></image>
      <text class="service-name">我的任务</text>
    </view>
    <view class="service-item" @click="navigateToService('training')">
      <image src="/static/images/training.png" mode="aspectFit" class="service-icon"></image>
      <text class="service-name">培训技能</text>
    </view>
  </view>
</view>

      <!-- 培训课程区域 -->
      <view class="training-courses-section">
        <view class="section-header">
          <text class="section-title">{{ (textConfig.trainingTexts && textConfig.trainingTexts.courseList) || '培训课程' }}</text>
          <text class="view-all" @click="navigateToTraining">查看全部</text>
        </view>
        <view class="courses-list">
          <view v-if="dashboardData.courses && dashboardData.courses.length > 0">
<view v-for="course in dashboardData.courses.slice(0, 3)" :key="course.id" class="course-item">
              <view class="course-info">
                <text class="course-title">{{ course.title }}</text>
                <view class="course-details">
                  <text class="course-instructor">
{{ (textConfig.trainingTexts && textConfig.trainingTexts.instructor) || '讲师' }}: {{ course.instructor }}
                  </text>
                  <text class="course-duration">
                    {{ (textConfig.trainingTexts && textConfig.trainingTexts.duration) || '时长' }}: {{ course.duration }}
                  </text>
                </view>
                <view class="course-stats">
                  <text class="registered-count">
                    {{ (textConfig.trainingTexts && textConfig.trainingTexts.registeredCount) || '已报名' }}: {{ course.registeredCount || 0 }}
                  </text>
                  <text class="course-capacity">
                    {{ (textConfig.trainingTexts && textConfig.trainingTexts.capacity) || '容量' }}: {{ course.capacity || 0 }}
                  </text>
                </view>
              </view>
              <button class="register-btn" @click="registerCourse(course.id)">
                {{ (textConfig.trainingTexts && textConfig.trainingTexts.registerBtn) || '报名' }}
              </button>
            </view>
          </view>
          <view v-else class="no-courses">
            <text>{{ (textConfig.trainingTexts && textConfig.trainingTexts.noCourses) || '暂无培训课程' }}</text>
          </view>
        </view>
      </view>

      <!-- 最近任务 -->
      <view class="recent-tasks-section">
        <text class="section-title">{{ textConfig.recentTasksText || '最近任务' }}</text>
        <view class="tasks-list">
          <view v-for="task in dashboardData.tasks" :key="task.id" class="task-item">
            <view class="task-info">
              <text class="task-title">{{ task.title }}</text>
              <text class="task-type">{{ textConfig.serviceTexts && textConfig.serviceTexts[task.type] || task.type }}</text>
              <text class="task-date">{{ task.date }}</text>
            </view>
            <view class="task-status" :class="{
              'status-pending': task.status === 'pending',
              'status-in-progress': task.status === 'in_progress',
              'status-completed': task.status === 'completed'
            }">
              {{ textConfig.taskStatusTexts && textConfig.taskStatusTexts[task.status] || task.status }}
            </view>
          </view>
        </view>
      </view>

      <!-- 自定义底部导航 -->
      <view class="custom-tabbar">
        <view class="tab-item" :class="{ active: currentTab === 'index' }" @click="switchTab('index')">
          <image class="tab-icon" src="/static/icons/home.png" mode="aspectFit"></image>
          <text class="tab-text">首页</text>
        </view>
        <view class="tab-item" :class="{ active: currentTab === 'services' }" @click="switchTab('services')">
          <image class="tab-icon" src="/static/icons/services.png" mode="aspectFit"></image>
          <text class="tab-text">服务</text>
        </view>
        <view class="tab-item" :class="{ active: currentTab === 'messages' }" @click="switchTab('messages')">
          <image class="tab-icon" src="/static/icons/messages.png" mode="aspectFit"></image>
          <text class="tab-text">消息</text>
        </view>
        <view class="tab-item" :class="{ active: currentTab === 'profile' }" @click="switchTab('profile')">
          <image class="tab-icon" src="/static/icons/profile.png" mode="aspectFit"></image>
          <text class="tab-text">我的</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'index',
      loading: false,
      error: false,
      dashboardData: {
        userInfo: {},
        stats: {},
        quickServices: [],
        tasks: [],
        courses: [] // 添加培训课程数据字段
      },
      textConfig: {}
    }
  },
  
  onLoad() {
    this.loadVolunteerData()
  },
  
  onShow() {
    this.loadVolunteerData()
  },
  
  methods: {
    async loadVolunteerData() {
  try {
    this.loading = true
    this.error = false
    
    console.log('开始请求志愿者数据...')
    
    // 获取页面文字配置
    const textConfigRes = await this.$request.get('/volunteer/text-config')
    console.log('text-config接口返回:', textConfigRes)
    
    if (textConfigRes && textConfigRes.code === 200) {
      this.textConfig = textConfigRes.data
    }
    
    // 获取首页数据 - 修复响应处理逻辑
    console.log('开始请求dashboard接口...')
    const dashboardRes = await this.$request.get('/volunteer/dashboard')
    console.log('dashboard接口返回完整响应:', dashboardRes)
    
    // 修复：正确提取data字段
    if (dashboardRes && dashboardRes.code === 200) {
      console.log('接口返回成功，开始处理数据...')
      const dashboardData = dashboardRes.data
      console.log('dashboard接口返回数据:', dashboardData)
      console.log('dashboard接口返回的quickServices:', dashboardData.quickServices)
      console.log('dashboard接口返回的quickServices长度:', dashboardData.quickServices ? dashboardData.quickServices.length : 0)
      
      // 正确使用data字段中的数据
      if (dashboardData.userInfo) {
        this.dashboardData.userInfo = dashboardData.userInfo
        console.log('用户信息已赋值')
      }
      if (dashboardData.stats) {
        this.dashboardData.stats = dashboardData.stats
        console.log('统计数据已赋值')
      }
      if (dashboardData.quickServices) {
        // 确保数组被正确赋值
        this.dashboardData.quickServices = [...dashboardData.quickServices]
        console.log('quickServices已赋值:', this.dashboardData.quickServices)
        console.log('quickServices长度:', this.dashboardData.quickServices.length)
      } else {
        console.warn('接口返回的quickServices为空或未定义')
      }
      if (dashboardData.tasks) {
        this.dashboardData.tasks = dashboardData.tasks
        console.log('任务数据已赋值')
      }
      if (dashboardData.courses) {
        this.dashboardData.courses = dashboardData.courses
        console.log('课程数据已赋值')
      }
      
      console.log('最终dashboardData:', this.dashboardData)
      console.log('最终quickServices数据:', this.dashboardData.quickServices)
      console.log('最终quickServices长度:', this.dashboardData.quickServices ? this.dashboardData.quickServices.length : 0)
    } else {
      console.error('接口返回数据为空或状态码错误:', dashboardRes ? dashboardRes.code : '无响应')
    }
    
    // 设置页面标题
    if (this.textConfig && this.textConfig.pageTitle) {
      uni.setNavigationBarTitle({
        title: this.textConfig.pageTitle
      })
    }
    
    this.loading = false
    
  } catch (error) {
    console.error('加载志愿者数据失败:', error)
    console.error('错误详情:', error.message)
    console.error('错误堆栈:', error.stack)
    this.loading = false
    this.error = true
  }
},
    
    retryLoad() {
      this.loadVolunteerData()
    },
    
        navigateToService(code) {
      const routes = {
        shopping: '/pages/volunteer/shopping',
        housework: '/pages/volunteer/housework',
        training: '/pages/volunteer/training',
        apply: '/pages/volunteer/apply',
        'my-tasks': '/pages/volunteer/my-tasks'
      }
      
      if (routes[code]) {
        uni.navigateTo({
          url: routes[code]
        })
      }
    },
    
    navigateToTraining() {
      uni.navigateTo({
        url: '/pages/volunteer/training'
      })
    },
    
    async registerCourse(courseId) {
      try {
        const res = await this.$request.post(`/volunteer/training/${courseId}/register`)
        if (res.code === 200) {
          uni.showToast({
            title: (this.textConfig.trainingTexts && this.textConfig.trainingTexts.registerSuccess) || '报名成功',
            icon: 'success'
          })
          // 刷新数据
          this.loadVolunteerData()
        } else {
          uni.showToast({
            title: res.message || '报名失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('报名失败:', error)
        uni.showToast({
          title: '报名失败，请重试',
          icon: 'none'
        })
      }
    },
    
    switchTab(tab) {
      this.currentTab = tab
      const routes = {
        index: '/pages/volunteer/index',
        services: '/pages/volunteer/services',
        messages: '/pages/volunteer/messages',
        profile: '/pages/volunteer/profile'
      }
      
      if (routes[tab]) {
        uni.redirectTo({
          url: routes[tab]
        })
      }
    }
  }
}
</script>

<style>
.volunteer-container {
  padding: 30rpx 30rpx 130rpx 30rpx; /* 增加底部内边距，避免内容被底部导航遮挡 */
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 用户信息区域 */
.user-info-section {
  background: linear-gradient(135deg, #007AFF, #5AC8FA);
  border-radius: 20rpx;
  padding: 40rpx;
  color: white;
  margin-bottom: 30rpx;
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.user-avatar image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}
.user-role {
  font-size: 28rpx;
  opacity: 0.9;
}

/* 服务统计区域 */
.stats-section {
  display: flex;
  justify-content: space-around;
  margin-bottom: 30rpx;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 48rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.stat-label {
  font-size: 24rpx;
  opacity: 0.9;
}

/* 快速服务入口 */
.quick-services-section {
  background-color: white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  min-height: 300rpx; /* 确保有足够高度 */
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30rpx;
}

.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx;
  background-color: #f8f8f8;
  border-radius: 15rpx;
  border: 1rpx solid #e0e0e0;
  min-height: 120rpx; /* 确保服务项有足够高度 */
  justify-content: center; /* 垂直居中 */
}

.service-icon {
  width: 80rpx;
  height: 80rpx;
  margin-bottom: 15rpx;
  display: block; /* 确保图片显示 */
}

.service-name {
  font-size: 28rpx;
  color: #333;
  text-align: center;
  font-weight: 500; /* 增加字体粗细 */
}

.no-services {
  text-align: center;
  padding: 20rpx;
  color: #999;
  font-size: 28rpx;
}

/* 最近任务 */
.recent-tasks-section {
  background-color: white;
  border-radius: 20rpx;
  padding: 30rpx;
}

.tasks-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: #f8f8f8;
  border-radius: 10rpx;
}

.task-info {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.task-title {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 5rpx;
}

.task-type, .task-date {
  font-size: 24rpx;
  color: #666;
}

.task-status {
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  white-space: nowrap;
}

.status-pending {
  background-color: #FF9500;
  color: white;
}

.status-in-progress {
  background-color: #007AFF;
  color: white;
}

.status-completed {
  background-color: #4CD964;
  color: white;
}

/* 自定义底部导航 */
.custom-tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #FFFFFF;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1rpx solid #eeeeee;
  z-index: 999;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
}

.tab-item.active {
  color: #007AFF;
}

.tab-icon {
  width: 40rpx;
  height: 40rpx;
  margin-bottom: 8rpx;
}

.tab-text {
  font-size: 20rpx;
}

/* 培训课程区域 */
.training-courses-section {
  background: white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.view-all {
  color: #007AFF;
  font-size: 28rpx;
}

.courses-list {
  margin-top: 20rpx;
}

.course-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.course-item:last-child {
  border-bottom: none;
}

.course-info {
  flex: 1;
}

.course-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
  display: block;
}

.course-details {
  margin-bottom: 10rpx;
}

.course-instructor, .course-duration {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 5rpx;
}

.course-stats {
  display: flex;
  gap: 20rpx;
}

.registered-count, .course-capacity {
  font-size: 24rpx;
  color: #999;
}

.register-btn {
  background: #007AFF;
  color: white;
  border: none;
  border-radius: 10rpx;
  padding: 10rpx 20rpx;
  font-size: 24rpx;
  min-width: 100rpx;
}

.no-courses {
  text-align: center;
  padding: 40rpx;
  color: #999;
  font-size: 28rpx;
}

/* 加载状态样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
  padding: 100rpx;
}

.loading-spinner {
  width: 80rpx;
  height: 80rpx;
  border: 8rpx solid #f3f3f3;
  border-top: 8rpx solid #007AFF;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 30rpx;
}

.loading-text {
  font-size: 28rpx;
  color: #666;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 错误状态样式 */
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
  padding: 100rpx;
}

.error-icon {
  width: 120rpx;
  height: 120rpx;
  margin-bottom: 30rpx;
}

.error-text {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 40rpx;
  text-align: center;
}

.retry-button {
  background-color: #007AFF;
  color: white;
  border: none;
  border-radius: 10rpx;
  padding: 20rpx 40rpx;
  font-size: 28rpx;
}

.retry-button:active {
  background-color: #0056CC;
}
</style>