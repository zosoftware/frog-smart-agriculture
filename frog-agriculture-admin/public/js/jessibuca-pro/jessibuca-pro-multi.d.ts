import JessibucaPro from "./jessibuca-pro";

declare namespace JessibucaProMulti {

    /** 事件 */
    enum EVENTS {
        /**  */
        load = 'load',
        /**  */
        timeUpdate = 'timeUpdate',
        /**  */
        videoInfo = 'videoInfo',

        audioInfo = 'audioInfo',

        error = 'error',

        fetchError = 'fetchError',

        websocketError = 'websocketError',

        kBps = 'kBps',

        start = 'start',

        timeout = 'timeout',

        loadingTimeout = 'loadingTimeout',

        loadingTimeoutRetryEnd = 'loadingTimeoutRetryEnd',

        delayTimeout = 'delayTimeout',

        delayTimeoutRetryEnd = 'delayTimeoutRetryEnd',

        playFailedAndPaused = 'playFailedAndPaused',

        fullscreen = 'fullscreen',

        play = 'play',

        pause = 'pause',

        mute = 'mute',

        stats = 'stats',

        performance = 'performance',

        recordingTimestamp = 'recordingTimestamp',

        recordStart = 'recordStart',

        recordEnd = 'recordEnd',

        recordBlob = 'recordBlob',

        playToRenderTimes = 'playToRenderTimes',

        playbackSeek = 'playbackSeek',

        playbackStats = 'playbackStats',

        playbackTimestamp = 'playbackTimestamp',

        playbackPauseOrResume = 'playbackPauseOrResume',

        playbackPreRateChange = 'playbackPreRateChange',

        playbackRateChange = 'playbackRateChange',

        ptz = 'ptz',

        streamQualityChange = 'streamQualityChange',

        zooming = 'zooming',

        crashLog = 'crashLog',

        focus = 'focus',

        blur = 'blur',

        visibilityHiddenTimeout = 'visibilityHiddenTimeout',

        visibilityChange = 'visibilityChange',

        websocketOpen = 'websocketOpen',

        websocketClose = 'websocketClose',

        audioResumeState = 'audioResumeState',

        networkDelayTimeout = 'networkDelayTimeout',

        close = 'close'

    }

    /** 错误信息 */
    enum ERROR {
        /** 播放错误，url 为空的时候，调用 play 方法 */
        playError = 'playError',
        /** http 请求失败 */
        fetchError = 'fetchError',
        /** websocket 请求失败 */
        websocketError = 'websocketError',
        /** webcodecs 解码 h265 失败 */
        webcodecsH265NotSupport = 'webcodecsH265NotSupport',

        webcodecsDecodeError = 'webcodecsDecodeError',

        webcodecsWidthOrHeightChange = 'webcodecsWidthOrHeightChange',

        /** mediaSource 解码 h265 失败 */
        mediaSourceH265NotSupport = 'mediaSourceH265NotSupport',

        mediaSourceDecoderConfigurationError = 'mediaSourceDecoderConfigurationError',

        mediaSourceFull = 'mediaSourceFull',

        mseSourceBufferError = 'mseSourceBufferError',

        mediaSourceAppendBufferError = 'mediaSourceAppendBufferError',

        mediaSourceBufferListLarge = 'mediaSourceBufferListLarge',

        mediaSourceAppendBufferEndTimeout = 'mediaSourceAppendBufferEndTimeout',
        /** wasm 解码失败 */
        wasmDecodeError = 'wasmDecodeError',

        wasmDecodeVideoNoResponseError = 'wasmDecodeVideoNoResponseError',

        hlsError = 'hlsError',

        webrtcError = 'webrtcError',

        webglAlignmentError = 'webglAlignmentError',


        flvDemuxBufferSizeTooLarge = 'flvDemuxBufferSizeTooLarge'
    }

    /** 多画面事件 */
    enum MULTI_EVENTS {
        dblSelected = 'multiDblSelected',
        selected = 'multiSelected',
        mouseOver = 'multiMouseover',
        mouseOut = 'multiMouseout',
        mouseUp = 'multiMouseup',
    }

    interface Config {
        /**
         * 播放器容器
         * *  若为 string ，则底层调用的是 document.getElementById('id')
         * */
        container: HTMLElement | string;

        /**
         * 最大分屏数,默认值4
         */
        maxSplit?: number,

        /**
         * 当前分屏数,默认值1
         */
        split?: number,

        /**
         * 多屏样式
         * *  默认值：{
         *          border:'#343434',
         *          borderSelect:'#FFCC00',
         *          background:'#000'
         *          }
         */
        style?: {
            border?: string,
            borderSelect?: string,
            background?: string,
        }

        /**
         * 是否支持双击单个播放窗口，使之全屏到container容器大小。
         */
        supportDblclickContainerFullscreen?:boolean;

        /**
         * 设置最大缓冲时长，单位秒，播放器会自动消除延迟
         */
        videoBuffer?: number;

