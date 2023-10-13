<template>
  <div class="app-container-sm">
      <el-card class="card-margin-bottom">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" class="form-minus-bottom">
      <el-form-item label="作物名称" prop="cropName">
        <el-input
          v-model="queryParams.cropName"
          placeholder="请输入作物名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="种质名称" prop="germplasmName">
        <el-input
          v-model="queryParams.germplasmName"
          placeholder="请输入种质名称"
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
                    v-hasPermi="['agriculture:germplasm:add']"
            >新增</el-button>
            <el-button
                    type="warning"
                    plain
                    icon="el-icon-download"
                    size="mini"
                    @click="handleExport"
                    v-hasPermi="['agriculture:germplasm:export']"
            >导出</el-button>
        </el-form-item>
    </el-form>
      </el-card>
<el-card class="card-padding-bottom">
    <el-table v-loading="loading" :data="germplasmList" >
      <el-table-column label="作物名称" align="center" prop="cropName" />
      <el-table-column label="作物英文名称" align="center" prop="cropEnName" />
      <el-table-column label="种质名称" align="center" prop="germplasmName" />
      <el-table-column label="种质英文名称" align="center" prop="germplasmEnName" />
      <el-table-column label="种质图片" align="center" prop="germplasmImg" width="180">
          <template v-slot:default="{ row }">
              <div class="image" @click="previewImage(`${ image.baseUrl  + row.germplasmImg }`,row)">
                  <img style="width:50px;height:50px;" :src="`${ image.baseUrl  + row.germplasmImg }`" />
              </div>
          </template>
      </el-table-column>
      <el-table-column label="宣传语" align="center" prop="germplasmDes" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="small"
            type="primary"
            class="padding-5"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['agriculture:germplasm:edit']"
          >修改</el-button>
          <el-button
            size="small"
            type="danger"
            class="padding-5"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agriculture:germplasm:remove']"
          >删除</el-button>
          <el-dropdown class="margin-left-10"  @command="handleCommand($event, scope.row)" v-hasPermi="['agriculture:intro:list', 'agriculture:method:list','agriculture:standardJob:list']">
                <el-button class="padding-5" size="small" type="warning">
                    更多菜单<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleMethod" icon="el-icon-key"
                    v-hasPermi="['agriculture:intro:list']">种植方法</el-dropdown-item>
                  <el-dropdown-item command="handleIntro" icon="el-icon-circle-check"
                    v-hasPermi="['agriculture:method:list']">种质介绍</el-dropdown-item>
                    <el-dropdown-item command="handleStandardJob" icon="el-icon-circle-check"
                    v-hasPermi="['agriculture:standardJob:list']">作业流程</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
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
    <!-- 添加或修改种质对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="作物名称" prop="cropName">
          <el-input v-model="form.cropName" placeholder="请输入作物名称" />
        </el-form-item>
        <el-form-item label="作物英文名称" prop="cropEnName">
          <el-input v-model="form.cropEnName" placeholder="请输入作物英文名称" />
        </el-form-item>
        <el-form-item label="种质名称" prop="germplasmName">
          <el-input v-model="form.germplasmName" placeholder="请输入种质名称" />
        </el-form-item>
        <el-form-item label="种质英文名称" prop="germplasmEnName">
          <el-input v-model="form.germplasmEnName" placeholder="请输入种质英文名称" />
        </el-form-item>
        <el-form-item label="种质图片" prop="germplasmImg">
          <imageUpload v-model="form.germplasmImg" :limit="1"/>
        </el-form-item>
        <el-form-item label="宣传语" prop="germplasmDes">
          <el-input v-model="form.germplasmDes" type="textarea" placeholder="请输入内容" />
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
    <!-- 种质介绍和种植方法管理对话框 -->
    <el-dialog v-if="mDialog.open" :title="mDialog.title" :visible.sync="mDialog.open" width="1000px" append-to-body>
      <method v-if="mDialog.type=='method'" :germplasm-id="mDialog.germplasmId"></method>
      <intro v-if="mDialog.type=='intro'" :germplasm-id="mDialog.germplasmId"></intro>
      <standard-job v-if="mDialog.type=='standard-job'" :germplasm-id="mDialog.germplasmId"></standard-job>
    </el-dialog>
  </div>
