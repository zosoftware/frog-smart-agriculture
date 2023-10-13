<template>
<div style="padding:6px;">
    <el-card style="margin-bottom:6px;">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="75px" style="margin-bottom:-20px;">
            <el-form-item label="设备名称" prop="deviceName">
                <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable size="small" @keyup.enter.native="handleQuery" style="width:150px;" />
            </el-form-item>
            <el-form-item label="设备编号" prop="serialNumber">
                <el-input v-model="queryParams.serialNumber" placeholder="请输入设备编号" clearable size="small" @keyup.enter.native="handleQuery" style="width:150px;" />
            </el-form-item>
            <el-form-item label="设备状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="请选择设备状态" clearable size="small" style="width:150px;">
                    <el-option v-for="dict in dict.type.iot_device_status" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="设备类型">
                <el-select v-model="queryParams.deviceType" placeholder="请选择设备类型" clearable size="small" style="width:150px;" >
                    <el-option v-for="dict in dict.type.iot_device_type" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"></el-option>
                </el-select>
            </el-form-item>
            <!-- <el-form-item label="我的分组">
                <el-select v-model="queryParams.groupId" placeholder="请选择我的分组" clearable size="small" style="width:150px;" >
                    <el-option v-for="group in myGroupList" :key="group.groupId" :label="group.groupName" :value="group.groupId" />
                </el-select>
            </el-form-item> -->
            <el-form-item label="分配地块" >
                <el-select v-model="queryParams.landId" size="small" placeholder="请选择地块" style="width:150px;">
                    <el-option
                    v-for="land in landList"
                    :key="land.landId"
                    :label="land.landName"
                    :value="land.landId"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
            <el-form-item style="float:right;">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddDevice" v-hasPermi="['iot:device:add']">新增</el-button>
            </el-form-item>
        </el-form>
    </el-card>

    <el-card style="padding-bottom:100px;">
        <el-row :gutter="30" v-loading="loading">
            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="6" v-for="(item,index) in deviceList" :key="index" style="margin-bottom:30px;text-align:center;">
                <el-card :body-style="{ padding: '20px'}" shadow="always" class="card-item">
                    <el-row type="flex" :gutter="10" justify="space-between">
                        <el-col :span="20" style="text-align:left;">
                            <el-link type="" :underline="false" @click="handleEditDevice(item)" style="font-weight:bold;font-size:16px;line-height:32px;">
                                <el-tooltip class="item" effect="dark" content="分享的设备" placement="top-start">
                                    <svg-icon icon-class="share" style="font-size:20px;" v-if="item.isOwner!=1" />
                                </el-tooltip>
                                <svg-icon icon-class="device" v-if="item.isOwner==1" />
                                <span style="margin:0 5px;">{{item.deviceName}}</span>
                                <el-tag size="mini" type="info">Ver {{item.firmwareVersion}}</el-tag>
                            </el-link>
                        </el-col>
                        <el-col :span="4">
                            <div style="font-size:28px;color:#ccc;">
                                <svg-icon v-if="item.status==3 && item.rssi >= '-55'" icon-class="wifi_4" />
                                <svg-icon v-else-if="item.status==3 && item.rssi >= '-70' && item.rssi < '-55' " icon-class="wifi_3" />
                                <svg-icon v-else-if="item.status==3 && item.rssi >= '-85' && item.rssi < '-70' " icon-class="wifi_2" />
                                <svg-icon v-else-if="item.status==3 && item.rssi >= '-100' && item.rssi < '-85' " icon-class="wifi_1" />
                                <svg-icon v-else icon-class="wifi_0" />
                            </div>
                        </el-col>
                    </el-row>
                    <el-row :gutter="10">
                        <el-col :span="15">
                            <div style="text-align:left;line-height:40px;white-space:nowrap;">
                                <dict-tag :options="dict.type.iot_device_status" :value="item.status" size="small" style="display:inline-block;" />
                                <span style="display:inline-block;margin:0 10px;">
                                    <el-tag type="success" size="small" v-if="item.isShadow==1">影子</el-tag>
                                    <el-tag type="info" size="small" v-else>影子</el-tag>
                                </span>
                                <dict-tag :options="dict.type.iot_location_way" :value="item.locationWay" size="small" style="display:inline-block;" />
                            </div>
                            <el-descriptions :column="1" size="mini" style="white-space:nowrap;">
                                <el-descriptions-item label="编号">
                                    {{item.serialNumber}}
                                </el-descriptions-item>
                                <el-descriptions-item label="产品">
                                    {{item.productName}}
                                </el-descriptions-item>
                                <el-descriptions-item label="激活时间">
                                    {{ parseTime(item.activeTime, '{y}-{m}-{d}') }}
                                </el-descriptions-item>
                            </el-descriptions>
                        </el-col>
                        <el-col :span="9">
                            <div style="margin-top:10px;">
                                <el-image style="height:100px;border-radius:10px;" lazy :preview-src-list="[baseUrl+item.imgUrl.split(',')[0]]" :src="baseUrl+item.imgUrl.split(',')[0]" fit="cover" v-if="item.imgUrl!=null && item.imgUrl!=''"></el-image>

                            </div>
                        </el-col>
                    </el-row>
                    <el-button-group style="margin-top:15px;">
                        <el-button type="danger" size="mini" style="padding:5px 10px;" icon="el-icon-delete" @click="handleDelete(item)" v-hasPermi="['iot:device:remove']">删除 </el-button>
                        <el-button type="primary" size="mini" style="padding:5px 15px;" icon="el-icon-view" @click="handleEdit(item)" v-hasPermi="['iot:device:edit']">编辑 </el-button>
                        <el-button type="success" size="mini" style="padding:5px 15px;" icon="el-icon-odometer" @click="handDeviceView(item)" v-hasPermi="['iot:device:view']">运行状态 </el-button>
                    </el-button-group>
                </el-card>
            </el-col>
        </el-row>

        <el-empty description="暂无数据，请添加设备" v-if="total==0"></el-empty>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" :pageSizes="[12, 24, 36, 60]" @pagination="getList" />

    </el-card>

    <el-dialog
        :title="title"
        :visible.sync="open"
        width="60%"
        @close="open=false" >
        <device-form  v-if="open" :deviceId="deviceId" @success="handleSuccess" @close="open=false"></device-form>
    </el-dialog>

