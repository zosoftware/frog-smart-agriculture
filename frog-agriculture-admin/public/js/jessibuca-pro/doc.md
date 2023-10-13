## vue、react 推荐

### vue

1. 推荐绑定在 `this` 上面，不推荐绑定在`data` 对象上面，不然会触发无效的事件监听。
2. 或者在实例的时候，绑定在`data`上面的时候，命名的时候以 `_` 或者`$` 开头，这样也不会触发无效的事件监听。
   在 `vue` 中

> 以 _ 或 $ 开头的属性将不会被组件实例代理，因为它们可能和 Vue 的内置属性、API 方法冲突。你必须以 this.$data._property
> 的方式访问它们。

见： https://cn.vuejs.org/api/options-state.html#data

```vue
// 可以挂载在Vue上面
Vue.prototype.$player = new Jessibuca({

})
```

```vue
// 也可以挂载在 $options 上面
this.$options.jessibuca = new Jessibuca({

})
```

#### 什么时候初始化播放器

建议是在 `mounted` 生命周期里面初始化

> 确保dom已经渲染出来了，再初始化播放器。

### react

推荐绑定在 `this` 上面，不推荐绑定在`state` 对象上面。

## 关于硬解码或者video标签渲染自动播放

背景：用户希望打开页面的时候就直接自动播放视频（单屏或者多屏），但是浏览器的自动播放策略是，必须是用户手动触发了事件之后，才能自动播放。

会抛出`DOMException: play() failed because the user didn't interact with the document first. https://goo.gl/xX8pDD` 错误。

### 解决方案

1. 添加一个交互事件，让用户手动触发下，再去播放视频。
2. 使用`wcs`解码(在https环境下)，然后使用`canvas`标签渲染。
3. 使用wasm(simd) 软解码，然后使用`canvas`标签渲染。

## 推荐配置方案建议

### http(s)

由于`mse`的兼容性比`wcs`要强，所以在`http` 或者`https` 环境下，都可以优先使用`mse`进行解码。

```
{
    useMSE: true,
    useSIMD:true,
    autoWasm:true,
}
```

### https

如果运行环境是`https`的，并且不想使用 `mse` 解码的，可以按照如下配置。

> wcs的高分辨率的解码能力要强于mse，但是对于视频的兼容性不如mse

```
{
    useWCS: true,
    useSIMD:true,
    autoWasm:true,
}
```

> wcs 对于视频的兼容性不如mse，

```
{
    useWCS: true,
    useSIMD:true,
    autoWasm:true,
}
```

优先使用mse硬解码，如果不支持，降级到simd wasm解码,兜底使用wasm解码。

## 关于解码（useMSE、useWCS、wasm(simd)）优先级

### useMSE

使用的是浏览器提供的`MediaSource`接口，来进行解码。

- 硬解码
- 兼容性好
- ios safari不支持
- 支持H264和H265解码
- 支持http和https

### useWCS

使用的是`WebCodec`接口，来进行解码。

- 硬解码
- 支持H264和H265解码
- 支持https
- ios safari不支持
- 兼容性不如mse

### wasm(simd)

使用的是`webassembly`来进行解码。

- 软解码
- 兼容性好
- 支持H264和H265解码
- 支持http和https

### 优先级

如果同时配置了`useMSE`和`useWCS`，则优先使用`useMSE`，如果`useMSE`不支持，则使用`useWCS`，如果 `useWCS`
不支持，则降级到`wasm`解码。

> useMSE > useWCS > wasm

## mse(wcs) 解码报错的时候，不降级到wasm解码（仍然使用mse或者wcs解码）

> 可能会存在流本身就会存在缺帧的情况（推流端没推完整），会导致mse或者wcs 解码报错。

这边我们希望解码报错的时候，仍然使用mse或者wcs解码，而不是降级到wasm解码。

```
{
    mseDecodeErrorReplay:true,
    wcsDecodeErrorReplay:true,
    autoWasm:false
}
```

## 监听请求流的失效(404、403等)或者500报错

可以监听`play`方法的`catch`

```js
jessibucaPro.play(url).catch((err) => {
    // err 就是错误信息
})
```

### 理解loadingTimeout 和 delayTimeout

#### loadingTimeout

loadingTimeout 是指在`播放器在请求url的时候`，接口是返回200状态码了，但是数据还迟迟没有推送给web端 ，如果在`loadingTimeout`
时间内，没有收到流数据，则会抛出`loadingTimeout`错误。

#### delayTimeout

delayTimeout 是指在`播放器播放过程中`，如果在`delayTimeout`时间内，没有收到流数据，则会抛出`delayTimeout`错误。

## 关于移动端（H5）切换网络的时候，播放器会触发什么事件。

### http请求

会触发`fetchError`事件

```js

jessibucaPro.on("fetchError", function (msg) {
    console.log('fetchError:', msg)
})
```

### websocket请求

会触发`websocketError`事件

```js
jessibucaPro.on("websocketError", function (msg) {
    console.log('websocketError:', msg)
})
```

### 小结

或者可以通过监听`error`错误事件，来监听所有的错误事件。

```js
jessibucaPro.on("error", function (error) {
    console.log('error:', error)

    if (error === jessibucaPro.ERROR.fetchError || error === jessibucaPro.ERROR.websocketError) {
        // 这里统一的做重连。

        jessibucaPro.destroy().then(() => {
            jessibucaPro = null;
            jessibucaPro = new JessibucaPro({
                // 参数
            })
            jessibucaPro.play(url);
        })
    }
})

```

## 关于播放器延迟(超过延迟触发丢帧)设置

目前播放器提供`videoBuffer`和`videoBufferDelay`两个参数来控制延迟。

对于`videoBuffer`参数（单位秒），是用来设置缓存的时长，啥意思呢？

> 就是播放器在接收流数据之后，并没有立马解码播放，还是等待缓存了`videoBuffer`时长的数据之后才去按照一定的频率去解码->播放

在播放过程中，会根据新来流的时间戳结合本地的时间戳，实时计算当前的延迟时长，当延迟小于`videoBufferDelay + videoBuffer`
,则不做任何处理，继续
解码->播放。

> 当最新过来的流数据的时间戳，计算最新的延迟时长大于`videoBufferDelay + videoBuffer`的时候，就会触发，丢帧机制。

## 关于网络延迟

对于网络延迟，是如何计算而来的呢？

> 当流过来的时候，本地也会同步的起一个时间线，因为是直播流，所以理论上如果没有网络延迟的情况下，1s的时间里面，流里面的数据也应该是1s的。

所以根据这个依据，当本地过了`1s`之后，如果计算的流的数据只有`0.9s`的话，那这`1 - 0.9 = 0.1` 的就是网络延迟。

播放器支持配置`networkDelay`参数（单位秒s）,当播放器计算的网络延迟超过了预定设置的`networkDelay`
参数之后，当设置了`networkDelayTimeoutReplay`为true的时候，就会触发`networkDelayTimeout`事件。

业务可以通过监听

```js
jessbucaPro.on(JessibucaPro.EVENTS.networkDelayTimeout, (ts) => {
    // ts 就是网络超时的时间，单位ms(毫秒)
})
```

### 会出现本身延迟很低，但是网络延迟数据却很大

一般这种情况，会出现在机器是4g网络，然后机器向上推流的时候会存在丢帧的逻辑，但是时间戳的逻辑是不会变的，所以会出现这种情况。

比如机器的FPS是25,正常情况下，每秒会有25帧，然后每秒40ms的增加，这样一秒下来，25帧的时间戳就是1000ms。

但是如果存在丢帧的情况，比如丢帧了5帧，那么一秒就只会上传20帧的数据，但是还是每秒40ms的增加，这样一秒下来，20帧的时间戳就是800ms。这样就会少了200ms的数据。

