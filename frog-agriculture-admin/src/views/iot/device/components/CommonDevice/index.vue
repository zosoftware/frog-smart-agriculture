<template>
  <div>
      <device-view :deviceId="deviceId">
          <template #p1="{device}">
              <cd-detail :device="device"></cd-detail>
          </template>
      </device-view>
  </div>
</template>

<script>
import DeviceView from '@/views/iot/device/components/DeviceView'
import CdDetail from './CdDetail.vue'
export default {
  name: 'VenturiWm',
  mixins: [],
  components: {DeviceView,CdDetail},
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
