<template>
  <view class="shopping-container">
    <!-- 代购需求发布区域 -->
    <view class="demand-section">
      <view class="section-title">
        <text>发布代购需求</text>
      </view>
      <view class="demand-form">
        <view class="form-item">
          <text class="label">代购物品</text>
          <textarea placeholder="请详细描述需要代购的物品清单" v-model="demandForm.serviceContent" style="height: 120rpx;"></textarea>
        </view>
        <view class="form-item">
          <text class="label">期望送达时间</text>
          <picker mode="date" :value="demandForm.appointmentTime" @change="onDateChange">
            <view class="picker">{{ demandForm.appointmentTime || '请选择日期' }}</view>
          </picker>
        </view>
        <view class="form-item">
          <text class="label">送达地址</text>
          <input type="text" placeholder="请输入详细地址" v-model="demandForm.address" />
        </view>
        <view class="form-item">
          <text class="label">联系电话</text>
          <input type="text" placeholder="请输入联系电话" v-model="demandForm.contactPhone" />
        </view>
        <view class="form-item">
          <text class="label">备注信息</text>
          <textarea placeholder="请输入特殊要求或备注" v-model="demandForm.remarks" style="height: 80rpx;"></textarea>
        </view>
        <button type="primary" @click="submitDemand">发布代购需求</button>
      </view>
    </view>

    <!-- 商品浏览区域（保留原有功能） -->
    <view class="browse-section">
      <view class="section-title">
        <text>商品浏览</text>
      </view>
      <view class="search-bar">
        <input type="text" placeholder="搜索需要代购的商品" v-model="searchText" />
        <button @click="search">搜索</button>
      </view>
      
      <view class="category-list">
        <view class="category-item" v-for="category in categories" :key="category.id" @click="selectCategory(category)">
          <text>{{ category.name }}</text>
        </view>
      </view>
      
      <view class="product-list">
        <view class="product-item" v-for="product in products" :key="product.id">
          <image class="product-image" :src="product.image" mode="aspectFill"></image>
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-price">¥{{ product.price }}</text>
            <button class="add-btn" @click="addProduct(product)">加入购物车</button>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { shoppingService } from '../../utils/api'

export default {
  data() {
    return {
      // 代购需求表单数据
      demandForm: {
        serviceContent: '',
        appointmentTime: '',
        address: '',
        contactPhone: '',
        remarks: ''
      },
      // 原有商品浏览数据
      searchText: '',
      categories: [],
      products: [],
      cartCount: 0
    }
  },
  onLoad() {
    this.loadShoppingData()
    // 初始化默认日期为今天
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    this.demandForm.appointmentTime = `${year}-${month}-${day}`
  },
  methods: {
    async loadShoppingData() {
      try {
        uni.showLoading({ title: '加载中...' })
        // 加载商品分类
        const categoryRes = await shoppingService.getCategories()
        if (categoryRes.code === 200) {
          this.categories = categoryRes.data
        }
        
        // 加载商品列表
        const productRes = await shoppingService.getProducts()
        if (productRes.code === 200) {
          this.products = productRes.data
        }
      } catch (error) {
        console.error('加载商品数据失败:', error)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    
    // 代购需求相关方法
    onDateChange(e) {
      this.demandForm.appointmentTime = e.detail.value
    },
    
    async submitDemand() {
      if (!this.validateDemandForm()) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      const demandData = {
        serviceTypeId: 5, // 代购服务的类型ID应该是5
        serviceContent: this.demandForm.serviceContent + (this.demandForm.remarks ? `\n备注：${this.demandForm.remarks}` : ''),
        appointmentTime: this.demandForm.appointmentTime,
        address: this.demandForm.address,
        contactPhone: this.demandForm.contactPhone
      }
      
      try {
        uni.showLoading({ title: '发布中...' })
        console.log('发送代购申请数据:', demandData)
        
        const response = await shoppingService.applyService(demandData)
        console.log('代购申请接口返回:', response) 
        
        // 修复：正确解析响应结构
        if (response && response.code === 200) {
          // 成功处理
          uni.showToast({
            title: response.message || '代购需求发布成功',
            icon: 'success',
            duration: 2000
          });
          
          // 发布成功后延迟返回上一页
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          // 失败处理
          uni.showToast({
            title: response?.message || '发布失败',
            icon: 'none',
            duration: 3000
          });
        }
      } catch (error) {
        console.error('发布代购需求失败:', error)
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    
    validateDemandForm() {
      return this.demandForm.serviceContent && 
             this.demandForm.appointmentTime && 
             this.demandForm.address && 
             this.demandForm.contactPhone
    },
    
    resetDemandForm() {
      this.demandForm = {
        serviceContent: '',
        appointmentTime: '',
        address: '',
        contactPhone: '',
        remarks: ''
      }
      // 重新设置默认日期
      const today = new Date()
      const year = today.getFullYear()
      const month = String(today.getMonth() + 1).padStart(2, '0')
      const day = String(today.getDate()).padStart(2, '0')
      this.demandForm.appointmentTime = `${year}-${month}-${day}`
    },
    
    // 原有商品浏览方法
    search() {
      // 搜索商品
    },
    selectCategory(category) {
      // 选择分类
    },
    addProduct(product) {
      // 添加商品到购物车
      this.cartCount++
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

/* 代购需求区域样式 */
.demand-section {
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.section-title {
  padding: 20rpx;
  background-color: #f8f8f8;
  border-bottom: 1rpx solid #eee;
  font-size: 32rpx;
  font-weight: bold;
}

.demand-form {
  padding: 20rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  margin-bottom: 10rpx;
  color: #333;
}

.demand-form input,
.demand-form textarea {
  width: 100%;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 20rpx;
  font-size: 28rpx;
  background-color: #fafafa;
}

.picker {
  width: 100%;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 20rpx;
  font-size: 28rpx;
  background-color: #fafafa;
}

.demand-form button {
  width: 100%;
  height: 80rpx;
  font-size: 32rpx;
  background-color: #007AFF;
  color: #fff;
  border-radius: 10rpx;
}

/* 商品浏览区域样式 */
.browse-section {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 20rpx;
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

.category-list {
  display: flex;
  overflow-x: scroll;
  margin-bottom: 20rpx;
  background-color: #fff;
  padding: 20rpx 0;
  border-radius: 10rpx;
}

.category-item {
  padding: 20rpx 30rpx;
  margin: 0 10rpx;
  background-color: #f0f0f0;
  border-radius: 20rpx;
  white-space: nowrap;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.product-item {
  width: 48%;
  background-color: #fff;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 200rpx;
}

.product-info {
  padding: 20rpx;
}

.product-name {
  display: block;
  font-size: 32rpx;
  margin-bottom: 10rpx;
  height: 70rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-price {
  display: block;
  font-size: 36rpx;
  color: #FF4444;
  margin-bottom: 20rpx;
}

.add-btn {
  width: 100%;
  height: 60rpx;
  font-size: 28rpx;
  background-color: #007AFF;
  color: #fff;
}
</style>

.cart-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 30rpx;
  border-top: 1rpx solid #eee;
}

.cart-count {
  background-color: #FF4444;
  color: #fff;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  margin-right: 20rpx;
}

.cart-bar button {
  width: 200rpx;
  height: 70rpx;
  font-size: 32rpx;
}
</style>