<template>
  <div id="devicePlayer" v-loading="isLoging">
    <el-dialog title="视频播放" top="0" :close-on-click-modal="false" :visible.sync="showVideoDialog" @close="close()">
      <div class="player-wrapper">
        <div ref="player" class="player" style="">
          <div class="container-shell">
            <div id="container" ref="container"/>
          </div>
        </div>
        <div class="player-display"/>
      </div>
      <el-tabs v-model="tabActiveName" @tab-click="tabHandleClick" style="padding-bottom:15px;">
        <!--遥控界面-->
        <el-tab-pane label="设备直播" name="media">
          <el-col :span="24">
            <div style="display: flex; justify-content: left;">
              <div class="control-wrapper">
                <div class="control-btn control-top" @mousedown="ptzDirection(0, 1)" @mouseup="ptzDirection(0, 0)">
                  <i class="el-icon-caret-top"></i>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-btn control-left" @mousedown="ptzDirection(2, 0)" @mouseup="ptzDirection(0, 0)">
                  <i class="el-icon-caret-left"></i>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-btn control-bottom" @mousedown="ptzDirection(0, 2)" @mouseup="ptzDirection(0, 0)">
                  <i class="el-icon-caret-bottom"></i>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-btn control-right" @mousedown="ptzDirection(1, 0)" @mouseup="ptzDirection(0, 0)">
                  <i class="el-icon-caret-right"></i>
                  <div class="control-inner-btn control-inner"></div>
                </div>
                <div class="control-round">
                  <div class="control-round-inner"><i class="fa fa-pause-circle"></i></div>
                </div>

                <div style="position: absolute; left: 8.25rem; top: 1.1rem;cursor:pointer;display:flex;width:100px;"
                     @mousedown="ptzScale(1)" @mouseup="ptzScale( 0)">
                  <svg-icon icon-class="big" style="font-size:30px;"/>
                  <span style="line-height:30px;color:#999;margin-left:6px;">放大</span></div>
                <div style="position: absolute; left: 8.25rem; top: 3.3rem; cursor:pointer;display:flex;width:100px;"
                     @mousedown="ptzScale(2)" @mouseup="ptzScale(0)">
                  <svg-icon icon-class="small" style="font-size:30px;"/>
                  <span style="line-height:30px;color:#999;margin-left:6px;">缩小</span></div>
                <div class="contro-speed" style="position: absolute; left: 4px; top: 7rem; width: 9rem;">
                  <el-slider v-model="controSpeed" :max="255"></el-slider>
                </div>
              </div>
            </div>
          </el-col>
        </el-tab-pane>

        <el-tab-pane label="录像回放" name="record">
          <div class="components-container">
            <div v-if="videoVod">
              <el-row style="margin-bottom:34px;">
                <span style="overflow: auto;">选择录像日期：</span>
                <el-date-picker v-model="queryDate" type="date" size="small" clearable placeholder="选择日期"
                                @change="loadDevRecord"/>
              </el-row>
            </div>
            <el-row v-loading="playerLoading">
              <el-col :span="24">
                <el-row type="flex" align="middle" class="ctrl ctrl-btn">
                  <el-col :span="24" :xs="24" :sm="24" style="overflow: auto;display: flex">
                    <el-button v-if="!playing" type="primary" icon="el-icon-video-play" size="mini"
                               @click="preparePlay">播放
                    </el-button>
                    <el-button v-if="playing" type="danger" icon="el-icon-video-pause" size="mini" @click="stopPlay">
                      停止
                    </el-button>
                    <el-button v-if="pausing" type="primary" icon="el-icon-video-play" size="mini" :disabled="!playing"
                               @click="resume">恢复
                    </el-button>
                    <el-button v-if="!pausing" type="danger" icon="el-icon-video-pause" size="mini" :disabled="!playing"
                               @click="pause">暂停
                    </el-button>
                    <el-slider v-model="volume" :disabled="quieting" :format-tooltip="formatVolumeToolTip"
                               class="hidden-xs-only" style="width: 100px;margin: 0 30px 0 30px;"
                               @change="changeVolume"/>
                    <el-button icon="el-icon-full-screen" size="mini" class="hidden-xs-only" @click="fullscreen">全屏
                    </el-button>
                    <el-button icon="el-icon-camera-solid" size="mini" @click="screenShot">截图</el-button>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import {
  startPlay,
  stopPlay,
  getStreaminfo,
  playback,
  playbackPause,
  playbackReplay,
  playbackSeek,
  playbackSpeed,
  playbackStop
} from "@/api/iot/channel";
import {
  ptzdirection,
  ptzscale
} from "@/api/iot/sipdevice";
import {
  getDevRecord
} from "@/api/iot/record";

