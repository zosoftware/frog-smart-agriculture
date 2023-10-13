<template>
  <div class="app-container-sm">
    <el-card class="card-margin-bottom">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px"
        class="form-minus-bottom">
        <el-form-item label="农机名称" prop="machineName">
          <el-input v-model="queryParams.machineName" placeholder="请输入农机名称" clearable size="small"
            @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="农机类别" prop="machineTypeId">
          <el-select v-model="queryParams.machineTypeId" placeholder="请选择农机类别" clearable @change="handleQuery">
            <el-option v-for="item in machineTypeList" :key="item.machineTypeId" :label="item.machineTypeName"
              :value="item.machineTypeId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
        <el-form-item class="fr">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
            v-hasPermi="['agriculture:machineInfo:add']">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="card-padding-bottom">
      <el-table v-loading="loading" :data="machineInfoList">
        <el-table-column label="农机编码" align="center" prop="machineCode" />
        <el-table-column label="农机名称" align="center" prop="machineName" />
        <el-table-column label="农机类别" align="center" prop="machineTypeId" />
        <el-table-column label="计量单位" align="center" prop="measureUnit" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  size="small"
            class="padding-5"
            type="primary"
             icon="el-icon-edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['agriculture:machineInfo:edit']">修改</el-button>
            <el-button  size="small"
            class="padding-5"
            type="danger"
             icon="el-icon-delete" @click="handleDelete(scope.row)"
              v-hasPermi="['agriculture:machineInfo:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
        @pagination="getList" />
    </el-card>
    <!-- 添加或修改农机信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="农机编码" prop="machineCode">
          <el-input v-model="form.machineCode" placeholder="请输入农机编码" />
        </el-form-item>
        <el-form-item label="农机名称" prop="machineName">
          <el-input v-model="form.machineName" placeholder="请输入农机名称" />
        </el-form-item>
        <el-form-item label="农机类别" prop="machineTypeId" >
          <el-select  class="display-block" v-model="form.machineTypeId" placeholder="请选择农机类别" clearable @change="handleQuery">
            <el-option v-for="item in machineTypeList" :key="item.machineTypeId" :label="item.machineTypeName"
              :value="item.machineTypeId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="计量单位" prop="measureUnit">
          <el-input v-model="form.measureUnit" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listMachineInfo, getMachineInfo, delMachineInfo, addMachineInfo, updateMachineInfo } from "@/api/agriculture/machineInfo";
import { listMachineType } from "@/api/agriculture/machineType";

export default {
  name: "MachineInfo",
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
      // 农机信息表格数据
      machineInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 农机类别数据源
      machineTypeList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        machineCode: null,
        machineName: null,
        machineTypeId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        machineCode: [
          { required: true, message: "农机编码不能为空", trigger: "blur" }
        ],
        machineName: [
          { required: true, message: "农机名称不能为空", trigger: "blur" }
        ],
        machineTypeId: [
          { required: true, message: "农机类别不能为空", trigger: "blur" }
        ],
        measureUnit: [
          { required: true, message: "计量单位不能为空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "排序不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "删除标志不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getMachineTypeList();
  },
  methods: {
    /** 查询农机信息列表 */
    getList() {
      this.loading = true;
      listMachineInfo(this.queryParams).then(response => {
        this.machineInfoList = response.rows;
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
        machineId: null,
        machineCode: null,
        machineName: null,
        machineTypeId: null,
        measureUnit: null,
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
    /** 获取农机类别数据源信息 */
     getMachineTypeList() {
      listMachineType().then(response=>{
        this.machineTypeList = response.rows;
      });
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
      this.title = "添加农机信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const machineId = row.machineId || this.ids
      getMachineInfo(machineId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改农机信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.machineId != null) {
            updateMachineInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMachineInfo(this.form).then(response => {
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
      const machineIds = row.machineId || this.ids;
      this.$modal.confirm('是否确认删除农机信息编号为"' + machineIds + '"的数据项？').then(function () {
        return delMachineInfo(machineIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('agriculture/machineInfo/export', {
        ...this.queryParams
      }, `machineInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
