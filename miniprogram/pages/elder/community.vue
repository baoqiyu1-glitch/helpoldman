<template>
  <view class="community-container">
    <view class="tab-bar">
      <view class="tab-item" :class="{ active: activeTab === 0 }" @click="switchTab(0)">互助求助</view>
      <view class="tab-item" :class="{ active: activeTab === 1 }" @click="switchTab(1)">互助帮助</view>
    </view>
    
    <view class="content" v-if="activeTab === 0">
      <button class="publish-btn" @click="publishHelp">发布求助</button>
      
      <view class="help-list">
        <view class="help-item" v-for="help in helpList" :key="help.id">
          <view class="help-header">
            <image class="avatar" :src="help.avatar" mode="aspectFill"></image>
            <view class="user-info">
              <text class="username">{{ help.username }}</text>
              <text class="time">{{ help.time }}</text>
            </view>
          </view>
          <view class="help-content">
            <text class="title">{{ help.title }}</text>
            <text class="desc">{{ help.description }}</text>
          </view>
          <view class="help-footer">
            <text class="status">{{ help.status }}</text>
            <button class="help-btn" @click="offerHelp(help)">我来帮忙</button>
          </view>
        </view>
      </view>
    </view>
    
    <view class="content" v-if="activeTab === 1">
      <view class="offer-list">
        <view class="offer-item" v-for="offer in offerList" :key="offer.id">
          <view class="offer-header">
            <image class="avatar" :src="offer.avatar" mode="aspectFill"></image>
            <view class="user-info">
              <text class="username">{{ offer.username }}</text>
              <text class="skill">{{ offer.skill }}</text>
            </view>
          </view>
          <view class="offer-content">
            <text class="desc">{{ offer.description }}</text>
            <text class="available-time">可提供帮助时间：{{ offer.availableTime }}</text>
          </view>
          <button class="contact-btn" @click="contactVolunteer(offer)">联系TA</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 0,
      helpList: [
        { 
          id: 1, 
          username: '王奶奶', 
          avatar: '../../static/images/avatar1.png', 
          time: '2小时前', 
          title: '需要帮忙买菜', 
          description: '需要有人帮忙购买一些蔬菜和水果，送到家里', 
          status: '待帮助'
        },
        { 
          id: 2, 
          username: '李爷爷', 
          avatar: '../../static/images/avatar2.png', 
          time: '5小时前', 
          title: '需要帮忙修电视', 
          description: '家里电视坏了，需要有人帮忙修理', 
          status: '已有人帮忙'
        }
      ],
      offerList: [
        { 
          id: 1, 
          username: '张志愿者', 
          avatar: '../../static/images/avatar3.png', 
          skill: '家电维修', 
          description: '擅长修理各种家用电器，免费为老人服务', 
          availableTime: '周一至周五 下午'
        },
        { 
          id: 2, 
          username: '刘志愿者', 
          avatar: '../../static/images/avatar4.png', 
          skill: '代购服务', 
          description: '可以帮忙购买生活用品，送到老人家中', 
          availableTime: '周末全天'
        }
      ]
    }
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab
    },
    publishHelp() {
      uni.navigateTo({
        url: '/pages/elder/publish-help'
      })
    },
    offerHelp(help) {
      uni.showModal({
        title: '提供帮助',
        content: `确定要帮助"${help.username}"吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '已成功提供帮助' })
          }
        }
      })
    },
    contactVolunteer(offer) {
      uni.makePhoneCall({
        phoneNumber: '13800138000' // 这里替换为实际电话号码
      })
    }
  }
}
</script>

<style scoped>
.community-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.tab-bar {
  display: flex;
  background-color: #fff;
  border-bottom: 1rpx solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
  font-size: 32rpx;
  color: #666;
  border-bottom: 4rpx solid transparent;
}

.tab-item.active {
  color: #007AFF;
  border-bottom-color: #007AFF;
}

.content {
  padding: 20rpx;
}

.publish-btn {
  width: 100%;
  height: 80rpx;
  font-size: 32rpx;
  background-color: #007AFF;
  color: #fff;
  margin-bottom: 20rpx;
}

.help-list, .offer-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.help-item, .offer-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.help-item:last-child, .offer-item:last-child {
  border-bottom: none;
}

.help-header, .offer-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.user-info {
  flex: 1;
}

.username {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 5rpx;
}

.time, .skill {
  display: block;
  font-size: 26rpx;
  color: #999;
}

.help-content, .offer-content {
  margin-bottom: 20rpx;
}

.title {
  display: block;
  font-size: 34rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  line-height: 45rpx;
}

.available-time {
  display: block;
  font-size: 28rpx;
  color: #999;
  margin-top: 10rpx;
}

.help-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status {
  padding: 5rpx 15rpx;
  background-color: #f0f0f0;
  border-radius: 15rpx;
  font-size: 26rpx;
  color: #666;
}

.help-btn, .contact-btn {
  width: 150rpx;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}
</style>