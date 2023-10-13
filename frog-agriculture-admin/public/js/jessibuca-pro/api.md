[toc]

# 播放器 API

## JessibucaPro(options)

options 支持的参数有：

### container

- **类型**：`DOM` or `string`
- **用法**：

播放器容器。

> 若为 string ，则底层调用的是 document.querySelector(container)

源码

```js

let $container = options.container;
if (typeof options.container === 'string') {
    $container = document.querySelector(options.container);
}
if (!$container) {
    throw new Error('jessibucaPro need container option');
    return;
}

if ($container.nodeName === 'CANVAS' || $container.nodeName === 'VIDEO') {
    throw new Error(`jessibucaPro container type can not be ${$container.nodeName} type`);
    return;
}
```

> container 不能是 video 或者canvas 标签

### videoBuffer

- **类型**：`number`
- **默认值**：`1`
- **用法**： 设置最小缓冲时长，单位秒，播放器会自动消除延迟。

>

### videoBufferDelay（pro）

- **类型**：`number`
- **默认值**：`1`
- **用法**： 设置最大缓冲延迟时长，单位秒，最小不能低于1秒，如果延迟超过该值，就会进行丢帧处理。

### decoder

- **类型**：`string`
- **默认值**：`decoder.js`
- **用法**：

worker地址

> 默认引用的是根目录下面的decoder-pro.js文件 ，decoder-pro.js 与 decoder-pro.wasm文件必须是放在同一个目录下面。

### forceNoOffscreen(废弃)

- **类型**：`boolean`
- **默认值**：true
- **用法**：

是否不使用离屏模式（提升渲染能力）

> 由于目前浏览器兼容性太差，会出现慕名奇妙的错误问题，目前播放器内部禁用离屏渲染。

### useOffscreen（pro）(废弃)

- **类型**：`boolean`
- **默认值**：false
- **用法**：

是否使用离屏模式（提升渲染能力）默认是不开启的

> 主要是为了替代`forceNoOffscreen` 参数使用的。

### hiddenAutoPause

- **类型**：`boolean`
- **默认值**：false
- **用法**：

是否开启当页面的'visibilityState'变为'hidden'的时候，自动暂停播放。

### hasAudio

- **类型**：`boolean`
- **默认值**：true
- **用法**：

是否有音频，如果设置`false`，则不对音频数据解码，提升性能。

### hasVideo（pro）

- **类型**：`boolean`
- **默认值**：true
- **用法**：

是否有视频，如果设置`false`，则不对视频数据解码，只播放音频数据。

### rotate

- **类型**：`number`
- **默认值**：0
- **用法**：

设置旋转角度，支持，0(默认) ，90，180，270 四个值。

### isResize

- **类型**：`boolean`
- **默认值**：`true`
- **用法**：

1. 当为`true`的时候：视频画面做等比缩放后,高或宽对齐canvas区域,画面不被拉伸,但有黑边。 等同于 `setScaleMode(1)`
2. 当为`false`的时候：视频画面完全填充canvas区域,画面会被拉伸。等同于 `setScaleMode(0)`

### isFullResize

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：

1. 当为`true`
   的时候：视频画面做等比缩放后,完全填充canvas区域,画面不被拉伸,没有黑边,但画面显示不全。等同于 `setScaleMode(2)`

### isFlv

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：

1. 当为`true`的时候：ws协议不检验是否以.flv为依据，进行协议解析。

### debug

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否开启控制台调试打印

### debugLevel（pro）

- **类型**：`string`
- **默认值**：`warn`
- **用法**：

debug模式下的日志级别，支持`debug`、`warn`，默认是`warn`

### isMulti（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：

当多路播放的时候，可以配置起来，这样在看日志的时候，会有一个uuid来区分。

> 该参数只有在 debug:true 模式下才会生效

### timeout

- **类型**：`number`
- **默认值**：`10`
- **用法**：

1. 设置超时时长, 单位秒
2. 在连接成功之前(loading)和播放中途(heart),如果超过设定时长无数据返回,则回调timeout事件

> 不推荐使用，只是为了兼容v2版本的参数，推荐使用`loadingTimeout`和`heartTimeout` 参数，单独配置。

### heartTimeout

- **类型**：`number`
- **默认值**：`10`
- **用法**：

1. 设置超时时长, 单位秒
2. 播放中途,如果超过设定时长无数据返回,则回调timeout事件

### heartTimeoutReplay

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： 是否开启心跳超时之后自动再播放

> heartTimeout 心跳超时之后自动再播放,不再抛出异常，而直接重新播放视频地址。

### heartTimeoutReplayTimes

- **类型**：`number`
- **默认值**：`3`
- **用法**： heartTimeoutReplay 重试次数

> heartTimeoutReplay 重试失败之后，不再重新播放视频地址。

> 是整个生命周期中重试的次数。

> 如果想无限次重试，可以设置为-1。

### loadingTimeout

- **类型**：`number`
- **默认值**：`10`
- **用法**：

1. 设置超时时长, 单位秒
2. 在刚开始播放的时候，在连接成功之前,如果超过设定时长无数据返回,则回调timeout事件

### loadingTimeoutReplay

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： 是否开启loading超时之后自动再播放

> loadingTimeout 心跳超时之后自动再播放,不再抛出异常，而直接重新播放视频地址。

### loadingTimeoutReplayTimes

- **类型**：`number`
- **默认值**：`3`
- **用法**： loadingTimeoutReplay 重试次数

> loadingTimeoutReplay 重试失败之后，不再重新播放视频地址。

> 如果想无限次重试，可以设置为-1

### supportDblclickFullscreen

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否支持屏幕的双击事件，触发全屏，取消全屏事件。

### showBandwidth

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否显示网速

### showPerformance（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否显示性能面板

### operateBtns

- **类型**：`object`

- **用法**： 配置底部操作按钮

1. fullscreen 是否显示全屏按钮
2. screenshot 是否显示截图按钮
3. play 是否显示播放暂停按钮
4. audio 是否显示声音按钮
5. record 是否显示录制按钮
6. ptz 是否显示PTZ轮盘按钮（pro）
7. quality 是否显示清晰度按钮（pro）
8. close 是否显示关闭按钮（pro）
9. zoom 是否显示电子放大（pro）
10. scale 是否显示sacle按钮（pro）
11. performance 是否显示性能按钮（pro）
12. face 是否显示人脸识别按钮（pro）
12. fullscreenFn 绑定全屏按钮的自定义方法（pro），如设置了，点击全屏按钮的时候触发。
15. fullscreenExitFn 绑定退出全屏按钮的自定义方法（pro），如设置了，点击退出全屏按钮的时候触发。
13. screenshotFn 绑定截图按钮的自定义方法（pro），如设置了，点击截图按钮的时候触发。
14. playFn 绑定播放按钮的自定义方法（pro），如设置了，点击播放按钮的时候触发。
16. pauseFn 绑定暂停按钮的自定义方法（pro），如设置了，点击暂停按钮的时候触发。
17. recordFn 绑定录制按钮的自定义方法（pro），如设置了，点击录制按钮的时候触发。
18. recordStopFn 绑定停止录制按钮的自定义方法（pro），如设置了，点击停止录制按钮的时候触发。

### qualityConfig（pro）

- **类型**：`array`
- **默认值**：`[]`
- **用法**： 配置清晰度 ，例如 `['普清', '高清', '超清', '4K', '8K']`

> 点击回调的参数为当前清晰度的值，比如点击了`高清`，回调的参数就是`高清`。

### defaultStreamQuality（pro）

- **类型**：`string`
- **默认值**：``
- **用法**： 默认显示的清晰度，如果不设置，会显示第一个清晰度

### watermarkConfig（pro）

- **类型**：`object | array`
- **默认值**：`{}`
- **用法**： 配置播放器播放过程中水印显示，支持`图片`和`文字`和`矩形边框`和`自定义HTML`

支持`单个对象` 或者 `数组`

支持的参数有：

```
{
    left: '', //
    right: '', //
    top: '', //
    bottom: '', //
    opacity: 1, // 透明度
    backgroundColor:''// 背景颜色 同css backgroundColor
    // 图片配置
    image: {
        src: '',
        width: '100',
        height: '60'
    },
    // 文字配置
    text: {
        content: '',
        fontSize: '14',
        color: '#000'
    },
    // 矩形边框
    rect:{
            color: '',
            lineWidth: 2,
            width: '',
            height: ''

    },
    // 自定义html
    html: ''
}
```

#### 图片配置

支持 url 或者 base64 格式

```

{
    watermarkConfig: {
        image: {
            src: 'data:image/png;base64,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',
            width:150,
            height:48
        },
        right: 10,
        top:10
    }

}
```

```
{
    watermarkConfig: {
        image: {
            src:"http://jessibuca.monibuca.com/jessibuca-logo.png",
            width:150,
            height:48
        },
        right: 10,
        top:10
    }
}
```

#### 文字配置

```

{
    watermarkConfig:{
        text:{
            content:'jessibuca-pro',
                fontSize:'14',
                color:'#000'
        },
        right: 10,
        top:10,
        left:10,
        bottom:10
    }

}
```