</template>

<script>
import { listGermplasm, getGermplasm, delGermplasm, addGermplasm, updateGermplasm } from "@/api/agriculture/germplasm";
import Method from "./components/Method";
import Intro from "./components/Intro";
import StandardJob from "./components/StandardJob";

export default {
  name: "Germplasm",
  data() {
    return {
      //图片预览
      image:{
          baseUrl:window.location.origin+process.env.VUE_APP_BASE_API,
          open:false,
          imgUrl:'',
          title:''
      },
      //种质介绍和种植方法管理对话框
      mDialog:{
        open:false,
        title:'',
        germplasmId:'',
        type:''
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
      // 种质表格数据
      germplasmList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cropName: null,
        germplasmName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cropName: [
          { required: true, message: "作物名称不能为空", trigger: "blur" }
        ],
        cropEnName: [
          { required: true, message: "作物英文名称不能为空", trigger: "blur" }
        ],
        germplasmName: [
          { required: true, message: "种质名称不能为空", trigger: "blur" }
        ],
        germplasmEnName: [
          { required: true, message: "种质英文名称不能为空", trigger: "blur" }
        ],
        germplasmImg: [
          { required: true, message: "种质图片不能为空", trigger: "blur" }
        ],
        germplasmDes: [
          { required: true, message: "宣传语不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询种质列表 */
    getList() {
      this.loading = true;
      listGermplasm(this.queryParams).then(response => {
        this.germplasmList = response.rows;
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
        germplasmId: null,
        cropName: null,
        cropEnName: null,
        germplasmName: null,
        germplasmEnName: null,
        germplasmImg: null,
        germplasmDes: null,
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
      this.title = "添加种质";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const germplasmId = row.germplasmId || this.ids
      getGermplasm(germplasmId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改种质";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.germplasmId != null) {
            updateGermplasm(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGermplasm(this.form).then(response => {
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
      const germplasmIds = row.germplasmId || this.ids;
      this.$modal.confirm('是否确认删除种质编号为"' + germplasmIds + '"的数据项？').then(function() {
        return delGermplasm(germplasmIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 更多按钮处理 */
    handleCommand(command,row){
      switch(command){
        case 'handleMethod':
        this.handleMethod(row);
        break;
        case 'handleIntro':
        this.handleIntro(row);
        break;
        case 'handleStandardJob':
        this.handleStandardJob(row);
        break;
        default:
          break;
      }

    },
    /** 方法按钮操作 */
    handleMethod(row){
      this.mDialog.germplasmId = row.germplasmId;
      this.mDialog.open=true;
      this.mDialog.title='种植方法';
      this.mDialog.type = 'method';
    },
    /** 介绍按钮操作 */
    handleIntro(row){
      this.mDialog.germplasmId = row.germplasmId;
      this.mDialog.open=true;
      this.mDialog.title='种质介绍';
      this.mDialog.type = 'intro';
    },
    /**  */
    handleStandardJob(row){
      this.mDialog.germplasmId = row.germplasmId;
      this.mDialog.open=true;
      this.mDialog.title='作业流程';
      this.mDialog.type = 'standard-job';
    },
    /** 图片预览按钮操作 */
    previewImage(imgUrl,row){
        this.image.title = row.version
        this.image.open = true;
        this.image.imgUrl = imgUrl;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('agriculture/germplasm/export', {
        ...this.queryParams
      }, `germplasm_${new Date().getTime()}.xlsx`)
    }
  },
  components:{ Method,Intro,StandardJob }
};
</script>
