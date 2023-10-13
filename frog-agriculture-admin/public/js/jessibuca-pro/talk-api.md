
# 语音通讯 API

## JessibucaProTalk(options)

options 支持的参数有：

### encType

- **类型**：`string`
- **默认值**：`g711a`
- **用法**：

语音编码类型，支持`g711a`和`g711u`，默认是`g711a`

### packetType

- **类型**：`string`
- **默认值**：`rtp`
- **用法**：

语音包类型，支持`rtp`，默认是`rtp`

### rtpSsrc

- **类型**：`string`

- **用法**：

rtp包的ssrc，10位

### numberChannels

- **类型**：`number`
- **默认值**：`1`
- **用法**：

采样通道

### sampleRate

- **类型**：`number`
- **默认值**：`8000`
- **用法**：
  采样率

### sampleBitsWidth

- **类型**：`number`
- **默认值**：`16`
- **用法**：
  采样精度

### debug

- **类型**：`boolean`
- **默认值**：false
- **用法**：
  是否开启debug模式

### debugLevel（pro）

- **类型**：`string`
- **默认值**：`warn`
- **用法**：

debug模式下的日志级别，支持`debug`、`warn`，默认是`warn`

> error 的信息默认就是会输出出来的。

### testMicrophone

- **类型**：`boolean`
- **默认值**：false
- **用法**：
  是否开启测试麦克风,不连接ws

### engine

- **类型**：`string`
- **默认值**：`worklet`
- **用法**：

语音引擎，支持`worklet`和`script`，默认是`worklet`

### checkGetUserMediaTimeout

- **类型**：`boolean`
- **默认值**：false
- **用法**：
  是否开启检测getUserMedia超时

### getUserMediaTimeout

- **类型**：`number`
- **默认值**：`10 * 1000`
- **用法**：

getUserMedia超时时间，单位ms

## 方法

### startTalk(wsUrl, options)

- **参数**：
    - `{string} wsUrl`
    - `{object} options`
- **用法**： 开启语音
- **返回值**：`Promise`

> options 参数通初始化 new JessibucaProTalk(options) 时的参数一致

### stopTalk()

- **用法**： 关闭语音
- **返回值**：`Promise`

### getTalkVolume()

- **用法**： 获取语音音量
- **返回值**：`Promise(number)`

> 返回值是一个0-100的数字，表示当前语音音量

### setTalkVolume(volume)

- **用法**： 设置语音音量
- **返回值**：`Promise`
- **参数**：
    - `{number} volume` 0-100的数字，表示当前语音音量