#### 矩形边框
```

{
    watermarkConfig:{
        rect:{
            color: 'red',
            lineWidth: 2,
            width: '100',
            height: '200'
        },
        right: 10,
        top:10,
        left:10,
        bottom:10
    }

}
```

#### HTML

```

{
    watermarkConfig:{
        html:'<div>this is a <span style='color:red'>test</span> html</div>',
        right: 10,
        top:10,
        left:10,
        bottom:10
    }

}
```

> 在单个配置项上面：优先级 image > text > rect > html


#### 多个水印配置

配置成数组就可以支持多个水印效果。

```

{
    watermarkConfig:[
        {
            html:'<div>this is a <span style='color:red'>test</span> html</div>',
            right: 10,
            top:10,
            left:10,
            bottom:10
        },
        {
            text:{
                content:'jessibuca-pro',
                    fontSize:'14',
                    color:'#000'
            },
            right: 10,
            top:10,
            left:10,
            bottom:10
        }
    ]

}
```



### keepScreenOn

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： 开启屏幕常亮，在手机浏览器上, canvas标签渲染视频并不会像video标签那样保持屏幕常亮。

> 只会在移动端生效，默认开启。

### isNotMute

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否开启声音，默认是关闭声音播放的。

> 是否不是静音 = 是否开启声音

> 配置了不一定会生效，因为浏览器的限制，只有在用户主动触发事件后才能播放声音。

### loadingIcon

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： 加载过程中的时候，是否显示加载icon。

### loadingText

- **类型**：`string`
- **默认值**：``
- **用法**： 加载过程中文案。

### background

- **类型**：`string`
- **默认值**：``
- **用法**： 播放前的背景图片。

### backgroundLoadingShow

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 如果配置了`background`参数，在loading 过程中的时候还继续显示`background`配置的图片。

### useMSE

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否开启MediaSource硬解码

> 视频编码只支持H.264视频（Safari on iOS不支持）

> 不支持 forceNoOffscreen 为 false (开启离屏渲染)

> 优先级高于useWCS:true

### mseUseCanvasRender（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： MediaSource硬解码通过canvas播放流数据

> 推荐使用`useCanvasRender` 参数

### hlsUseCanvasRender （pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： hls通过canvas播放流数据

> 推荐使用`useCanvasRender` 参数

### useWCS

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否开启Webcodecs硬解码

> 视频编码只支持H.264视频 (需在chrome 94版本以上，需要https或者localhost环境)

> 支持 forceNoOffscreen 为 false （开启离屏渲染）

> 如果同时设置了useMSE:true, 则优先使用useMSE

### wcsUseVideoRender（pro）

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： Webcodecs硬解码之后通过video播放流数据,仅仅useWCS 为true的时候有效

> 推荐使用 `useVideoRender` 参数

### useSIMD（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否使用SIMD 软解码，仅仅wasm软解码的时候有效

> 只会在wasm解码的时候才会生效

> 使用Chrome/Edge 91, Firefox89及之后正式提供的SIMD指令集加速解码, 在1080P以上分辨率带来100%-300%的性能提升

> 尤其在HEVC的解码上提升非常明显.

> Safari暂不支持

### wasmUseVideoRender（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： wasm软解码之后通过video播放流数据，仅仅wasm软解码的时候有效

> 推荐使用 `useVideoRender` 参数

### wasmDecodeErrorReplay（pro）

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： 是否开启解码失败重新播放

> wasm解码报错之后，不再抛出异常，而是直接重新播放视频地址。

### mseDecodeErrorReplay（pro）

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： mse解码报错之后，是否重新播放

> mse解码报错之后，不再抛出异常，而是直接重新播放视频地址。


### wcsDecodeErrorReplay（pro）

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： wcs解码报错之后，是否重新播放

> wcs解码报错之后，不再抛出异常，而是直接重新播放视频地址。

### autoWasm

- **类型**：`boolean`
- **默认值**：`true`
- **用法**： 在使用MSE或者 Webcodecs 播放H265的时候，是否自动降级到wasm模式。

> 在MSE或者WCS解码出现错误的时候，如果配置的`mseDecodeErrorReplay:true`,或者`wcsDecodeErrorReplay:true`的前提下。

> 如果配置了`autoWasm:true`, 则会自动降级到wasm模式播放。

> 在多屏的情况下，如果解码失败，配置了`autoWasm:true`，则会自动降级到wasm模式播放，会加大CPU的负担。

### hotKey

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 是否开启键盘快捷键

> 目前支持的键盘快捷键有：esc -> 退出全屏；arrowUp -> 声音增加；arrowDown -> 声音减少；

### controlAutoHide

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： 控制栏是否自动隐藏

> 只有鼠标聚焦到播放器内部才会显示，移除之后，会消失。

> 只支持pc端，移动端不生效。

### playbackConfig（pro）

- **类型**：`object`
- **默认值**：`{}`
- **参数**：TF卡流（录像流）播放配置
    - `{array} playList` 底部UI 24小时高亮时间端，如在该时间段内，可触发点击事件
        - `{number} start`  开始时间戳 例如 1653841634 或者 16538416340000
        - `{number} end`  结束时间戳 例如 1653843420 或者 16538434200000
    - `{number} fps`  默认值`25` 渲染FPS(如果不设置，播放器会根据流数据计算出fps),
    - `{number} isCacheBeforeDecodeForFpsRender` 默认值`false` 是否在解码前缓存数据
    - `{boolean} showRateBtn`   默认值 `true` 是否显示倍速播放按钮
    - `{array} rateConfig`   默认值 `[]` 倍速播放配置
        - `{number} value`  倍速播放倍数
        - `{string} label`  倍速播放按钮显示的文字
    - `{boolean} showControl`   默认值 `true` 是否显示底部UI 24小时控制条
    - `{boolean} uiUsePlaybackPause` 默认值 `false`  ui上面是否使用 playbackPause 方法
    - `{boolean } isUseFpsRender` 默认值 `false` 是否使用固定的fps渲染，如果设置的fps小于流推过来的，会造成内存堆积甚至溢出
    - `{boolean } isUseLocalCalculateTime` 默认值 `false` 是否使用本地时间来计算playback时间
    - `{number} localOneFrameTimestamp`  默认值`40` 一帧 40ms, isUseLocalCalculateTime 为 true
      生效。（不适合高倍率I帧渲染的场景,当切换成只解码I帧的时候需要更新这个时间戳）。

- **用法**： TF卡流时间端配置

例如

```js
jessibucaPro.playbackConfig('url', {
    playList: [
        {
            start: 16538416340000,
            end: 16538434200000
        },
        {
            start: 1653881963,// 会自动补齐最后四个0
            end: 1653885397 // 会自动补齐最后四个0
        }
    ],
    fps: 25,
    showControl: true,
    uiUsePlaybackPause: true,
    isCacheBeforeDecodeForFpsRender: false,
    isUseFpsRender: false,
    isUseLocalCalculateTime: false,
    localOneFrameTimestamp: 40
})
```

> 其他不在时间段的区域，显示是表示没有数据段。不绑定点击时间回调事件。其他在时间区域内的区间点击是有事件响应的。

> isCacheBeforeDecodeForFpsRender 为 true 时，会在解码前缓存数据，针对于有些特殊的流，在原本播放器只需要一倍率播放的情况下，
> 但是服务器端推流是2倍或者以上，这种建议设置为true，防止解码之后缓存，导致内存溢出。

### openWebglAlignment (pro)

- **类型**：`boolean`
- **默认值**：`false`
- **用法**： webgl渲染是否每次取1字节的数据，仅在使用canvas 渲染的时候生效。

> 为了解决 视频渲染发绿（软解码）

### playbackForwardMaxRateDecodeIFrame (pro)

- **类型**：`number`
- **默认值**：`4`
- **用法**：录像流播放的时候，当倍率达到多少之后，直接只解码I帧数据。

> 默认是达到4倍率的时候，就直接只解码I帧数据。

### playbackCurrentTimeMove(pro)

- **类型**：`boolean`
- **默认值**：`true`
- **用法**：录像流数据的当前时间是否跟着播放时长移动。

### mirrorRotate(pro)

- **类型**：`string`
- **默认值**：`none`
- **用法**：镜像翻转，支持水平(level)和垂直(vertical)

### useVideoRender(pro)

- **类型**：`boolean`
- **默认值**：`true`
- **用法**：是否使用video标签进行渲染

> 方便一键式配置渲染元素。

### useCanvasRender(pro)

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：是否使用canvas标签进行渲染

> 方便一键式配置渲染元素。

### networkDelay(pro)

- **类型**：`number`
- **默认值**：`10`
- **用法**：单位秒(s), 根据本地时间戳和流播放的时间戳，检测网络播放延迟。

### networkDelayTimeoutReplay(pro)

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：设置网络超时之后，是否重新播放。

> 如果设置为true，那么当网络超时之后，会重新播放。


> 因为这个参数的特殊性，如果配置 `false` 并不会暂停播放。 所以如果业务层需要自己做超时处理，则只需要自己监听


```js
// 监听一次（once）即可。
jessibucaPro.once('networkDelayTimeout', () => {
    // do something
})
```

### syncAudioAndVideo（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：是否根据时间戳来音视频同步。