然后结合播放器本地1s的时间，结合流里面的时间戳的时间，就会计算出200ms的网络延迟。


> 基于这种情况，网络延迟的数据，只是一个参考值，不是绝对的。

## 配置低延迟（300ms）解决方案建议【直播流】

因为超低延迟，对于mse 和wcs并不能做到这么低的延迟方案，所以推荐使用wasm simd来实现。

推荐的配置方案：

```
{
    useMSE:false,
    useWCS:false,
    videoBuffer:0.1,
    videoBufferDelay:0.2,
    useSIMD: true,
}
```

如果需要更低的延迟，可以使用`videoBufferDelay`来配置延迟时间，单位是秒。

## 崩溃日志上报

当播放器播放失败，或者崩溃重新播放的时候，会触发`crashLog`事件，可以监听这个事件，来上报崩溃日志。

可以监听`crashLog`事件

```js
jessibuca.on('crashLog', (data) => {

})
```

返回的数据有：

- url： 播放地址
- error： 错误信息
- playType ： 播放类型
- demuxType：解封装类型
- decodeType：解码类型
- renderType: 渲染类型
- videoInfo : 视频信息 {width,height,encType}
- audioInfo : 音频信息 {encType,channels,sampleRate}
- audioEngine ：音频引擎
- allTimes ：播放时长，单位秒

方便业务拿到数据进行数据上报。

## 页面最小化（切换tab）超时检测建议

一般业务有需要监听页面最小化，或者切换tab的事件，来关闭播放器播放，并提示用户。

可以通过设置`pageVisibilityHiddenTimeout` 参数，单位`秒`，目前默认值是`5 * 60` 5分钟。

```js
// 配置信息：
{
    pageVisibilityHiddenTimeout: 5 * 60
}
```

通过监听`visibilityHiddenTimeout`事件，来进行业务逻辑处理。

```js
player.on('visibilityHiddenTimeout', function () {
    // 窗口不可见5分钟超时
});
```

## 播放地址首次就请求失败（非超时）

### 监听`jessibucaPro.EVENTS.error`事件

```js
jessibucaPro.on(JessibucaPro.EVENTS.error, (error) => {
    if (error === JessibucaPro.ERROR.fetchError) {
        // http-flv 错误
    } else if (error === JessibucaPro.ERROR.websocketError) {
        // ws-flv 错误
    }
})
```

### 监听`JessibucaPro.ERROR.fetchError`和`JessibucaPro.ERROR.websocketError`事件

根据自己的业务播放的url地址类型，来监听对应的错误事件。

```js
jessibucaPro.on(JessibucaPro.ERROR.fetchError, (error) => {
    // http-flv 错误
})
jessibucaPro.on(JessibucaPro.ERROR.websocketError, (error) => {
    // ws-flv 错误
})
```

## 页面首次加载超时检测

目前播放器的默认配置是

```
{
    loadingTimeout: 10,
    loadingTimeoutReplay:true,
    loadingTimeoutReplayTimes:3
}
```

> 如果想要如果想无限次重试，可以设置loadingTimeoutReplayTimes为-1

## 页面播放过程中超时检测

目前播放器的默认配置是

```
{
    heartTimeout: 10,
    heartTimeoutReplay:true,
    heartTimeoutReplayTimes:3
}
```

> 如果想要如果想无限次重试，可以设置heartTimeoutReplayTimes为-1

## 录像流集成建议

涉及到录像流的 api 有：

- playback
- playbackPause
- pause
- playbackResume
- forward
- normal
- updatePlaybackForwardMaxRateDecodeIFrame
- setPlaybackStartTime
- getPlaybackCurrentRate
- playbackClearCacheBuffer
- isPlaybackPause

涉及到录像流的 事件 有：

- playbackSeek
- playbackStats
- playbackTimestamp
- playbackPauseOrResume
- playbackPreRateChange
- playbackRateChange

具体集成的demo 可以看 `playback-demo.html`文件

调用的是`playback`方法。

```js
jessibuca.playback(url, {
    // config
})
```

### 24小时UI配置

录像流支持配置24小时组件(支持放大和缩小)和倍率按钮

对于配置参数

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
    ]
})
```

> 未匹配到的时间，在ui上面是不可触发点击事件的。

### 24小时UI点击事件（seek）

当点击了ui上面的时间，会触发`playbackSeek`事件，可以监听这个事件，来进行业务逻辑处理。

```js
jessibucaPro.on('playbackSeek', (data) => {
    // data 为对象
    /**
     * {
        "hour": 1,
        "min": 2,
        "second": 0
        }
     */

    // 拿到时间，去调用服务器端的seek接口
})
```

业务代码可以根据回调的数据，来进行通知服务器端进行seek 操作。

当服务器端更新完了流，可以调用`setPlaybackStartTime`方法，来更新ui的时间。

总体流程： 点击ui获取时间->调用服务器端seek接口->服务器端更新流->调用`setPlaybackStartTime`方法更新ui时间

> setPlaybackStartTime的参数是时间戳，单位是秒。指的是当天的时间戳。播放器拿到这个时候，会转换成 时 分 秒， 然后会更新UI
> 上面的当前时间。

例如 需要更新的时间是3:10:10

```js
// 3:10:10
const time = new Date().setHours(3, 10, 10, 0)
jessibucaPro.setPlaybackStartTime(time)
```

> ui 也支持更新当前的进度时间

### 24小时UI配置（更新）开始时间

在录播流播放模式下，底部的开始时间是从0点开始的。

所以，如果需要配置开始时间，可以调用`setPlaybackStartTime`方法。

```js