export default {
  name: 'devicePlayer',
  props: {},
  jessibuca: null,
  computed: {
    getPlayerShared: function () {
      return {
        sharedflv: this.streamInfo.flv,
        sharedws_flv: this.streamInfo.ws_flv,
        sharedts: this.streamInfo.ts,
        sharedws_ts: this.streamInfo.ws_ts,
        sharedrtmp: this.streamInfo.rtmp,
        sharedrtsp: this.streamInfo.rtsp,
      };
    }
  },
  beforeDestroy() {
    this.cleanPlayer(true)
  },
  data() {
    return {
      tabActiveName: 'media',
      volume: 100,
      deviceId: '',
      channelId: '',
      streamId: '',
      streamInfo: {},
      video: '',
      videoUrl: '',
      hasaudio: false,
      isLoging: false,
      showVideoDialog: false,
      controSpeed: 30,
      videoVod: false,
      vodIndex: 0,
      vodData: {},
      playerLoading: false,
      url: {
        ssrc: '',
        playurl: ''
      },
      hisData: [],
      queryDate: '',
      playing: false,
      quieting: true,
      pausing: false,
      paused: false,
    };
  },
  methods: {
    initUrl(data) {
      if (data) {
        this.streamId = data.ssrc
        this.url.ssrc = data.ssrc
        this.url.playurl = data.playurl
      } else {
        this.streamId = ''
        this.url.ssrc = ''
        this.url.playurl = ''
      }
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
        loadingText: '商用版请购买授权,加载中',
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
        console.log('create videoVod')
        jessibuca.on('playbackSeek', (timeObj) => {
          _this.seekPlay(timeObj)
        })
      }
    },
    cleanPlayer(stop) {
      this.destroy()
      if (stop) {
        this.stopPlay()
      }
      this.playing = false
    },
    destroy() {
      if (this.$options.jessibuca) {
        this.$options.jessibuca.destroy()
      }
      if (this.showVideoDialog) {
        this.create();
      }
    },
    tabHandleClick: function (tab, event) {
      this.cleanPlayer(true);
      switch (this.tabActiveName) {
        case "media":
          this.videoVod = false;
          this.sendDevicePush();
          break;
        case "record":
          this.videoVod = true;
          break;
      }
    },
    openDialog: function (tab, deviceId, channelId, param) {
      this.tabActiveName = tab;
      this.channelId = channelId;
      this.deviceId = deviceId;
      this.streamInfo = param.streamInfo;
      this.streamId = param.streamInfo.streamId;
      this.videoUrl = param.streamInfo.flv;
      this.hasaudio = param.hasAudio;
      this.showVideoDialog = true;
      this.$nextTick(() => {
        this.preparePlay();
      })
    },
    preparePlay: function () {
      this.cleanPlayer(false)
      switch (this.tabActiveName) {
        case "media":
          this.play(this.hasAudio)
          break;
        case "record":
          this.loadDevRecord()
          break;
      }
    },
    //直播
    sendDevicePush: function () {
      console.log("通知设备推流1：" + this.deviceId + " : " + this.channelId);
      startPlay(this.deviceId, this.channelId).then((response) => {
        console.log("开始播放：" + this.deviceId + " : " + this.channelId);
      });
      getStreaminfo(this.deviceId, this.channelId).then((response) => {
        console.log("流媒体信息：" + response.data);
        let res = JSON.parse(response.data);
        console.log("playurl：" + res.playurl);
        this.isLoging = false;
        this.playing = true;
        this.playing = true;
        this.streamId = res.streamId;
        this.videoUrl = res.playurl;
        this.$options.jessibuca.play(this.videoUrl);
      });
    },
    play: function (hasAudio) {
      this.hasaudio = hasAudio;
      this.isLoging = false;
      this.playing = true;
      this.$options.jessibuca.play(this.videoUrl);
    },
    close: function () {
      if (this.$options.jessibuca) {
        console.log('jessibuca播放器销毁');
        this.$options.jessibuca.destroy();
      }
      if (this.streamId && this.playing) {
        stopPlay(this.deviceId, this.channelId, this.streamId).then(response => {
          console.log("停止推流 streamId：" + this.streamId);
        });
      }
      this.videoUrl = '';
      this.queryDate = '';
      this.streamId = '';
      this.videoVod = false;
      this.showVideoDialog = false;
      this.url = {
        ssrc: '',
        flv: ''
      };
    },
    //云台控制
    ptzDirection: function (leftRight, upDown) {
      console.log('云台方向控制：' + leftRight + ' : ' + upDown);
      var data = {
        leftRight: leftRight,
        upDown: upDown,
        moveSpeed: 125
      }
      ptzdirection(this.deviceId, this.channelId, data).then((response) => {
        console.log("云台方向控制：" + JSON.stringify(response));
      });
    },

    ptzScale: function (inOut) {
      console.log('云台缩放：' + inOut);
      var data = {
        inOut: inOut,
        scaleSpeed: 30
      }
      ptzscale(this.deviceId, this.channelId, data).then((response) => {
        console.log("云台方向控制：" + JSON.stringify(response));
      });
    },
    //录像控制
    loadDevRecord() {
      this.cleanPlayer(false);
      if (this.deviceId && this.channelId) {
        const date = this.queryDate ? new Date(this.queryDate).getTime() : new Date(new Date().toLocaleDateString()).getTime()
        const start = date / 1000
        const end = Math.floor((date + 24 * 60 * 60 * 1000 - 1) / 1000)
        const query = {
          start: start,
          end: end,
        }

        getDevRecord(this.deviceId, this.channelId, query).then(res => {
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
              this.playbackstart(this.hisData)
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
    },
    triggerPlay(playTimes) {
      if (this.playing) {
        clearTimeout(this.timer)
        this.$options.jessibuca.on('play', () => {
          this.quieting = this.$options.jessibuca.quieting
        })
        if (this.videoVod) {
            console.log('backurl',this.url.playurl);
          this.$options.jessibuca.playback(this.url.playurl, {
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
    playbackstart(playTimes) {
      if (this.deviceId && this.channelId) {
        this.playerLoading = true
        this.pausing = false
        if (this.url.ssrc) {
          playbackStop(this.deviceId, this.channelId, this.url.ssrc).then(res => {
            const query = {
              start: this.vodData.start,
              end: this.vodData.end,
            }
            playback(this.deviceId, this.channelId, query).then(res => {
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
          playback(this.deviceId, this.channelId, query).then(res => {
              console.log('playBack',res.data);
            this.playing = true
            this.initUrl(res.data)
          }).finally(() => {
            this.triggerPlay(playTimes)
          })
        }
      }
    },
    stopPlay() {
      if (this.streamId && this.playing) {
        this.playerLoading = true
        this.pausing = false
        this.url.playurl = ''
        playbackStop(this.deviceId, this.channelId, this.streamId).then(res => {
          this.playing = false
          if (this.$options.jessibuca) {
            this.retryCount = 0
            this.destroy()
          }
          this.url.ssrc = ''
          this.streamId = ''
        }).finally(() => {
          this.playerLoading = false
        })
      }
    },
    playNext(index) {
      this.playerLoading = true
      this.url.playurl = ''
      playbackStop(this.deviceId, this.channelId).then(res => {
        this.playing = false
        this.url.ssrc = ''
      }).finally(() => {
        this.playerLoading = false
      })
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
    scalePlay() {
      if (this.url.ssrc && this.playing) {
        playbackSpeed(this.deviceId, this.channelId, this.streamId, this.speed).then(res => {
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
        playbackSeek(this.deviceId, this.channelId, this.streamId, query).then(res => {
          this.$options.jessibuca.setPlaybackStartTime(curTime)
        })
      }
    },
    pause() {
      if (this.url.ssrc && this.playing) {
        if (this.videoVod) {
          playbackPause(this.deviceId, this.channelId, this.streamId).then(res => {
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
          playbackReplay(this.deviceId, this.channelId, this.streamId).then(res => {
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
    changeSpeed() {
      if (this.speed !== val) {
        this.speed = val
        this.scalePlay()
      }
    },
  }
};
</script>

<style lang="scss">
.player-wrapper {
  position: relative;
  width: 100%;
  height: 480px;
  margin: 5px auto;
  margin-top: -30px;

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

.container-shell {
  // backdrop-filter: blur(5px);
  /* border: 2px solid black; */
  width: 100%;
  height: 100%;
  position: relative;
  margin: 0;
}

#container {
  background: rgba(13, 14, 27, 0.7);
  width: 100%;
  height: 100%;
  margin: 0 auto;
}

@media (max-width: 720px) {
  .player-wrapper {
    width: 90vw;
    height: 52.7vw;
  }

  .control-wrapper {
    width: 90vw;

    .control {
      display: flex;
    }
  }
}
</style>

<style>
.control-wrapper {
  position: relative;
  width: 6.25rem;
  height: 6.25rem;
  max-width: 6.25rem;
  max-height: 6.25rem;
  border-radius: 100%;
  margin-top: 1.5rem;
  margin-left: 0.5rem;
  float: left;
}

.control-panel {
  position: relative;
  top: 0;
  left: 5rem;
  height: 11rem;
  max-height: 11rem;
}

.control-btn {
  display: flex;
  justify-content: center;
  position: absolute;
  width: 44%;
  height: 44%;
  border-radius: 5px;
  border: 1px solid #1890ff;
  box-sizing: border-box;
  transition: all 0.3s linear;
}

.control-btn:hover {
  cursor: pointer
}

.control-btn i {
  font-size: 20px;
  color: #1890ff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.control-btn i:hover {
  cursor: pointer
}

.control-zoom-btn:hover {
  cursor: pointer
}

.control-round {
  position: absolute;
  top: 21%;
  left: 21%;
  width: 58%;
  height: 58%;
  background: #fff;
  border-radius: 100%;
}

.control-round-inner {
  position: absolute;
  left: 13%;
  top: 13%;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 70%;
  height: 70%;
  font-size: 40px;
  color: #1890ff;
  border: 1px solid #1890ff;
  border-radius: 100%;
  transition: all 0.3s linear;
}

.control-inner-btn {
  position: absolute;
  width: 60%;
  height: 60%;
  background: #fafafa;
}

.control-top {
  top: -8%;
  left: 27%;
  transform: rotate(-45deg);
  border-radius: 5px 100% 5px 0;
}

.control-top i {
  transform: rotate(45deg);
  border-radius: 5px 100% 5px 0;
}

.control-top .control-inner {
  left: -1px;
  bottom: 0;
  border-top: 1px solid #1890ff;
  border-right: 1px solid #1890ff;
  border-radius: 0 100% 0 0;
}

.control-left {
  top: 27%;
  left: -8%;
  transform: rotate(45deg);
  border-radius: 5px 0 5px 100%;
}

.control-left i {
  transform: rotate(-45deg);
}

.control-left .control-inner {
  right: -1px;
  top: -1px;
  border-bottom: 1px solid #1890ff;
  border-left: 1px solid #1890ff;
  border-radius: 0 0 0 100%;
}

.control-right {
  top: 27%;
  right: -8%;
  transform: rotate(45deg);
  border-radius: 5px 100% 5px 0;
}

.control-right i {
  transform: rotate(-45deg);
}

.control-right .control-inner {
  left: -1px;
  bottom: -1px;
  border-top: 1px solid #1890ff;
  border-right: 1px solid #1890ff;
  border-radius: 0 100% 0 0;
}

.control-bottom {
  left: 27%;
  bottom: -8%;
  transform: rotate(45deg);
  border-radius: 0 5px 100% 5px;
}

.control-bottom i {
  transform: rotate(-45deg);
}

.control-bottom .control-inner {
  top: -1px;
  left: -1px;
  border-bottom: 1px solid #1890ff;
  border-right: 1px solid #1890ff;
  border-radius: 0 0 100% 0;
}
</style>
