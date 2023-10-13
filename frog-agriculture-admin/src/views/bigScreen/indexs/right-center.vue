<template>
<div style="display:flex;">
    <dv-active-ring-chart :config="config" style="width:250px;height:250px" />
    <div style="font-size:14px;margin-top:80px;line-height:10px;margin-left:-20px;">
        <div style="margin-bottom:20px;color:#23cdd8">
            发送字节：<span style="color:#fff;">{{formatter(this.static['bytes.sent'])}}</span>
            <dv-decoration-3 style="width:200px;height:20px;margin-top:5px;" />
        </div>
        <div style="margin-bottom:20px;color:#23cdd8">
            接收字节：<span style="color:#fff;">{{formatter(this.static['bytes.received'])}}</span>
            <dv-decoration-3 style="width:200px;height:20px;margin-top:5px;" />
        </div>

    </div>
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
    computed: {},
    beforeDestroy() {
        this.clearData()
    },
    methods: {
        /** 查询emqx统计*/
        statisticMqtt() {
            statisticMqtt().then(response => {
                this.static = response.data.data[0].metrics;
                // 图标配置
                this.config = {
                    data: [{
                            name: '发送',
                            value: this.static['bytes.sent']
                        },
                        {
                            name: '接收',
                            value: this.static['bytes.received']
                        },
                    ],
                    color: ["#ffdb5c", "#67e0e3"]
                };
                // 轮询
                this.switper();
            })
        },
        // 数字格式化
        formatter(number) {
            if (number) {
                const numbers = number.toString().split('').reverse()
                const segs = []
                while (numbers.length) segs.push(numbers.splice(0, 3).join(''))
                return segs.join(',').split('').reverse().join('')
            }
            return 0;
        },
        clearData() {
            if (this.timer) {
                clearInterval(this.timer)
                this.timer = null
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

    },
};
</script>
