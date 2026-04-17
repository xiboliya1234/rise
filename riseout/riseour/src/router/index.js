import Vue from 'vue'
import VueRouter from 'vue-router'

import xp from '../views/zw/xp.vue'
import dzzl from '../views/zl/dzzl.vue'
import layout from "@/views/Layout.vue";
import login from '../views/login/login.vue'
import register from '../views/login/Register.vue'
import videoUpload from '../views/video/upload.vue'
import videoManage from '../views/video/manage.vue'
import videoList from '../views/video/list.vue'
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
    path: '/',
    name: 'login',
    component: login
  },
  {
    path: '/map',
    name: 'map',
    component: map
  },
  {
    path: '/register',
    name: 'register',
    component: register
  },
  {
    path: '/layout',
    name: 'layout',
    component: layout,
    children: [
      {
        path: '/zl/dzzl',
        name: 'dzzl',
        component: dzzl
      },
      {
        path: '/map/map2',
        name: 'map2',
        component: map2
      },
      {
        path: '/ocr/ocr',
        name: 'ocr',
        component: ocr
      },
      {
        path: '/qrcode/qrcode',
        name: 'qrcode',
        component: qrcode
      },
      {
        path: '/ollama/ollama',
        name: 'ollama',
        component: ollama
      },
      {
        path: '/zl/cslx',
        name: 'cslx',
        component: cslx
      },
      {
        path: '/zl/dzec',
        name: 'dzec',
        component: dzec
      },
      {
        path: '/video/upload',
        name: 'videoUpload',
        component: videoUpload
      },
      {
        path: '/video/manage',
        name: 'videoManage',
        component: videoManage
      },
      {
        path: '/video/list',
        name: 'videoList',
        component: videoList
      },
      {
        // Legacy route mapping to new list page.
        path: '/video/video',
        name: 'videoLegacy',
        component: videoList
      },
      {
        path: '/zw/xp',
        name: 'xp',
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

router.beforeEach((to, from, next) => {
  if (to.path === '/') {
    next();
    return;
  }
  const user = localStorage.getItem("user");
  if (!user && to.path !== '/') {
    return next("/");
  }
  next();
})

export default router