        /**
         * 设置丢帧最大延迟，单位秒，当延迟超过（videoBufferDelay + videoBuffer）之后，就会触发丢帧机制
         */
        videoBufferDelay?: number;
        /**
         * 设置网络延迟，单位秒，当网络延迟超过这个值之后，会触发'networkDelayTimeout' 事件。
         */
        networkDelay?: number;
        /**
         * worker地址
         * *  默认引用的是根目录下面的decoder.js文件 ，decoder-pro.js 与 decoder-pro.wasm文件必须是放在同一个目录下面。 */
        decoder?: string;
        /**
         * 是否不使用离屏模式（提升渲染能力）
         * 废弃：暂不支持配置
         */
        forceNoOffscreen?: boolean;
        /**
         * 是否开启当页面的'visibilityState'变为'hidden'的时候，自动暂停播放。
         */
        hiddenAutoPause?: boolean;
        /**
         * 是否有音频，如果设置`false`，则不对音频数据解码，提升性能。
         */
        hasAudio?: boolean;

        /**
         * 是否有视频，如果设置`false`，则不对视频数据解码。
         */
        hasVideo?: boolean;
        /**
         * 设置旋转角度，只支持，0(默认)，90，180，270 三个值
         */
        rotate?: boolean;

        /**
         * 镜像翻转：默认`none`, 水平 `level`,垂直 `vertical`
         */
        mirrorRotate?: string;

        /**
         * 1. 当为`true`的时候：视频画面做等比缩放后,高或宽对齐canvas区域,画面不被拉伸,但有黑边。 等同于 `setScaleMode(1)`
         * 2. 当为`false`的时候：视频画面完全填充canvas区域,画面会被拉伸。等同于 `setScaleMode(0)`
         */
        isResize?: boolean;
        /**
         * 1. 当为`true`的时候：视频画面做等比缩放后,完全填充canvas区域,画面不被拉伸,没有黑边,但画面显示不全。等同于 `setScaleMode(2)`
         */
        isFullResize?: boolean;
        /**
         * 1. 当为`true`的时候：ws协议不检验是否以.flv为依据，进行协议解析。
         */
        isFlv?: boolean;
        /**
         * 是否是裸流（264、265）
         */
        isNakedFlow?: boolean;
        /**
         * 是否开启控制台日志
         */
        debug?: boolean;

        /**
         * 日志等级：支持 'warn','debug'
         */
        debugLevel?: string;
        /**
         * 是否多实例播放。
         * 如果开启了，控制台打印的日志就会有uuid区分多实例。
         */
        isMulti?: boolean;

        /**
         * 1. 设置超时时长, 单位秒
         * 2. 在连接成功之前(loading)和播放中途(heart),如果超过设定时长无数据返回,则回调timeout事件
         */
        timeout?: number;
        /**
         * 1. 设置超时时长, 单位秒
         * 2. 在连接成功之前,如果超过设定时长无数据返回,则回调timeout事件
         */
        heartTimeout?: number;
        /**
         * 1. 设置超时时长, 单位秒
         * 2. 在连接成功之前,如果超过设定时长无数据返回,则回调timeout事件
         */
        loadingTimeout?: number;

        /**
         * 页面窗口不可见超时时间，单位秒
         */
        pageVisibilityHiddenTimeout?: number;

        /**
         * 是否支持屏幕的双击事件，触发全屏，取消全屏事件
         */
        supportDblclickFullscreen?: boolean;
        /**
         * 是否显示网
         */
        showBandwidth?: boolean;

        /**
         * 是否显示性能面板
         */
        showPerformance?: boolean;
        /**
         * 配置操作按钮
         */
        operateBtns?: {
            /** 是否显示全屏按钮 */
            fullscreen?: boolean;
            /** 是否显示截图按钮 */
            screenshot?: boolean;
            /** 是否显示播放暂停按钮 */
            play?: boolean;
            /** 是否显示声音按钮 */
            audio?: boolean;
            /** 是否显示录制按 */
            record?: boolean;

            ptz?: boolean;
            quality?: boolean;
            zoom?: boolean;
            close?: boolean;
            scale?: boolean;
            performance?: boolean;
            face?: boolean;
            fullscreenFn?: Function | null;
            fullscreenExitFn?: Function | null;
            screenshotFn?: Function | null;
            playFn?: Function | null;
            pauseFn?: Function | null;
            recordFn?: Function | null;
            recordStopFn?: Function | null;
        };

        extendOperateBtns?: [
            {}
        ];

        contextmenuBtns?: [
            {}
        ];

        watermarkConfig?: {}

        /**
         * 底部UI控制栏是否自动隐藏（仅pc端）
         */
        controlAutoHide?: boolean;

        /**
         * 加载的时候是否显示加载中icon
         */
        loadingIcon?: boolean;

        /**
         * 录像流配置
         */
        playbackConfig?: {
            /**
             * 底部UI 24小时高亮时间端，如在该时间段内，可触发点击事件
             */
            playList?: [
                {
                    // 开始时间戳 例如 1653841634 或者 16538416340000
                    start: number,
                    // 结束时间戳  例如 1653843420  或者 16538434200000
                    end: number
                }
            ],
            /**
             * 渲染FPS
             */
            fps?: number,
            /**
             * 是否显示底部UI 24小时控制条
             */
            showControl?: boolean,
            /**
             * 是否显示倍率按钮
             */
            showRateBtn?: boolean,
            /**
             * 倍率按钮配置
             * 支持[{label:'正常',value:1},{label:'2倍',value:2}]
             */
            rateConfig?: [
                {
                    label?: string,
                    value?: number
                }
            ],
            /**
             * rfs渲染时，是否在解码前缓存数据
             */
            isCacheBeforeDecodeForFpsRender?: boolean,
            /**
             * ui上面是否使用 playbackPause 方法
             */
            uiUsePlaybackPause?: boolean,
            /**
             * 是否使用固定的fps渲染，如果设置的fps小于流推过来的，会造成内存堆积甚至溢出
             */
            isUseFpsRender?: boolean,
            /**
             * 是否使用本地时间来计算playback时间
             */
            isUseLocalCalculateTime?: boolean,
            /**
             * 一帧 40ms,
             */
            localOneFrameTimestamp?: number
        }

