<template>
    <div class="calendarSelect" :style="{cursor:disabled?'not-allowed':'point'}">
        <div class="select_content">
            <el-date-picker
            :class="{'disableClass':disabled}"
            v-model="date"
            type="date"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            :editable="false"
            :clearable="false"
            :picker-options="pickerOptions"
            :disabled="disabled"
            @change="changeData"
            >
            </el-date-picker>
            <div class="date">
                <div v-if="date">{{date}}</div>
                <div v-else>--</div>
                <div><slot>日期选择</slot></div>
            </div>
            <div class="del">
                <i v-if="!disabled" @click="clearDate" class="el-icon-close"></i>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name:"CalendarSelect",
    props:{
        value:{
            type:String
        },
        startDate:{//控制结束时间选择时，不能选择开始之前的时间
            type:String
        },
        disabled:{
            type:Boolean,
            default:false
        }
    },
    data() {
        return {
            date:null,
            pickerOptions:{
                disabledDate:(time)=> {
                    return time.getTime() < Date.parse(this.startDate);
                }
            }
        }
    },
    watch:{
        value:{
            handler(value){
                this.date=value
            },
            immediate:true
        },
        date:{
            handler(value){
                this.$emit('input',value)
            },
            immediate:true
        }
    },
    methods:{
        clearDate(){
            if (this.date) {
                this.date=null
                this.$emit('change',this.date)
            }
        },
        changeData(value){
            this.$emit('change',value)
        }
    }
}
</script>

<style lang="scss" scoped>
.calendarSelect{
    user-select: none;
    cursor: pointer;
    height: 50px;
    display: flex;
    align-items: center;
    .select_content ::v-deep{
        display: flex;
        height: 35px;
        align-items: center;
        .el-icon-date{
            font-size: 15px;
            color: #aaa;
            width: 35px;
            line-height: 35px;
        }
        .date{
            height: 40px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            margin-right: 10px;;
            color: #999;
            div{
                &:first-of-type{
                    font-size: 16px;
                }
                &:last-of-type{
                    font-size: 12px;
                }
            }
        }
        .el-date-editor{
            width: 35px;
            height: 35px;
            border: 1px solid #aaa;
            border-radius: 50%;
            overflow: hidden;
            margin-right: 10px;
            &{
                &.disableClass{
                    background: #f5f5f5;
                    border-color: #f5f5f5;
                }
            }
            &:hover{
                border: 1px solid rgb(29,167,106);
            }
            .el-input__inner{
                display: none;
                .el-input__icon{
                    line-height: 35px;
                    width: 100%;
                    margin: 0;
                    padding: 0;
                }
            }
            .el-input__prefix{
                left: 0;
            }
        }
        &:hover{
            .del{
                .el-icon-close{
                    visibility: visible;;
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
        .el-icon-close{
            visibility: hidden;
            &:hover{
                color: red;
            }
        }
    }

}
</style>>
