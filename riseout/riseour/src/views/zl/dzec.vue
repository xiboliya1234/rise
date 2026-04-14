<template>
  <div style="margin-top: 20px;" >

    <div id="main" style="width: 600px;height:400px;"></div>


    <div  style="margin-top: 20px;overflow-y: auto" >



    </div>



  </div>
</template>

<script>
import request from "@/utils/request";
import qs from "qs";
import * as echarts from 'echarts';
export default {
  data() {
    return {
      onebie:[],
      myChart :[],
    }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  mounted() {
    this.loadchartnum();
    this.loadchart();

   // this.getData();
    //this.getvideo();

  }
  ,
  methods: {
    loadchartnum(){
      request.get('/vue/dz/getdzbie').then(res => {
        if (res.code === "0") {


          this.loadchartToBie(res.data);

        }
      })
    },
    loadchartToBie(data){
      let chartDom = document.getElementById('main');
      let  myChart = echarts.init(chartDom);
      let  option;

      console.log("12312321"+this.onebie );
      option = {
        title: {
          text: '场所类型数量',
          subtext: '根据场所类型统计数量',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '场所类型',
            type: 'pie',
            radius: '50%',
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);


    },
    getData() {
      // 发送请求获取数据
      //console.log("11"+this.params.pagesize);
      request.get('/vue/dz/getdzbie').then(res => {
        if (res.code === "0") {
          console.log(res.data);

          this.tableData = res.data;

        }
      })
    },



  }}
</script>