        /**
         * 分辨率设置
         * 支持 ['高清','超清','4K'] 等
         */
        qualityConfig?: [];

        /**
         * 默认分辨率，必须得在 qualityConfig 数组里面匹配到
         */
        defaultStreamQuality?: string

        /**
         * 开启屏幕常亮，在手机浏览器上, canvas标签渲染视频并不会像video标签那样保持屏幕常亮
         */
        keepScreenOn?: boolean;
        /**
         * 是否开启声音，默认是关闭声音播放的
         */
        isNotMute?: boolean;
        /**
         * 加载过程中文案
         */
        loadingText?: string;
        /**
         * 背景图片
         */
        background?: string;
        /**
         * 是否开启MediaSource硬解码
         */
        useMSE?: boolean;
        /**
         * 是否开启Webcodecs硬解码
         * *  视频编码只支持H.264视频 (需在chrome 94版本以上，需要https或者localhost环境)
         * *  支持 forceNoOffscreen 为 false （开启离屏渲染)
         * */
        useWCS?: boolean;

        /**
         * 是否使用wasm simd 解码
         * *
         */
        useSIMD?: boolean;
        /**
         * 是否开启键盘快捷键
         * 目前支持的键盘快捷键有：esc -> 退出全屏；arrowUp -> 声音增加；arrowDown -> 声音减少；
         */
        hotKey?: boolean;
        /**
         *  在使用MSE或者Webcodecs 播放H265的时候，是否自动降级到wasm模式。
         *  设置为false 则直接关闭播放，抛出Error 异常，设置为true 则会自动切换成wasm模式播放。
         */
        autoWasm?: boolean;
        /**
         * heartTimeout 心跳超时之后自动再播放,不再抛出异常，而直接重新播放视频地址。
         */
        heartTimeoutReplay?: boolean,
        /**
         * heartTimeoutReplay 从试次数，超过之后，不再自动播放
         */
        heartTimeoutReplayTimes?: number,
        /**
         * loadingTimeout loading之后自动再播放,不再抛出异常，而直接重新播放视频地址。
         */
        loadingTimeoutReplay?: boolean,
        /**
         * heartTimeoutReplay 从试次数，超过之后，不再自动播放
         */
        loadingTimeoutReplayTimes?: number
        /**
         * wasm解码报错之后，不再抛出异常，而是直接重新播放视频地址。
         */
        wasmDecodeErrorReplay?: boolean,

        /**
         * 播放过程中，超时触发的重播，是否使用最后一帧画面当背景展示。(历史参数，推荐 replayUseLastFrameShow)
         */
        heartTimeoutReplayUseLastFrameShow?: boolean;

        /**
         * 播放过程中，超时触发的重播，是否使用最后一帧画面当背景展示。
         */
        replayUseLastFrameShow?: boolean;
        /**
         * https://github.com/langhuihui/jessibuca/issues/152 解决方案
         * 例如：WebGL图像预处理默认每次取4字节的数据，但是540x960分辨率下的U、V分量宽度是540/2=270不能被4整除，导致绿屏。
         */
        openWebglAlignment?: boolean,

        /**
         * wcs 是否使用 video 渲染
         * * 推荐使用 `useVideoRender` 参数替代
         */
        wcsUseVideoRender?: boolean,

        /**
         * wasm 用video标签渲染
         * * 推荐使用 `useVideoRender` 参数替代
         */
        wasmUseVideoRender?: boolean,

        /**
         * mse 用canvas标签渲染
         * * 推荐使用 `useVideoRender` 参数替代
         */
        mseUseCanvasRender?: boolean,

        /**
         * hls 用canvas标签渲染
         * * 推荐使用 `useVideoRender` 参数替代
         */
        hlsUseCanvasRender?: boolean,


        /**
         * 使用video标签渲染
         */
        useVideoRender?: boolean,

        /**
         * 使用canvas渲染
         */
        useCanvasRender?: boolean,

        /**
         * 网络延迟重连
         */
        networkDelayTimeoutReplay?: boolean,
        /**
         * 检查第一帧是否是I帧
         */
        checkFirstIFrame?: boolean,

        /**
         *
         */
        audioEngine?: string,

        /**
         * 是否显示录制的UI（在没有配置底部的录制按钮的情况下），
         * * 如果为true，则会显示录制的UI，
         * * 如果为false，则不显示录制的UI。
         */
        isShowRecordingUI?: boolean,

        /**
         * 是否显示缩放的UI（在没有配置底部的缩放按钮的情况下），
         * * 如果为true，则会显示缩放的UI，
         * * 如果为false，则不显示缩放的UI。
         */
        isShowZoomingUI?: boolean,

