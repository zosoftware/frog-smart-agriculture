<template>
    <div class="map_demo" ref="myMap">
      <div id="indexMap" class="indexMap" @mouseover="drawer3==true?followPointShow=true:''" @mouseleave="drawer3==true?followPointShow=false:''"></div>
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
          <i class="btn el-icon-map-location" ></i>
        </el-tooltip>

        <el-popover  placement="right-start" width="100" trigger="click" v-model="isShow2">
            <div v-for="item in product" :key="item.productId" class="product flex aic" @click="addDevice(item)"><img :src="baseUrl+item.imgUrl" alt="" >{{item.productName}}</div>
            <i class="btn el-icon-bangzhu" slot="reference"></i>
        </el-popover>

      </div>
      <img class="followPoint" v-show="followPointShow" :src="baseUrl+deviceImgUrl" alt="">
      <!-- 地图工具按钮 -->
      <div class="tool">
        <el-tooltip transition="el-zoom-in-center" class="item" effect="dark" content="尺子" placement="left">
          <i class="btn el-icon-position" id="ruler"></i>
        </el-tooltip>

        <el-tooltip transition="el-zoom-in-center" class="item" effect="dark" content="中心点" placement="left">
          <i class="btn el-icon-aim" id="center"></i>
        </el-tooltip>

        <el-tooltip transition="el-zoom-in-center" class="item" effect="dark" content="全屏" placement="left">
          <i class="btn el-icon-full-screen" @click="click"></i>
        </el-tooltip>

        <el-tooltip transition="el-zoom-in-center" class="item" effect="dark" content="地图放大" placement="left">
          <i class="btn el-icon-plus" id="plus"></i>
        </el-tooltip>

        <el-tooltip transition="el-zoom-in-center" class="item" effect="dark" content="地图缩小" placement="left">
          <i class="btn el-icon-minus" id="minus"></i>
        </el-tooltip>
      </div>
      <!-- 图层 -->
      <div class="layer">
        <el-tooltip transition="el-zoom-in-center" class="item" effect="dark" content="图层" placement="left">
          <i class="btn el-icon-document-copy" @click="drawer=true"></i>
        </el-tooltip>
      </div>
      <!-- 图层抽屉 -->
      <transition name="el-zoom-in-right">
        <div class="drawer" v-show="drawer">
          <div class="title">
            <div>图层</div>
            <i class="el-icon-close" @click="closeDrawer(1)"></i>
          </div>
          <ul class="controlList">
            <div class="label"><i class="el-icon-menu el_icon"></i>地块</div>
            <li class="control_item" v-for="(item,index) in areaModule" :key="item.label+index">
              <div class="icon"><i class="el_icon" :class="item.label=='地块'?'el-icon-menu':'el-icon-postcard'"></i>{{item.label}}</div>
              <div>
                <el-checkbox v-model="item.show" @change="show(item)"></el-checkbox>
              </div>
            </li>
            <div class="label"><i class="el-icon-printer el_icon"></i>设备</div>
            <li class="control_item" v-for="(item,index) in product" :key="index">
              <div class="icon"><img :src="baseUrl+item.imgUrl" alt="">{{item.productName.length>7?`${item.productName.substring(0,7)}...`:item.productName}}</div>
              <div>
                <el-checkbox v-model="item.show" @change="show(item)"></el-checkbox>
              </div>
            </li>
          </ul>
        </div>
      </transition>

      <!-- 多边形编辑抽屉 -->
      <transition name="el-zoom-in-right">
        <div class="drawer drawer2" v-show="drawer2">
          <div class="title">
            <div>地块编辑</div>
            <i class="el-icon-close" @click="closeDrawer(2)"></i>
          </div>
          <land-form :id="areaId" :path="areaPath" @upDataStyle="setStyle"  @Refresh="Refresh" :finishEdit="finishEdit" @cancel="closeDrawer(2)"></land-form>
        </div>
      </transition>

      <!-- 设备新增编辑 -->
      <transition name="el-zoom-in-right">
        <div class="drawer drawer3" v-show="drawer3">
          <div class="title">
            <div>{{title}}</div>
            <i class="el-icon-close" @click="closeDrawer(3)"></i>
          </div>
          <device-form @close="closeDrawer(3)" ref="deviceForm" :proportion="24" :deviceInfo="form" @success="success" :deviceId="deviceId"></device-form>
        </div>
      </transition>
    </div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";
