<template>
  <div class="dataPanel">
      <div class="header">
          <span class="title">{{title}}</span>
          <span class="more" v-if="!!more">
              <router-link :to="link">
                {{more}} <i class="el-icon-arrow-right"></i>
              </router-link>
          </span>
      </div>
      <div class="main">
          <slot></slot>
      </div>
      <div class="footer">
          <slot name="footer"></slot>
      </div>
  </div>
</template>

<script>
export default {
    name:'DataPanel',
    props:{
        title:{
            type:String,
            default:''
        },
        more:{
            type:String,
            default:''
        },
        link:{
            type:String,
            default:''
        }
    },
    data(){
        return{

        }
    }
}
</script>

<style lang="scss" scoped>
.dataPanel{
    background: #fff;
    border-radius: 8px;
}
.header{
    display: flex;
    justify-content: space-between;
    padding: 0 10px;
    height: 50px;
    align-items: center;
    border-bottom:1px solid #e6eaf5;
    & .title{
        color: #16171a;
        font-size: 16px;
        margin-bottom: 0;
        font-weight: bold;
    }
    & .more{
        cursor: pointer;
        color: #a1a6b3;
        display: flex;
        align-items: center;
        font-size: 14px;
    }
}
</style>