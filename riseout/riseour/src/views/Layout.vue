

<template>
  <div id="app">
    <el-container>
      <el-header style="background-color:darkseagreen ">
        <img src="@/assets/仪轨.png" alt="" style="width: 50px; position: relative; top: 10px;">
        <span style="font-size: 20px; margin-left: 15px; color: white">黄粱一梦</span>
        <el-dropdown style=" margin-left: 15px; position: absolute;right: 10px;top:20px;color: white">
          <span class="el-dropdown-link" >
            道友{{ user.username }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu  v-slot="dropdown">
            <el-dropdown-item >
              <div @click="quit()">
              注销</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </el-header>
    </el-container>
    <el-container>
      <el-aside style="overflow: hidden; min-height: 100vh; background-color: #545c64; width: 250px">
        <!-- 侧边栏 -->
        <el-menu
            default-active="$router.path"
            background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
            router>

          <el-submenu index="1" >
            <template slot="title">
              <span>紫薇研习</span>
            </template>
            <el-menu-item index="/zw/xp">星盘</el-menu-item>
          </el-submenu>
          <el-menu-item index="/video/video">
            <span slot="title">森罗万象</span>
          </el-menu-item>
          <el-menu-item index="/map/map2" >
            <span slot="title">地图管理</span>
          </el-menu-item>
          <el-menu-item index="/ocr/ocr">
            <span slot="title">ocr</span>
          </el-menu-item>
          <el-menu-item index="/ollama/ollama">
            <span slot="title">万法宗坛</span>
          </el-menu-item>
          <el-submenu index="/" >
            <template slot="title">
              <span>地址场所治理</span>
            </template>
            <el-menu-item index="/zl/dzzl">场所详情</el-menu-item>
            <el-menu-item index="/zl/cslx">场所类型</el-menu-item>
            <el-menu-item index="/zl/dzec">场所标图</el-menu-item>
          </el-submenu>

          <el-menu-item index="/zl/dzzl">
            <span slot="title">白玉京</span>
          </el-menu-item>
          <el-menu-item index="/qrcode/qrcode">
            <span slot="title">二维码</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <!-- 主要内容 -->
        <router-view/>
      </el-main>
    </el-container>


  </div>
</template>
<script>
  export default {
  name: "Layout",
    data() {
      return {
        user :JSON.parse(localStorage.getItem("user")),

     }
    },created() {
      //const   qwe = JSON.parse(localStorage.getItem("user"));
      //console.log(qwe[0].username);

    },methods:{
        quit(){


            localStorage.removeItem("user");
            this.$router.push("/");
        }
    }
  }
</script>