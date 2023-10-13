<template>
  <div>
    <!-- <el-breadcrumb separator="/" class="font-size-16">
        <el-breadcrumb-item>
          <i class="el-icon-menu font-color-primary"></i> 地块:
          {{ form.landName }}
        </el-breadcrumb-item>
        <el-breadcrumb-item
          ><i class="el-icon-s-order font-color-primary"></i> 批次:
          {{ form.batchName }}</el-breadcrumb-item
        >
        <el-breadcrumb-item
          ><i class="el-icon-s-claim font-color-primary"></i> 任务:{{
            form.taskName
          }}</el-breadcrumb-item
        >
      </el-breadcrumb> -->
    <el-input
      :value="form.landName + ' / ' + form.batchName + ' / ' + form.taskName"
      placeholder=""
      size="small"
      clearable
    ></el-input>

    <el-form
      label-position="top"
      ref="form"
      :model="form"
      :rules="rules"
      label-width="0"
    >
      <el-row class="margin-top-20">
        <el-col :span="4" class="flex aic">
          <status-select v-model="form.status" @change="submitForm"> 当前状态 </status-select>
        </el-col>
        <el-col :span="4" class="flex aic">
          <el-form-item label="" size="normal" prop="taskHead" class="margin-bottom-0">
            <radio-select
            :options="employeeList"
            v-model="form.taskHead"
            key-name="employeeName"
            value-name="employeeId"
            @change="submitForm"
            >责任人</radio-select
          >
          </el-form-item>
        </el-col>
        <el-col :span="4" class="flex aic">
          <calendar-select v-model="form.planStart" disabled
          @change="submitForm"
            >计划开始时间</calendar-select
          >
        </el-col>
        <el-col :span="4" class="flex aic">
          <calendar-select v-model="form.planFinish" disabled
          @change="submitForm"
            >计划完成时间</calendar-select
          >
        </el-col>
        <el-col :span="4" class="flex aic">
          <calendar-select v-model="form.actualStart"
          @change="submitForm"
            >实际开始时间</calendar-select
          >
        </el-col>
        <el-col :span="4" class="flex aic">
          <calendar-select v-model="form.actualFinish"
          @change="submitForm"
            >实际结束时间</calendar-select
          >
        </el-col>
      </el-row>

      <el-tabs v-model="activeName" class="margin-top-20">
        <el-tab-pane name="taskInfo">
          <template #label>
            <i class="el-icon-document-checked"></i> 任务信息
          </template>
          <div>任务详情：</div>
          <template v-if="!remarkOpen">
            <div v-if="form.remark" class="margin-top-15">
              {{ form.remark }}
              <span
                class="font-color-primary margin-left-10 cursor-point"
                @click="remarkOpen = true"
                >编辑</span
              >
            </div>
            <div v-if="!form.remark" class="margin-top-15">
              --暂无描述--
              <span
                class="font-color-primary margin-left-10 cursor-point"
                @click="remarkOpen = true"
                >添加描述</span
              >
            </div>
          </template>
          <template v-if="remarkOpen">
              <el-input
                type="textarea"
                :rows="20"
                resize="none"
                v-model="form.remark"
                placeholder=""
                :maxlength="-1"
                :show-word-limit="false"
                :autosize="{ minRows: 2, maxRows: 4 }"
                class="margin-top-10"
              >
              </el-input>
              <el-button-group class="margin-top-5 float-right">
                <el-button type="primary"  size="mini" @click="submitForm();remarkOpen = false;" > 确定</el-button>
                <el-button type="primary" plain size="mini" @click="remarkOpen=false">取消</el-button>
              </el-button-group>
          </template>

          <div class="margin-top-30">人员信息：</div>
          <multiple-select
            v-model="selectEmployeeList"
            :options="employeeList"
            key-name="employeeName"
            value-name="employeeId"
            class="margin-top-15"
            @change="handleTaskEmployeeChange"
          ></multiple-select>

          <div class="flex aic margin-top-15 margin-bottom-15">
            <span
              class="fwb flex00a padding-right-5 font-weight-bold font-size-15"
              >操作信息</span
            >
            <div class="height-1 flex1" style="background:#DCDFE6"></div>
          </div>
          <div class="height-150 overflow-auto">
            <div class="line-height-30" v-for="(item, index) in logList" :key="index">
              {{ index + 1 }}、{{ item.createTime }} ，
              由<span class="font-color-warning font-weight-bold padding-lr-5">{{ item.operName }}</span><span>{{item.operDes}}</span>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane name="costEmployee">
          <template #label> <i class="el-icon-user"></i> 人工工时 </template>
          <cost-employee :task-id="taskId" @log="getLoglist"></cost-employee>
          </el-tab-pane>
        <el-tab-pane name="costMachine"
          ><template #label> <i class="el-icon-truck"></i> 机械工时 </template>
            <cost-machine :task-id="taskId" @log="getLoglist"></cost-machine>
          </el-tab-pane>
        <el-tab-pane name="costMaterial"
          ><template #label>
            <i class="el-icon-suitcase-1"></i> 农资用量
          </template>
            <cost-material :taskId="taskId" @log="getLoglist"></cost-material>
          </el-tab-pane>
        <el-tab-pane name="annex"
          ><template #label> <i class="el-icon-paperclip"></i> 附件 </template>
           <div class="font-weight-bold">图片:</div>
           <image-upload class="margin-top-10" @change="submitForm" v-model="form.taskImages" :is-rectangle="true" :is-show-tip="false"></image-upload>
          <div class="font-weight-bold margin-top-10">视频:</div>
          <video-upload class="margin-top-10" v-model="form.taskVideos" @change="submitForm"></video-upload>
          </el-tab-pane
        >
      </el-tabs>
    </el-form>
    <!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div> -->
  </div>
