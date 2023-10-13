<template>
<el-card style="margin:6px;padding-bottom:100px;">
    <el-tabs v-model="activeName" tab-position="left" style="padding:10px;min-height:400px;">
        <el-tab-pane name="basic">
            <span slot="label"> * 基本信息</span>
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-row :gutter="100">
                    <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="8">
                        <el-form-item label="设备名称" prop="deviceName">
                            <el-input v-model="form.deviceName" placeholder="请输入设备名称">
                                <el-button slot="append" @click="openSummaryDialog" v-if="form.deviceId!=0">摘要</el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="" prop="productName">
                            <template slot="label">
                                <span style="color:red;">* </span>所属产品
                            </template>
                            <el-input readonly v-model="form.productName" placeholder="请选择产品" :disabled="form.status!=1">
                                <el-button slot="append" @click="selectProduct()" :disabled="form.status!=1">选择</el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="" prop="serialNumber">
                            <template slot="label">
                                <span style="color:red;">* </span>设备编号
                            </template>
                            <el-input v-model="form.serialNumber" placeholder="请输入设备编号" :disabled="form.status!=1" maxlength="32">
                                <el-button v-if="form.deviceType !==3" slot="append" @click="generateNum" :loading="genDisabled" :disabled="form.status!=1">生成
                                </el-button>
                                <el-button v-if="form.deviceType ===3" slot="append" @click="genSipID()" :disabled="form.status!=1">生成
                                </el-button>
                            </el-input>
                        </el-form-item>

                        <el-form-item label="固件版本" prop="firmwareVersion">
                            <el-input v-model="form.firmwareVersion" placeholder="请输入固件版本" type="number" step="0.1" :disabled="form.status!=1 || form.deviceType ===3">
                                <template slot="prepend">Version</template>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="禁用设备" prop="deviceStatus">
                            <el-switch v-model="deviceStatus" active-text="" inactive-text="" :disabled="form.status==1 || form.deviceType ===3" :active-value="1" :inactive-value="0" active-color="#F56C6C">
                            </el-switch>
                        </el-form-item>
                        <el-form-item label="设备影子" prop="isShadow">
                            <el-switch v-model="form.isShadow" active-text="" inactive-text="" :active-value="1" :inactive-value="0" :disabled="form.deviceType ===3">
                            </el-switch>
                        </el-form-item>
                        <el-form-item label="备注信息" prop="remark">
                            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" rows="3" />
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="8">
                        <el-form-item label="定位方式" prop="locationWay">
                            <el-select v-model="form.locationWay" placeholder="请选择设备状态" clearable size="small" style="width:100%;" :disabled="form.deviceType ===3">
                                <el-option v-for="dict in dict.type.iot_location_way" :key="dict.value" :label="dict.label" :value="Number(dict.value)" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="设备经度" prop="longitude">
                            <el-input v-model="form.longitude" placeholder="请输入设备经度" type="number" :disabled="form.locationWay!=3">
                                <el-link slot="append" :underline="false" href="https://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank" :disabled="form.locationWay!=3">坐标拾取
                                </el-link>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="设备纬度" prop="latitude">
                            <el-input v-model="form.latitude" placeholder="请输入设备纬度" type="number" :disabled="form.locationWay!=3">
                                <el-link slot="append" :underline="false" href="https://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank" :disabled="form.locationWay!=3">坐标拾取
                                </el-link>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="所在地址" prop="networkAddress">
                            <el-input v-model="form.networkAddress" placeholder="请输入设备所在地址" :disabled="form.locationWay!=3" />
                        </el-form-item>
                        <el-form-item label="入网地址" prop="networkIp">
                            <el-input v-model="form.networkIp" placeholder="设备入网IP" disabled />
                        </el-form-item>
                        <el-form-item label="激活时间" prop="activeTime">
                            <el-date-picker clearable v-model="form.activeTime" type="date" value-format="yyyy-MM-dd" placeholder="设备激活时间" disabled style="width:100%">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item label="设备信号" prop="rssi">
                            <el-input v-model="form.rssi" placeholder="设备信号强度" disabled />
                        </el-form-item>
                        <el-form-item label="设备状态" prop="status">
                            <dict-tag :options="dict.type.iot_device_status" :value="form.status" style="width:60px;display:inline-block;" />
                        </el-form-item>
                    </el-col>

                    <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="8">
                        <div style="border:1px solid #dfe4ed;border-radius:5px;padding:5px;text-align:center;line-height:400px;">
                            <div id="map" style="height:435px;width:100%;">地图展示区域，新增后显示</div>
                        </div>
                    </el-col>
                </el-row>
            </el-form>

            <el-form label-width="100px" style="margin-top:50px;">
                <el-form-item style="text-align: center;margin-left:-100px;margin-top:10px;">
                    <el-button type="primary" @click="submitForm" v-hasPermi="['iot:device:edit']" v-show="form.deviceId!=0">修 改</el-button>
                    <el-button type="primary" @click="submitForm" v-hasPermi="['iot:device:add']" v-show="form.deviceId==0">新 增</el-button>
                </el-form-item>
            </el-form>

            <!-- 选择产品 -->
            <product-list ref="productList" :productId="form.productId" @productEvent="getProductData($event)" />

            <sipid ref="sipidGen" :product="form" @addGenEvent="getSipIDData($event)" />

        </el-tab-pane>

        <el-tab-pane name="runningStatus" :disabled="form.deviceId==0" v-if="form.deviceType !==3">
            <span slot="label">运行状态</span>
            <running-status ref="runningStatus" :device="form" @statusEvent="getDeviceStatusData($event)" />
        </el-tab-pane>

        <el-tab-pane name="deviceTimer" :disabled="form.deviceId==0" v-if="form.deviceType !==3">
            <span slot="label">设备定时</span>
            <device-timer ref="deviceTimer" :device="form" />
        </el-tab-pane>

        <el-tab-pane name="deviceUser" :disabled="form.deviceId==0">
            <span slot="label">设备用户</span>
            <device-user ref="deviceUser" :device="form" @userEvent="getUserData($event)" />
        </el-tab-pane>

        <el-tab-pane name="deviceLog" :disabled="form.deviceId==0" v-if="form.deviceType !==3">
            <span slot="label">设备日志</span>
            <device-log ref="deviceLog" :device="form" />
        </el-tab-pane>

        <el-tab-pane name="deviceMonitor" :disabled="form.deviceId==0" v-if="form.deviceType !==3">
            <span slot="label">实时监测</span>
            <device-monitor ref="deviceMonitor" :device="form" />
        </el-tab-pane>

        <el-tab-pane name="deviceStastic" :disabled="form.deviceId==0" v-if="form.deviceType !==3">
            <span slot="label">监测统计</span>
            <device-statistic ref="deviceStatistic" :device="form" />
        </el-tab-pane>

        <el-tab-pane name="runningStatus" :disabled="form.deviceId==0" v-if="form.deviceType ===3">
            <span slot="label">设备通道</span>
            <channel ref="deviceChannel" :device="form" />
        </el-tab-pane>

        <!-- 用于设置间距 -->
        <el-tab-pane>
            <span slot="label">
                <div style="margin-top:200px;"></div>
            </span>
        </el-tab-pane>

        <el-tab-pane name="device04" v-if="form.deviceType !==3">
            <span slot="label">
                <el-tooltip class="item" effect="dark" content="用于查看发送的指令，设备是否已经响应" placement="right-start">
                    <el-button type="warning" size="mini" @click="deviceSynchronization()" :disabled="form.deviceId==0">数据同步</el-button>
                </el-tooltip>
            </span>
        </el-tab-pane>
        <el-tab-pane name="device05">
            <span slot="label">
                <el-button type="info" size="mini" @click="goBack()">返回列表</el-button>
            </span>
        </el-tab-pane>
    </el-tabs>

    <!-- 设备配置JSON -->
    <el-dialog title="摘要（设备上传的只读数据）" :visible.sync="openSummary" width="700px" append-to-body>
        <el-row :gutter="20">
            <el-col :span="14">
                <div style="border:1px solid #ccc;margin-top:-15px;height:350px;width:360px; overflow:scroll;">
                    <json-viewer :value="summary" :expand-depth=10 copyable>
                        <template v-slot:copy>
                            复制
                        </template>
                    </json-viewer>
                </div>
            </el-col>
            <el-col :span="10">
                <div style="border:1px solid #ccc;width:200px;text-align: center;margin-left:20px;margin-top:-10px;">
                    <vue-qr :text="qrText" :size="200"></vue-qr>
                    <div style="padding-bottom:10px;">设备二维码</div>
                </div>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-button type="info" @click="closeSummaryDialog">关 闭</el-button>
        </div>
    </el-dialog>

