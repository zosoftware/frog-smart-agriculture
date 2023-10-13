<template>
  <div></div>
</template>

<script>
export default {
  name: '',
  mixins: [],
  components: {},
  props: {},
  data() {
    return{
        name:'name'
    }
  },
  computed: {
      comname(){
          return 123
      }
  },
  watch: {
      name:{
          handler:function(){
              console.log(this.name)
              console.log(this.aa)
              console.log(this.comname)
          },
          immediate:true
      }
  },
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
       aa(){
           console.log()
       }
  },
};
</script>
<style lang="scss" scoped>
</style>
