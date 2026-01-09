<template>
  <view class="apply-container">
    <view class="page-header">
      <text class="title">志愿者申请</text>
    </view>
    
    <view class="form-container">
      <view class="form-item">
        <text class="label">姓名</text>
        <input v-model="form.name" placeholder="请输入姓名" />
      </view>
      
      <view class="form-item">
        <text class="label">性别</text>
        <picker mode="selector" :range="genders" v-model="form.genderIndex" @change="onGenderChange">
          <view class="picker">
            {{ genders[form.genderIndex] }}
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">年龄</text>
        <input v-model="form.age" type="number" placeholder="请输入年龄" />
      </view>
      
      <view class="form-item">
        <text class="label">联系电话</text>
        <input v-model="form.phone" type="number" placeholder="请输入联系电话" />
      </view>
      
      <view class="form-item">
        <text class="label">邮箱</text>
        <input v-model="form.email" placeholder="请输入邮箱" />
      </view>
      
      <view class="form-item">
        <text class="label">居住地址</text>
        <input v-model="form.address" placeholder="请输入详细居住地址" />
      </view>
      
      <view class="form-item">
        <text class="label">擅长领域</text>
        <view class="skills-container">
          <view class="skill-item" v-for="skill in skills" :key="skill.id" @click="toggleSkill(skill.id)">
            <text :class="{ active: selectedSkills.includes(skill.id) }">{{ skill.name }}</text>
          </view>
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">可服务时间</text>
        <textarea v-model="form.availableTime" placeholder="请描述您的可服务时间" style="height: 120rpx;"></textarea>
      </view>
      
      <view class="form-item">
        <text class="label">个人简介</text>
        <textarea v-model="form.introduction" placeholder="请简要介绍自己" style="height: 150rpx;"></textarea>
      </view>
      
      <button type="primary" @click="submitApplication">提交申请</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      genders: ['男', '女'],
      skills: [
        { id: 1, name: '代购' },
        { id: 2, name: '家务' },
        { id: 3, name: '护理' },
        { id: 4, name: '教学' },
        { id: 5, name: '维修' },
        { id: 6, name: '心理疏导' }
      ],
      form: {
        name: '',
        genderIndex: 0,
        age: '',
        phone: '',
        email: '',
        address: '',
        availableTime: '',
        introduction: ''
      },
      selectedSkills: []
    }
  },
  methods: {
    onGenderChange(e) {
      this.form.genderIndex = e.detail.value
    },
    toggleSkill(skillId) {
      const index = this.selectedSkills.indexOf(skillId)
      if (index > -1) {
        this.selectedSkills.splice(index, 1)
      } else {
        this.selectedSkills.push(skillId)
      }
    },
    submitApplication() {
      // 表单验证
      if (!this.form.name || !this.form.age || !this.form.phone || !this.form.address) {
        uni.showToast({
          title: '请填写基本信息',
          icon: 'none'
        })
        return
      }
      
      if (this.selectedSkills.length === 0) {
        uni.showToast({
          title: '请选择至少一项擅长领域',
          icon: 'none'
        })
        return
      }
      
      // 模拟提交
      uni.showLoading({
        title: '提交中...'
      })
      
      // 实际项目中，这里应该调用API提交申请
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '申请提交成功',
          icon: 'success'
        })
        
        // 重置表单
        this.form = {
          name: '',
          genderIndex: 0,
          age: '',
          phone: '',
          email: '',
          address: '',
          availableTime: '',
          introduction: ''
        }
        this.selectedSkills = []
      }, 1500)
    }
  }
}
</script>

<style scoped>
.apply-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20rpx;
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

.form-container {
  background-color: #fff;
  border-radius: 10rpx;
  padding: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  display: block;
  font-size: 32rpx;
  color: #666;
  margin-bottom: 15rpx;
}

input, textarea {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
}

.picker {
  width: 100%;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 5rpx;
  font-size: 32rpx;
  background-color: #f9f9f9;
}

.skills-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.skill-item {
  padding: 15rpx 30rpx;
  border: 2rpx solid #ddd;
  border-radius: 25rpx;
}

.skill-item text {
  font-size: 28rpx;
  color: #666;
}

.skill-item text.active {
  color: #007AFF;
  font-weight: bold;
}

.skill-item.active {
  border-color: #007AFF;
  background-color: #f0f8ff;
}

button {
  width: 100%;
  height: 90rpx;
  font-size: 34rpx;
  border-radius: 10rpx;
}
</style>
