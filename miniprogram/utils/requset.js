const config = require('../config')

class Request {
    constructor() {
        this.baseURL = config.baseURL
        this.token = uni.getStorageSync('token')
    }

    setToken(token) {
        this.token = token
        uni.setStorageSync('token', token)
    }

    removeToken() {
        this.token = null
        uni.removeStorageSync('token')
    }

    async request(options) {
        const { url, method = 'GET', data = {}, header = {} } = options

        // 拼接完整URL
        const fullURL = this.baseURL + url

        // 设置请求头
        const headers = {
            'Content-Type': 'application/json',
            ...header
        }

        // 添加token
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`
        }

        try {
            const response = await uni.request({
                url: fullURL,
                method,
                data,
                header: headers
            })

            const [error, res] = response

            if (error) {
                uni.showToast({
                    title: '网络请求失败',
                    icon: 'none'
                })
                return Promise.reject(error)
            }

            const { statusCode, data: result } = res

            if (statusCode === 401) {
                // token过期或无效
                this.removeToken()
                uni.showToast({
                    title: '登录已过期，请重新登录',
                    icon: 'none'
                })
                uni.navigateTo({
                    url: '/pages/auth/login'
                })
                return Promise.reject(new Error('登录已过期'))
            }

            if (statusCode !== 200) {
                uni.showToast({
                    title: result.message || '请求失败',
                    icon: 'none'
                })
                return Promise.reject(new Error(result.message || '请求失败'))
            }

            if (result.code !== 200) {
                uni.showToast({
                    title: result.message || '操作失败',
                    icon: 'none'
                })
                return Promise.reject(new Error(result.message || '操作失败'))
            }

            return result.data
        } catch (error) {
            uni.showToast({
                title: '请求失败',
                icon: 'none'
            })
            return Promise.reject(error)
        }
    }

    get(url, data = {}, header = {}) {
        return this.request({
            url,
            method: 'GET',
            data,
            header
        })
    }

    post(url, data = {}, header = {}) {
        return this.request({
            url,
            method: 'POST',
            data,
            header
        })
    }

    put(url, data = {}, header = {}) {
        return this.request({
            url,
            method: 'PUT',
            data,
            header
        })
    }

    delete(url, data = {}, header = {}) {
        return this.request({
            url,
            method: 'DELETE',
            data,
            header
        })
    }
}

module.exports = new Request()