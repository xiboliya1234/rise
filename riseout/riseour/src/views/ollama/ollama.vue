<script setup>
import {ref} from "vue";
const result =ref([]);
const message = ref('');
const ask1 =()=>{
  let source =new EventSource('http://192.168.31.235:45454/ai/generateStream?message='+message.value);
  let count =0;
  source.onmessage = (event)=>{

    if(event.data===''){
      count++;

    }

    if(count===1){

      source.close();
      result.value.push("<span>/r</span>");
      let txt=document.getElementById("history").innerHTML;
      console.log(txt);
      //document.getElementById("history").innerHTML= txt.replace("</div></div>","<br></div></div>");
    }
    result.value.push(event.data);
  }
};
</script>
<template>
  <div style="margin-top: 20px;" >
  <div>



  </div>
  <div style="width: 100%">
  <div style="width: 90%;float: left" id="history">

    <el-card class="box-card" id="history">

      <span v-for="r in result">{{r}}</span>

    </el-card>
  </div>
    <div style="width: 90%;float: left" id="chat">

      <el-card class="box-card">
        <el-input v-model="message" placeholder="请输入内容"></el-input>
        <el-button type="primary" @click="ask1">发送</el-button>
      </el-card>
    </div>

    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import qs from "qs";
import {ref} from "vue";


export default {
  data() {
    return {
      message:'',
      input1: '',
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







    reset(){
      this.params={}
    },


}}
</script>