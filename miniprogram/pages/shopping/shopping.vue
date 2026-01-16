// ... existing code ...
submitDemand() {
    // 验证表单数据
    if (!this.validateForm()) {
        return;
    }
    
    // 显示加载中
    uni.showLoading({
        title: '发布中...'
    });
    
    // 调用后端API
    try {
        // 这里需要根据您的实际API调用方式修改
        const res = await this.$api.shoppingService.applyService({
            title: this.title,
            content: this.content,
            address: this.address,
            contactPhone: this.contactPhone,
            serviceTime: this.serviceTime,
            serviceTypeId: 5 // 代购服务
        });
        
        // 修复：正确的响应检查逻辑
        if (res && res.code === 200) {
            uni.hideLoading();
            uni.showToast({
                title: res.message || '代购需求发布成功',
                icon: 'success',
                duration: 2000
            });
            
            // 发布成功后返回上一页
            setTimeout(() => {
                uni.navigateBack();
            }, 1500);
        } else {
            uni.hideLoading();
            uni.showToast({
                title: res?.message || '发布失败，请稍后重试',
                icon: 'none',
                duration: 3000
            });
        }
    } catch (error) {
        uni.hideLoading();
        console.error('发布代购需求失败:', error);
        
        // 网络错误处理
        if (error.errMsg && error.errMsg.includes('request:fail')) {
            uni.showToast({
                title: '网络连接失败，请检查网络设置',
                icon: 'none',
                duration: 3000
            });
        } else {
            uni.showToast({
                title: '发布失败，请稍后重试',
                icon: 'none',
                duration: 3000
            });
        }
    }
},
// ... existing code ...