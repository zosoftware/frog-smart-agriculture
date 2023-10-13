<template>
  <div>{{getName}}</div>
</template>

<script>
export default {
  name: 'TestCom',
  mixins: [],
  components: {},
  props: {},
  data() {
      return{
          name:'yapqiong'
      }
  },
  computed: {
      getName(){
          console.log('子computed');
      }
  },
  watch: {
      name:{
          handler(){
              console.log('子watch')
          },
          immediate:true
      }
  },
  beforeCreate(){
      console.log('子beforeCreate',this.name)
  },
  created() {
      console.log('子created',this.name)
  },
  beforeMount(){
      console.log('子beforeMount',this.name)
  },
  mounted() {
      console.log('子mounted',this.name)
  },
  beforeDestroy(){
    console.log('子beforeDestroy',this.name)
  },
  destroyed() {
      console.log('子destroyed',this.name)
  },
  methods: {},
};
</script>
<style lang="scss" scoped>
</style>