        /**
         * 使用人脸检测
         * * 依赖引入人脸检测模块(js,wasm)
         */
        useFaceDetector?: boolean,

        /**
         * PTZ 点击类型
         * * 支持： `click` 点击。 会在点击之后，立即触发ptz arrow事件,300ms之后触发stop事件
         * * 支持：`mouseDownAndUp` 鼠标按下和松开。会在鼠标按下，立即触发ptz arrow事件，鼠标松开，触发stop事件。
         */
        ptzClickType?: string,

        /**
         * 微信安卓音频播放块大小
         */
        weiXinInAndroidAudioBufferSize?: number,
        /**
         * 是否加密流
         */
        isCrypto?: boolean,


        /**
         * 用web全屏(旋转播放器90度)(只会在移动端生效)
         */
        useWebFullScreen?: boolean,

        /**
         * 自定义控制栏的html
         */
        controlHtml?: string,

        /**
         * 是否显示ptz zoom 按钮
         */
        ptzZoomShow?: boolean,
    }
}


declare class JessibucaProMulti {

    constructor(config?: JessibucaProMulti.Config);

    /**
     * 销毁单个播放窗口播放器实例
     * @param windowIndex
     */
    destroySingle(windowIndex?: number): void;

    /**
     * 整个播放全屏
     * @param flag
     */
    setFullscreenMulti(flag: boolean): void;

    /**
     * 选择某个播放窗口
     * @param windowIndex
     */
    selectWindow(windowIndex: number): void;

    /**
     * 切换分屏数
     * @param split
     */
    arrangeWindow(split: number|string): void;

    /**
     * 获取当前分屏数
     */
    getCurrentSplit(): number;

    /**
     * 获取当前选中的播放窗口下标
     */
    getSelectedWindowIndex(): number;

    /**
     * 是否开启控制台调试打印
     @example
     // 开启
     jessibuca.setDebug(true)
     // 关闭
     jessibuca.setDebug(false)
     */
    setDebug(flag: boolean, windowIndex?: number): void;


    /**
     * 获取配置参数信息
     */
    getOption(windowIndex?: number): JessibucaProMulti.Config;

    /**
     * 静音
     @example
     jessibuca.mute()
     */
    mute(windowIndex?: number): void;

    /**
     * 取消静音
     @example
     jessibuca.cancelMute()
     */
    cancelMute(windowIndex?: number): void;

    /**
     * 留给上层用户操作来触发音频恢复的方法。
     *
     * iPhone，chrome等要求自动播放时，音频必须静音，需要由一个真实的用户交互操作来恢复，不能使用代码。
     *
     * https://developers.google.com/web/updates/2017/09/autoplay-policy-changes
     */
    audioResume(windowIndex?: number): void;

    /**
     *
     * 设置超时时长, 单位秒
     * 在连接成功之前和播放中途,如果超过设定时长无数据返回,则回调timeout事件

     @example
     jessibuca.setTimeout(10)

     jessibuca.on('timeout',function(){
        //
    });
     */
    setTimeout(number: number, windowIndex?: number): void;

    /**
     * @param mode
     *      0 视频画面完全填充canvas区域,画面会被拉伸  等同于参数 `isResize` 为false
     *
     *      1 视频画面做等比缩放后,高或宽对齐canvas区域,画面不被拉伸,但有黑边 等同于参数 `isResize` 为true
     *
     *      2 视频画面做等比缩放后,完全填充canvas区域,画面不被拉伸,没有黑边,但画面显示不全 等同于参数 `isFullResize` 为true
     @example
     jessibuca.setScaleMode(0)

     jessibuca.setScaleMode(1)

     jessibuca.setScaleMode(2)
     */
    setScaleMode(mode: number, windowIndex?: number): void;

    /**
     * 暂停播放
     */
    pause(isClear: boolean, windowIndex?: number): Promise<void>;

    /**
     * 关闭视频,不释放底层资源
     @example
     jessibuca.close();
     */
    close(windowIndex?: number): void;

    /**
     * 关闭视频，释放底层资源
     @example
     jessibuca.destroy()
     */
    destroy(): void;

    /**
     * 清理画布为黑色背景
     @example
     jessibuca.clearView()
     */
    clearView(windowIndex?: number): void;

    /**
     * 播放视频
     @example

     jessibuca.play('url').then(()=>{
        console.log('play success')
    }).catch((e)=>{
        console.log('play error',e)
    })
     //
     jessibuca.play()
     */
    play(url?: string, options?: object, windowIndex?: number): Promise<void>;

    /**
     * 重新调整视图大小
     */
    resize(windowIndex?: number): void;

    /**
     * 设置最大缓冲时长，单位秒，播放器会自动消除延迟。
     *
     * 等同于 `videoBuffer` 参数。
     *
     @example
     // 设置 200ms 缓冲
     jessibuca.setBufferTime(0.2)
     */
    setBufferTime(time: number, windowIndex?: number): void;

    /**
     * 设置最大延迟时间，单位秒，播放器当延迟超过这个时间之后就会丢帧，消除延迟。
     * @param time
     */
    setBufferTimeDelay(time: number, windowIndex?: number): void;

