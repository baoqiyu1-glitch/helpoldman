<template>
  <view class="shopping-container">
    <view class="search-bar">
      <input type="text" :placeholder="textConfig.searchPlaceholder || '搜索需要代购的商品'" v-model="searchText" />
      <button @click="search">{{ textConfig.searchButton || '搜索' }}</button>
    </view>
    
    <view class="order-list">
      <view class="order-item" v-for="order in orders" :key="order.id" @click="viewOrderDetails(order)">
        <view class="order-header">
          <text class="order-id">{{ textConfig.orderIdText || '订单编号' }}：{{ order.id }}</text>
          <text class="order-status" :class="order.status">
            {{ getStatusText(order.status) }}
          </text>
        </view>
        <view class="order-info">
          <text class="user-name">{{ textConfig.userText || '用户' }}：{{ order.userName }}</text>
          <text class="order-time">{{ textConfig.orderTimeText || '下单时间' }}：{{ order.time }}</text>
        </view>
        <view class="order-address">
          <text class="address-label">{{ textConfig.deliveryAddressText || '配送地址' }}：</text>
          <text class="address-detail">{{ order.address }}</text>
        </view>
        <view class="product-list">
          <view class="product-item" v-for="product in order.products" :key="product.id">
            <text class="product-name">{{ product.name }} x{{ product.quantity }}</text>
            <text class="product-price">¥{{ product.price }}</text>
          </view>
        </view>
        <view class="order-total">
          <text class="total-label">{{ textConfig.totalText || '总计' }}：</text>
          <text class="total-price">¥{{ order.totalPrice }}</text>
        </view>
        <view class="order-actions" v-if="order.status === 'pending'">
          <button class="accept-btn" @click.stop="acceptOrder(order)">{{ textConfig.acceptOrderText || '接受订单' }}</button>
          <button class="reject-btn" @click.stop="rejectOrder(order)">{{ textConfig.rejectOrderText || '拒绝订单' }}</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchText: '',
      orders: [],
      textConfig: {}
    }
  },
  
  onLoad() {
    this.loadTextConfig()
    this.loadOrders()
  },
  
  methods: {
    async loadTextConfig() {
      try {
        const res = await this.$request.get('/volunteer/text-config')
        if (res.code === 200) {
          this.textConfig = res.data
          // 设置页面标题
          uni.setNavigationBarTitle({
            title: this.textConfig.shoppingPageTitle || '代购服务'
          })
        }
      } catch (error) {
        console.error('加载文字配置失败:', error)
        this.setDefaultTextConfig()
      }
    },
    
    async loadOrders() {
      try {
        console.log('开始加载代购订单数据...')
        
        // 添加分页参数
        const res = await this.$request.get('/volunteer/shopping-orders?page=1&size=10')
        console.log('代购订单接口返回:', res)
        
        if (res && res.code === 200) {
          // 修复：正确获取orders数据
          this.orders = res.data.orders || []
          console.log('加载订单成功，订单数量:', this.orders.length)
          console.log('订单数据:', this.orders)
        } else {
          console.error('接口返回错误:', res ? res.message : '接口无响应')
          // 使用默认数据
          this.setDefaultOrders()
        }
      } catch (error) {
        console.error('加载订单失败:', error)
        console.error('错误详情:', error.message)
        // 使用默认数据
        this.setDefaultOrders()
      }
    },
    
    setDefaultTextConfig() {
      this.textConfig = {
        searchPlaceholder: '搜索需要代购的商品',
        searchButton: '搜索',
        orderIdText: '订单编号',
        userText: '用户',
        orderTimeText: '下单时间',
        deliveryAddressText: '配送地址',
        totalText: '总计',
        acceptOrderText: '接受订单',
        rejectOrderText: '拒绝订单',
        shoppingPageTitle: '代购服务',
        statusTexts: {
          pending: '待接单',
          accepted: '已接单',
          completed: '已完成',
          rejected: '已拒绝'
        },
        modalTitles: {
          acceptOrder: '确认接单',
          rejectOrder: '确认拒绝'
        },
        modalContents: {
          acceptOrder: '确定要接受这个代购订单吗？',
          rejectOrder: '确定要拒绝这个代购订单吗？'
        },
        toastMessages: {
          acceptSuccess: '接单成功',
          rejectSuccess: '拒绝成功'
        }
      }
    },
    
    setDefaultOrders() {
      this.orders = [
        {
          id: 'V20240101001',
          status: 'pending',
          userName: '张奶奶',
          time: '2024-01-01 09:30',
          address: '北京市朝阳区XX街道XX小区3号楼1单元101室',
          products: [
            { id: 1, name: '大米', price: 50, quantity: 1 },
            { id: 2, name: '食用油', price: 80, quantity: 1 }
          ],
          totalPrice: 130
        },
        {
          id: 'V20240101002',
          status: 'accepted',
          userName: '李爷爷',
          time: '2024-01-01 10:15',
          address: '北京市朝阳区XX街道XX小区5号楼2单元202室',
          products: [
            { id: 3, name: '药品', price: 120, quantity: 1 },
            { id: 4, name: '日用品', price: 60, quantity: 2 }
          ],
          totalPrice: 240
        },
        {
          id: 'V20240101003',
          status: 'completed',
          userName: '王奶奶',
          time: '2024-01-01 11:45',
          address: '北京市朝阳区XX街道XX小区2号楼3单元303室',
          products: [
            { id: 5, name: '蔬菜', price: 30, quantity: 1 },
            { id: 6, name: '水果', price: 45, quantity: 1 }
          ],
          totalPrice: 75
        }
      ]
    },
    
    search() {
      // 搜索逻辑
      console.log('搜索商品:', this.searchText);
    },
    
    viewOrderDetails(order) {
      // 查看订单详情
      uni.navigateTo({
        url: `/pages/volunteer/order-details?id=${order.id}`
      });
    },
    
    getStatusText(status) {
      return this.textConfig.statusTexts?.[status] || status;
    },
    
    acceptOrder(order) {
      uni.showModal({
        title: this.textConfig.modalTitles?.acceptOrder || '确认接单',
        content: this.textConfig.modalContents?.acceptOrder || '确定要接受这个代购订单吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              // 调用后端接口接受订单
              const response = await this.$request.post(`/volunteer/shopping-orders/${order.id}/accept`);
              
              if (response && response.code === 200) {
                // 更新本地状态
                order.status = 'accepted';
                uni.showToast({
                  title: this.textConfig.toastMessages?.acceptSuccess || '接单成功',
                  icon: 'success'
                });
                // 刷新订单列表
                this.loadOrders();
              } else {
                uni.showToast({
                  title: response?.message || '接单失败',
                  icon: 'none'
                });
              }
            } catch (error) {
              console.error('接单失败:', error);
              uni.showToast({
                title: '接单失败，请重试',
                icon: 'none'
              });
            }
          }
        }
      });
    },
    
    rejectOrder(order) {
      uni.showModal({
        title: this.textConfig.modalTitles?.rejectOrder || '确认拒绝',
        content: this.textConfig.modalContents?.rejectOrder || '确定要拒绝这个代购订单吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              // 调用后端接口拒绝订单
              const response = await this.$request.post(`/volunteer/shopping-orders/${order.id}/reject`);
              
              if (response && response.code === 200) {
                // 更新本地状态
                order.status = 'rejected';
                uni.showToast({
                  title: this.textConfig.toastMessages?.rejectSuccess || '拒绝成功',
                  icon: 'success'
                });
                // 刷新订单列表
                this.loadOrders();
              } else {
                uni.showToast({
                  title: response?.message || '拒绝失败',
                  icon: 'none'
                });
              }
            } catch (error) {
              console.error('拒绝订单失败:', error);
              uni.showToast({
                title: '拒绝失败，请重试',
                icon: 'none'
              });
            }
          }
        }
      });
    }
  }
}
</script>

