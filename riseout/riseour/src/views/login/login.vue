<template>
  <div class="section1">
    <div>
    <div class="color"></div>
    <div class="color"></div>
    <div class="color"></div>
    <div class="box">

    <div  class="container">
      <div  class="form" >
        <h2>仪轨1.0登陆界面</h2>
        <div style="margin-top: 25px; text-align: center; height: 320px;">
          <el-form :model="user" v-loading="loading">
            <el-form-item>
              <el-input v-model="user.username" prefix-icon="el-icon-user"  placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="user.password" prefix-icon="el-icon-lock"  placeholder="请输入密码"></el-input>
            </el-form-item>
            <div class="inputBox">
              <input type="button" @click="login()"  value="登录">
            </div>

            <p class="forget">还没有账户?
              <a href ="/register">注册</a>
            </p>
          </el-form>

        </div>
      </div>
    </div>
      <div class="square"style="--i:0"></div>
      <div class="square"style="--i:1"></div>
      <div class="square"style="--i:2"></div>
      <div class="square"style="--i:3"></div>
      <div class="square"style="--i:4"></div>

    </div>
      </div>
  </div>

</template>

<script>

import request from "@/utils/request";
import '@/assets/login.css';
export default {
  name: "LOGIN",
  data() {
    return {
      user: {},
      loading: false,
    }
  },

  methods: {
    login(){
      this.loading= true;

      request.post('/vue/login/getlogin',this.user).then(res => {
        console.log("code"+res.code);


         if(res.code==="-1"){

            this.$message({
              message: res.message,
              type: 'error'
            });
           this.loading= false;
          }else{

           this.$message({
           message: '登陆成功',
           type: 'success'
         });
           this.loading= false;
           localStorage.setItem("user",JSON.stringify(res.data));
          this.$router.push("/layout");
         }


      })

    },
  }
}
</script>
