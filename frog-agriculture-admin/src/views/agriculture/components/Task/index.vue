<template>
  <div class="gantt-container">
    <div class="search">
      <el-form
        :model="queryParams"
        ref="queryForm"
        :inline="true"
        v-show="showSearch"
        label-width="100px"
      >
        <el-form-item label="任务名称" prop="taskName">
          <el-input
            v-model="queryParams.taskName"
            placeholder="请输入任务名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="计划开始日期" prop="psr">
          <el-date-picker
            size="small"
            v-model="queryParams.psr"
            type="daterange"
            range-separator="-"
            start-placeholder="时间范围开始"
            value-format="yyyy-MM-dd"
            end-placeholder="时间范围结束"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
        </el-form-item>
        <el-form-item class="fr">
          <!-- <el-button
                    type="warning"
                    plain
                    icon="el-icon-download"
                    size="mini"
                    @click="handleExport"
                    v-hasPermi="['agriculture:task:export']"
            >导出</el-button> -->
          <el-button-group>
            <el-button
              type="primary"
              @click="handleChange('table')"
              :plain="gantt"
              size="mini"
              icon="el-icon-s-order"
            ></el-button>
            <el-button
              type="primary"
              @click="handleChange('gantt')"
              :plain="!gantt"
              size="mini"
              icon="el-icon-s-unfold"
            ></el-button>
          </el-button-group>
        </el-form-item>
      </el-form>
    </div>
    <div class="table">
      <el-table
        v-if="!gantt"
        v-loading="loading"
        :data="taskList"
        :border="tableBorder"
      >
        <el-table-column label="任务名称" align="center" prop="taskName" />
        <el-table-column
          label="计划开始日期"
          align="center"
          prop="planStart"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.planStart, "{y}-{m}-{d}") }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="计划结束日期"
          align="center"
          prop="planFinish"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.planFinish, "{y}-{m}-{d}") }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="实际开始日期"
          align="center"
          prop="actualStart"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.actualStart, "{y}-{m}-{d}") }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="实际结束日期"
          align="center"
          prop="actualFinish"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.actualFinish, "{y}-{m}-{d}") }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="任务详情" align="center" prop="taskDetail" />
      <el-table-column label="图片资料" align="center" prop="taskImages" />
      <el-table-column label="视频资料" align="center" prop="taskVideos" /> -->
        <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
          <dict-tag
            :options="dict.type.agriculture_batch_task_status"
            :value="scope.row.status"
          />
        </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="200"
          class-name="small-padding fixed-width"
        >
          <template #header>
              <el-button class="width-90" v-if="tableBorder" type="primary" :disabled="!checkPermi(['agriculture:batchTask:add'])" size="mini" plain icon="el-icon-plus" @click="handleAdd">新增任务</el-button>
                <span v-else>操作</span>
          </template>
          <template slot-scope="scope">
            <el-button
              size="small"
              type="primary"
              class="padding-5"
              plain
              icon="el-icon-edit"
              @click="handleTask(scope.row.taskId)"
              v-if="!tableBorder"
              v-hasPermi="['agriculture:batchTask:query']"
              >任务处理</el-button
            >
            <el-button
              size="small"
              type="primary"
              class="padding-5"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-if="tableBorder"
              v-hasPermi="['agriculture:batchTask:edit']"
              >修改</el-button
            >
            <el-button
              size="small"
              type="danger"
              class="padding-5"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['agriculture:batchTask:remove']"
              v-if="tableBorder"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <gantt v-else class="gantt" :tasks="tasks"></gantt>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <!-- 添加或修改批次任务对话框 -->
    <el-dialog :title="title2" :visible.sync="open2" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <!-- <el-form-item label="批次ID" prop="batchId">
          <el-input v-model="form.batchId" placeholder="请输入批次ID" />
        </el-form-item> -->
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="计划开始日期"  prop="planStart">
          <el-date-picker clearable class="w100"
            v-model="form.planStart"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择计划开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="计划结束日期" prop="planFinish">
          <el-date-picker clearable class="w100"
            v-model="form.planFinish"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择计划结束日期">
          </el-date-picker>
        </el-form-item>
        <!-- <el-form-item label="实际开始日期" prop="actualStart">
          <el-date-picker clearable size="small"
            v-model="form.actualStart"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择实际开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实际结束日期" prop="actualFinish">
          <el-date-picker clearable size="small"
            v-model="form.actualFinish"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择实际结束日期">
          </el-date-picker>
        </el-form-item> -->
        <el-form-item label="任务详情" prop="taskDetail">
          <el-input v-model="form.taskDetail" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel2">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改批次任务对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1100px"
      append-to-body
      v-if="open"
      @close="cancel"
    >
      <task-detail :opr-type="oprType" :task-id="taskId"></task-detail>
    </el-dialog>
  </div>
</template>