// 3:10:10
const time = new Date().setHours(3, 10, 10, 0)
jessibucaPro.setPlaybackStartTime(time)
```

这样配置了之后，UI上面的开始时间就是从3:10:10开始一秒一秒的走。

> 同样的在触发`seek`事件的时候，需要在ui上面更新开始时间，也是调用这个方法。

### 暂停（不关闭流）

> 会存在业务上面的录像流暂停，但是不关闭流的需求，这个时候可以调用播放器的`playbackPause()`方法，来暂停播放。

> 暂停的时候会把缓存的数据清空。所以如果不清除缓存可以设置参数`isPlaybackPauseClearCache:true`。

1. 调用服务器端暂停接口
2. 调用播放器的`playbackPause()`方法，画面暂停
3. 调用服务器端恢复接口
4. 调用播放器的`playbackResume()`方法，画面播放

### 暂停（关闭流）

可以调用 `playbackPause(true)` 或者 `pause()` 方法。

### 倍率播放

1. 首先调用服务器端接口，开启倍率播放
2. 调用播放器的`forward(rate)`方法
3. 关闭倍率播放，调用服务器端接口
4. 调用播放器的`forward(1)`或者`normal()`方法

### 倍率播放（配置了rateConfig 参数）

1. 首先选择UI上面的倍率按钮，配合`playbackPreRateChange`事件，拿到倍率，
2. 调用服务器端接口，开启倍率播放。
3. 调用播放器的`forward(rate)`方法。

### seek操作（配合24小时UI）

1. 点击24小时UI，监听`playbackSeek`获取时间
2. 调用服务器端seek接口
3. 服务器端更新流
4. 调用`setPlaybackStartTime`方法，更新ui时间

### 解码前缓存数据

> 会存在有些流，一倍率的时候，但是服务器端推送的流是2倍的，这个时候就需要播放器兼容这种特殊的流，只解码所需的1倍率的数据。

可以通过在调用playback方法播放的时候，config配置参数里面配置`isCacheBeforeDecodeForFpsRender` 参数

```js
jessibucaPro.playback(url, {
    isCacheBeforeDecodeForFpsRender: true
})
```

### 监听当前缓存情况（需要通知服务器端暂停推流）

目前播放器的缓存数据有，`待解码`，`解码后待渲染` 两块缓存数据，单位帧。

> 待解码缓存数据，是指，从服务器端获取到的数据，还没有解码的数据。
>
> 解码后待渲染缓存数据，是指，解码后的数据，还没有渲染的数据。


可以通过监听`stats`事件，来获取当前的缓存数据。

`playbackVideoBuffer` 指的是`视频待渲染`帧。

`demuxBuffer` 指的是`视频待渲染`帧。

可以通过`stats` 事件里面的fps 来计算出每一帧的时间，然后计算出缓存的时间。

假设fps 为25，那么每一帧的时间就是 1000 / 25 = 40ms。

如果`playbackVideoBuffer` 为100，那么缓存的时间就是 100 * 40 = 4000ms = 4s。如果`demuxBuffer` 为100，那么缓存的时间就是 100

* 40 = 4000ms = 4s。 那么一起的缓存时长就是 4 + 4 = 8s

> stats 事件里面已经添加了 `playbackCacheDataDuration`值。

### 固定倍率播放

> 会存在一些流，服务器端的录像流的推过来的fps不是定码率的，是波动性比较大的那种，这个时候就需要客户端去按照固定倍率去播放。


则需要配置使用固定fps进行渲染，并配置 fps 值。默认值是 `25`

```
playbackConfig:{
    isUseFpsRender:true,
    fps: '', // fps值
}
```

可能会存在fps 设置的不对的情况，所以播放器也会去校正这个参数。

> 播放器也会根据流的时间戳来计算出准确的`fps`大小，并更新到`fps`参数上面去。

### 使用播放器本地时间戳回调时间（从0 开始那种）

> 存在一些流，流里面的时间戳不是从 0 开始的，这个时候，如果录像流想要从0 开始回调时间。


这个时候就需要配置 `playbackConfig` 参数

```
playbackConfig:{
    isUseLocalCalculateTime:true, // 是否使用本地时间来计算playback时间
    localOneFrameTimestamp:40 // 一帧的间隔时间戳 40ms  100 / 25 = 40ms
}
```

业务层通过监听`playbackTime`，`playbackTimestamp` 事件，可以监听到当前播放的时间。

### 当前录像流的播放时间

可以监听`playbackTimestamp`事件，来获取当前播放的时间。

```js
jessibucaPro.on('playbackTimestamp', (data) => {
    // data 为对象
    /**
     * {
        "hour": 1,
        "min": 2,
        "second": 0,
        "ts": "" // 时间戳
        }
     */
})
```

### 配置I帧解码

> 过滤掉其他帧，只解码I帧，可以提高播放性能。

当倍率达到播放器性能瓶颈的时候，就需要配置I帧解码，来提高播放性能。

可以通过调用`updatePlaybackForwardMaxRateDecodeIFrame`方法，来配置I帧解码。

> 播放器默认I帧解码的倍率是4倍。

```js
// 当倍率达到3倍的时候，就触发i帧解码。
jessibucaPro.updatePlaybackForwardMaxRateDecodeIFrame(3)
```

> 支持在播放前后调用，

### 播放结束

因为播放器播放的是流，所以播放器并不知道这个流的结束时间是多少。

> 播放器唯一能做的就是监听超时。

需要业务层自己结合后端接口来实现。

## 语音通讯集成

具体集成的demo 可以看 `talk-demo.html`文件

> 需要单独引用`jessibuca-pro-talk.js`文件

## 单视频播放

> 只需要将`hasAudio`设置为`false`即可, 播放器就不会解码音频流。

## 单音频播放

具体集成的demo 可以看 `only-audio-demo.html`文件

> 只需要将`hasVideo`设置为`false`即可, 播放器就不会解码视频流。

## 配置自定义底部UI按钮

只需要配置`extendOperateBtns`参数即可

## 配置右键菜单

只需要配置`contextmenuBtns`参数即可

## jessibuca-pro.js decoder-pro.js(decoder-pro-simd.js) decoder-pro.wasm(decoder-pro-simd.wasm)文件想通过CDN加载

因为默认情况下 decoder-pro.js(decoder-pro-simd.js) 是通过相对路径引入 decoder-pro.wasm(decoder-pro-simd.wasm) 文件的。

如果想引用CDN的地址，需要修改成CDN的绝对地址。

所以如果想通过CDN加载，需要修改decoder-pro.js(decoder-pro-simd.js)文件

需要配置`decoder` 参数为CDN绝对地址文件。


> decoder-pro.js 和 decoder-pro-simd.js 文件都需要修改。

> jessibuca-pro.js decoder-pro.js(decoder-pro-simd.js) decoder-pro.wasm(decoder-pro-simd.wasm) 需要放在同一个目录下。

```js
const jessibucaPro = new JessibucaPro({
    decoder: 'https://your-cdn.com/decoder-pro.js',
    ...
})
```

```js
// 修改前 src/worker/index.js

this.decoderWorker = new Worker(player._opt.decoder)

