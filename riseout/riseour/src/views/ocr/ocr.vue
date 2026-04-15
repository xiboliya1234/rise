<template>
  <div style="margin-top: 20px;" >
  <div>

      <el-upload

           action="http://localhost:12345/vue/files/uploadIMG"
          :on-success="successupload">
        <el-button size="small" type="primary">点击选择图片</el-button>
      </el-upload>

  </div>
  <div>

    <el-card class="box-card">
      <div  class="text item">
        {{'ocr解析内容: ' + ocrresult }}
      </div>
    </el-card>
  </div>
    <div>

      <el-card class="box-card">
        <div  class="text item">
          {{'ocr分词内容: ' + nlpansj }}
        </div>
      </el-card>
    </div>
    <div>

      <el-card class="box-card">
        <el-descriptions title="模型解析信息" :data="dmx">
          <el-descriptions-item  label="身份证号">{{ dmx.sfzh }}</el-descriptions-item>

        </el-descriptions>
      </el-card>
    </div>
    <div>

      <el-card class="box-card">
        <el-descriptions title="正则解析信息" :data="zz">
          <el-descriptions-item  label="身份证号">{{ zz.sfzh }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request3000host";
import qs from "qs";
import axios from "axios";
export default {
  data() {
    return {
      zz:{},
      dmx:{},
      xp:{},
      ocrresult:'',
      nlpansj:'',
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
    }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  created() {
   // this.getData();
    //this.getvideo();

  }
  ,
  methods: {

    successupload(res){
        console.log(res);
        this.ocrresult = res.ocrResult;
        this.nlpansj = res.nlpansj;
        const  data = {
          question: this.ocrresult
        };
      const jsonString = JSON.stringify(data);
      //console.log(jsonString);
      this.getregular(jsonString);//使用正则解析
      this.getflowise(jsonString);//使用大模型解析

    },

    getregular(res) {
      const regex = /^[1-9]\d{5}(19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[Xx\d]$/;
      const match = res.match(regex);
      if (match) {
        console.log("ID number found:", match[0]);
        this.zz = {
          sfzh: match[0]
        }
      } else {
        console.log("ID number not found");
      }
    },

    getflowise(res) {
      // 发送请求获取数据
      request.post('/api/v1/prediction/53d58e9f-1209-45f6-98c9-fa2145e08f23',res).then(res => {

        const newStr = res.json.answer.replace(/\s+/g, '');
        console.log(newStr);
        this.dmx= {
          sfzh: newStr
        }
      })

    },


    reset(){
      this.params={}
    },
    handleSizeChange(pagesize){
      this.params.pagesize = pagesize;
      request.get('/vue/video/getvideo',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
    handleCurrentChange(pagenum){
      this.params.pagenum = pagenum;
      request.get('/vue/video/getvideo',{params:this.params}).then(res => {
        if (res.code === "0") {
          console.log(res.data);
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      })
    },
}}
</script>
