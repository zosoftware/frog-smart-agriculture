<template>
  <div class="app-container-sm">
      <el-card class="card-margin-bottom">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" class="form-minus-bottom">
      <el-form-item label="名称" prop="methodName">
        <el-input
          v-model="queryParams.methodName"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
        <el-form-item class="fr">
            <el-button
                    type="primary"
                    plain
                    icon="el-icon-plus"
                    size="mini"
                    @click="handleAdd"
                    v-hasPermi="['agriculture:method:add']"
            >新增</el-button>
            <el-button
                    type="warning"
                    plain
                    icon="el-icon-download"
                    size="mini"
                    @click="handleExport"
                    v-hasPermi="['agriculture:method:export']"
            >导出</el-button>
        </el-form-item>
    </el-form>
      </el-card>
<el-card>
    <el-table v-loading="loading" :data="methodList" >
      <el-table-column label="名称" align="center" prop="methodName" />
      <el-table-column label="图片" align="center" prop="methodImg" width="180">
          <template v-slot:default="{ row }">
              <div class="image" @click="previewImage(`${ image.baseUrl  + row.methodImg }`,row)">
                  <img style="width:50px;height:50px;" :src="`${ image.baseUrl  + row.methodImg }`" />
              </div>
          </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="methodDes" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['agriculture:method:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agriculture:method:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
</el-card>
    <!-- 添加或修改种植方法对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="methodName">
          <el-input v-model="form.methodName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="图片" prop="methodImg">
          <imageUpload v-model="form.methodImg" :limit="1"/>
        </el-form-item>
        <el-form-item label="描述" prop="methodDes">
          <el-input v-model="form.methodDes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
        <!-- 图片预览对话框 -->
        <el-dialog :title="image.title" :visible.sync="image.open" width="240px">
            <img style="width:200px;height:200px;" :src="image.imgUrl" />
        </el-dialog>
  </div>
</template>

<script>
import { listMethod, getMethod, delMethod, addMethod, updateMethod } from "@/api/agriculture/method";

export default {
  name: "Method",
  props:{
    germplasmId:{
      type:Number,
      default:0
    }
  },
  data() {
    return {
      //图片预览
      image:{
          baseUrl:window.location.origin+process.env.VUE_APP_BASE_API,
          open:false,
          imgUrl:'',
          title:''
      },
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
      // 种植方法表格数据
      methodList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        methodName: null,
        germplasmId:this.germplasmId
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        methodName: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        methodImg: [
          { required: true, message: "图片不能为空", trigger: "blur" }
        ],
        methodDes: [
          { required: true, message: "描述不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询种植方法列表 */
    getList() {
      this.loading = true;
      listMethod(this.queryParams).then(response => {
        this.methodList = response.rows;
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
        methodId: null,
        germplasmId: null,
        methodName: null,
        methodImg: null,
        methodDes: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加种植方法";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const methodId = row.methodId || this.ids
      getMethod(methodId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改种植方法";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.methodId != null) {
            updateMethod(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.germplasmId = this.germplasmId;
            addMethod(this.form).then(response => {
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
      const methodIds = row.methodId || this.ids;
      this.$modal.confirm('是否确认删除种植方法编号为"' + methodIds + '"的数据项？').then(function() {
        return delMethod(methodIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 图片预览按钮操作 */
    previewImage(imgUrl,row){
        this.image.title = row.version
        this.image.open = true;
        this.image.imgUrl = imgUrl;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('agriculture/method/export', {
        ...this.queryParams
      }, `method_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>