// 修改后 src/worker/index.js
const blob = new Blob([`importScripts("${player._opt.decoder}")`], {"type": 'application/javascript'});
const blobUrl = window.URL.createObjectURL(blob);
this.decoderWorker = new Worker(blobUrl);
```

```js
// 修改前 src/decoder/decoder-pro.js
wasmBinaryFile = 'decoder-pro.wasm';
// 修改后 src/decoder/decoder.js
wasmBinaryFile = 'https://cdn.com/decoder-pro.wasm';
```

```js
// 修改前 src/decoder/decoder-pro-simd.js
wasmBinaryFile = 'decoder-pro-simd.wasm';
// 修改后 src/decoder/decoder-pro-simd.js
wasmBinaryFile = 'https://cdn.com/decoder-pro-simd.wasm';
```

然后需要重新执行下 `npm run build` 命令 就可以了。

## 限制js的使用域名和限制播放url的域名

可以看下`utils`文件夹下面的`verify.js`文件。

对于host的生成，可以看下`utils`文件夹下面的`string2CharCodeHex.js`文件。

## 关于自定义按钮和系统默认提供的按钮

一般现在的问题：

1. 系统自带的按钮顺序和icon不满足业务系统
2. 系统自带的按钮的功能不满足业务系统
3. 希望使用自带的按钮样式和功能（排序也有要求）

### 解决方案：

如果是按钮的功能不满足业务需求，可以通过配置`playFn`,`pauseFn` 等方法来配置自定义的方法、

如果不想要系统提供的按钮，可以通过配置`extendOperateBtns` 参数来配置、支持设置`index`，`icon` ，`active icon `, `click`
, `active click` 等参数

## 关于性能面板

在调试过程中，性能面板是一个非常重要的一个东西，上面显示的是流的实时的数据。

可以通过配置

```
{
    showPerformance:true, // 直接显示性能面板数据
    operateBtns:{
        performance:true // 底部控制条按钮
    }
}
```

## 关于日志输出

关于日志，是主要是控制台输出的内容，可以通过配置`debug:true` 来打开日志输出。

> 不管是否配置了`debug:true`，error的日志默认是强制直接输出的。

默认显示的日志级别是`warn`,可以通过配置`debugLevel:'debug'` 讲日志级别调成全量模式。

> 如果是多屏的输出，可以通过配置`isMulti:true` 来配置播放多实例模式。这个模式下，日志输出会有[uuid] 来区分各个实例日志。

## 播放的时候解除静音

播放器默认播放的时候，是静音播放的。所以如果想播放的时候解除静音，则需要配置`isNotMute:true`就可以了。

> 如果是程序触发的自动播放播放器，是不会有声音的，需要用户手动触发才会有声音。

### 苹果手机默认没有声音。

在设置`isNotMute:true` 在苹果的手机端默认是没有声音的。

> iPhone，chrome等要求自动播放时，音频必须静音，需要由一个真实的用户交互操作来恢复，不能使用代码。


程序里面可以检查下播放器的状态，如果是静音状态，就需要需要出一个ui交互，让用户手动恢复声音播放。

```js
var result = jessibucaPro.isMute()
console.log(result) // true
if (result) {

    // 这里可以出一个ui交互，让用户手动恢复声音播放
}
```

可以结合`audioResume()`方法。

```js
jessibucaPro.audioResume();
```

## 关于水印

### 全屏水印

全屏水印指的是：`播放器整个播放窗口重复显示水印`。

可以通过参数`fullscreenWatermarkConfig` 来实现全屏水印，仅支持文字

### 局部水印

支持：`图片` 和 `文字`

可以通过配置`watermarkConfig` 来实现局部水印。

### 使用那种标签渲染

目前播放器支持`video` 和`canvas`两种标签渲染

可以通过参数 `useVideoRender` 和 `useCanvasRender` 来配置

> 当两者同时设置为true的时候，video的标签渲染优先级大于canvas。

> 录像(TF)流因为要支持倍率播放，所以只支持canvas渲染

## 关于视频录制

目前播放器支持录制 `mp4` 和 `webm` 格式的视频

> mp4 只支持录制视频
>
> webm 格式支持录制视频和音频

## 关于出现绿屏或者花屏的时候

如果流数据的首帧不是I帧，一般情况下，解码了之后，就会很容易出现`绿屏`或者`花屏`情况。

播放器支持配置检查首帧是否是I帧，如果不是I帧就直接过滤掉，直到等到I帧出现才开始解码流数据

只需要配置`checkFirstIFrame:true`

> 默认播放器就是设置为true的，不建议修改为false

如果本身流就是特殊的流，能够保证解码出来的画面不是花屏的，就可以设置为false

## 关于音频引擎

目前播放器支持三种音频引擎来播放音频数据，`worklet`、`script`、`active`

目前播放器会根据当前用户的实际情况来动态选择最适合的播放引擎。

> 会优先判断是否在`微信安卓`环境

### worklet

必须是https 环境下，配置才会生效。

> 默认https环境下，默认就是这个播放引擎。

### script

兼容性最高的播放引擎。

> 一般情况下都是这种引擎。

### active

为了支持在`安卓的微信`环境播放，特殊实现的一种播放引擎。

> 其他环境不建议使用这个引擎。

## ptz 操作

目前ptz操作支持两种交互方式`click` 和`mouseDownAndUp`。

### click

鼠标点击事件

### mouseDownAndUp

鼠标按下，和松开 分别触发事件。

## 加密流播放

> 目前加密流只对接了m7s 的服务器

如果想要播放加密流，需要配置`isCrypto:true` 还有`cryptoKeyUrl: your_href`参数。

> cryptoKeyUrl 是获取秘钥的接口地址。

## 关于集成语音通讯

### 整体逻辑

1. 浏览器端采集麦克风数据
2. 将麦克风数据(rtp包)发送给服务端
3. 服务器端将麦克风数据(rtp包)转发给摄像头
4. 摄像头播放声音

目前支持`单独`或者`集成`在播放器中使用语音通讯

> `单独` 使用需要单独引用`jessibuca-pro-talk.js`文件

目前语音通讯支持采集`pcm`、`g711a`、`g711u`、格式的数据，支持封装成`rtp`包格式。

并支持`checkGetUserMediaTimeout` 和 `getUserMediaTimeout` 参数用来检测获取`getUserMedia` 超时

## 需要监听websocket的事件和调用send 方法

会存在私有的websocket请求，需要授权才能播放，就需要监听`websocket`的`open`事件和调用`send` 方法。

为此，播放器提供了`websocketOpen` 事件和 `sendWebsocketMessage` 方法。

```js
jessibucaPro.on(JessibucaPro.EVENTS.websocketOpen, function () {
    console.log('websocket open')
    // 在这里可以做授权的逻辑
    jessibucaPro.sendWebsocketMessage('授权的消息')
})
```

## 手机端全屏

播放器默认是调用的系统层面的全屏事件。

会存在客户需要手机端全屏，但是不想调用系统层面的全屏事件的情况。可以通过配置`useWebFullScreen` 来实现。

同样的需要监听`webFullscreen` 来监听全屏事件。

## 多屏播放

可以使用`jessibuca-pro-multi.js` 来实现多屏播放。

> 具体demo 看 `multi-demo.html` 文件

> 如果引用了`jessibuca-pro-multi.js`，那么`jessibuca-pro.js`就不需要引用了。

## 关于视频录制的一些问题

### 录制的mp4格式的视频，会有加快倍率播放的现象

因为目前`mp4格式`的录制，是按照固定`25FPS`来录制的。所以如果直播流的fps 只有20，那么录制出来的视频在播放的时候，就会有加快倍率的现象。

### 录制的mp4格式的视频，为啥不支持音频数据。

目前mp4格式的文件，只支持AAC格式封装的音频。但是目前的直播流，音频一般都是G711格式的，所以目前mp4格式的录制，不支持音频数据。

> 虽然也有一些直播流，音频是AAC格式的，但是目前pro的音频解码是放wasm里面做的。所以处理起来会麻烦很多。所以暂不支持音频数据。

## gzip 压缩资源文件

> 可以通过gzip压缩资源文件，来减少资源文件的大小。

#### mac(linux)

安装gzip

[https://formulae.brew.sh/formula/gzip](https://formulae.brew.sh/formula/gzip)

#### jessibuca-pro.js

```
gzip jessibuca-pro.js
mv jessibuca-pro.js.gz jessibuca-pro.js
```

#### decoder-pro.js

```
gzip 和decoder-pro.js
mv 和decoder-pro.js.gz 和decoder-pro.js
```

#### decoder-pro.wasm

```
gzip 和decoder-pro.wasm
mv 和decoder-pro.wasm.gz 和decoder-pro.wasm
```

#### decoder-pro-simd.js

```
gzip 和decoder-pro-simd.js
mv 和decoder-pro-simd.js.gz 和decoder-pro-simd.js
```

#### decoder-pro-simd.wasm

```
gzip 和decoder-pro-simd.wasm
mv 和decoder-pro-simd.wasm.gz 和decoder-pro-simd.wasm
```

#### windows

安装gzip

[http://gnuwin32.sourceforge.net/downlinks/gzip-bin-zip.php](http://gnuwin32.sourceforge.net/downlinks/gzip-bin-zip.php)

解压后，将 `jessibuca-pro.js` 和 `decoder-pro.js`和 `decoder-pro.wasm` 和 `decoder-pro-simd.js`和 `decoder-pro-simd.wasm`
文件拖到 gzip.exe上，文件就压缩好了，也需要去掉.gz后缀

## 如何配置底部自定义按钮（事件）

### 自定义按钮

### 自定义事件

## 异步加载js文件

有客户不想通过script标签来加载js文件，而是想通过异步加载js文件的方式来加载。

可以通过`loadJs`方法来实现。

具体demo见：`async-demo.html`

## 如果业务层需要自己处理播放异常逻辑。

> 会存在客户的流，在一次播放之后，就没法再使用了。所以需要在播放异常的时候，重新请求流的地址。

需要客户把一些参数都关闭掉，不能触发播放器自己内部的重播。

```
{
    autoWasm: false,
    webglAlignmentErrorReplay:false,
    wasmDecodeErrorReplay:false,
    mseDecodeErrorReplay: false,
    wcsDecodeErrorReplay: false,
    networkDelayTimeoutReplay:false,
    heartTimeoutReplay:false,
    loadingTimeoutReplay:false,
    mediaSourceTsIsMaxDiffReplay:false
}


