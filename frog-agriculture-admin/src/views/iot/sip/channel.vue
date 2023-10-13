<template>
<div style="padding-left:20px;">
    <el-row :gutter="10" class="mb8">
        <!-- <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-share" size="mini" v-hasPermi="['iot:device:share']">新增</el-button>
        </el-col> -->
        <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-refresh" size="mini" @click="getList">刷新</el-button>
        </el-col>
    </el-row>
    <el-table v-loading="loading" :data="channelList" size="mini">
        <el-table-column label="设备ID" align="center" prop="deviceSipId" />
        <el-table-column label="通道ID" align="center" prop="channelSipId" />
        <el-table-column label="通道名称" align="center" prop="channelName" />
        <el-table-column label="产品型号" align="center" prop="model" />
        <el-table-column label="状态" align="center" prop="status" width="80">
            <template slot-scope="scope">
                <dict-tag :options="dict.type.sip_gen_status" :value="scope.row.status" size="mini" />
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width">
            <template slot-scope="scope">
                <el-button size="small" type="success" icon="el-icon-video-play" style="padding: 5px" :disabled="scope.row.status!=2" @click="sendDevicePush(scope.row)"> 查看直播</el-button>
            </template>
        </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 设备播放器 -->
    <devicePlayer ref="devicePlayer"></devicePlayer>
</div>
</template>

<script>
import {
    listChannel,
    getChannel,
    delChannel,
    addChannel,
    updateChannel,
    startPlay,
    stopPlay,
    getStreaminfo,
} from "@/api/iot/channel";
import devicePlayer from "@/views/components/player/devicePlayer";
export default {
    name: "Channel",
    dicts: ['sip_gen_status', 'video_type', 'channel_type'],
    components: {
        devicePlayer,
    },
    props: {
        device: {
            type: Object,
            default: null
        }
    },
    watch: {
        // 获取到父组件传递的device后
        device: function (newVal, oldVal) {
          this.deviceInfo = newVal;
          if (this.deviceInfo && this.deviceInfo.deviceId != 0) {
            this.queryParams.deviceSipId = this.deviceInfo.serialNumber;
                this.getList();
            }
        }
    },
    data() {
        return {
            // 设备信息
            deviceInfo: {},
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
            // 监控设备通道信息表格数据
            channelList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                deviceSipId: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                channelSipId: [{
                    required: true,
                    message: "通道SipID不能为空",
                    trigger: "blur"
                }, ],
                channelName: [{
                    required: true,
                    message: "通道名称不能为空",
                    trigger: "blur"
                }, ],
                manufacture: [{
                    required: true,
                    message: "厂商名称不能为空",
                    trigger: "blur"
                }, ],
                model: [{
                    required: true,
                    message: "产品型号不能为空",
                    trigger: "blur"
                }, ],
                owner: [{
                    required: true,
                    message: "设备归属不能为空",
                    trigger: "blur"
                }, ],
                civilcode: [{
                    required: true,
                    message: "行政区域不能为空",
                    trigger: "blur"
                }, ],
                block: [{
                    required: true,
                    message: "警区不能为空",
                    trigger: "blur"
                }],
                address: [{
                    required: true,
                    message: "安装地址不能为空",
                    trigger: "blur"
                }, ],
                parentid: [{
                    required: true,
                    message: "父级id不能为空",
                    trigger: "blur"
                }, ],
                password: [{
                    required: true,
                    message: "密码不能为空",
                    trigger: "blur"
                }, ],
                ptztype: [{
                    required: true,
                    message: "PTZ类型不能为空",
                    trigger: "change"
                }, ],
                ptztypetext: [{
                    required: true,
                    message: "PTZ类型描述字符串不能为空",
                    trigger: "blur",
                }, ],
                status: [{
                    required: true,
                    message: "设备状态不能为空",
                    trigger: "blur"
                }, ],
                streamid: [{
                    required: true,
                    message: "流媒体ID不能为空",
                    trigger: "blur"
                }, ],
                subcount: [{
                    required: true,
                    message: "子设备数不能为空",
                    trigger: "blur"
                }, ]
            },
        };
    },
    created() {
        this.queryParams.deviceSipId = this.device.serialNumber;
        this.getList();
    },
    methods: {
        //通知设备上传媒体流
        sendDevicePush: function (itemData) {
            let deviceId = itemData.deviceSipId;
            this.isLoging = true;
            let channelId = itemData.channelSipId;
            console.log("通知设备推流1：" + deviceId + " : " + channelId);
            let that = this;

            startPlay(deviceId, channelId).then((response) => {
                console.log("开始播放：" + deviceId + " : " + channelId);
                console.log("流媒体信息：" + response.data);
                let res = response.data;
                console.log("playurl：" + res.playurl);
                that.$refs.devicePlayer.openDialog("media", deviceId, channelId, {
                    streamInfo: res,
                    hasAudio: itemData.hasaudio,
                });
            });
        },
        stopDevicePush: function (itemData) {
            stopPlay(itemData.streamid).then((response) => {
                console.log("停止推流：" + itemData.streamId);
            });
        },

        /** 查询监控设备通道信息列表 */
        getList() {
            this.loading = true;
            listChannel(this.queryParams).then((response) => {
                this.channelList = response.rows;
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
                channelId: null,
                channelSipId: null,
                deviceSipId: null,
                channelName: null,
                manufacture: null,
                model: null,
                owner: null,
                civilcode: null,
                block: null,
                address: null,
                parentid: null,
                ipaddress: null,
                port: null,
                password: null,
                ptztype: null,
                ptztypetext: null,
                status: 0,
                longitude: null,
                latitude: null,
                streamid: null,
                subcount: null,
                parental: 1,
                hasaudio: 1,
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const channelId = row.channelId || this.ids;
            getChannel(channelId).then((response) => {
                this.form = response.data;
                this.open = true;
                this.title = "修改监控设备通道信息";
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const channelIds = row.channelId || this.ids;
            this.$modal
                .confirm(
                    '是否确认删除监控设备通道信息编号为"' + channelIds + '"的数据项？'
                )
                .then(function () {
                    return delChannel(channelIds);
                })
                .then(() => {
                    this.getList();
                    this.$modal.msgSuccess("删除成功");
                })
                .catch(() => {});
        },
    },
};
</script>
