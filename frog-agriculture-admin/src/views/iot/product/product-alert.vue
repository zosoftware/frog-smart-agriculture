<template>
<div style="padding-left:20px;">
    <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['iot:alert:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-refresh" size="mini" @click="getList">刷新</el-button>
        </el-col>
    </el-row>

    <el-table v-loading="loading" :data="alertList" @selection-change="handleSelectionChange" border size="mini">
        <el-table-column label="告警名称" align="center" prop="alertName" />
        <el-table-column label="状态" align="center" prop="status" width="70">
            <template slot-scope="scope">
                <el-tag type="success" size="small" v-if="scope.row.status==1">启动</el-tag>
                <el-tag type="danger" size="small" v-if="scope.row.status==2">暂停</el-tag>
            </template>
        </el-table-column>
        <el-table-column label="告警级别" align="center" prop="alertLevel" width="90">
            <template slot-scope="scope">
                <dict-tag :options="dict.type.iot_alert_level" :value="scope.row.alertLevel" size="small" />
            </template>
        </el-table-column>
        <el-table-column label="消息通知" align="center" prop="messageType" width="90">
            <template slot-scope="scope">
                <div v-html="formatMessageTypeDisplay(scope.row.messageType)" style="color:#999;"></div>
            </template>
        </el-table-column>
        <el-table-column label="触发器" align="left" header-align="center" prop="triggers" min-width="120">
            <template slot-scope="scope">
                <div v-html="formatTriggersDisplay(scope.row.triggers)" style="overflow:hidden;white-space:nowrap;"></div>
            </template>
        </el-table-column>
        <el-table-column label="执行动作" align="left" header-align="center" prop="actions">
            <template slot-scope="scope">
                <div v-html="formatActionsDisplay(scope.row.actions)" style="overflow:hidden;white-space:nowrap;"></div>
            </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="100">
            <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="80">
            <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['iot:alert:edit']">修改</el-button><br />
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['iot:alert:remove']">删除</el-button>
            </template>
        </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改设备告警对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
        <div class="el-divider el-divider--horizontal" style="margin-top: -25px;"></div>
        <el-form ref="form" :model="form" :rules="rules" label-width="90px">
            <el-row :gutter="50">
                <el-col :span="12">
                    <el-form-item label="告警名称" prop="alertName">
                        <el-input v-model="form.alertName" placeholder="请输入告警名称" />
                    </el-form-item>
                    <el-form-item label="告警级别" prop="alertLevel">
                        <el-select v-model="form.alertLevel" placeholder="请选择告警级别" style="width:100%;">
                            <el-option v-for="dict in dict.type.iot_alert_level" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="告警状态">
                        <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="消息通知" prop="messageType">
                        <el-select v-model="form.messageType" multiple placeholder="请选择" style="width:100%;" @remove-tag="removeMessageTypeTag">
                            <el-option v-for="item in messageTypeoptions" :key="item.value" :label="item.label" :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="备注信息" prop="remark">
                        <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" rows="3" />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-divider></el-divider>
            <el-form-item label="" prop="triggers">
                <el-tooltip class="item" effect="dark" content="满足任意一个条件触发" placement="right-start" slot="label">
                    <span>触发器 <i class="el-icon-question"></i></span>
                </el-tooltip>
                <div v-for="(item,index) in formJson.triggers" :key="index" style="margin-bottom:15px;padding:10px;background-color:#f8f8f8;border-radius:5px;">
                    <el-row>
                        <el-col :span="4">
                            <el-select v-model="item.source" placeholder="请选择" size="small" @change="changeTriggerSource($event,index)">
                                <el-option v-for="subItem in triggerSource" :key="subItem.value" :label="subItem.label" :value="subItem.value">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="2" :offset="18" v-if="index!=0">
                            <el-button size="small" plain type="danger" style="padding:5px;" icon="el-icon-delete" @click="removeTriggerItem(index)">删除</el-button>
                        </el-col>
                    </el-row>

                    <!--定时-->
                    <el-row v-if="item.source==2">
                        <el-col :span="4">
                            <el-time-picker style="width:100%;" v-model="item.timerTimeValue" size="small" value-format="HH:mm" format="HH:mm" placeholder="选择时间" @change="timeChange($event,index)" :disabled="item.isAdvance==1"></el-time-picker>
                        </el-col>
                        <el-col :span="19" :offset="1">
                            <el-select v-model="item.timerWeekValue" placeholder="请选择" multiple style="width:100%;" @change="weekChange($event,index)" size="small" :disabled="item.isAdvance==1">
                                <el-option v-for="subItem in timerWeeks" :key="subItem.value" :label="subItem.label" :value="subItem.value">
                                </el-option>
                            </el-select>
                        </el-col>
                    </el-row>
                    <el-row v-if="item.source==2" style="margin:5px auto">
                        <el-col :span="4">
                            <el-checkbox v-model="item.isAdvance" :true-label="1" :false-label="0" @change="customerCronChange(($event,index))" border size="small" style="width:100%;">自定义CRON</el-checkbox>
                        </el-col>
                        <el-col :span="19" :offset="1" style="padding-top:5px;">
                            <el-input v-model="item.cronExpression" placeholder="cron执行表达式" :disabled="item.isAdvance==0" size="small">
                                <template slot="append">
                                    <el-button type="primary" @click="handleShowCron(item,index)" :disabled="item.isAdvance==0">
                                        生成表达式
                                        <i class="el-icon-time el-icon--right"></i>
                                    </el-button>
                                </template>
                            </el-input>
                        </el-col>
                    </el-row>
                    <!--设备-->
                    <el-row>
                        <el-col :span="4">
                            <el-select v-model="item.type" placeholder="请选择" size="small" @change="triggerTypeChange($event,index)">
                                <el-option v-for="(subItem,subIndex) in triggerTypes" :key="subIndex+'type'" :label="subItem.label" :value="subItem.value">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="5" :offset="1">
                            <el-select v-model="item.id" placeholder="请选择" size="small" v-if="item.type==1" @change="thingsModelTriggerItemChange($event,index)">
                                <el-option v-for="(subItem,subIndex) in thingsModel.properties" :key="subIndex+'property'" :label="subItem.name" :value="subItem.id">
                                </el-option>
                            </el-select>
                            <el-select v-model="item.id" placeholder="请选择" size="small" v-else-if="item.type==2" @change="thingsModelTriggerItemChange($event,index)">
                                <el-option v-for="(subItem,subIndex) in thingsModel.functions" :key="subIndex+'func'" :label="subItem.name" :value="subItem.id">
                                </el-option>
                            </el-select>
                            <el-select v-model="item.id" placeholder="请选择" size="small" v-else-if="item.type==3" @change="thingsModelTriggerItemChange($event,index)">
                                <el-option v-for="(subItem,subIndex) in thingsModel.events" :key="subIndex+'func'" :label="subItem.name" :value="subItem.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4" :offset="1">
                            <el-select v-model="item.operator" placeholder="请选择操作符" size="small" v-if="item.type==1 || item.type==2 || item.type==3">
                                <el-option key="=" label="等于（=）" value="=" />
                                <el-option key="!=" label="不等于（!=）" value="!=" />
                                <el-option key=">" label="大于（>）" value=">" />
                                <el-option key="<" label="小于（<）" value="<" />
                                <el-option key=">=" label="大于等于（>=）" value=">=" />
                                <el-option key="<=" label="小于等于（<=）" value="<=" />
                                <el-option key="contain" label="包含（contain）" value="contain" />
                                <el-option key="notcontain" label="不包含（not contain）" value="notcontain" />
                            </el-select>
                        </el-col>
                        <el-col :span="8" :offset="1" v-if="item.type==1 || item.type==2 || item.type==3">
                            <span v-if="item.thingsModelItem &&(item.thingsModelItem.datatype.type=='integer' || item.thingsModelItem.datatype.type=='decimal')">
                                <el-input v-model="item.value" placeholder="值" :max="item.thingsModelItem.datatype.max" :min="item.thingsModelItem.datatype.min" type="number" size="small">
                                    <template slot="append">{{item.thingsModelItem.datatype.unit}}</template>
                                </el-input>
                            </span>
                            <span v-else-if=" item.thingsModelItem && item.thingsModelItem.datatype.type=='bool'">
                                <el-switch v-model="item.value" :active-text="item.thingsModelItem.datatype.trueText" :inactive-text="item.thingsModelItem.datatype.falseText" active-value="1" inactive-value="0">
                                </el-switch>
                            </span>
                            <span v-else-if="item.thingsModelItem && item.thingsModelItem.datatype.type=='enum'">
                                <el-select v-model="item.value" placeholder="请选择" style="width:100%" size="small">
                                    <el-option v-for="(subItem,subIndex) in item.thingsModelItem.datatype.enumList" :key="subIndex+'things'" :label="subItem.text" :value="subItem.value">
                                    </el-option>
                                </el-select>
                            </span>
                            <span v-else-if="item.thingsModelItem && item.thingsModelItem.datatype.type=='string'">
                                <el-input v-model="item.value" placeholder="请输入字符串" :max="item.thingsModelItem.datatype.maxLength" />
                            </span>
                            <span v-else-if="item.thingsModelItem && item.thingsModelItem.datatype.type=='array'">
                                <el-input v-model="item.value" placeholder="请输入英文逗号分隔的数组" />
                            </span>
                        </el-col>
                    </el-row>
                </div>
                <div>+ <a style="color:#409EFF" @click="addTriggerItem()">添加触发器</a></div>
            </el-form-item>

            <el-divider></el-divider>
            <el-form-item label="执行动作">
                <el-row v-for="(item,index) in formJson.actions" :key="index" style="margin-bottom:10px;">
                    <el-col :span="4">
                        <el-select v-model="item.type" placeholder="请选择" size="small" @change="actionTypeChange($event,index)">
                            <el-option v-for="(subItem,subIndex) in actionTypes" :key="subIndex+'type'" :label="subItem.label" :value="subItem.value">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="5" :offset="1">
                        <el-select v-model="item.id" placeholder="请选择" v-if="item.type==1" size="small" @change="thingsModelActionItemChange($event,index)">
                            <el-option v-for="(subItem,subIndex) in thingsModel.propertiesExceptMonitor" :key="subIndex+'property'" :label="subItem.name" :value="subItem.id">
                            </el-option>
                        </el-select>
                        <el-select v-model="item.id" placeholder="请选择" v-else-if="item.type==2" size="small" @change="thingsModelActionItemChange($event,index)">
                            <el-option v-for="(subItem,subIndex) in thingsModel.functions" :key="subIndex+'func'" :label="subItem.name" :value="subItem.id">
                            </el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="10" :offset="1">
                        <!--物模型项的值-->
                        <span v-if="item.thingsModelItem &&(item.thingsModelItem.datatype.type=='integer' || item.thingsModelItem.datatype.type=='decimal')">
                            <el-input v-model="item.value" placeholder="值" :max="item.thingsModelItem.datatype.max" :min="item.thingsModelItem.datatype.min" type="number" size="small">
                                <template slot="append">{{item.thingsModelItem.datatype.unit}}</template>
                            </el-input>
                        </span>
                        <span v-else-if=" item.thingsModelItem && item.thingsModelItem.datatype.type=='bool'">
                            <el-switch v-model="item.value" :active-text="item.thingsModelItem.datatype.trueText" :inactive-text="item.thingsModelItem.datatype.falseText" active-value="1" inactive-value="0">
                            </el-switch>
                        </span>
                        <span v-else-if="item.thingsModelItem && item.thingsModelItem.datatype.type=='enum'">
                            <el-select v-model="item.value" placeholder="请选择" style="width:100%" size="small">
                                <el-option v-for="(subItem,subIndex) in item.thingsModelItem.datatype.enumList" :key="subIndex+'things'" :label="subItem.text" :value="subItem.value">
                                </el-option>
                            </el-select>
                        </span>
                        <span v-else-if="item.thingsModelItem && item.thingsModelItem.datatype.type=='string'">
                            <el-input v-model="item.value" placeholder="请输入字符串" :max="item.thingsModelItem.datatype.maxLength" />
                        </span>
                        <span v-else-if="item.thingsModelItem && item.thingsModelItem.datatype.type=='array'">
                            <el-input v-model="item.value" placeholder="请输入英文逗号分隔的数组" />
                        </span>
                    </el-col>
                    <el-col :span="2" :offset="1">
                        <el-button size="small" plain type="danger" style="padding:5px;" icon="el-icon-delete" @click="removeActionItem(index)">删除</el-button>
                    </el-col>
                </el-row>
                <div>+ <a style="color:#409EFF" @click="addActionItem()">添加执行动作</a></div>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm" :loading="confirmLoading">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
        </div>
    </el-dialog>

    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar">
        <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression" style="padding-bottom:80px;"></crontab>
    </el-dialog>
</div>
</template>

<script>
import {
    listAlert,
    getAlert,
    delAlert,
    addAlert,
    updateAlert
} from "@/api/iot/alert";
import {
    cacheJsonThingsModel
} from "@/api/iot/model";
import Crontab from '@/components/Crontab'

export default {
    name: "device-alert",
    dicts: ['iot_alert_level', 'sys_job_status'],
    components: {
        Crontab
    },
    props: {
        product: {
            type: Object,
            default: null
        }
    },
    watch: {
        // 获取到父组件传递的productId后，刷新列表
        product: function (newVal, oldVal) {
            this.productInfo = newVal;
            if (this.productInfo && this.productInfo.productId != 0) {
                this.queryParams.productId = this.productInfo.productId;
                // 获取缓存的Json物模型
                cacheJsonThingsModel(newVal.productId).then(response => {
                    this.thingsModel = JSON.parse(response.data);
                    // 过滤监测数据，监测数据为只读
                    this.thingsModel.propertiesExceptMonitor = this.thingsModel.properties.filter(item => item.isMonitor == 0);
                    this.getList();
                });
            }
        }
    },
    data() {
        return {
            // 确认按钮加载
            confirmLoading:false,
            // 物模型JSON
            thingsModel: {},
            // 遮罩层
            loading: false,
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
            // 设备告警表格数据
            alertList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 是否显示Cron表达式弹出层
            openCron: false,
            // 传入的表达式
            expression: "",
            // 触发器的索引，用于接收传入的表达式
            triggerIndex: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                alertName: null,
                alertLevel: null,
                productId: null,
                productName: null,
            },
            // 周
            timerWeeks: [{
                value: 1,
                label: '周一'
            }, {
                value: 2,
                label: '周二'
            }, {
                value: 3,
                label: '周三'
            }, {
                value: 4,
                label: '周四'
            }, {
                value: 5,
                label: '周五'
            }, {
                value: 6,
                label: '周六'
            }, {
                value: 7,
                label: '周日'
            }],
            // 触发器源 1=设备，2=定时
            triggerSource: [{
                value: 1,
                label: '设备触发'
            }, {
                value: 2,
                label: '定时触发'
            }],
            // 触发器类别
            triggerTypes: [{
                    value: 1,
                    label: '属性'
                }, {
                    value: 2,
                    label: '功能'
                }, {
                    value: 3,
                    label: '事件'
                },
                // {
                //     value: 4,
                //     label: '设备升级'
                // }, 
                {
                    value: 5,
                    label: '设备上线'
                },
                {
                    value: 6,
                    label: '设备下线'
                }
            ],
            // 动作类别
            actionTypes: [{
                value: 1,
                label: '属性'
            }, {
                value: 2,
                label: '功能'
            }],
            // 消息通知类型选项
            messageTypeoptions: [{
                value: '1',
                label: '设备告警',
                disabled: true,
            }, {
                value: '2',
                label: '短信通知',
            }, {
                value: '3',
                label: '移动端推送'
            }],
            // 表单参数
            formJson: {
                triggers: [{
                    // 时间
                    timerTimeValue: '',
                    // 星期
                    timerWeekValue: [1, 2, 3, 4, 5, 6, 7],
                }],
                actions: [],
            },
            form: {
                // 1=设备告警 2=短信通知 3=移动端推送
                messageType: ['1'],
            },
            // 产品
            productInfo: {},
            // 表单校验
            rules: {
                alertName: [{
                    required: true,
                    message: "告警名称不能为空",
                    trigger: "blur"
                }],
                messageType: [{
                    required: true,
                    message: "消息通知不能为空",
                    trigger: "blur"
                }],
                alertLevel: [{
                    required: true,
                    message: "告警级别不能为空",
                    trigger: "change"
                }],
                productId: [{
                    required: true,
                    message: "产品ID不能为空",
                    trigger: "blur"
                }],
                productName: [{
                    required: true,
                    message: "产品名称不能为空",
                    trigger: "blur"
                }],
            }
        };
    },

    created() {

    },
    methods: {
        /** 查询设备告警列表 */
        getList() {
            this.loading = true;
            listAlert(this.queryParams).then(response => {
                this.alertList = response.rows;
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
                alertId: null,
                alertName: null,
                alertLevel: 1,
                productId: null,
                productName: null,
                remark: null,
                status: 1,
                // 1=设备告警 2=短信提醒 3=移动端推送
                messageType: ['1'],
            };
            this.formJson = {
                triggers: [{
                    id: "",
                    name: "",
                    value: "",
                    type: 1,
                    productId: 0,
                    productName: "",
                    source: 1, //1=设备，2=定时
                    jobId: 0,
                    cronExpression: "",
                    isAdvance: 0,
                    operator: "=",
                    // 时间
                    timerTimeValue: '',
                    // 星期
                    timerWeekValue: [1, 2, 3, 4, 5, 6, 7]
                }],
                actions: [],
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
            this.ids = selection.map(item => item.alertId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加告警配置";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const alertId = row.alertId || this.ids
            getAlert(alertId).then(response => {
                this.form = response.data;
                this.form.messageType = this.form.messageType.split(',');
                // actions赋值
                this.formJson.actions = JSON.parse(this.form.actions);
                for (let i = 0; i < this.formJson.actions.length; i++) {
                    if (this.formJson.actions[i].type == 1) {
                        for (let j = 0; j < this.thingsModel.properties.length; j++) {
                            if (this.formJson.actions[i].id == this.thingsModel.properties[j].id) {
                                this.formJson.actions[i].thingsModelItem = this.thingsModel.properties[j];
                                break;
                            }
                        }
                    } else if (this.formJson.actions[i].type == 2) {
                        for (let j = 0; j < this.thingsModel.functions.length; j++) {
                            if (this.formJson.actions[i].id == this.thingsModel.functions[j].id) {
                                this.formJson.actions[i].thingsModelItem = this.thingsModel.functions[j];
                                break;
                            }
                        }
                    }
                }
                // triggers赋值
                this.formJson.triggers = JSON.parse(this.form.triggers);
                for (let i = 0; i < this.formJson.triggers.length; i++) {
                    if (this.formJson.triggers[i].source == 2) {
                        if (this.formJson.triggers[i].isAdvance == 0) {
                            // 解析执行时间
                            let arrayValue = this.formJson.triggers[i].cronExpression.substring(12).split(",").map(Number);
                            this.formJson.triggers[i].timerWeekValue = arrayValue;
                            let timerTimeValue = this.formJson.triggers[i].cronExpression.substring(5, 7) + ":" + this.formJson.triggers[i].cronExpression.substring(2, 4)
                            // 解决时间选择器不能编辑问题
                            this.$set(this.formJson.triggers[i], 'timerTimeValue', timerTimeValue);
                        }
                    }
                    // 绑定物模型
                    if (this.formJson.triggers[i].type == 1) {
                        for (let j = 0; j < this.thingsModel.properties.length; j++) {
                            if (this.formJson.triggers[i].id == this.thingsModel.properties[j].id) {
                                this.formJson.triggers[i].thingsModelItem = this.thingsModel.properties[j];
                                break;
                            }
                        }
                    } else if (this.formJson.triggers[i].type == 2) {
                        for (let j = 0; j < this.thingsModel.functions.length; j++) {
                            if (this.formJson.triggers[i].id == this.thingsModel.functions[j].id) {
                                this.formJson.triggers[i].thingsModelItem = this.thingsModel.functions[j];
                                break;
                            }
                        }
                    } else if (this.formJson.triggers[i].type == 3) {
                        for (let j = 0; j < this.thingsModel.events.length; j++) {
                            if (this.formJson.triggers[i].id == this.thingsModel.events[j].id) {
                                this.formJson.triggers[i].thingsModelItem = this.thingsModel.events[j];
                                break;
                            }
                        }
                    }
                }
                this.open = true;
                this.title = "修改设备告警";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    // 触发器
                    for (let i = 0; i < this.formJson.triggers.length; i++) {
                        //设置产品信息
                        this.formJson.triggers[i].productId = this.productInfo.productId;
                        this.formJson.triggers[i].productName = this.productInfo.productName;
                        // 设备升级、上线、下线
                        if (this.formJson.triggers[i].type == 4 || this.formJson.triggers[i].type == 5 || this.formJson.triggers[i].type == 6) {
                            continue;
                        }
                        // 值不能为空
                        if (this.formJson.triggers[i].id == "" || this.formJson.triggers[i].name == "" || this.formJson.triggers[i].value == "") {
                            this.$modal.alertError("触发器中的选项和值不能为空");
                            return;
                        }
                        // 定时时间不能为空
                        if (this.formJson.triggers[i].source == 2) {
                            if (this.formJson.triggers[i].isAdvance == 0) {
                                if (this.formJson.triggers[i].timerTimeValue == "" || this.formJson.triggers[i].timerTimeValue == null) {
                                    this.$modal.alertError("执行时间不能空");
                                    return;
                                }
                                if (this.formJson.triggers[i].timerWeekValue == null || this.formJson.triggers[i].timerWeekValue == "") {
                                    this.$modal.alertError("请选择要执行的星期");
                                    return;
                                }
                            } else if (this.formJson.triggers[i].isAdvance == 1) {
                                if (this.formJson.triggers[i].cronExpression == "") {
                                    this.$modal.alertError("cron表达式不能为空");
                                    return;
                                }
                            }
                        }
                        // 清空不需要提交数据
                        delete this.formJson.triggers[i].thingsModelItem;
                        delete this.formJson.triggers[i].timerTimeValue;
                        delete this.formJson.triggers[i].timerWeekValue;
                        delete this.formJson.triggers[i].timerWeekRepeatValue;
                    }
                    // 动作
                    for (let i = 0; i < this.formJson.actions.length; i++) {
                        // 清空不需要提交数据
                        delete this.formJson.actions[i].thingsModelItem;
                        if (this.formJson.actions[i].id == "" || this.formJson.actions[i].name == "" || this.formJson.actions[i].value == "") {
                            this.$modal.alertError("执行动作中的选项和值不能为空");
                            return;
                        }
                        //设置产品信息
                        this.formJson.actions[i].productId = this.productInfo.productId;
                        this.formJson.actions[i].productName = this.productInfo.productName;
                    }
                    this.form.messageType = this.form.messageType.join(",");
                    this.form.productId = this.productInfo.productId;
                    this.form.productName = this.productInfo.productName;
                    this.form.triggers = JSON.stringify(this.formJson.triggers);
                    this.form.actions = JSON.stringify(this.formJson.actions);
                    this.confirmLoading=true;
                    if (this.form.alertId != null) {
                        updateAlert(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.confirmLoading=false;
                            this.getList();
                        });
                    } else {
                        addAlert(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.confirmLoading=false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const alertIds = row.alertId || this.ids;
            this.$modal.confirm('是否确认删除设备告警编号为"' + alertIds + '"的数据项？').then(function () {
                return delAlert(alertIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => {});
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('iot/alert/export', {
                ...this.queryParams
            }, `alert_${new Date().getTime()}.xlsx`)
        },
        /** 添加动作 */
        addActionItem() {
            this.formJson.actions.push({
                id: "",
                name: "",
                value: "",
                type: 2
            })
        },
        /** 删除动作 */
        removeActionItem(index) {
            this.formJson.actions.splice(index, 1);
        },
        /** 触发器源改变事件 **/
        changeTriggerSource(data, index) {
            // 1=设备，2=定时
            if (data == 2) {
                // 时间
                this.formJson.triggers[index].timerTimeValue = '';
                // 星期
                this.formJson.triggers[index].timerWeekValue = [1, 2, 3, 4, 5, 6, 7];
            }
        },
        /** 添加触发器 */
        addTriggerItem() {
            this.formJson.triggers.push({
                id: "",
                name: "",
                value: "",
                type: 1,
                productId: 0,
                productName: "",
                source: 1, //1=设备，2=定时
                jobId: 0,
                cronExpression: "",
                isAdvance: 0,
                operator: "=",
                // 时间
                timerTimeValue: '',
                // 星期
                timerWeekValue: [1, 2, 3, 4, 5, 6, 7]
            })
        },
        /** 删除触发器 */
        removeTriggerItem(index) {
            this.formJson.triggers.splice(index, 1);
        },
        /** cron表达式按钮操作 */
        handleShowCron(item, index) {
            this.expression = item.cronExpression;
            this.triggerIndex = index;
            this.openCron = true;
        },
        /** 确定后回传值 */
        crontabFill(value) {
            this.formJson.triggers[this.triggerIndex].cronExpression = value;
        },
        /** 星期改变事件 **/
        weekChange(data, index) {
            this.gentCronExpression(index);
        },
        /** 时间改变事件 **/
        timeChange(data, index) {
            this.gentCronExpression(index);
        },
        /**自定义cron表达式选项改变事件 */
        customerCronChange(data, index) {

        },
        /** 生成cron表达式**/
        gentCronExpression(index) {
            let hour = "00";
            let minute = "00";
            console.log('time', this.formJson.triggers[index].timerTimeValue);
            if (this.formJson.triggers[index].timerTimeValue != null && this.formJson.triggers[index].timerTimeValue != "") {
                hour = this.formJson.triggers[index].timerTimeValue.substring(0, 2);
                minute = this.formJson.triggers[index].timerTimeValue.substring(3);
            }
            let week = "*";
            if (this.formJson.triggers[index].timerWeekValue.length > 0) {
                week = this.formJson.triggers[index].timerWeekValue.sort();
            }
            this.formJson.triggers[index].cronExpression = "0 " + minute + " " + hour + " ? * " + week;
        },
        /** 移除消息通知标签事件**/
        removeMessageTypeTag(data) {
            if (data == 1) {
                // 设备告警为必填
                this.form.messageType.unshift(data);
            }
        },
        /** 触发器类型改变事件 **/
        triggerTypeChange(data, index) {
            this.formJson.triggers[index].id = null;
            this.formJson.triggers[index].thingsModelItem = null;
            this.formJson.triggers[index].value = '';
            this.formJson.triggers[index].operator = "=";
        },
        /** 触发器物模型项改变事件 **/
        thingsModelTriggerItemChange(identifier, index) {
            this.formJson.triggers[index].value = "";
            if (this.formJson.triggers[index].type == 1) {
                //属性
                for (let i = 0; i < this.thingsModel.properties.length; i++) {
                    if (this.thingsModel.properties[i].id == identifier) {
                        this.formJson.triggers[index].name = this.thingsModel.properties[i].name;
                        this.formJson.triggers[index].thingsModelItem = this.thingsModel.properties[i];
                        break;
                    }
                }
            } else if (this.formJson.triggers[index].type == 2) {
                //功能
                for (let i = 0; i < this.thingsModel.functions.length; i++) {
                    if (this.thingsModel.functions[i].id == identifier) {
                        this.formJson.triggers[index].name = this.thingsModel.functions[i].name;
                        this.formJson.triggers[index].thingsModelItem = this.thingsModel.functions[i];
                        break;
                    }
                }
            } else if (this.formJson.triggers[index].type == 3) {
                //功能
                for (let i = 0; i < this.thingsModel.events.length; i++) {
                    if (this.thingsModel.events[i].id == identifier) {
                        this.formJson.triggers[index].name = this.thingsModel.events[i].name;
                        this.formJson.triggers[index].thingsModelItem = this.thingsModel.events[i];
                        break;
                    }
                }
            }
        },
        /** 动作类型改变事件 **/
        actionTypeChange(data, index) {
            this.formJson.actions[index].id = null;
            this.formJson.actions[index].value = null;
            this.formJson.actions[index].thingsModelItem = null;
        },
        /** 动作物模型项改变事件 **/
        thingsModelActionItemChange(identifier, index) {
            this.formJson.actions[index].value = "";
            if (this.formJson.actions[index].type == 1) {
                //属性
                for (let i = 0; i < this.thingsModel.properties.length; i++) {
                    if (this.thingsModel.properties[i].id == identifier) {
                        this.formJson.actions[index].name = this.thingsModel.properties[i].name;
                        this.formJson.actions[index].thingsModelItem = this.thingsModel.properties[i];
                        break;
                    }
                }
            } else if (this.formJson.actions[index].type == 2) {
                //功能
                for (let i = 0; i < this.thingsModel.functions.length; i++) {
                    if (this.thingsModel.functions[i].id == identifier) {
                        this.formJson.actions[index].name = this.thingsModel.functions[i].name;
                        this.formJson.actions[index].thingsModelItem = this.thingsModel.functions[i];
                        break;
                    }
                }
            } else if (this.formJson.actions[index].type == 3) {
                //功能
                for (let i = 0; i < this.thingsModel.events.length; i++) {
                    if (this.thingsModel.events[i].id == identifier) {
                        this.formJson.actions[index].name = this.thingsModel.events[i].name;
                        this.formJson.actions[index].thingsModelItem = this.thingsModel.events[i];
                        break;
                    }
                }
            }
        },
        /** 格式化显示消息类型 */
        formatMessageTypeDisplay(data) {
            let messageTypeArray = data.split(',').sort();
            let result = '';
            for (let i = 0; i < messageTypeArray.length; i++) {
                if (messageTypeArray[i] == "1") {
                    result = result + '<span>设备告警</span><br />';
                } else if (messageTypeArray[i] == "2") {
                    result = result + '<span>短信通知</span><br />';
                } else if (messageTypeArray[i] == "3") {
                    result = result + '<span>移动端推送</span><br />';
                }
            }
            return result;
        },
        /** 格式化显示动作 */
        formatActionsDisplay(json) {
            if (json == null || json == "") {
                return;
            }
            let actions = JSON.parse(json);
            let result = "";
            for (let i = 0; i < actions.length; i++) {
                let value = actions[i].value;
                if (actions[i].type == 1) {
                    // 属性
                    for (let j = 0; j < this.thingsModel.properties.length; j++) {
                        if (actions[i].id == this.thingsModel.properties[j].id) {
                            if (this.thingsModel.properties[j].datatype.type == "decimal" || this.thingsModel.properties[j].datatype.type == "integer") {
                                value = actions[i].value + this.thingsModel.properties[j].datatype.unit;
                            } else if (this.thingsModel.properties[j].datatype.type == "enum") {
                                for (let k = 0; k < this.thingsModel.properties[j].datatype.enumList.length; k++) {
                                    if (actions[i].value == this.thingsModel.properties[j].datatype.enumList[k].value) {
                                        value = this.thingsModel.properties[j].datatype.enumList[k].text;
                                        break;
                                    }
                                }
                            } else if (this.thingsModel.properties[j].datatype.type == "bool") {
                                value = actions[i].value == "1" ? this.thingsModel.properties[j].datatype.trueText : this.thingsModel.properties[j].datatype.falseText;
                            }
                            break;
                        }
                    }
                } else if (actions[i].type == 2) {
                    // 功能
                    for (let j = 0; j < this.thingsModel.functions.length; j++) {
                        if (actions[i].id == this.thingsModel.functions[j].id) {
                            if (this.thingsModel.functions[j].datatype.type == "decimal" || this.thingsModel.functions[j].datatype.type == "integer") {
                                value = actions[i].value + this.thingsModel.functions[j].datatype.unit;
                            } else if (this.thingsModel.functions[j].datatype.type == "enum") {
                                for (let k = 0; k < this.thingsModel.functions[j].datatype.enumList.length; k++) {
                                    if (actions[i].value == this.thingsModel.functions[j].datatype.enumList[k].value) {
                                        value = this.thingsModel.functions[j].datatype.enumList[k].text;
                                        break;
                                    }
                                }
                            } else if (this.thingsModel.functions[j].datatype.type == "bool") {
                                value = actions[i].value == "1" ? this.thingsModel.functions[j].datatype.trueText : this.thingsModel.functions[j].datatype.falseText;
                            }
                            break;
                        }
                    }
                }
                result = result + actions[i].name + "：<span style=\"color:#F56C6C\">" + value + "</span><br />";
            }
            return result == "" ? "无" : result;
        },
        /** 格式化显示触发器 */
        formatTriggersDisplay(json) {
            if (json == null || json == "") {
                return;
            }
            let triggers = JSON.parse(json);
            let result = "";
            for (let i = 0; i < triggers.length; i++) {
                let value = triggers[i].value;
                if (triggers[i].type == 1) {
                    // 属性
                    for (let j = 0; j < this.thingsModel.properties.length; j++) {
                        if (triggers[i].id == this.thingsModel.properties[j].id) {
                            if (this.thingsModel.properties[j].datatype.type == "decimal" || this.thingsModel.properties[j].datatype.type == "integer") {
                                value = triggers[i].value + this.thingsModel.properties[j].datatype.unit;
                            } else if (this.thingsModel.properties[j].datatype.type == "enum") {
                                for (let k = 0; k < this.thingsModel.properties[j].datatype.enumList.length; k++) {
                                    if (triggers[i].value == this.thingsModel.properties[j].datatype.enumList[k].value) {
                                        value = this.thingsModel.properties[j].datatype.enumList[k].text;
                                        break;
                                    }
                                }
                            } else if (this.thingsModel.properties[j].datatype.type == "bool") {
                                value = triggers[i].value == "1" ? this.thingsModel.properties[j].datatype.trueText : this.thingsModel.properties[j].datatype.falseText;
                            }
                            break;
                        }
                    }
                    result = result + triggers[i].name + " <span style=\"color:#F56C6C\">" + triggers[i].operator + " " + value + "</span>";
                } else if (triggers[i].type == 2) {
                    // 功能
                    for (let j = 0; j < this.thingsModel.functions.length; j++) {
                        if (triggers[i].id == this.thingsModel.functions[j].id) {
                            if (this.thingsModel.functions[j].datatype.type == "decimal" || this.thingsModel.functions[j].datatype.type == "integer") {
                                value = triggers[i].value + this.thingsModel.functions[j].datatype.unit;
                            } else if (this.thingsModel.functions[j].datatype.type == "enum") {
                                for (let k = 0; k < this.thingsModel.functions[j].datatype.enumList.length; k++) {
                                    if (triggers[i].value == this.thingsModel.functions[j].datatype.enumList[k].value) {
                                        value = this.thingsModel.functions[j].datatype.enumList[k].text;
                                        break;
                                    }
                                }
                            } else if (this.thingsModel.functions[j].datatype.type == "bool") {
                                value = triggers[i].value == "1" ? this.thingsModel.functions[j].datatype.trueText : this.thingsModel.functions[j].datatype.falseText;
                            }
                            break;
                        }
                    }
                    result = result + triggers[i].name + " <span style=\"color:#F56C6C\">" + triggers[i].operator + " " + value + "</span>";
                } else if (triggers[i].type == 3) {
                    // 事件
                    for (let j = 0; j < this.thingsModel.events.length; j++) {
                        if (triggers[i].id == this.thingsModel.events[j].id) {
                            if (this.thingsModel.events[j].datatype.type == "decimal" || this.thingsModel.events[j].datatype.type == "integer") {
                                value = triggers[i].value + this.thingsModel.events[j].datatype.unit;
                            } else if (this.thingsModel.events[j].datatype.type == "enum") {
                                for (let k = 0; k < this.thingsModel.events[j].datatype.enumList.length; k++) {
                                    if (triggers[i].value == this.thingsModel.events[j].datatype.enumList[k].value) {
                                        value = this.thingsModel.events[j].datatype.enumList[k].text;
                                        break;
                                    }
                                }
                            } else if (this.thingsModel.events[j].datatype.type == "bool") {
                                value = triggers[i].value == "1" ? this.thingsModel.events[j].datatype.trueText : this.thingsModel.events[j].datatype.falseText;
                            }
                            break;
                        }
                    }
                    result = result + triggers[i].name + " <span style=\"color:#F56C6C\">" + triggers[i].operator + " " + value + "</span>";
                } else if (triggers[i].type == 4) {
                    // 设备升级
                    result = result + "<span style=\"color:#F56C6C\">设备升级</span>";
                } else if (triggers[i].type == 5) {
                    // 设备上线
                    result = result + "<span style=\"color:#F56C6C\">设备上线</span>";
                } else if (triggers[i].type == 6) {
                    // 设备下线
                    result = result + "<span style=\"color:#F56C6C\">设备下线</span>";
                }
                // 定时
                if (triggers[i].source == 2) {
                    result = result + '<span style="margin-left:10px;color:#409EFF"><i class="el-icon-time"></i> ' + triggers[i].cronExpression + "</span><br />";
                } else {
                    result = result + '<span style="margin-left:10px;color:#aaa;">设备</span><br />';
                }
            }
            return result;
        },
    }
};
</script>
