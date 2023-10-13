<template>
  <div class="flex jcc aic hb">
      <el-empty description="非常抱歉,开源版本不包含此功能，请查看授权版">
          <el-button type="primary" size="default" @click="open">查看授权版</el-button>

      </el-empty>
  </div>
</template>

<script>
export default {
  name: '',
  mixins: [],
  components: {},
  props: {},
  data() {
    return{}
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
      open(){
          window.open("http://www.zosoft.top/docs/chanpinshouquan/chanpinshouquan.html",'_blank')
      }
  },
};
</script>
<style lang="scss" scoped>
</style>