</div>
</template>

<script>
import { checkPermi} from "@/utils/permission"; // 权限判断函数
import {
    listDeviceShort,
    delDevice,
} from "@/api/iot/device";
import {
    listGroup
} from "@/api/iot/group";
import {
    delSipDeviceBySipId
} from "@/api/iot/sipdevice";
import { listLand } from '@/api/agriculture/land';

import DeviceForm from "./device-form.vue"

export default {
    name: "Device",
    dicts: ['iot_device_status', 'iot_is_enable', 'iot_location_way','iot_device_type'],
    components: {DeviceForm},
    data() {
        return {
            // 遮罩层
            loading: true,
            // 总条数
            total: 0,
            // 设备列表数据
            deviceList: [],
            // 我的分组列表数据
            myGroupList: [],
            // 根路径
            baseUrl: process.env.VUE_APP_BASE_API,
            //弹窗
            open:false,
            //弹窗标题
            title:'',
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 12,
                deviceName: null,
                productId: null,
                landId:null,
                groupId: null,
                productName: null,
                serialNumber: null,
                status: null,
                deviceType:null
            },
            deviceId:0,
            landList:[]
        };
    },
    created() {
        this.getLandList();
        // 产品筛选
        let productId = this.$route.query.productId
        if (productId != null) {
            this.queryParams.productId = Number(productId);
            this.queryParams.groupId = null;
            this.queryParams.serialNumber = null;
        }
        // 分组筛选
        let groupId = this.$route.query.groupId
        if (groupId != null) {
            this.queryParams.groupId = Number(groupId);
            this.queryParams.productId = null;
            this.queryParams.serialNumber = null;
        }
        // 设备编号筛选
        let sn = this.$route.query.sn
        if (sn != null) {
            this.queryParams.serialNumber = sn;
            this.queryParams.productId = null;
            this.queryParams.groupId = null;
        }
        this.connectMqtt();
    },
    activated() {
        const time = this.$route.query.t;
        if (time != null && time != this.uniqueId) {
            this.uniqueId = time;
            // 页码筛选
            let pageNum = this.$route.query.pageNum;
            if (pageNum != null) {
                this.queryParams.pageNum = Number(pageNum);
            }
            // 产品筛选
            let productId = this.$route.query.productId
            if (productId != null) {
                this.queryParams.productId = Number(productId);
                this.queryParams.groupId = null;
                this.queryParams.serialNumber = null;
            }
            // 分组筛选
            let groupId = this.$route.query.groupId
            if (groupId != null) {
                this.queryParams.groupId = Number(groupId);
                this.queryParams.productId = null;
                this.queryParams.serialNumber = null;
            }
            // 设备编号筛选
            let sn = this.$route.query.sn
            if (sn != null) {
                this.queryParams.serialNumber = sn;
                this.queryParams.productId = null;
                this.queryParams.groupId = null;
            }
            this.getList();
        }
    },
    methods: {
        checkPermi,
        /* 连接Mqtt消息服务器 */
        async connectMqtt() {
            if (this.$mqttTool.client == null) {
                await this.$mqttTool.connect();
            }
            this.mqttCallback();
            this.getList();
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
                    console.log('接收到【设备状态】主题：', topic);
                    console.log('接收到【设备状态】内容：', message);
                    // 更新列表中设备的状态
                    for (let i = 0; i < this.deviceList.length; i++) {
                        if (this.deviceList[i].serialNumber == deviceNum) {
                            this.deviceList[i].status = message.status;
                            this.deviceList[i].isShadow = message.isShadow;
                            this.deviceList[i].rssi = message.rssi;
                            return;
                        }
                    }
                }
            });
        },
        /* 订阅消息 */
        mqttSubscribe(list) {
            // 订阅当前页面设备状态和实时监测
            let topics = [];
            for (let i = 0; i < list.length; i++) {
                let topicStatus = '/' + list[i].productId + '/' + list[i].serialNumber + '/status/post';
                topics.push(topicStatus);
            }
            this.$mqttTool.subscribe(topics);
        },
        /** 查询设备分组列表 */
        getGroupList() {
            this.loading = true;
            let queryParams = {
                pageSize: 30,
                pageNum: 1,
                userId: this.$store.state.user.userId
            }
            listGroup(queryParams).then(response => {
                this.myGroupList = response.rows;
            });
        },
        /** 查询所有简短设备列表 */
        getList() {
            this.loading = true;
            this.queryParams.params = {};
            this.getGroupList();
            listDeviceShort(this.queryParams).then(response => {
                this.deviceList = response.rows;
                this.total = response.total;
                // 订阅消息
                if (this.deviceList && this.deviceList.length > 0) {
                    this.mqttSubscribe(this.deviceList);
                }
                this.loading = false;
            });
        },
         /** 查询地块列表 */
        getLandList() {
            listLand().then((response) => {
                this.landList = response.rows;
            });
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.queryParams.productId = null;
            this.queryParams.deviceType = null;
            this.queryParams.serialNumber = null;
            this.queryParams.landId = null;
            this.resetForm("queryForm");
            this.handleQuery();
        },
        /** 新增按钮处理 */
        handleAddDevice(){
            this.deviceId = 0;
            this.title="新增设备"
            this.open = true;
        },
        /** 修改按钮操作 */
        handleEdit(item){
            this.title="编辑设备"
            this.open=true;
            this.deviceId = item.deviceId;
        },
        /** 修改按钮操作 */
        handleEditDevice(row, activeName) {
            let deviceId = 0;
            if (row != 0) {
                deviceId = row.deviceId || this.ids
            }
            this.$router.push({
                path: '/iot/device-edit',
                query: {
                    deviceId: deviceId,
                    pageNum: this.queryParams.pageNum,
                    activeName: activeName
                }
            });
        },
        /** 处理设备查看 */
        handDeviceView(item){
            let productId = item.productId;
            switch (productId) {
                case 98:
                    this.$router.push({
                        path: '/iot/camera/singlevideo',
                        query: {
                            t: Date.now(),
                            deviceId: item.deviceId,
                        }
                    });
                    break;
                default:
                    this.$router.push({
                        path: '/iot/device-view',
                        query: {
                            t: Date.now(),
                            deviceId: item.deviceId,
                            productId:item.productId
                        }
                    });
                    break;
            }

        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const deviceIds = row.deviceId || this.ids;
            this.$modal.confirm('是否确认删除设备编号为"' + deviceIds + '"的数据项？').then(function () {
                if (row.deviceType === 3) {
                    delSipDeviceBySipId(row.serialNumber);
                }
                return delDevice(deviceIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => {});
        },
        /** 未启用设备影子*/
        shadowUnEnable(device, thingsModel) {
            // 1-未激活，2-禁用，3-在线，4-离线
            if (device.status != 3 && device.isShadow == 0) {
                return true;
            }
            if (thingsModel.isReadonly) {
                return true;
            }
            return false;
        },
        handleSuccess(){
            this.open= false;
            this.getList();
        }
    }
};
</script>

<style scoped>
.card-item {
    border-radius: 15px;
}
</style>
