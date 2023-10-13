<template>
  <div class="w100 hbm150">
    <div class="w100 background-color-fff height-130 border-radius-10 padding-lr-15">
      <div class="display-inline-block margin-top-10">设备属性</div>
      <div class="flex jcsb padding-tb-20" style="overflow:auto">
        <div class="flex padding-lr-10" style="flex:0 0 auto" v-for="(item,index) in proList" :key="index">
            <div class="flex jcc aic width-50 height-50 border-radius-25" :style="{background:item.color}" >
            <svg-icon
                class="el-icon-s-help font-color-fff font-size-25"
                :icon-class="item.icon"
            ></svg-icon>
            </div>
            <div class="height-45 flex fdc jcsb margin-left-15">
                <span v-if="item.value" class="font-color-l1 font-size-24 ont-weight-500">{{item.value}}</span>
                 <span v-if="item.text" class="font-color-l1 font-size-18 font-weight-500 ">{{item.text}}</span>
                <span class="font-color-l2 font-size-13">{{item.label}}</span>
            </div>
        </div>
      </div>
    </div>
    <div class="flex">
      <div class="flex fdc hbm295 margin-top-15 flex1">
        <div class=" flex fdc flex1 height-0 background-color-fff border-radius-10 padding-15">
            <div class="height-20">
                <span>虫害照片</span>
            </div>
            <div class="flex1 height-0 overflow-auto flex aic">
                <el-image v-for="(item,index) in imgList" :key="index" style="flex:0 0 auto"
                    class="h80 margin-right-10"
                    :src="item"
                    :preview-src-list="imgList">
                </el-image>
            </div>
        </div>
        <div class="height-15"></div>
        <div class="flex fdc flex1 height-0 background-color-fff border-radius-10 padding-15">
            <div class="height-20">
                <span>虫害统计</span>
            </div>
            <div class="flex1 height-0 overflow-auto flex aic" ref="chart">
            </div>
        </div>
      </div>

      <div class="hbm295 width-15"></div>
      <div class="hbm295 margin-top-15 background-color-fff border-radius-10 flex1 padding-15">
          <div class="height-20 flex jcsb aic">
              <span>设备属性</span>
              <el-button-group >
                <el-button type="info"  size="mini" >读取配置</el-button>
                <el-button type="primary" size="mini" >保存配置</el-button>
          </el-button-group>
          </div>
          <div class="flex">
            <div class="hbm345 w40 flex aic jcc ">
                <img :src="$baseUrl+device.imgUrl.split(',')[0]" class="w100"/>
            </div>
            <div class="hbm345 w60">
                <el-form :model="form" ref="form"  label-width="120px" :inline="false" size="normal" class="h95 flex fdc jcsa margin-top-20">
                    <el-form-item label="工作模式" prop="workType">
                       <el-select v-model="form.workType" value-key="" placeholder="" clearable filterable size="mini" class="display-block">
                           <el-option v-for="item in [{label:'光控',value:1},{label:'时控',value:2}]"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                           </el-option>
                       </el-select>
                    </el-form-item>
                    <el-form-item label="定时时长" size="normal" prop="time">
                        <el-input v-model="form.time" placeholder="" size="mini" type="number" clearable></el-input>

                    </el-form-item>
                    <el-form-item label="工作时间" size="normal" prop="workRange">
                         <el-time-picker class="display-block" style="width: 100%"
                            size="mini"
                            is-range
                            v-model="form.workRange"
                            range-separator="至"
                            start-placeholder="开始时间"
                            end-placeholder="结束时间"
                            placeholder="选择时间范围">
                        </el-time-picker>
                    </el-form-item>
                    <el-form-item label="虫落时间" size="normal" prop="bugTime">
                        <el-slider v-model="form.bugTime" :min="5" :max="30" :step="5" :vertical="false" show-stops >
                        </el-slider>
                    </el-form-item>
                    <el-form-item label="加热时间" size="normal" prop="jiareTime">
                        <el-slider v-model="form.jiareTime" :min="5" :max="30" :step="5" :vertical="false" show-stops >
                        </el-slider>
                    </el-form-item>
                    <el-form-item label="加热温度" size="normal" prop="jiareTemp">
                        <el-slider v-model="form.jiareTemp" :min="5" :max="30" :step="5" :vertical="false" show-stops >
                        </el-slider>
                    </el-form-item>
                     <el-form-item label="高温保护阀值" size="normal" prop="gwbh">
                        <el-slider v-model="form.gwbh" :min="40" :max="70" :step="5" :vertical="false" show-stops >
                        </el-slider>
                    </el-form-item>
                    <el-form-item label="低温保护阀值" size="normal" prop="dwbh">
                        <el-slider v-model="form.dwbh" :min="-5" :max="10" :step="5" :vertical="false" show-stops >
                        </el-slider>
                    </el-form-item>
                    <el-form-item label="数据上传间隔" size="normal" prop="uploadGap">
                        <el-slider v-model="form.uploadGap" :min="1" :max="10" :step="1" :vertical="false" show-stops >
                        </el-slider>
                    </el-form-item>
                </el-form>

            </div>
          </div>


      </div>
    </div>
  </div>
</template>

<script>
import {lineChart,pieChart,barChart} from "@/views/components/DataChart";
export default {
  name: 'BlDetail',
  mixins: [],
  components: {},
  props: {
      device:{
          type:Object,
          default:function(){
              return {
                  imgUrl:''
              }
          }
      }
  },
  data() {
    return {
        form:{},
        proList:[{
                icon:'kqwd',
                label:'环境温度 (℃)',
                value:'25',
                color:'#D68410'
            },{
                icon:'kqsd',
                label:'环境湿度 (%RH)',
                value:'30',
                color:'#80BF2F'
            },{
                icon:'kqwd',
                label:'加热仓温度 (°C)',
                value:'68',
                color:'#EBAC53'
            },{
                icon:'yl',
                label:'雨控状态',
                text:'正常',
                color:'#47B770'
            },{
                icon:'gz',
                label:'光照状态',
                text:'正常',
                color:'#486FE1'
            },{
                icon:'kqwd',
                label:'温控状态',
                text:'正常',
                color:'#5F3DBC'
            },{
                icon:'jr',
                label:'加热状态',
                text:'正常',
                color:'#CFD94A'
            },{
                icon:'ps',
                label:'通道状态',
                text:'正常',
                color:'#E9754F'
            },{
                icon:'bb',
                label:'设备版本',
                value:'5.9',
                color:'#2DD4D2'
            },{
                icon:'wifi',
                label:'信号强度',
                value:'25',
                color:'#D68410'
        }],
        imgList: [
          'https://img.agropages.com/UserFiles/FCKFile/zkc_2018-04-12_10-53-36_746.jpg',
          'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcbu01.alicdn.com%2Fimg%2Fibank%2F2015%2F684%2F198%2F2465891486_1655619071.jpg%3F__r__%3D1452522304035&refer=http%3A%2F%2Fcbu01.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1694312956&t=ed59f86a1c59b724c516d39d9ca74a8f',
          'https://img2.baidu.com/it/u=1940740141,973708377&fm=253&fmt=auto&app=138&f=JPEG?w=670&h=446',

        ]
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
      lineChart(this.$refs.chart,'虫害统计',{name:'个数'},['1月','2月','3月','4月'],[10,30,15,7,9],'line')
  },
  destroyed() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.el-form-item{
    margin-bottom:0;
}
</style>
