import Vue from 'vue'
import VueRouter from 'vue-router'

import xp from '../views/zw/xp.vue'
import dzzl from '../views/zl/dzzl.vue'
import layout from "@/views/Layout.vue";
import login from '../views/login/login.vue'
import register from '../views/login/Register.vue'
import video from '../views/video/video.vue'
import cslx from "@/views/zl/cslx.vue";
import map from "@/views/map/map.vue";
import map2 from "@/views/map/map2.vue";
import dzec from "@/views/zl/dzec.vue";
import ocr from "@/views/ocr/ocr.vue";
import qrcode from "@/views/qrcode/qrcode.vue";
import ollama from "@/views/ollama/ollama.vue";
Vue.use(VueRouter)

const routes = [

  {
    // 路径
    path: '/',
    // 名称
    name: 'login',
    // 组件
    component: login
  },
  {
    // 路径
    path: '/map',
    // 名称
    name: 'map',
    // 组件
    component: map
  },

  {
    // 路径
    path: '/register',
    // 名称
    name: 'register',
    // 组件
    component: register
  },
  {
    // 路径
    path: '/layout',
    // 名称
    name: 'layout',
    // 组件
    component: layout,
    children: [
      {
        // 路径
        path: '/zl/dzzl',
        // 名称
        name: 'dzzl',
        // 组件
        component: dzzl
      },
      {
        // 路径
        path: '/map/map2',
        // 名称
        name: 'map2',
        // 组件
        component: map2
      },
      {
        // 路径
        path: '/ocr/ocr',
        // 名称
        name: 'ocr',
        // 组件
        component: ocr
      },
      {
        // 路径
        path: '/qrcode/qrcode',
        // 名称
        name: 'qrcode',
        // 组件
        component: qrcode
      },
      {
        // 路径
        path: '/ollama/ollama',
        // 名称
        name: 'ollama',
        // 组件
        component: ollama
      },
      {
        // 路径
        path: '/zl/cslx',
        // 名称
        name: 'cslx',
        // 组件
        component: cslx
      },
      {
        // 路径
        path: '/zl/dzec',
        // 名称
        name: 'dzec',
        // 组件
        component: dzec
      },
      {
        // 路径
        path: '/video/video',
        // 名称
        name: 'video',
        // 组件
        component: video
      },
      {
        // 路径
        path: '/zw/xp',
        // 名称
        name: 'xp',
        // 组件
        component: xp
      }
    ]
  },


]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.path === '/') {
    next();
  }
  const user = localStorage.getItem("user");
  if (!user[0] && to.path !== '/') {
    return next("/");
  }
  next();
})


export default router