```

可以通过监听`playFailedAndPaused`事件来监听。

然后后续操作：

1. 销毁播放器
2. 重新建立播放器
3. 调用play()方法

```js
jessibucaPro.on('playFailedAndPaused', function (error) {

    //
    jessibucaPro.destroy().then(() => {
        //
        jessibucaPro = null;
        //
        jessibucaPro = new JessibucaPro({
            //
        });
        //
        jessibucaPro.play('新的播放地址');
    }).catch(() => {

    });
});
```

## 如果业务层需要处理移动端切换网络环境导致的播放器断开连接的问题。

### 就是在断开网络的时候，会触发`error`事件。业务需要监听这个事件，然后## vue、react 推荐

### vue

1. 推荐绑定在 `this` 上面，不推荐绑定在`data` 对象上面，不然会触发无效的事件监听。
2. 或者在实例的时候，绑定在`data`上面的时候，命名的时候以 `_` 或者`$` 开头，这样也不会触发无效的事件监听。
   在 `vue` 中

> 以 _ 或 $ 开头的属性将不会被组件实例代理，因为它们可能和 Vue 的内置属性、API 方法冲突。你必须以 this.$data._property
> 的方式访问它们。

见： https://cn.vuejs.org/api/options-state.html#data

```vue
// 可以挂载在Vue上面
Vue.prototype.$player = new Jessibuca({

})
```

```vue
// 也可以挂载在 $options 上面
this.$options.jessibuca = new Jessibuca({

})
```

#### 什么时候初始化播放器

建议是在 `mounted` 生命周期里面初始化

> 确保dom已经渲染出来了，再初始化播放器。

### react

推荐绑定在 `this` 上面，不推荐绑定在`state` 对象上面。

## 关于硬解码或者video标签渲染自动播放

背景：用户希望打开页面的时候就直接自动播放视频（单屏或者多屏），但是浏览器的自动播放策略是，必须是用户手动触发了事件之后，才能自动播放。

会抛出`DOMException: play() failed because the user didn't interact with the document first. https://goo.gl/xX8pDD` 错误。

### 解决方案

1. 添加一个交互事件，让用户手动触发下，再去播放视频。
2. 使用`wcs`解码(在https环境下)，然后使用`canvas`标签渲染。
3. 使用wasm(simd) 软解码，然后使用`canvas`标签渲染。

## 推荐配置方案建议

### http(s)

由于`mse`的兼容性比`wcs`要强，所以在`http` 或者`https` 环境下，都可以优先使用`mse`进行解码。

```
{
    useMSE: true,
    useSIMD:true,
    autoWasm:true,
}
```

### https

如果运行环境是`https`的，并且不想使用 `mse` 解码的，可以按照如下配置。

> wcs的高分辨率的解码能力要强于mse，但是对于视频的兼容性不如mse

```
{
    useWCS: true,
    useSIMD:true,
    autoWasm:true,
}
```

> wcs 对于视频的兼容性不如mse，

```
{
    useWCS: true,
    useSIMD:true,
    autoWasm:true,
}
```

优先使用mse硬解码，如果不支持，降级到simd wasm解码,兜底使用wasm解码。

## 关于解码（useMSE、useWCS、wasm(simd)）优先级

### useMSE

使用的是浏览器提供的`MediaSource`接口，来进行解码。

- 硬解码
- 兼容性好
- ios safari不支持
- 支持H264和H265解码
- 支持http和https

### useWCS

使用的是`WebCodec`接口，来进行解码。

- 硬解码
- 支持H264和H265解码
- 支持https
- ios safari不支持
- 兼容性不如mse

### wasm(simd)

使用的是`webassembly`来进行解码。

- 软解码
- 兼容性好
- 支持H264和H265解码
- 支持http和https

### 优先级

如果同时配置了`useMSE`和`useWCS`，则优先使用`useMSE`，如果`useMSE`不支持，则使用`useWCS`，如果 `useWCS`
不支持，则降级到`wasm`解码。

> useMSE > useWCS > wasm

## mse(wcs) 解码报错的时候，不降级到wasm解码（仍然使用mse或者wcs解码）

> 可能会存在流本身就会存在缺帧的情况（推流端没推完整），会导致mse或者wcs 解码报错。

这边我们希望解码报错的时候，仍然使用mse或者wcs解码，而不是降级到wasm解码。

```
{
    mseDecodeErrorReplay:true,
    wcsDecodeErrorReplay:true,
    autoWasm:false
}
```

## 监听请求流的失效(404)或者500报错

可以监听`play`方法的`catch`

```js
jessibucaPro.play(url).catch((err) => {
    // err 就是错误信息
})
```

> 注意：这个是初次请求的时候，如果流失效，会触发`catch`，如果流有效，但是后面流失效了，不会触发`catch`。

> 播放过程中流发生500报错，会触发`error`事件。

> 播放过程中由于网络切换（网络动荡），导致流失效，会触发`error`事件。

## 关于播放器延迟(超过延迟触发丢帧)设置

目前播放器提供`videoBuffer`和`videoBufferDelay`两个参数来控制延迟。

对于`videoBuffer`参数（单位秒），是用来设置缓存的时长，啥意思呢？

> 就是播放器在接收流数据之后，并没有立马解码播放，还是等待缓存了`videoBuffer`时长的数据之后才去按照一定的频率去解码->播放

在播放过程中，会根据新来流的时间戳结合本地的时间戳，实时计算当前的延迟时长，当延迟小于`videoBufferDelay + videoBuffer`
,则不做任何处理，继续
解码->播放。

> 当最新过来的流数据的时间戳，计算最新的延迟时长大于`videoBufferDelay + videoBuffer`的时候，就会触发，丢帧机制。

## 关于网络延迟

对于网络延迟，是如何计算而来的呢？

> 当流过来的时候，本地也会同步的起一个时间线，因为是直播流，所以理论上如果没有网络延迟的情况下，1s的时间里面，流里面的数据也应该是1s的。

所以根据这个依据，当本地过了`1s`之后，如果计算的流的数据只有`0.9s`的话，那这`1 - 0.9 = 0.1` 的就是网络延迟。

播放器支持配置`networkDelay`参数（单位秒s）,当播放器计算的网络延迟超过了预定设置的`networkDelay`
参数之后，当设置了`networkDelayTimeoutReplay`为true的时候，就会触发`networkDelayTimeout`事件。

业务可以通过监听

```js
jessbucaPro.on(JessibucaPro.EVENTS.networkDelayTimeout, (ts) => {
    // ts 就是网络超时的时间，单位ms(毫秒)
})
```

### 会出现本身延迟很低，但是网络延迟数据却很大

一般这种情况，会出现在机器是4g网络，然后机器向上推流的时候会存在丢帧的逻辑，但是时间戳的逻辑是不会变的，所以会出现这种情况。

比如机器的FPS是25,正常情况下，每秒会有25帧，然后每秒40ms的增加，这样一秒下来，25帧的时间戳就是1000ms。

但是如果存在丢帧的情况，比如丢帧了5帧，那么一秒就只会上传20帧的数据，但是还是每秒40ms的增加，这样一秒下来，20帧的时间戳就是800ms。这样就会少了200ms的数据。

然后结合播放器本地1s的时间，结合流里面的时间戳的时间，就会计算出200ms的网络延迟。


> 基于这种情况，网络延迟的数据，只是一个参考值，不是绝对的。

## 配置低延迟（300ms）解决方案建议【直播流】

因为超低延迟，对于mse 和wcs并不能做到这么低的延迟方案，所以推荐使用wasm simd来实现。

推荐的配置方案：

```
{
    useMSE:false,
    useWCS:false,
    videoBuffer:0.1,
    videoBufferDelay:0.2,
    useSIMD: true,
}
```

