<template>
  <div>
      <device-view :deviceId="deviceId">
          <template #p1="{device}">
              <vm-detail :device="device"></vm-detail>
          </template>
      </device-view>
  </div>
</template>

<script>
import DeviceView from '@/views/iot/device/components/DeviceView'
import VmDetail from './VmDetail.vue'
export default {
  name: 'VenturiWm',
  mixins: [],
  components: {DeviceView,VmDetail},
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
