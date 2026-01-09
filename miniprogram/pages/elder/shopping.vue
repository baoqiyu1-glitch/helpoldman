<template>
  <view class="shopping-container">
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
    
    <view class="cart-bar" v-if="cartCount > 0">
      <text class="cart-count">{{ cartCount }}</text>
      <button type="primary" @click="goToCart">去结算</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchText: '',
      categories: [
        { id: 1, name: '食品' },
        { id: 2, name: '日用品' },
        { id: 3, name: '药品' },
        { id: 4, name: '其他' }
      ],
      products: [
        { id: 1, name: '大米', price: 50, image: '../../static/images/rice.png', categoryId: 1 },
        { id: 2, name: '面粉', price: 30, image: '../../static/images/flour.png', categoryId: 1 },
        { id: 3, name: '食用油', price: 80, image: '../../static/images/oil.png', categoryId: 1 },
        { id: 4, name: '洗发水', price: 45, image: '../../static/images/shampoo.png', categoryId: 2 },
        { id: 5, name: '牙膏', price: 15, image: '../../static/images/toothpaste.png', categoryId: 2 }
      ],
      cartCount: 0
    }
  },
  onLoad() {
    // 初始化数据
  },
  methods: {
    search() {
      // 搜索商品
    },
    selectCategory(category) {
      // 选择分类
    },
    addProduct(product) {
      // 添加商品到购物车
      this.cartCount++
    },
    goToCart() {
      // 跳转到购物车页面
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
