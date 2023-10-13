<template>
  <div>
    <div class="header">
      <div class="left">
        <img class="img" :src="$baseUrl+device.imgUrl.split(',')[0]" alt="" />
      </div>
      <div class="right">
        <div class="name">{{ device && device.deviceName }}</div>
        <div class="des"><span>编号:</span> {{ device && device.serialNumber }} <el-tag class="margin-left-10" :type="deviceStatus.color" size="mini">{{deviceStatus.title}}</el-tag></div>

      </div>
    </div>
    <div class="body">
      <el-tabs v-model="activeName" @tab-click="handleTabClick">
        <el-tab-pane label="采集数据" name="p1">
          <div class="content-box">
            <div class="content" :class="{'grey':grey,'padding0':noPadding}" >
              <slot name="p1" :device="device">
              </slot>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="定时任务" name="p2">
          <div class="content-box">
            <div class="content">
                <slot name="p2">
                    <device-timer class="margin-top-20" :device="device" ></device-timer>
                </slot>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="设备用户" name="p3">
          <div class="content-box">
            <div class="content">
                <slot name="p3">
                    <device-user class="margin-top-20" :device="device"></device-user>
                </slot>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="设备日志" name="p4">
          <div class="content-box">
            <div class="content">
              <slot name="p4">
                 <device-log class="margin-top-20" :device="device"></device-log>
              </slot>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="数据分析" name="p5">
          <div class="content-box">
            <div class="content">
                <slot name="p5">
                <device-statistic
                    :chart-visible="chartVisible"
                    class="margin-top-20"
                    :device="device"
                ></device-statistic>
                </slot>
            </div>
          </div>
        </el-tab-pane>
        <slot name="tabs">
        </slot>
      </el-tabs>
    </div>
  </div>
</template>
<script>
//设备组件
// import WeatherStation from '@/views/iot/device/components/WeatherStation';
// import BugLamp from '@/views/iot/device/components/BugLamp';
// import VenturiWm from '@/views/iot/device/components/VenturiWm';


//数据组件
import DeviceForm from '@/views/iot/device/device-form.vue';
import DeviceTimer from '@/views/iot/device/device-timer.vue';
import DeviceUser from '@/views/iot/device/device-user.vue';
import DeviceLog from '@/views/iot/device/device-log.vue';
import DeviceStatistic from '@/views/iot/device/device-statistic.vue';
import { getDevice } from '@/api/iot/device';
import { cacheJsonThingsModel } from '@/api/iot/model';
import { formatThingsModel } from './FormatThingsModel';
import { getDeviceRunningStatus } from '@/api/iot/device';
export default {
  name: 'DeviceView',
  components: { DeviceForm,DeviceTimer ,DeviceUser,DeviceLog,DeviceStatistic},
  props:{
      deviceId:[Number,String],
      grey:{
          type:Boolean,
          default:false
      },
      noPadding:false
  },
  data() {
    return {
      device: {
          imgUrl:''
      },
      activeName: 'p1',
      chartVisible: false,
    };
  },
  computed:{
    deviceStatus() {
        const {device} = this;
        if (device.status == 3) {
            return{
                title:'在线模式',
                color:'primary'
            }
        } else {
            if (device.isShadow == 1) {
                return{
                    title:'影子模式',
                    color:'warning'
                }
            } else {
                return{
                    title:'离线模式',
                    color:'danger'
                }
            }
        }
    },
  },
  created() {
    this.deviceId && this.getDevice();
  },
  methods: {
      handleTabClick(e) {
      if (e.paneName == 'p5') {
        this.chartVisible = true;
      }
    },
    /**获取设备详情*/
    async getDevice() {
      const { deviceId } = this;
      getDevice(deviceId).then(async (response) => {
        // 获取缓存物模型
        response.data.cacheThingsModel = await this.getCacheThingsModdel(response.data.productId);
        // 获取设备运行状态：从缓存去除屋物模型和缓存中的上报的值
        response.data.thingsModels = await this.getDeviceStatus(deviceId);
        // 格式化物模型，拆分出监测值,数组添加前缀
        formatThingsModel(response.data);
        this.device = response.data;
        // 解析设备摘要
        if (this.device.summary != null && this.device.summary != '') {
          this.summary = JSON.parse(this.device.summary);
        }
      });
    },
    /** 获取缓存物模型*/
    getCacheThingsModdel(productId) {
      return new Promise((resolve, reject) => {
        cacheJsonThingsModel(productId)
          .then((response) => {
            resolve(JSON.parse(response.data));
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    /**获取设备运行状态*/
    getDeviceStatus(deviceId) {
      return new Promise((resolve, reject) => {
        getDeviceRunningStatus(deviceId)
          .then((response) => {
            resolve(response.data.thingsModels);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
};
</script>
<style lang="scss" scoped>
   .header {
    display: flex;
    padding: 15px;
    .left {
      .img {
        width: 50px;
        height: 50px;
        border-radius: 10px;
        margin-right: 10px;
        vertical-align: bottom;
      }
    }
    .right {
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      .name {
        font-size: 15px;
      }
      .des {
        font-size: 13px;
        color: #7c7c7c;
      }
    }
  }
  .body {
    .content-box {
      background: #f0f0f0;
      height: calc(100vh - 120px - 84px);
      padding: 0 15px;
      overflow: hidden;
      display: flex;
      align-items: center;
      .content {
        height: calc(100vh - 120px - 84px - 30px);
        background: #fff;
        border-radius: 10px;
        box-sizing: border-box;
        flex: 1;
        padding: 0 20px;
        overflow: auto;
        &.grey{
             background: #f0f0f0 !important;
        }
        &.padding0{
            padding:0 !important;
        }
      }

    }
  }
::v-deep {
  .el-tabs__header {
    margin-bottom: 0;
  }
  .el-tabs__header {
    margin-left: 15px;
  }
}

</style>
