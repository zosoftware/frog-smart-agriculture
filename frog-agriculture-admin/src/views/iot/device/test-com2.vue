<template>
  <div></div>
</template>

<script>
export default {
  name: 'TestCom2',
  mixins: [],
  components: {},
  props: {
      obj:{
          type:Object
      }
  },
  data() {
      return{}
  },
  computed: {},
  watch: {
      obj:{
          handler:function(n,o){
              console.log('test-com2',n);
          },
          deep:true
      }
  },
  created() {},
  mounted() {},
  destroyed() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
</style>
