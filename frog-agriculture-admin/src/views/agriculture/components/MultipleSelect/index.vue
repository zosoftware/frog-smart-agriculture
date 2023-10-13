<template>
    <div class="multipleSelect">
        <div style="display:flex;align-items:center;">
            <ul class="selected">
                <el-tooltip v-for="item in selectList" :key="item.value" effect="dark" :content="item.label" placement="top">
                    <li class="item" >{{item.label.slice(0,1)}}<i v-if="!disabled" class="el-icon-close" @click="delItem(item)"></i></li>
                </el-tooltip>
            </ul>
            <el-popover placement="bottom-start" width="150" trigger="manual" v-model="visible" popper-class="message-popover">
                <ul class="group" v-if="options.length>0">
                    <li class="multiple" v-for="(item,index) in dataList" :key="index" @click="select(item)" 
                    :style="{color:value.includes(item.value)?'rgb(29, 167, 106)':'#000',background:value.includes(item.value)?'rgb(240, 240, 240)':'#fff'}"> 
                        <div class="label">{{item.label}}</div>
                        <div class="icon" v-show="value.includes(item.value)"><i class="el-icon-check"></i></div>
                    </li>
                </ul>
                <div v-else class="empty">无数据</div>
                <div :style="{cursor:disabled?'not-allowed':'point'}" class="icon-add" slot="reference" @click="disabled?visible = false:visible = true"></div>
            </el-popover>
        </div>
    </div>
</template>

<script>
export default {
    name:"multipleSelect",
    props:{
        options:{
            type: Array,
            default: ()=>[]
        },
        value:{
            type:Array,
            default: ()=>[]
        },
        keyName:String,
        valueName:String,
        disabled:{
            type:Boolean,
            default:false
        }
    },
    data() {
        return {
            visible:false,
            selectList:[],
        }
    },
    mounted(){
        document.addEventListener('mouseup', e => {
        const dom1 = document.querySelector('.icon-add')
        if (dom1) {
          if (!dom1.contains(e.target)) {
            this.visible = false
          }
        }
      })
    },
    methods: {
        select(data){
            if (!this.value.includes(data.value)) {
                this.selectList.push(data)
                let check = this.selectList.map(i=>{
                    return i.value
                })
                this.$emit("input",check)
                if (this.value.length!=check.length) {
                    this.$emit("change",{type:"add",value:data.value,values:check})
                }
                this.visible=false
            }
        },
        delItem(data){
            this.selectList.splice(this.value.indexOf(data.value),1)
            let check = this.selectList.map(i=>{
                return i.value
            })
            this.$emit("input",check)
            if (this.value.length!=check.length) {
                this.$emit("change",{type:"del",value:data.value,values:check})
            }
        }
    },
    watch:{
        // value:{
        //     handler(val){
        //         let list=[]
        //         val.forEach(i=>{
        //             this.dataList.forEach(j=>{
        //                 if (i==j.value) {
        //                     list.push(j)
        //                 }
        //             })
        //         })
        //         this.selectList=list
        //     },
        //     immediate:true
        // },
        newData:{
            handler(val){
                let list=[]
                val.value.forEach(i=>{
                    this.dataList.forEach(j=>{
                        if (i==j.value) {
                            list.push(j)
                        }
                    })
                })
                this.selectList=list
            }
        }
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
}
</script>
<style>
.message-popover{
  padding: 0 !important;
}
</style>
<style lang="scss" scoped>
.multipleSelect {
    user-select: none;
    display: flex;
    align-items: center;
    .selected{
        display: flex;
        list-style: none;
        padding: 0;
        margin: 0;
        .item{
            width: 35px;
            height: 35px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 10px;
            cursor: pointer;
            color: rgb(29,167,106);
            background: rgb(235,250,243);
            position: relative;
            font-weight: bold;
            .el-icon-close{
                position: absolute;
                right: -5px;
                top: -5px;
                visibility: visible;
                background: rgb(29,167,106);
                color: #fff;
                visibility: hidden;
                border-radius: 50%;
                padding: 2px;
                transform: scale(0.6,0.6);
            }
            &:hover{
                .el-icon-close{
                    visibility: visible;
                }
            }
        }
    }
    .icon-add{
        cursor: pointer;
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
}
.group ::v-deep{
    list-style: none;
    padding: 0;
    margin: 0;
    max-height: 200px;
    overflow:auto;
    // &:hover{
    //     .multiple{
    //         background: #eee;
    //     }
    // }
    .multiple{
        cursor: pointer;
        display: flex;
        justify-content: space-between;
        height: 35px;
        line-height: 35px;
        &:hover{
            background: rgb(240, 240, 240)!important;
        }
        .label{
            width: 70%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            padding-left: 10px;
        }
        .icon{
            width: 20%;
        }
    }
}
.empty{
    text-align: center;
}
</style>