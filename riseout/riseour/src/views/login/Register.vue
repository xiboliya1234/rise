<template>
  <div class="section1">
    <div>
      <div class="color"></div>
      <div class="color"></div>
      <div class="color"></div>
      <div class="box">

        <div  class="container">
          <div  class="form" >
            <h2>仪轨1.0注册界面</h2>
            <div style="margin-top: 25px; text-align: center; height: 320px;">
              <el-form :model="admin">
                <el-form-item>
                  <el-input v-model="admin.username" prefix-icon="el-icon-user"  placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="admin.password" prefix-icon="el-icon-lock"  placeholder="请输入密码"></el-input>
                </el-form-item>
                <div class="inputBox">
                  <input type="button" @click="register()"  value="注册">
                </div>

                <p class="forget">返回登录页面?
                  <a href ="/">返回</a>
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

export default {
  name: "Register",
  data() {
    return {
      admin: {}
    }
  },
  // 页面加载的时候，做一些事情，在created里面
  created() {
  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
    register() {
      request.post("/vue/login/saveUser", this.admin).then(res => {
        if (res.code === '0') {
          this.$message({
            message: '注册成功',
            type: 'success'
          });
          this.$router.push("/");
        } else {
          this.$message({
            message: res.message,
            type: 'error'
          });
        }
      })
    }
  }
}
</script>

