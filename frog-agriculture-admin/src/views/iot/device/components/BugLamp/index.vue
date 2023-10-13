<template>
  <div>
      <device-view :deviceId="deviceId" :grey="true" :no-padding="true">
          <template #p1="{device}">
              <bl-detail :device="device"></bl-detail>
          </template>
      </device-view>
  </div>
</template>

<script>
import DeviceView from '@/views/iot/device/components/DeviceView'
import BlDetail from './BlDetail.vue'
export default {
  name: 'VenturiWm',
  mixins: [],
  components: {DeviceView,BlDetail},
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
