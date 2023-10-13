<template>
  <div class="map">
    <el-autocomplete
      style="width: 100%"
      class="mb8"
      v-model="keyword"
      :fetch-suggestions="fetchLocationData"
      placeholder="请输入查询关键字"
      @select="handleSelect"
    >
    <i slot="prefix" class="el-input__icon el-icon-search"></i>
    </el-autocomplete>
    <div id="mapBox" class="map-box" style="width: 100%;"></div>
    <div class="map-tool">
      <el-button type="primary" plain size="mini" :disabled="!!this.polygon" @click="handleCreateBthClick">新建</el-button>
      <el-button type="success" plain size="mini" @click="handleCreateBthClick">编辑</el-button>
      <el-button type="warning" plain size="mini" @click="handleClearBthClick">清除</el-button>
      <el-button type="danger" plain size="mini" @click="handleCloseBthClick">结束</el-button>
    </div>
  </div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";
export default {
  name: "DrawArea",
  props:{
    polygonPath:{
      type:String,
      default:''
    },
    polygonStyle:{
      type:Object,
      default:()=>{
        return{
          fillColor:'#26EB23',
          fillOpacity:0.9,
          strokeColor:'#00D3FC',
          strokeOpacity:0.9,
          strokeWeight:2
        }
      }
    }
  },
  data() {
    return {
      keyword: "",
      searchOverlays:[], //搜索图层
      polygon:null,
    };
  },
  watch:{
    polygonStyle:{
      handler:function(n,o){
        if(this.polygon){
          this.resetPolygonStyle();
          this.removePolygon();
          this.addPolygon();
        }
      },
      deep:true
    }
  },
  async mounted() {
    console.log(123,this)
    await this.initMap();
    this.initPolygonEditor();
  },

  methods: {
    /** 初始化map */
    initMap(){
      const{ AMap } = this;
      this.map = new AMap.Map("mapBox", {
          //设置地图容器id
          //pitch: 40,
          viewMode: "3D", //是否为3D地图模式
          mapStyle: "amap://styles/802500eb9c17892dd91047988cc1ece1",
          zoom: 16, //初始化地图级别
          center: [120.153106, 33.348826], //初始化地图中心点位置
          layers: [
          new AMap.TileLayer.Satellite(),
          //new AMap.TileLayer.RoadNet()
          ],
          //showLabel: false,
      });
      /** 添加空间 控件 */
      this.map.addControl(new AMap.Scale());
      //this.map.addControl(new AMap.ToolBar());
      this.map.addControl(new AMap.MapType());
      /** 创建PlaceSearch实例 */
      this.placeSearch = new AMap.PlaceSearch({city:'全国'});
    },
    /** init polygonEditor
     * isData：determine whether to render with value
    */
    initPolygonEditor(isData=true){
      /** 创建多边形吸附实例 */
      if(this.polygonPath && isData){
        this.polygon = new AMap.Polygon({path:this.polygonPath.split('|').map(item=>item.split(','))});
        this.resetPolygonStyle();
        this.map.add(this.polygon);
        this.polygonEditor = new AMap.PolygonEditor(this.map,this.polygon);
      }else{
        this.polygonEditor = new AMap.PolygonEditor(this.map);
      }
      this.polygonEditor.on('add', ({target:polygon}) => {
        this.polygon = polygon;
        this.resetPolygonStyle();
      });
      this.polygonEditor.on('end',({target:polygon})=>{
        this.resetPolygonStyle()
      });
      this.polygonEditor.on('adjust',({target:polygon})=>{
        this.$emit('change-path',this.polygon._opts.path);
      });
    },
    /** 位置查询处理 */
    fetchLocationData(queryString, cb) {
      if (queryString) {
        clearTimeout(this.timer);
        // 延迟 1.2 s
        this.timer = setTimeout(() => {
          this.placeSearch.search(queryString, (status, result) => {
            console.log(result)
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
    /** handle create button click event */
    handleCreateBthClick(){
        this.polygonEditor.close();
        this.polygonEditor.open();
    },
    /** handle close button click event */
    handleCloseBthClick(){
      this.polygonEditor.close();
      this.$emit('change-path',this.polygon._opts.path);
    },
    /** handle clear button click */
    handleClearBthClick(){
      this.clearPolygonEditor();
    },
    /** 清除多边形编辑工具 */
    clearPolygonEditor(){//TODO：【项目】需优化
      this.polygon = null;
      this.map.destroy();
      this.initMap();
      this.initPolygonEditor(false);
    },
    /** 重设polygon样式 */
    resetPolygonStyle(){
      const {polygon,polygonStyle:{fillColor,fillOpacity,strokeColor,strokeOpacity,strokeWeight}} = this
      polygon._opts.fillColor = fillColor;
      polygon._opts.fillOpacity = fillOpacity;
      polygon._opts.strokeColor = strokeColor;
      polygon._opts.strokeOpacity = strokeOpacity;
      polygon._opts.strokeWeight = strokeWeight;
    },
    /** 移除多边形 */
    removePolygon(){
      this.map.remove(this.polygon);
    },
    /** 添加多边形 */
    addPolygon(){
      this.map.add(this.polygon);
    }
  }
};
</script>
<style lang="scss" scoped>
  .map{
    width:100%;
    height: 500px;
    display: flex;
    flex-direction: column;
    position: relative;
    // overflow: hidden;
  }
  .map-box{
    flex:auto;
  }
  .map-tool{
    display: flex;
    flex-direction: column;
    position: absolute;
    right: 10px;
    bottom: 10px;
    background-color: #fff;
    padding:10px;
    border-radius: 5px;
  }
  ::v-deep .el-button+.el-button{
    margin:0;
  }
  ::v-deep .el-button+.el-button:nth-child(2){
    margin:5px 0;
  }
  ::v-deep .el-button+.el-button:nth-child(3){
    margin-bottom:5px;
  }
</style>
