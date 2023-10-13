<template>
<div class="login">
    <el-row>
        <el-col :xs="24">
            <div style="color:#fff;background-color:#0f73ee;width:100%;height:200px;text-align: center;padding:15px;font-family:'微软雅黑'">
                <div style="font-size:42px;padding-top:40px;width:300px;margin:0 auto;">
                    <img :src="logo" alt="logo" style="width:100px;height:100px;float:left;">
                    <div style="float:left;margin-top:13px;width:200px;text-align: left;">
                        <div>FastBee</div>
                        <div style="letter-spacing: 1.5px;font-size:20px;font-weight:600;margin-top:-8px;margin-left:3px;">开源物联网平台</div>
                    </div>
                </div>
            </div>
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
                <div style="margin-bottom:10px;font-size:14px;font-family:'微软雅黑';color:#f78e21;">演示账号：wumei  123456</div>
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                        <svg-icon slot="prefix" icon-class="user" class="input-icon" />
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码" @keyup.enter.native="handleLogin">
                        <svg-icon slot="prefix" icon-class="password" class="input-icon" />
                    </el-input>
                </el-form-item>
                <el-form-item prop="code" v-if="captchaOnOff">
                    <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%" @keyup.enter.native="handleLogin">
                        <svg-icon slot="prefix" icon-class="validCode" class="input-icon" />
                    </el-input>
                    <div class="login-code">
                        <img :src="codeUrl" @click="getCode" />
                    </div>
                </el-form-item>
                <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;color:#000;">记住密码</el-checkbox>

                <el-form-item style="width:100%;">
                    <el-button v-if="!bindAccount" :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
                        <span v-if="!loading">登 录</span>
                        <span v-else>登 录 中...</span>
                    </el-button>
                    <el-button v-else :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
                        <span v-if="!loading">绑定</span>
                        <span v-else>绑 定 中...</span>
                    </el-button>
                </el-form-item>
                <el-form-item>
                    <el-link href="https://fastbee.cn/" :underline="false" target="_blank" style="float:left;">返回官网</el-link>
                    <el-link href="https://fastbee.cn/doc" :underline="false" target="_blank" style="float:left;margin-left:20px;">查看文档</el-link>
                    <router-link :to='{path:"/register",query: this.$route.query }' style="float:left;margin-left:20px;">注册账号
                    </router-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
    <!--  底部  -->
    <div class="el-login-footer">
        <span>Copyright © 2023 <a target="_blank" href="http://fastbee.cn">FastBee</a> All Rights Reserved.</span>
    </div>
</div>
</template>

<script>
import 'element-ui/lib/theme-chalk/display.css';
import logo from '@/assets/logo/logo.gif'
import {
    getCodeImg,
    checkBindId,
    getErrorMsg
} from "@/api/login";
import Cookies from "js-cookie";
import {
    encrypt,
    decrypt
} from '@/utils/jsencrypt'

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
                bindId: ""
            },
            loginRules: {
                username: [{
                    required: true,
                    trigger: "blur",
                    message: "请输入您的账号"
                }],
                password: [{
                    required: true,
                    trigger: "blur",
                    message: "请输入您的密码"
                }],
                code: [{
                    required: true,
                    trigger: "change",
                    message: "请输入验证码"
                }]
            },
            loading: false,
            // 验证码开关
            captchaOnOff: true,
            bindAccount: false,
            // 注册开关
            register: true,
            redirect: undefined
        };
    },
    watch: {
        $route: {
            handler: function (route) {
                this.redirect = route.query && route.query.redirect;
            },
            immediate: true
        }
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
            this.$store.dispatch("RedirectLogin", loginId).then(() => {
                this.$router.push({
                    path: this.redirect || "/"
                }).catch(() => {});
            }).catch(() => {
                this.loading = false;
                if (this.captchaOnOff) {
                    this.getCode();
                }
                this.$router.push({
                    query: {}
                })
            });
        },
        checkBind() {
            let query = this.$route.query;
            let bindId = query.bindId;
            if (bindId === undefined || bindId === null) {
                this.bindAccount = false;
            } else {
                this.bindAccount = true;
                checkBindId(bindId).then(res => {
                    this.bindAccount = res.bindAccount === undefined ? true : res.bindAccount
                    if (this.bindAccount) {
                        this.loginForm.bindId = bindId;
                    } else {
                        this.loginForm.bindId = "";
                        this.$router.push({
                            query: {}
                        })
                    }
                })
            }
        },
        checkErrorMsg() {
            let errorId = this.$route.query.errorId;
            if (errorId !== undefined && errorId !== null) {
                getErrorMsg(errorId).then(res => {}).catch(err => {
                    this.$router.push({
                        query: {}
                    })
                })
            }
        },
        getCode() {
            getCodeImg().then(res => {
                this.captchaOnOff = res.captchaOnOff === undefined ? true : res.captchaOnOff;
                if (this.captchaOnOff) {
                    this.codeUrl = "data:image/gif;base64," + res.img;
                    this.loginForm.uuid = res.uuid;
                }
            });
        },
        getCookie() {
            const username = Cookies.get("username");
            const password = Cookies.get("password");
            const rememberMe = Cookies.get('rememberMe')
            this.loginForm = {
                username: username === undefined ? this.loginForm.username : username,
                password: password === undefined ? this.loginForm.password : decrypt(password),
                rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
            };
        },
        qqLogin() {
            window.location.href = "http://localhost:8080/auth/render/qq";
        },
        authLogin() {
            this.$alert('第三方登录正在集成中...', '提示消息', {
                confirmButtonText: '确定',
                callback: action => {
                    this.$message({
                        type: 'info',
                        message: `action: ${action}`
                    });
                }
            });
        },
        handleLogin() {
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    this.loading = true;
                    if (this.loginForm.rememberMe) {
                        Cookies.set("username", this.loginForm.username, {
                            expires: 30
                        });
                        Cookies.set("password", encrypt(this.loginForm.password), {
                            expires: 30
                        });
                        Cookies.set('rememberMe', this.loginForm.rememberMe, {
                            expires: 30
                        });
                    } else {
                        Cookies.remove("username");
                        Cookies.remove("password");
                        Cookies.remove('rememberMe');
                    }
                    console.log(this.loginForm)
                    this.$store.dispatch("Login", this.loginForm).then(() => {
                        this.$router.push({
                            path: this.redirect || "/"
                        }).catch(() => {});
                    }).catch(() => {
                        this.loading = false;
                        if (this.captchaOnOff) {
                            this.getCode();
                        }
                    });
                }
            });
        },
    }
};
</script>

<style lang="scss">
.login {
    height: 100%;
    overflow: auto;
}

.bindAccountTitle {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #333;
    font-size: 24px;
}

.login-form {
    margin: 30px auto 0 auto;
    padding: 15px;
    z-index: 1000;
    max-width: 350px;

    input {
        height: 38px;
        background-color: #f1f1f1;
        color: #666;
    }

    .input-icon {
        height: 39px;
        width: 14px;
        margin-left: 2px;
    }
}

.login-code {
    width: 33%;
    float: right;

    img {
        cursor: pointer;
        vertical-align: middle;
        border-radius: 3px;
        height: 38px;
    }
}

.el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #333;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
}
</style>
