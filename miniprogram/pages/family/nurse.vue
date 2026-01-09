<template>
  <view class="nurse-container">
    <view class="nurse-list">
      <view class="nurse-item" v-for="nurse in nurseList" :key="nurse.id" @click="selectNurse(nurse)">
        <image class="nurse-image" :src="nurse.image" mode="aspectFill"></image>
        <view class="nurse-info">
          <text class="nurse-name">{{ nurse.name }}</text>
          <text class="nurse-level">{{ nurse.level }}</text>
          <text class="nurse-desc">{{ nurse.description }}</text>
          <view class="nurse-meta">
            <text class="experience">工作经验：{{ nurse.experience }}</text>
            <text class="price">¥{{ nurse.price }}/天</text>
          </view>
          <button class="book-btn" @click.stop="bookNurse(nurse)">立即预约</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      nurseList: [
        { 
          id: 1, 
          name: '张护工', 
          level: '高级护工', 
          description: '10年护理经验，擅长老人日常护理、康复护理', 
          experience: '10年', 
          price: 200,
          image: '../../static/images/nurse1.png'
        },
        { 
          id: 2, 
          name: '李护工', 
          level: '中级护工', 
          description: '5年护理经验，耐心细致，擅长与老人沟通', 
          experience: '5年', 
          price: 150,
          image: '../../static/images/nurse2.png'
        },
        { 
          id: 3, 
          name: '王护工', 
          level: '初级护工', 
          description: '2年护理经验，认真负责，学习能力强', 
          experience: '2年', 
          price: 100,
          image: '../../static/images/nurse3.png'
        }
      ]
    }
  },
  methods: {
    selectNurse(nurse) {
      // 查看护工详情
      uni.navigateTo({
        url: `/pages/family/nurse-detail?id=${nurse.id}`
      })
    },
    bookNurse(nurse) {
      uni.showModal({
        title: '预约确认',
        content: `确定要预约"${nurse.name}"护工吗？价格：¥${nurse.price}/天`,
        success: (res) => {
          if (res.confirm) {
            uni.navigateTo({
              url: `/pages/family/nurse-booking?id=${nurse.id}&name=${nurse.name}&price=${nurse.price}`
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.nurse-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.nurse-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.nurse-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.nurse-item:last-child {
  border-bottom: none;
}

.nurse-image {
  width: 100%;
  height: 300rpx;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.nurse-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 5rpx;
}

.nurse-level {
  display: block;
  font-size: 28rpx;
  color: #007AFF;
  margin-bottom: 15rpx;
}

.nurse-desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  margin-bottom: 20rpx;
  line-height: 45rpx;
}

.nurse-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.experience {
  font-size: 28rpx;
  color: #999;
}

.price {
  font-size: 34rpx;
  color: #FF4444;
  font-weight: bold;
}

.book-btn {
  width: 100%;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}
</style>