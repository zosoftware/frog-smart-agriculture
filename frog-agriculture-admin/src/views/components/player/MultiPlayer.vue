<template>
  <div class="multi-player">

    <div ref="container" class="container h100" :style="{ width: width }"></div>

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
  playbackStop,
} from '@/api/iot/channel';
import { getDevRecord } from "@/api/iot/record";
import { ptzdirection, ptzscale } from '@/api/iot/sipdevice';

export default {
  name: 'MultiPlayer',
  mixins: [],
  components: {},
  props: {
    //布局类型：1 2 4 5 3-1 4-1
    split: {
      type: [Number, String],
      default: 1,
    },
    videoParam: {
      type: Object,
      default: function () {
        return null;
      },
    },
    //宽度
    width: String,
  },
  data() {
    return {
      replayActive: false,
      dSplit:0,
      streamIds:[],
      chooseWin:null,
      playWinList:[]
    };
  },
  computed: {},
  watch: {
      dSplit:{
          handler:function(){
              this.updateSplit();
          }
      },
    videoParam: {
      handler: function (n, o) {
        this.play();
      },
      deep: true,
    },
    width: {
      handler: function (n, o) {
        this.$nextTick(() => {
          this.create();
        });
      },
    },
  },
  created() {},
  mounted() {
    this.create();
  },
  beforeDestroy() {
    //   this.playWinList.forEach((item)=>{
    //       this.stopPlay(item.streamId);
    //   });
  },
  methods: {
    create() {
      this.$jessibuca && this.$jessibuca.destroy();
      const jessibucaMulti = new JessibucaProMulti({
        container: this.$refs.container,
        videoBuffer: 0.1, // 缓存时长
        videoBufferDelay: 0.2, //
        decoder: '/js/jessibuca-pro/decoder-pro.js',
        split: this.split,
        isResize: false,
        isFlv: true,
        debug: false,
        useMSE: false,
        useSIMD: true,
        debugLevel: 'debug',
        hasAudio: false,
        showBandwidth: true, // 显示网速
        showPerformance: false, // 显示性能
        showPlaybackOperate:true,
        operateBtns: {
          fullscreen: true,
          screenshot: true,
          play: true,
          audio: true,
          record: true,
          ptz: true,
        //   quality: true,
          performance: true,
        },
        ptzClickType: 'mouseDownAndUp',
        ptzZoomShow:true,
        // ptzMoreArrowShow: true,
        // ptzApertureShow: true,
        // controlHtml: '<div>我是 <span @click="updateSplit(2)" style="color: red">test</span>文案</div>'
        // qualityConfig: ['720P', '1080P', '4K'],
        extendOperateBtns:[{
            name:'1',
            index:0,
            iconTitle:'一分屏',
            icon:'data:image/png;base64,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',
            iconHover:'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAAAAXNSR0IArs4c6QAABq9JREFUeF7t3Gtu21YARGFyZYlXlnhlcVbGgoUCFEHjiFejB+d+Bor+0R1xzvCAlm1kXXwhgMAfCazYIIDAnwkQxN2BwCcECOL2QIAg7gEExgh4goxxc2oSAgSZZGg1xwgQZIybU5MQIMgkQ6s5RoAgY9ycmoQAQSYZWs0xAgQZ4+bUJAQIMsnQao4RIMgYN6cmIUCQSYZWc4wAQca4OTUJAYJMMrSaYwQIMsbNqUkIEGSSodUcI0CQMW5OTUKAIJMMreYYAYKMcXNqEgIEmWRoNccI3EWQbdu+Lsuy/7d/fRm7NKcQuJrAz2VZPtZ1/bj6xJUvjAqybdv3ZVm+XfneXoZAmsAuyHtSlIgglyfGj3RbeQgMEoiJcrMgnhqDEzr2CAJvtz5NbhLEk+MRG3uPGwncJMmwIOS4cTbHH0ZgXdfh+3z44LZt+2eOXz+pelhZb4TAAIH9g/v+A6TDX0OCeHoc5uzA8wkMSTIqiKfH8wd3BccIPFSQ7di1eTUCzycw8lnk8BPEt1fPH9oVDBM4/BMtggyzdvCEBA5/mzUiiD8nOeGd4ZL/JUAQNwICnxAgiNsDAYK4BxAYI+AJMsbNqUkIEGSSodUcI0CQMW5OTUKAIJMMreYYAYKMcXNqEgIvI8j7JMDVfC0Cf/v3EF5DkJE/Cnstzq7mjAS2bfvbH9ES5IzDuuYMAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJUCQ0mHVyhAgSIajlFICBCkdVq0MAYJkOEopJXAaQUr5q3V+Au/run4/UmM98uL9tdu27W/w7eg5r0fgBQi8rev6ceQ6RgT5uizLjyNv4rUIvAiB+wtyeYpsL1LYZSBwNYF1XQ8/EA4f8G3W1Xt44WsR+FjX9e3oJRHkKDGvPyuBw99e7UWHBPEUOes9Mu11Dz09bhLEZ5Fpb7YzFh96eiQE8ROtM94uc13zsBw3C3J5ipBkrhvuTG0P/2Lw93LDn0H+G7Rt2y7J/svD/f++EHg2gf2Xgbsch34p+H8XHRHkVzBRnn1feP+LGIf+nOQzalFBfhNlf5p8MRkCdybw85K//6Tq5ifGXb7FujMA8Qg8jcBdniBPa+ONEQgTIEgYqLguAgTp2lObMAGChIGK6yJAkK49tQkTIEgYqLguAgTp2lObMAGChIGK6yJAkK49tQkTIEgYqLguAgTp2lObMAGChIGK6yJAkK49tQkTIEgYqLguAgTp2lObMAGChIGK6yJAkK49tQkTIEgYqLguAgTp2lObMAGChIGK6yJAkK49tQkTIEgYqLguAv8A2vziBfOsPTkAAAAASUVORK5CYII='
            ,click:()=>{
                this.dSplit = 1
            }
        },{
            name:'4',
            index:1,
            iconTitle:'四分屏',
            icon:'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAAAAXNSR0IArs4c6QAAB9hJREFUeF7tnGFu2zgYBZXD8BxJTpbkZE3PocN0oYULLIptIn6e5FHkGOg/8omcpwFty83D5ksCEvgrgQfZSEACfyegIN4dEviAgIJ4e0hAQbwHJFAj4AlS4+asRQgoyCJFu80aAQWpcXPWIgQUZJGi3WaNgILUuDlrEQIKskjRbrNGQEFq3Jy1CAEFWaRot1kjoCA1bs5ahICCLFK026wRUJAaN2ctQkBBFinabdYIKEiNm7MWIaAgixTtNmsEFKTGzVmLEFCQRYp2mzUCClLj5qxFCCjIIkW7zRqBLxFk3/enbduOf8frsbY0Z0ngNIGf27a9t9beT884ORAVZN/3123bXk5e22ESoAkcgryRoiCC3E6MH/RuzZNAkQAmyt2CeGoUK3TadxB4vvc0uUsQT47v6Nhr3EngLknKgijHnbU5/dsItNbK93l54r7vx2eO399UfdtmvZAECgSOD+7HF0jdr5Ignh7dnJ2QJ1CSpCqIp0e+cFfQR+BbBfnVtzZHSyBPoPJZpPsE8e1VvmhXUCbQ/Y2WgpRZO/GCBLrfZlUE8eckF7wzXPK/BBTEG0ECHxBQEG8PCSiI94AEagQ8QWrcnLUIAQVZpGi3WSOgIDVuzlqEgIIsUrTbrBFQkBo3Zy1CYBhB3hYBfu82jz9o8dl/GZDlecqf/T2EMQSp/CjsPIN5Rp78XVv374fmIdS3k33fP/sRrYL0Ic2OVhCWv4KwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlZwGUHYbZsmAYzAW2vttSftoWfwMXbf9+MCL73zHC+BAQgoyAAluIRxCSjIuN24sgEIKMgAJbiEcQkoyLjduLIBCCjIACW4hHEJKMi43biyAQgMI8jbADCusITHbduePlmoLM83+dnjhzEEaa11P185z2CekT5JZ7u8zJN0BTlXvIKc43R2lIKcJXWRcQrCFqUgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWoCAsz3iagrAVKAjLM56mIGwFCsLyjKcpCFuBgrA842kKwlagICzPeJqCsBUoCMsznqYgbAUKwvKMpykIW4GCsDzjaQrCVqAgLM94moKwFSgIyzOepiBsBQrC8oynKQhbgYKwPONpCsJWcBlB2G2bJgGMwFtr7bUn7aFn8DF23/fjAi+98xwvgQEIPLfW3nvWURHkadu2Hz0XcawEBiHw9YLcTpFfg2zYZUjgNIHWWveB0D3Bt1mn+3DgWATeW2vPvUtSkF5ijr8qge63V8dGS4J4ilz1Hll23aXT4y5B/Cyy7M12xY2XTg9CEL/RuuLtstaay3LcLcjtFFGStW64K+22+8Hgn5srfwb5b9DtJxPHw8NDFl8SSBM4HgYecnQ9FPy/RSOC/A5WlPR94fVvYnT9nOQjaqggf4hynCaPViaBLybw85Z/fFN194nxJW+xvhiA8RKIEfiSEyS2Gy8sAZiAgsBAjZuLgILM1ae7gQkoCAzUuLkIKMhcfbobmICCwECNm4uAgszVp7uBCSgIDNS4uQgoyFx9uhuYgILAQI2bi4CCzNWnu4EJKAgM1Li5CCjIXH26G5iAgsBAjZuLgILM1ae7gQkoCAzUuLkIKMhcfbobmICCwECNm4uAgszVp7uBCSgIDNS4uQgoyFx9uhuYgILAQI2bi8A/rDfnI7LXnJcAAAAASUVORK5CYII=',
            iconHover:'data:image/png;base64,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'
            ,click:()=>{
                this.dSplit = 2
            }
        },{
            name:'9',
            index:2,
            iconTitle:'九分屏',
            icon:'data:image/png;base64,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',
            iconHover:'data:image/png;base64,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'
            ,click:()=>{
                this.dSplit = 3
            }
        }]
      });

      jessibucaMulti.on('ptz', (index, d) => {
          console.log(d)

        switch (d) {
          case 'up':
            this.ptzDirection(0, 1,index);
            break;
          case 'down':
            this.ptzDirection(0, 2,index);
            break;
          case 'left':
            this.ptzDirection(2, 0,index);
            break;
          case 'right':
            this.ptzDirection(1, 0,index);
            break;
          case 'zoomExpand':
            this.ptzScale(1,index);
            break;
          case 'zoomNarrow':
            this.ptzScale(2,index);
            break;
          case 'stop':
            this.ptzDirection(0, 0,index);
            this.ptzScale(0,index);
            break;


        }
      });

      this.$jessibuca = jessibucaMulti;
      this.$jessibuca2 = jessibuca;
    },
    updateSplit() {
      this.$jessibuca && this.$jessibuca.arrangeWindow(this.dSplit);
    },
    /** 直播 */
    async play() {
      if (this.videoParam?.deviceId && this.videoParam?.channelId) {
        await startPlay(this.videoParam.deviceId, this.videoParam.channelId);
        const response = await getStreaminfo(
          this.videoParam.deviceId,
          this.videoParam.channelId
        );
        let res = JSON.parse(response.data);
        console.log('playurl：' + res.playurl);
        //存储流数组，方便统一停止流
        this.streamId = res.streamId;
        //存储播放状态的窗口
        this.playWinList.push({
            index:this.$jessibuca.getSelectedWindowIndex(),
            deviceId:this.videoParam.deviceId,
            channelId:this.videoParam.channelId,
            streamId:res.streamId
        });
        res.playurl && this.$jessibuca.play(res.playurl);
      }
    },
    /** 停止播放直播 */
    async stopPlay(streamId){
        stopPlay(this.videoParam.deviceId, this.videoParam.channelId, streamId)
    },
   /** 方向控制 */
    ptzDirection(leftRight, upDown,index) {
      var data = {
        leftRight: leftRight,
        upDown: upDown,
        moveSpeed: 125,
      };
      let curWin = this.playWinList.find(item=>item.index==index);
      if(curWin){
        ptzdirection(curWin.deviceId, curWin.channelId, data);
      }
    },
    //缩放控制
    ptzScale: function (inOut,index) {
      console.log('云台缩放：' + inOut);
      var data = {
        inOut: inOut,
        scaleSpeed: 30
      }
      let curWin = this.playWinList.find(item=>item.index==index);
      ptzscale(curWin.deviceId, curWin.channelId, data)
    },
  },
};
</script>
<style lang="scss" scoped>
.container{
    background: rgba(13, 14, 27, 0.7)
}
</style>
