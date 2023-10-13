import Vue from 'vue'
import chinaMap from './geoJson/china.json'
let vm = new Vue();
/**
 *
 * @param {*} dom
 * @param {*} seriesName 图表名称
 * @param {*} formatterTitle 中间名称
 * @param {*} data 图表值
 * @param {*} total 图表中间值
 */
let pieChart = function (dom,seriesName,formatterTitle,data,total) {
    let myChart = vm.$echarts.init(dom);
    let option = {
        color: [
            "#5470c6",
            "#91cc75",
            "#fac858",
            "#ee6666",
            "#73c0de",
            "#3ba272",
            "#fc8452",
            "#9a60b4",
            "#ea7ccc",
        ],
        tooltip: {
            trigger: "item",
        },
        // legend: {
        //   top: '30%',
        //   left: '5%',
        //   orient:'vertial'
        // },
        series: [
            {
                name: seriesName||'seriesName',
                type: "pie",
                center: ["50%", "50%"],
                radius: ["60%", "70%"],
                itemStyle: {
                    borderRadius: 10,
                    borderColor: "#fff",
                    borderWidth: 2,
                },
                avoidLabelOverlap: false,
                label: {
                    show: true,
                    position: "center",
                    formatter: [`{a|${total || 0}}`, `{b|${formatterTitle|| 'formatterTitle'}}`].join("\n"),
                    rich: {
                        a: {
                            fontSize: "30px",
                            fontWeight: "bold",
                        },
                        b: {
                            color: "#666",
                            lineHeight: 40,
                        },
                    },
                },
                data:data||
                 [
                    { value: 1048, name: "18-30岁" },
                    { value: 735, name: "31-40岁" },
                    { value: 580, name: "41-50岁" },
                    { value: 300, name: "50-60岁" },
                    { value: 300, name: "60岁以上" },
                ],
            },
        ],
    };
    option && myChart.setOption(option);
}
/**
 * 折线图
 * @param {*} dom
 * @param {*} seriesName 图表名称
 * @param {*} yAxis 纵坐标名称
 * @param {*} xAxisData 横坐标值
 * @param {*} seriesData 图表值
 * @param {*} type line OR bar
 */
let lineChart = function (dom,seriesName,yAxis,xAxisData,seriesData,type) {
    let myChart = vm.$echarts.init(dom);
    let option = {
        legend: {
            data: [seriesName||'seriesName']
        },
        tooltip: {
            trigger: 'axis'
          },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: xAxisData||['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: yAxis.name||'yAxisName',
                min: yAxis.min||0,
                max:yAxis.max|| 250,
                interval: yAxis.interval || 50,
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: seriesName||'seriesName',
                type: type||'bar',
                data:seriesData|| [
                    2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3
                ]
            }
        ]
    };
    option && myChart.setOption(option);
}

let barChart = function (dom) {
    let myChart = vm.$echarts.init(dom);
    let option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                // Use axis to trigger tooltip
                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
            }
        },
        legend: {
            data: ['使用', '闲置','其他']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value'
        },
        yAxis: {
            type: 'category',
            data: ['CPU', '内存', '硬盘']
        },
        series: [
            {
                name: '使用',
                type: 'bar',
                stack: 'total',
                label: {
                    show: true
                },
                barWidth:15,
                itemStyle:{
                    borderRadius: 5
                },
                emphasis: {
                    focus: 'series'
                },
                data: [320, 302, 301]
            },
            {
                name: '闲置',
                type: 'bar',
                stack: 'total',
                label: {
                    show: true
                },
                itemStyle:{
                    borderRadius: 5
                },
                emphasis: {
                    focus: 'series'
                },
                data: [120, 132,220]
            },
            {
                name: '其他',
                type: 'bar',
                stack: 'total',
                label: {
                    show: true
                },
                itemStyle:{
                    borderRadius: 5
                },
                emphasis: {
                    focus: 'series'
                },
                data: [220, 182]
            }
        ]
    };
    option && myChart.setOption(option);
}
let mapChart = function(dom,data,i,bgColor,lineColor){
    let myChart = vm.$echarts.init(dom);
    vm.$echarts.registerMap("china", { geoJSON: chinaMap });
    // let data =[{name:'yc',value:[117.37,32.94,3000]}]

       var option = {
           title:{
               text:'产品溯源统计',
               subtext:`我们的产品走过了${data.length}个城市，被客户查询了${i}次`,
               x:'center',
               y:'top',
               textStyle:{
                   color:'#2b7',
                   fontSize:'29',
               },
               subtextStyle:{
                color:'#C0C4CC',
                fontSize:'15',
                height:'40',
                lineHeight:'40'
                }
           },
           toolbox:{},
        //    legend:{
        //        x:'left',
        //        y:'top'
        //    },
           visualMap:{
               min:0,
               max:1000,
               show:true,
               type:'piecewise',
               pieces:[
              {gt: 1500},            // (1500, Infinity]
               {gt: 900, lte: 1500},  // (900, 1500]
               {gt: 310, lte: 1000},  // (310, 1000]
               {gt: 200, lte: 300},   // (200, 300]
               {gt: 0, lte: 200, color:'#2b7'}, ],
               textStyle: {
                color: lineColor?'#ffffff':'#666'
              },
               },
           geo:{
               map:'china',
               roam:true,
               show:true,
               zoom:1.7,
               top:250,
               select:{
                disabled:false
               },
               itemStyle:{
                   borderColor:'#fff',
                   borderWidth:2,
                   areaColor:bgColor || '#e6e8eb'
               }
           },
           tooltip:{
               show:true,
               formatter:function(param,ticket,callback){
                   return `在${param.data.name}查询了${param.data.value[2]}次`
               }
           },
           series:[
            //    {
            //        type:'heatmap',
            //        coordinateSystem:'geo',
            //        data:data,
            //        name:'热力图'
            //    },
               {
                   type:'scatter',
                   symbol:'image://data:image/png;base64,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',
                   symbolSize:'28',
                   coordinateSystem:'geo',
                   data:data,
                   label:{
                       show:false
                   }
               }
           ],
       };
    option && myChart.setOption(option);
}

export { pieChart, lineChart,barChart,mapChart }