> 播放器内部默认是根据流的顺序来进行音视频同步播放的。

> 如果设置为`true`，那么就是根据时间戳来进行音视频同步播放的。

### recordType（pro）

- **类型**：`string`
- **默认值**：`mp4`
- **用法**：视频录制默认的格式，支持`webm`和`mp4`。

### ptzClickType（pro）

- **类型**：`string`
- **默认值**：`click`
- **用法**：ptz 操作指令点击事件类型，支持`click` 和 `mouseDownAndUp`

> click 事件 会在点击之后，立即触发ptz arrow事件,300ms之后触发stop事件。
>
> mouseDownAndUp 会在鼠标按下，立即触发ptz arrow事件，鼠标松开，触发stop事件。

### ptzZoomShow（pro）
- **类型**：`boolean`
- **默认值**：`false`
- **用法**：是否显示ptz zoom 按钮


### extendOperateBtns（pro）

- **类型**：`array`
- **默认值**：`[]`
- **参数**：
    - `{string} name`  `必填` 类似uuid 用于区分按钮
    - `{number} index` `选填` 排序字段，默认值 `0`
    - `{string} icon`  `必填` 按钮图标,支持base64 或者src，主要是设置`background-url` 属性
    - `{string} iconHover` `选填` 按钮hover图标,等同于`icon`
    - `{string} iconTitle`  `选填` 按钮图标title
    - `{string} activeIcon`  `选填` 按钮active图标,等同于`icon`
    - `{string} activeIconHover`  `选填` 按钮active hover图标,等同于`icon`
    - `{string} activeIconTitle`  `选填` 按钮active图标title
    - `{function} click` `必填` 按钮点击事件
    - `{function} activeClick` `选填` active 状态下按钮点击事件
- **用法**：添加自定义按钮

```js
{
    extendOperateBtns:[
        {
            name: 'testBtn',
            index: 2,
            icon: '/expand.png',
            iconHover: '/expand-hover.png',
            iconTitle: '测试hover',
            activeIcon: '/zoom-stop.png',
            activeIconHover: '/zoom-stop-hover.png',
            activeIconTitle: '测试active-hover',
            click: () => {
                console.log('test click')
            },
            activeClick: () => {
                console.log('test active click')
            },
        },
        {
            name: 'testBtn2',
            index: 5,
            icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAMy0lEQVRoQ8WZ+5PX1XnHX+9zPt/vsktBbuqyLLdlAUXwBmgM3m9YE1vjdJzpTKejdhpQ08y0/4CT6e/5IZlWZaxpM500YZJGG+9XRK0IohEBgeyyCywrwiL3vX0/5zyd8/nulyAuQZYde3ZmZ2c/n89znvd5bu/nOWKU14LVVu49XFmg4K8XtgClH21H4d1+K23pXqHe0dxSoyksyZrxrzbR+3ypjOWghRjOxKceXs+Df79zP/v5keJo7TuqAOb/m43LQ77IAneC7kbMrSqqXSi+gem1SsX/fk8P+0YLxKgBuPjnNnZcX2W+RX+7YXcglgAThk76BLBZ2Ns4vTE44D+e08P+NT9Sfr6WGBUAs35mE9RfmSf8MmR/DiwGJp2m3DFgh8zWRNNaef/7MRPZt/V+DZ4PiPMC0PoTqwv1TFDM52JuGdiNQ8pffAaljgNbDd4H1sr5Tf1GV/f36UOykQAZOQAzzVvF5Jz8EszdAHaHweWqnvyfknsc1AH2tok1Lvfr2vezd6QxMXIAq83P3k+zfLge8R1IWeek26QscxQ4hohm/JlgIuCGTjm5zTpJz0n5C817y9tHGg8jBnDzY5Z1NDM9C+EGg3sgZR7GFwoaBxCbER1Y4RwtUSwUTB4CMGDwHuh5Z/lLE1x5+8YVqnyzLpQscIQpCvlCiVvMdDswB8gwtoK9irTJwDnZlZhuM7ikmlW128zecuINw384o5vub9wCmGnWv1MXjjOpXMovx7gBp4VmjJHZJ8ALkG1O+kblVzi409AVVQOxTcZbEb9x0LH//yeIT7H3rH+xxszllwRpjlCdKbaFkG3Y/YgOpdean7JJ5ZhfbebmO/AxWKfJb+pcya6RZp/a9iOOgS/562PmWpoY5xzjYsTlgWM15WvvpVpBPxMyhw+O4/UXcOh8a0DhjSMJnLN90/xjq2c89XUVMtVhg/3krp6+zgfVf7Zvz/X5nwZgpqZV1Id6dGk7A2cNNDO1PsW0vFJpdt5NJjIGYXL0xRAP+qzU3dzFvq8l56eUe+vwDQOEtkZy7lcYDtwZASSTWy9TM5dPEtabhdLubT/QweGEJAp97HOmZKW8FblFstgqaTJGXQQT9AnrMVxHdHGLr2Q7QwMHzmSRxGjL2WBTNDfRog3GrHQgHGNf1z+p7/T9hwVQCFB+RZBb4ogXG+4zR3yvHLJPtj6qRAdOrlQPupqYaTEsjdh1kq40aBY0YPhq2iQHSxV4D7KPQR9E/Id99ez5/G+ViN7J1foTGx9KlUtwfqmLcTYwYGgr3m/YOYH20y3xFQAtT9oFkC8yY7nQdaDJYHsNrRFhzYmG0qe1TdPJDx7hohjCNcDdYDeA0qZpHUGcSP0AMJYECAKoE2wDxmvO+XVtE+msKZUOzvnKPBf9t1EhqwUsFbiPDV4qmX9vx0q6T81cXwKQ+PxgJb9KcJuhOwSXAmOAlA4/kOl5c+7FnSu0O2mY0qfz4WqwVIXvAM2r6m5tSFvM6JaRIZola02lo0onLKXPNy3wQl3079Zcs3XVwGXBsptldhsUdPzCVEbAOg29LuMV4derwpG2H2rgS1konbyzfEFMnZR0K3A5cMHQaSZC8AkWn7GY/aLjEW3nMXNzLq5cZc5/Byxxocuq76b2Mb6lqPXRbK/zZETNjNhVkpYB8wGPsVnS/5jyZydS3raxmzCrqbLUo3vMdBeQil42tH9qQzcjWyN4ySzr2rlSf6gCWG1+Rg/jk89HuBkVlGDRSV5TldArY4M5PROje6bzYXWmVFk3Niw3+Bus4EHjQJtFfCnCK3kl29LQwLFwAuUlJjnCQmS3g7sVLFnqmMHrzvhVKfi1Y0scPcTgJUZ2p6zoKZaepsMRjC2Jokja2b4y+3kBYM7jAwuj3FTBLaAbgIWndFKJNR7F2IHsLeD1wSz7qOvvOJSodLBwn8FDwLUJJOJFjF9k8mt3rFDPychM1PsJmiqEZRJ/UeVOBTvdBFoN7neDvXT5sUxwlifSdz0U1lowxHDrhmT1yNgSobPj4eyBAsDsx/O/l2wOpltRYd4qo6yuw0Nd1NvR8YovZR9dUOFwYe5GZkjxr0R8ELQgMVATv3Qx/kf7I+WNp8io/rna/JyeypXR6V7h7gVLe+027L8x+3V9LG3tbSD3/YzH5ZdZtcdIDVJypSknXRmOStbdvqKUwKHZT1T+WdKlWArCLymfnvcYfIDpeYL7XccPtKsQlKjDNFqxeB8WHwDNl+g27D8tZE8XMTLMalll84jxe8D9YMlNPzN41pz9pqGSbayl6Ok/taasFG5V6jOMm4Cpp4k7vHNlliyIWh4P/yjZpQbfHebFI0NmfjHK/bZzhbYVgsw093Fm5z5+TxYfShYwOOjEfynGp9seLn80HIDWJwevjqb7gfuq6dY6DJ5xjmeOj8k+rqXnlidsrllcLtldBt86pY9IYlOz1L1zZTa9CmBV5faU3mSkwEm+3FhkiepKqaorWUHwMs6/W/SwK9Tb+rRdGCrhLzEeUvW7NGF4BeOXlvnXOro4UGsTU73oP0SjxXCTlJRP8UZDIVdajdwLg8fZ48YkKk5jloVrrDh5LQVrGaohSZ8+0M4EfOfKLDVRqKDCnkmmkKL+Nku8HlK+rqHtQ3yGsUHiZXL/Rvuj2pMa+lgOKV//dbUGMCV1YDLeTEXHcr9xUonurnrKY44xI3PhChO3I27CSEp9QToU45eu4tfm4xh0g4NzDX+tgxvNlOpLOuVUBNOB9oPaMHsbqW3nSv/jAkD6tfhJKx0NTA8+fAsrXCllgGmnWMJAmyA+e7IOpPYrZTCXLXfR7rHqHKgB0W7G/0pah9kuiTJorpldBaSKnZSvCD7E9FvJPde2UluTDoeVL7WY9ldKy6kOlf4YvNYGescs1YG4u/OR8rqTANIfidPsupBpysK3CxqRTuqPlqhU3UjPOrlft61Qe/qmmAf15YucuMuc+y5mKQWn8cheoBNxQFAyo1Gi2Yw0bnGSbTPjxdS1xbpsY+eDOpwSw6xplctl/u5T6kAtfe4D3kxFzEX/TqWeL4pvvtIPJCGNzHCEZcWkQSzFEpVQj2TvxMCrfox/r+0hHagF8+ynuEghLDa4S3CdRFNqK63KQlMdSVyoOEmJfrPCHdcLXlXmN7RdwGc1LjTvcZsWFZZELNWk5AUJcKLRW5Ced8rX5qVye+cDDNT40FfZaMrXB5htPiwVdrWhyYb1yPR+yPyHpT66azykALHa/LyDNAbli0zuSjO7VNAMSvOhpHjCMoBxCNEFSkVxE/hPJsJnp04jZv3MxljO1FIIlwdsiTM14hJofeoU1vVS2nb6dHtYOp0CNC8xNSOfFaQpZnbMsmzH7C72DNeMFJR6JhPDYKXJmWvBaXq0eKEz6lNDY8YJc+6Aw/YGxU4fSt3T9/HFmWTtuYipoZS3+KBJyPpMcW8sl/fU3ObUFH3mjmy1+VknGFfuZ2xIQXcKAxwuxw/FxBh6mUCWxo2V8ZjqlGibWb/F0jFKHLaMI2drLVNAf+4Z5wN1dVA5Dr1nmlycvSe25MqFA5/T7DLl/qNd1XrS1czgmVrCMx3G1/3/2QF8XUm19x4zx2XojAqvNr/4EG7j98nP9VCGU2VUAKTgy3KmhLzS5FFDdPFgLJf/UHOVpietYUxKozFvdMIbsccNlHe1/VBpfnpe67wBpADubKLVE66NxuIUuCZ9FM29kPqGpF3LkwOLRHaLYcVkDtOm6MJbDRNKW893NjRyAI+Zmz+DsQM505yF61DR1KRqnArXOsRvJP9xoa+FGx3cY1ZcfCQytkno5UhY6wdLHW3/kKbY5xZjNbONGEDKFAcj00S4xsEd5rgZIzX0qefdDlpnpjYcpmiXmQpWmWhEKmjdiW6YeNnj32zrpuMbvx8ouEvOzOjDTaK4H7iZ6h1AWsfA9lmq4JYG7DYZ1DxEzNLzokXF6fk8d8/t2s/2bxxA4i6tM5mcD+RXO8dyLI1VlLqs2krjkDRKTOm3PDTdqD4TuyyyxsFLHv/26aOSc4nqEbtQ2iTl+hNHmOZCWFwdn9uy6iynGMUMtxI32gOsrzb04f0+V955Ppff5wWgdkegE1zss3BllYQVU4d0kVGjwjUgAawd9K5gjfAflAO7t+6nd6TuUzXmaKzEYi/iIufCkup9WcEmT3WnwaJ9NK1LLBT59W6Q3V8ihSPUY3QAVHOlZq6iMSNcW9yZFTFRtKfp4Q6Z1qaxjDO/vjSFrvPN/+edRoc9sOpFR7NiuDWKe1UdnUjSeou87M2/N96zZ6QXesPtOXoWGJJebeAH52N+CdisdMmXxo0m/8GsbtrOejdwjq406gBSg9N6hKm55XN8VGMCYGm6HbIdnY8qtYajuv4P6KgPi/FF/nwAAAAASUVORK5CYII=',
            iconHover: 'data:image/png;base64,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',
            iconTitle: '测试hover2',
            click: () => {
                console.log('test click2')
            },
        }
    ]
}
```