如果需要更低的延迟，可以使用`videoBufferDelay`来配置延迟时间，单位是秒。

## 崩溃日志上报

当播放器播放失败，或者崩溃重新播放的时候，会触发`crashLog`事件，可以监听这个事件，来上报崩溃日志。

可以监听`crashLog`事件

```js
jessibuca.on('crashLog', (data) => {

})
```

返回的数据有：

- url： 播放地址
- error： 错误信息
- playType ： 播放类型
- demuxType：解封装类型
- decodeType：解码类型
- renderType: 渲染类型
- videoInfo : 视频信息 {width,height,encType}
- audioInfo : 音频信息 {encType,channels,sampleRate}
- audioEngine ：音频引擎
- allTimes ：播放时长，单位秒

方便业务拿到数据进行数据上报。

## 页面最小化（切换tab）超时检测建议

一般业务有需要监听页面最小化，或者切换tab的事件，来关闭播放器播放，并提示用户。

可以通过设置`pageVisibilityHiddenTimeout` 参数，单位`秒`，目前默认值是`5 * 60` 5分钟。

```js
// 配置信息：
{
    pageVisibilityHiddenTimeout: 5 * 60
}
```

通过监听`visibilityHiddenTimeout`事件，来进行业务逻辑处理。

```js
player.on('visibilityHiddenTimeout', function () {
    // 窗口不可见5分钟超时
});
```

## 播放地址首次就请求失败（非超时）

### 监听`jessibucaPro.EVENTS.error`事件

```js
jessibucaPro.on(JessibucaPro.EVENTS.error, (error) => {
    if (error === JessibucaPro.ERROR.fetchError) {
        // http-flv 错误
    } else if (error === JessibucaPro.ERROR.websocketError) {
        // ws-flv 错误
    }
})
```

### 监听`JessibucaPro.ERROR.fetchError`和`JessibucaPro.ERROR.websocketError`事件

根据自己的业务播放的url地址类型，来监听对应的错误事件。

```js
jessibucaPro.on(JessibucaPro.ERROR.fetchError, (error) => {
    // http-flv 错误
})
jessibucaPro.on(JessibucaPro.ERROR.websocketError, (error) => {
    // ws-flv 错误
})
```

## 页面首次加载超时检测

目前播放器的默认配置是

```
{
    loadingTimeout: 10,
    loadingTimeoutReplay:true,
    loadingTimeoutReplayTimes:3
}
```

> 如果想要如果想无限次重试，可以设置loadingTimeoutReplayTimes为-1

## 页面播放过程中超时检测

目前播放器的默认配置是

```
{
    heartTimeout: 10,
    heartTimeoutReplay:true,
    heartTimeoutReplayTimes:3
}
```

> 如果想要如果想无限次重试，可以设置heartTimeoutReplayTimes为-1

## 录像流集成建议

涉及到录像流的 api 有：

- playback
- playbackPause
- pause
- playbackResume
- forward
- normal
- updatePlaybackForwardMaxRateDecodeIFrame
- setPlaybackStartTime
- getPlaybackCurrentRate
- playbackClearCacheBuffer
- isPlaybackPause

涉及到录像流的 事件 有：

- playbackSeek
- playbackStats
- playbackTimestamp
- playbackPauseOrResume
- playbackPreRateChange
- playbackRateChange

具体集成的demo 可以看 `playback-demo.html`文件

调用的是`playback`方法。

```js
jessibuca.playback(url, {
    // config
})
```

### 24小时UI配置

录像流支持配置24小时组件(支持放大和缩小)和倍率按钮

对于配置参数

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
    ]
})
```

> 未匹配到的时间，在ui上面是不可触发点击事件的。

### 24小时UI点击事件（seek）

当点击了ui上面的时间，会触发`playbackSeek`事件，可以监听这个事件，来进行业务逻辑处理。

```js
jessibucaPro.on('playbackSeek', (data) => {
    // data 为对象
    /**
     * {
        "hour": 1,
        "min": 2,
        "second": 0
        }
     */

    // 拿到时间，去调用服务器端的seek接口
})
```

业务代码可以根据回调的数据，来进行通知服务器端进行seek 操作。

当服务器端更新完了流，可以调用`setPlaybackStartTime`方法，来更新ui的时间。

总体流程： 点击ui获取时间->调用服务器端seek接口->服务器端更新流->调用`setPlaybackStartTime`方法更新ui时间

```js

jessibucaPro.setPlaybackStartTime(1653881963)
```

> ui 也支持更新当前的进度时间

### 暂停（不关闭流）

> 会存在业务上面的录像流暂停，但是不关闭流的需求，这个时候可以调用播放器的`playbackPause()`方法，来暂停播放。

> 暂停的时候会把缓存的数据清空。所以如果不清除缓存可以设置参数`isPlaybackPauseClearCache:true`。

1. 调用服务器端暂停接口
2. 调用播放器的`playbackPause()`方法，画面暂停
3. 调用服务器端恢复接口
4. 调用播放器的`playbackResume()`方法，画面播放

### 暂停（关闭流）

可以调用 `playbackPause(true)` 或者 `pause()` 方法。

### 倍率播放

1. 首先调用服务器端接口，开启倍率播放
2. 调用播放器的`forward(rate)`方法
3. 关闭倍率播放，调用服务器端接口
4. 调用播放器的`forward(1)`或者`normal()`方法

### 倍率播放（配置了rateConfig 参数）

1. 首先选择UI上面的倍率按钮，配合`playbackPreRateChange`事件，拿到倍率，
2. 调用服务器端接口，开启倍率播放。
3. 调用播放器的`forward(rate)`方法。

### seek操作（配合24小时UI）

1. 点击24小时UI，监听`playbackSeek`获取时间
2. 调用服务器端seek接口
3. 服务器端更新流
4. 调用`setPlaybackStartTime`方法，更新ui时间

### 解码前缓存数据

> 会存在有些流，一倍率的时候，但是服务器端推送的流是2倍的，这个时候就需要播放器兼容这种特殊的流，只解码所需的1倍率的数据。

可以通过在调用playback方法播放的时候，config配置参数里面配置`isCacheBeforeDecodeForFpsRender` 参数

```js
jessibucaPro.playback(url, {
    isCacheBeforeDecodeForFpsRender: true
})
```

### 监听当前缓存情况（需要通知服务器端暂停推流）

目前播放器的缓存数据有，`待解码`，`解码后待渲染` 两块缓存数据，单位帧。

> 待解码缓存数据：是指，从服务器端获取到的数据，还没有解码的数据。

> 解码后待渲染缓存数据：是指，解码后的数据，还没有渲染的数据。


可以通过监听`stats`事件，来获取当前的缓存数据。

`playbackVideoBuffer` 指的是`视频待渲染`帧。

`demuxBuffer` 指的是`视频待渲染`帧。

可以通过`stats` 事件里面的fps 来计算出每一帧的时间，然后计算出缓存的时间。

假设fps 为25，那么每一帧的时间就是 1000 / 25 = 40ms。

如果`playbackVideoBuffer` 为100，那么缓存的时间就是 100 * 40 = 4000ms = 4s。如果`demuxBuffer` 为100，那么缓存的时间就是 100

* 40 = 4000ms = 4s。 那么一起的缓存时长就是 4 + 4 = 8s

> stats 事件里面已经添加了 `playbackCacheDataDuration`值。

### 固定倍率播放

> 会存在一些流，服务器端的录像流的推过来的fps不是定码率的，是波动性比较大的那种（一下子推送过来2倍率的数据），这个时候就需要客户端去按照固定倍率去播放。


则需要配置使用固定fps进行渲染，并配置 fps 值。默认值是 `25`

```
playbackConfig:{
    isUseFpsRender:true,
    fps: '', // fps值
}
```

> 播放器也会根据流里面的时间戳，自动校准fps。

### 使用播放器本地时间戳回调时间（从0 开始那种）

> 存在一些流，流里面的时间戳不是从 0 开始的，这个时候，如果录像流想要从0 开始回调时间。


这个时候就需要配置 `playbackConfig` 参数

```
playbackConfig:{
    isUseLocalCalculateTime:true, // 是否使用本地时间来计算playback时间
    localOneFrameTimestamp:40 // 一帧的间隔时间戳 40ms  100 / 25 = 40ms
}
```

业务层通过监听`playbackTime`，`playbackTimestamp` 事件，可以监听到当前播放的时间。

### 当前录像流的播放时间

可以监听`playbackTimestamp`事件，来获取当前播放的时间。

```js
jessibucaPro.on('playbackTimestamp', (data) => {
    // data 为对象
    /**
     * {
        "hour": 1,
        "min": 2,
        "second": 0,
        "ts": "" // 时间戳
        }
     */
})
```

### 配置I帧解码

> 过滤掉其他帧，只解码I帧，可以提高播放性能。

当倍率达到播放器性能瓶颈的时候，就需要配置I帧解码，来提高播放性能。

可以通过调用`updatePlaybackForwardMaxRateDecodeIFrame`方法，来配置I帧解码。

```js
jessibucaPro.updatePlaybackForwardMaxRateDecodeIFrame(4)
```

> 支持在播放前后调用，

### 播放结束

因为播放器播放的是流，所以播放器并不知道这个流的结束时间是多少。

> 播放器唯一能做的就是监听超时。

需要业务层自己结合后端接口来实现。

## 语音通讯集成

具体集成的demo 可以看 `talk-demo.html`文件

> 需要单独引用`jessibuca-pro-talk.js`文件

## 单视频播放

> 只需要将`hasAudio`设置为`false`即可, 播放器就不会解码音频流。

## 单音频播放

具体集成的demo 可以看 `only-audio-demo.html`文件

> 只需要将`hasVideo`设置为`false`即可, 播放器就不会解码视频流。

## 配置自定义底部UI按钮

只需要配置`extendOperateBtns`参数即可

## 配置右键菜单

只需要配置`contextmenuBtns`参数即可

## jessibuca-pro.js decoder-pro.js(decoder-pro-simd.js) decoder-pro.wasm(decoder-pro-simd.wasm)文件想通过CDN加载

因为默认情况下 decoder-pro.js(decoder-pro-simd.js) 是通过相对路径引入 decoder-pro.wasm(decoder-pro-simd.wasm) 文件的。

如果想引用CDN的地址，需要修改成CDN的绝对地址。

所以如果想通过CDN加载，需要修改decoder-pro.js(decoder-pro-simd.js)文件

需要配置`decoder` 参数为CDN绝对地址文件。


> decoder-pro.js 和 decoder-pro-simd.js 文件都需要修改。

> jessibuca-pro.js decoder-pro.js(decoder-pro-simd.js) decoder-pro.wasm(decoder-pro-simd.wasm) 需要放在同一个目录下。

```js
const jessibucaPro = new JessibucaPro({
    decoder: 'https://your-cdn.com/decoder-pro.js',
    ...
})
```

```js
// 修改前 src/worker/index.js

