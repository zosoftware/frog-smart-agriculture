<template>
  <div class="container">
    <div class="index">
      <el-row :gutter="10">
        <el-col :span="8">
          <data-panel title="基地概况">
            <div class="main flex fdc ">
              <el-row :gutter="10" class="flex1">
                <el-col :span="8" class="h100 flex aic"
                  ><data-box
                    icon="el-icon-s-data"
                    backgroundColor="#5470c6"
                    text="基地面积(亩)"
                    :value="baseInfo.areaCount"
                    class="flex1"
                  ></data-box
                ></el-col>
                <el-col :span="8" class="h100 flex aic"
                  ><data-box
                    icon="el-icon-s-finance"
                    backgroundColor="#91cc75"
                    text="种养种类(种)"
                    :value="baseInfo.germplasmCount"
                    class="flex1"
                  ></data-box
                ></el-col>
                <el-col :span="8" class="h100 flex aic"
                  ><data-box
                    icon="el-icon-s-grid"
                    backgroundColor="#fac858"
                    text="地块数量(块)"
                    :value="baseInfo.diCount"
                    class="flex1"
                  ></data-box
                ></el-col>
              </el-row>
              <el-row :gutter="10" class="margin-top-10 flex1">
                <el-col :span="8" class="h100 flex aic"
                  ><data-box
                    icon="el-icon-s-custom"
                    backgroundColor="#ee6666"
                    text="人员总数(位)"
                    :value="baseInfo.employeeCount"
                    class="flex1"
                  ></data-box
                ></el-col>
                <el-col :span="8" class="h100 flex aic"
                  ><data-box
                    icon="el-icon-s-claim"
                    backgroundColor="#73c0de"
                    text="种植批次(批)"
                    :value="baseInfo.batchCount"
                    class="flex1"
                  ></data-box
                ></el-col>
                <el-col :span="8" class="h100 flex aic"
                  ><data-box
                    icon="el-icon-s-shop"
                    backgroundColor="#3ba272"
                    text="大棚数量(个)"
                    :value="baseInfo.pengCount"
                    class="flex1"
                  ></data-box
                ></el-col>
              </el-row>
            </div>
          </data-panel>
        </el-col>
        <el-col :span="8">
          <data-panel title="设备统计" more="更多设备" link="/iot/device">
            <div class="main">
              <el-row class="h100" :gutter="10">
                <el-col :md="10" class="h100"
                  ><div ref="indexDeviceCountChart" class="h100"></div
                ></el-col>
                <el-col :md="7" class="h100 flex fdc jcsa">
                  <data-box v-for="item in deviceCountList.slice(0,2) " :key="item.productId"
                    icon="el-icon-s-promotion"
                    backgroundColor="red"
                    :text="item.name+'(台)'"
                    :value="item.value"
                    :isBorder="false"
                    :isIcon="false"
                  ></data-box>
                </el-col>
                <el-col :md="7" class="h100 flex fdc jcsa">
                  <data-box v-for="item in deviceCountList.slice(2,4) " :key="item.productId"
                    icon="el-icon-s-promotion"
                    backgroundColor="red"
                    :text="item.name+'(台)'"
                    :value="item.value"
                    :isBorder="false"
                    :isIcon="false"
                  ></data-box>
                </el-col>
              </el-row>
            </div>
          </data-panel>
        </el-col>
        <el-col :span="8">
          <data-panel title="设备定时任务">
            <div class="main">
              <el-row class="h100" :gutter="10">
                <el-col :md="10" class="h100"
                  ><div ref="indexDeviceControlChart" class="h100"></div
                ></el-col>
                <el-col :md="7" class="h100 flex fdc jcsa">
                  <data-box v-for="item in deviceJobCountList" :key="item.status"
                    icon="el-icon-s-promotion"
                    backgroundColor="red"
                    :text="item.status==1?'暂停(条)':'启用(条)'"
                    :value="item.jobCount"
                    :isBorder="false"
                    :isIcon="false"
                  ></data-box>
                </el-col>
                <el-col :md="7" class="h100 flex fdc jcsa">
                 <data-box v-for="item in deviceJobCountList" :key="item.status"
                    icon="el-icon-s-promotion"
                    backgroundColor="red"
                    :text="item.status==0?'执行成功(次)':'执行失败(次)'"
                    :value="item.resultCount"
                    :isBorder="false"
                    :isIcon="false"
                  ></data-box>
                </el-col>
              </el-row>
            </div>
          </data-panel>
        </el-col>
      </el-row>
      <el-row :gutter="10" class="margin-top-10">
        <el-col :span="8">
          <data-panel title="溯源统计" more="更多溯源" link="/trace/recordCount">
            <div class="main">
              <div ref="indexDeviceMonitorChart" class="h100"></div>
            </div>
          </data-panel>
          <data-panel title="农事统计" more="更多农事" link="/agriculture/task" class="margin-top-10">
            <div class="main">
              <!-- <div ref="indexServiceMonitorChart" class="h100"></div> -->
              <el-row class="h100" :gutter="10">
                <el-col :md="10" class="h100"
                  ><div ref="indexServiceMonitorChart" class="h100"></div
                ></el-col>
                <el-col :md="7" class="h100 flex fdc jcsa">
                  <data-box
                    v-for="item in taskInfo.slice(0,2)" :key="item.name"
                    icon="el-icon-s-promotion"
                    backgroundColor="red"
                    :text="item.name+'(条)'"
                    :value="item.value"
                    :isBorder="false"
                    :isIcon="false"
                  ></data-box>
                </el-col>
                <el-col :md="7" class="h100 flex fdc jcsa">
                   <data-box
                    v-for="item in taskInfo.slice(2,4)" :key="item.name"
                    icon="el-icon-s-promotion"
                    backgroundColor="red"
                    :text="item.name+'(条)'"
                    :value="item.value"
                    :isBorder="false"
                    :isIcon="false"
                  ></data-box>
                </el-col>
              </el-row>
            </div>
          </data-panel>
        </el-col>
        <el-col :span="16">
          <data-panel title="地图管理" more="地图管理" link="/agriculture/map">
            <div class="mapMain">
              <div id="indexMap" class="h100"></div>
            </div>
          </data-panel>
        </el-col>
      </el-row>
    </div>
      <el-card shadow="none" style="margin:10px 10px 10px 10px;">
        <el-row :gutter="40">
            <el-col :xs="24" :sm="24" :md="10" :lg="10" :xl="10" style="padding:40px;">
                <div style="padding:30px;margin:20px 0;font-size:14px;">
                    <div style="font-size:28px;font-weight:bold;margin-bottom:20px;">青蛙智慧农业平台</div>
                    <div style="display:table;font-size:14px;margin-bottom:10px;">
                        <div style="display:table-cell;line-height:22px;"><b style="color:#67C23A;margin-right:10px;">开源版本采用AGPL-3.0许可协议，商用需要获得授权</b>

                        </div>
                    </div>
                    <div style="display:table;margin-bottom:10px;">
                        <div style="width:70px;font-weight: bold;display:table-cell;">开源版本：</div>
                        <div style="display:table-cell;line-height:22px;">可用于个人学习和使用，非商业用途</div>
                    </div>
                    <div style="display:table;">
                        <div style="width:70px;font-weight: bold;display:table-cell;">商业版本：</div>
                        <div style="display:table-cell;line-height:22px;">商业用途，并提供所有源码，功能优先开源版本发布。<br />
                            <el-link target="_blank" href="http://www.zosoft.top/docs/chanpinshouquan/chanpinshouquan.html">查看详情 >></el-link>
                        </div>
                    </div>
                </div>
                <div style="padding:30px;font-size:14px;">
                    <div style="float:left;width:230px;">
                        <el-image style="width:210px;" :src="require('@/assets/images/code.jpg')"></el-image>
                    </div>
                    <div style="float:left;">
                        <div style="font-size:18px;font-weight:bold;margin:16px 0;">微信扫一扫，联系作者</div>
                        <div style="font-size:14px;font-weight:bold;margin:16px 0;color:#F56C6C">右侧是移动端H5版本演示
                            <br>(包含安卓和苹果APP) </div>
                            <div style="font-size:14px;font-weight:bold;margin:16px 0;">摄像头控制使用的是touch事件，PC端会失效，<br>请手机端访问:http://h5.zosoft.top/</div>
                        <div style="display:table;margin-bottom:10px;">
                            <div style="width:70px;font-weight: bold;display:table-cell;">官方网站</div>
                            <div style="display:table-cell;">
                                <el-link target="_blank" href="https://www.zosoft.top.cn/">www.zosoft.top</el-link>
                            </div>
                        </div>
                        <div style="display:table;margin-bottom:10px;">
                            <div style="width:70px;font-weight: bold;display:table-cell;">在线文档</div>
                            <div style="display:table-cell;">
                                <el-link target="_blank" href="http://www.zosoft.top/docs/chanpinwendang/bendiyunxing.html">www.zosoft.top/doc</el-link>
                            </div>
                        </div>
                        <div style="display:table;margin-bottom:15px;">
                            <div style="width:70px;font-weight: bold;display:table-cell;">系统源码</div>
                            <div style="display:table-cell;">
                                <el-link target="_blank" href="https://gitee.com/nealtsiao/frog-smart-agriculture" type="danger">Gitee源码</el-link>
                            </div>
                        </div>
                    </div>

                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="padding:40px 40px 0 40px;">
                <div class="phone">
                    <iframe src="http://h5.zosoft.top/#/pages/home/index" id="iframe" frameborder="0" scrolling="auto" height="100%" width="100%" class="phone-container"></iframe>
                </div>
            </el-col>
        </el-row>

    </el-card>
  </div>
