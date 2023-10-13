<template>
    <div class="radioSelect" :style="{cursor:disabled?'not-allowed':'point'}">
        <div style="display:flex;align-items:center;height:100%">
            <el-popover placement="bottom-start" width="150" trigger="manual" v-model="visible" popper-class="message-popover">
                <ul class="group" v-if="options.length>0">
                    <li class="radio" v-for="(item,index) in dataList" :key="index" @click="select(item)">{{item.label}}</li>
                </ul>
                <div class="empty" v-else>无数据</div>
                <div v-show="!activeName" @click="disabled?visible = false:visible = true" class="icon" slot="reference"></div>
                <el-avatar v-show="activeName" style="background:rgb(0, 196, 186)" slot="reference">{{activeName?activeName.slice(0,1):activeName}}</el-avatar>
            </el-popover>
            <span class="title">
                <div>{{activeName?activeName:'--'}}</div>
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
    name:"RadioSelect",
    props:{
        value:[Number,String],
        options:{
            type:Array,
            default:()=>[]
        },
        disabled:{
            default:false,
            type:Boolean
        },
        keyName:String,
        valueName:String
    },
    data() {
        return {
            visible:false,
            activeIndex:null,
            activeName:null,
        }
    },
    watch:{
        // value:{
        //     handler(val){
        //         this.dataList.forEach(item=>{
        //             if (item.value==val) {
        //                 this.select(item)
        //             }
        //         })
        //     },
        //     immediate:true
        // },
        newData:{
            handler(val,O){
                this.dataList.forEach(item=>{
                    if (item.value==val.value) {
                        this.select(item)
                    }
                })
            }
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
        newData(){
            const {value, options} = this;
            return {value, options};
        },
        dataList(){
            if(this.options.length>0){
                return this.options.map(item=>({label:item[this.keyName?this.keyName:'label'],value:item[this.valueName?this.valueName:'value']}));
            }else{
                return [];
            }
        }
    },
    methods:{
        select(data){
            this.activeIndex=data.value
            this.activeName=data.label
            this.$emit('input',data.value)
            this.visible=false
            if (this.value!=data.value) {
                this.$emit("change",data.value)
            }
        },
        clear(){
            let data={
                value:null,
                label:"",
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
    margin-right: 10px;
    height: 50px;
    display: flex;
    align-items: center;
    padding-left: 10px;
    .icon{
        // cursor: pointer;
        margin-right: 10px;
        width: 35px;
        height: 35px;
        border: 1px solid #aaa;
        box-sizing: border-box;
        color: #aaa;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        &:hover{
            border-color: rgb(29,167,106);
            &::before{
                background: rgb(29,167,106);
            }
            &::after{
                background: rgb(29,167,106);
            }
        }
        &::before{
            display: block;
            content: "";
            width: 30%;
            height: 2px;
            background: #aaa;
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%,-50%);
        }
        &::after{
            display: block;
            content: "";
            height: 30%;
            width: 2px;
            background: #aaa;
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%,-50%);
        }
    }
    .el-avatar{
        width: 35px;
        height: 35px;
        line-height: 35px;
        margin-right: 10px;
        font-size: 15px;
        overflow:inherit;
    }
    .title{
        height: 35px;
        line-height: 100%;
        user-select: none;
        color: #aaa;
        width: 70px;
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
                line-height: 100%;
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