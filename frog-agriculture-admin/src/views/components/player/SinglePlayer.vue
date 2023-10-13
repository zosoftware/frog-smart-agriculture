<template>
  <div class="multi-player">
      <div v-if="open" style="position:absolute;right:33px;bottom:65px;z-index:999">
          <el-date-picker v-model="recordDate" format="yyyy/MM/dd" value-format="yyyy/MM/dd" type="date" size="normal" placeholder="选择日期时间" @change="handlerChooseDateClick">
          </el-date-picker>
      </div>
    <div v-loading="loading" element-loading-text="正在读取摄像头录像" element-loading-background="rgba(0, 0, 0, 0.8)"
    element-loading-spinner="el-icon-loading" ref="container" class="container h100" :style="{ width: width }"></div>

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
  name: 'SinglePlayer',
  mixins: [],
  components: {},
  props: {
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
        //视频loading
        loading:false,
        deviceId:'',
        channelId:'',
        streamId:'',
        recordDate:null,
        open:false
    };
  },
  computed: {},
  watch: {
    videoParam: {
      handler: async function (n, o) {
        //只要监听到外部的切换，就将当前流停掉
        this.stopPlay();
        //重新创建播放器
        this.create();
        //然后再赋值播放
        if(!n.deviceId.includes('time') && !n.channelId.includes('time')){
            this.deviceId = n.deviceId;
            this.channelId = n.channelId;
            this.play();
        }else{
            this.$message({
            type: 'warning',
            message: '没有在线通道'
        })
        }
      },
      deep: true
    },
    width: {
      handler: function (n, o) {
        this.$nextTick(() => {
          this.create();
        });
      }
    },
  },
  created() {
  },
  mounted() {
  },
  beforeDestroy() {
    //   this.stopPlay();
    //   this.stopPlayBack();
  },
  methods: {
     async create(type) {
      this.$jessibucaPro && await this.$jessibucaPro.destroy();
      const baseConfig = {
        container: this.$refs.container,
        videoBuffer: 0.1, // 缓存时长
        videoBufferDelay: 0.2, //
        loadingText: '加载中',
        decoder: '/js/jessibuca-pro/decoder-pro.js',
        isResize: false,
        isFlv: true,
        debug: false,
        useMSE: false,
        useSIMD: true,
        debugLevel: 'debug',
        showBandwidth: true, // 显示网速
        showPerformance: false, // 显示性能
        showPlaybackOperate:true,
      }
      let config1 = {
        ...baseConfig,
        ptzZoomShow:true,
         operateBtns: {
          fullscreen: true,
          screenshot: true,
          play: true,
          audio: true,
          record: true,
          ptz: true,
          performance: true,
        },
        ptzClickType: 'mouseDownAndUp',
        extendOperateBtns:[{
            name:'record',
            index:10,
            iconTitle:'视频回播',
            icon:'data:image/png;base64,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',
            iconHover:'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAAAAXNSR0IArs4c6QAACz9JREFUeF7tnWFy2zgMRqWTtT1ZtydrezLt0JUyjmNbAvhBpMDXP7szISHygU8gZTmZJ/5BAAIvCcywgQAEXhNAEFYHBN4QQBCWBwQQhDUAAR8BKoiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIIIiPG70GIYAggySaafoIXEqQZVm+r9Pc/uub9bi9/mxTn+f54//HxbE/824FWWUoInybpgkh9nPpbVFE+VU6I81XhF0JsizLf+sQf3qzTb8qAkWWv9M0/UGWfxy7EGQVAymq1ra8862yjC5KU0FWMdhCyde2NODQojQR5O58QdWQruXQYGXb9SP0Ch0GP10QtlMdrgLbkH6MtO06VRDksK3EjluXs8n2QKXjYdYP7TRBkKM+WZ1FGEKScEHW80Y5a/BZRmcrXDCc9OeSMwT5jRyCpdhviNSVJFSQZVmQo9+FrRxZWknCBOHMoVx/l4iVUpIQQYLkuL0vVF6DuL0CwMt2ZmvuXvYsfSPOhekeAcsFWZNQtlaKf0UK3gtSkHwRQ/wQJd2hPUIQxbkjZbkOXOfVoYWipMqdVBBR9UhXpqtX74kBFNvjeZ6l6+rE6X+5lHQilU+t0pXnlomtubbgRpemisgEqYSaBmjNwuytb80NL0sVUQriPXtQOXoz4248y7IszuGluOlJBKmpHlnuNM5F1H23mjNJhtyqBClvdnq+28GBvHtFpqlCEll+775DtBE75fMwlSCe7RVbqwvIsQ3RudWq2mYZvnEa9q3HakEqtleyu8uF1tllh+qtIp5tlvda63fopd9TaSUI1eOCqjiriOlGWCHH/dZL9ssmFIJ4tlcmaBdcSymH7Fy8h7ZZFTuRp6w9letZoCaCqAaf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',
            activeIcon:'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAAAAXNSR0IArs4c6QAACyFJREFUeF7tneF12zoMRumM0NcVMkeTcdoOkdchXjtO0jmyQtsRonfoRKma2qYIApQIXP/qaUiI/IArgJRMHxIfFECBswoc0AYFUOC8AgBCdKDABQUAhPBAAQAhBlBApgAZRKYbvYIoACBBHM00ZQoAiEw3egVRAECCOJppyhQAEJlu9AqiAIAEcTTTlCkAIDLd6BVEAQAJ4mimKVMAQGS60SuIAgASxNFMU6YAgMh0o1cQBQAkiKOZpkwBAJHpRq8gCgBIEEczTZkCACLTjV5BFACQII5mmjIFwgHy7r/Hf7NUh0P6cDikL/nfPz5eP8jk89/r/bfHm6endLPULJJeYQA5gnFIdydDekpffn2+PoLD57cClzS7OqTbCKCEAOSfr4/3U3q+C579AMmrNMesMaX74s0igGbuAbmYOd5EwCGlh5+frm+LgeG4wWo4XjT49enadQy5nlz24arssQj4yJDU3ExeJXOeRdwD8u7r41R7w48IiQiOLCyA1IbXvtpLADnu2AQqt8RwAMi+gl0ymtoS6+01vO/WNMGRUvKuj/8S69L27krivAZBKxwRsqx7QDIDrYGQbXiDBE3W3R1DAAIkfwYDcKyD42Utur7x6C01AmP0XRsNDbxl00txHSaDzCJoBMiokLRuWHgsNUs3/XCAaJVbo0HSCkeEBfkpWEICEg0S4CjlifN/DwtIBEhq36s6cwcN/X5aaEByQGgE0R7LLa/zkucCWc/wgHiEBDhkMLAGuaCbRlDtYSEbeZdOD4vflsggC1UzJNOU7opfrrrgiS0hAQ59RADkhKYj7voAhz4c4Z6k10ioAUk+FKLH97aBo8azdW3JIBf0aoWkx5Nn4KgL+NrWuwEk1/958PMRM7UTsWqfjwdqWZMcxzU9Hy9k8jl3UkvNxSzHVzGOq6v0evxSj8y7ZmibATKfT3X2KJ41o6eNewXypkevUvWUmF0BAQr38Ww6wS1g6QaISq1sKj/GR1GgJyjmgADGKGE34Dg7nKhiBojGk+kBXcaQOytgnU1MAAGOzlHC5czODFAHhJKKaN1MAYOSSxUQ4NgsNLjwrIAyJGqAUFYRo7tRQBESFUCAYzehwUBeFNA6dV4FEI13lvAsCqgqoJRFmgFh3aHqVoxpKqAASRMglFaa3sSWiQKNkDQBQmll4lKMaiqwFSBkD00vYstUgQZIxBmE7GHqUoxrKtAbELKHpvew1UMB6bavKIOQPXq4lGuoKiDMIiJApL/7pzphjKFApQKSLFINCOVVpVdovhsFJL9rUg0I5dVu/M1AahUQlFkAUisy7YdVQHLqZRUglFfDxgYDf1Ggdh0CIIROKAVq1yFVgPBiYqhYcjlZAOng1lzLTlP6ni+VTwNsPRG+w5C5xKxA5UK9KoOwg5XSpYUeGXYADgHEzklrdkE0fmPEbgZYrv25PDJITcxU3H0ApUbYjm0rfJhHVQVI9FdMahd4WWDKro7Bv+JSa6qApRkAWSHq3EQCyNyX9VuF0IZNAcRQ3NqHTG+HQtll6JyVpgFkpVCSZq2AzNfkjQSJ+jp9AERHx5NWtACZjbM+MXTWGdMAYqi5NiB5qJRdhg47YRpADPW2AGRZdvFE3tB5L6YBxFBjS0AouwwdtzANIIY69wCEssvQgc8P/h5+frq+XXsVnoOsVSql1AsQyq4Kp1Q2BZBKwWqa9waEsqvGO+vaAsg6nUSttgIEUETuOtkJQPS0/MvS1oCwPml3LoC0a3jWwh4AYX3S5mAAadPvYu89AULZJXM0gMh0W9Vrj4BQdq1y3WsjAKnTq6r1XgGh7FrvRgBZr1V1y70DQtlVdimAlDUStxgFkHmCfEnrb1cDiDj8yx1HA4T1CYCUo1qxxYiALNcnT1O6V5RjSFNkEEO3jQwI65NnBQAEQIoKRP6SFoAUw0PewEMGWc4+IigAIo//Yk9vgEQsuwCkGObyBl4BibTbBSDy+C/29AzIcrfL83fjAaQY5vIGEQDxXnYBiDz+iz0jATKL4e1pPIAUw1zeICIgWS1PB9wBiDz+iz2jAuIJEgAphrm8QWRAsmoeyi0Akcd/sWd0QDyUWgBSDHN5g+iAeDiVHkDk8V/sGR0QSqxCiET/CTYAebyfUrop3kl23IAMYuicyIB4WH/k0AAQAFFXwAscAKIeGn8ajJZBMhiHQ/owelm19CIZxBCSSIB4yhoAYgjF0nQEQLyCMfuRDGIIi2dAvIMBIIZgzKa9AhIFDhbpxpB4AyQSGGQQYziyeS+ARAQDQACkqMARjOc6467Y2GkDFumGjh05g0TOGmzzGkIx+jYvYPwZHGQQQ1hGyiCAcToQAARAXH2HXNudAKKt6MLe3jMIWaPsfAApayRusVdAAGO9SwFkvVbVLfcGCNu21S7k+yD1kq3vsSdAyBrr/cY2r0yr6l57AAQwqt32RwdKrDb9LvbeEhDA0HEsgOjoeNLKVoAAh55TAURPy78s9QYEMPSdCSD6mr5a7AUIYNg5EUDstDV/3Z1tW0PnvZgGEEONLTMIWcPQcQvTAGKoswUggGHosBOmAcRQb01AAMPQURdMA4ih7lqAAIehkwqmAcRQ+1ZAAMPQOStNA8hKoSTNrg7p9sfH64favoBRq5hde1NAPPw+RJP0U/ry6/P188EHKz5s264QqXMTALEUvAIQsoalIxpsV/gwX+VQcymcnlKpzEKjmojaoC2A2It+ChLAsNdd4wqlG9zba5BBhKrnWjZ3nab0PfJBbEL5NusGIJtJz4VHUMAUkCxA9B/yHCEIGONpBWp3sKoX6blD+K1eom9YBboAwmJ02PgIP/Da8kqUQd5/e7x5mtJ9eLURYDgFJK8KVe1izYqwDhkuNsIPWFJeiTLIcaGef2ci8G9MhI+2AQWQlFdiQNjNGjBCgg9ZUl61AUIWCR5yA02/8vWS5cxEaxDKrIGCg6E2HbYhBgRIiLwhFGjIHk0lVu7Mlu8QIRJ2kNKdK5US63XLl7VI2ADc+8SlO1eqgFBq7T1Mgo6vsbSaVWtagyyl5+Fh0EDc4bQ1Sit9QCi1dhgqMYckfeZxSi21DMKiPWYw7mnWmplDPYPMBtnZ2lPIxBmLBRzN27zn5AeSOIG5h5lawWEGyFxuTVO6m1K62YOIjMGpAkq7VefUUV2DnLoI2cRpYG48rZw18oEZNQf5SYZsDsg8KF6Rl7iHPicVMM4ay2t2A+R1l+sp3RwO6QOlF8Ffo0CvjPF2TF0BWV48l155jZL/D1hqQiVO262g2CyDlFyboZnbPD0VFveK32icHVEaX6S/q2f5KX0p6Xd19XwYn+QE/ZJt6d83yyDSAZusaTrWtK3z7tlf8/UhjRcHe859vhaApPKB1Fs4Zg/X1DwDTfP1j57aDAuI5vbxqM6zDhStnUfLB3nWGgwLSBZGw4EjO886OI4af32cmq8zcAk7NCCtDgSOcug334QGhiOrMz4gDa/Zj7pwLIe1bgspJB5uQMMDkkNhfqZS9Txl8DubLgJla9WQONHXBSBVW79OHFcOaf0Wq29EjjR2BcgcEvMDx+PbxPkXoPJW7lV62NMDKP3w7WvxuIs4vzbkWGOXgPQNFa7mWQEA8exd5tasAIA0S4gBzwoAiGfvMrdmBQCkWUIMeFYAQDx7l7k1KwAgzRJiwLMCAOLZu8ytWQEAaZYQA54VABDP3mVuzQoASLOEGPCsAIB49i5za1YAQJolxIBnBQDEs3eZW7MCANIsIQY8KwAgnr3L3JoVAJBmCTHgWQEA8exd5tasAIA0S4gBzwoAiGfvMrdmBQCkWUIMeFbgf+ap3BT53v4qAAAAAElFTkSuQmCC',
            click:async ()=>{
                this.open=true;
            }
        }],
      }
      let config2 ={
        ...baseConfig,
          operateBtns: {
          fullscreen: true,
          screenshot: true,
          hiddenAutoPause:true,
          play: true,
          audio: true,
          record: true,
          performance: true,
        },
        ptzClickType: 'mouseDownAndUp',
        extendOperateBtns:[{
            name:'record',
            index:10,
            iconTitle:'视频直播',
            icon:'data:image/png;base64,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',
            iconHover:'data:image/png;base64,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',
            click:async ()=>{
                await this.create();
                this.play();
            }
        }],
      }
      const jessibucaPro = new JessibucaPro(type==1?config2:config1);

      jessibucaPro.on('ptz', (d) => {
          console.log(d)
        switch (d) {
          case 'up':
            this.ptzDirection(0, 1);
            break;
          case 'down':
            this.ptzDirection(0, 2);
            break;
          case 'left':
            this.ptzDirection(2, 0);
            break;
          case 'right':
            this.ptzDirection(1, 0);
            break;
          case 'zoomExpand':
            this.ptzScale(1);
            break;
          case 'zoomNarrow':
            this.ptzScale(2);
            break;
          case 'stop':
            this.ptzDirection(0, 0);
            this.ptzScale(0);
            break;
        }
      });
      jessibucaPro.on("pause",  (flag)=> {
        this.$streamId &&  this.pausePlayBack();
      })
      jessibucaPro.on("play",  (flag) =>{
        this.$streamId && this.replayPlayBack();
      })
      jessibucaPro.on("playbackSeek",(d)=>{
          this.seekPlayBack(d);
      })
      this.$jessibucaPro = jessibucaPro;
    },
    /** 直播 */
    async play() {
      if (this.deviceId && this.channelId) {
        await startPlay(this.deviceId, this.channelId);
        const response = await getStreaminfo(
          this.videoParam.deviceId,
          this.videoParam.channelId
        );
        let res = JSON.parse(response.data);

        console.log('playurl：' + res.playurl);

        this.streamId = res.streamId;
        res.playurl && this.$jessibucaPro.play(res.playurl);
      }
    },
    /** 停止播放直播 */
    async stopPlay(){
        if (this.deviceId && this.channelId) {
            stopPlay(this.deviceId, this.channelId, this.streamId)
        }
    },
    async handlerChooseDateClick(){
        this.open=false;
        await this.create(1)
        this.playBack();
    },
    /** 回播 */
   async playBack(){                 //  2020/1/1
     let date = this.recordDate?new Date(this.recordDate).getTime(): new Date(new Date().toLocaleDateString()).getTime();//yyyy-dd-hh 00:00:00的时间戳
     this.$start = date / 1000;// yyyy-dd-hh 00:00:00时间戳的秒数
     this.$end = Math.floor((date + 24 * 60 * 60 * 1000 - 1) / 1000);// yyyy-dd-hh 23:59:59时间戳的秒数
     this.loading = true;
     const {data:{recordItems}} = await getDevRecord(this.deviceId, this.channelId, {start:this.$start,end:this.$end});
     this.$videoStart = recordItems[0].start;//设置录像开始时间
     if(recordItems && recordItems.length>0){
         //播放时间为第一段录像的开始时间到结束
        const res = await playback(this.deviceId, this.channelId, {start:recordItems[0].start,end:this.$end});
        this.$ssrc = res.data.ssrc;
        this.$streamId = res.data.streamId;
        this.loading = false;
        this.$jessibucaPro.playback(res.data.playurl, {
            playList: recordItems,
            fps: 20
        })

     }else{
        this.$message({
            type: 'warning',
            message: '当前通道没有录像'
        })
     }
   },
   /** 停止回播 */
   stopPlayBack(){
       if (this.deviceId && this.channelId) {
        playbackStop(this.deviceId, this.channelId, this.$ssrc)
       }
   },
   /** 暂停回播 */
   pausePlayBack(){
       if (this.deviceId && this.channelId) {
        playbackPause(this.deviceId, this.channelId, this.$ssrc)
       }
   },
   /** 重新播放回播*/
    replayPlayBack(){
        if (this.deviceId && this.channelId) {
            playbackReplay(this.deviceId, this.channelId, this.$ssrc);
        }
   },
   /** 选时播放 */
   seekPlayBack(time){
      let curTime = this.$start + time.hour * 3600 + time.min * 60 + time.second;
      let range = this.$start + time.hour * 3600 + time.min * 60 + time.second -this.$videoStart;//视频开始时间到当前时间的秒数
      if (this.$ssrc) {
        const query = {
          seek: range,
        }
        if (this.deviceId && this.channelId) {
          playbackSeek(this.deviceId, this.channelId, this.$ssrc, query).then(res => {
          this.$jessibucaPro.setPlaybackStartTime(curTime)
        })
        }
      }
   },
   /** 方向控制 */
    ptzDirection(leftRight, upDown) {
      var data = {
        leftRight: leftRight,
        upDown: upDown,
        moveSpeed: 125,
      };
      if (this.deviceId && this.channelId) {
        ptzdirection(this.deviceId, this.channelId, data);

      }
    },
     //缩放控制
    ptzScale: function (inOut) {
      console.log('云台缩放：' + inOut);
      var data = {
        inOut: inOut,
        scaleSpeed: 30
      }
      ptzscale(this.deviceId, this.channelId, data)
    },
  },
};
</script>
<style lang="scss" scoped>
    .container{
        background: rgba(13, 14, 27, 0.7);
    }
</style>
