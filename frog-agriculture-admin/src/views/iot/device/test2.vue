<template>
  <div>
      <multi-player :video-param="videoParam" width="700px;" style="height:500px;"></multi-player>
      <el-button class="margin-left-100" type="primary" size="default" @click="handleClick(0)">设备1</el-button>
      <el-button type="primary" size="default" @click="handleClick(1)">设备2</el-button>
  </div>
</template>

<script>
import MultiPlayer from '@/views/components/player/MultiPlayer';
export default {
  name: '',
  mixins: [],
  components: {MultiPlayer},
  props: {},
  data() {
    return{
        videoList:[{
            deviceId:'32090200001310000001',
            channelId:'32090200001320000009',
            id:1
        },{
            deviceId:'32090200001310000001',
            channelId:'32090200001320000009',
            id:2
        }],
        videoParam:{name:'qiaojun'}

    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
      handleClick(i){
          this.videoParam = this.videoList[i];
      }
  },
};
</script>
<style lang="scss" scoped>
</style>
