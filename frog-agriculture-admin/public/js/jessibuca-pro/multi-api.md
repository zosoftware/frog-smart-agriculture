# 播放器（Multi）API

## JessibucaProMulti(options)

options 支持的参数有：

### container

等同于 `JessibucaPro` 的 `container` 参数

### maxSplit

- **类型**：`number`
- **默认值**：`4`
- **用法**：

最大分屏数

### split

- **类型**：`number`
- **默认值**：`1`
- **用法**：

当前分屏数

### style

- **类型**：`number`
- **默认值**：`1`
- **参数**：
    - `{string} border`  `选填` 边框颜色 `默认：#343434`
    - `{string} borderSelect` `选填`  边框选中颜色 `默认：#FFCC00`
    - `{string} background` `选填`  背景颜色 `默认：#000`

### supportDblclickContainerFullscreen

- **类型**：`boolean`
- **默认值**：`false`
- **用法**：

是否支持双击单个播放窗口，使之全屏到container容器大小。

> 双击切换container内全屏/取消全屏

> 如果设置为true了，则会影响 `supportDblclickFullscreen` 这个参数的生效。默认会在内部把`supportDblclickFullscreen` 改为false。

### 其他参数

其他参数等同于 `JessibucaPro` 的参数

## 静态变量

### MULTI_EVENTS

事件里面的`key`值

播放器对外的事件

```js
jessibucaPro.on(JessibucaPro.MULTI_EVENTS.blur, () => {
    // 窗口不可见
})
```

### ERROR

播放器对外的事件

等同于 `JessibucaPro` 的 `ERROR`

事件`error`里面的value 的值

### EVENTS

播放器对外的事件

等同于 `JessibucaPro` 的 `EVENTS`

事件里面的`key`值

```js
jessibucaPro.on(JessibucaPro.EVENTS.selected, () => {
    // 窗口不可见
})
```

## 方法

### destroySingle(windowIndex)
- **参数**：
    - `{number} windowIndex` `选填`  窗口下标(从0开始)，如果不传，则销毁目前选中的窗口
- **返回**：
    - `{Promise}`
- **用法**： 销毁单个播放窗口播放器实例

```js
jessibucaPro.destroySingle(0).then(() => {
    // 销毁成功
}).catch((error) => {
    // 销毁失败
})
```


### destroy()
- **返回**：
    - `{Promise}`
- **用法**： 销毁整个jessibucaProMulti实例

```js

jessibucaPro.destroy().then(() => {
    // 销毁成功
}).catch((error) => {
    // 销毁失败
})
```

### setFullscreenMulti(flag)
- **参数**：
    - `{boolean} flag` `必填`  是否全屏
- **返回**：
    - `{Promise}`
- **用法**： 整个播放全屏/取消全屏


```js
jessibucaPro.setFullscreenMulti(true).then(() => {
    // 全屏成功
}).catch((error) => {
    // 全屏失败
})

jessibucaPro.setFullscreenMulti(false).then(() => {
    // 取消全屏成功
}).catch((error) => {
    // 取消全屏失败
})
```


### selectWindow(windowIndex)
- **参数**：
    - `{number} windowIndex` `选填`  窗口下标(从0开始)，如果不传，则销毁目前选中的窗口
- **返回**：
    - `{void}`
- **用法**： 选择某个播放窗口



### arrangeWindow(split)
- **参数**：
    - `{number|string} split` `必填` 数量 num: `1`, `2`, `3`, `4`, 特殊 str: `'3-1'`,`'4-1'`
- **返回**：
    - `{void}`
- **用法**： 切换分屏数 `1*1`，`2*2`，`3*3`，`4*4` `不规则尺寸3-1`，`不规则尺寸4-1`

```js
jessibucaPro.arrangeWindow(1)
jessibucaPro.arrangeWindow(2)
jessibucaPro.arrangeWindow(3)
jessibucaPro.arrangeWindow(4)

jessibucaPro.arrangeWindow('3-1')
jessibucaPro.arrangeWindow('4-1')
```

### getCurrentSplit()
- **返回**：
    - `{number}`
- **用法**： 获取当前分屏数

```js
const split = jessibucaPro.getCurrentSplit()
console.log(split)
```

### getSelectedWindowIndex()
- **返回**：
    - `{number}`
- **用法**： 获取当前选中的播放窗口下标

```js
const windowIndex = jessibucaPro.getSelectedWindowIndex()
console.log(windowIndex)
```

### 其他方法

其他方法等同于 `JessibucaPro` 的方法，只是多了一个参数 `index`，用来指定操作的第几个播放器窗口，`下标从0开始`

例如 `play` 方法

```js
jessibucaProMulti.play(url, options, index).then(() => {

}).catch((error) => {

});
```

```js
jessibucaProMulti.play(url, index).then(() => {

}).catch((error) => {

});
```

## 事件

### dblSelected

双击选中事件

```js
jessibucaProMulti.on('dblSelected', (index) => {
    console.log('dblSelected', index);
})
```

### selected

单击选中事件

```js
jessibucaProMulti.on('selected', (index) => {
    console.log('selected', index);
})
```

### mouseOver

鼠标移入事件

```js
jessibucaProMulti.on('mouseOver', (index) => {
    console.log('mouseOver', index);
})
```

### mouseOut

鼠标移出事件

```js
jessibucaProMulti.on('mouseOut', (index) => {
    console.log('mouseOut', index);
})
```

### mouseUp

鼠标mouseUp事件

```js
jessibucaProMulti.on('mouseUp', (index) => {
    console.log('mouseUp', index);
})
```

### 其他事件

其他事件等同于 `JessibucaPro` 的事件

事件的第一个参数是`index` 剩下的参数和`JessibucaPro`的同等事件一样

```js
jessibucaProMulti.on('xxx', (index, value) => {
    console.log('xxx', index, value);
})
```
