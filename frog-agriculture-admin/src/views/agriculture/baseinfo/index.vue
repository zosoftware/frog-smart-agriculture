<template>
    <el-card class="card-container">
       <div slot="header" class="clearfix">
        <span>基地信息</span>
       </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="form">
        <el-form-item label="基地简称" prop="baseShortName">
          <el-input v-model="form.baseShortName" placeholder="请输入基地简称" />
        </el-form-item>
        <el-form-item label="基地编号" prop="baseCode">
          <el-input v-model="form.baseCode" placeholder="请输入基地编号" />
        </el-form-item>
        <el-form-item label="基地全称" prop="baseName">
          <el-input v-model="form.baseName" placeholder="请输入基地全称" />
        </el-form-item>
        <el-form-item label="基地负责人" prop="baseLeader">
          <el-input v-model="form.baseLeader" placeholder="请输入基地负责人" />
        </el-form-item>
        <el-form-item label="基地负责人电话" prop="leaderTel">
          <el-input v-model="form.leaderTel" placeholder="请输入基地负责人电话" />
        </el-form-item>
        <el-form-item label="基地负责人地址" prop="baseAddress">
          <el-input v-model="form.baseAddress" placeholder="请输入基地负责人地址" />
        </el-form-item>
        <el-form-item label="基地面积" prop="baseArea">
          <el-input v-model="form.baseArea" placeholder="请输入基地面积">
             <template slot="append">亩</template>
          </el-input>
        </el-form-item>
        <el-form-item label="基地海拔" prop="baseAltitude">
          <el-input v-model="form.baseAltitude" placeholder="请输入基地海拔">
             <template slot="append">米</template>
          </el-input>
        </el-form-item>
        <el-form-item
            label="基地坐标"
            prop="coordinate"
            v-if="proportion != 24"
          >
            <point-select
              v-model="form.baseCoordinate"
            ></point-select>
          </el-form-item>
        <el-form-item label="现场图片">
          <imageUpload v-model="form.baseImg" :fileSize="30" :limit="1"/>
        </el-form-item>
        <el-form-item label="基地描述" prop="baseDes">
          <el-input v-model="form.baseDes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
</template>

<script>
import { getBaseinfoLimitOne, addBaseinfo, updateBaseinfo } from "@/api/agriculture/baseinfo";
import PointSelect from '@/views/iot/device/components/PointSelect';
export default {
  name: "Baseinfo",
  components:{PointSelect},
  data() {
    return {
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        baseShortName: [
          { required: true, message: "基地简称不能为空", trigger: "blur" }
        ],
        baseCode: [
          { required: true, message: "基地编号不能为空", trigger: "blur" }
        ],
        baseName: [
          { required: true, message: "基地全称不能为空", trigger: "blur" }
        ],
        baseLeader: [
          { required: true, message: "基地负责人不能为空", trigger: "blur" }
        ],
        leaderTel: [
          { required: true, message: "基地负责人电话不能为空", trigger: "blur" }
        ],
        baseAddress: [
          { required: true, message: "基地负责人地址不能为空", trigger: "blur" }
        ],
        baseArea: [
          { required: true, message: "基地面积不能为空", trigger: "blur" }
        ],
        baseAltitude: [
          { required: true, message: "基地海拔不能为空", trigger: "blur" }
        ],
        baseImg: [
          { required: true, message: "现场图片不能为空", trigger: "blur" }
        ],
        baseDes: [
          { required: true, message: "基地描述不能为空", trigger: "blur" }
        ],
        baseCoordinate: [
          { required: true, message: "基地坐标不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getInfo();
  },
  methods: {
    // 表单重置
    reset() {
      this.form = {
        baseId: null,
        baseShortName: null,
        baseCode: null,
        baseName: null,
        baseLeader: null,
        leaderTel: null,
        baseAddress: null,
        baseArea: null,
        baseAltitude: null,
        baseImg: null,
        baseDes: null,
        baseCoordinate:''
      };
      this.resetForm("form");
    },
    /** 获取基地信息 */
    getInfo() {
      this.reset();
      getBaseinfoLimitOne().then(response => {
        const { data } = response;
        if(data){
          this.form = data;
        }
      });
    },
    /** 取消按钮操作 */
    cancel(){
      this.$store.dispatch('tagsView/delView', this.$route);
      this.$router.go(-1);
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.baseId != null) {
            updateBaseinfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBaseinfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.card-container{
  margin:6px;
  min-height: calc(100vh - 84px - 12px);
}
.form{
  width: 600px;
  margin-left: 100px;
}
</style>