this.decoderWorker = new Worker(player._opt.decoder)

// 修改后 src/worker/index.js
const blob = new Blob([`importScripts("${player._opt.decoder}")`], {"type": 'application/javascript'});
const blobUrl = window.URL.createObjectURL(blob);
this.decoderWorker = new Worker(blobUrl);
```

```js
// 修改前 src/decoder/decoder-pro.js
wasmBinaryFile = 'decoder-pro.wasm';
// 修改后 src/decoder/decoder.js
wasmBinaryFile = 'https://cdn.com/decoder-pro.wasm';
```

```js
// 修改前 src/decoder/decoder-pro-simd.js
wasmBinaryFile = 'decoder-pro-simd.wasm';
// 修改后 src/decoder/decoder-pro-simd.js
wasmBinaryFile = 'https://cdn.com/decoder-pro-simd.wasm';
```

然后需要重新执行下 `npm run build` 命令 就可以了。

## 限制js的使用域名和限制播放url的域名

可以看下`utils`文件夹下面的`verify.js`文件。

对于host的生成，可以看下`utils`文件夹下面的`string2CharCodeHex.js`文件。

## 关于自定义按钮和系统默认提供的按钮

一般现在的问题：

1. 系统自带的按钮顺序和icon不满足业务系统
2. 系统自带的按钮的功能不满足业务系统
3. 希望使用自带的按钮样式和功能（排序也有要求）

### 解决方案：

如果是按钮的功能不满足业务需求，可以通过配置`playFn`,`pauseFn` 等方法来配置自定义的方法、

如果不想要系统提供的按钮，可以通过配置`extendOperateBtns` 参数来配置、支持设置`index`，`icon` ，`active icon `, `click`
, `active click` 等参数

## 关于性能面板

在调试过程中，性能面板是一个非常重要的一个东西，上面显示的是流的实时的数据。

可以通过配置

```
{
    showPerformance:true, // 直接显示性能面板数据
    operateBtns:{
        performance:true // 底部控制条按钮
    }
}
```

## 关于日志输出

关于日志，是主要是控制台输出的内容，可以通过配置`debug:true` 来打开日志输出。

> 不管是否配置了`debug:true`，error的日志默认是强制直接输出的。

默认显示的日志级别是`warn`,可以通过配置`debugLevel:'debug'` 讲日志级别调成全量模式。

> 如果是多屏的输出，可以通过配置`isMulti:true` 来配置播放多实例模式。这个模式下，日志输出会有[uuid] 来区分各个实例日志。

## 播放的时候解除静音

播放器默认播放的时候，是静音播放的。所以如果想播放的时候解除静音，则需要配置`isNotMute:true`就可以了。

> 如果是程序触发的自动播放播放器，是不会有声音的，需要用户手动触发才会有声音。

### 苹果手机默认没有声音。

在设置`isNotMute:true` 在苹果的手机端默认是没有声音的。

> iPhone，chrome等要求自动播放时，音频必须静音，需要由一个真实的用户交互操作来恢复，不能使用代码。


程序里面可以检查下播放器的状态，如果是静音状态，就需要需要出一个ui交互，让用户手动恢复声音播放。

```js
var result = jessibucaPro.isMute()
console.log(result) // true
if (result) {

    // 这里可以出一个ui交互，让用户手动恢复声音播放
}
```

可以结合`audioResume()`方法。

```js
jessibucaPro.audioResume();
```

## 关于水印

### 全屏水印

全屏水印指的是：`播放器整个播放窗口重复显示水印`。

可以通过参数`fullscreenWatermarkConfig` 来实现全屏水印，仅支持文字

### 局部水印

支持：`图片` 和 `文字`

可以通过配置`watermarkConfig` 来实现局部水印。

### 使用那种标签渲染

目前播放器支持`video` 和`canvas`两种标签渲染

可以通过参数 `useVideoRender` 和 `useCanvasRender` 来配置

> 当两者同时设置为true的时候，video的标签渲染优先级大于canvas。

> 录像(TF)流因为要支持倍率播放，所以只支持canvas渲染

## 关于视频录制

目前播放器支持录制 `mp4` 和 `webm` 格式的视频

> mp4 只支持录制视频
>
> webm 格式支持录制视频和音频

## 关于出现绿屏或者花屏的时候

如果流数据的首帧不是I帧，一般情况下，解码了之后，就会很容易出现`绿屏`或者`花屏`情况。

播放器支持配置检查首帧是否是I帧，如果不是I帧就直接过滤掉，直到等到I帧出现才开始解码流数据

只需要配置`checkFirstIFrame:true`

> 默认播放器就是设置为true的，不建议修改为false

如果本身流就是特殊的流，能够保证解码出来的画面不是花屏的，就可以设置为false

## 关于音频引擎

目前播放器支持三种音频引擎来播放音频数据，`worklet`、`script`、`active`

目前播放器会根据当前用户的实际情况来动态选择最适合的播放引擎。

> 会优先判断是否在`微信安卓`环境

### worklet

必须是https 环境下，配置才会生效。

> 默认https环境下，默认就是这个播放引擎。

### script

兼容性最高的播放引擎。

> 一般情况下都是这种引擎。

### active

为了支持在`安卓的微信`环境播放，特殊实现的一种播放引擎。

> 其他环境不建议使用这个引擎。

## ptz 操作

目前ptz操作支持两种交互方式`click` 和`mouseDownAndUp`。

### click

鼠标点击事件

### mouseDownAndUp

鼠标按下，和松开 分别触发事件。

## 加密流播放

> 目前加密流只对接了m7s 的服务器

如果想要播放加密流，需要配置`isCrypto:true` 还有`cryptoKeyUrl: your_href`参数。

> cryptoKeyUrl 是获取秘钥的接口地址。

## 关于集成语音通讯

### 整体逻辑

1. 浏览器端采集麦克风数据
2. 将麦克风数据(rtp包)发送给服务端
3. 服务器端将麦克风数据(rtp包)转发给摄像头
4. 摄像头播放声音

目前支持`单独`或者`集成`在播放器中使用语音通讯

> `单独` 使用需要单独引用`jessibuca-pro-talk.js`文件

目前语音通讯支持采集`pcm`、`g711a`、`g711u`、格式的数据，支持封装成`rtp`包格式。

并支持`checkGetUserMediaTimeout` 和 `getUserMediaTimeout` 参数用来检测获取`getUserMedia` 超时

## 需要监听websocket的事件和调用send 方法

会存在私有的websocket请求，需要授权才能播放，就需要监听`websocket`的`open`事件和调用`send` 方法。

为此，播放器提供了`websocketOpen` 事件和 `sendWebsocketMessage` 方法。

```js
jessibucaPro.on(JessibucaPro.EVENTS.websocketOpen, function () {
    console.log('websocket open')
    // 在这里可以做授权的逻辑
    jessibucaPro.sendWebsocketMessage('授权的消息')
})
```

## 手机端全屏

播放器默认是调用的系统层面的全屏事件。

会存在客户需要手机端全屏，但是不想调用系统层面的全屏事件的情况。可以通过配置`useWebFullScreen` 来实现。

同样的需要监听`webFullscreen` 来监听全屏事件。

## 多屏播放

可以使用`jessibuca-pro-multi.js` 来实现多屏播放。

> 具体demo 看 `multi-demo.html` 文件

> 如果引用了`jessibuca-pro-multi.js`，那么`jessibuca-pro.js`就不需要引用了。

## 关于视频录制的一些问题

### 录制的mp4格式的视频，会有加快倍率播放的现象

因为目前`mp4格式`的录制，是按照固定`25FPS`来录制的。所以如果直播流的fps 只有20，那么录制出来的视频在播放的时候，就会有加快倍率的现象。

### 录制的mp4格式的视频，为啥不支持音频数据。

目前mp4格式的文件，只支持AAC格式封装的音频。但是目前的直播流，音频一般都是G711格式的，所以目前mp4格式的录制，不支持音频数据。

> 虽然也有一些直播流，音频是AAC格式的，但是目前pro的音频解码是放wasm里面做的。所以处理起来会麻烦很多。所以暂不支持音频数据。

## gzip 压缩资源文件

> 可以通过gzip压缩资源文件，来减少资源文件的大小。

#### mac(linux)

安装gzip

[https://formulae.brew.sh/formula/gzip](https://formulae.brew.sh/formula/gzip)

#### jessibuca-pro.js

```
gzip jessibuca-pro.js
mv jessibuca-pro.js.gz jessibuca-pro.js
```

#### decoder-pro.js

```
gzip 和decoder-pro.js
mv 和decoder-pro.js.gz 和decoder-pro.js
```

#### decoder-pro.wasm

```
gzip 和decoder-pro.wasm
mv 和decoder-pro.wasm.gz 和decoder-pro.wasm
```

#### decoder-pro-simd.js

```
gzip 和decoder-pro-simd.js
mv 和decoder-pro-simd.js.gz 和decoder-pro-simd.js
```

#### decoder-pro-simd.wasm

```
gzip 和decoder-pro-simd.wasm
mv 和decoder-pro-simd.wasm.gz 和decoder-pro-simd.wasm
```

#### windows

安装gzip

[http://gnuwin32.sourceforge.net/downlinks/gzip-bin-zip.php](http://gnuwin32.sourceforge.net/downlinks/gzip-bin-zip.php)

解压后，将 `jessibuca-pro.js` 和 `decoder-pro.js`和 `decoder-pro.wasm` 和 `decoder-pro-simd.js`和 `decoder-pro-simd.wasm`
文件拖到 gzip.exe上，文件就压缩好了，也需要去掉.gz后缀

## 如何配置底部自定义按钮（事件）

### 自定义按钮

### 自定义事件

## 异步加载js文件

有客户不想通过script标签来加载js文件，而是想通过异步加载js文件的方式来加载。

可以通过`loadJs`方法来实现。

具体demo见：`async-demo.html`

## 如果业务层需要自己处理播放异常逻辑。

> 会存在客户的流，在一次播放之后，就没法再使用了。所以需要在播放异常的时候，重新请求流的地址。

需要客户把一些参数都关闭掉，不能触发播放器自己内部的重播。

```
{
    autoWasm: false,
    webglAlignmentErrorReplay:false,
    wasmDecodeErrorReplay:false,
    mseDecodeErrorReplay: false,
    wcsDecodeErrorReplay: false,
    networkDelayTimeoutReplay:false,
    heartTimeoutReplay:false,
    loadingTimeoutReplay:false,
    mediaSourceTsIsMaxDiffReplay:false
}