    /**
     * 设置旋转角度，只支持，0(默认) ，180，270 三个值。
     *
     * > 可用于实现监控画面小窗和全屏效果，由于iOS没有全屏API，此方法可以模拟页面内全屏效果而且多端效果一致。   *
     @example
     jessibuca.setRotate(0)

     jessibuca.setRotate(90)

     jessibuca.setRotate(270)
     */
    setRotate(deg: number, windowIndex?: number): void;

    /**
     *
     * 设置音量大小，取值0 — 1
     *
     * > 区别于 mute 和 cancelMute 方法，虽然设置setVolume(0) 也能达到 mute方法，但是mute 方法是不调用底层播放音频的，能提高性能。而setVolume(0)只是把声音设置为0 ，以达到效果。
     * @param volume 当为0时，完全无声;当为1时，最大音量，默认值
     @example
     jessibuca.setVolume(0.2)

     jessibuca.setVolume(0)

     jessibuca.setVolume(1)
     */
    setVolume(volume: number, windowIndex?: number): void;

    /**
     * 返回是否加载完毕
     @example
     var result = jessibuca.hasLoaded()
     console.log(result) // true
     */
    hasLoaded(windowIndex?: number): boolean;

    /**
     * 开启屏幕常亮，在手机浏览器上, canvas标签渲染视频并不会像video标签那样保持屏幕常亮。
     * H5目前在chrome\edge 84, android chrome 84及以上有原生亮屏API, 需要是https页面
     * 其余平台为模拟实现，此时为兼容实现，并不保证所有浏览器都支持
     @example
     jessibuca.setKeepScreenOn()
     */
    setKeepScreenOn(windowIndex?: number): boolean;

    /**
     * 全屏(取消全屏)播放视频
     @example
     jessibuca.setFullscreen(true)
     //
     jessibuca.setFullscreen(false)
     */
    setFullscreen(flag: boolean, windowIndex?: number): void;

    /**
     *
     * 截图，调用后弹出下载框保存截图
     * @param filename 可选参数, 保存的文件名, 默认 `时间戳`
     * @param format   可选参数, 截图的格式，可选png或jpeg或者webp ,默认 `png`
     * @param quality  可选参数, 当格式是jpeg或者webp时，压缩质量，取值0 ~ 1 ,默认 `0.92`
     * @param type 可选参数, 可选download或者base64或者blob，默认`download`

     @example

     jessibuca.screenshot("test","png",0.5)

     const base64 = jessibuca.screenshot("test","png",0.5,'base64')

     const fileBlob = jessibuca.screenshot("test",'blob')
     */
    screenshot(options: {
        filename?: string, format?: string, quality?: number, type?: string
    }, windowIndex?: number): void;


    /**
     * 截图(支持水印参数)，调用后弹出下载框保存截图
     * 1. filename: 可选参数, 保存的文件名, 默认 `时间戳`
     * 2. format : 可选参数, 截图的格式，可选png或jpeg或者webp ,默认 `png`
     * 3. quality: 可选参数, 当格式是jpeg或者webp时，压缩质量，取值0 ~ 1 ,默认 `0.92`
     * 4. type: 可选参数, 可选download或者base64或者blob，默认`download`
     * @param options
     */
    screenshotWatermark(options: {
        filename: string,
        format: string,
        quality: number,
        type: string
    }, windowIndex?: number): void

    /**
     * 开始录制。
     * @param fileName 可选，默认时间戳
     @example
     jessibuca.startRecord('xxx')
     */
    startRecord(fileName: string, windowIndex?: number): void;

    /**
     * 暂停录制并下载。
     * @param type 可选，类型，默认`download`，支持`download`和`blob`
     * @param fileName 可选，文件名字，默认时间戳
     @example
     jessibuca.stopRecordAndSave('webm','test')
     */
    stopRecordAndSave(type: string, fileName: string, windowIndex?: number): void;

    /**
     * 返回是否正在播放中状态。
     @example
     var result = jessibuca.isPlaying()
     console.log(result) // true
     */
    isPlaying(windowIndex?: number): boolean;


    /**
     * 返回是否正在加载中状态
     */
    isLoading(windowIndex?: number): boolean;

    /**
     * 返回是否正在暂停中状态
     */
    isPause(windowIndex?: number): boolean;

    /**
     * 是否正在回放暂停中状态
     */
    isPlaybackPause(windowIndex?: number): boolean;

    /**
     *   返回是否静音。
     @example
     var result = jessibuca.isMute()
     console.log(result) // true
     */
    isMute(windowIndex?: number): boolean;

    /**
     * 返回是否正在录制。
     @example
     var result = jessibuca.isRecording()
     console.log(result) // true
     */
    isRecording(windowIndex?: number): boolean;

    /**
     * 手动消除缓冲区数据
     */
    clearBufferDelay(windowIndex?: number): boolean;


    /**
     * 设置网络延迟时间，用于播放器计算播放时间，单位秒s
     * @param timestamp 单位秒s
     */
    setNetworkDelayTime(timestamp: number, windowIndex?: number): void;

    /**
     * 设置分辨率 `quality` 必须是`qualityConfig`里面的数据
     * @param quality
     */
    setStreamQuality(quality: number, windowIndex?: number): void;