</el-card>
</template>

<script>
import JsonViewer from 'vue-json-viewer'
import 'vue-json-viewer/style.css'
import productList from "./product-list"
import deviceLog from './device-log';
import deviceUser from './device-user';
import runningStatus from './running-status';
import deviceMonitor from './device-monitor'
import deviceStatistic from './device-statistic'
import deviceTimer from './device-timer'
import channel from '../sip/channel'
import sipid from '../sip/sipid-dialog'
import vueQr from 'vue-qr'
import {
    loadBMap
} from '@/utils/map.js'
import {
    deviceSynchronization,
    getDevice,
    addDevice,
    updateDevice,
    generatorDeviceNum
} from "@/api/iot/device";
import {
    getDeviceRunningStatus
} from "@/api/iot/device"
import {
    cacheJsonThingsModel
} from "@/api/iot/model";
import {
    getSipDevice
} from "@/api/iot/sipdevice";

export default {
    name: "DeviceEdit",
    dicts: ['iot_device_status', 'iot_location_way'],
    components: {
        deviceLog,
        deviceUser,
        deviceMonitor,
        deviceStatistic,
        runningStatus,
        productList,
        deviceTimer,
        JsonViewer,
        vueQr,
        channel,
        sipid
    },
    watch: {
        activeName(val) {
            if (val == "deviceStastic") {
                this.$nextTick(() => {
                    // TODO 【项目】重置统计表格的尺寸

                })
            }
        }
    },
    computed: {
        deviceStatus: {
            set(val) {
                if (val == 1) {
                    // 1-未激活，2-禁用，3-在线，4-离线
                    this.form.status = 2;
                } else if (val == 0) {
                    this.form.status = 4;
                } else {
                    this.form.status = this.oldDeviceStatus;
                }
            },
            get() {
                if (this.form.status == 2) {
                    return 1;
                }
                return 0
            },
        },
    },
    data() {
        return {
            // 二维码内容
            qrText: 'frog',
            // 打开设备配置对话框
            openSummary: false,
            // 生成设备编码是否禁用
            genDisabled: false,
            // 选中选项卡
            activeName: 'basic',
            // 遮罩层
            loading: true,
            // 设备开始状态
            oldDeviceStatus: null,
            // 表单参数
            form: {
                productId: 0,
                status: 1,
                locationWay: 1,
                firmwareVersion: 1.0,
                serialNumber:"",
            },
            // 设备摘要
            summary: [],
            // 地址
            baseUrl: process.env.VUE_APP_BASE_API,
            // 地图相关
            map: null,
            mk: null,
            latitude: '',
            longitude: '',
            // 表单校验
            rules: {
                deviceName: [{
                    required: true,
                    message: "设备名称不能为空",
                    trigger: "blur"
                }, {
                    min: 2,
                    max: 32,
                    message: '设备名称长度在 2 到 32 个字符',
                    trigger: 'blur'
                }],
                firmwareVersion: [{
                    required: true,
                    message: "固件版本不能为空",
                    trigger: "blur"
                }],
            },
            isMediaDevice: false,
        };
    },
    created() {
        // 跳转选项卡
        let activeName = this.$route.query.activeName
        if (activeName != null && activeName != "") {
            this.activeName = activeName;
        }
        // 获取设备信息
        this.form.deviceId = this.$route.query && this.$route.query.deviceId;
        if (this.form.deviceId != 0) {
            this.connectMqtt();
            this.getDevice(this.form.deviceId);
        }

    },
    activated() {
        // 跳转选项卡
        let activeName = this.$route.query.activeName
        if (activeName != null && activeName != "") {
            this.activeName = activeName;
        }

    },
    destroyed() {
        // 取消订阅主题
        this.mqttUnSubscribe(this.form);
    },
    methods: {
        /* 连接Mqtt消息服务器 */
        async connectMqtt() {
            if (this.$mqttTool.client == null) {
                await this.$mqttTool.connect(this.vuex_token);
            }
            this.mqttCallback();
        },
        /* Mqtt回调处理  */
        mqttCallback() {
            this.$mqttTool.client.on('message', (topic, message, buffer) => {
                let topics = topic.split('/');
                let productId = topics[1];
                let deviceNum = topics[2];
                message = JSON.parse(message.toString());
                if (!message) {
                    return;
                }
                if (topics[3] == 'status') {
                    console.log('接收到【设备状态-详情】主题：', topic);
                    console.log('接收到【设备状态-详情】内容：', message);
                    // 更新列表中设备的状态
                    if (this.form.serialNumber == deviceNum) {
                        this.oldDeviceStatus = message.status;
                        this.form.status = message.status;
                        this.form.isShadow = message.isShadow;
                        this.form.rssid = message.rssid;
                    }
                }
            });
        },
        /** Mqtt订阅主题 */
        mqttSubscribe(device) {
            // 订阅当前设备状态和实时监测
            let topicStatus = '/' + device.productId + '/' + device.serialNumber + '/status/post';
            let topicProperty = '/' + device.productId + '/' + device.serialNumber + '/property/post';
            let topicFunction = '/' + device.productId + '/' + device.serialNumber + '/function/post';
            let topicMonitor = "/" + device.productId + "/" + device.serialNumber + "/monitor/post";
            let topics = [];
            topics.push(topicStatus);
            topics.push(topicProperty);
            topics.push(topicFunction);
            topics.push(topicMonitor);
            this.$mqttTool.subscribe(topics);
        },
        /** Mqtt取消订阅主题 */
        mqttUnSubscribe(device) {
            // 订阅当前设备状态和实时监测
            let topicStatus = '/' + device.productId + '/' + device.serialNumber + '/status/post';
            let topicProperty = '/' + device.productId + '/' + device.serialNumber + '/property/post';
            let topicFunction = '/' + device.productId + '/' + device.serialNumber + '/function/post';
            let topicMonitor = "/" + device.productId + "/" + device.serialNumber + "/monitor/post";
            let topics = [];
            topics.push(topicStatus);
            topics.push(topicProperty);
            topics.push(topicFunction);
            topics.push(topicMonitor);
            console.log('取消订阅', topics);
            this.$mqttTool.unsubscribe(topics);
        },

        // 获取子组件订阅的设备状态
        getDeviceStatusData(status) {
            this.form.status = status;
        },
        /** 数据同步*/
        deviceSynchronization() {
            deviceSynchronization(this.form.serialNumber).then(async response => {
                // 获取缓存物模型
                response.data.cacheThingsModel = await this.getCacheThingsModdel(response.data.productId);
                // 获取设备运行状态
                response.data.thingsModels = await this.getDeviceStatus(this.form.deviceId);
                // 格式化物模型，拆分出监测值,数组添加前缀
                this.formatThingsModel(response.data);
                this.form = response.data;
                // 选项卡切换
                this.activeName = 'runningStatus';
                this.oldDeviceStatus = this.form.status;
                this.loadMap();
            });
        },
        /**获取设备详情*/
        getDevice(deviceId) {
            getDevice(deviceId).then(async response => {
                // 获取缓存物模型
                response.data.cacheThingsModel = await this.getCacheThingsModdel(response.data.productId);
                // 获取设备运行状态
                response.data.thingsModels = await this.getDeviceStatus(deviceId);
                // 格式化物模型，拆分出监测值,数组添加前缀
                this.formatThingsModel(response.data);
                this.form = response.data;
                // 解析设备摘要
                if (this.form.summary != null && this.form.summary != "") {
                    this.summary = JSON.parse(this.form.summary);
                }
                this.oldDeviceStatus = this.form.status;
                this.loadMap();
                //Mqtt订阅
                this.mqttSubscribe(this.form);
            });
        },
        /** 获取缓存物模型*/
        getCacheThingsModdel(productId) {
            return new Promise((resolve, reject) => {
                cacheJsonThingsModel(productId).then(response => {
                    resolve(JSON.parse(response.data));
                }).catch(error => {
                    reject(error);
                })
            })
        },
        /**获取设备运行状态*/
        getDeviceStatus(deviceId) {
            return new Promise((resolve, reject) => {
                getDeviceRunningStatus(deviceId).then(response => {
                    resolve(response.data.thingsModels);
                }).catch(error => {
                    reject(error);
                })
            })
        },
        formatThingsModel(data) {
            data.monitorList = [];
            // 物模型格式化
            for (let i = 0; i < data.thingsModels.length; i++) {
                if (data.thingsModels[i].dataType.type == "array") {
                    if (data.thingsModels[i].dataType.arrayType == "object") {
                        for (let k = 0; k < data.thingsModels[i].dataType.arrayParams.length; k++) {
                            for (let j = 0; j < data.thingsModels[i].dataType.arrayParams[k].length; j++) {
                                // 数组元素中参数ID添加前缀，例如：array_00_
                                let index = k > 9 ? String(k) : '0' + k;
                                let prefix = 'array_' + index + '_';
                                data.thingsModels[i].dataType.arrayParams[k][j].id = prefix + data.thingsModels[i].dataType.arrayParams[k][j].id;
                                // 图表分类放置
                                if (data.thingsModels[i].dataType.arrayParams[k][j].isMonitor == 1) {
                                    data.thingsModels[i].dataType.arrayParams[k][j].name = "[" + data.thingsModels[i].name + (k + 1) + "] " + data.thingsModels[i].dataType.arrayParams[k][j].name;
                                    data.thingsModels[i].dataType.arrayParams[k][j].dataType.arrayType = "object";
                                    data.monitorList.push(data.thingsModels[i].dataType.arrayParams[k][j]);
                                    data.thingsModels[i].dataType.arrayParams[k].splice(j--, 1);
                                }
                            }
                        }
                    } else {
                        // 字符串拆分为物模型数组 model=id/name/type/isReadonly/value/shadow
                        let values = data.thingsModels[i].value != "" ? data.thingsModels[i].value.split(',') : [];
                        let shadows = data.thingsModels[i].shadow != "" ? data.thingsModels[i].shadow.split(',') : [];
                        for (let j = 0; j < data.thingsModels[i].dataType.arrayCount; j++) {
                            if (!data.thingsModels[i].dataType.arrayModel) {
                                data.thingsModels[i].dataType.arrayModel = [];
                            }
                            // 数组里面的ID需要添加前缀和索引，例如：array_00_temperature
                            let index = j > 9 ? String(j) : '0' + j;
                            let prefix = 'array_' + index + '_';
                            data.thingsModels[i].dataType.arrayModel[j] = {
                                id: prefix + data.thingsModels[i].id,
                                name: data.thingsModels[i].name,
                                type: data.thingsModels[i].type,
                                isReadonly: data.thingsModels[i].isReadonly,
                                value: values[j] ? values[j] : "",
                                shadow: shadows[j] ? shadows[j] : ""
                            }
                        }

                    }
                } else if (data.thingsModels[i].dataType.type == "object") {
                    for (let j = 0; j < data.thingsModels[i].dataType.params.length; j++) {
                        // 图表分类放置
                        if (data.thingsModels[i].dataType.params[j].isMonitor == 1) {
                            data.thingsModels[i].dataType.params[j].name = "[" + data.thingsModels[i].name + "] " + data.thingsModels[i].dataType.params[j].name;
                            data.monitorList.push(data.thingsModels[i].dataType.params[j]);
                            data.thingsModels[i].dataType.params.splice(j--, 1);
                        }
                    }
                } else if (data.thingsModels[i].isMonitor == 1) {
                    data.monitorList.push(data.thingsModels[i]);
                    // 使用i--解决索引变更问题
                    data.thingsModels.splice(i--, 1);
                }

            }
        },
        /**加载地图*/
        loadMap() {
            this.$nextTick(() => {
                loadBMap().then(() => {
                    this.getmap();
                });
            })
        },
        /** 返回按钮 */
        goBack() {
            const obj = {
                path: "/iot/device",
                query: {
                    t: Date.now(),
                    pageNum: this.$route.query.pageNum
                }
            };
            this.$tab.closeOpenPage(obj);
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                deviceId: 0,
                deviceName: null,
                productId: null,
                productName: null,
                userId: null,
                userName: null,
                tenantId: null,
                tenantName: null,
                serialNumber: "",
                firmwareVersion: 1.0,
                status: 1,
                rssi: null,
                networkAddress: null,
                networkIp: null,
                longitude: null,
                latitude: null,
                activeTime: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                remark: null,
                locationWay: 1,
            };
            this.deviceStatus = 0;
            this.resetForm("form");
        },
        /** 提交按钮 */
        submitForm() {
            if (this.form.serialNumber == null || this.form.serialNumber == 0) {
                this.$modal.alertError("设备编号不能为空");
                return;
            }
            let reg = /^[0-9a-zA-Z]+$/;
            if (!reg.test(this.form.serialNumber)) {
                this.$modal.alertError("设备编号只能是字母和数字");
                return;
            }
            if (this.form.productId == null || this.form.productId == 0) {
                this.$modal.alertError("所属产品不能为空");
                return;
            }

            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.deviceId != 0) {
                        updateDevice(this.form).then(response => {
                            if (response.data == 0) {
                                this.$modal.alertError(response.msg);
                            } else {
                                this.$modal.alertSuccess("修改成功");
                                this.form = JSON.parse(JSON.stringify(this.form));
                                this.loadMap();
                            }
                        });
                    } else {
                        addDevice(this.form).then(response => {
                            this.form = response.data;
                            this.oldDeviceStatus = this.form.status;
                            if (this.form.deviceId == null || this.form.deviceId == 0) {
                                this.$modal.alertError("设备编号已经存在，添加设备失败");
                            } else {
                                if (this.form.status == 2) {
                                    this.deviceStatus = 1;
                                }
                                this.$modal.alertSuccess("添加设备成功");
                                this.loadMap();
                            }
                        });
                    }
                }
            });
        },
        /**选择产品 */
        selectProduct() {
            this.$refs.productList.open = true;
            this.$refs.productList.getList();
        },
        genSipID() {
            this.$refs.sipidGen.open = true;
        },
        /**获取选中的产品 */
        getProductData(product) {
            this.form.productId = product.productId;
            this.form.productName = product.productName;
            this.form.deviceType = product.deviceType;
            this.form.tenantId = product.tenantId;
            this.form.tenantName = product.tenantName;
        },
        getSipIDData(devsipid) {
            this.form.serialNumber = devsipid;
        },
        // 获取选中的用户
        getUserData(user) {

        },
        /**关闭物模型 */
        openSummaryDialog() {
            let json = {
                type: 1, // 1=扫码关联设备
                deviceNumber: this.form.serialNumber,
                productId: this.form.productId,
                productName: this.form.productName,
            };
            this.qrText = JSON.stringify(json);
            this.openSummary = true;
        },
        /**关闭物模型 */
        closeSummaryDialog() {
            this.openSummary = false;
        },

        // 地图定位
        getmap() {
            this.map = new BMap.Map('map')
            let point = null;
            if (this.form.longitude != null && this.form.longitude != "" && this.form.latitude != null && this.form.latitude != "") {
                point = new BMap.Point(this.form.longitude, this.form.latitude)
            } else {
                point = new BMap.Point(116.404, 39.915)
            }
            this.map.centerAndZoom(point, 19)
            this.map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
            this.map.addControl(new BMap.NavigationControl())

            // 标注设备位置
            this.mk = new BMap.Marker(point)
            this.map.addOverlay(this.mk)
            this.map.panTo(point)
        },
        // 生成随机字母和数字
        generateNum() {
            if(!this.form.productId || this.form.productId==0){
              this.$modal.alertError("请先选择产品");
              return;
            }
            this.genDisabled = true;
            generatorDeviceNum().then(response => {
                this.form.serialNumber = response.data;
                this.genDisabled = false;
            })
        }
    }
};
</script>
