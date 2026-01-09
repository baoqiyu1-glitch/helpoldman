import Vue from 'vue'
import App from './App.vue'
import request from './utils/requset'

Vue.config.productionTip = false

// 挂载API请求
Vue.prototype.$request = request

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()