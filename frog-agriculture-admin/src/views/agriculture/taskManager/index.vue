<template>
  <div class="app-container-gray">
    <el-row class="box">
      <el-col :span="4" class="left">
        <search-menu title="任务" :fun="listBatch" search-parma="batchName" search-placeholder="请输入批次名称" key-name="batchId" label-name="batchName" @select="handleSelect" />
      </el-col>
      <el-col :span="20" class="right">
        <task :batch-id="batchId"></task>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import Task from "../components/Task";
import SearchMenu from "../components/SearchMenu";
import { listLand } from "@/api/agriculture/land";
import { listBatch} from "@/api/agriculture/batch";
export default {
  components: { Task,SearchMenu },
  data() {
    return {
      listBatch,
      batchId:null
    };
  },
  created(){
  },
  methods:{

    /** 处理菜单的点击 */
    handleSelect(e){
      this.batchId = Number(e.batchId);
    }
  },
};
</script>
<style lang="scss" scoped>
.box {
  padding: 15px;
}
.left {
  background: #fff;
  border-radius: 10px 0 0 10px;
}
.right {
  background: #fff;
  height:calc(100vh - 84px - 30px);
  border-radius: 0 10px 10px 0;
  border-left: 1px solid #eee;
  padding: 0 15px;
  overflow: auto;
}
</style>
