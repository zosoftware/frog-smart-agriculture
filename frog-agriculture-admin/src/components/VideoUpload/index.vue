<template>
  <div class="video_upload">
    <ul class="upload-list">
      <transition-group class="animation" name="el-zoom-in-top">
        <li class="upload-list_item" v-for="(item,index) in fileList" :key="item">
          <video  class="video" controls :src="'/dev-api'+item"></video>
          <i class="el-icon-delete" v-show="!disabled" @click="handleDel(index)"></i>
        </li>
      </transition-group>
    </ul>
    <el-upload
      class="upload"
      :action="uploadImgUrl"
      :show-file-list="false"
      :on-success="handleSuccess"
      :headers="headers"
      :on-error="handleUploadError"
      :disabled="disabled"
      :multiple="multiple"
    >
      <i class="el-icon-plus"></i>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible" title="预览" width="500" append-to-body>
      <video
        controls
        autoplay
        
        :src="'/dev-api'+dialogImageUrl"
        style="display: block; max-width: 100%; margin: 0 auto;height:400px"
      ></video>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
export default {
  props: {
    value: String,
    multiple: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      //uploadImgUrl: "dev-api/common/upload",
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      headers: {
        // Authorization:"Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImRlN2M5NzVkLTcwZmYtNDUyMi1hM2U5LTE3YjdkMmEyYmMxYyJ9.jFHzsT_fyRDK1CzLxCsxCRFoY2Kqp5kYL4odD48tVwlrEN809FC2Ox95Eo0v33cWwJaTWuC1Jz9BE-9b0Sc9Sw"
        Authorization: "Bearer " + getToken()
      },
      fileList: [],
      dialogVisible: false,
      dialogImageUrl: null
    };
  },
  watch: {
    value: {
      handler(value) {
        if (value) {
          this.fileList = value.split(",");
        }
      },
      immediate: true
    }
  },
  methods: {
    handleSuccess(response, file, fileList) {
      this.fileList.push(response.fileName);
      this.$emit("input", this.fileList.toString());
      this.$emit("change", this.fileList.toString());
    },
    handleDel(i) {
      this.fileList.splice(i, 1);
      this.$emit("input", this.fileList.toString());
      this.$emit("change", this.fileList.toString());
    },
    // 上传失败
    handleUploadError() {
      this.$modal.msgError("上传图片失败，请重试");
      this.$modal.closeLoading();
    },
    handlePreview(src) {
      this.dialogImageUrl = src;
      this.dialogVisible = true;
    }
  }
};
</script>

<style lang="scss" scoped>
.video_upload {
  display: flex;
  flex-wrap: wrap;
  .upload-list {
    list-style: none;
    margin: 0;
    padding: 0;
    .animation {
      display: flex;
    }
    .upload-list_item {
      width: 270px;
      height: 150px;
      margin-right: 6px;
      border-radius: 10px;
      overflow: hidden;
      position: relative;
      .video {
        width: 100%;
        height: 100%;
      }
      .el-icon-delete {
        position: absolute;
        right: 10px;
        top: 10px;
        visibility: hidden;
        border-radius: 2px;
        padding: 2px;
      }
      &:hover {
        .el-icon-delete {
          visibility: visible;
          background: #fff;
          &:hover {
            cursor: pointer;
            color: red;
          }
        }
      }
    }
  }
  .upload ::v-deep {
    .el-icon-plus {
      width: 270px;
      height: 150px;
      font-size: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 1px dashed #c0ccda;
      color: #ccc;
      border-radius: 10px;
      &:hover {
        border-color: #409eff;
        color: #409eff;
        background: #fbfdff;
      }
    }
  }
}
</style>
