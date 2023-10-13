<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['agriculture:upgrade:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['agriculture:upgrade:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['agriculture:upgrade:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['agriculture:upgrade:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="upgradeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="记录ID" align="center" prop="recordId" /> -->
      <el-table-column label="升级类型" align="center" prop="updateType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_update_type" :value="scope.row.updateType"/>
        </template>
      </el-table-column>
      <el-table-column label="是否强制升级" align="center" prop="isForceUpdate">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isForceUpdate"/>
        </template>
      </el-table-column>
      <el-table-column label="apk升级地址" align="center" prop="androidUrl">
        <template v-slot:default="{ row }">
          <div class="qrcode-wrap" @click="previewImage(`${ host + baseUrl + row.androidUrl }`,row)">
            <qrcode-vue :size="30" :value="`${ host + baseUrl + row.androidUrl }`"></qrcode-vue>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="ios升级地址" align="center" prop="iosUrl">
        <template v-slot:default="{ row }">
          <div class="qrcode-wrap" @click="previewImage(`${ host + baseUrl + row.iosUrl }`,row)">
            <qrcode-vue :size="30" :value="row.iosUrl"></qrcode-vue>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="升级内容描述" align="center" prop="con" />
      <el-table-column label="版本号" align="center" prop="version" />
      <el-table-column label="wgt版本号" align="center" prop="wgtVersion" />
      <el-table-column label="版本名称" align="center" prop="versionName" />
      <el-table-column label="应用版本号" align="center" prop="versionCode" />
      <el-table-column label="测试用户" align="center" prop="testUser" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="启用" align="center" prop="isCurrent">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isCurrent"
            active-value="1"
            inactive-value="0"
            @change="handEnable(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="130">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['agriculture:upgrade:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agriculture:upgrade:remove']"
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

    <!-- 添加或修改App升级对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
        <el-alert
      type="warning"
      show-icon
      class="margin-bottom-10"
      description="每次升级的应用版本号和应用版本名称必须高于上一次（壳内wgt的版本号与应用版本名称是一致的），采用整包升级的方式壳子应用版本名称和壳内应用的版本号(wgt.version)是一致的，采用wgt升级或导致壳内wgt版本号高于壳子应用版本名称。"
    >
        </el-alert>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="升级类型" prop="updateType">
          <el-select v-model="form.updateType" placeholder="请选择升级类型" class="display-block">
            <el-option
              v-for="dict in dict.type.sys_update_type"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否强制升级" prop="isForceUpdate" >
          <el-select v-model="form.isForceUpdate" placeholder="请选择是否强制升级" class="display-block">
            <el-option
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="apk升级地址" prop="androidUrl">
          <file-upload :fileType="['apk']" :limit="1" :fileSize="70" v-model="form.androidUrl"/>
        </el-form-item>
        <el-form-item label="ios升级地址" prop="iosUrl">
           <el-input v-model="form.iosUrl" placeholder="输入appstore升级地址" />
        </el-form-item>
        <el-form-item label="升级内容描述" prop="con">
          <el-input v-model="form.con" placeholder="请输入升级内容描述" />
        </el-form-item>
        <el-form-item label="版本号" prop="version">
          <el-input v-model="form.version" placeholder="例如1.2.0，用于app对比下载安装包" />
        </el-form-item>
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="form.versionName" placeholder="应用版本名称,高于上一次，例如1.0.0,手机安装升级包需要" />
        </el-form-item>
        <el-form-item label="应用版本号" prop="versionCode">
          <el-input v-model="form.versionCode" placeholder="应用版本号,必须是整数，高于上一次，例如100,手机安装升级包需要" />
        </el-form-item>
        <el-form-item label="wgt版本号" prop="wgtVersion">
          <el-input v-model="form.wgtVersion" placeholder="wgt版本号,wgt升级需要" />
        </el-form-item>
        <el-form-item label="测试用户" prop="testUser">
          <el-input v-model="form.testUser" placeholder="请输入测试用户" />
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

    <!-- 二维码弹窗 -->
    <el-dialog :title="qrcode.title" :visible.sync="qrcode.open" width="240px">
      <qrcode-vue :value="qrcode.value" :size="200"></qrcode-vue>
    </el-dialog>
  </div>
</template>

<script>
import { listUpgrade, getUpgrade, delUpgrade, addUpgrade, updateUpgrade } from "@/api/agriculture/upgrade";
import QrcodeVue from 'qrcode.vue'

export default {
  name: "Upgrade",
  dicts: ['sys_update_type', 'sys_yes_no'],
  data() {
    return {
      //baseURL
      baseUrl:process.env.VUE_APP_BASE_API,
      // 遮罩层
      loading: true,
      host:window.location.origin,
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
      // App升级表格数据
      upgradeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        updateType: [
          { required: true, message: "升级类型不能为空", trigger: "change" }
        ],
        isForceUpdate: [
          { required: true, message: "是否强制升级不能为空", trigger: "change" }
        ],
        androidUrl: [
          { required: true, message: "apk升级地址不能为空", trigger: "blur" }
        ],
        iosUrl: [
          { required: true, message: "ios升级地址不能为空", trigger: "blur" }
        ],
        con: [
          { required: true, message: "升级内容描述不能为空", trigger: "blur" }
        ],
        version: [
          { required: true, message: "版本号不能为空", trigger: "blur" }
        ],
        wgtVersion: [
          { required: true, message: "wgt版本号不能为空", trigger: "blur" }
        ],
        versionName: [
          { required: true, message: "版本名称不能为空", trigger: "blur" }
        ],
        versionCode: [
          { required: true, message: "应用版本号不能为空", trigger: "blur" }
        ],
        testUser: [
          { required: true, message: "测试用户不能为空", trigger: "blur" }
        ],
      },
      //二维码弹窗
      qrcode:{
        title:null,
        open:false,
        value:null
      }
    };
  },
  components: {
    QrcodeVue
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询App升级列表 */
    getList() {
      this.loading = true;
      listUpgrade(this.queryParams).then(response => {
        this.upgradeList = response.rows;
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
        recordId: null,
        updateType: null,
        isForceUpdate: null,
        androidUrl: null,
        iosUrl: null,
        con: null,
        version: null,
        wgtVersion: null,
        versionName: null,
        versionCode: null,
        testUser: null,
        isCurrent: null,
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加App升级";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids
      getUpgrade(recordId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改App升级";
      });
    },
    /* 用户状态修改 */
    async handEnable(row) {
      let text = row.isCurrent === "0" ? "停用" : "启用";
      await this.$modal.confirm(`确认${text}版本${row.version}吗？`)
      await updateUpgrade({recordId:row.recordId,isCurrent:row.isCurrent});
      await this.$modal.msgSuccess('设置成功');
      this.getList();
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateUpgrade(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUpgrade(this.form).then(response => {
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
      const recordIds = row.recordId || this.ids;
      this.$modal.confirm('是否确认删除App升级编号为"' + recordIds + '"的数据项？').then(function() {
        return delUpgrade(recordIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/upgrade/export', {
        ...this.queryParams
      }, `upgrade_${new Date().getTime()}.xlsx`)
    },

    /** 二维码弹窗 */
    previewImage(value,row){
      this.qrcode.title = row.version
      this.qrcode.open = true;
      this.qrcode.value = value;
    }
  }
};
</script>
