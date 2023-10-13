
declare namespace JessibucaProTalk {
    enum EVENTS {
        talkStreamClose = 'talkStreamClose',
        talkStreamError = 'talkStreamError',
        talkStreamInactive = 'talkStreamInactive',
        talkGetUserMediaTimeout = 'talkGetUserMediaTimeout'
    }

    interface Config {
        // 语音编码类型，支持`g711a`和`g711u`，默认是`g711a`
        encType: string,
        // 语音包类型，支持`rtp`，默认是`rtp`
        packetType: string,
        // rtp包的ssrc，10位
        rtpSsrc: string,
        // 采样通道
        numberChannels: number,
        // 采样率
        sampleRate: number,
        // 采样精度
        sampleBitsWidth: number,
        // 是否开启debug模式
        debug: boolean,
        // debug模式下的日志级别，支持`debug`、`warn`，默认是`warn`
        debugLevel: string,
        //   是否开启测试麦克风,不连接ws
        testMicrophone: boolean,
        // 语音引擎，支持`worklet`和`script`，默认是`worklet`
        engine: string,
        // 是否开启检测getUserMedia超时
        checkGetUserMediaTimeout: boolean,
        // getUserMedia超时时间，单位ms
        getUserMediaTimeout: number
    }
}


declare class JessibucaProTalk {
    constructor(config?: JessibucaProTalk.Config);

    /**
     * 开启语音
     * @param wsUrl
     * @param options
     */
    startTalk(wsUrl, options: JessibucaProTalk.Config): Promise<any>;


    /**
     * 关闭语音
     */
    stopTalk(): Promise<any>;

    /**
     * 获取语音音量
     * * 返回值是一个0-100的数字，表示当前语音音量
     */
    getTalkVolume(): Promise<Number>;

    /**
     * 设置语音音量
     * @param volume 0-100的数字，表示当前语音音量
     */
    setTalkVolume(volume: number): Promise<any>;


    /**
     * 监听ws 断开
     * @param event
     * @param callback
     */
    on(event: JessibucaProTalk.EVENTS.talkStreamClose, callback: Function): void;

    /**
     * 监听 ws error
     * @param event
     * @param callback
     */
    on(event: JessibucaProTalk.EVENTS.talkStreamError, callback: Function): void;


    /**
     * 监听 stream oninactive
     * @param event
     * @param callback
     */
    on(event: JessibucaProTalk.EVENTS.talkStreamInactive, callback: Function): void;

    /**
     * 检查 getUserMedia 超时
     * @param event
     * @param callback
     */
    on(event: JessibucaProTalk.EVENTS.talkGetUserMediaTimeout, callback: Function): void;

    /**
     * 监听方法
     *
     @example

     JessibucaProTalk.on("talkStreamClose",function(){console.log('talkStreamClose')})
     */
    on(event: string, callback: Function): void;
}

export default JessibucaProTalk;