    /**
     * 设置镜像翻转 `mirrorRotate` ，
     *  可选参数有 默认`none`,水平 `level`,垂直 `vertical`
     * @param mirrorRotate
     */
    setMirrorRotate(mirrorRotate: string, windowIndex?: number): void;

    /**
     * 隐藏/显示 性能面板
     *
     * @param flag 是否显示性能面板，如果不写参数，则当做toggle切换
     */
    togglePerformancePanel(flag: boolean, windowIndex?: number): void;


    /**
     * 打开电子放大
     */
    openZoom(windowIndex?: number): void;

    /**
     * 关闭电子放大
     */
    closeZoom(windowIndex?: number): void;

    /**
     * 当前是否处于电子放大模式下
     */
    isZoomOpen(windowIndex?: number): boolean;

    /**
     * 电子放大-放大
     */
    expandZoom(windowIndex?: number): void;

    /**
     * 电子放大-缩小
     */
    narrowZoom(windowIndex?: number): void;

    /**
     * 获取当前电子放大档位 范围 1~5
     */
    getCurrentZoomIndex(windowIndex?: number): number;

    /**
     * 返回一个字符串 告知当前解码类型。如果是组合型的话，会用空格分隔。
     * * 可能得结果有：`mse`，`wcs`，`offscreen`，`wasm`，`simd`，`webrtc`，`hls`
     */
    getDecodeType(windowIndex?: number): string;


    /**
     * 返回一个字符串 告知当前解复用类型。
     * 可能得结果有：`flv`，`m7s`，`hls`，`webrtc`，`webTransport`，`nakedFlow`
     */
    getDemuxType(windowIndex?: number): string;

    /**
     * 当前渲染组件 包含 `video`,`canvas`
     */
    getRenderType(windowIndex?: number): string;


    /**
     * 告知当前播放时间戳。 单位 秒
     */
    getPlayingTimestamp(windowIndex?: number): number;

    /**
     * 当前播放器状态
     * * 包含状态有 `playing` `paused` `loading`
     */
    getStatus(windowIndex?: number): string;

    /**
     * 当前播放器类型
     * * 包含类型有 `player` `playbackTF`
     */
    getPlayType(windowIndex?: number): string;

    /**
     * 返回当前音频引擎类型
     * * 返回的结果有 `audio`， `worklet`， `script`， `active`
     */
    getAudioEngineType(windowIndex?: number): string;

    /**
     * 设置裸流播放的fps
     * @param fps :fps值 1~100
     */
    setNakedFlowFps(fps: number, windowIndex?: number): Promise<any>;

    /**
     * 设置调试等级
     * @param level `debug`和`warn`
     */
    updateDebugLevel(level: string, windowIndex?: number): void;


    /**
     * 打开人脸检测
     * * 人脸检测功能需要额外加载js和wasm文件
     */
    faceDetectOpen(windowIndex?: number): void;

    /**
     * 关闭人脸检测
     * *人脸检测功能需要额外加载js和wasm文件
     */
    faceDetectClose(windowIndex?: number): void;

    /**
     * 更新全屏水印
     * @param config 等同于 `fullscreenWatermarkConfig` 配置
     */
    updateFullscreenWatermark(config: object, windowIndex?: number): void;


    /**
     * 移除全屏水印
     */
    removeFullscreenWatermark(windowIndex?: number): void;

    /**
     * 发送websocket消息
     * @param message
     */
    sendWebsocketMessage(message: any, windowIndex?: number): void;

    /**
     * 给canvas 添加内容
     * @param contentList
     */
    addContentToCanvas(contentList: [], windowIndex?: number): void;

    /**
     *  清空canvas 额外添加的内容
     */
    clearContentToCanvas(windowIndex?: number): void;


    /**
     * 设置控制面板的html
     * @param html
     */
    setControlHtml(html: string, windowIndex?: number): void;

    /**
     * 清空控制面板的html
     */
    clearControlHtml(windowIndex?: number): void;

    /**
     * 监听 jessibuca 初始化事件
     * @example
     * jessibuca.on("load",function(){console.log('load')})
     */
    on(event: JessibucaProMulti.EVENTS.load, callback: (windowIndex: number) => void): void;

    /**
     * 视频播放持续时间，单位ms
     * @example
     * jessibuca.on('timeUpdate',function (ts) {console.log('timeUpdate',ts);})
     */
    on(event: JessibucaProMulti.EVENTS.timeUpdate, callback: (windowIndex: number) => void): void;

    /**
     * 当解析出视频信息时回调，2个回调参数
     * @example
     * jessibuca.on("videoInfo",function(data){console.log('width:',data.width,'height:',data.width)})
     */
    on(event: JessibucaProMulti.EVENTS.videoInfo, callback: (windowIndex: number, data: {
        /** 视频宽 */
        width: number;
        /** 视频高 */
        height: number;
    }) => void): void;

    /**
     * 当解析出音频信息时回调，2个回调参数
     * @example
     * jessibuca.on("audioInfo",function(data){console.log('numOfChannels:',data.numOfChannels,'sampleRate',data.sampleRate)})
     */
    on(event: JessibucaProMulti.EVENTS.audioInfo, callback: (windowIndex: number, data: {
        /** 声频通道 */
        numOfChannels: number;
        /** 采样率 */
        sampleRate: number;
    }) => void): void;

