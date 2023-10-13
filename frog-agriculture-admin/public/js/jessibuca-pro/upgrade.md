# 开源版 升级到 Pro 版本

## 替换文件

需要将原本开源版的

- jessibuca.js
- decoder.js
- decoder.wasm

替换成

- jessibuca-pro.js
- decoder-pro.js
- decoder-pro.wasm
- decoder-pro-simd.js
- decoder-pro-simd.wasm

> 如果有多屏需求，可以将`jessibuca-pro.js`替换成`jessibuca-pro-multi.js`。

> `jessibuca-pro.js` 与`jessibuca-pro-multi.js`只需要引用一个即可（不需要两个同时引用）。

> `decoder-pro-simd.js`是simd解码器（适用于高分辨率解码）

## 替换方法

将原本 `new Jessibuca()` 的地方替换成 `new JessibucaPro()`

```js
// 原本的
const jessibuca = new Jessibuca({
    // ...
})
```

```js
// 替换成
const jessibucaPro = new JessibucaPro({
    // ...
})
```
```js
// 替换成 多屏
const jessibucaProMulti = new JessibucaProMulti({
    // ...
})
```



需要将原本开源版的 `destroy()` 从同步方法，修改为异步方法

```js
// 原本的
jessibuca.destroy()
```

```js

// 替换成
await jessibucaPro.destroy()
// 或者
jessibucaPro.destroy().then(() => {
    // ...
})
```

## 服务器配置wasm支持

最新版本的chrome（version >= 112）貌似对于wasm 格式返回 `application/otct-stream` 的文件会报错，需要服务器配置支持


### nginx
#### 方法一：（推荐）
在 `mime.types` 文件中添加

```nginx
application/wasm wasm;
```

#### 方法二：

```nginx
location ~* \.wasm$ {
    add_header Content-Type application/wasm;
}
```

### IIS

在 `web.config` 文件中添加

```xml
<staticContent>
    <mimeMap fileExtension=".wasm" mimeType="application/wasm" />
</staticContent>
```

### apache

在 `httpd.conf` 文件中添加

```apache
AddType application/wasm .wasm
```



