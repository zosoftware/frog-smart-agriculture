<template>
  <div class="app-container-sm">
    <el-card v-show="showSearch"  class="card-margin-bottom">
      <el-form
        :model="queryParams"
        ref="queryForm"
        size="small"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
        class="form-minus-bottom"
      >
        <el-form-item label="地块名称" prop="landName">
          <el-input
            v-model="queryParams.landName"
            placeholder="请输入地块名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="地块类别" prop="landType">
          <el-select
            v-model="queryParams.landType"
            placeholder="请选择地块类别"
            clearable
          >
            <el-option
              v-for="dict in dict.type.agriculture_land_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="地块状态" prop="cropStatus">
          <el-select
            v-model="queryParams.cropStatus"
            placeholder="请选择地块状态"
            clearable
          >
            <el-option
              v-for="dict in dict.type.agriculture_land_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="种植作物" prop="cropName">
          <el-input
            v-model="queryParams.cropName"
            placeholder="请输入种植作物"
            clearable
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
         <el-form-item style="float:right;">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['agriculture:land:add']"
              >新增</el-button
            >
         </el-form-item>
      </el-form>
    </el-card>

  <el-card class="card-padding-bottom">
    <el-table
      v-loading="loading"
      :data="landList"
    >
      <el-table-column label="地块名称" align="center" prop="landName" />
      <el-table-column label="地块类别" align="center" prop="landType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.agriculture_land_type"
            :value="scope.row.landType"
          />
        </template>
      </el-table-column>
      <el-table-column label="地块状态" align="center" prop="cropStatus">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.agriculture_land_status"
            :value="scope.row.cropStatus"
          />
        </template>
      </el-table-column>
      <el-table-column label="种植作物" align="center" prop="cropName" />
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
            v-hasPermi="['agriculture:land:edit']"
            >修改</el-button
          >
          <el-button
            size="small"
            class="padding-5"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agriculture:land:remove']"
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
    <!-- 添加或修改地块对话框 -->
    <el-dialog v-if="open" :title="title" :visible.sync="open" width="1200px" append-to-body>
      <div class="map-form">
        <draw-area :polygon-style="polygonStyle" :polygon-path="this.form.landPath" @change-path="getPloygon"/>
        <div style="width:430px">
          <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="地块名称" prop="landName">
            <el-input v-model="form.landName" placeholder="请输入地块名称" />
          </el-form-item>
          <el-form-item label="地块类别" prop="landType">
            <el-select v-model="form.landType" placeholder="请选择地块类别">
              <el-option
                v-for="dict in dict.type.agriculture_land_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="地块面积" prop="landArea">
            <el-input v-model="form.landArea" placeholder="请输入地块面积">
              <template slot="append">亩</template>
            </el-input>
          </el-form-item>
          <el-form-item label="边框宽度" prop="strokeWeight">
            <el-slider v-model="form.strokeWeight" :step="1" :max="5"></el-slider>
          </el-form-item>
          <el-form-item label="边框颜色" prop="strokeColor">
            <el-color-picker
              v-model="form.strokeColor"
            ></el-color-picker>
          </el-form-item>
          <el-form-item label="边框透明度" prop="strokeOpacity">
            <el-slider v-model="form.strokeOpacity" :step="0.1" :max="1" ></el-slider>
          </el-form-item>
          <el-form-item label="地块颜色" prop="fillColor">
            <el-color-picker
              v-model="form.fillColor"
            ></el-color-picker>
          </el-form-item>
          <el-form-item label="地块透明度" prop="drawBoder">
            <el-slider v-model="form.fillOpacity" :step="0.1" :max="1" ></el-slider>
          </el-form-item>
        </el-form>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </el-card>
  </div>
</template>

<script>
import {
  listLand,
  getLand,
  delLand,
  addLand,
  updateLand,
} from "@/api/agriculture/land";
import { getToken } from "@/utils/auth";
import DrawArea from "./components/DrawArea";
export default {
  name: "Land",
  dicts: ["agriculture_land_status", "agriculture_land_type"],
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
      // 地块表格数据
      landList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //导入参数
      upload: {
        // 是否显示弹出层（地块导入）
        open: false,
        // 弹出层标题（地块导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的地块数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/agriculture/land/importData",
      },
      map: {
        //是否显示弹出层（地图绘制）
        open: false,
        //弹出层标题（地图绘制）
        title: "",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        landName: null,
        landType: null,
        cropStatus: null,
        cropName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        landName:[{ required: true, message: '请输入地块名称', trigger: 'blur' }],
        landType:[{ required: true, message: '请输入地块', trigger: 'blur' }],
        landArea:[{ required: true, message: '请输入地块面积', trigger: 'blur' }],
      }
    };
  },
  computed:{
    polygonStyle:function(){
      return{
          fillColor:this.form.fillColor,
          fillOpacity:this.form.fillOpacity,
          strokeColor:this.form.strokeColor,
          strokeOpacity:this.form.strokeOpacity,
          strokeWeight:this.form.strokeWeight
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {

    /** 查询地块列表 */
    getList() {
      this.loading = true;
      listLand(this.queryParams).then((response) => {
        this.landList = response.rows;
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
        landId: null,
        landName: null,
        landType: null,
        landArea: null,
        strokeWeight: 1,
        strokeColor: '#1AC233',
        strokeOpacity:0.8,
        landPath: null,
        fillColor: '#094015',
        fillOpacity:0.8,
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
      this.title = "添加地块";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const landId = row.landId || this.ids;
      getLand(landId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改地块";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.landId != null) {
            updateLand(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLand(this.form).then((response) => {
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
      const landIds = row.landId || this.ids;
      this.$modal
        .confirm('是否确认删除地块编号为"' + landIds + '"的数据项？')
        .then(function () {
          return delLand(landIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 获取多边形数据 */
    getPloygon(p) {
      this.form.landPath = p.join('|');
    }
  },
  components: { DrawArea },
};
</script>
<style lang="scss" scoped>
  .map-form{
    display: flex;

  }
</style>