    /**
     * 错误信息
     * @example
     * jessibuca.on("error",function(error){
        if(error === JessibucaProMulti.ERROR.fetchError){
            //
        }
        else if(error === JessibucaProMulti.ERROR.webcodecsH265NotSupport){
            //
        }
        console.log('error:',error)
    })
     */
    on(event: JessibucaProMulti.EVENTS.error, callback: (windowIndex: number, err: JessibucaProMulti.ERROR) => void): void;

    /**
     * 播放器播放的时候，http(s) 协议请求失败
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.ERROR.fetchError, callback: (windowIndex: number) => void): void;

    /**
     * 播放器播放的时候，websocket 请求失败
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.ERROR.websocketError, callback: (windowIndex: number) => void): void;


    /**
     * 当前网速， 单位KB 每秒1次,
     * @example
     * jessibuca.on("kBps",function(data){console.log('kBps:',data)})
     */
    on(event: JessibucaProMulti.EVENTS.kBps, callback: (windowIndex: number, value: number) => void): void;

    /**
     * 渲染开始
     * @example
     * jessibuca.on("start",function(){console.log('start render')})
     */
    on(event: JessibucaProMulti.EVENTS.start, callback: (windowIndex: number) => void): void;

    /**
     * 当设定的超时时间内无数据返回,则回调
     * @example
     * jessibuca.on("timeout",function(error){console.log('timeout:',error)})
     */
    on(event: JessibucaProMulti.EVENTS.timeout, callback: (windowIndex: number, error: JessibucaProMulti.EVENTS.timeout) => void): void;

    /**
     * 当play()的时候，如果没有数据返回，则回调
     * @example
     * jessibuca.on("loadingTimeout",function(){console.log('timeout')})
     */
    on(event: JessibucaProMulti.EVENTS.loadingTimeout, callback: (windowIndex: number) => void): void;

    /**
     * 当播放过程中，如果超过timeout之后没有数据渲染，则抛出异常。
     * @example
     * jessibuca.on("delayTimeout",function(){console.log('timeout')})
     */
    on(event: JessibucaProMulti.EVENTS.delayTimeout, callback: (windowIndex: number,) => void): void;

    /**
     * 当前是否全屏
     * @example
     * jessibuca.on("fullscreen",function(flag){console.log('is fullscreen',flag)})
     */
    on(event: JessibucaProMulti.EVENTS.fullscreen, callback: (windowIndex: number,) => void): void;

    /**
     * 触发播放事件
     * @example
     * jessibuca.on("play",function(flag){console.log('play')})
     */
    on(event: JessibucaProMulti.EVENTS.play, callback: (windowIndex: number,) => void): void;

    /**
     * 触发暂停事件
     * @example
     * jessibuca.on("pause",function(flag){console.log('pause')})
     */
    on(event: JessibucaProMulti.EVENTS.pause, callback: (windowIndex: number,) => void): void;

    /**
     * 触发声音事件，返回boolean值
     * @example
     * jessibuca.on("mute",function(flag){console.log('is mute',flag)})
     */
    on(event: JessibucaProMulti.EVENTS.mute, callback: (windowIndex: number,) => void): void;

    /**
     * 流状态统计，流开始播放后回调，每秒1次。
     * @example
     * jessibuca.on("stats",function(s){console.log("stats is",s)})
     */
    on(event: JessibucaProMulti.EVENTS.stats, callback: (windowIndex: number, stats: {
        /** 当前缓冲区时长，单位毫秒 */
        buf: number;
        /** 当前视频帧率 */
        fps: number;
        /** 当前音频码率，单位bit */
        abps: number;
        /** 当前视频码率，单位bit */
        vbps: number;
        /** 当前视频帧pts，单位毫秒 */
        ts: number;

        pTs: number;

        dts: number;

    }) => void): void;

    /**
     * 渲染性能统计，流开始播放后回调，每秒1次。
     * @param performance 0: 表示卡顿,1: 表示流畅,2: 表示非常流程
     * @example
     * jessibuca.on("performance",function(performance){console.log("performance is",performance)})
     */
    on(event: JessibucaProMulti.EVENTS.performance, callback: (windowIndex: number, performance: 0 | 1 | 2) => void): void;

    /**
     * 录制开始的事件

     * @example
     * jessibuca.on("recordStart",function(){console.log("record start")})
     */
    on(event: JessibucaProMulti.EVENTS.recordStart, callback: (windowIndex: number,) => void): void;

    /**
     * 录制结束的事件

     * @example
     * jessibuca.on("recordEnd",function(){console.log("record end")})
     */
    on(event: JessibucaProMulti.EVENTS.recordEnd, callback: (windowIndex: number,) => void): void;

    /**
     * 录制的时候，返回的录制时长，1s一次

     * @example
     * jessibuca.on("recordingTimestamp",function(timestamp){console.log("recordingTimestamp is",timestamp)})
     */
    on(event: JessibucaProMulti.EVENTS.recordingTimestamp, callback: (windowIndex: number, timestamp: number) => void): void;

