<template>
  <div>
      <el-input v-model="form.name" placeholder="" size="normal" clearable @change="change"></el-input>
      <!-- {{form.age.ha}} -->

  </div>
</template>

<script>
export default {
  name: '',
  mixins: [],
  components: {},
  props: {},
  data() {
    return{
        form:{}
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
      change(){
          console.log(this.form.name);
      }
  },
};
</script>
<style lang="scss" scoped>
</style>
