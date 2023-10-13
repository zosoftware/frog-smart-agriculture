<template>
  <div>
      <device-view :deviceId="deviceId">
          <template #p1="{device}">
              <ws-detail :device="device"></ws-detail>
          </template>
      </device-view>
  </div>
</template>

<script>
import DeviceView from '@/views/iot/device/components/DeviceView'
import WsDetail from './WsDetail.vue'
export default {
  name: 'WeatherStation',
  mixins: [],
  components: {DeviceView,WsDetail},
  props: {
      deviceId:[Number,String]
  },
  data() {
    return{}
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
</style>
