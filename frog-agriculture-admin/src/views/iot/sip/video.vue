<template>
<div class="components-container">
    <el-row>
        <span style="overflow: auto;">选择录像日期：</span>
        <el-date-picker v-if="videoVod" v-model="queryDate" type="date" size="small" clearable placeholder="选择日期" @change="loadDevRecord" />
    </el-row>
    <el-divider />
    <el-row v-loading="playerLoading">
        <div class="player-wrapper" style="margin-top:-20px;">
            <div ref="player" class="player" style="">
                <div class="container-shell">
                    <div id="container" ref="container" />
                </div>
            </div>
            <div class="player-display" />
        </div>
        <el-col :span="24">
            <el-row type="flex" align="middle" class="ctrl ctrl-btn">
                <el-col :span="24" :xs="24" :sm="24" style="overflow: auto;display: flex;">
                    <el-button v-if="!playing" class="comm-margin" type="primary" icon="el-icon-video-play" size="small" @click="preparePlay">播放</el-button>
                    <el-button v-if="playing" class="comm-margin" type="danger" icon="el-icon-video-pause" size="small" @click="stopPlay">停止</el-button>
                    <el-button v-if="!quieting" icon="el-icon-close-notification" size="small" class="comm-margin" type="info" @click="mute">静音</el-button>
                    <el-button v-if="quieting" icon="el-icon-bell" size="small" class="hidden-xs-only comm-margin" type="warning" @click="cancelMute">放音</el-button>
                    <el-slider v-model="volume" :disabled="quieting" :format-tooltip="formatVolumeToolTip" class="hidden-xs-only" style="width: 100px;margin: 0 30px 0 0;" @change="changeVolume" />
                    <el-button icon="el-icon-full-screen" size="small" class="hidden-xs-only" @click="fullscreen">全屏</el-button>
                    <el-button icon="el-icon-camera-solid" size="small" @click="screenShot">截图</el-button>
                    <el-button v-if="pausing" class="comm-margin" type="primary" icon="el-icon-video-play" size="small" :disabled="!playing" @click="resume">恢复</el-button>
                    <el-button v-if="!pausing" class="comm-margin" type="danger" icon="el-icon-video-pause" size="small" :disabled="!playing" @click="pause">暂停</el-button>
                </el-col>
            </el-row>
        </el-col>
    </el-row>
</div>
</template>

<script>
import {
    parseTime
} from '@/utils'
import 'element-ui/lib/theme-chalk/display.css'
import {
    playback,
    playbackStop,
    playbackPause,
    playbackReplay,
    playbackSeek,
    playbackSpeed,
} from "@/api/iot/channel";

import {
    getDevRecord,
    getRecord,
} from "@/api/iot/record";

