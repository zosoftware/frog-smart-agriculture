let chartOption={
    tooltip: {
      trigger: 'axis',
    },
    title: {
        text:'',
        left: 'center',
    },
    toolbox: {
      feature: {
        saveAsImage: {},
      },
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
    },
    yAxis: {
      type: 'value',
      boundaryGap: [0, '100%'],
    },
    dataZoom: [
      {
        type: 'slider',
        start: 0,
        end: 100,
      },
    ],
    grid:{
        left:0,
        right:0,
        top:40
    },
    series: [
      {
        name: '',
        type: 'line',
        symbol: 'none',
        sampling: 'lttb',
        itemStyle: {
          color: '#2b7',
        },
        smooth: true,
        data: [11, 34, 41, 35, 16, 25, 34, 19, 20, 32, 24],
      },
    ],
  }
  export { chartOption };
