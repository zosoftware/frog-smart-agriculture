<template>
<div>
    <el-row :gutter="120">
        <el-col :xs="24" :sm="24" :md="24" :lg="14" :xl="10" style="margin-bottom:50px;">
            <el-descriptions :column="1" border style="margin-bottom:50px;">
                <!-- 设备模式-->
                <el-descriptions-item :labelStyle="statusColor">
                    <template slot="label">
                        <i class="el-icon-menu"></i>
                        设备模式
                    </template>
                    <el-link :underline="false" style="line-height:28px;font-size:16px;padding-right:10px;">{{title}}</el-link>
                </el-descriptions-item>
                <!-- 设备升级-->
                <el-descriptions-item :labelStyle="statusColor">
                    <template slot="label">
                        <svg-icon icon-class="ota" />
                        OTA升级
                    </template>
                    <el-link :underline="false" style="line-height:28px;font-size:16px;padding-right:10px;">Version {{deviceInfo.firmwareVersion}}</el-link>
                    <el-button type="success" size="mini" style="float:right;" @click="getLatestFirmware(deviceInfo.deviceId)" :disabled="deviceInfo.status!=3">检查更新</el-button>
                </el-descriptions-item>

                <!-- 设备物模型-->
                <el-descriptions-item v-for="(item,index) in deviceInfo.thingsModels" :key="index" :labelStyle="statusColor">
                    <template slot="label">
                        <i class="el-icon-open"></i>
                        {{item.name}}
                    </template>
                    <div v-if="item.dataType.type=='bool'">
                        <el-switch v-model="item.shadow" @change="mqttPublish(deviceInfo,item)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" :disabled="(shadowUnEnable || item.isReadonly==1)" />
                    </div>
                    <div v-if="item.dataType.type=='enum'">
                        <el-select v-model="item.shadow" placeholder="请选择" @change="mqttPublish(deviceInfo,item)" :disabled="(shadowUnEnable || item.isReadonly==1)">
                            <el-option v-for="subItem in item.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                        </el-select>
                    </div>
                    <div v-if="item.dataType.type=='string'">
                        <el-input v-model="item.shadow" :placeholder="'请输入字符串 '+(item.dataType.unit?'，单位：'+item.dataType.unit:'')" :disabled="(shadowUnEnable || item.isReadonly==1)">
                            <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,item)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && item.isReadonly==0)"></el-button>
                        </el-input>
                    </div>
                    <div v-if="item.dataType.type=='decimal'">
                        <el-input v-model="item.shadow" type="number" :placeholder="'请输入小数 '+(item.dataType.unit?'，单位：'+item.dataType.unit:'')" :disabled="shadowUnEnable || item.isReadonly==1">
                            <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,item)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && item.isReadonly==0)"></el-button>
                        </el-input>
                    </div>
                    <div v-if="item.dataType.type=='integer'">
                        <el-input v-model="item.shadow" type="integer" :placeholder="'请输入整数 '+(item.dataType.unit?'，单位：'+item.dataType.unit:'')" :disabled="shadowUnEnable || item.isReadonly==1">
                            <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,item)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && item.isReadonly==0)"></el-button>
                        </el-input>
                    </div>
                    <div v-if="item.dataType.type=='object'">
                        <el-descriptions :column="1" size="mini" border>
                            <el-descriptions-item v-for="(param,index) in item.dataType.params" :key="index" :label="param.name">
                                <div v-if="param.dataType.type=='bool'">
                                    <el-switch v-model="param.shadow" @change="mqttPublish(deviceInfo,param)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" :disabled="(shadowUnEnable || param.isReadonly==1)" />
                                </div>
                                <div v-if="param.dataType.type=='enum'">
                                    <el-select v-model="param.shadow" placeholder="请选择" @change="mqttPublish(deviceInfo,param)" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                        <el-option v-for="subItem in param.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                                    </el-select>
                                </div>
                                <div v-if="param.dataType.type=='string'">
                                    <el-input v-model="param.shadow" placeholder="请输入字符串" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                    </el-input>
                                </div>
                                <div v-if="param.dataType.type=='decimal'">
                                    <el-input v-model="param.shadow" type="number" placeholder="请输入小数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                    </el-input>
                                </div>
                                <div v-if="param.dataType.type=='integer'">
                                    <el-input v-model="param.shadow" type="integer" placeholder="请输入整数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                    </el-input>
                                </div>
                            </el-descriptions-item>
                        </el-descriptions>
                    </div>
                    <div v-if="item.dataType.type=='array'">
                        <!-- <div style="margin-bottom:10px;">
                            <el-input v-model="item.shadow" placeholder="请输入英文逗号分隔的字符串" :disabled="shadowUnEnable || item.isReadonly==1" v-if="item.dataType.arrayType!='object'" @input="arrayInputChange($event,item)">
                                <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,item)" style="font-size:20px;" title="指令发送"></el-button>
                            </el-input>
                        </div> -->
                        <el-descriptions :column="1" size="mini" border v-if="item.dataType.arrayType!='object'">
                            <el-descriptions-item v-for="(model,index) in item.dataType.arrayModel" :key="index" :label="item.name+(index+1)">
                                <div v-if="item.dataType.arrayType=='string'">
                                    <el-input placeholder="请输入字符串" size="mini" v-model="model.shadow" :disabled="shadowUnEnable || item.isReadonly==1" @input="arrayItemChange($event,item)">
                                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,model)" style="font-size:20px;" title="指令发送" v-if="!shadowUnEnable || item.isReadonly==0"></el-button>
                                    </el-input>
                                </div>
                                <div v-if="item.dataType.arrayType=='decimal'">
                                    <el-input type="number" placeholder="请输入小数 " size="mini" v-model="model.shadow" :disabled="shadowUnEnable || item.isReadonly==1" @input="arrayItemChange($event,item)">
                                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,model)" style="font-size:20px;" title="指令发送" v-if="!shadowUnEnable || item.isReadonly==0"></el-button>
                                    </el-input>
                                </div>
                                <div v-if="item.dataType.arrayType=='integer'">
                                    <el-input type="integer" placeholder="请输入整数 " size="mini" v-model="model.shadow" :disabled="shadowUnEnable || item.isReadonly==1" @input="arrayItemChange($event,item)">
                                        <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,model)" style="font-size:20px;" title="指令发送" v-if="!shadowUnEnable || item.isReadonly==0"></el-button>
                                    </el-input>
                                </div>
                            </el-descriptions-item>
                        </el-descriptions>
                        <el-collapse v-if="item.dataType.arrayType=='object'">
                            <el-collapse-item v-for="(arrayParam,index) in item.dataType.arrayParams" :key="index">
                                <template slot="title">
                                    <span style="color:#666;"><i class="el-icon-tickets"></i> {{item.name+(index+1)}}</span>
                                </template>
                                <el-descriptions :column="1" size="mini" border>
                                    <el-descriptions-item v-for="(param,index) in arrayParam" :key="index" :label="param.name">
                                        <div v-if="param.dataType.type=='bool'">
                                            <el-switch v-model="param.shadow" @change="mqttPublish(deviceInfo,param)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" :disabled="(shadowUnEnable || param.isReadonly==1)" />
                                        </div>
                                        <div v-if="param.dataType.type=='enum'">
                                            <el-select v-model="param.shadow" placeholder="请选择" @change="mqttPublish(deviceInfo,param)" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                                <el-option v-for="subItem in param.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                                            </el-select>
                                        </div>
                                        <div v-if="param.dataType.type=='string'">
                                            <el-input v-model="param.shadow" placeholder="请输入字符串" :disabled="(shadowUnEnable || param.isReadonly==1)">
                                                <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                            </el-input>
                                        </div>
                                        <div v-if="param.dataType.type=='decimal'">
                                            <el-input v-model="param.shadow" type="number" placeholder="请输入小数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                                <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                            </el-input>
                                        </div>
                                        <div v-if="param.dataType.type=='integer'">
                                            <el-input v-model="param.shadow" type="integer" placeholder="请输入整数 " :disabled="(shadowUnEnable || param.isReadonly==1)">
                                                <el-button slot="append" icon="el-icon-s-promotion" @click="mqttPublish(deviceInfo,param)" style="font-size:20px;" title="指令发送" v-if="(!shadowUnEnable && param.isReadonly==0)"></el-button>
                                            </el-input>
                                        </div>
                                    </el-descriptions-item>
                                </el-descriptions>
                            </el-collapse-item>
                        </el-collapse>
                    </div>
                </el-descriptions-item>
            </el-descriptions>

            <!---设备状态(影子模式，value值不会更新)-->
            <el-descriptions :column="1" border size="mini" v-if="deviceInfo.isShadow==1 && deviceInfo.status!=3">
                <template slot="title">
                    <span style="font-size:14px;color:#606266;">设备离线时状态</span>
                </template>

                <!-- 设备物模型-->
                <el-descriptions-item v-for="(item,index) in deviceInfo.thingsModels" :key="index">
                    <template slot="label">
                        <i class="el-icon-open"></i>
                        {{item.name}}
                    </template>
                    <div v-if="item.dataType.type=='bool'">
                        <el-switch v-model="item.value" @change="mqttPublish(deviceInfo,item)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" disabled />
                    </div>
                    <div v-if="item.dataType.type=='enum'">
                        <el-select v-model="item.value" placeholder="请选择" @change="mqttPublish(deviceInfo,item)" disabled size="mini">
                            <el-option v-for="subItem in item.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                        </el-select>
                    </div>
                    <div v-if="item.dataType.type=='string'">
                        <el-input v-model="item.value" placeholder="请输入字符串" disabled size="mini">
                        </el-input>
                    </div>
                    <div v-if="item.dataType.type=='decimal'">
                        <el-input v-model="item.value" type="number" placeholder="请输入小数 " disabled size="mini">
                        </el-input>
                    </div>
                    <div v-if="item.dataType.type=='integer'">
                        <el-input v-model="item.value" type="integer" placeholder="请输入整数 " disabled size="mini">
                        </el-input>
                    </div>
                    <div v-if="item.dataType.type=='object'">
                        <el-descriptions :column="1" size="mini" border>
                            <el-descriptions-item v-for="(param,index) in item.dataType.params" :key="index" :label="param.name">
                                <div v-if="param.dataType.type=='bool'">
                                    <el-switch v-model="param.value" size="mini" @change="mqttPublish(deviceInfo,param)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" disabled />
                                </div>
                                <div v-if="param.dataType.type=='enum'">
                                    <el-select v-model="param.value" placeholder="请选择" @change="mqttPublish(deviceInfo,param)" disabled size="mini">
                                        <el-option v-for="subItem in param.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                                    </el-select>
                                </div>
                                <div v-if="param.dataType.type=='string'">
                                    <el-input v-model="param.value" placeholder="请输入字符串" disabled size="mini">
                                    </el-input>
                                </div>
                                <div v-if="param.dataType.type=='decimal'">
                                    <el-input v-model="param.value" type="number" placeholder="请输入小数 " disabled size="mini">
                                    </el-input>
                                </div>
                                <div v-if="param.dataType.type=='integer'">
                                    <el-input v-model="param.value" type="integer" placeholder="请输入整数 " disabled size="mini">
                                    </el-input>
                                </div>
                            </el-descriptions-item>
                        </el-descriptions>
                    </div>
                    <div v-if="item.dataType.type=='array'">
                        <!-- <div style="margin-bottom:10px;">
                            <el-input v-model="item.shadow" placeholder="请输入英文逗号分隔的字符串" disabled v-if="item.dataType.arrayType!='object'">
                            </el-input>
                        </div> -->
                        <el-descriptions :column="1" size="mini" border v-if="item.dataType.arrayType!='object'">
                            <el-descriptions-item v-for="(model,index) in item.dataType.arrayModel" :key="index" :label="item.name+(index+1)">
                                <div v-if="item.dataType.arrayType=='string'">
                                    <el-input v-model="model.value" placeholder="请输入字符串" size="mini" disabled>
                                    </el-input>
                                </div>
                                <div v-if="item.dataType.arrayType=='decimal'">
                                    <el-input v-model="model.value" type="number" placeholder="请输入小数 " size="mini" disabled>
                                    </el-input>
                                </div>
                                <div v-if="item.dataType.arrayType=='integer'">
                                    <el-input v-model="model.value" type="integer" placeholder="请输入整数 " size="mini" disabled>
                                    </el-input>
                                </div>
                            </el-descriptions-item>
                        </el-descriptions>
                        <el-collapse v-if="item.dataType.arrayType=='object'">
                            <el-collapse-item v-for="(arrayParam,index) in item.dataType.arrayParams" :key="index">
                                <template slot="title">
                                    <span style="color:#666;"><i class="el-icon-tickets"></i> {{item.name+(index+1)}}</span>
                                </template>
                                <el-descriptions :column="1" size="mini" border>
                                    <el-descriptions-item v-for="(param,index) in arrayParam" :key="index" :label="param.name">
                                        <div v-if="param.dataType.type=='bool'">
                                            <el-switch v-model="param.value" @change="mqttPublish(deviceInfo,param)" active-text="" inactive-text="" active-value="1" inactive-value="0" style="min-width:100px;" disabled />
                                        </div>
                                        <div v-if="param.dataType.type=='enum'">
                                            <el-select v-model="param.value" placeholder="请选择" @change="mqttPublish(deviceInfo,param)" disabled size="mini">
                                                <el-option v-for="subItem in param.dataType.enumList" :key="subItem.value" :label="subItem.text" :value="subItem.value" />
                                            </el-select>
                                        </div>
                                        <div v-if="param.dataType.type=='string'">
                                            <el-input v-model="param.value" placeholder="请输入字符串" disabled size="mini">
                                            </el-input>
                                        </div>
                                        <div v-if="param.dataType.type=='decimal'">
                                            <el-input v-model="param.value" type="number" placeholder="请输入小数 " disabled size="mini">
                                            </el-input>
                                        </div>
                                        <div v-if="param.dataType.type=='integer'">
                                            <el-input v-model="param.value" type="integer" placeholder="请输入整数 " disabled size="mini">
                                            </el-input>
                                        </div>
                                    </el-descriptions-item>
                                </el-descriptions>
                            </el-collapse-item>
                        </el-collapse>
                    </div>
                </el-descriptions-item>
            </el-descriptions>
        </el-col>

        <!-- 图表-->
        <el-col :xs="24" :sm="24" :md="24" :lg="10" :xl="14" v-if="deviceInfo.monitorList.length > 0">
            <el-row :gutter="20" style="background-color:#F5F7FA;padding:20px 10px 20px 10px;border-radius:15px;margin-right:5px;">
                <el-col :xs="24" :sm="12" :md="12" :lg="24" :xl="8" v-for="(item,index) in deviceInfo.monitorList" :key="index">
                    <el-card shadow="hover" style="border-radius:30px;margin-bottom:20px;">
                        <div ref="map" style="height:230px;width:185px;margin:0 auto;"></div>
                    </el-card>
                </el-col>
            </el-row>
        </el-col>
    </el-row>

    <!-- 添加或修改产品固件对话框 -->
    <el-dialog title="设备固件升级" :visible.sync="openFirmware" width="600px" append-to-body>
        <div v-if="firmware==null || deviceInfo.firmwareVersion>=firmware.version" style="text-align:center;font-size:16px;"><i class="el-icon-success" style="color:#67C23A;"></i> 已经是最新版本，不需要升级</div>
        <el-descriptions :column="1" border size="large" v-if="firmware!=null && deviceInfo.firmwareVersion<firmware.version" :labelStyle='{"width":"100px","font-weight":"bold"}'>
            <template slot="title">
                <el-link icon="el-icon-success" type="success" :underline="false"> 可以升级到以下版本</el-link>
            </template>
            <el-descriptions-item label="固件名称">{{firmware.firmwareName}}</el-descriptions-item>
            <el-descriptions-item label="所属产品">{{firmware.productName}}</el-descriptions-item>
            <el-descriptions-item label="固件版本">Version {{firmware.version}}</el-descriptions-item>
            <el-descriptions-item label="下载地址">
                <el-link :href="getDownloadUrl(firmware.filePath)" :underline="false" type="primary">{{getDownloadUrl(firmware.filePath)}}</el-link>
            </el-descriptions-item>
            <el-descriptions-item label="固件描述">{{firmware.remark}}</el-descriptions-item>
        </el-descriptions>
        <div slot="footer" class="dialog-footer">
            <el-button type="success" @click="otaUpgrade" v-if="firmware!=null && deviceInfo.firmwareVersion<firmware.version">升 级</el-button>
            <el-button @click="cancel">取 消</el-button>
        </div>
    </el-dialog>
