<template>
  <div class="weather flex jcc aic">
    <div class="c_box">
      <div class="c_img flex fdc">
        <img src="@/assets/images/weather1.gif" alt="" />
        <img src="@/assets/images/weather2.png" alt="" />
      </div>
      <div class="c_left weather">
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">风向</div>
          <div class="value flex aic jcc">
            {{ getProperty('风向', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">日照时数</div>
          <div class="value flex aic jcc">
            {{ getProperty('日照时数', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">总辐射</div>
          <div class="value flex aic jcc">
            {{ getProperty('总辐射', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">PM10</div>
          <div class="value flex aic jcc">
            {{ getProperty('PM10', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">光照强度</div>
          <div class="value flex aic jcc">
            {{ getProperty('光照强度', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">PM2.5</div>
          <div class="value flex aic jcc">
            {{ getProperty('PM2.5', 'value') }}
          </div>
        </div>
        <div class="weather_item"></div>
        <div class="weather_item"></div>
        <div class="weather_item">
          <div class="label flex aic jcc">蒸发量</div>
          <div class="value flex aic jcc">
            {{ getProperty('蒸发量', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">降雨量</div>
          <div class="value flex aic jcc">
            {{ getProperty('降雨量', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">液位</div>
          <div class="value flex aic jcc">
            {{ getProperty('液位', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">液体电导率</div>
          <div class="value flex aic jcc">
            {{ getProperty('液体电导率', 'value') }}
          </div>
        </div>
      </div>
      <div class="c_right weather">
        <div
          class="weather_item"
          @click="handleChartClick(getProperty('风速', 'id'))"
        >
          <div class="label flex aic jcc">风速</div>
          <div class="value flex aic jcc">
            {{ getProperty('风速', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">光合有效辐射</div>
          <div class="value flex aic jcc">
            {{ getProperty('光合有效辐射', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">二氧化碳浓度</div>
          <div class="value flex aic jcc">
            {{ getProperty('二氧化碳浓度', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">紫外线传感器</div>
          <div class="value flex aic jcc">
            {{ getProperty('紫外线传感器', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">空气温度</div>
          <div class="value flex aic jcc">
            {{ getProperty('空气温度', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">空气湿度</div>
          <div class="value flex aic jcc">
            {{ getProperty('空气湿度', 'value') }}
          </div>
        </div>
        <div class="weather_item"></div>
        <div class="weather_item"></div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">大气压</div>
          <div class="value flex aic jcc">
            {{ getProperty('大气压', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">液体压力</div>
          <div class="value flex aic jcc">
            {{ getProperty('液体压力', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">水温</div>
          <div class="value flex aic jcc">
            {{ getProperty('水温', 'value') }}
          </div>
        </div>
        <div
          class="weather_item"
          @click="getMonitorList(getProperty('风向', 'id'))"
        >
          <div class="label flex aic jcc">氨气</div>
          <div class="value flex aic jcc">
            {{ getProperty('氨气', 'value') }}
          </div>
        </div>
      </div>
      <div class="c_bot">
        <div class="land_item">
          <div class="label flex aic jcc">土壤湿度</div>
          <div class="value flex aic jcc">
            {{ getProperty('土壤湿度', 'value') }}
          </div>
        </div>
        <div class="land_item">
          <div class="label flex aic jcc">土壤温度</div>
          <div class="value flex aic jcc">
            {{ getProperty('土壤温度', 'value') }}
          </div>
        </div>
        <div class="land_item">
          <div class="label flex aic jcc">土壤导电率</div>
          <div class="value flex aic jcc">
            {{ getProperty('土壤导电率', 'value') }}
          </div>
        </div>
        <div class="land_item">
          <div class="label flex aic jcc">土壤盐分</div>
          <div class="value flex aic jcc">
            {{ getProperty('土壤盐分', 'value') }}
          </div>
        </div>
        <div class="land_item">
          <div class="label flex aic jcc">土壤PH值</div>
          <div class="value flex aic jcc">
            {{ getProperty('土壤PH值', 'value') }}
          </div>
        </div>
      </div>
    </div>

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
      <div ref="chart" v-loading="chartLoading" class="chart"></div>
    </el-dialog>
  </div>
</template>

<script>
import { listMonitor } from '@/api/iot/deviceLog';
import { chartOption } from './ChartOption';
export default {
  name: 'WsDetail',
  props: {
    device: {
      type: Object,
      default: function () {
        return null;
      },
    },
  },
  data() {
    return {
      monitorList: null,
      dialogVisible: false,
      daterangeTime: null,
      thingsId: null,
      chartLoading: false,
      queryParams: {
        serialNumber: null,
        identity: '',
        total: 200,
      },
    };
  },
  computed: {
    getProperty: function () {
      let monitorList = this?.monitorList;
      return function (name, type) {
        if (monitorList) {
          let item = monitorList.find((item) => item.name == name);
          if (item) {
            if (type == 'value') {
              return item.value
                ? item.value + item.dataType.unit
                : '--' + item.dataType.unit;
            } else if (type == 'id') {
              return item.id;
            } else {
              return '';
            }
          } else {
            return '无设备';
          }
        }
      };
    },
  },
  watch: {
    device: {
      handler: function (n, o) {
        if (Object.keys(this.device).length > 3) {
            console.log(this.device);
          this.monitorList = this.device?.monitorList;
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
  mounted() {},
  methods: {
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
        const { monitorList } = this;
        if (monitorList) {
          for (let i = 0; i < monitorList.length; i++) {
            for (let j = 0; j < _message.length; j++) {
              if (monitorList[i].id == _message[j].id) {
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

    handleChartClick(thingsId) {
      this.dialogVisible = true;
      this.thingsId = thingsId;
      this.initChart();
    },
    async initChart() {
      const { thingsId } = this;
      this.queryParams.serialNumber = this.device.serialNumber;
      this.queryParams.identity = thingsId;
      if (this.daterangeTime) {
        this.queryParams.beginTime = this.daterangeTime[0];
        this.queryParams.endTime = this.daterangeTime[1] + ' 23:59';
      }
      if (String(thingsId)) {
        this.chartLoading = true;
        const { rows } = await listMonitor(this.queryParams);
        let times = [],
          values = [];
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
  },
};
</script>

<style lang="scss" scoped>
.weather {
  height: 100%;
  width: 100%;
  user-select: none;
  .c_box {
    width: 85%;
    height: 0;
    padding-bottom: 38%;
    position: relative;
    .c_img {
      width: 30%;
      height: auto;
      overflow: hidden;
      position: absolute;
      left: 50%;
      top: 0;
      transform: translate(-50%, 0);
      img {
        width: 100%;
        height: auto;
      }
    }
    .c_left {
      height: 75%;
      position: absolute;
      left: 0;
    }
    .c_right {
      height: 76%;
      position: absolute;
      right: 0;
    }
    .c_bot {
      height: 20%;
      width: 100%;
      position: absolute;
      bottom: 0;
      left: 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .land_item {
        cursor: pointer;
        height: 50%;
        width: 13%;
        border-radius: 5px;
        border: 1px solid #e0e0e0;
        box-sizing: border-box;
        overflow: hidden;
        .label {
          height: 45%;
          color: #999;
          background: #f4f6f9;
          font-size: 0.5em;
        }
        .value {
          height: 55%;
          background: #fff;
          color: #1e88e5;
          font-size: 0.7em;
        }
        &:nth-of-type(7) {
          visibility: hidden;
        }
        &:nth-of-type(8) {
          visibility: hidden;
        }
      }
    }
    .weather {
      width: 28%;
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
      .weather_item {
        cursor: pointer;
        height: 13%;
        width: 47%;
        border-radius: 5px;
        border: 1px solid #e0e0e0;
        box-sizing: border-box;
        overflow: hidden;
        .label {
          height: 45%;
          color: #999;
          background: #f4f6f9;
          font-size: 0.5em;
        }
        .value {
          height: 55%;
          background: #fff;
          color: #1e88e5;
          font-size: 0.7em;
        }
        &:nth-of-type(7) {
          visibility: hidden;
        }
        &:nth-of-type(8) {
          visibility: hidden;
        }
      }
    }
  }
  .chart {
    width: 100%;
    height: 300px;
  }
}
</style>