### contextmenuBtns（pro）

- **类型**：`array`
- **默认值**：`[]`
- **参数**：
    - `{string} content`  `必填`
    - `{number} index` `选填` 排序字段，默认值 `0`
    - `{function} click` `必填` 点击事件
- **用法**：添加自定义右键菜单按钮

```js
{
    contextmenuBtns: [
        {
            content: '测试1',
            index: 0,
            click: () => {
                console.log('test click1')
            },
        },
        {
            content: '测试2',
            index: 1,
            click: () => {
                console.log('test click2')
            },
        }
    ]
}
```

### checkFirstIFrame（pro）

- **类型**：`boolean`
- **默认值**：`true`
- **用法**：是否检测第一个iframe，如果为true，则会在播放的时候，首帧必须是i帧的时候，才会去解码，如果不是i帧，就会一直等待i帧的出现。

> 如果设置为false，则会不判断首帧是否是i帧，可能会出现绿屏或者花屏的情况。

### audioEngine （pro）

- **类型**：`string`
- **默认值**：`'null'`
- **用法**：设置音频引擎，可选值有`'worklet'`、`'script'`、`'active'`、，默认值为`'null'`，会自动选择最佳的音频引擎。

> worklet：使用webworklet，需要浏览器支持(需要https环境)，如果不支持，会自动降级到script引擎。

> active：是为了兼容安卓的微信端播放的，一般情况下，在安卓的微信端，默认会使用active引擎。

> script：使用script引擎，一般情况下，会自动选择这个引擎（兼容性最好的音频播放音频）。

### weiXinInAndroidAudioBufferSize（pro）

- **类型**：`number`
- **默认值**：`4800`
- **用法**：安卓微信端的音频缓冲大小，单位是字节。

> 只会在安卓微信端生效（audioEngine参数设置为 null 或者 active），其他情况下，不会生效。

> 一般情况下，不需要设置这个参数，如果出现音频播放卡顿的情况，可以尝试设置这个参数。

> 属于高级参数，一般情况下，不需要设置。如有需要，可以联系作者。

### isShowRecordingUI（pro）

- **类型**：`boolean`
- **默认值**：`true`
- **用法**：是否显示录制的UI（在没有配置底部的录制按钮的情况下），如果为true，则会显示录制的UI，如果为false，则不显示录制的UI。

> 方便通过直接调用js的录制方法的时候，可以显示录制的UI。

> 录制中的UI默认是根据底部按钮的配置来显示的，如果没有配置底部按钮，则会根据这个配置来显示。

### isShowZoomingUI（pro）

- **类型**：`boolean`
- **默认值**：`true`
- **用法**：是否显示缩放的UI（在没有配置底部的缩放按钮的情况下），如果为true，则会显示缩放的UI，如果为false，则不显示缩放的UI。

> 方便通过直接调用js的缩放方法的时候，可以显示缩放的UI。

> 缩放中的UI默认是根据底部按钮的配置来显示的，如果没有配置底部按钮，则会根据这个配置来显示。

### useFaceDetector（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：是否使用人脸检测，如果为true，则会使用人脸检测，如果为false，则不使用人脸检测。

> 人脸检测的功能，需要额外的引入 `js`和`wasm` 文件。
>

### useWebFullScreen(pro)

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：是否使用web全屏(旋转90度)（只会在移动端生效）。

> 如果手机设置了自动选择，建议不要开启。
>
> web全屏使用的是css方式实现的伪全屏，实现的是横屏播放的。如果手机设置了自动旋转，建议不要使用这个功能。
>
> ios上面不支持系统级别的全屏方法，推荐使用这个参数。



### fullscreenWatermarkConfig (pro)

- **类型**：`object`
- **默认值**：`{}`
- **参数**：全屏水印配置
- **参数**：
    - `{string} text`  `必填` 水印内容
    - `{string} opacity` `选填`  透明度 `默认：0.15` 参考css opacity
    - `{string} angle` `选填`  倾斜角度 `默认：15` 参考css transform rotate
    - `{string} color` `选填`  字体颜色 `默认：black` 参考 css color
    - `{string} fontSize` `选填` 字体大小 `默认：18px` ，参考css的font-size
    - `{string} fontFamily` `选填` 字体 `默认：微软雅黑`，参考css font-family

### pageVisibilityHiddenTimeout (pro)

- **类型**：`number`
- **默认值**：`5 * 60` 5 分钟
- **用法**：监听窗口不可见的超时时间，单位秒。如果为0，则不会监听窗口不可见的事件。

> 需要监听`visibilityHiddenTimeout` 事件来监听事件

```js
// 配置信息：
{
    pageVisibilityHiddenTimeout: 5 * 60
}
```

```js


player.on('visibilityHiddenTimeout', function () {
    // 窗口不可见5分钟超时
});
```

### heartTimeoutReplayUseLastFrameShow（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：播放过程中，超时重连时，是否使用最后一帧来显示。


### replayUseLastFrameShow（pro）

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：触发重播的时候，是否使用最后一帧来显示。


> heartTimeoutReplayUseLastFrameShow 是历史版本的遗留（不推荐使用了）。

> replayUseLastFrameShow 配置优先级高于 heartTimeoutReplayUseLastFrameShow。

> replayUseLastFrameShow 配置了之后，会覆盖 heartTimeoutReplayUseLastFrameShow 的配置。



### controlHtml（pro）

- **类型**：`string | DOM`
- **默认值**：`''`
- **用法**：自定义控制栏的html，如果配置了这个参数，则会在底部控制栏渲染出来。