</template>

<script>
import {selectBaseInfo,selectDeviceInfo,selectDeviceJobInfo,selectTraceInfo,selectTaskInfo} from "@/api/agriculture/dataStatistics"
import DataPanel from "./components/DataPanel";
import DataBox from "./components/DataBox";
import {lineChart,pieChart,barChart} from "./components/DataChart";
import {listLand} from "@/api/agriculture/land";
import {listDevice} from "@/api/iot/device";
import { getBaseinfoLimitOne } from "@/api/agriculture/baseinfo";

export default {
  name: "",
  components: {
    DataPanel,
    DataBox,
  },
  data() {
    return {
        landList:[],
        deviceList:[],
        baseUrl:process.env.VUE_APP_BASE_API,
        baseInfo:{
            areaCount: 0,
            germplasmCount: 0,
            diCount: 0,
            employeeCount: 0,
            batchCount: 0,
            pengCount: 0
        },
        deviceCountList:[],
        deviceTotal:0,
        deviceJobCountList:[],
        traceInfo:{
            date:[],
            num:[]
        },
        taskInfo:[]
    };
  },
  async mounted() {
      await this.getBaseInfo();
    this.initMap();
    await this.getDate();
    pieChart(this.$refs.indexDeviceCountChart,'设备数量','设备总数(个)',this.deviceCountList,this.deviceTotal);
    pieChart(this.$refs.indexDeviceControlChart,'定时任务','总任务数(条)',this.$deviceJob,this.$jobTotal);
    lineChart(this.$refs.indexDeviceMonitorChart,'溯源次数',{name:'溯源次数',},this.traceInfo.date,this.traceInfo.num);
    pieChart(this.$refs.indexServiceMonitorChart,'农事任务','任务进度(%)',this.taskInfo,this.$taskTotal);
    await this.getDeviceList();
    await this.getLandList();
    this.addFeatures();
  },
  methods: {
    /** 初始化map */
    async initMap() {
      this.map = new this.AMap.Map("indexMap", {
        //设置地图容器id
        //pitch: 40,
        viewMode: "3D", //是否为3D地图模式
        mapStyle: "amap://styles/802500eb9c17892dd91047988cc1ece1",
        zoom: 16, //初始化地图级别
        center: this.$zb, //初始化地图中心点位置
        layers: [
          new this.AMap.TileLayer.Satellite(),
        ],
      });
      /** 添加空间 控件 */
      this.map.addControl(new this.AMap.Scale());
      //this.map.addControl(new AMap.ToolBar());
      this.map.addControl(new this.AMap.MapType());
    },
    async getBaseInfo(){
        const {data} = await getBaseinfoLimitOne();
        this.$zb = data.baseCoordinate.split(',');
    },
    async getLandList(){
        const { rows } = await listLand();
        this.landList = rows;
    },
    async getDeviceList(){
        const { rows } = await listDevice();
        this.deviceList = rows;
    },
    addFeatures(){
        this.landList.forEach(item=>{
            let {landPath,fillColor,fillOpacity,strokeColor,strokeOpacity,strokeWeight,landName} = item;
            let path=landPath && landPath.split('|').map(item=>item.split(','))
            let centerPoint = this.getAreaCenter(path);
            if(path){
                this.map.add(new this.AMap.Polygon({path,fillColor,fillOpacity,strokeColor,strokeOpacity,strokeWeight}));
                this.map.add(new this.AMap.Text({position:centerPoint,anchor:'center',text:landName,style:{'background':'none','border':'none','color':'#fff'}}))
            }
        });
        this.deviceList.forEach(device=>{
            // console.log(this.baseUrl+device.imgUrl[1])
            if(device.longitude&&device.latitude){
                let marker = new this.AMap.Marker({
                icon: new this.AMap.Icon({
                    image:this.baseUrl+device.imgUrl.split(',')[1],
                    imageSize: new this.AMap.Size(26,26)
                }),
                position: [device.longitude, device.latitude],
                title:device.deviceName,
                anchor:"bottom-center",
                extData:{
                    deviceId:device.deviceId,
                    productId:device.productId
                }
                })
                marker.on("click",({target})=>{
                    let deviceId = target._opts.extData.deviceId;
                    let productId = target._opts.extData.productId;
                    switch (productId) {
                    case 98:
                        this.$router.push({
                            path: '/iot/camera/singlevideo',
                            query: {
                                t: Date.now(),
                                deviceId: deviceId,
                            }
                        });
                        break;
                    default:
                        this.$router.push({
                            path: '/iot/device-view',
                            query: {
                                t: Date.now(),
                                deviceId: deviceId,
                                productId:productId
                            }
                        });
                        break;
                }

                })
                this.map.add(marker)
            }
        })
        this.map.setFitView();
    },
    getAreaCenter(points) {
      var total = points.length;
      var X = 0,
        Y = 0,
        Z = 0;
      points.forEach( lnglat => {
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
    /** 获取首页统计数据 */
    async getDate(){
        const res = await selectBaseInfo()
            this.baseInfo = res.rows[0]

        const res2 = await selectDeviceInfo()
            this.deviceCountList = res2.rows.slice(0,4).map(item=>({name:item.productName,value:item.deviceCount}));
            this.deviceTotal = res2.rows[4].deviceCount;
            console.log(this.deviceTotal)

        const res3 = await selectDeviceJobInfo()
            this.deviceJobCountList = res3.rows;
            this.$jobTotal=0;
            this.$deviceJob = this.deviceJobCountList.map(item=>{
                this.$jobTotal+=item.jobCount;
                if(item.status == 0){
                    return {name:'未启用',value:item.jobCount}
                }else{
                    return {name:'启用',value:item.jobCount}
                }
            })

        const res4 = await selectTraceInfo()
            res4.rows.forEach(item=>{
                this.traceInfo.date.push(item.date);
                this.traceInfo.num.push(item.num);
            });

        const res5 = await selectTaskInfo()
            //未分配
            let wfp = res5.rows.find(item=>item.status==0)?res5.rows.find(item=>item.status==0).num:0;
            //已分配
            let yfp = res5.rows.find(item=>item.status==1)?res5.rows.find(item=>item.status==1).num:0;
            //进行中
            let jxz = res5.rows.find(item=>item.status==2)?res5.rows.find(item=>item.status==2).num:0;
            //已完成
            let ywc = res5.rows.find(item=>item.status==3)?res5.rows.find(item=>item.status==3).num:0;
            //任务进度
            this.$taskTotal= (wfp+yfp+ywc+jxz)==0?0:(ywc*100/(wfp+yfp+ywc+jxz)).toFixed(2);
           this.taskInfo = [{
               name:'未分配',
               value:wfp
           },{
                name:'已分配',
               value:yfp
           },{
               name:'进行中',
               value:jxz
           },{
                name:'已完成',
               value:ywc
           }]


    }
  },
};
</script>

<style lang="scss" scoped>
$margin: 10px;
.container {
  background: #edeef0;
  overflow: hidden;
}
.index {
  height: calc(100vh - 84px - #{$margin} * 2);
  margin: $margin;
}
.main {
  box-sizing: border-box;
  height: calc((100vh - 84px - #{$margin} * 4 - 51px * 3) / 3);
  padding: 10px;
}
.mapMain{
  box-sizing: border-box;
  height: calc(((100vh - 84px - #{$margin} * 4 - 51px * 3) / 3) * 2 + #{$margin} + 51px);
  padding: 5px;
  min-height: calc(180px * 2 + #{$margin} + 51px);
}
.phone {
    height: 729px;
    width: 370px;
    background-image: url("../assets/images/phone.png");
    background-size: cover;
    margin: 0 auto;
}

.phone-container {
    height: 618px;
    width: 343px;
    position: relative;
    top: 46px;
    left: 12px;
    background-color: #fff;
}

</style>
