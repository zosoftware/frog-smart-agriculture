<template>
    <div class="map_demo" ref="myMap">
        <el-input readonly @click.native="disabled?null:initMap()" :value="value" :disabled="disabled" style="width:100%" prefix-icon="el-icon-location-information" placeholder="请选择坐标"></el-input>
        <el-dialog title="位置选择" v-show="visible" :visible="true" width="800px" :close-on-click-modal="false" :modal="false" @close="visible=false;pointShow=false">
            <div class="boxCon">
                <div id="indexMap" class="indexMap"></div>
                <div class="operate">
                    <el-popover
                        placement="right-start"
                        width="400"
                        trigger="click"
                        title="地址搜索"
                        v-model="isShow"
                        >
                        <div class="search" v-if="type">
                            <i class="el-icon-sort" @click="changeType"></i>
                            <el-autocomplete
                            style="width: 100%"
                            v-model="address"
                            :fetch-suggestions="fetchLocationData"
                            placeholder="请输入查询关键字"
                            @select="handleSelect"
                            class="margin-tb-6"
                            >
                            <i slot="prefix" class="el-input__icon el-icon-search"></i>
                            </el-autocomplete>
                        </div>
                        <div v-else class="coordinate">
                            <el-alert class="margin-tb-12" show-icon title="该系统采用GCJ-02坐标系，请输入该坐标系的数值" type="warning" :closable="false"></el-alert>
                            <div class="search">
                            <i class="el-icon-sort" @click="changeType"></i>
                            <el-input style="width:150px" placeholder="经度" v-model="xValue"></el-input>
                            <el-input style="width:150px" placeholder="纬度" v-model="yValue"></el-input>
                            <el-button type="text" @click="analysis">搜索</el-button>
                            </div>
                            <div class="list" v-show="place.length>0">
                            <div v-for="(item,index) in place" :key="index" @click="goTo"><i class="el-icon-location-information"></i>{{item}}</div>
                            </div>
                        </div>
                        <el-tooltip slot="reference" transition="el-zoom-in-center" class="item" effect="dark" content="搜索" placement="right">
                            <i class="btn el-icon-search" ></i>
                        </el-tooltip>
                    </el-popover>

                    <el-tooltip transition="el-zoom-in-center" class="item" effect="dark" content="定位" placement="right">
                        <i class="btn el-icon-map-location" @click="adding"></i>
                    </el-tooltip>
                </div>
                <div class="edit" v-show="pointShow">
                    <el-button @click="pointShow=false">取消</el-button>
                    <el-button type="primary" @click="confirm">确认</el-button>
                </div>
                <img class="point" v-show="pointShow" :src="require('@/assets/images/point.svg')" alt="">
            </div>
        </el-dialog>
    </div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";
import {listLand} from "@/api/agriculture/land";