## 静态变量

### ERROR

播放器对外的错误

```js
jessibucaPro.on(JessibucaPro.EVENTS.error, (error) => {
    if (error === JessibucaPro.ERROR.playError) {
        // 播放错误
    }
})
```

### EVENTS

播放器对外的事件

事件里面的`key`值

```js
jessibucaPro.on(JessibucaPro.EVENTS.blur, () => {
    // 窗口不可见
})
```

## 方法

### setDebug(flag)

- **参数**：
    - `{boolean} flag`
- **返回值**：`void`
- **用法**： 是否开启控制台调试打印

```js
// 开启
jessibucaPro.setDebug(true)
// 关闭
jessibucaPro.setDebug(false)
```

### getOption() （pro）

- **用法**： 获取配置参数信息
- **返回值**：`{object}`

```js
jessibucaPro.getOption()
```

### mute()

- **用法**： 静音
- **返回值**：`void`

```js
jessibucaPro.mute()
```

### cancelMute()

- **用法**： 取消静音
- **返回值**：`void`

```js
jessibucaPro.cancelMute()
```

### audioResume()

- **用法**： 留给上层用户操作来触发音频恢复的方法。
- **返回值**：`void`

> iPhone，chrome等要求自动播放时，音频必须静音，需要由一个真实的用户交互操作来恢复，不能使用代码。

https://developers.google.com/web/updates/2017/09/autoplay-policy-changes

### setTimeout(time)

- **参数**：
    - `{number} time`
- **返回值**：`void`
- **用法**：

设置超时时长, 单位秒 在连接成功之前和播放中途,如果超过设定时长无数据返回,则回调timeout事件

> 会同步设置`loadingTimeout`和`heartTimeout`的值

```js
jessibucaPro.setTimeout(10)

jessibucaPro.on('timeout', function () {
    //
});
```

### setScaleMode(mode)

- **参数**：
    - `{number} mode`
- **返回值**：`void`
- **用法**：

1. `0` 视频画面完全填充canvas区域,画面会被拉伸 等同于参数 `isResize` 为false
2. `1` 视频画面做等比缩放后,高或宽对齐canvas区域,画面不被拉伸,但有黑边 等同于参数 `isResize` 为true
3. `2` 视频画面做等比缩放后,完全填充canvas区域,画面不被拉伸,没有黑边,但画面显示不全 等同于参数 `isFullResize` 为true

```js
jessibucaPro.setScaleMode(0)

jessibucaPro.setScaleMode(1)

jessibucaPro.setScaleMode(2)
```

### pause(isClear)

- **参数**：
    - `{boolean} isClear` 是否清除画面，默认 `false`
- **返回**：
    - `{Promise}`
- **用法**： 暂停播放

```js
jessibucaPro.pause().then(() => {
    console.log('pause success')

    jessibucaPro.play().then(() => {

    }).catch((e) => {

    })

}).catch((e) => {
    console.log('pause error', e);
})

```

```js
jessibucaPro.pause(true).then(() => {
    console.log('pause success')

    jessibucaPro.play().then(() => {

    }).catch((e) => {

    })

}).catch((e) => {
    console.log('pause error', e);
})

```

可以在pause 之后，再调用 `play()`方法就继续播放之前的流。

### close()

- **用法**： 关闭视频,不释放底层资源
- **返回**：
    - `{Promise}`

```js
jessibucaPro.close().then(() => {
    console.log('close success')
}).catch((e) => {
    console.log('close error', e);
})
```

### destroy()

- **用法**： 关闭视频，释放底层资源
- **返回**：`Promise`


```js
jessibucaPro.destroy().then(()=>{
    console.log('destroy success')
}).catch((e) => {
    console.log('destroy error', e);
})
```

### clearView()

- **用法**： 清理画布为黑色背景
- **返回**：`void`

```js
jessibucaPro.clearView()
```

### play([url],[options])

- **参数**：
    - `{string} url`
    - `{object} options` 目前用不到这个参数

- **返回**：
    - `{Promise}`
- **用法**： 播放视频

```js

jessibucaPro.play('url').then(() => {
    console.log('play success')
}).catch((e) => {
    console.log('play error', e)
})
//
jessibucaPro.play()
```

### resize()

- **用法**： 重新调整视图大小
- **返回**：`void`

```js
jessibucaPro.resize()
```

### setBufferTime(time)

- **参数**：
    - `{number} time`
- **返回**：`void`

- **用法**： 设置最大缓冲时长，单位秒，播放器会自动消除延迟，如果设置为0 则不进行任何缓冲，直接进行解码渲染。
  等同于 `videoBuffer` 参数。

```js
// 设置 200ms 缓冲
jessibucaPro.setBufferTime(0.2)
```

### setBufferDelayTime(time)(pro)

- **参数**：
    - `{number} time`
- **返回**：`void`

- **用法**： 设置最大延迟时间，单位秒，播放器当延迟超过这个时间之后就会丢帧，消除延迟。

等同于 `videoBufferDelay` 参数。

```js
// 设置 1000ms 最大延迟时间
jessibucaPro.setBufferTimeDelay(1)
```

### setRotate(deg)

- **参数**：
    - `{number} deg`
- **返回**：`void`
- **用法**： 设置旋转角度，支持，0(默认) ，90，180，270 三个值。

> 可用于实现监控画面小窗和全屏效果，由于iOS没有全屏API，此方法可以模拟页面内全屏效果而且多端效果一致。

> 注意，当旋转90度后，用户也会旋转手机，这时如果手机未锁定方向，画面又可能因为浏览器旋转导致画面又旋转了。可以监听页面旋转事件，将角度又旋转回0

```js
jessibucaPro.setRotate(0)

jessibucaPro.setRotate(90)

jessibucaPro.setRotate(270)
```

### setVolume(volume)

- **参数**：
    - `{number} volume`

- **用法**：

1. 设置音量大小，取值0 — 1
2. 当为0时，完全无声
3. 当为1时，最大音量，默认值

> 区别于 mute 和 cancelMute 方法，虽然设置setVolume(0) 也能达到 mute方法，但是mute
> 方法是不调用底层播放音频的，能提高性能。而setVolume(0)只是把声音设置为0 ，以达到效果。

```js
jessibucaPro.setVolume(0.2)

jessibucaPro.setVolume(0)

jessibucaPro.setVolume(1)
```

### hasLoaded()

- **返回值**：`boolean`
- **用法**： 返回是否加载完毕

```js
var result = jessibucaPro.hasLoaded()
console.log(result) // true
```

### setKeepScreenOn()

- **用法**： 开启屏幕常亮，在手机浏览器上, canvas标签渲染视频并不会像video标签那样保持屏幕常亮。 H5目前在chrome\edge 84,
  android chrome 84及以上有原生亮屏API, 需要是https页面
  其余平台为模拟实现，此时为兼容实现，并不保证所有浏览器都支持
- **返回值**：`void`

```js
jessibucaPro.setKeepScreenOn()
```

### setFullscreen(flag)

- **参数**：
    - `{boolean} flag`

- **用法**： 全屏(取消全屏)播放视频

> iOS不支持，iPadOS13后支持

> 推荐iOS端配置上 useWebFullScreen 这个参数。

```js

jessibucaPro.setFullscreen(true)
//
jessibucaPro.setFullscreen(false)
```

### screenshot(filename, format, quality,type)

- **参数**：
    - `{string} filename`
    - `{string} format`
    - `{number} quality`
    - `{string} type`
- **用法**：

截图，调用后弹出下载框保存截图

1. filename: 可选参数, 保存的文件名, 默认 `时间戳`
2. format : 可选参数, 截图的格式，可选`png`或`jpeg`或者`webp` ,默认 `png`
3. quality: 可选参数, 当格式是`jpeg`或者`webp`时，压缩质量，取值0 ~ 1 ,默认 `0.92`
4. type: 可选参数, 可选`download`或者`base64`或者`blob`，默认`download`

> 手机浏览器不一定能保存下来。 建议手机端通过`base64`形式。让用户自主保存。

```js

jessibucaPro.screenshot("test", "png", 0.5)


const base64 = jessibucaPro.screenshot("test", "png", 0.5, 'base64')


const fileBlob = jessibucaPro.screenshot("test", 'blob')

```

### screenshotWatermark(options)（pro）

- **返回值**：`Promise`
- **options 参数**：
    - `{string} filename`
    - `{string} format`
    - `{number} quality`
    - `{string} type`
    -
- **用法**：

截图(支持水印参数)，调用后弹出下载框保存截图

1. filename: 可选参数, 保存的文件名, 默认 `时间戳`
2. format : 可选参数, 截图的格式，可选`png`或`jpeg`或者`webp` ,默认 `png`
3. quality: 可选参数, 当格式是`jpeg`或者`webp`时，压缩质量，取值0 ~ 1 ,默认 `0.92`
4. type: 可选参数, 可选`download`或者`base64`或者`blob`，默认`download`

