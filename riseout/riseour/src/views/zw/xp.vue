<template>
  <div style="margin-top: 20px;" >


    <div>

      <el-descriptions title="星盘" :data="xp">
        <el-descriptions-item  label="阴历">{{ xp.lunarDate }}</el-descriptions-item>
        <el-descriptions-item label="阳历">{{ xp.solarDate }}</el-descriptions-item>
        <el-descriptions-item label="四柱">{{ xp.chineseDate }}</el-descriptions-item>
        <el-descriptions-item label="时辰">{{ xp.time }}</el-descriptions-item>
      </el-descriptions>

    </div>



    <div>

      <el-dialog title="请填写信息" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form">
          <el-form-item label="出生日期" label-width="20%">
            <el-date-picker
              v-model="form.date"
              type="date"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
          </el-date-picker>
          </el-form-item>
          <el-form-item label="性别" label-width="15%">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary"  @click="submit()">测算</el-button>
        </div>
      </el-dialog>

    </div>
    <div>
      <el-button type="primary"  @click="add()" style="position: fixed;  bottom: 10px;">开始测算</el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import qs from "qs";
import {astro} from "iztro";

export default {
  data() {
    return {
      input: '',
      params: {
        pagenum:1,
        pagesize:10
      },
      total:0,
      tableData: [],
      dialogFormVisible:false,
      form: {},
      options: [],
      loading: true,
      xp:{},
    }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  created() {

    //this.getvideo();

  }
  ,
  methods: {


    add(){
      this.form = {};
      this.dialogFormVisible=true;
    },


    submit(){
      // 通过阳历获取星盘信息
      const  date = this.form.date;
      const  sex = this.form.sex;

      console.log("date"+date);
      console.log("sex"+sex);
      // 获取运限数据
      const astrolabe = astro.bySolar(date, 2, sex);
      astrolabe.horoscope(new Date());


      console.log("astrolabe"+astrolabe.lunarDate);
      this.xp=astrolabe;
          this.dialogFormVisible=false;

    },


    reset(){
      this.params={}
    },


  }}
</script>