export default {
  name:"PointSelect",
  props:{
    value:{
        default:null,
        type:String
    },
    disabled:{
      default:false,
      type:Boolean
    },
  },
  data() {
    return {
      areaId:null,
      type:true,//搜索状态切换
      address:null,//搜索地址
      xValue:null,
      yValue:null,
      place:[],
      isShow:false,
      area:[],//地块数据
      // 地块覆盖物数组
      overlayGroup:[],
      // 地块和名称数组
      areaModule:[],
      visible:false,
      pointShow:false,
      container:null,
      marker:[],
      markerInfo:null
    }
  },
  // async mounted() {
  //   //获去地块数据
  //   let res = await listLand()
  //   this.area=res.rows

  //   //地图初始化
  //   this.initMap()

  // },
  watch:{
    // value:{
    //     handler(value){
    //         if (value) {
    //             let lnglat={
    //               lng:value.split(",")[0],
    //               lat:value.split(",")[1]
    //             }
    //             this.setPoint(lnglat)
    //         }
    //     },
    //     immediate:true
    // },
    isShow:{
      handler(value){
        if (value) {
          this.address=null
          this.type=true
        }
      }
    }
  },
  methods: {
    /** 初始化map */
    async initMap() {
      this.map = new this.AMap.Map("indexMap", {
        //设置地图容器id
        mapStyle: "amap://styles/802500eb9c17892dd91047988cc1ece1",
        zoom: 15, //初始化地图级别
        center: [120.15066,33.349802], //初始化地图中心点位置
        layers: [
          new this.AMap.TileLayer.Satellite()
        ],
        doubleClickZoom:false,
      });
      /* 搜索 */
      this.placeSearch = new this.AMap.PlaceSearch({city:'全国'});
      /* 逆地址解析 */
      this.geocoder = new this.AMap.Geocoder({
        city: "全国"
      });
      this.visible=true
      let res = await listLand()
      this.area=res.rows
        this.createAreaPolygon()
      if (this.value) {
        let lnglat={
          lng:this.value.split(",")[0],
          lat:this.value.split(",")[1]
        }
        this.setPoint(lnglat)
      }
    },
    /* 创建地块 */
    createAreaPolygon(){
      let label=[]
      this.overlayGroup = []
      this.areaModule = []
      this.area.forEach(item => {
        if (item.landPath) {
          let path = [],pointList=[]
          item.landPath.split("|").forEach(point=>{
            path.push(new this.AMap.LngLat(point.split(",")[0],point.split(",")[1]))
            pointList.push([Number(point.split(",")[0]),Number(point.split(",")[1])])
          })
          let polygon =new this.AMap.Polygon({
              path: path,
              fillColor: item.fillColor,
              fillOpacity:item.fillOpacity,
              strokeColor: item.strokeColor,
              strokeWeight:item.strokeWeight,
              strokeOpacity:item.strokeOpacity,
              extData:{
                landId:item.landId
              }
          })
          this.overlayGroup.push(polygon)
          //添加地块名称
          label.push(new this.AMap.Text({
            text:item.landName,
            anchor:"center",
            position: this.getAreaCenter(pointList),
            style:{
              "background":"transparent",
              "color":"#fff",
              "border":"none"
            }
          }))
        }
      });
      //地块覆盖物群组
      this.AreaPolygonGroup=new this.AMap.OverlayGroup(this.overlayGroup)

      this.areaModule.push({
        label:"地块",
        show:true,
        layer:this.AreaPolygonGroup
      })
      //地块名称图层
      this.AreaLabelGroup=new this.AMap.OverlayGroup(label)
      this.areaModule.push({
        label:"名称",
        show:true,
        layer:this.AreaLabelGroup
      })

      this.map.add(this.AreaLabelGroup);
      this.map.add(this.AreaPolygonGroup);
      this.map.on("click",this.clickOn)
      this.overlayGroup.forEach((item)=>{
        item.on("click",this.clickOn)
      })
    },
    changeType(){
      this.type=!this.type
      this.address=null
      this.xValue=null
      this.yValue=null
      this.place=[]
    },
    analysis(){
      this.geocoder.getAddress(new this.AMap.LngLat(this.xValue, this.yValue), (status, result) => {
        if (status === "complete" && result.info === "OK") {
          this.place=[]
          this.place.push(result.regeocode.formattedAddress)
        }
      });
    },
    goTo(){
      this.map.panTo([this.xValue, this.yValue]);
    },
    /** 位置查询处理 */
    fetchLocationData(queryString, cb) {
      if (queryString) {
        clearTimeout(this.timer);
        // 延迟 1.2 s
        this.timer = setTimeout(() => {
          this.placeSearch.search(queryString, (status, result) => {
            if (status === 'complete') {
              cb(result.poiList.pois.map(item => ({ ...item, value: item.address })))
            }
            else {
              cb([])
            }
          });
        }, 1200);
      } else {
        cb([]);
      }
    },
    /** handle select */
    handleSelect(item) {
        const { map, searchOverlays } = this;
        const { location, address } = item;
        const { lng, lat } = location;
        map.panTo([lng, lat]);
    },
    //计算覆盖物中心点
    getAreaCenter(location) {
      var total = location.length;
      var X = 0,
        Y = 0,
        Z = 0;
      location.forEach( lnglat => {
        var lng = (lnglat [0] * Math.PI) / 180;
        var lat = (lnglat [1] * Math.PI) / 180;
        var x, y, z;
        x = Math.cos(lat) * Math.cos(lng);
        y = Math.cos(lat) * Math.sin(lng);
        z = Math.sin(lat);
        X += x;
        Y += y;
        Z += z;
      });
      X = X / total;
      Y = Y / total;
      Z = Z / total;

      var Lng = Math.atan2(Y, X);
      var Hyp = Math.sqrt(X * X + Y * Y);
      var Lat = Math.atan2(Z, Hyp);
      return [
        parseFloat((Lng * 180) / Math.PI),
        parseFloat((Lat * 180) / Math.PI),
      ];
    },
    adding(){
        this.pointShow=true

        this.container = this.$el.querySelector('.indexMap');
        // 监听鼠标移动事件，并更新 follower 的位置信息
        this.container.addEventListener('mousemove', e => {
            let rect = this.container.getBoundingClientRect();
            let mouseX = e.clientX - rect.left - 10;
            let mouseY = e.clientY - rect.top - 22;
            // 设置平移和旋转样式
            this.$el.querySelector('.point').style.left = mouseX+'px'
            this.$el.querySelector('.point').style.top =mouseY+'px'
        });
    },
    clickOn({lnglat}){
      if (this.pointShow) {
        this.map.remove(this.marker)
        this.marker=[]
        this.setPoint(lnglat)
      }
    },
    setPoint(lnglat){
        var markerContent = document.createElement("div");
        var markerImg = document.createElement("img");
        markerImg.src = require('@/assets/images/point.svg');
        markerImg.setAttribute('width', '20px');
        markerImg.setAttribute('height', '20px');
        markerContent.appendChild(markerImg);
        let point=new this.AMap.Marker({
            position:[lnglat.lng,lnglat.lat],
        })
        point.setContent(markerContent);
        this.marker.push(point)
        console.log(this.map)
        this.map.add(this.marker)
        this.markerInfo=lnglat.lng+","+lnglat.lat
    },
    confirm(){
        this.$emit("input",this.markerInfo)
        this.pointShow=false
        this.visible=false
    }
  }
};
</script>

