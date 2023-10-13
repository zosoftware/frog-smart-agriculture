<template>
  <div class="h100">

    <el-button
      v-if="!draggable"
      type="primary"
      size="mini"
      @click="draggable = true"
      class="margin-top-10 position-absolute right-120"
      :loading="loading"
      :disabled="!checkPermi(['agriculture:layout:add'])"
      >更改节点排版</el-button
    >
    <el-button-group v-if="draggable" class="margin-top-10 position-absolute right-120" >
      <el-button type="warning" size="mini" @click="handleReset" :disabled="!resetBtn" >复位</el-button >
      <el-button type="info" size="mini" @click="draggable = false" >取消</el-button>
      <el-button type="primary" size="mini" @click="handleSave">保存</el-button>
    </el-button-group>
     <el-button
      v-if="!isMonitor"
      type="warning"
      size="mini"
      @click="beginMonitor"
      class="margin-top-10 position-absolute right-30"
      :loading="loading"
      :disabled="!checkPermi(['device:view:monitor'])"
      >实时数据</el-button
    >
    <el-button
      v-if="isMonitor"
      type="danger"
      size="mini"
      @click="stopMonitor"
      class="margin-top-10 position-absolute right-30 "
      :loading="loading"
      >关闭实时</el-button
    >
    <vue-draggable-resizable v-if="layout.img"
        :resizable="draggable"
        :draggable="draggable"
        class-name="original"
        @dragstop="handleDragstop(arguments,'img')"
        @resizestop="handleResizestop(arguments,'img')"
        :x="Number(layout.img.x)"
        :y="Number(layout.img.y)"
        :w="Number(layout.img.w)"
        :h="Number(layout.img.h)"
    >
        <el-image class="w100 h100" :src="device.imgUrl?$baseUrl+device.imgUrl.split(',')[0]:''" fit="fill" :lazy="true"></el-image>
    </vue-draggable-resizable>
    <vue-draggable-resizable
        v-for="item in layout.monitorList"
        :key="item.identifier"
        class-name="original"
        class-name-draggable="draggable"
        class-name-dragging="dragging"
        class="border-radius-20"
        h="auto"
        w="auto"
        :x="Number(item.x)"
        :y="Number(item.y)"
        :resizable="false"
        :draggable="draggable"
        :parent="true"
        @dragstop="handleDragstop(arguments,item.identifier)"
    >
    <div class="inline-flex border-d8d8d8 border-radius-20 aic" @dblclick="handleChartClick(item.identifier)">
        <div class="width-35 height-35 border-radius-20 flex aic jcc" :style="{ backgroundColor: item.color }" >
            <svg-icon class="font-size-20 font-color-fff" :icon-class="item.icon" ></svg-icon>
        </div>
        <span class="padding-lr-10 font-color-l1 font-size-14">{{ item.name }}</span>
        <span class="padding-right-10 font-color-l1 font-size-14">
        <span class="font-weight-bold font-size-17">{{ item.value }}</span>
        <span class="margin-left-5">{{ item.unit }}</span>
        </span>
    </div>
    </vue-draggable-resizable>
     <vue-draggable-resizable
        v-for="item in layout.controlList"
        :key="item.identifier"
        class-name="original"
        class-name-draggable="draggable"
        class-name-dragging="dragging"
        class="border-radius-10"
        h="auto"
        w="auto"
        :x="Number(item.x)"
        :y="Number(item.y)"
        :resizable="false"
        :draggable="draggable"
        :parent="true"
        @dragstop="handleDragstop(arguments,item.identifier)"
    >
    <div class="flex aic width-250 height-100 border-radius-10" style="background-color:#fbfafa;">
        <div class="margin-lr-15 width-50 height-50 border-radius-30 flex aic jcc" :style="{ backgroundColor: item.color }" >
            <svg-icon class="font-size-20 font-color-fff" icon-class="control" ></svg-icon>
        </div>
        <div class="flex fdc height-60 jcsa">
            <span class="font-size-14">{{item.name}}</span>
            <div class="width-150">
                <div v-if="item.dataType.type=='bool'">
                    <el-switch size="mini" v-model="item.shadow" @change="mqttPublish(item)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" :disabled="(shadowUnEnable || item.isReadonly==1)" />
                </div>
                <div v-if="item.dataType.type=='enum'">
                    <el-select size="mini" v-model="item.shadow" placeholder="请选择" @change="mqttPublish(item)" :disabled="(shadowUnEnable || item.isReadonly==1)">
                        <el-option v-for="subItem in item.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                    </el-select>
                </div>
                <div v-if="item.dataType.type=='string'">
                    <el-input v-model="item.shadow" :placeholder="'请输入字符串 '+(item.dataType.unit?'，单位：'+item.dataType.unit:'')" :disabled="(shadowUnEnable || item.isReadonly==1)">
                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(item)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && item.isReadonly==0)"></el-button>
                    </el-input>
                </div>
                <div v-if="item.dataType.type=='decimal'">
                    <el-input v-model="item.shadow" type="number" :placeholder="'请输入小数 '+(item.dataType.unit?'，单位：'+item.dataType.unit:'')" :disabled="shadowUnEnable || item.isReadonly==1">
                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(item)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && item.isReadonly==0)"></el-button>
                    </el-input>
                </div>
                <div v-if="item.dataType.type=='integer'">
                    <el-input v-model="item.shadow" type="integer" :placeholder="'请输入整数 '+(item.dataType.unit?'，单位：'+item.dataType.unit:'')" :disabled="shadowUnEnable || item.isReadonly==1">
                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(item)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && item.isReadonly==0)"></el-button>
                    </el-input>
                </div>
                <div v-if="item.dataType.type=='object'">
                    <el-descriptions :column="1" size="mini" border>
                        <el-descriptions-item v-for="(param,index) in item.dataType.params" :key="index" :label="param.name">
                            <div v-if="param.dataType.type=='bool'">
                                <el-switch v-model="param.shadow" @change="mqttPublish(param)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" :disabled="(shadowUnEnable || param.isReadonly==1)" />
                            </div>
                            <div v-if="param.dataType.type=='enum'">
                                <el-select v-model="param.shadow" placeholder="请选择" @change="mqttPublish(param)" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                    <el-option v-for="subItem in param.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                                </el-select>
                            </div>
                            <div v-if="param.dataType.type=='string'">
                                <el-input v-model="param.shadow" placeholder="请输入字符串" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                    <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                </el-input>
                            </div>
                            <div v-if="param.dataType.type=='decimal'">
                                <el-input v-model="param.shadow" type="number" placeholder="请输入小数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                    <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                </el-input>
                            </div>
                            <div v-if="param.dataType.type=='integer'">
                                <el-input v-model="param.shadow" type="integer" placeholder="请输入整数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                    <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                </el-input>
                            </div>
                        </el-descriptions-item>
                    </el-descriptions>
                </div>
                <div v-if="item.dataType.type=='array'">
                    <!-- <div style="margin-bottom:10px;">
                        <el-input v-model="item.shadow" placeholder="请输入英文逗号分隔的字符串" :disabled="shadowUnEnable || item.isReadonly==1" v-if="item.dataType.arrayType!='object'" @input="arrayInputChange($event,item)">
                            <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,item)" style="font-size:20px;" title="指令发送"></el-button>
                        </el-input>
                    </div> -->
                    <el-descriptions :column="1" size="mini" border v-if="item.dataType.arrayType!='object'">
                        <el-descriptions-item v-for="(model,index) in item.dataType.arrayModel" :key="index" :label="item.name+(index+1)">
                            <div v-if="item.dataType.arrayType=='string'">
                                <el-input placeholder="请输入字符串" size="mini" v-model="model.shadow" :disabled="shadowUnEnable || item.isReadonly==1" @input="arrayItemChange($event,item)">
                                    <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(model)" style="font-size:20px;" title="指令发送" v-if="!shadowUnEnable || item.isReadonly==0"></el-button>
                                </el-input>
                            </div>
                            <div v-if="item.dataType.arrayType=='decimal'">
                                <el-input type="number" placeholder="请输入小数 " size="mini" v-model="model.shadow" :disabled="shadowUnEnable || item.isReadonly==1" @input="arrayItemChange($event,item)">
                                    <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(model)" style="font-size:20px;" title="指令发送" v-if="!shadowUnEnable || item.isReadonly==0"></el-button>
                                </el-input>
                            </div>
                            <div v-if="item.dataType.arrayType=='integer'">
                                <el-input type="integer" placeholder="请输入整数 " size="mini" v-model="model.shadow" :disabled="shadowUnEnable || item.isReadonly==1" @input="arrayItemChange($event,item)">
                                    <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(model)" style="font-size:20px;" title="指令发送" v-if="!shadowUnEnable || item.isReadonly==0"></el-button>
                                </el-input>
                            </div>
                        </el-descriptions-item>
                    </el-descriptions>
                    <el-collapse v-if="item.dataType.arrayType=='object'">
                        <el-collapse-item v-for="(arrayParam,index) in item.dataType.arrayParams" :key="index">
                            <template slot="title">
                                <span style="color:#666;"><i class="el-icon-tickets"></i> {{item.name+(index+1)}}</span>
                            </template>
                            <el-descriptions :column="1" size="mini" border>
                                <el-descriptions-item v-for="(param,index) in arrayParam" :key="index" :label="param.name">
                                    <div v-if="param.dataType.type=='bool'">
                                        <el-switch v-model="param.shadow" @change="mqttPublish(param)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" :disabled="(shadowUnEnable || param.isReadonly==1)" />
                                    </div>
                                    <div v-if="param.dataType.type=='enum'">
                                        <el-select v-model="param.shadow" placeholder="请选择" @change="mqttPublish(param)" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                            <el-option v-for="subItem in param.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                                        </el-select>
                                    </div>
                                    <div v-if="param.dataType.type=='string'">
                                        <el-input v-model="param.shadow" placeholder="请输入字符串" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                            <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                        </el-input>
                                    </div>
                                    <div v-if="param.dataType.type=='decimal'">
                                        <el-input v-model="param.shadow" type="number" placeholder="请输入小数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                            <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                        </el-input>
                                    </div>
                                    <div v-if="param.dataType.type=='integer'">
                                        <el-input v-model="param.shadow" type="integer" placeholder="请输入整数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                            <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                        </el-input>
                                    </div>
                                </el-descriptions-item>
                            </el-descriptions>
                        </el-collapse-item>
                    </el-collapse>
                </div>
            </div>
        </div>
    </div>
    </vue-draggable-resizable>
    <el-dialog
      title="历史记录"
      :visible.sync="dialogVisible"
      width="50%"
      :close-on-click-modal="false"
      :modal="false"
      v-if="dialogVisible"
    >
      <div class="flex jcsb">
        <el-date-picker
          v-model="daterangeTime"
          type="daterange"
          range-separator="—"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          size="mini"
        >
        </el-date-picker>
      </div>
      <div ref="chart" v-loading="chartLoading" class="w100 height-300"></div>
    </el-dialog>
  </div>