```js

jessibucaPro.screenshotWatermark({
    filename: 'test',
    text: {
        content: 'jessibuca-pro',
        fontSize: '40',
        color: 'red'
    },
    right: 20,
    top: 40
})


jessibucaPro.screenshotWatermark({
    filename: 'test',
    text: {
        content: 'jessibuca-pro',
        fontSize: '40',
        color: 'red'
    },
    right: 20,
    top: 40,
    type: 'base64'
}).then((base64) => {

})


jessibucaPro.screenshotWatermark({
    image: {
        src: 'data:image/png;base64,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',
        width: 60,
        height: 60
    },
    right: 20,
    top: 20,
    type: `blob`,
}).then((fileBlob) => {

})

```

### startRecord(fileName,fileType)

- **参数**：
    - `{string} filename`
    - `{string} fileType`（废弃）
- **返回值**：`{Promise}`
- **用法**： 开始录制。

1. fileName: 可选，默认时间戳
   ~~2. fileType: 可选，支持webm 和mp4 格式 默认webm~~

```js
jessibucaPro.startRecord('xxx')
```

> 文件类型根据 `recordType` 参数走。

### stopRecordAndSave(type,fileName)

- **用法**： 暂停录制并下载。
- **参数**：
    - `{string} type`
    - `{string} fileName`
- **返回值**：`{Promise}`

1. type: 可选，默认`download`，支持`download`和`blob`
2. fileName: 可选，默认时间戳

```js
jessibucaPro.stopRecordAndSave()
```

```js
jessibucaPro.stopRecordAndSave('', 'fileName')
```

```js
```

```js
jessibucaPro.stopRecordAndSave('blob')
jessibucaPro.once('recordBlob', (fileBlob) => {
    console.log(fileBlob)
})
```

```js
jessibucaPro.stopRecordAndSave('blob').then((fileBlob) => {
    console.log(fileBlob)
})
```

### isPlaying()

- **返回值**：`boolean`
- **用法**： 返回是否正在播放中状态。

```js
var result = jessibucaPro.isPlaying()
console.log(result) // true
```

### isLoading()（pro）

- **返回值**：`boolean`
- **用法**： 返回是否正在加载中状态。

```js
var result = jessibucaPro.isLoading()
console.log(result) // true
```

### isPause()（pro）

- **返回值**：`boolean`
- **用法**： 返回是否正在暂停中状态。

```js
var result = jessibucaPro.isPause()
console.log(result) // true
```

### isPlaybackPause()（pro）

- **返回值**：`boolean`
- **用法**： 返回是否正在回放暂停中状态。

```js
var result = jessibucaPro.isPlaybackPause()
```

### isMute()

- **返回值**：`boolean`
- **用法**： 返回是否静音。

```js
var result = jessibucaPro.isMute()
console.log(result) // true
```

### isRecording()

- **返回值**：`boolean`
- **用法**： 返回是否正在录制。

```js
var result = jessibucaPro.isRecording()
console.log(result) // true
```

### clearBufferDelay() （pro）

- **返回值**：`void`
- **用法**： 手动消除缓冲区数据。

```js
jessibucaPro.clearBufferDelay()
```

### playback(url,options)（pro）

- **参数**：
    - `{string} url`
    - `{object} options` 同 `playbackConfig`配置参数

- **返回**：
    - `{Promise}`
- **用法**： 播放录像流视频

### forward(rate)（pro）

- **参数**：
    - `{number} rate`

- **返回**：
    - `{Promise}`
- **用法**： 快放 1倍，2倍，4倍，8倍,16倍,32倍,支持范围 0.1 - 32

### playbackForward(rate)

等同于 `forward(rate)` 方法

### normal()（pro）

- **返回**：
    - `{Promise}`
- **用法**：快放->恢复

```js
jessibuca.normal().then(() => {
    console.log('normal')
}).catch((err) => {
    console.log(err)
})
```

### playbackNormal()

等同于 `normal` 方法

### playbackPause(isPause)（pro）

- **参数**：
    - `{boolean} isPause` 默认：`false`
- **返回**：
    - `{Promise}`

- **用法**： 录像流暂停，只是停止渲染画面，继续接收流数据，不触发超时机制

如果 `playbackPause(true)` 等同于调用`pause()` 方法

### playbackResume()（pro）

- **返回**：
    - `{Promise}`

- **用法**： 录像流暂停->恢复播放

### updatePlaybackForwardMaxRateDecodeIFrame(rate)（pro）

- **参数**：
    - `{number} rate` 支持范围 1 - 8 整数
- **返回**：
    - `{void}`
      更新TF卡流只解码i帧播放倍率，支持playback()之前调用。

### setPlaybackStartTime(timestamp)（pro）

- **参数**：
    - `{number} timestamp` 时间戳，针对于24H的时间戳，
- **返回**：
    - `{void}`

请求完服务器端seek之后，把seek之后的时间传递给播放器，用于UI上面展示更新之后的时间。

如果是通过点击UI上面的时间，会得到时分秒，然后转换成时间戳`const timestamp = new Date().setHours(hour,min,second,0)`，然后调用`setPlaybackStartTime(timestamp)`方法。

> 会存在一种场景，就是seek之后，流媒体服务器端还有一小戳的seek之前的数据推给前端，因为前端24H UI 上面的时间戳是根据流里面的时间戳做`当对值`得到的。
> 所以如果立马触发`setPlaybackStartTime`方法的话，会就有可能因为还是seek之前的时间戳，等seek之后的时间戳到来的时候，UI上面的时间会直接跳的不对。

解决方案：延迟些时间去触发`setPlaybackStartTime`方法。大概500ms左右。

```js
setTimeout(() => {
    jessibucaPro.setPlaybackStartTime(timestamp)
}, 500)
```


### getPlaybackCurrentRate()（pro）

- **返回**：
    - `{number}`

获取当前TF卡流播放的倍率。

### playbackClearCacheBuffer()（pro）

- **返回**：
    - `{void}`

清除缓存的数据，用于seek之后，清除之前的数据。

### updatePlaybackLocalOneFrameTimestamp(timestamp)（pro）

- **参数**：
    - `{number} timestamp`
- **返回**：
    - `{void}`

更新`localOneFrameTimestamp` 字段

> 当播放器以倍率播放的时候，如果超过了`playbackForwardMaxRateDecodeIFrame`
> 设置的倍率，这个时候只会解码i帧的数据。就会导致`localOneFrameTimestamp`字段不准确。

> 所以当切换倍率超过限制之后，就需要触发这个方法，去更新`localOneFrameTimestamp`字段。

### setNetworkDelayTime(timestamp)（pro）

- **参数**：
    - `{number} timestamp` 单位秒s
- **返回**：
    - `{void}`

设置网络延迟时间，用于播放器计算播放时间，单位秒s

### setStreamQuality(quality)（pro）

- **参数**：
    - `{number} quality`
- **返回**：
    - `{void}`

- **用法**： 设置分辨率 `quality` 必须是`qualityConfig`里面的数据

### setMirrorRotate(mirrorRotate)（pro）

- **参数**：
    - `{string} mirrorRotate`
- **返回**：
    - `{void}`

- **用法**： 设置镜像翻转 `mirrorRotate` ，可选参数有 默认`none`,水平 `level`,垂直 `vertical`

### togglePerformancePanel(flag)（pro）

- **参数**：
    - `{boolean} flag` 是否显示性能面板，如果不写参数，则当做toggle切换
- **返回**：
    - `{void}`

- **用法**： 隐藏/显示 性能面板

### openZoom()（pro）

- **返回**：
    - `{void}`

- **用法**： 打开电子放大

### closeZoom()（pro）

- **返回**：
    - `{void}`

- **用法**： 关闭电子放大

### isZoomOpen()（pro）

- **返回**：
    - `{boolean}`

- **用法**： 当前是否处于电子放大模式下

### expandZoom()（pro）

- **返回**：
    - `{void}`

- **用法**： 电子放大-放大

### narrowZoom()（pro）

- **返回**：
    - `{void}`

- **用法**： 电子放大-缩小

### getCurrentZoomIndex()（pro）

- **返回**：
    - `{number} currentZoom` 当前放大的档位

- **用法**： 获取当前电子放大档位 范围 1~5

### getDecodeType()（pro）

- **返回**：
    - `{string} decodeType` 当前解码类型

返回一个字符串 告知当前解码类型。如果是组合型的话，会用空格分隔。

可能得结果有：`mse`，`wcs`，`offscreen`，`wasm`，`simd`，`webrtc`，`hls`

### getDemuxType()（pro）

- **返回**：
    - `{string} demuxType` 当前解复用类型

返回一个字符串 告知当前解复用类型。

可能得结果有：`flv`，`m7s`，`hls`，`webrtc`，`webTransport`，`nakedFlow`

### getRenderType()（pro）

- **返回**：
    - `{string} renderType` 当前渲染组件 包含 `video`,`canvas`

返回一个字符串 告知当前渲染组件。

### getPlayingTimestamp()（pro）

- **返回**：
    - `{number} pTs` 播放时间戳 单位 秒

返回一个数字，告知当前播放时间戳。

> 等同于 stats 事件中的 `pTs` 字段

### getStatus()（pro）

- **返回**：
    - `{string} status` 当前播放器状态 包含状态有 `playing` `paused` `loading`

返回一个字符串，告知当前播放器状态。

### getPlayType()（pro）