<script>
import {checkPermi} from '@/utils/permission';
import { listBatchTask,getBatchTask,addBatchTask,updateBatchTask, delBatchTask } from "@/api/agriculture/batchTask";
import TaskDetail from "../TaskDetail";
import Gantt from "@/components/Gantt";
export default {
  name: "BatchTask",
  components: { TaskDetail, Gantt },
  props: {
    batchId: [Number],
    tableBorder: {
      type: Boolean,
      default: false,
    },
  },
  dicts:['agriculture_batch_task_status'],
  data() {
    return {
      //gantt
      gantt: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 批次任务表格数据
      taskList: [],
      open:false,
      title:'',
      //任务id
      taskId: null,
      //操作类型：add和update
      oprType: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskName: null,
        planStart: null,
        planStartStart: null,
        planStartEnd: null,
        planFinish: null,
        batchId: this.batchId,
        psr: null,
      },
      tasks: {
        data: [],
      },
      title2:'',
      open2:false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        taskName: [
          { required: true, message: "任务名称不能为空", trigger: "blur" }
        ],
        planStart: [
          { required: true, message: "计划开始日期不能为空", trigger: "blur" }
        ],
        planFinish: [
          { required: true, message: "计划结束日期不能为空", trigger: "blur" }
        ],
        actualStart: [
          { required: true, message: "实际开始日期不能为空", trigger: "blur" }
        ],
        actualFinish: [
          { required: true, message: "实际结束日期不能为空", trigger: "blur" }
        ],
      }
    };
  },
  watch: {
    batchId: {
      handler: function (n, o) {
        this.queryParams.batchId = n;
        this.getList();
      },
    }
  },
  created() {
    this.getList();
    gantt.config.columns = [
      { name: "text", label: "任务名称", tree: true, width: "200" },
    ];

    gantt.attachEvent("onTaskDblClick",(id, e)=> {
      this.handleTask(Number(id))//TODO: 【项目】导致甘特图左移
      return false;
    });
  },
  methods: {
    checkPermi,
    /** 查询批次任务列表 */
    getList() {
      this.loading = true;
      const {
        queryParams: { psr },
      } = this;
      if (psr && Array.isArray(psr) && psr.length == 2) {
        this.queryParams.params = {
          planStartStart: psr[0],
          planStartEnd: psr[1],
        };
      }else{
          this.queryParams.params = {
          planStartStart: null,
          planStartEnd: null,
        };
      }
      listBatchTask(this.queryParams).then((response) => {
        this.taskList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.tasks.data = this.taskList.map((item) => ({
          text: item.taskName,
          id: item.taskId,
          start_date: new Date(`${item.planStart} 00:00:00`),
          end_date: new Date(`${item.planFinish} 00:00:00`),
          color: "#2b7",
        }));
      });
    },
    // 取消按钮
    cancel2() {
      this.open2 = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        taskId: null,
        batchId: null,
        taskName: null,
        planStart: null,
        planFinish: null,
        actualStart: null,
        actualFinish: null,
        taskDetail: null,
        taskImages: null,
        taskVideos: null,
        remark: null,
        status: "0",
        orderNum: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 处理任务按钮 */
    handleTask(taskId) {
      this.taskId = taskId;
      this.oprType = 'update';
      this.open=true;
      this.title="编辑任务";
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open2 = true;
      this.title2 = "添加批次任务";
      this.form.batchId = this.batchId;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getBatchTask(taskId).then(response => {
        this.form = response.data;
        this.open2 = true;
        this.title2 = "修改批次任务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.taskId != null) {
            updateBatchTask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open2 = false;
              this.getList();
            });
          } else {
            addBatchTask(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open2 = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const taskIds = row.taskId || this.ids;
      this.$modal
        .confirm('是否确认删除批次任务编号为"' + taskIds + '"的数据项？')
        .then(function () {
          return delBatchTask(taskIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "agriculture/task/export",
        {
          ...this.queryParams,
        },
        `task_${new Date().getTime()}.xlsx`
      );
    },
    /** 处理切换按钮操作 */
    handleChange(type) {
      if (type == "gantt") {
        this.gantt = true;
      } else {
        this.gantt = false;
      }
    },

    cancel() {
      this.getList();
    },
  },
};
</script>
<style lang="scss" scoped>
.gantt-container {
  height: 100%;
  .table {
    background: #fff;
    padding: 0 10px;
    height: calc(100% - 70px - 70px);
    .gantt {
      overflow: hidden;
      position: relative;
      height: 100%;
    }
  }
  .search {
    background: #fff;
    height: 70px;
    display: flex;
    align-items: center;
    position: sticky;
    top: 0px;
    z-index: 2;
  }
}

.pagination-container {
  height: 50px;
}
.search ::v-deep .el-form-item {
  margin-bottom: 0;
}
.search ::v-deep .el-form {
  flex: 1;
}
</style>