<style lang="scss" scoped>
.map_demo{
    .boxCon{
        position: relative;
        .indexMap{
            height: 500px;
            width: 100%;
        }
        .operate{
            position: absolute;
            left: 10px;
            top: 10px;
            display: flex;
            flex-direction: column;
            .el-icon-search{
                background: #1DA76A;
                color: #fff;
            }
            .el-icon-map-location{
                background:#FF6C66;
                color: #fff;
            }
            .el-icon-bangzhu{
                background:#756EFF;
                color: #fff;
            }
            .btn{
                display: inline-block;
                width: 40px;
                height: 40px;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 5px;
                margin-bottom: 10px;;
            }
        }
        .edit{
            position: absolute;
            bottom: 10px;
            right: 10px;
        }
        .point{
            position: absolute;
            width: 20px;
            height: 20px;
            left: 0;
            top: 0;
        }
    }
}
.search{
  display: flex;
  align-items: center;
  justify-content: space-between;
  .el-icon-sort{
    font-size: 24px;
    transform: rotate(90deg);
    cursor: pointer;
    color: #1DA76A;
    margin-right:10px;
  }
  .el-autocomplete{
    margin: 0;
  }
  .el-input{
    width: 300px;
    border-color: #1DA76A;
  }
}
.coordinate{
  .list{
    div{
      height: 30px;
      line-height: 30px;
      padding:0 38px;
      cursor: pointer;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      color: #1DA76A;
      margin-top: 10px;
    }
  }
}
</style>