</template>
<script>
import {
  getBatchTask,
  addBatchTask,
  updateBatchTask,
} from "@/api/agriculture/batchTask";
import { listBatch } from "@/api/agriculture/batch";
import { listUser } from "@/api/system/user";
import { listLand } from "@/api/agriculture/land";
import { listLog,addLog } from "@/api/agriculture/log";
import { listData } from "@/api/system/dict/data";
import { listEmployee } from "@/api/agriculture/employee";
import { listTaskEmployee,addTaskEmployee,delTaskEmployeeByTaskIdAndEmployeeId} from "@/api/agriculture/taskEmployee";

import RadioSelect from "../RadioSelect";
import CalendarSelect from "../CalendarSelect";
import MultipleSelect from "../MultipleSelect";
import StatusSelect from "../StatusSelect";
import CostEmployee from "./CostEmployee.vue"
import CostMachine from './CostMachine.vue';
import CostMaterial from './CostMaterial.vue';
export default {
  name: "TaskDetail",
  components: { MultipleSelect, CalendarSelect, RadioSelect, StatusSelect,CostEmployee,CostMachine,CostMaterial},
  props: {
    taskId: {
      type: Number,
    },
    oprType: {
      type: String,
      default: "",
    },
    batchId: {
      type: Number,
    },
  },
  data() {
    return {
      activeName: "taskInfo",
      batchList: [],
      userList: [],
      landList: [],
      batchTaskStatusList: [],
      selectEmployeeList: [],
      employeeList: [],
      logList: [],
      remarkOpen: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        taskHead: [
          { required: true, message: "任务名称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  watch: {
    oprType: {
      handler: function (n, o) {
        if (n.includes("add")) {
          this.handleAdd();
        } else if (n.includes("update")) {
          this.handleUpdate();
        }
      },
      immediate: true,
    },
  },
  created() {
    this.getBatchList();
    this.getUserList();
    this.getLandList();
    this.getBatchTaskStatusList();
    this.getLoglist();
    this.getEmployeeList();
    this.getTaskEmployee();
  },
  methods: {
    /** 查询批次 */
    getBatchList() {
      listBatch().then((response) => {
        this.batchList = response.rows;
      });
    },
    /** 查询用户 */
    getUserList() {
      listUser().then((response) => {
        this.userList = response.rows;
      });
    },
    /** 查询雇员 */
    getEmployeeList() {
      listEmployee().then((response) => {
        this.employeeList = response.rows;
      });
    },
    /** 查询地块 */
    getLandList() {
      listLand().then((response) => {
        this.landList = response.rows;
      });
    },
    /** 查询taskEmployee */
    getTaskEmployee(){
      listTaskEmployee({taskId:this.taskId}).then(response=>{
        const { rows } = response;
        this.selectEmployeeList = rows.map(item=>item.employeeId);
      });
    },
    /** 查询任务日志 */
    getLoglist() {
      listLog({ taskId: this.taskId }).then((response) => {
        this.logList = response.rows;
      });
    },
    /** 查询字典agriculture_batch_task_status值  */
    getBatchTaskStatusList() {
      listData({ dictType: "agriculture_batch_task_status" }).then(
        (response) => {
          this.batchTaskStatusList = response.rows;
        }
      );
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
        delFlag: null,
        userName: null,
        batchHead: null,
      };
      this.resetForm("form");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加批次任务";
    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.reset();
      const taskId = this.taskId;
      getBatchTask(taskId).then((response) => {
        this.form = response.data;
      });
    },
    /** 插入任务日志 */
    async addTaskLog(des){
        await addLog({ taskId: this.taskId,operDes:des });
        this.getLoglist()
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.taskId != null) {
            updateBatchTask(this.form).then((response) => {
              this.addTaskLog('完善项目信息')
              this.$modal.msgSuccess("修改成功");
            });
          } else {
            addBatchTask(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
            });
          }
        }
      });
    },
    /** 处理任务雇员选择事件 */
    handleTaskEmployeeChange(e){
      const {type,value,values} = e;
      if(type === 'add'){
        addTaskEmployee({taskId:this.taskId,employeeId:value}).then(respose=>{
          this.addTaskLog("增加参与人员")
          this.$modal.msgSuccess("添加成功");
        });
      }else if(type === 'del'){
        delTaskEmployeeByTaskIdAndEmployeeId(this.taskId,value).then(respose=>{
            this.addTaskLog("删除参与人员")
          this.$modal.msgSuccess("删除成功");
        });
      }
    }
  },
};
</script>
<style lang="scss" scoped>
::v-deep  .el-tabs__nav-wrap::after {
    height: 1px;
  }
</style>
