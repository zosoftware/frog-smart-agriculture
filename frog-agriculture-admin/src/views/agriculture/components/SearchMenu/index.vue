<template>
  <div class="search-menu">
    <div class="search-menu-header">{{title}}</div>
    <div class="search-menu-body">
      <el-input
        v-model="queryParams[searchParma]"
        @input="handleInput"
        :placeholder="searchPlaceholder"
        suffix-icon="el-icon-search"
      >
      </el-input>

      <el-menu
        v-if="list.length > 0"
        @select="handleSelect"
        v-loading="loading"
        :collapse-transition="false"
        :default-active="defaultActive"
      >
        <template v-if="isSub">
          <el-submenu
            v-for="(item,index) in list"
            :index="index.toString()"
            :key="item[keyName]"
          >
            <template slot="title">
              <svg-icon
                :icon-class="icon"
                class="margin-left-5 margin-right-10"
              />
              <span slot="title">{{
                item[labelName].length > 11 ? `${item[labelName].substring(0, 11)}...`: item[labelName]
              }}</span>
            </template>
            <template v-if="item.children.length>0">
                <el-menu-item   v-for="subItem in item.children" :key="subItem[subKeyName]" :index="subItem[subKeyName].toString()" :disabled="subTag[subItem[subTag.propertyName]].disable">
                <template #title >
                    <svg-icon :icon-class="subIcon" class="margin-left-10 margin-right-5" />
                    <span class="font-size-13">{{subItem[subLabelName].length > 11? `${subItem[subLabelName].substring(0, 11)}...`: subItem[subLabelName]}}</span>
                    <el-tag class="margin-left-5" :type="subTag[subItem[subTag.propertyName]].type" size="mini">{{subTag[subItem[subTag.propertyName]].text}}</el-tag>
                </template>
                </el-menu-item>
            </template>
            <template v-else>
                <div class="font-size-12 flex jcc">
                    暂无数据
                </div>
            </template>

          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item
            v-for="(item, index) in list"
            :index="index.toString()"
            :key="index"
            :class="customClass"
          >
            <template #title>
                <slot :data="item">
                    <svg-icon :icon-class="icon" class="margin-left-5 margin-right-5" />
                    <span>{{item[labelName].length > 11? `${item[labelName].substring(0, 11)}...`: item[labelName]}}</span>
                </slot>
            </template>
          </el-menu-item>
        </template>
      </el-menu>
      <el-empty v-else :image-size="100" description="暂无数据"></el-empty>
    </div>
    <div class="search-menu-footer">
      <el-pagination
        small
        background
        layout="prev, pager, next"
        :page-size="queryParams.pageSize"
        :total="total"
        @current-change="handlePagination"
        @prev-click="handlePagination"
        @next-click="handlePagination"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
export default {
  name: 'SearchMenu',
  props: {
    title:{
        type:String,
    },
    fun: [Function],
    searchParma: [String],
    searchPlaceholder: [String],
    keyName: [String],
    labelName: [String],
    activeMenu: {
      type: Object,
      default:function(){
          return{
              isActive:false,
              name:'',//属性
              value:''//值
          }
      }
    },
    icon: {
      type: String,
      default: 'land',
    },
    isSub: false,
    subIcon:{
        type:String,
        default:'channel'
    },
    subKeyName:[String],
    subLabelName:[String],
    subTag:Object,
    customClass:{
        type:String,
        default:''
    }
  },
  data() {
    return {
      loading: false,
      list: [],
      total: null,
      queryParams: {
        pageNum: 1,
        pageSize: 16,
        [this.searchParma]: null,
      },
    };
  },
  computed:{
      defaultActive:{
          get:function(){
              const { isActive,name,value } = this.activeMenu;
              const { list } = this;
              if(isActive){
                  if(name && value){
                      let index =list.findIndex(item=>item[name]==value);
                      return index == -1?'0':index.toString();
                  }else{
                      return '0'
                  }
              }else{
                  return ''
              }
          }
      },
  },
  watch:{
      dataList:{
          handler:function(){

          }
      }
  },
  async created() {
    await this.getList();
    const { isActive,name,value } = this.activeMenu;
    const { list } = this;
    if(isActive){
        if(name && value){
            let item =list.find((i)=>i[name]==value);
            this.$emit('select',item ?item:this.list[0])
        }else{
            this.$emit('select',this.list[0])
        }
    }
  },
  methods: {
    /** 查询列表 */
    async getList() {
      this.loading = true;
      if (
        this.fun !== null &&
        typeof this.fun !== 'undefined' &&
        typeof this.fun === 'function'
      ) {
        const { rows, total } = await this.fun(this.queryParams);
        this.list = rows;
        this.total = total;
        this.loading = false;
      }
    },
    handleSelect(index) {
        if(this.isSub){
            let childrenList=[];
            this.list.forEach(item => {
                childrenList = childrenList.concat(item.children);
            });
            this.$emit('select', childrenList.filter(item=>item.channelSipId==index)[0]);
        }else{
            this.$emit('select', this.list[Number(index)]);
        }
    },
    handleInput(value) {
      this.queryParams[this.searchParma] = value;
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handlePagination(e) {
      this.queryParams.pageNum = e;
      this.getList();
    }
  },
};
</script>
<style lang="scss" scoped>
.search-menu {
  &-header {
    height: 70px;
    display: flex;
    align-items: center;
    padding-left: 15px;
  }
  &-body {
    height: calc(100vh - 84px - 70px - 50px - 30px);
    padding: 0px 15px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  &-footer {
    height: 50px;
    text-align: center;
  }
}
::v-deep {
  & .el-menu {
    border-right: none;
    width: 100%;
    margin-top: 10px;
  }
  & .el-menu-item {
    padding-left: 0 !important;
    height: 40px;
    line-height: 40px;
  }
  & .el-submenu__title{
      padding-left: 5px !important;
  }
}
</style>
