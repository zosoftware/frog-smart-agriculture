<template>
  <div class="login">
    <el-row>
      <el-col :span="14">
        <div class="left">
          <div class="login-title">智慧农业平台</div>
          <div class="login-subTitle">专注于农业，面向未来，自主创新</div>
          <div class="login-bgImage">
            <img src="@/assets/images/loginBg.svg" alt="" />
          </div>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="right">
          <div class="loginForm-title">系统登录</div>
          <div class="loginForm-subTitle">SYSTEM LOGIN</div>
          <div class="loginForm-form">
            <el-form label-position="top">
              <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
                <el-form-item label="账号" prop="username">
                  <el-input
                    v-model="loginForm.username"
                    prefix-icon="el-icon-user"
                    placeholder="请输入用户名"
                  ></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                  <el-input
                    v-model="loginForm.password"
                    prefix-icon="el-icon-lock"
                    type="password"
                    placeholder="请输入密码"
                    show-password
                  ></el-input>
                </el-form-item>
                <el-form-item label="验证码" prop="code" v-if="captchaOnOff">
                  <el-row class="login-captchaBox">
                    <el-col :span="15">
                      <el-input
                        v-model="loginForm.code"
                        auto-complete="off"
                        placeholder="验证码"
                        prefix-icon="el-icon-circle-check"
                        @keyup.enter.native="handleLogin"
                      />
                    </el-col>
                    <el-col :span="8" :offset="1" style="display:flex" >
                      <div class="login-code">
                        <img :src="codeUrl" @click="getCode" />
                      </div>
                    </el-col>
                  </el-row>
                </el-form-item>

                <el-checkbox
                  v-model="loginForm.rememberMe"
                  style="margin: 0px 0px 25px 0px; color: #000"
                  >记住密码</el-checkbox
                >

                <el-form-item style="width: 100%">
                  <el-button
                    v-if="!bindAccount"
                    :loading="loading"
                    type="primary"
                    style="width: 100%"
                    @click.native.prevent="handleLogin"
                  >
                    <span v-if="!loading">登 录</span>
                    <span v-else>登 录 中...</span>
                  </el-button>
                  <el-button
                    v-else
                    :loading="loading"
                    type="primary"
                    style="width: 100%"
                    @click.native.prevent="handleLogin"
                  >
                    <span v-if="!loading">绑定</span>
                    <span v-else>绑 定 中...</span>
                  </el-button>
                </el-form-item>
              </el-form>
            </el-form>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import "element-ui/lib/theme-chalk/display.css";
import logo from "@/assets/logo/logo.gif";
import { getCodeImg, checkBindId, getErrorMsg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";

export default {
  name: "Login",
  data() {
    return {
      logo,
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: "",
        bindId: "",
      },
      loginRules: {
        username: [
          {
            required: true,
            trigger: "blur",
            message: "请输入您的账号",
          },
        ],
        password: [
          {
            required: true,
            trigger: "blur",
            message: "请输入您的密码",
          },
        ],
        code: [
          {
            required: true,
            trigger: "change",
            message: "请输入验证码",
          },
        ],
      },
      loading: false,
      // 验证码开关
      captchaOnOff: true,
      bindAccount: false,
      // 注册开关
      register: true,
      redirect: undefined,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  created() {
    let loginId = this.$route.query.loginId;
    if (loginId === undefined || loginId === null) {
      this.checkBind();
      this.checkErrorMsg();
      this.getCode();
      this.getCookie();
    } else {
      this.redirectLogin(loginId);
    }
  },
  methods: {
    redirectLogin(loginId) {
      this.loading = true;
      this.$store
        .dispatch("RedirectLogin", loginId)
        .then(() => {
          this.$router
            .push({
              path: this.redirect || "/",
            })
            .catch(() => {});
        })
        .catch(() => {
          this.loading = false;
          if (this.captchaOnOff) {
            this.getCode();
          }
          this.$router.push({
            query: {},
          });
        });
    },
    checkBind() {
      let query = this.$route.query;
      let bindId = query.bindId;
      if (bindId === undefined || bindId === null) {
        this.bindAccount = false;
      } else {
        this.bindAccount = true;
        checkBindId(bindId).then((res) => {
          this.bindAccount =
            res.bindAccount === undefined ? true : res.bindAccount;
          if (this.bindAccount) {
            this.loginForm.bindId = bindId;
          } else {
            this.loginForm.bindId = "";
            this.$router.push({
              query: {},
            });
          }
        });
      }
    },
    checkErrorMsg() {
      let errorId = this.$route.query.errorId;
      if (errorId !== undefined && errorId !== null) {
        getErrorMsg(errorId)
          .then((res) => {})
          .catch((err) => {
            this.$router.push({
              query: {},
            });
          });
      }
    },
    getCode() {
      getCodeImg().then((res) => {
        this.captchaOnOff =
          res.captchaOnOff === undefined ? true : res.captchaOnOff;
        if (this.captchaOnOff) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    qqLogin() {
      window.location.href = "http://localhost:8080/auth/render/qq";
    },
    authLogin() {
      this.$alert("第三方登录正在集成中...", "提示消息", {
        confirmButtonText: "确定",
        callback: (action) => {
          this.$message({
            type: "info",
            message: `action: ${action}`,
          });
        },
      });
    },
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, {
              expires: 30,
            });
            Cookies.set("password", encrypt(this.loginForm.password), {
              expires: 30,
            });
            Cookies.set("rememberMe", this.loginForm.rememberMe, {
              expires: 30,
            });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove("rememberMe");
          }
          console.log(this.loginForm);
          this.$store
            .dispatch("Login", this.loginForm)
            .then(() => {
              this.$router
                .push({
                  path: this.redirect || "/",
                })
                .catch(() => {});
            })
            .catch(() => {
              this.loading = false;
              if (this.captchaOnOff) {
                this.getCode();
              }
            });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.login {
  .left {
    height: 100vh;
    background: #109b5e;
    overflow: hidden;
    .login-title {
      display: flex;
      align-items: center;
      justify-content: center;
      padding-bottom: 26px;
      color: #fff;
      letter-spacing: 11px;
      font-size: 50px;
      font-weight: 500;
      margin-top: 15vh;
    }
    .login-subTitle {
      display: flex;
      align-items: center;
      justify-content: center;
      padding-bottom: 26px;
      color: #fff;
      font-size: 20px;
    }
    .login-bgImage {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 15vh;
      img {
        width: 75%;
      }
    }
  }
  .right {
    height: 100vh;
    overflow: hidden;
    .loginForm-title {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 30px;
      font-weight: bold;
      margin-top: 20vh;
    }
    .loginForm-subTitle {
      display: flex;
      align-items: center;
      justify-content: center;
      color: rgba(22, 23, 26, 0.1);
      font-size: 23px;
      white-space: nowrap;
      letter-spacing: 2px;
      margin-top:-10px;
    }
    .loginForm-form {
        padding:0 25%;
    }

    .login-captchaBox{
      display: flex;
      img{
        height: 50px;
      }
    }

    //组件样式修改
    ::v-deep {
        .el-input__inner{
            height: 50px;
            border-radius: 25px;
            background: rgb(245, 245, 245);
            padding-left: 40px;
            font-size: 20px;
            color:#000;
            border:1px solid whitesmoke;
        }
        .el-input__inner:focus{
          border:1px solid #109b5e;
        }
        .el-input__inner:placeholder-shown{
            font-size: 16px;
        }
        .el-input__prefix{
            font-size:20px;
            left:10px;
        }
        .el-button{
          height: 50px;
          border-radius: 30px;
          font-size: 20px;
        }
    }
  }
}
</style>