import screenfull from 'screenfull';
import {listDevice} from "@/api/iot/device";
import {listProduct} from "@/api/iot/product";
import {listLand,getLand,delLand,addLand,updateLand,} from "@/api/agriculture/land";
import LandForm from "./LandForm"
import DeviceForm from "../../../iot/device/device-form.vue"
export default {
  components:{
    LandForm,DeviceForm
  },
  name:"MapArea",
  props:{
    moveTarget:[String,Number]
  },
  data() {
    return {
      baseUrl:process.env.VUE_APP_BASE_API,
      drawer:false,//抽屉控制
      drawer2:false,//抽屉控制
      drawer3:false,
      title:null,
      areaId:null,
      areaPath:null,
      polyEditor:null,//多边形编辑器
      polygonDemo:null,//正在编辑的多边形
      isFullscreen: false,//全屏控制
      type:true,//搜索状态切换
      address:null,//搜索地址
      xValue:null,
      yValue:null,
      place:[],
      isShow:false,
      isShow2:false,
      isWatch:false,
      area:[],//地块数据
      device:[],//设备数据
      product:[],//设备类别
      // 地块覆盖物数组
      overlayGroup:[],
      // 地块和名称数组
      areaModule:[],
      form:{
        productId:null,
        productName:null,
        deviceType:null,
        tenantId:null,
        tenantName:null,
        locationWay:null
      },
      deviceImgUrl:null,
      followPointShow:false,
      marker:[],
      // markerInfo:null,
      pointShow:false,
      deviceId:0,
      pointDemo:null
    };
  },
  async mounted() {
    //获去地块数据
    let res = await listLand()
    this.area=res.rows
    //获取设备数据
    let res1 = await listDevice()
    this.device=res1.rows.map(item=>{
      return {
        ...item,
        imgUrl:item.imgUrl.split(",").length>1?item.imgUrl.split(",")[1]:item.imgUrl
      }
    })
    //获取设备类别
    let res2 = await listProduct()
    this.product=res2.rows.map(item=>{
      return {
        ...item,
        imgUrl:item.imgUrl.split(",").length>1?item.imgUrl.split(",")[1]:item.imgUrl
      }
    })
    //地图初始化
    this.initMap()
    //全屏插件初始化
    this.init()
  },
  watch:{
    // 监听点击左侧列表跳转位置
    moveTarget:{
      handler(value){
        let list = value.split("|")
        if (list[2]==1) {//地块
          this.overlayGroup.forEach(item=>{
            if (item._opts.extData.landId==list[0]) {
              this.map.setFitView([item],true)
            }
          })
        }else{//设备
          this.product.forEach(item=>{
            if (item.devicePointList.length>0) {
              item.devicePointList.forEach(device=>{
                if (device._opts.extData.deviceId==list[0]) {
                  this.map.setFitView([device],true)
                }
              })
            }
          })
        }
      }
    },
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
        zoom: 16, //初始化地图级别
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

      /* 尺子 */
      var ruler=new this.AMap.RangingTool(this.map);
      document.getElementById('ruler').onclick=function(){
        ruler.turnOn();
      }
      ruler.on("end",()=>{
        ruler.turnOff()
      })
      /* 过度到中心点 */
      document.getElementById('center').onclick=()=>{
        this.map.setCenter()
      }

      /* 地图缩放 */
      document.getElementById('plus').onclick=()=>{
        this.map.zoomIn()
      }
      document.getElementById('minus').onclick=()=>{
         this.map.zoomOut()
      }

      //创建多边形编辑
      this.polyEditor = new this.AMap.PolygonEditor(this.map);
      //点击地图添加设备

      this.createAreaPolygon()
      this.createDevicePoint()
    },
    addDevice(item){
      if (this.drawer3==true) {
        this.$message({
          message: '当前正在添加设备选点!',
          type: 'warning'
        });
      }else{
        this.deviceId=0
        this.map.on("click",this.setPoint)
        this.overlayGroup.forEach((item)=>{
          item.on("click",this.setPoint)
        })
        this.followPointShow=true
        this.title=item.productName
        this.isShow2=false
        this.pointShow=true
        this.form.productId = item.productId;
        this.form.productName = item.productName;
        this.form.deviceType = item.deviceType;
        this.form.tenantId = item.tenantId;
        this.form.tenantName = item.tenantName;
        this.form.locationWay = 3;

        this.deviceImgUrl=item.imgUrl

        this.container = this.$el.querySelector('.indexMap');
        // 监听鼠标移动事件，并更新 follower 的位置信息
        this.container.addEventListener('mousemove', e => {
            let rect = this.container.getBoundingClientRect();
            let mouseX = e.clientX - rect.left - 10;
            let mouseY = e.clientY - rect.top - 22;
            // 设置平移和旋转样式
            this.$el.querySelector('.followPoint').style.left = mouseX+'px'
            this.$el.querySelector('.followPoint').style.top =mouseY+'px'
        });
        this.$refs.deviceForm.reset()
      }
    },
    //地图点击事件
    editDevice(data){
      if (this.drawer3==true) {
        this.$message({
          message: '当前正在添加设备选点!',
          type: 'warning'
        });
      }else{
        this.deviceId=data.extData.deviceId
        this.map.on("click",this.editPoint)
        this.overlayGroup.forEach((item)=>{
          item.on("click",this.editPoint)
        })
        this.followPointShow=true
        this.title=data.title
        this.isShow2=false
        this.pointShow=true
        this.deviceImgUrl=data.imgUrl
        this.container = this.$el.querySelector('.indexMap');
        // 监听鼠标移动事件，并更新 follower 的位置信息
        this.container.addEventListener('mousemove', e => {
            let rect = this.container.getBoundingClientRect();
            let mouseX = e.clientX - rect.left - 10;
            let mouseY = e.clientY - rect.top - 22;
            // 设置平移和旋转样式
            this.$el.querySelector('.followPoint').style.left = mouseX+'px'
            this.$el.querySelector('.followPoint').style.top =mouseY+'px'
        });
        this.drawer3=true
      }
    },
    setPoint({lnglat}){
      this.map.remove(this.marker)
      this.marker=[]
      var markerContent = document.createElement("div");
      var markerImg = document.createElement("img");
      markerImg.src = this.baseUrl+this.deviceImgUrl;
      markerImg.setAttribute('width', '20px');
      markerImg.setAttribute('height', '20px');
      markerContent.appendChild(markerImg);
      let point=new this.AMap.Marker({
          position:[lnglat.lng,lnglat.lat],
      })
      point.setContent(markerContent);
      this.marker.push(point)
      this.map.add(this.marker)
      this.$set(this.form,"coordinate",lnglat.lng+","+lnglat.lat)
      this.drawer3=true
    },
    editPoint({lnglat}){
      this.pointDemo.setPosition([lnglat.lng,lnglat.lat])
      this.$set(this.form,"coordinate",lnglat.lng+","+lnglat.lat)
    },
    success(){
      this.drawer3=false
      this.followPointShow=false
       //解除创建点的点击事件
      this.map.off("click",this.setPoint)
      this.overlayGroup.forEach((item)=>{
        item.off("click",this.setPoint)
      })
      this.map.off("click",this.editPoint)
      this.overlayGroup.forEach((item)=>{
        item.off("click",this.editPoint)
      })
      this.Refresh()
      this.$emit("refreshList")
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
          // 地块添加双击事件
          polygon.on("dblclick",({target})=>{
            if ((this.drawer2==true||this.drawer3==true)&&this.areaId!=target._opts.extData.landId) {
              this.$message({
                message: '请先关闭编辑模式!',
                type: 'warning'
              });
            }else if (this.drawer2==true&&this.areaId==target._opts.extData.landId) {
              this.$message({
                message: '当前地块正在编辑!',
                type: 'warning'
              });
            } else{
              this.areaId=target._opts.extData.landId
              this.drawer2=true
              this.polygonDemo=target
              this.polyEditor.setTarget(target)
              //点击确定获取编辑之后的地块路径
              this.polyEditor.on('end',({target: PolygonDemo})=>{
                if (PolygonDemo._opts.path!=null) {
                  //TODO: 【项目】调用open开启编辑时也能监听到，待解决
                  this.areaPath = PolygonDemo._opts.path.map(item=>item.toString()).join("|")
                }
              });
              this.polyEditor.open();
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
      this.isWatch=true
    },
    /* 创建设备marker点 */
    createDevicePoint(){
      this.product.forEach(product=>{
        this.$set(product,"children",[])
        this.$set(product,"layer",null)
        this.$set(product,"devicePointList",[])
        this.$set(product,"show",true)
        this.device.forEach(device=>{
          if (product.productId==device.productId) {
            product.children.push(device)
          }
        })
      })
      this.product.forEach(item=>{
        if (item.children.length>0) {
          let devicePointList=[]
          item.children.forEach(device=>{
            let marker = new this.AMap.Marker({
              icon: new this.AMap.Icon({
              image:this.baseUrl+device.imgUrl,
              imageSize: new this.AMap.Size(26,26)}),
              position: [device.longitude, device.latitude],
              title:device.deviceName,
              anchor:"bottom-center",
              extData:{
                deviceId:device.deviceId
              }
            })
            marker.on("dblclick",({target})=>{
              if ((this.drawer2==true||this.drawer3==true)&&this.deviceId!=target._opts.extData.deviceId) {
                this.$message({
                  message: '请先关闭编辑模式!',
                  type: 'warning'
                });
              }else if (this.drawer3==true&&this.deviceId==target._opts.extData.deviceId) {
                this.$message({
                  message: '当前设备正在编辑!',
                  type: 'warning'
                });
              } else{
                this.pointDemo=target
                this.editDevice({...target._opts,imgUrl:device.imgUrl})
              }
            })
            devicePointList.push(marker)
          })
          item.devicePointList=devicePointList
          item.layer=new this.AMap.OverlayGroup(devicePointList)
          this.map.add(item.layer);
        }
      })
    },
    //控制显示隐藏
    show(item){
      if (item.layer!=null) {
        if (item.show==true) {
          item.layer.show()
        }else if (item.show==false){
          item.layer.hide()
        }
      }
    },
    //地块样式回显
    setStyle(data){
      this.polygonDemo.setOptions({
        fillColor: data.fillColor,
        fillOpacity:data.fillOpacity,
        strokeColor: data.strokeColor,
        strokeWeight:data.strokeWeight,
        strokeOpacity:data.strokeOpacity,
        extData:{
          landId:data.landId
        }
      })
    },
    // 关闭抽屉
    closeDrawer(index){
      if (index==1) {
        this.drawer=false
      }else if (index==2){
        this.drawer2=false
        this.areaId=null
        this.polyEditor.close()
        this.map.clearMap()
        //清除覆盖物重新渲染
        this.createAreaPolygon()
        this.createDevicePoint()
      }else{
        this.drawer3=false
        this.followPointShow=false
        //解除创建点的点击事件
        this.map.off("click",this.setPoint)
        this.overlayGroup.forEach((item)=>{
          item.off("click",this.setPoint)
        })
        this.map.off("click",this.editPoint)
        this.overlayGroup.forEach((item)=>{
          item.off("click",this.editPoint)
        })
        //清除覆盖物重新渲染
        this.map.clearMap()
        this.createAreaPolygon()
        this.createDevicePoint()
      }
    },
    // 修改完成之后刷新
    async Refresh(){
      //获去地块数据
      let res = await listLand()
      this.area=res.rows
      //获取设备数据
      let res1 = await listDevice()
      this.device=res1.rows.map(item=>{
        return {
          ...item,
          imgUrl:item.imgUrl.split(",").length>1?item.imgUrl.split(",")[1]:item.imgUrl
        }
      })
      //获取设备类别
      let res2 = await listProduct()
      this.product=res2.rows.map(item=>{
        return {
          ...item,
          imgUrl:item.imgUrl.split(",").length>1?item.imgUrl.split(",")[1]:item.imgUrl
        }
      })
      this.$emit("refreshList")
      this.map.clearMap()
      this.createAreaPolygon()
      this.createDevicePoint()
    },
    finishEdit(){
      this.polyEditor.close()
      this.drawer2=false
      this.areaId = null
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
        console.log('location',location)
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
    click() {
      if (!screenfull.isEnabled) {
        this.$message({ message: '你的浏览器不支持全屏', type: 'warning' })
        return false
      }
      screenfull.toggle(this.$refs.myMap)
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
    },
    init() {
      if (screenfull.isEnabled) {
        screenfull.on('change', this.change)
      }
    },
    destroy() {
      if (screenfull.isEnabled) {
        screenfull.off('change', this.change)
      }
    }
  },
  beforeDestroy() {
    this.destroy()
  }
};
</script>

<style lang="scss" scoped>
.map_demo{
    height: 100%;
    width: 100%;
    box-sizing: border-box;
    position: relative;
    overflow: hidden;
    .indexMap{
      height: 100%;
      width: 100%;
    }
    .layer{
      position: absolute;
      right: 10px;
      top: 50%;
      transform: translateY(-50%);
      .btn{
        display: inline-block;
        width: 36px;
        height: 36px;
        background: #fff;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 5px;
        &:hover{
          background: #1DA76A;
          color: #fff;
        }
      }
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
    .followPoint{
      position: absolute;
      width: 20px;
      height: 20px;
      left: 0;
      top: 0;
    }
    .tool{
      position: absolute;
      right: 10px;
      bottom: 30px;
      display: flex;
      flex-direction: column;
      .el-icon-position{
        border-top-left-radius: 5px;
        border-top-right-radius: 5px;
      }
      .el-icon-full-screen{
        margin-bottom: 20px;
        border-bottom-left-radius: 5px;
        border-bottom-right-radius: 5px;
      }
      .el-icon-plus{
        border-top-left-radius: 5px;
        border-top-right-radius: 5px;
      }
      .el-icon-minus{
        border-bottom-left-radius: 5px;
        border-bottom-right-radius: 5px;
      }
      .btn{
        display: inline-block;
        width: 36px;
        height: 36px;
        background: #fff;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        &:hover{
          background: #eee;
        }
      }
    }

    .el-zoom-in-right-enter-active,
    .el-zoom-in-right-leave-active{
      opacity: 1;
      -webkit-transform: scaleX(1);
      transform: scaleX(1);
      -webkit-transition: opacity 0.3s cubic-bezier(0.23, 1, 0.32, 1),
      -webkit-transform 0.3s cubic-bezier(0.23, 1, 0.32, 1);
      transition: opacity 0.3s cubic-bezier(0.23, 1, 0.32, 1),
      -webkit-transform 0.3s cubic-bezier(0.23, 1, 0.32, 1);
      transition: transform 0.3s cubic-bezier(0.23, 1, 0.32, 1),
      opacity 0.3s cubic-bezier(0.23, 1, 0.32, 1);
      transition: transform 0.3s cubic-bezier(0.23, 1, 0.32, 1),
      opacity 0.3s cubic-bezier(0.23, 1, 0.32, 1),
      -webkit-transform 0.3s cubic-bezier(0.23, 1, 0.32, 1);
      -webkit-transform-origin: center right;
      transform-origin: center right;
    }
    .el-zoom-in-right-enter,
    .el-zoom-in-right-leave-active {
      opacity: 0;
      -webkit-transform: scaleX(0);
      transform: scaleX(0);
    }
    .drawer{
      position: absolute;
      right: 0;
      top: 0;
      height: 100%;
      width:250px;
      background: #fff;
      &.drawer2{
        width: 350px;
        padding:0 10px;
      }
      &.drawer3{
        width: 400px;
        padding:0 10px;
      }
      .title{
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #f0f0f0;
        height: 55px;
        padding: 0 15px;
        font-weight: 500;
        font-size: 18px;
        margin-bottom: 15px;
        .el-icon-close{
          cursor: pointer;
          font-size: 22px;
        }
      }
      .controlList{
        list-style: none;
        padding: 0 20px;
        color: rgb(153, 153, 153);
        font-size: 16px;
        .label{
          margin-bottom: 15px;
          display: flex;
          align-items: center;
          .el_icon{
            margin-right: 10px;
            font-size: 20px;
          }
        }
        .control_item{
          display: flex;
          justify-content: space-between;
          padding-left: 20px;
          margin-bottom: 15px;
          .icon{
            display: flex;
            align-items: center;
            font-size: 15px;
            img{
              width: 24px;
              height: 24px;
              margin-right: 10px;
            }
            .el_icon{
              margin-right: 10px;
              font-size: 20px;
            }
          }
        }
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
.product{
  user-select: none;
  height: 30px;
  cursor: pointer;
  img{
    width: 20px;
    height: 20px;
  }
  &:hover{
    background: #eee;
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