export default {
    name: 'Video',
    jessibuca: null,
    data() {
        return {
            defaultProps: {
                children: 'children',
                label: 'label'
            },
            RecordSn: '',
            expandIds: [],
            filterText: '',
            groupLoading: false,
            groupData: [],
            selectData: {},
            treeVisible: false,
            curInfo: '',
            protocolFlag: true,
            selectDeviceId: '34020000001320000003',
            selectChannelId: '34020000001320000002',
            channelData: [],
            videoVod: true,
            vodIndex: 0,
            vodData: {},
            playerLoading: false,
            url: {
                ssrc: '',
                flv: ''
            },
            hisData: [],
            queryDate: '',
            playing: false,
            quieting: true,
            pausing: false,
            paused: false,
            volume: 100,
            // speedEnum: [{ name: '快进: X1', value: 1 }, { name: '快进: X2', value: 2 }, { name: '快进: X3', value: 3 }, { name: '快进: X4', value: 4 }],
            speed: 1,
            orientation: 1
        }
    },
    watch: {
        filterText(val) {
            this.$refs.tree.filter(val)
        }
    },
    mounted() {
        this.expandIds = ['0']
        //this.loadChannelData()
        this.create()
    },
    beforeDestroy() {
        this.cleanPlayer(true)
    },
    methods: {
        loadChannelData() {
            this.channelData = null
            const arr = channelData.split('_')
            this.selectDeviceId = arr[0]
            this.selectChannelId = arr[1]
        },
        create() {
            const mobile = document.body.clientWidth < 720
            const jessibuca = new window.Jessibuca({
                container: this.$refs.container,
                videoBuffer: Number(0.2), // 缓存时长
                decoder: "/js/jessibuca/decoder.js",
                timeout: 20,
                debug: true,
                isResize: false,
                loadingText: '加载中',
                // hasAudio: false,
                isFlv: true,
                showBandwidth: true,
                supportDblclickFullscreen: true,
                operateBtns: {
                    fullscreen: mobile,
                    screenshot: false,
                    play: false,
                    audio: false
                },
                forceNoOffscreen: true,
                isNotMute: false,
                playbackForwardMaxRateDecodeIFrame: 4,
                playbackCurrentTimeMove: false
            })

            this.$options.jessibuca = jessibuca
            const _this = this

            jessibuca.on('error', function (error) {
                console.log('error')
                console.log(error)
                _this.destroy()
            })
            jessibuca.on('pause', function (pause) {
                console.log('pause success!')
                console.log(pause)
            })
            jessibuca.on('stats', function (s) {
                console.log('stats is', s)
            })
            jessibuca.on('timeout', function () {
                console.log('timeout')
                _this.destroy()
                if (_this.retryCount <= 1) {
                    _this.startPlay(_this.url)
                    _this.retryCount++
                }
            })
            let pre = 0
            let cur = 0
            jessibuca.on('timeUpdate', function (ts) {
                cur = parseInt(ts / 60000)
                if (pre !== cur) {
                    pre++
                }
            })
            if (this.videoVod) {
                jessibuca.on('playbackSeek', (timeObj) => {
                    _this.seekPlay(timeObj)
                })
            }
        },
        destroy() {
            console.log('destroy')
            if (this.$options.jessibuca) {
                this.$options.jessibuca.destroy()
            }
            this.create()
        },
        preparePlay() {
            this.cleanPlayer(false)
            this.loadDevRecord()
        },
        cleanPlayer(stop) {
            this.destroy()
            if (stop) {
                this.stopPlay()
            }
        },
        mute() {
            if (this.playing) {
                this.$options.jessibuca.mute()
                this.quieting = true
            }
        },
        cancelMute() {
            if (this.playing) {
                this.$options.jessibuca.cancelMute()
                this.quieting = false
            }
        },
        formatVolumeToolTip(index) {
            return '音量条: ' + index
        },
        changeVolume() {
            this.$options.jessibuca.setVolume(this.volume / 100)
        },
        screenShot() {
            if (this.playing) {
                this.$options.jessibuca.screenshot()
            }
        },
        fullscreen() {
            if (this.playing) {
                this.fullscreening = !this.fullscreening
                this.$options.jessibuca.setFullscreen(this.fullscreening)
            }
        },
        initUrl(data) {
            if (data) {
                this.url.ssrc = data.streamId
                this.url.flv = data.flv
            } else {
                this.url.ssrc = ''
                this.url.flv = ''
            }
        },
        triggerPlay(playTimes) {
            if (this.playing) {
                clearTimeout(this.timer)
                this.$options.jessibuca.on('play', () => {
                    this.quieting = this.$options.jessibuca.quieting
                })
                if (this.videoVod) {
                    this.$options.jessibuca.playback(this.url.flv, {
                        playList: playTimes,
                        fps: 20
                    })
                    this.timer = setTimeout(() => {
                        this.playerLoading = false
                    }, 2000)
                }
            } else {
                this.playerLoading = false
            }
        },
        play(playTimes) {
            if (this.selectDeviceId && this.selectChannelId) {
                this.playerLoading = true
                this.pausing = false
                if (this.url.ssrc) {
                    playbackStop(this.selectDeviceId, this.selectChannelId).then(res => {
                        const query = {
                            start: this.vodData.start,
                            end: this.vodData.end,
                        }
                        playback(this.selectDeviceId, this.selectChannelId, query).then(res => {
                            this.playing = true
                            this.initUrl(res.data)
                        }).finally(() => {
                            this.triggerPlay(playTimes)
                        })
                    }).catch(() => {
                        this.playerLoading = false
                    })
                } else {
                    const query = {
                        start: this.vodData.start,
                        end: this.vodData.end,
                    }
                    playback(this.selectDeviceId, this.selectChannelId, query).then(res => {
                        console.log(res)
                        this.playing = true
                        this.initUrl(res.data)
                    }).finally(() => {
                        this.triggerPlay(playTimes)
                    })
                }
            }
        },
        playNext(index) {
            this.playerLoading = true
            this.url.flv = ''
            playbackStop(this.selectDeviceId, this.selectChannelId).then(res => {
                this.playing = false
                this.url.ssrc = ''
            }).finally(() => {
                this.playerLoading = false
            })
        },
        scalePlay() {
            if (this.url.ssrc && this.playing) {
                playbackSpeed(this.selectDeviceId, this.selectChannelId, this.speed).then(res => {
                    this.$refs.player.scale(this.speed)
                })
            }
        },
        seekPlay(s) {
            const curTime = this.vodData.base + s.hour * 3600 + s.min * 60 + s.second
            const seekRange = curTime - this.vodData.start
            if (this.url.ssrc && this.playing) {
                const query = {
                    seek: seekRange,
                }
                playbackSeek(this.selectDeviceId, this.selectChannelId, query).then(res => {
                    this.$options.jessibuca.setPlaybackStartTime(curTime)
                })
            }
        },
        pause() {
            if (this.url.ssrc && this.playing) {
                if (this.videoVod) {
                    playbackPause(this.selectDeviceId, this.selectChannelId).then(res => {
                        if (res.code === 1) {
                            this.pausing = true
                            this.$options.jessibuca.pause()
                        }
                    })
                } else {
                    this.$options.jessibuca.pause()
                    this.pausing = true
                }
            }
        },
        resume() {
            if (this.url.ssrc) {
                if (this.pausing) {
                    playbackReplay(this.selectDeviceId, this.selectChannelId).then(res => {
                        if (res.data === 'no channel info') {
                            this.pausing = false
                            this.play()
                        } else {
                            this.$options.jessibuca.play()
                            this.pausing = false
                        }
                    })
                }
            }
        },
        isPause(ispause) {
            this.paused = ispause
        },
        stopPlay() {
            if (this.url.ssrc && this.playing) {
                this.playerLoading = true
                this.pausing = false
                this.url.flv = ''
                playbackStop(this.selectDeviceId, this.selectChannelId).then(res => {
                    this.playing = false
                    if (this.$options.jessibuca) {
                        this.retryCount = 0
                        this.destroy()
                    }
                    this.url.ssrc = ''
                }).finally(() => {
                    this.playerLoading = false
                })
            }
        },
        changeSpeed() {
            if (this.speed !== val) {
                this.speed = val
                this.scalePlay()
            }
        },
        loadDevRecord() {
            if (this.selectDeviceId && this.selectChannelId) {
                const date = this.queryDate ? new Date(this.queryDate).getTime() : new Date(new Date().toLocaleDateString()).getTime()
                const start = date / 1000
                const end = Math.floor((date + 24 * 60 * 60 * 1000 - 1) / 1000)
                const query = {
                    start: start,
                    end: end,
                }
                getDevRecord(this.selectDeviceId, this.selectChannelId, query).then(res => {
                    this.hisData = res.data.recordItems
                    if (res.data.recordItems) {
                        const len = this.hisData.length
                        if (len > 0) {
                            if (this.hisData[0].start < start) {
                                this.vodData = {
                                    start: start,
                                    end: end,
                                    base: start
                                }
                                this.hisData[0].start = start
                            } else {
                                this.vodData = {
                                    start: this.hisData[0].start,
                                    end: end,
                                    base: start
                                }
                            }
                            this.play(this.hisData)
                        } else {
                            this.$message({
                                type: 'warning',
                                message: '当前通道没有录像'
                            })
                        }
                    } else {
                        this.$message({
                            type: 'warning',
                            message: '当前通道没有录像'
                        })
                    }
                }).catch(() => {
                    this.playerLoading = false
                })
            }
        }
    }
}
</script>