- **返回**：
    - `{string} playType` 当前播放器类型 包含类型有 `player` `playbackTF`

返回一个字符串，告知当前播放器类型。

### getAudioEngineType()（pro）

- **返回**：`{string}` 返回当前音频引擎类型

返回的结果有 `audio`， `worklet`， `script`， `active`

### setNakedFlowFps(fps)（pro）

- **用法**： 设置裸流播放的fps
- **参数**：`{number} fps` fps值 1~100
- **返回**：`{promise}`

### updateDebugLevel(level)（pro）

- **用法**： 设置调试等级
- **参数**：`{number} level` `debug`和`warn`
- **返回**：`{void}`

### faceDetectOpen()（pro）

- **用法**： 打开人脸检测
- **返回**：`{void}`
- **注意**： 人脸检测功能需要额外加载js和wasm文件

### faceDetectClose()（pro）

- **用法**： 关闭人脸检测
- **返回**：`{void}`
- **注意**： 人脸检测功能需要额外加载js和wasm文件

### updateFullscreenWatermark(config)（pro）

- **用法**： 更新全屏水印
- **返回**：`{void}`
- **注意**： config 等同于 `fullscreenWatermarkConfig` 配置

### removeFullscreenWatermark()（pro）

- **用法**： 移除全屏水印
- **返回**：`{void}`

### sendWebsocketMessage(message)（pro）

- **用法**： 发送websocket消息
- **参数**：`{*} message` 消息内容
- **返回**：`{void}`

### addContentToCanvas(contentList)

- **用法**： 给canvas（video） 添加内容
- **返回**：`{void}`
- **参数**：`{Array} contentList` 内容列表
    - `{string} type` 内容类型 支持 `text`,`rect`
    - `{string} text` 文本内容(type 为 text 有效)
    - `{number} fontSize` 字体大小(type 为 text 有效)
    - `{number} width` 宽度(type 为 rect 有效)
    - `{number} height` 高度(type 为 rect 有效)
    - `{number} lineWidth` 边框宽度(type 为 rect 有效)
    - `{string} color` 颜色
    - `{number} x` x坐标
    - `{number} y` y坐标

> ~~目前仅支持 在硬解码模式下，在使用 canvas 渲染。~~

> 现在 `软解码`，`硬解码`, `canvas渲染`，`video渲染` 都支持。

### clearContentToCanvas()

- **用法**： 清空canvas 额外添加的内容
- **返回**：`{void}`

### setControlHtml(html)

- **用法**： 设置控制面板的html
- **返回**：`{void}`


### clearControlHtml()

- **用法**： 清空控制面板的html
- **返回**：`{void}`


### updateWatermark(config)

- **用法**： 更新水印
- **参数**：`{object | array} config` 水印配置
- **返回**：`{void}`

水印参数支持 `object` 和 `array` 支持配置多个水印数据。

### removeWatermark()

- **用法**： 移除水印
- **返回**：`{void}`


### on(event, callback)

- **参数**：
    - `{string} event`
    - `{function} callback`
- **用法**： 监听方法

```js

jessibucaPro.on("load", function () {
    console.log('load')
})
```

## 事件

### load

监听 jessibucaPro 初始化事件。

```js

jessibucaPro.on("load", function () {
    console.log('load')
})
```

### timeUpdate

视频播放持续时间，单位ms

```js
jessibucaPro.on('timeUpdate', function (ts) {
    console.log('timeUpdate', ts);
})
```

### videoInfo

当解析出视频信息时回调，2个回调参数

1. width：视频宽
2. height：视频高

```js

jessibucaPro.on("videoInfo", function (data) {
    console.log('width:', data.width, 'height:', data.width)
})
```

### audioInfo

当解析出音频信息时回调，2个回调参数

1. numOfChannels：声频通道
2. sampleRate 采样率

```js

jessibucaPro.on("audioInfo", function (data) {
    console.log('numOfChannels:', data.numOfChannels, 'sampleRate', data.sampleRate)
})
```

### error

错误信息

目前已有的错误信息：

1. jessibucaPro.ERROR.playError ;播放错误，url 为空的时候，调用play方法
2. jessibucaPro.ERROR.fetchError ;http 请求失败
3. jessibucaPro.ERROR.websocketError; websocket 请求失败
4. jessibucaPro.ERROR.webcodecsH265NotSupport; webcodecs 解码 h265 失败
5. jessibucaPro.ERROR.mediaSourceH265NotSupport; mediaSource 解码 h265 失败
6. jessibucaPro.ERROR.wasmDecodeError ; wasm 解码失败
7. jessibucaPro.ERROR.hlsError ; hls 播放失败
8. jessibucaPro.ERROR.wasmDecodeVideoNoResponseError ; wasm 解码视频没有响应

```js

jessibucaPro.on("error", function (error) {
    if (error === jessibucaPro.ERROR.fetchError) {
        //
    } else if (error === jessibucaPro.ERROR.webcodecsH265NotSupport) {
        //
    }
    console.log('error:', error)
})
```

### fetchError

播放器播放的时候，http(s) 协议请求失败

```js

jessibucaPro.on("fetchError", function (data) {
    console.log('fetchError:', data)
})
```

### websocketError

播放器播放的时候，websocket 请求失败

```js
jessibucaPro.on("websocketError", function (data) {
    console.log('websocketError:', data)
})
```

### kBps

当前网速， 单位KB 每秒1次,

```js

jessibucaPro.on("kBps", function (data) {
    console.log('kBps:', data)
})
```

### start

渲染开始

> 如果触发重播机制，也会触发这个方法。


> 如果需要在start 之后调用截图方法，需要写一个延迟方法去执行。因为代码中无法感应到画面被渲染出来了。

```js

jessibucaPro.on("start", function () {
    console.log('start render')

    // 如果有截图需求。需要延迟一下，因为代码中无法感应到画面被渲染出来了。
    // 1s 之后截图
    setTimeout(function () {
        jessibuca.screenshot('xxx')
    }, 1 * 1000)
})

// 只想监听一次
jessibucaPro.once("start", function () {
    console.log('start render')
})
```

### timeout

当设定的超时时间内无数据返回,则回调

1. jessibucaPro.TIMEOUT.loadingTimeout ; 同loadingTimeout
2. jessibucaPro.TIMEOUT.delayTimeout ; 同delayTimeout

```js

jessibucaPro.on("timeout", function (error) {
    console.log('timeout:', error)
})
```

### loadingTimeout

当play()的时候，如果没有数据返回，则回调

```js

jessibucaPro.on("loadingTimeout", function () {
    console.log('timeout')
})
```

### loadingTimeoutRetryEnd

当loadingTimeout事件触发后，如果超过重试次数，则回调

```js

jessibucaPro.on("loadingTimeoutRetryEnd", function () {
    console.log('loadingTimeoutRetryEnd')
})
```

### delayTimeout

当播放过程中，如果超过timeout之后没有数据渲染，则抛出异常。

```js

jessibucaPro.on("delayTimeout", function () {
    console.log('timeout')
})
```

### delayTimeoutRetryEnd

当delayTimeout事件触发后，如果超过重试次数，则回调

```js
jessibucaPro.on("delayTimeoutRetryEnd", function () {
    console.log('delayTimeoutRetryEnd')
})
```

### playFailedAndPaused

当播放失败后，没有触发任何的重播机制，就会触发这个事件。

```js
jessibucaPro.on("playFailedAndPaused", function () {
    console.log('playFailedAndPaused')
})
```

### fullscreen

当前是否全屏

```js
jessibucaPro.on("fullscreen", function (flag) {
    console.log('is fullscreen', flag)
})
```

### webFullscreen

当前是否全屏(H5)

```js
jessibucaPro.on("webFullscreen", function (flag) {
    console.log('is webFullscreen', flag)
})
```

