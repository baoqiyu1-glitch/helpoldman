<template>
  <view class="container">
    <view class="header">
      <text class="title">订单详情</text>
    </view>
    <view class="order-info" v-if="order">
      <view class="info-section">
        <text class="section-title">基本信息</text>
        <view class="info-item">
          <text class="label">订单编号：</text>
          <text class="value">{{ order.id }}</text>
        </view>
        <view class="info-item">
          <text class="label">用户：</text>
          <text class="value">{{ order.userName }}</text>
        </view>
        <view class="info-item">
          <text class="label">下单时间：</text>
          <text class="value">{{ order.time }}</text>
        </view>
        <view class="info-item">
          <text class="label">地址：</text>
          <text class="value">{{ order.address }}</text>
        </view>
        <view class="info-item">
          <text class="label">状态：</text>
          <text class="value status-text" :class="`status-${order.status}`">
            {{ getStatusText(order.status) }}
          </text>
        </view>
      </view>
      <view v-if="order.products && order.products.length > 0" class="info-section">
        <text class="section-title">商品清单</text>
        <view class="products-list">
          <view v-for="(product, index) in order.products" :key="index" class="product-item">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-price">¥{{ product.price }} x {{ product.quantity }}</text>
          </view>
        </view>
        <view class="total">
          <text class="total-label">总计：</text>
          <text class="total-value">¥{{ order.totalPrice }}</text>
        </view>
      </view>
      <view v-if="order.description" class="info-section">
        <text class="section-title">服务内容</text>
        <text class="description">{{ order.description }}</text>
      </view>
      <view class="actions" v-if="order.status === 'in_progress'">
        <button class="complete-btn" @click="completeOrder">完成订单</button>
      </view>
    </view>
    <view v-else class="loading">
      <text>加载中...</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      order: null,
      orderId: '',
      textConfig: {}
    }
  },
  onLoad(options) {
    this.orderId = options.id
    this.loadOrderDetails()
    this.loadTextConfig()
  },
  methods: {
    async loadOrderDetails() {
      try {
        const res = await this.$request.get(`/volunteer/order-details?id=${this.orderId}`)
        if (res.code === 200) {
          this.order = res.data
        }
      } catch (error) {
        console.error('加载订单详情失败:', error)
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },
    async loadTextConfig() {
      try {
        const res = await this.$request.get('/volunteer/text-config')
        if (res.code === 200) {
          this.textConfig = res.data
        }
      } catch (error) {
        console.error('加载文字配置失败:', error)
      }
    },
    getStatusText(status) {
      const statusMap = {
        pending: '待接单',
        accepted: '已接单',
        in_progress: '进行中',
        completed: '已完成',
        rejected: '已拒绝'
      }
      return statusMap[status] || status
    },
    async completeOrder() {
      uni.showModal({
        title: '确认完成',
        content: '确定要完成这个订单吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              const res = await this.$request.post(`/volunteer/orders/${this.orderId}/complete`)
              if (res.code === 200) {
                uni.showToast({ title: '完成成功', icon: 'success' })
                this.order.status = 'completed'
                setTimeout(() => {
                  uni.navigateBack()
                }, 1500)
              }
            } catch (error) {
              console.error('完成订单失败:', error)
              uni.showToast({ title: '操作失败', icon: 'none' })
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
  min-height: 100vh;
  background-color: #f5f5f5;
}
.header {
  padding: 20rpx 0;
  margin-bottom: 20rpx;
}
.title {
  font-size: 36rpx;
  font-weight: bold;
}
.order-info {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}
.info-section {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}
.info-section:last-child {
  border-bottom: none;
}
.section-title {
  font-size: 34rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  display: block;
}
.info-item {
  display: flex;
  margin-bottom: 15rpx;
}
.label {
  font-size: 32rpx;
  color: #666;
  width: 150rpx;
}
.value {
  font-size: 32rpx;
  color: #333;
  flex: 1;
}
.status-text {
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
}
.status-pending {
  background-color: #fffbe6;
  color: #faad14;
}
.status-accepted, .status-in_progress {
  background-color: #e6f7ff;
  color: #007AFF;
}
.status-completed {
  background-color: #f6ffed;
  color: #52c41a;
}
.status-rejected {
  background-color: #fff1f0;
  color: #ff4d4f;
}
.products-list {
  margin-bottom: 20rpx;
}
.product-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
  padding: 15rpx;
  background-color: #f9f9f9;
  border-radius: 5rpx;
}
.product-name {
  font-size: 30rpx;
  color: #333;
}
.product-price {
  font-size: 30rpx;
  color: #007AFF;
}
.total {
  display: flex;
  justify-content: flex-end;
  font-size: 32rpx;
  font-weight: bold;
}
.total-label {
  color: #666;
}
.total-value {
  color: #ff4d4f;
  margin-left: 10rpx;
}
.description {
  font-size: 30rpx;
  color: #333;
  line-height: 1.5;
}
.actions {
  padding: 20rpx;
  text-align: center;
}
.complete-btn {
  width: 100%;
  height: 80rpx;
  font-size: 34rpx;
  background-color: #52c41a;
  color: #fff;
  border-radius: 10rpx;
}
.loading {
  padding: 40rpx;
  text-align: center;
  color: #999;
}
</style>
