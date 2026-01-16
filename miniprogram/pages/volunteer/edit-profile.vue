<template>
  <view class="edit-profile-container">
    <view class="loading" v-if="loading">加载中...</view>
    <view class="error" v-if="error">{{ error }}</view>
    
    <view class="page-header" v-if="!loading && !error">
      <text class="title">{{ textConfig.editProfileTitle || '编辑资料' }}</text>
    </view>
    
    <view class="form-container" v-if="!loading && !error">
      <view class="form-item">
        <text class="label">{{ textConfig.nameLabel || '姓名' }}</text>
        <input v-model="form.name" :placeholder="textConfig.namePlaceholder || '请输入姓名'" />
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.genderLabel || '性别' }}</text>
        <picker mode="selector" :range="genders" v-model="form.genderIndex" @change="onGenderChange">
          <view class="picker">
            {{ genders[form.genderIndex] }}
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.ageLabel || '年龄' }}</text>
        <input v-model="form.age" type="number" :placeholder="textConfig.agePlaceholder || '请输入年龄'" />
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.phoneLabel || '联系电话' }}</text>
        <input v-model="form.phone" type="number" :placeholder="textConfig.phonePlaceholder || '请输入联系电话'" />
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.emailLabel || '邮箱' }}</text>
        <input v-model="form.email" :placeholder="textConfig.emailPlaceholder || '请输入邮箱'" />
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.addressLabel || '居住地址' }}</text>
        <input v-model="form.address" :placeholder="textConfig.addressPlaceholder || '请输入详细居住地址'" />
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.skillsLabel || '擅长领域' }}</text>
        <view class="skills-container">
          <view class="skill-item" v-for="skill in skills" :key="skill.id" 
                :class="{ active: selectedSkills.includes(skill.id) }" 
                @click="toggleSkill(skill.id)">
            <text :class="{ active: selectedSkills.includes(skill.id) }">{{ skill.name }}</text>
          </view>
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.availableTimeLabel || '可服务时间' }}</text>
        <textarea v-model="form.availableTime" :placeholder="textConfig.availableTimePlaceholder || '请描述您的可服务时间'" style="height: 120rpx;"></textarea>
      </view>
      
      <view class="form-item">
        <text class="label">{{ textConfig.introductionLabel || '个人简介' }}</text>
        <textarea v-model="form.introduction" :placeholder="textConfig.introductionPlaceholder || '请简要介绍自己'" style="height: 150rpx;"></textarea>
      </view>
      
      <button type="primary" @click="updateProfile">{{ textConfig.updateBtnText || '保存修改' }}</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      genders: ['男', '女'],
      skills: [],
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
      selectedSkills: [],
      textConfig: {},
      loading: false,
      error: '',
      userInfo: {}
    }
  },
  onLoad() {
    this.loadTextConfig();
    this.loadUserInfo();
    this.loadSkills();
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
          editProfileTitle: '编辑资料',
          nameLabel: '姓名',
          namePlaceholder: '请输入姓名',
          genderLabel: '性别',
          ageLabel: '年龄',
          agePlaceholder: '请输入年龄',
          phoneLabel: '联系电话',
          phonePlaceholder: '请输入联系电话',
          emailLabel: '邮箱',
          emailPlaceholder: '请输入邮箱',
          addressLabel: '居住地址',
          addressPlaceholder: '请输入详细居住地址',
          skillsLabel: '擅长领域',
          availableTimeLabel: '可服务时间',
          availableTimePlaceholder: '请描述您的可服务时间',
          introductionLabel: '个人简介',
          introductionPlaceholder: '请简要介绍自己',
          updateBtnText: '保存修改',
          updateSuccessText: '资料更新成功'
        };
      }
    },
    
    async loadUserInfo() {
      this.loading = true;
      try {
        const token = uni.getStorageSync('token');
        const response = await uni.request({
          url: 'http://localhost:8080/api/volunteer/profile',
          method: 'GET',
          header: {
            'Authorization': token ? `Bearer ${token}` : ''
          }
        });
        
        if (response[1].statusCode === 200 && response[1].data.code === 200) {
          this.userInfo = response[1].data.data || {};
          // 填充表单数据
          this.form.name = this.userInfo.name || '';
          this.form.age = this.userInfo.age || '';
          this.form.phone = this.userInfo.phone || '';
          this.form.email = this.userInfo.email || '';
          this.form.address = this.userInfo.address || '';
          this.form.availableTime = this.userInfo.availableTime || '';
          this.form.introduction = this.userInfo.introduction || '';
          
          // 设置性别
          if (this.userInfo.gender) {
            this.form.genderIndex = this.genders.indexOf(this.userInfo.gender);
            if (this.form.genderIndex === -1) this.form.genderIndex = 0;
          }
          
          // 设置技能
          if (this.userInfo.skills && Array.isArray(this.userInfo.skills)) {
            this.selectedSkills = this.userInfo.skills.map(skillName => {
              const skill = this.skills.find(s => s.name === skillName);
              return skill ? skill.id : null;
            }).filter(id => id !== null);
          }
        }
      } catch (error) {
        console.error('加载用户信息失败:', error);
        this.error = '加载用户信息失败，请稍后重试';
      } finally {
        this.loading = false;
      }
    },
    
    async loadSkills() {
      try {
        const response = await this.$request.get('/volunteer/skills');
        
        if (response && response.code === 200) {
          this.skills = response.data || [];
        } else {
          throw new Error('加载技能列表失败');
        }
      } catch (error) {
        console.error('加载技能列表失败:', error);
        // 设置默认技能列表
        this.skills = [
          { id: 1, name: '代购' },
          { id: 2, name: '家务' },
          { id: 3, name: '护理' },
          { id: 4, name: '教学' },
          { id: 5, name: '维修' },
          { id: 6, name: '心理疏导' }
        ];
      }
    },
    
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
    
    async updateProfile() {
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
      
      this.loading = true;
      
      try {
        const token = uni.getStorageSync('token');
        const response = await uni.request({
          url: 'http://localhost:8080/api/volunteer/update-profile',
          method: 'PUT',
          header: {
            'Authorization': token ? `Bearer ${token}` : '',
            'Content-Type': 'application/json'
          },
          data: {
            name: this.form.name,
            gender: this.genders[this.form.genderIndex],
            age: parseInt(this.form.age),
            phone: this.form.phone,
            email: this.form.email,
            address: this.form.address,
            skills: this.selectedSkills.map(skillId => {
              const skill = this.skills.find(s => s.id === skillId);
              return skill ? skill.name : '';
            }).filter(name => name),
            availableTime: this.form.availableTime,
            introduction: this.form.introduction
          }
        });
        
        if (response[1].statusCode === 200 && response[1].data.code === 200) {
          uni.showToast({
            title: this.textConfig.updateSuccessText || '资料更新成功',
            icon: 'success'
          });
          
          // 返回上一页
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          throw new Error(response[1].data.msg || '更新资料失败');
        }
      } catch (error) {
        console.error('更新资料失败:', error);
        uni.showToast({
          title: '更新资料失败，请稍后重试',
          icon: 'error'
        });
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
.edit-profile-container {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20rpx;
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
  cursor: pointer;
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
