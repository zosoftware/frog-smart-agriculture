<template>
  <div>
    <template v-for="(item, index) in dataList">
      <template v-if="values.includes(item.value)">

        <span
          v-if="type=='notag'"
          :key="item.value"
          :index="index"
          >{{ item.label }}</span
        >
        <el-tag
          v-else
          :disable-transitions="true"
          :key="item.value"
          :index="index"
          :type="type"
        >
          {{ item.label }}
        </el-tag>
      </template>
    </template>
  </div>
</template>

<script>
export default {
  name: "DictTag",
  props: {
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
    // notag:无标签样式 空：主题色 success：绿色 info：灰色 warning：黄色 danger：红色
    type:{
      type:String,
      default:'notag'
    },
    valueName:[String],
    labelName:[String]
  },
  computed: {
    values() {
      if (this.value !== null && typeof this.value !== 'undefined') {
        return Array.isArray(this.value) ? this.value : [String(this.value)];
      } else {
        return [];
      }
    },
    dataList(){
      if(this.options !== null && typeof this.options !== 'undefined' && Array.isArray(this.options)){
        return this.options.map(item=>({label:item[this.labelName],value:String(item[this.valueName])}));
      }else{
        return [];
      }
    }
  },
};
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>