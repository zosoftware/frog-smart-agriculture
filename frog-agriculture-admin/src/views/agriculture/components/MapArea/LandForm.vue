<template>
  <div class="PolygonForm">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
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
        <el-color-picker v-model="form.strokeColor"></el-color-picker>
      </el-form-item>
      <el-form-item label="边框透明度" prop="strokeOpacity">
        <el-slider v-model="form.strokeOpacity" :step="0.1" :max="1"></el-slider>
      </el-form-item>
      <el-form-item label="地块颜色" prop="fillColor">
        <el-color-picker v-model="form.fillColor"></el-color-picker>
      </el-form-item>
      <el-form-item label="地块透明度" prop="drawBoder">
        <el-slider v-model="form.fillOpacity" :step="0.1" :max="1"></el-slider>
      </el-form-item>
      <!-- <el-form-item>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </el-form-item> -->
    </el-form>
    <div class="footer">

      <el-button type="primary" @click="submitForm" :disabled="!checkPermi(['agriculture:land:edit'])">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </div>
</template>

<script>
import {listLand,getLand,delLand,addLand,updateLand,} from "@/api/agriculture/land";
import { checkPermi} from "@/utils/permission"; // 权限判断函数

export default {
    name:"LandForm",
    dicts: ["agriculture_land_status", "agriculture_land_type"],
    props:{
        id:{
            default:null
        },
        areaPath:{
            default:null
        },
        finishEdit:Function,
        path:{
          default:null,
          type:String
        }
    },
    data() {
        return {
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                landName:[{ required: true, message: '请输入地块名称', trigger: 'blur' }],
                landType:[{ required: true, message: '请输入地块', trigger: 'blur' }],
                landStatus:[{ required: true, message: '请选择地块状态', trigger: 'blur' }],
                landArea:[{ required: true, message: '请输入地块面积', trigger: 'blur' }],
                cropName:[{ required: true, message: '请输入地块作物', trigger: 'blur' }]
            }
        }
    },
    watch:{
        id:{
            handler(value){
              if (value!=null) {
                this.handleUpdate(value)
              }
            },
            immediate:true
        },
        path:{
          handler(value){
            if (value!=null) {
              this.form.landPath=value
            }
          },
          immediate:true
        },
        form:{
          handler(value){
            if (this.id) {
              this.$emit("upDataStyle",value)
            }
          },
          deep:true
        }
    },
    methods:{
        checkPermi,
        // 表单重置
        reset() {
            this.form = {
                landId: null,
                landName: null,
                landType: null,
                landStatus: null,
                landArea: null,
                cropName: null,
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
        /** 修改按钮操作 */
        handleUpdate(id) {
            this.reset();
            this.form.landId=id
            getLand(id).then((response) => {
                this.form = response.data;
                this.$emit("upDataStyle",response.data)
            });
        },
        /** 提交按钮 */
        submitForm() {
          this.finishEdit()
          setTimeout(()=>{
            this.$refs["form"].validate((valid) => {
              if (valid) {
                if (this.form.landId != null) {
                  updateLand(this.form).then((response) => {
                    this.$modal.msgSuccess("修改成功");
                    this.$emit("Refresh")
                  });
                }
              }
            });
          })
        },
        cancel(){
          this.$emit("cancel")
        }
    }
};
</script>

<style scoped>
.PolygonForm{
  position: relative;
  height: calc(100% - 80px);
}
.footer{
  position: absolute;
  bottom: 0;
  right: 0;
}
</style>
