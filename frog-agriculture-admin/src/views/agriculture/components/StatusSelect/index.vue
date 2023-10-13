<template>
    <div class="radioSelect" :style="{cursor:disabled?'not-allowed':'point'}">
        <div style="display:flex;align-items:center;height:100%">
            <el-popover placement="bottom-start" width="150" trigger="manual" v-model="visible" popper-class="message-popover">
                <ul class="group" v-if="options.length>0">
                    <li class="radio" v-for="(item,index) in dataList" :key="index" @click="select(item)">{{item.label}}</li>
                </ul>
                <div class="empty" v-else>无数据</div>
                <div class="icon" @click="disabled?visible = false:visible = true" :style="{background:color,borderColor:color}" slot="reference">
                     <i :class="activeIcon" style="color:#fff"></i>
                </div>
            </el-popover>
            <span class="title">
                <div>{{activeName}}</div>
                <div><slot></slot></div>
            </span>
            <div class="del">
                <i v-if="!disabled" @click="clear" class="el-icon-close"></i>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name:"StatusSelect",
    props:{
        value:[Number,String],
        options:{
            type: Array,
            default: ()=>[
                {label:"未分配",activeIcon:"el-icon-minus",color:"#F71653",value:0},
                {label:"已分配",activeIcon:"el-icon-s-claim",color:"#13ce66",value:1},
                {label:"进行中",activeIcon:"el-icon-s-promotion",color:"#ffba00",value:2},
                {label:"已完成",activeIcon:"el-icon-check",color:"#ff4949",value:3},
            ]
        },
        disabled:{
            default:false,
            type:Boolean
        },
        keyName:String,
        valueName:String,
        colorName:String,
        iconName:String,
    },
    data() {
        return {
            visible:false,
            activeIndex:null,
            activeName:"--",
            activeIcon:"el-icon-minus",
            color:"#70B4E0"
        }
    },
    watch:{
        value:{
            handler(val){
                this.dataList.forEach(item=>{
                    if (item.value==val) {
                        this.select(item)
                    }
                })
            },
            immediate:true
        }
    },
    mounted(){
        document.addEventListener('mouseup', e => {
        const dom1 = document.querySelector('.radioSelect')
        if (dom1) {
          if (!dom1.contains(e.target)) {
            this.visible = false
          }
        }
      })
    },
    computed:{
        dataList(){
            if(this.options.length>0){
                return this.options.map(item=>{
                    return {
                        label:item[this.keyName?this.keyName:'label'],
                        value:item[this.valueName?this.valueName:'value'],
                        color:item[this.colorName?this.colorName:'color'],
                        activeIcon:item[this.iconName?this.iconName:'activeIcon']
                    }
                })
            }else{
                return [];
            }
        }
    },
    methods:{
        select(data){
            this.activeIndex=data.value
            this.activeName=data.label
            this.color=data.color
            this.activeIcon=data.activeIcon
            this.$emit('input',data.value)
            this.visible=false
            if (this.value!=data.value) {
                this.$emit("change",data.value)
            }
        },
        clear(){
            let data={
                value:null,
                label:"--",
                color:"#70B4E0",
                activeIcon:"el-icon-minus"
            }
            this.select(data)
        }
    }
}
</script>
<style>
.message-popover{
  padding: 0 !important;
}
</style>
<style lang="scss" scoped>
.radioSelect{
    cursor: pointer;
    user-select: none;
    height: 50px;
    display: flex;
    align-items: center;
    .icon{
        width: 35px;
        height: 35px;
        border-radius: 50%;
        margin-right:10px;
        display: flex;
        align-items: center;
        justify-content: center;
        i{
            font-size: 20px;
        }
    }
    .el-avatar{
        margin-right: 10px;
        font-size: 20px;
    }
    .title{
        user-select: none;
        color: #aaa;
        width: 70px;
        height:40px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        div{
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            &:first-of-type{
                color: #666;
                font-size:16px;
            }
            &:last-of-type{
                color: #999;
                font-size:12px;
            }
        }
    }
    .del{
        width: 20px;
        height: 100%;
        display: flex;
        align-items: center;
        .el-icon-close{
            visibility: hidden;
        }
    }
    &:hover{
       .el-icon-close{
            visibility:visible;
            &:hover{
                color:red
            }
       } 
    }
}
.group ::v-deep{
    list-style: none;
    padding: 0;
    margin: 0;
    max-height: 200px;
    overflow:auto;
    .radio{
        cursor: pointer;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        height: 35px;
        line-height: 35px;
        padding: 0 10px;
        box-sizing: border-box;
        &:hover{
            background: #eee;
        }
    }
}
.empty{
    text-align: center;
}
</style>