<style lang="scss">
.player-wrapper {
    position: relative;
    width: 90%;
    height: 630px;
    margin: 5px auto;

    .player {
        position: relative;
        width: 100%;
        height: 100%;
    }

    .player-display {
        position: absolute;
        left: 4px;
        bottom: 48px;
        display: flex;
        width: 150px;
        height: 30px;
        justify-content: center;
        color: #fff;
    }
}

.m-d-10 {
    margin-block: 8px;
}

.m-d-11 {
    display: block;
}

.el-select .el-input {
    width: 100%;
}

.el-radio {
    min-width: 140px;
    margin-top: 3px;
}

.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #F56C6C;
    color: #ffffff;

    .el-button--text {
        color: #ffffff;
    }
}

.el-divider--horizontal {
    margin: 20px 0;
}

.el-popover {
    height: 500px;
    overflow: auto;
}
</style><style lang="scss" scoped>
.components-container {
    position: relative;
    border: 1px solid #eeeeee;
    padding: 8px 10px;
}

.el-form-item {
    margin-bottom: 0;
}

.el-dropdown {
    font-size: 12px;
}

.el-divider--vertical {
    height: 100%;
}

.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}

.ctrl {
    padding: 5px;
    border: 1px solid #DCDFE6;
    margin: 0 auto;
}

.ctrl-timeline {
    width: 100%;
}

.ctrl-btn {
    width: 100%;
}

.comm-margin {
    margin-right: 10px;
}

.time-rule {
    position: relative;
    height: 50px;
    width: 1440px;
    font-size: 12px;
    background-color: #CCC;
    margin: 3px auto 5px;
}

.time-day {
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 1440px;
    cursor: pointer;
}

.time-minute {
    float: left;
    width: 1px;
    height: 8px;
    margin: 0;
    /*background: green*/
}

.time-minute.active {
    background-color: green;
}

.time-text {
    float: left;
    width: 60px;
    border-left: 1px solid #999;
    border-top: 1px solid #999;
    -ms-user-select: none;
    user-select: none;
    text-align: center;
    height: 25px;
    line-height: 25px;
}

.time-00 {
    border-left: 0;
}

.time-cursor {
    position: absolute;
    left: 0;
    top: 0;
    height: 30px;
    width: 1px;
    background-color: red;
    text-align: center;
}

.time-cursor-text {
    position: absolute;
    padding: 0 5px;
    width: 60px;
    left: -30px;
    top: 30px;
    border: 1px solid red;
    height: 15px;
    line-height: 15px;
    cursor: move;
    background-color: white;
    -ms-user-select: none;
    user-select: none;
}

.scroller::-webkit-scrollbar {
    width: 8px;
    height: 8px;
}

.scroller::-webkit-scrollbar-track {
    background-color: #F5F5F5;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}

.scroller::-webkit-scrollbar-thumb {
    background-color: #c8c8c8;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}
</style>
