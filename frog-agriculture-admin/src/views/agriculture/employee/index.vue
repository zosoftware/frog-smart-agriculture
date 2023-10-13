<template>
  <div class="app-container-sm">
    <el-card class="card-margin-bottom">
      <el-form
        :model="queryParams"
        ref="queryForm"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
        class="form-minus-bottom"
      >
        <el-form-item label="编码" prop="employeeCode">
          <el-input
            v-model="queryParams.employeeCode"
            placeholder="请输入编码"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="姓名" prop="employeeName">
          <el-input
            v-model="queryParams.employeeName"
            placeholder="请输入姓名"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="手机号码" prop="employeeTel">
          <el-input
            v-model="queryParams.employeeTel"
            placeholder="请输入手机号码"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
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
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['agriculture:employee:add']"
            >新增</el-button
          >
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['agriculture:employee:export']"
            >导出</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="card-padding-bottom">
      <el-table v-loading="loading" :data="employeeList">
        <el-table-column label="编码" align="center" prop="employeeCode" />
        <el-table-column label="姓名" align="center" prop="employeeName" />
        <el-table-column label="员工类型" align="center" prop="employeeType">
          <template slot-scope="scope">
            <dict-tag
              :options="dict.type.agriculture_employee_type"
              :value="scope.row.employeeType"
            />
          </template>
        </el-table-column>
        <el-table-column label="手机号码" align="center" prop="employeeTel" />
        <el-table-column label="性别" align="center" prop="employeeSex">
          <template slot-scope="scope">
            <dict-tag
              :options="dict.type.sys_user_sex"
              :value="scope.row.employeeSex"
            />
          </template>
        </el-table-column>
        <el-table-column label="地址" align="center" prop="employeeAddress" />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="small"
              class="padding-5"
              type="primary"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['agriculture:employee:edit']"
              >修改</el-button
            >
            <el-button
              size="small"
              class="padding-5"
              type="danger"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['agriculture:employee:remove']"
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
    </el-card>
    <!-- 添加或修改雇员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编码" prop="employeeCode">
          <el-input v-model="form.employeeCode" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="姓名" prop="employeeName">
          <el-input v-model="form.employeeName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="员工类型" prop="employeeType">
          <el-select v-model="form.employeeType" placeholder="请选择员工类型">
            <el-option
              v-for="dict in dict.type.agriculture_employee_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号码" prop="employeeTel">
          <el-input v-model="form.employeeTel" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="性别" prop="employeeSex">
          <el-select v-model="form.employeeSex" placeholder="请选择性别">
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="地址" prop="employeeAddress">
          <el-input v-model="form.employeeAddress" placeholder="请输入地址" />
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
  listEmployee,
  getEmployee,
  delEmployee,
  addEmployee,
  updateEmployee,
} from "@/api/agriculture/employee";

export default {
  name: "Employee",
  dicts: ["agriculture_employee_type", "sys_user_sex"],
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
      // 雇员表格数据
      employeeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        employeeCode: null,
        employeeName: null,
        employeeTel: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        employeeCode: [
          { required: true, message: "编码不能为空", trigger: "blur" },
        ],
        employeeName: [
          { required: true, message: "姓名不能为空", trigger: "blur" },
        ],
        employeeType: [
          { required: true, message: "员工类型不能为空", trigger: "change" },
        ],
        employeeTel: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
        ],
        employeeSex: [
          { required: true, message: "性别不能为空", trigger: "change" },
        ],
        employeeAddress: [
          { required: true, message: "地址不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询雇员列表 */
    getList() {
      this.loading = true;
      listEmployee(this.queryParams).then((response) => {
        this.employeeList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        employeeId: null,
        employeeCode: null,
        employeeName: null,
        employeeType: null,
        employeeTel: null,
        employeeSex: null,
        employeeAddress: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加雇员";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const employeeId = row.employeeId || this.ids;
      getEmployee(employeeId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改雇员";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.employeeId != null) {
            updateEmployee(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEmployee(this.form).then((response) => {
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
      const employeeIds = row.employeeId || this.ids;
      this.$modal
        .confirm('是否确认删除雇员编号为"' + employeeIds + '"的数据项？')
        .then(function () {
          return delEmployee(employeeIds);
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
        "agriculture/employee/export",
        {
          ...this.queryParams,
        },
        `employee_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