```
### play

触发播放事件

```js
jessibucaPro.on("play", function (flag) {
    console.log('play')
})
```

### play

触发播放事件

```js
jessibucaPro.on("play", function (flag) {
    console.log('play')
})
```

### pause

触发暂停事件

```js
jessibucaPro.on("pause", function (flag) {
    console.log('pause')
})
```

### mute

触发声音事件，返回boolean值

```js
jessibucaPro.on("mute", function (flag) {
    console.log('is mute', flag)
})
```

### stats

流状态统计，流开始播放后回调，每秒1次。

- fps: 视频帧率(fps),
- maxFps:视频峰值帧率(fps),
- dfps:解码帧率(fps)
- abps: 音频码率(bit)
- vbps: 视频码率(bit)
- audioBuffer:音频缓冲帧,
- audioBuffer: 音频缓冲区PCM 数据
- demuxBuffer: 视频待解码帧(直播流)
- flvBuffer: 待解封装数据(byte)
- isDropping: 是否丢帧
- playbackVideoBuffer：视频待渲染(直播流)
- playbackCacheDataDuration：缓存时长(ms)(录像流)
- audioDemuxBuffer：音频待解码帧(直播流)
- mseDelay:MSE缓冲时长(ms)
- mseDecodeDiffTimes：MSE解码间隔(ms)
- mseTs：MSE解码时间(ms)
- mseDecodePlaybackRate：MSE播放模式(1:正常；>1 加速)
- wcsDecodeDiffTimes：WCS解码间隔(ms)
- hlsDelay：HLS缓冲时长(ms)
- hlsDecodePlaybackRate：HLS播放模式(1:正常；>1 加速)
- netBuf:网络延迟(ms)
- buf: 缓存时长(ms) ,
- pushLatestDelay:最新缓冲时长(ms)
- ts:视频显示时间(ms)
- audioTs:音频显示时间(ms)
- dts:视频解码时间(ms)
- delayTs:解码前-解码后延迟(ms)
- totalDelayTs:总延迟(网络+解码)(ms)
- pTs:当前播放器的播放时间(播放器自己计算的时间)，每次从0开始，单位毫秒（pro）

> 在音频有的情况下： 可以通过 ts - audioTs 来计算出：音视频同步时间戳(ms)

```js
jessibucaPro.on("stats", function (s) {
    console.log("stats is", s)
})
```

> 如果是TF卡流（录像流），建议使用`ts`字段，这个字段是数据流里面的

### performance

渲染性能统计，流开始播放后回调，每秒1次。

- 0: 表示卡顿
- 1: 表示流畅
- 2: 表示非常流程

```js
jessibucaPro.on("performance", function (performance) {
    console.log("performance is", performance)
})
```

### recordStart

录制开始的事件

```js
jessibucaPro.on("recordStart", function () {
    console.log("record start")
})
```

### recordEnd

录制结束的事件

```js
jessibucaPro.on("recordEnd", function () {
    console.log("record end")
})
```

### recordBlob

调用录制结束，接收的二进制内容。

```js
jessibucaPro.stopRecordAndSave('blob')
jessibucaPro.once('recordBlob', (fileBlob) => {
    console.log(fileBlob)
})
```
等同于：
```js
jessibucaPro.stopRecordAndSave('blob').then((fileBlob) => {
    console.log(fileBlob)
})
```

### recordingTimestamp

录制的时候，返回的录制时长，1s一次

```js
jessibucaPro.on("recordingTimestamp", function (timestamp) {
    console.log("recordingTimestamp is", timestamp)
})
```

### playToRenderTimes

监听调用play方法 经过 初始化-> 网络请求-> 解封装 -> 解码 -> 渲染 一系列过程的时间消耗

```js
jessibucaPro.on("playToRenderTimes", function (times) {
    console.log("playToRenderTimes is", times)
})
```

数据结构如下。

```
{
    playInitStart: '', //1
    playStart: '', // 2
    streamStart: '', //3
    streamResponse: '', // 4
    demuxStart: '', // 5
    decodeStart: '', // 6
    videoStart: '', // 7
    playTimestamp: '',// playStart- playInitStart
    streamTimestamp: '',// streamStart - playStart
    streamResponseTimestamp: '',// streamResponse - streamStart
    demuxTimestamp: '', // demuxStart - streamResponse
    decodeTimestamp: '', // decodeStart - demuxStart
    videoTimestamp: '',// videoStart - decodeStart
    allTimestamp: '' // videoStart - playInitStart
}

```

### playbackSeek（pro）

当点击播放器上面的时间进度条，响应的事件

数据结构如下。

```json
{
    "hour": 1,
    "min": 2,
    "second": 0
}

```

### playbackStats（pro）

录像流的 stats数据,1s回调一次

```json
{
    "fps": "",
    "rate": "",
    "start": "",
    "end": "",
    "timestamp": "",
    "dataTimestamp": "",
    "audioBufferSize": "",
    "videoBufferSize": "",
    "ts": ""
}
```

### playbackTimestamp（pro）

录像流的当前播放的时间,1s回调一次

```json
{
    "hour": "",
    "min": "",
    "second": "",
    "ts": ""
}
```

### playbackPauseOrResume(pro)

录像流的ui配置了 playbackPause 方法之后，当触发 playbackPause方法，会触发事件，方便业务层做与服务器端通讯

```js
jessibucaPro.on('playbackPauseOrResume', (value) => {
    if (value === true) {
        // pause 播放 -> 暂停
    } else {
        //  resume 暂停 -> 播放
    }
})
```

### playbackPreRateChange(pro)

录像流的ui配置了`rateConfig` 之后 ，当在ui上面选择了倍率之后，会触发`playbackPreRateChange`事件，方便业务层做与服务器端通讯。

```js

jessibucaPro.on('playbackPreRateChange', (value) => {
    // value 为当前的倍速
    // 可以与服务器端发送倍率的请求。
    // 然后更新播放器的倍率显示
})
```

### playbackRateChange(pro)

当播放器的倍率发生变化的时候，会触发`playbackRateChange`事件。

### ptz（pro）

回调ptz 操作的方向 包含`up`，`right`,`down`,`left`,`stop`,`zoomExpand`,`zoomNarrow`。

### streamQualityChange（pro）

切换分辨率的事件回调

### close(pro)

播放器被销毁的时候触发的事件
> 由于 close 事件必须先抛出，然后执行的内部的destroy 方法(不然内部执行执行接触事件绑定，外部on方法就会失效掉的)
> ，所以需要在监听事件内部通过 setTimeout 方法之后（让destroy执行成功）再执行后续的业务代码，例如关闭流连接啥的等等

> 如果不在setTimeout之后执行，会出现意想不到的事情

```js
jessibucaPro.on('close', () => {
    setTimeout(() => {
        // do some things
    }, 10)
})
```

### zooming(pro)

电子放大是否开启状态

```js
jessibucaPro.on('zooming', (flag) => {
    console.log('zooming status is', flag);
})
```

### talkStreamError（pro）

语音websocket 接口报 `error` 错误

```js
jessibucaPro.on('talkStreamError', (error) => {
    console.log('talkStreamError is', error);
})
```

### talkStreamClose（pro）

语音websocket 接口报 `close` 错误

```js
jessibucaPro.on('talkStreamClose', (error) => {
    console.log('talkStreamClose is', error);
})
```

### talkStreamInactive（pro）

语音设备的流突然断开了（把麦克风直接给拔掉了等情况，导致流断了）

```js
jessibucaPro.on('talkStreamInactive', (error) => {
    console.log('talkStreamInactive is', error);
})
```

### crashLog（pro）

触发播放器重播的时候，会抛出错误信息，方便业务层做错误上报

主要收集的数据有

- url： 播放地址
- type: 错误的类型
- error： 错误信息
- playType ： 播放类型
- demuxType：解封装类型
- decodeType：解码类型
- renderType: 渲染类型
- videoInfo : 视频信息 {width,height,encType}
- audioInfo : 音频信息 {encType,channels,sampleRate}
- audioEngine ：音频引擎
- allTimes ：播放时长，单位秒

```js
jessibucaPro.on('crashLog', (data) => {
    console.log('crashLog is', data);
})
```

### focus（pro）

播放器实例获取焦点（点击和右键）的时候触发的事件。

```js
jessibucaPro.on('focus', () => {
    // do something
})
```

### blur（pro）

播放器实例失去焦点的时候触发的事件。

```js
jessibucaPro.on('blur', () => {
    // do something
})
```

### visibilityHiddenTimeout（pro）

监听窗口不可见超时时间

```js
jessibucaPro.on('visibilityHiddenTimeout', () => {
    // do something
})
```

### visibilityChange（pro）
窗口可见性（隐藏/显示）变化的时候触发

```js

jessibucaPro.on('visibilityChange', (value) => {
    if (value === true) {
        // 窗口显示
    } else {
        // 窗口隐藏
    }
})
```


### websocketOpen（pro）

> 该事件只有在使用websocket协议的时候才会触发

监听websocket连接成功（open）事件

```js
jessibucaPro.on('websocketOpen', () => {
    // do something
})
```


### websocketClose（pro）

> 该事件只有在使用websocket协议的时候才会触发

监听websocket关闭（close）事件

```js
jessibucaPro.on('websocketClose', () => {
    // do something
})
```


### audioResumeState（pro）

当尝试做声音恢复的时候，会触发该事件，方便业务层做相关的处理

回调参数有：
```
{
    state:' running | suspended',
    isRunning: false|true,
}
```

`isRunning` 为`true` 或者检查 `state` 的值为`running`的时候，说明声音恢复成功了

```js
jessibucaPro.on('audioResumeState', (value) => {
    if (value.isRunning === true || value.state === 'running') {
        // do something
    }
})
```


### networkDelayTimeout（pro）

当网络延迟超过`networkDelay`的时候，会触发该事件，方便业务层做相关的处理。

> 如果配置了`networkDelayTimeoutReplay`，则会自动重播。 事件也会抛出。

> 只要网络延迟超过`networkDelay`，就会触发该事件（频繁），所以建议使用`once`方法监听一次即可。

```js
// 推荐once 监听一次即可。
jessibucaPro.once('networkDelayTimeout', () => {
    // do some things
})
```


### aiObjectDetectorInfo（pro）

当开启了ai物体检测功能的时候，会触发该事件，方便业务层做相关的处理。

> 该事件只有在开启了ai物体检测功能的时候才会触发

```js
jessibucaPro.on('aiObjectDetectorInfo', (data) => {
    console.log('aiObjectDetectorInfo is', data);
})
```


