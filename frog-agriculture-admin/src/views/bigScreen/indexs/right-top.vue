<template>
<div style="padding-top:10px;">
    <dv-capsule-chart :config="config" style="width:430px;;height:230px" />
</div>
</template>

<script>
import {
    statisticMqtt
} from "@/api/iot/emqx";
export default {
    data() {
        return {
            // emqx统计信息
            static: {},
            config: {},
            timer: null,
        };
    },
    created() {
        this.statisticMqtt();
    },

    mounted() {},
    beforeDestroy() {
        this.clearData();
    },
    methods: {
        /** 查询emqx统计*/
        statisticMqtt() {
            statisticMqtt().then(response => {
                this.static = response.data.data[0].metrics;
                this.config = {
                    data: [{
                            name: '传递消息',
                            value: this.static['messages.delivered']
                        }, {
                            name: '遗弃消息',
                            value: this.static['messages.dropped']
                        },
                        {
                            name: '发布消息',
                            value: this.static['messages.publish']
                        },
                        {
                            name: '接收消息',
                            value: this.static['messages.received']
                        },{
                            name: '认证次数',
                            value: this.static['client.authenticate']
                        },
                        {
                            name: '认证成功',
                            value: this.static['client.connected']
                        }, {
                            name: '断开连接',
                            value: this.static['client.disconnected']
                        }, 
                    ],
                    unit: '次数',
                    showValue: true
                }
                this.switper();
            })
        },
        clearData() {
            if (this.timer) {
                clearInterval(this.timer);
                this.timer = null;
            }
        },
        //轮询
        switper() {
            if (this.timer) {
                return
            }
            let looper = (a) => {
                this.statisticMqtt()
            };
            this.timer = setInterval(looper, 60000);
        },
    }
};
</script>
