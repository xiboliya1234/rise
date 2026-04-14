
<template>
  <div id ="content" style="margin-top: 20px;" >
    <div>

      <el-button type="primary" style="margin-left: 10px;" >查询</el-button>
      <el-button type="primary" style="margin-left: 10px;">清空</el-button>
      <el-button type="primary" style="margin-left: 10px;" @click="getData()">标注</el-button>
      <el-button type="success" style="margin-left: 10px" @click="toMap()">地图</el-button>


      <div class="input-card">
        <h4  id="123">左击获取经纬度：{{ xy}}</h4>
      </div>
    </div>
    <div  id="Map" style="margin: 0;overflow: hidden; width: 1500px;height: 800px;font-family: '微软雅黑';" >

    </div>




  </div>
</template>

<script>
import Vue from 'vue'
import AMapLoader from '@amap/amap-jsapi-loader';
import request from "@/utils/request";
export default {
  data() {
    return {
      show:true,
      xy: null,
      map: null, //地图实例
      marker:"",
      point1:[],
      point2:[],

      // icon: {type:'imgae',image:"@/assets/仪轨.png", size: [64, 30],anchor: 'center',},
      markerdom: null,
      markers: [],
      allowCollision : false,
      LabelsData :[],
      mapData: [

      ],

    }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  mounted() {
    this.getData();
    this.initMap();

  },

  methods: {
    // 获取地理定位

    initMap() {
      AMapLoader.load({
        key: "4598d1923afdc020e661e3956c6a999b", // 申请好的Web端开发者Key，首次调用 load 时必填
        //2.0版本太卡了 ，所以使用的1.4.0版本  其插件也有不同  如：ToolBar
        version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        resizeEnable: true,
        plugins: [
          "AMap.ToolBar", //工具条
          "AMap.Scale", // 比例尺
          "AMap.Geolocation",
          ,"AMap.InfoWindow", "AMap.Marker", "AMap.Polyline", "AMap.Icon",
        ], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      }).then((AMap) => {

        this.marker,this.map = new AMap.Map("Map", { //设置地图容器id
          zoom: 11,
          viewMode: '2D',//开启3D视图,默认为关闭
          center: [113.64021, 34.748693], //初始化地图中心点位置
        });
        this.markPoints();
        this.map.on('click', function(e) {

          this.xy=e.lnglat.getLng() + ',' + e.lnglat.getLat();
          document.getElementById("123").innerHTML = this.xy;
          // this.setMapMarker(e.lnglat.getLng(),e.lnglat.getLat());


        });






      }).catch(e => {
        console.log(e);
      })

    },

    getData(){
      request.get('/vue/map/getData').then(res => {
        if (res.code === "0") {


          this.mapData = res.data;
          console.log("123213"+this.mapData[0].name);
          //this.markPoints();
        }
      })
    },
    markPoints() {
      //console.log("123213"+this.mapData[0].name);
      //this.getData();
      //console.log("123213"+this.mapData[0].name);

      this.mapData.forEach(item => {
        // 创建一个 Marker 实例：
        const marker = new AMap.Marker({
          position: new AMap.LngLat(item.longitude, item.latitude),   // 经纬度对象，也可以是经纬度构成的一维数组[lng, lat]
        });
        // 将创建的点标记添加到已有的地图实例：
        this.map.add(marker);

        //给标记点添加事件
       marker.on('click', (e) => {
          this.setInfoWindows(e, item)
         console.log("长度"+this.point1.length);
         if(this.point1.length==0&&this.point2.length==0){
              this.point1 =[item.longitude,item.latitude];
         }else if(this.point1.length!=0&&this.point2.length==0){
              this.point2 =[item.longitude,item.latitude];
         }
         if(this.point1.length!=0&&this.point2.length!=0){
            const lengtt =new AMap;
            console.log("长度"+lengtt);
           //console.log("长度"+this.Map.GeometryUtil.distance(this.point1, this.point2));
         }

        })
      });
    },
        //显示的弹出框组件
        setInfoWindows(e, item) {
          console.log(item)
          // 信息窗体的内容

          let content = [
            // e.pos[0],e.pos[1],
            `<div style='\'padding:0px' 0px = '' 4px; \'=''><b>${item.name}</b>`,
            `场所类型 ：${item.content}</b>,经度 : ${item.longitude},纬度:${item.latitude}`,
            "</div></div>",
            `<img src=${item.img} alt="" style="width: 100px;height: 100px">`
          ];
          // 创建 infoWindow 实例
          let infoWindow = new AMap.InfoWindow({
            content: content.join("<br>")  //传入 dom 对象，或者 html 字符串
          });
          // 打开信息窗体
          let dd = this.map.getCenter()
          // dd.pos = [e.pos[0], e.pos[1]]
          dd.lat = item.latitude
          dd.lng = item.longitude
          console.log(dd)
          infoWindow.open(this.map, dd);
        },


  }}
</script>