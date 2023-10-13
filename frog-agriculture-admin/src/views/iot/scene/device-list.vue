<template>
<el-dialog title="选择设备" :visible.sync="openDeviceList" width="800px" append-to-body>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="设备名称" prop="deviceName">
            <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="设备编号" prop="serialNumber">
            <el-input v-model="queryParams.serialNumber" placeholder="请输入设备编号" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="deviceList" @row-click="rowClick" highlight-current-row size="mini">
        <el-table-column label="选择" width="50" align="center">
            <template slot-scope="scope">
                <input type="radio" :checked="scope.row.isSelect" name="product" />
            </template>
        </el-table-column>
        <el-table-column label="设备名称" align="center" prop="deviceName" />
        <el-table-column label="设备编号" align="center" prop="serialNumber" />
        <el-table-column label="产品名称" align="center" prop="productName" />
        <el-table-column label="设备类型" align="center" width="100">
            <template slot-scope="scope">
                <el-tag type="success" v-if="scope.row.isOwner==0">分享</el-tag>
                <el-tag type="primary" v-else>拥有</el-tag>
            </template>
        </el-table-column>
        <el-table-column label="定位方式" align="center" prop="locationWay" width="100">
            <template slot-scope="scope">
                <dict-tag :options="dict.type.iot_location_way" :value="scope.row.locationWay" />
            </template>
        </el-table-column>
        <el-table-column label="设备状态" align="center" prop="status" width="100">
            <template slot-scope="scope">
                <dict-tag :options="dict.type.iot_device_status" :value="scope.row.status" />
            </template>
        </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmSelectDevice">确 定</el-button>
        <el-button @click="closeSelectDeviceList">取 消</el-button>
    </div>
</el-dialog>
</template>

<script>
import {
    listDeviceShort,
} from "@/api/iot/device";
export default {
    name: "device-list",
    dicts: ['iot_device_status', 'iot_location_way'],
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 是否显示设备列表
            openDeviceList: false,
            // 总条数
            total: 0,
            // 设备表格数据
            deviceList: [],
            // 选中的设备
            selectDevice:{},
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                deviceName: null,
                productId: null,
                groupId: null,
                groupId: null,
                productName: null,
                userId: null,
                userName: null,
                tenantId: null,
                tenantName: null,
                serialNumber: null,
                status: null,
                networkAddress: null,
                activeTime: null,
            },
        };
    },
    created() {

    },
    methods: {
        // 获取设备列表
        getList(deviceId) {
            this.deviceList=[];
            this.total=0;
            this.loading = true;
            listDeviceShort(this.queryParams).then(response => {
                this.deviceList = response.rows;
                this.total = response.total;
                if (deviceId != 0) {
                    this.setRadioSelected(deviceId);
                }
                this.loading = false;
            });
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
        /** 单选数据 */
        rowClick(device) {
            if (device != null) {
                this.setRadioSelected(device.deviceId);
                this.selectDevice = device;
            }
        },
        /** 设置单选按钮选中 */
        setRadioSelected(deviceId) {
            for (let i = 0; i < this.deviceList.length; i++) {
                if (this.deviceList[i].deviceId == deviceId) {
                    this.deviceList[i].isSelect = true;
                } else {
                    this.deviceList[i].isSelect = false;
                }
            }
        },
        // 关闭选择设备列表
        closeSelectDeviceList() {
            this.openDeviceList = false;
        },
        /**确定选择设备，设备传递给父组件 */
        confirmSelectDevice() {
            this.$emit('deviceEvent', this.selectDevice);
            this.openDeviceList = false;
        },
    }
};
</script>
