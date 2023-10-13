<template>
  <div>
      <!-- <weather-station v-if="productId == 99" :deviceId="deviceId"></weather-station> -->
      <common-device v-if="productId == 99" :deviceId="deviceId"></common-device>

      <venturi-wm v-if="productId == 100" :deviceId="deviceId"></venturi-wm>
      <bug-lamp v-if="productId == 101" :deviceId="deviceId"></bug-lamp>
      <common-device v-if="productId == 102" :deviceId="deviceId"></common-device>
  </div>
</template>

<script>
//设备组件
import WeatherStation from '@/views/iot/device/components/WeatherStation';
import BugLamp from '@/views/iot/device/components/BugLamp';
import VenturiWm from '@/views/iot/device/components/VenturiWm';
import CommonDevice from '@/views/iot/device/components/CommonDevice';
export default {
  name: '',
  mixins: [],
  components: {WeatherStation,BugLamp,VenturiWm,CommonDevice},
  props: {},
  data() {
    return{
        productId:null,
        deviceId:null
    }
  },
  computed: {},
  watch: {},
  created() {
      this.deviceId = this.$route.query.deviceId;
      this.productId = this.$route.query.productId;
      console.log(this.deviceId,this.productId)
  },
  mounted() {},
  destroyed() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
</style>
