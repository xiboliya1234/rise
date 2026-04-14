import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/global.css';
import axios from 'axios'
import VueAxios from 'vue-axios'
import qs from 'qs'
import { astro } from "iztro";
import AMapLoader from '@amap/amap-jsapi-loader';

Vue.config.productionTip = false
// 设置ElementUI为Vue的插件
Vue.use(ElementUI,{size:'small',zIndex:3000});
Vue.use(VueAxios, axios);
Vue.use(qs);
Vue.use(astro);

window._AMapSecurityConfig = {
  securityJsCode:'092fbd796a32b2e0d32b126abb598d8e'
}

new Vue({
  // 初始化Vue实例
  router,
  // 渲染App组件
  render: h => h(App)
}).$mount('#app')