```

可以通过监听`playFailedAndPaused`事件来监听。

然后后续操作：

1. 销毁播放器
2. 重新建立播放器
3. 调用play()方法

```js
jessibucaPro.on('playFailedAndPaused', function (error) {

    //
    jessibucaPro.destroy().then(() => {
        //
        jessibucaPro = null;
        //
        jessibucaPro = new JessibucaPro({
            //
        });
        //
        jessibucaPro.play('新的播放地址');
    }).catch(() => {

    });
});
```

## 如果业务层需要处理移动端切换网络环境导致的播放器断开连接的问题。

### 就是在断开网络的时候，会触发`error`事件。业务需要监听这个事件，然后待网络恢复的时候。

```js
jessibuca.on('error', function (error) {
    if (error === jessibuca.ERROR.fetchError || error === jessibuca.ERROR.websocketError) {
        // 检查如果是网络断开了，销毁掉播放器。
        jessibuca.destroy().then(() => {
            jessibuca = null;
            // 检查网络是否已经连接了。
            // createPlayerAndPlay();

            //否则：
            // todo: 等待网络恢复的时候，重新建立播放器。
        }).catch(() => {

        });
    }
})
```

待网络恢复的时候。

```js
jessibuca.on('error', function (error) {
    if (error === jessibuca.ERROR.fetchError || error === jessibuca.ERROR.websocketError) {
        // 检查如果是网络断开了，销毁掉播放器。
        jessibuca.destroy().then(() => {
            jessibuca = null;
            // 检查网络是否已经连接了。
            // createPlayerAndPlay();

            //否则：
            // todo: 等待网络恢复的时候，重新建立播放器。
        }).catch(() => {

        });
    }
})
```