<style scoped>
.shopping-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.search-bar {
  display: flex;
  margin-bottom: 20rpx;
}

.search-bar input {
  flex: 1;
  height: 80rpx;
  border: 1rpx solid #ddd;
  border-radius: 10rpx 0 0 10rpx;
  padding: 0 20rpx;
  background-color: #fff;
}

.search-bar button {
  width: 150rpx;
  height: 80rpx;
  border-radius: 0 10rpx 10rpx 0;
  background-color: #007AFF;
  color: #fff;
}

.order-list {
  background-color: #fff;
  border-radius: 10rpx;
  overflow: hidden;
}

.order-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.order-id {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.order-status {
  font-size: 28rpx;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
}

.order-status.pending {
  background-color: #fff2cc;
  color: #ff8c00;
}

.order-status.accepted {
  background-color: #cce7ff;
  color: #007AFF;
}

.order-status.completed {
  background-color: #d4edda;
  color: #28a745;
}

.order-status.rejected {
  background-color: #f8d7da;
  color: #dc3545;
}

.order-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
  font-size: 30rpx;
  color: #666;
}

.order-address {
  margin-bottom: 15rpx;
}

.address-label {
  font-size: 30rpx;
  color: #666;
}

.address-detail {
  font-size: 30rpx;
  color: #333;
  line-height: 1.5;
}

.product-list {
  margin-bottom: 15rpx;
}

.product-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10rpx;
  font-size: 30rpx;
  color: #333;
}

.order-total {
  display: flex;
  justify-content: flex-end;
  font-size: 34rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.total-label {
  color: #666;
  margin-right: 10rpx;
}

.total-price {
  color: #FF4444;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 20rpx;
}

.accept-btn {
  width: 150rpx;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}

.reject-btn {
  width: 150rpx;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #FF4444;
  color: #fff;
}
</style>