    /**
     * 监听调用play方法 经过 初始化-> 网络请求-> 解封装 -> 解码 -> 渲染 一系列过程的时间消耗
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.performance, callback: (windowIndex: number, times: {
        playInitStart: number, // 1 初始化
        playStart: number, // 2 初始化
        streamStart: number, // 3 网络请求
        streamResponse: number, // 4 网络请求
        demuxStart: number, // 5 解封装
        decodeStart: number, // 6 解码
        videoStart: number, // 7 渲染
        playTimestamp: number,// playStart- playInitStart
        streamTimestamp: number,// streamStart - playStart
        streamResponseTimestamp: number,// streamResponse - streamStart
        demuxTimestamp: number, // demuxStart - streamResponse
        decodeTimestamp: number, // decodeStart - demuxStart
        videoTimestamp: number,// videoStart - decodeStart
        allTimestamp: number // videoStart - playInitStart
    }) => void): void

    /**
     * 当点击播放器上面的时间进度条，响应的事件
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.playbackSeek, callback: (windowIndex: number, times: {
        hour: number,
        min: number,
        second: number
    }) => void): void;

    /**
     * 录像流的 stats数据,1s回调一次
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.playbackStats, callback: (windowIndex: number, stats: {
        fps: number,
        start: number,
        end: number,
        timestamp: number,
        dataTimestamp: number,
        audioBufferSize: number,
        videoBufferSize: number,
        ts: number
    }) => void): void;


    /**
     * 录像流的当前播放的时间,1s回调一次
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.playbackTimestamp, callback: (windowIndex: number, times: {
        hour: number,
        min: number,
        second: number,
        ts: number
    }) => void): void;


    /**
     * 录像流的ui配置了 playbackPause 方法之后，当触发 playbackPause方法，会触发事件，方便业务层做与服务器端通讯
     * @param event
     * @param callback
     *
     * JessibucaProMulti.on('playbackPauseOrResume', (value) => {
     *     if (value === true) {
     *         // pause 播放 -> 暂停
     *     } else {
     *         //  resume 暂停 -> 播放
     *     }
     * })
     */
    on(event: JessibucaProMulti.EVENTS.playbackSeek, callback: (windowIndex: number, flag: boolean) => void): void;


    /**
     * 回调ptz 操作的方向
     * * 包含`up`，`right`,`down`,`left`,`stop`
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.ptz, callback: (windowIndex: number, flag: string) => void): void;

    /**
     * 切换分辨率的事件回调
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.streamQualityChange, callback: (flag: string) => void): void;

    /**
     * 播放器被销毁的时候触发的事件
     * * 由于 close 事件必须先抛出，然后执行的内部的destroy 方法(不然内部执行执行接触事件绑定，外部on方法就会失效掉的)
     * * 所以需要在监听事件内部通过 setTimeout 方法之后（让destroy执行成功）再执行后续的业务代码，例如关闭流连接啥的等等
     * * 如果不在setTimeout之后执行，会出现意想不到的事情
     * @param event
     * @param callback
     *
     * JessibucaProMulti.on('close', () => {
     *     setTimeout(() => {
     *         // do some things
     *     }, 10)
     * })
     */
    on(event: JessibucaProMulti.EVENTS.close, callback: (windowIndex: number,) => void): void;


    /**
     * 电子放大是否开启状态
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.zooming, callback: (windowIndex: number, flag: boolean) => void): void;

    /**
     * 触发播放器重播的时候，会抛出错误信息，方便业务层做错误上报
     * * 主要收集的数据有
     *
     * - url： 播放地址
     * - error： 错误信息
     * - playType ： 播放类型
     * - demuxType：解封装类型
     * - decodeType：解码类型
     * - renderType: 渲染类型
     * - videoInfo : 视频信息 {width,height,encType}
     * - audioInfo : 音频信息 {encType,channels,sampleRate}
     * - audioEngine ：音频引擎
     * - allTimes ：播放时长，单位秒
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.crashLog, callback: (windowIndex: number, log: {
        url: string,
        error: string,
        playType: string,
        demuxType: string,
        decodeType: string,
        renderType: string,
        videoInfo: {
            width: number,
            height: number,
            encType: string
        },
        audioInfo: {
            encType: string,
            channels: number,
            sampleRate: number,
        },
        audioEngine: string,
        allTimes: number
    }) => void): void;

    /**
     * 播放器实例获取焦点（点击和右键）的时候触发的事件。
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.focus, callback: (windowIndex: number,) => void): void;

    /**
     * 播放器实例失去焦点的时候触发的事件。
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.blur, callback: (windowIndex: number,) => void): void;


    /**
     * 监听窗口不可见超时时间
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.visibilityHiddenTimeout, callback: (windowIndex: number,) => void): void;

    /**
     * 该事件只有在使用websocket协议的时候才会触发
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.websocketOpen, callback: (windowIndex: number,) => void): void;

    /**
     * 该事件只有在使用websocket协议的时候才会触发
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.websocketClose, callback: (windowIndex: number,) => void): void;

    /**
     * 当play()或audioResume()进行音频恢复或者setVolume()更新音量时回调。
     * @param event
     * @param callback
     */
    on(event: JessibucaProMulti.EVENTS.audioResumeState, callback: (windowIndex: number, options: {
        state: 'running' | 'suspended',
        isRunning: boolean
    }) => void): void

    /**
     * 监听方法
     *
     @example

     jessibuca.on("load",function(){console.log('load')})
     */
    on(event: string, callback: Function): void;

}

export default JessibucaProMulti;