</div>
</template>

<script>
import {
    getLatestFirmware,
} from "@/api/iot/firmware";

export default {
    name: "running-status",
    components: {},
    props: {
        device: {
            type: Object,
            default: null
        }
    },
    watch: {
        // 获取到父组件传递的device后，刷新列表
        device: function (newVal, oldVal) {
            if (newVal && newVal.deviceId != 0) {
                this.deviceInfo = newVal;
                this.updateDeviceStatus(this.deviceInfo);
                this.$nextTick(function () {
                    this.MonitorChart();
                });
                this.mqttCallback();
            }
        }
    },
    data() {
        return {
            // 控制模块标题
            title: "设备控制 ",
            // 未启用设备影子
            shadowUnEnable: false,
            // 控制项标题背景
            statusColor: {
                background: '#67C23A',
                color: '#fff',
            },
            // 最新固件信息
            firmware: {},
            // 打开固件对话框
            openFirmware: false,
            // 遮罩层
            loading: true,
            // 设备信息
            deviceInfo: {
                boolList: [],
                enumList: [],
                stringList: [],
                integerList: [],
                decimalList: [],
                arrayList: [],
                thingsModels: [],
                monitorList: []
            },
            // 监测图表
            monitorChart: [{
                chart: {},
                data: {
                    id: '',
                    name: '',
                    value: ''
                },
            }],
        }
    },
    created() {
    },
    methods: {
        /* Mqtt回调处理 */
        mqttCallback() {
            this.$mqttTool.client.on('message', (topic, message, buffer) => {
                let topics = topic.split('/');
                let productId = topics[1];
                let deviceNum = topics[2];
                message = JSON.parse(message.toString());
                if(!message){
                  return;
                }
                if (topics[3] == 'status') {
                    console.log('接收到【设备状态-运行】主题：', topic);
                    console.log('接收到【设备状态-运行】内容：', message);
                    // 更新列表中设备的状态
                    if (this.deviceInfo.serialNumber == deviceNum) {
                        this.deviceInfo.status = message.status;
                        this.deviceInfo.isShadow = message.isShadow;
                        this.deviceInfo.rssi = message.rssi;
                        this.updateDeviceStatus(this.deviceInfo);
                    }
                }
                if (topics[3] == 'property' || topics[3] == 'function') {
                    console.log('接收到【物模型】主题：', topic);
                    console.log('接收到【物模型】内容：', message);
                    // 更新列表中设备的属性
                    if (this.deviceInfo.serialNumber == deviceNum) {
                        for (let j = 0; j < message.length; j++) {
                            let isComplete = false;
                            // 设备状态
                            for (let k = 0; k < this.deviceInfo.thingsModels.length && !isComplete; k++) {
                                if (this.deviceInfo.thingsModels[k].id == message[j].id) {
                                    // 普通类型
                                    this.deviceInfo.thingsModels[k].shadow = message[j].value;
                                    isComplete = true;
                                    break;
                                } else if (this.deviceInfo.thingsModels[k].dataType.type == "object") {
                                    // 对象类型
                                    for (let n = 0; n < this.deviceInfo.thingsModels[k].dataType.params.length; n++) {
                                        if (this.deviceInfo.thingsModels[k].dataType.params[n].id == message[j].id) {
                                            this.deviceInfo.thingsModels[k].dataType.params[n].shadow = message[j].value;
                                            isComplete = true;
                                            break;
                                        }
                                    }
                                } else if (this.deviceInfo.thingsModels[k].dataType.type == "array") {
                                    // 数组类型
                                    if (this.deviceInfo.thingsModels[k].dataType.arrayType == "object") {
                                        // 1.对象类型数组,id为数组中一个元素,例如：array_01_gateway_temperature
                                        if (String(message[j].id).indexOf("array_") == 0) {
                                            for (let n = 0; n < this.deviceInfo.thingsModels[k].dataType.arrayParams.length; n++) {
                                                for (let m = 0; m < this.deviceInfo.thingsModels[k].dataType.arrayParams[n].length; m++) {
                                                    if (this.deviceInfo.thingsModels[k].dataType.arrayParams[n][m].id == message[j].id) {
                                                        this.deviceInfo.thingsModels[k].dataType.arrayParams[n][m].shadow = message[j].value;
                                                        isComplete = true;
                                                        break;
                                                    }
                                                }
                                                if (isComplete) {
                                                    break;
                                                }
                                            }
                                        } else {
                                            // 2.对象类型数组，例如：gateway_temperature,消息ID添加前缀后匹配
                                            for (let n = 0; n < this.deviceInfo.thingsModels[k].dataType.arrayParams.length; n++) {
                                                for (let m = 0; m < this.deviceInfo.thingsModels[k].dataType.arrayParams[n].length; m++) {
                                                    let index = n > 9 ? String(n) : '0' + k;
                                                    let prefix = 'array_' + index + '_';
                                                    if (this.deviceInfo.thingsModels[k].dataType.arrayParams[n][m].id == prefix + message[j].id) {
                                                        this.deviceInfo.thingsModels[k].dataType.arrayParams[n][m].shadow = message[j].value;
                                                        isComplete = true;
                                                    }
                                                }
                                                if (isComplete) {
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        // 整数、小数和字符串类型数组
                                        for (let n = 0; n < this.deviceInfo.thingsModels[k].dataType.arrayModel.length; n++) {
                                            if (this.deviceInfo.thingsModels[k].dataType.arrayModel[n].id == message[j].id) {
                                                this.deviceInfo.thingsModels[k].dataType.arrayModel[n].shadow = message[j].value;
                                                isComplete = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            // 监测数据
                            for (let k = 0; k < this.deviceInfo.monitorList.length; k++) {
                                if (this.deviceInfo.monitorList[k].id.indexOf("array_") == 0) {
                                    // 数组类型匹配,例如：array_00_gateway_temperature
                                    if (this.deviceInfo.monitorList[k].id == message[j].id) {
                                        // let shadows = message[j].value.split(",");
                                        this.deviceInfo.monitorList[k].shadow = message[j].value;
                                        // 更新图表
                                        for (let m = 0; m < this.monitorChart.length; m++) {
                                            if (message[j].id == this.monitorChart[m].data.id) {
                                                let data = [{
                                                    value: message[j].value,
                                                    name: this.monitorChart[m].data.name
                                                }];
                                                this.monitorChart[m].chart.setOption({
                                                    series: [{
                                                        data: data
                                                    }]
                                                });
                                                break;
                                            }
                                        }
                                    }

                                } else {
                                    // 普通类型匹配
                                    if (this.deviceInfo.monitorList[k].id == message[j].id) {
                                        this.deviceInfo.monitorList[k].shadow = message[j].value;
                                        // 更新图表
                                        for (let m = 0; m < this.monitorChart.length; m++) {
                                            if (message[j].id == this.monitorChart[m].data.id) {
                                                isComplete = true;
                                                let data = [{
                                                    value: message[j].value,
                                                    name: this.monitorChart[m].data.name
                                                }];
                                                this.monitorChart[m].chart.setOption({
                                                    series: [{
                                                        data: data
                                                    }]
                                                });
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (isComplete) {
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        },

        /**
         * Mqtt发布消息
         * @device 设备
         * @model 物模型(id/name/type/name/isReadonly/value/shadow)，type 类型(1=属性，2=功能，3=OTA升级，4=实时监测)
         * */
        mqttPublish(device, model) {
            let topic = "";
            let message = ""
            if (model.type == 1) {
                if (device.status == 3) {
                    // 属性,在线模式
                    topic = "/" + device.productId + "/" + device.serialNumber + "/property-online/get";
                } else if (device.isShadow) {
                    // 属性,离线模式
                    topic = "/" + device.productId + "/" + device.serialNumber + "/property-offline/post";
                }
                message = '[{"id":"' + model.id + '","value":"' + model.shadow + '"}]';
            } else if (model.type == 2) {
                if (device.status == 3) {
                    // 功能,在线模式
                    topic = "/" + device.productId + "/" + device.serialNumber + "/function-online/get";

                } else if (device.isShadow) {
                    // 功能,离线模式
                    topic = "/" + device.productId + "/" + device.serialNumber + "/function-offline/post";
                }
                message = '[{"id":"' + model.id + '","value":"' + model.shadow + '"}]';
            } else if (model.type == 3) {
                // OTA升级
                topic = "/" + device.productId + "/" + device.serialNumber + "/ota/get";
                message = '{"version":' + this.firmware.version + ',"downloadUrl":"' + this.getDownloadUrl(this.firmware.filePath) + '"}';
            } else {
                return;
            }
            if (topic != "") {
                // 发布
                this.$mqttTool.publish(topic, message, model.name).then(res => {
                    this.$modal.notifySuccess(res);
                }).catch(res => {
                    this.$modal.notifyError(res);
                });
            }
        },

        /** 更新设备状态 */
        updateDeviceStatus(device) {
            if (device.status == 3) {
                this.statusColor.background = '#12d09f';
                this.title = "在线模式";
            } else {
                if (device.isShadow == 1) {
                    this.statusColor.background = '#409EFF';
                    this.title = "影子模式";
                } else {
                    this.statusColor.background = '#909399';
                    this.title = "离线模式";
                    this.shadowUnEnable = true;
                }
            }
            this.$emit('statusEvent', this.deviceInfo.status);
        },
        /** 物模型数组元素值改变事件 */
        arrayItemChange(value, thingsModel) {
            let shadow = "";
            for (let i = 0; i < thingsModel.dataType.arrayCount; i++) {
                shadow += thingsModel.dataType.arrayModel[i].shadow + ",";
            }
            shadow = shadow.substring(0, shadow.length - 1);
            thingsModel.shadow = shadow;
        },
        /** 物模型中数组值改变事件 */
        arrayInputChange(value, thingsModel) {
            let arrayModels = value.split(",");
            if (arrayModels.length != thingsModel.dataType.arrayCount) {
                this.$modal.alertWarning("元素个数不匹配，数组元素个数为" + thingsModel.dataType.arrayCount + "个，以英文逗号分隔。");
            } else {
                for (let i = 0; i < thingsModel.dataType.arrayCount; i++) {
                    thingsModel.dataType.arrayModel[i].shadow = arrayModels[i];
                }
            }
        },
        /** 设备升级 */
        otaUpgrade() {
            let model = {};
            model.name = "设备升级"
            model.type = 3;
            this.mqttPublish(this.deviceInfo, model);
            this.openFirmware = false;
        },
        /** 获取最新固件 */
        getLatestFirmware(deviceId) {
            getLatestFirmware(deviceId).then(response => {
                this.firmware = response.data;
                this.openFirmware = true;
            });
        },
        // 取消按钮
        cancel() {
            this.openFirmware = false;
        },
        // 获取下载路径前缀
        getDownloadUrl(path) {
            return window.location.origin + process.env.VUE_APP_BASE_API + path;
        },
        /**监测图表统计*/
        MonitorChart() {
            for (let i = 0; i < this.deviceInfo.monitorList.length; i++) {
                this.monitorChart[i] = {
                    chart: this.$echarts.init(this.$refs.map[i]),
                    data: {
                        id: this.deviceInfo.monitorList[i].id,
                        name: this.deviceInfo.monitorList[i].name,
                        value: this.deviceInfo.monitorList[i].shadow ? this.deviceInfo.monitorList[i].shadow : this.deviceInfo.monitorList[i].dataType.min
                    }
                };
                var option;
                option = {
                    tooltip: {
                        formatter: ' {b} <br/> {c}' + this.deviceInfo.monitorList[i].dataType.unit
                    },
                    series: [{
                        name: this.deviceInfo.monitorList[i].dataType.type,
                        type: 'gauge',
                        min: this.deviceInfo.monitorList[i].dataType.min,
                        max: this.deviceInfo.monitorList[i].dataType.max,
                        colorBy: 'data',
                        splitNumber: 10,
                        radius: '100%',
                        // 分割线
                        splitLine: {
                            distance: 4,
                        },
                        axisLabel: {
                            fontSize: 10,
                            distance: 10
                        },
                        // 刻度线
                        axisTick: {
                            distance: 4,
                        },
                        // 仪表盘轴线
                        axisLine: {
                            lineStyle: {
                                width: 8,
                                color: [
                                    [0.2, '#409EFF'], // 0~20%
                                    [0.8, '#12d09f'], // 40~60%
                                    [1, '#F56C6C'], // 80~100%
                                ],
                                opacity: 0.3
                            }

                        },
                        pointer: {
                            icon: 'triangle',
                            length: '60%',
                            width: 7
                        },
                        progress: {
                            show: true,
                            width: 8,
                        },
                        detail: {
                            valueAnimation: true,
                            formatter: '{value}' + ' ' + this.deviceInfo.monitorList[i].dataType.unit,
                            offsetCenter: [0, "80%"],
                            fontSize: 20,
                        },
                        data: [{
                            value: this.deviceInfo.monitorList[i].shadow ? this.deviceInfo.monitorList[i].shadow : this.deviceInfo.monitorList[i].dataType.min,
                            name: this.deviceInfo.monitorList[i].name
                        }],
                        title: {
                            offsetCenter: [0, "115%"],
                            fontSize: 16
                        }
                    }]
                };
                option && this.monitorChart[i].chart.setOption(option);
            }
        }
    },
}
</script>
