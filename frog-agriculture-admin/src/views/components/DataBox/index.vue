<template>
  <div class="dataBox" :class="{border:isBorder}">
    <div class="dataBox-icon" v-if="isIcon" :style="{backgroundColor:backgroundColor}">
      <i :class="icon" ></i>
    </div>
    <div class="dataBox-des">
      <div class="dataBox-des-text">{{text}}</div>
      <div class="dataBox-des-value">{{value}}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DataBox",
  data() {
    return {};
  },
  props: {
      isIcon:{
        type:Boolean,
        default:true
      },
      isBorder:{
        type:Boolean,
        default:true
      },
      icon:{
          type:String,
          default:''
      },
      backgroundColor:{
          type:String,
          default:''
      },
      text:{
          type:String,
          default:''
      },
      value:{
          type:[Number,String],
          default:''
      }
  },
};
</script>

<style lang="scss" scoped>
.dataBox {
  display: flex;
  align-items:center;
  box-sizing: border-box;
  padding:10px;
  justify-content: flex-start;
  border-radius: 10px;
  &-icon {
    font-size: 30px;
    color: #fff;
    width: 40px;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 5px;
    margin-right: 20px;
  }
  &-des {
    &-text {
      font-weight: 400;
      color: #6c7180;
      font-size: 13px;
    }
    &-value {
      font-size: 20px;
      margin-top:5px;
    }
  }
}
.border{
    border: 1px solid #f3f3f3;
}
</style>
