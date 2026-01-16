<template>
  <view class="shopping-container">
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

    <view class="search-bar">
      <input type="text" placeholder="搜索需要代购的商品" v-model="searchText" />
      <button @click="searchProducts">搜索</button>
    </view>
    
    <view class="category-list">
      <view class="category-item" 
            v-for="category in categories" 
            :key="category.id" 
            :class="{ active: selectedCategory && selectedCategory.id === category.id }"
            @click="selectCategory(category)">
        <text>{{ category.name }}</text>
      </view>
    </view>
    
    <view class="product-list">
      <view class="product-item" v-for="product in filteredProducts" :key="product.id">
        <image class="product-image" :src="product.image || '/static/default-product.png'" mode="aspectFill"></image>
        <view class="product-info">
          <text class="product-name">{{ product.name }}</text>
          <text class="product-price">¥{{ product.price }}</text>
          <button class="add-btn" @click="addProduct(product)">加入购物车</button>
        </view>
      </view>
    </view>
    
    <view class="cart-bar" v-if="cartItems.length > 0">
      <text class="cart-count">{{ cartItems.length }}</text>
      <button type="primary" @click="submitShoppingRequest">提交代购申请</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      elders: [],
      selectedElder: null,
      searchText: '',
      categories: [],
      selectedCategory: null,
      products: [],
      cartItems: []
    }
  },
  computed: {
    filteredProducts() {
      let filtered = this.products
      
      // 按分类筛选
      if (this.selectedCategory) {
        filtered = filtered.filter(product => product.categoryId === this.selectedCategory.id)
      }
      
      // 按搜索关键词筛选
      if (this.searchText) {
        filtered = filtered.filter(product => 
          product.name.toLowerCase().includes(this.searchText.toLowerCase())
        )
      }
      
      return filtered
    }
  },
  onLoad() {
    this.loadElders()
    this.loadCategories()
    this.loadProducts()
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
    
    async loadCategories() {
      try {
        const res = await this.$request.get('/product/categories')
        if (res.code === 200) {
          this.categories = res.data
        }
      } catch (error) {
        console.error('加载商品分类失败:', error)
      }
    },
    
    async loadProducts() {
      try {
        const res = await this.$request.get('/products')
        if (res.code === 200) {
          this.products = res.data
        }
      } catch (error) {
        console.error('加载商品列表失败:', error)
      }
    },
    
    selectElder(elder) {
      this.selectedElder = elder
    },
    
    selectCategory(category) {
      this.selectedCategory = this.selectedCategory && this.selectedCategory.id === category.id ? null : category
    },
    
    searchProducts() {
      // 搜索逻辑已在computed中实现
    },
    
    addProduct(product) {
      const existingItem = this.cartItems.find(item => item.id === product.id)
      if (existingItem) {
        existingItem.quantity++
      } else {
        this.cartItems.push({
          ...product,
          quantity: 1
        })
      }
      uni.showToast({ title: '已加入购物车', icon: 'success' })
    },
    
    async submitShoppingRequest() {
      if (!this.selectedElder) {
        uni.showToast({ title: '请选择老人', icon: 'none' })
        return
      }
      
      if (this.cartItems.length === 0) {
        uni.showToast({ title: '请添加商品到购物车', icon: 'none' })
        return
      }
      
      const shoppingData = {
        elderId: this.selectedElder.id,
        items: this.cartItems,
        totalAmount: this.cartItems.reduce((total, item) => total + (item.price * item.quantity), 0),
        deliveryAddress: this.selectedElder.address || '默认地址'
      }
      
      try {
        uni.showLoading({ title: '提交代购申请中...' })
        const res = await this.$request.post('/family/apply-service', {
          ...shoppingData,
          serviceType: 'shopping'  // 服务类型标识
        })
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({ title: '代购申请提交成功' })
          this.cartItems = []
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
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
.shopping-container {
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

.category-item.active {
  background-color: #007AFF;
  color: #fff;
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