</template>

<script>
import { checkPermi} from "@/utils/permission";
import { listMonitor } from '@/api/iot/deviceLog';
import { listLayout,addLayout,delLayout } from '@/api/agriculture/layout';
import { chartOption } from './ChartOption';
export default {
  name: '',
  mixins: [],
  components: {},
  props: {
    device: {
      type: Object,
    },
  },
  data() {
    return {
      loading:false,
      draggable: false,
      layoutList:[],
      layout:{
          monitorList:[],
          controlList:[],
          img:null
      },
      formLayoutList:[],
      imgLayout:{x:300,y:10,w:300,h:400},
      //图标查询统计
      daterangeTime: null,
      queryParams: {
        serialNumber: null,
        identity: '',
        total: 200,
      },
      chartLoading: false,
      dialogVisible: false,
      //实时监测参数
      isMonitor:false,
      //重置按钮
      resetBtn:true,
      shadowUnEnable:false
    };
  },
  computed: {},
  watch: {
    device: {
      handler: async function (n, o) {
        if (n.productId && n.serialNumber) {
          await this.getLayoutList();
          this.formatLayout()
          this.connectMqtt();
        }
      },
      immediate: true,
    },
    daterangeTime: {
      handler: function () {
        this.initChart();
      },
      deep: true,
    },
  },
  created() {
  },
  mounted() {},
  destroyed() {},
  methods: {
    checkPermi,
    //获取产品布局
    async getLayoutList(){
        const { productId} = this.device
        if(productId){
           const { rows } = await listLayout({productId})
           this.layoutList = rows;
           if(rows.length>0){
            this.resetBtn=true;
           }
        }
    },
    formatLayout(){
        const { productId} = this.device
        if (this.device.monitorList && this.device.monitorList.length > 0) {
            this.layout.monitorList = this.device.monitorList.map((item, index) => ({
            productId,
            identifier:item.id,
            icon: item.id,
            name: item.name,
            value: item.value ? item.value : '-',
            unit: item.dataType.unit,
            color: this.$colorList[index],
            x:this.layoutList.find(i=>i.identifier==item.id)?this.layoutList.find(i=>i.identifier==item.id).x:0,
            y:this.layoutList.find(i=>i.identifier==item.id)?this.layoutList.find(i=>i.identifier==item.id).y:index*50+10,
            w:'auto',
            h:'auto'
          }))
        }
        if (this.device.thingsModels && this.device.thingsModels.length > 0) {
            this.layout.controlList = this.device.thingsModels.map((item, index) => ({
            productId,
            identifier:item.id,
            icon: item.id,
            name: item.name,
            value: item.value ? item.value : '-',
            unit: item.dataType.unit,
            color: this.$colorList[index],
            x:this.layoutList.find(i=>i.identifier==item.id)?this.layoutList.find(i=>i.identifier==item.id).x:600,
            y:this.layoutList.find(i=>i.identifier==item.id)?this.layoutList.find(i=>i.identifier==item.id).y:index*120+10,
            w:'auto',
            h:'auto',
            //发布需要，后端不会接受到该值
            dataType:item.dataType,
            id:item.id,
            shadow:item.shadow,
            type:item.type
          }))
        }
        this.layout.img={
            productId,
            identifier:'img',
            x:this.layoutList.find(item=>'img'==item.identifier)?this.layoutList.find(item=>'img'==item.identifier).x:300,
            y:this.layoutList.find(item=>'img'==item.identifier)?this.layoutList.find(item=>'img'==item.identifier).y:200,
            w:this.layoutList.find(item=>'img'==item.identifier)?this.layoutList.find(item=>'img'==item.identifier).w:200,
            h:this.layoutList.find(item=>'img'==item.identifier)?this.layoutList.find(item=>'img'==item.identifier).h:300,
          }
    },

    handleDragstop(a,identifier) {
        this.setValue(a,identifier)
    },
    handleResizestop(a,identifier){
        console.log(a)
        this.setValue(a,identifier)
    },
    setValue(a,identifier){
        console.log(identifier)
      for(let i =0;i<this.layout.monitorList.length;i++){
            if(this.layout.monitorList[i].identifier == identifier){
                this.layout.monitorList[i].x = a[0];
                this.layout.monitorList[i].y = a[1]
                if(a.length == 4){
                    this.layout.monitorList[i].w = a[2];
                    this.layout.monitorList[i].h = a[3]
                }
            }
        }
        for(let i =0;i<this.layout.controlList.length;i++){
            if(this.layout.controlList[i].identifier == identifier){
                this.layout.controlList[i].x = a[0];
                this.layout.controlList[i].y = a[1]
                if(a.length == 4){
                    this.layout.controlList[i].w = a[2];
                    this.layout.controlList[i].h = a[3]
                }
            }
        }
        if(identifier == 'img'){
            this.layout.img.x = a[0];
            this.layout.img.y = a[1];
            if(a.length == 4){
                this.layout.img.w = a[2];
                this.layout.img.h = a[3]
            }
        }
    },
    /** 布局保存 */
    async handleSave() {
        const { monitorList,controlList,img } = this.layout;
        let arr = [...monitorList,...controlList,img]
        // .map(item=>({
        //     productId:item.productId
        // }))
        console.log(arr)
        if(arr.length>0){
            this.loading = true;
            await addLayout(arr);
            this.$modal.msgSuccess("保存成功");
            this.loading = false;
            this.draggable = false
        }
    },
    /** 布局重置 */
    async handleReset() {
        this.loading = true;
        const { productId} = this.device;
        if(productId){
           await delLayout(productId)
           await this.getLayoutList()
           await this.formatLayout();
           this.$modal.msgSuccess("重置成功");
           this.loading = false;
            this.draggable = false
        }
    },
    //mqtt链接
    async connectMqtt() {
      if (this.$mqttTool.client == null) {
        await this.$mqttTool.connect();
      }
      this.mqttSubscribe();
      this.mqttCallback();
    },
    /** mqtt回调 */
    mqttCallback() {
      this.$mqttTool.client.on('message', (topic, message, buffer) => {
        let _message = JSON.parse(message.toString());
        const { monitorList } = this.layout;
        if (monitorList.length>0) {
          for (let i = 0; i < monitorList.length; i++) {
            for (let j = 0; j < _message.length; j++) {
              if (monitorList[i].identifier == _message[j].id) {
                monitorList[i].value = _message[j].value;
              }
            }
          }
        }
      });
    },
    /** Mqtt订阅主题 */
    mqttSubscribe() {
      // 订阅当前设备状态和实时监测
      const { device } = this;
      let topicStatus =
        '/' + device.productId + '/' + device.serialNumber + '/status/post';
      let topicProperty =
        '/' + device.productId + '/' + device.serialNumber + '/property/post';
      let topicFunction =
        '/' + device.productId + '/' + device.serialNumber + '/function/post';
      let topicMonitor =
        '/' + device.productId + '/' + device.serialNumber + '/monitor/post';
      let topics = [];
      topics.push(topicStatus);
      topics.push(topicProperty);
      topics.push(topicFunction);
      topics.push(topicMonitor);
      this.$mqttTool.subscribe(topics);
    },
      /** Mqtt发布  */
    mqttPublish(model) {
            let topic = "";
            let message = ""
            const { device } = this;
            switch (model.type) {
                case 1:
                    if (device.status == 3) {
                        // 属性,在线模式
                        topic = "/" + device.productId + "/" + device.serialNumber + "/property-online/get";
                    } else if (device.isShadow) {
                        // 属性,离线模式
                        topic = "/" + device.productId + "/" + device.serialNumber + "/property-offline/post";
                    }
                    message = `{"id":"${model.id}","value":"${model.shadow}"}`;
                    break;
                case 2:
                    if (device.status == 3) {
                        // 功能,在线模式
                        topic = "/" + device.productId + "/" + device.serialNumber + "/function-online/get";
                    } else if (device.isShadow) {
                        // 功能,离线模式
                        topic = "/" + device.productId + "/" + device.serialNumber + "/function-offline/post";
                    }
                    message = `[{"id":"${model.id}","value":"${model.shadow}","remark":"设备控制"}]`;
                    break;
                case 3:
                    topic = "/" + device.productId + "/" + device.serialNumber + "/ota/get";
                    message = '{"version":' + this.firmware.version + ',"downloadUrl":"' + this.getDownloadUrl(this.firmware.filePath) + '"}';
                    break;
                case 4:
                    topic = "/" + device.productId + "/" + device.serialNumber + "/monitor/get";
                    message = model.message;
                    break;
                default:
                    break;
            }
            if (topic != "") {
                // 发布
                this.$mqttTool.publish(topic, message, model.name).then(res => {
                    this.$modal.notifySuccess(res);
                }).catch(res => {
                    this.$modal.notifyError(res);
                });
            }
        },
    /**  处理图标查询点击事件 */
    handleChartClick(identity) {
      this.dialogVisible = true;
      this.queryParams.identity = identity;
      this.initChart();
    },
    async initChart() {
      this.queryParams.serialNumber = this.device.serialNumber;
      if (this.daterangeTime) {
        this.queryParams.beginTime = this.daterangeTime[0];
        this.queryParams.endTime = this.daterangeTime[1] + ' 23:59';
      }
      if (String(this.queryParams.identity)) {
        this.chartLoading = true;
        const { rows } = await listMonitor(this.queryParams);
        let times = [],values = [];
        rows.forEach((item) => {
          times.push(item.time);
          values.push(item.value);
        });
        chartOption.xAxis.data = times;
        chartOption.series[0].data = values;
        this.chartLoading = false;
        this.myChart = this.$echarts.init(this.$refs.chart);
        this.myChart.setOption(chartOption);
      }
    },
 /** 更新实时监测参数*/
        beginMonitor() {
            if (this.device.status != 3) {
                this.$modal.alertError("设备不在线，下发指令失败");
                return;
            }

            this.mqttPublish({name:'更新实时监测',type:4,message:'{"interval":1000}'});//毫秒
            this.isMonitor=true;
        },
        /** 停止实时监测 */
        stopMonitor() {
            if (this.device.status != 3) {
                this.$modal.alertError("设备不在线，下发指令失败");
                return;
            }
            this.mqttPublish({name:'关闭实时监测',type:4,message:'{"interval":0}'});
             this.isMonitor=false;
        },
  },
};
</script>
<style lang="scss" scoped>
.original {
    position:absolute;
  border: none;
}
.draggable {
  border: 1px dashed #b4b4b4;
}
.dragging {
  border: 2px dashed #33e642;
}
</style>
