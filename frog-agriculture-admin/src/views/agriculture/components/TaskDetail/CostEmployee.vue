<template>
  <div class="padding-bottom-10">
    <el-alert
      title="登记人工工时"
      type="info"
      show-icon
      description="此处可以按照每个用工登记工时"
    >
    </el-alert>
    <el-table
      v-loading="loading"
      :data="costEmployeeList"
      class="margin-top-10"
    >
      <el-table-column label="雇员ID" align="center" prop="employeeId">
        <template v-slot:default="scope">
            <data-tag
              :options="taskEmployeeList"
              :value="scope.row.employeeId"
              labelName="employeeName"
              valueName="employeeId"
              type="notag"
            />
          </template>
      </el-table-column>
      <el-table-column label="工时" align="center" prop="workingHours" />
      <el-table-column
        label="开始日期"
        align="center"
        prop="workingStart"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workingStart, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="结束日期"
        align="center"
        prop="workingFinish"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workingFinish, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >

      <template #header>
         <el-tag
            @click="handleAdd"
            v-hasPermi="['agriculture:costEmployee:add']"
            class="cursor-pointer"
            >新增</el-tag
          >
      </template>
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['agriculture:costEmployee:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agriculture:costEmployee:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改人工工时对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="雇员ID" prop="employeeId">
          <el-select class="display-block" v-model="form.employeeId"  placeholder="请选择雇员" clearable filterable >
              <el-option v-for="item in taskEmployeeList"
                  :key="item.employeeId"
                  :label="item.employeeName"
                  :value="item.employeeId">
              </el-option>
          </el-select>

        </el-form-item>
        <el-form-item label="工时" prop="workingHours">
          <el-input v-model="form.workingHours" placeholder="请输入工时"  >
              <template v-slot:append>
                  天
              </template>
          </el-input>
        </el-form-item>
        <el-form-item label="开始日期" prop="workingStart">
          <el-date-picker
            class="w100"
            clearable
            v-model="form.workingStart"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择开始日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="workingFinish">
          <el-date-picker
          class="w100"
            clearable
            v-model="form.workingFinish"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择结束日期"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCostEmployee,
  getCostEmployee,
  delCostEmployee,
  addCostEmployee,
  updateCostEmployee,
} from "@/api/agriculture/costEmployee";
import { addLog } from "@/api/agriculture/log";
import { listTaskEmployee } from "@/api/agriculture/taskEmployee";
export default {
  name: "CostEmployee",
  props:{
      taskId:{
          type:[Number,String]
      }
  },
  data() {
    return {
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
      // 人工工时表格数据
      costEmployeeList: [],

      taskEmployeeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskId:this.taskId
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        employeeId: [
          { required: true, message: "雇员ID不能为空", trigger: "blur" },
        ],
        workingHours: [
          { required: true, message: "工时不能为空", trigger: "blur" },
        ],
        workingStart: [
          { required: true, message: "开始日期不能为空", trigger: "blur" },
        ],
        workingFinish: [
          { required: true, message: "结束日期不能为空", trigger: "blur" },
        ],
        delFlag: [
          { required: true, message: "删除标志不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getTaskEmployeeList();
  },
  methods: {
    /** 查询人工工时列表 */
    getList() {
      this.loading = true;
      listCostEmployee(this.queryParams).then((response) => {
        this.costEmployeeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询雇员 */
    getTaskEmployeeList() {
      listTaskEmployee({taskId:this.taskId}).then((response) => {
        this.taskEmployeeList = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        costId: null,
        taskId: this.taskId,
        employeeId: null,
        workingHours: null,
        workingStart: null,
        workingFinish: null,
        remark: null,
        status: "0",
        orderNum: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null,
      };
      this.resetForm("form");
    },
      /** 插入任务日志 */
    async addTaskLog(des){
        await addLog({ taskId: this.taskId,operDes:des });
        this.$emit('log')
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加人工工时";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const costId = row.costId || this.ids;
      getCostEmployee(costId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改人工工时";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.costId != null) {
            updateCostEmployee(this.form).then((response) => {
              this.addTaskLog("修改人工工时")
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCostEmployee(this.form).then((response) => {
                this.addTaskLog("新增人工工时")
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const costIds = row.costId || this.ids;
      this.$modal
        .confirm('是否确认删除人工工时编号为"' + costIds + '"的数据项？')
        .then(function () {
          return delCostEmployee(costIds);
        })
        .then(() => {
          this.getList();
          this.addTaskLog("删除人工工时")
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "agriculture/costEmployee/export",
        {
          ...this.queryParams,
        },
        `costEmployee_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
