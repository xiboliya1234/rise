
<template>
  <div id ="content" style="margin-top: 20px;" >
  <div>

    <el-button type="primary" style="margin-left: 10px;" >查询</el-button>
    <el-button type="primary" style="margin-left: 10px;">清空</el-button>
    <el-button type="primary" style="margin-left: 10px;" >新增</el-button>
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
export default {
  data() {
    return {
      show:true,
      xy: null,
      map: null, //地图实例
      marker:"",
     // icon: {type:'imgae',image:"@/assets/仪轨.png", size: [64, 30],anchor: 'center',},
      markerdom: null,
      markers: [],
      allowCollision : false,
       LabelsData :[],
      markernum: [
        [108.925107, 34.238578],
        [108.977807, 34.240636],
        [108.977893, 34.20508],
        [108.915065, 34.202951],
        [108.920713, 34.226592],

    //为了展示，用的写好的数据
  ]



  }
  },
  //在vue中created相当于初始化方法，在挂载元素之前执行，也就是onload函数
  mounted() {
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
          "AMap.Geolocation", //定位
        ], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      }).then((AMap) => {
        console.log(AMap);
        this.marker,this.map = new AMap.Map("Map", { //设置地图容器id
          zoom: 11,
          viewMode: '2D',//开启3D视图,默认为关闭
          center: [113.64021, 34.748693], //初始化地图中心点位置
        });
        this.map.on('click', function(e) {

         this.xy=e.lnglat.getLng() + ',' + e.lnglat.getLat();
            document.getElementById("123").innerHTML = this.xy;
         // this.setMapMarker(e.lnglat.getLng(),e.lnglat.getLat());
          this.map.setFitView();
          this.marker = new AMap.Marker({
            map: this.map,
            position: [e.lnglat.getLng(), e.lnglat.getLat()],
          });
          this.map.setFitView();
          this.map.add(this.marker);
        });






      }).catch(e => {
        console.log(e);
      })

    },
    setMapMarker(x,y) {
      // 自动适应显示想显示的范围区域
      this.map.setFitView();
      this.marker = new AMap.Marker({
        map: this.map,
        position: [x, y],
      });
      this.map.setFitView();
      this.map.add(this.marker);
    },


}}
</script>