<template>
  <div class="login-container">
    <el-card class="login-form">
      <template slot="header">
        <div class="login-form__title">
          <span>系统管理平台</span>
        </div>
      </template>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px" class="form-container">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="验证码" prop="captcha">
          <el-input v-model="loginForm.captcha" placeholder="请输入验证码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loginLoading">登录</el-button>
          <el-button type="info" class="register-btn" @click="handleRegister">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        captcha: '',
        captchaId: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 50, message: '用户名长度在 3 到 50 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        captcha: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      },
      loginLoading: false
    }
  },
  mounted() {
    // 生成新的 captchaId
    this.loginForm.captchaId = Math.random().toString(36).substr(2, 9)
  },
  methods: {
    async handleLogin() {
      try {
        console.log('开始登录，表单数据:', this.loginForm)
        // 跳过表单验证，直接调用接口
        this.loginLoading = true
        console.log('开始调用登录接口')
        // 调用登录接口
        const response = await axios.post('/auth/login', {
          username: this.loginForm.username,
          password: this.loginForm.password,
          captcha: this.loginForm.captcha,
          captchaId: this.loginForm.captchaId
        })
        console.log('登录接口返回:', response)
        // 登录成功，保存 token 和用户信息
        if (response && response.code === 0) {
          // 从后端获取数据
          localStorage.setItem('token', response.data.accessToken)
          localStorage.setItem('userInfo', JSON.stringify(response.data.user))
          
          // 跳转到仪表盘
          this.$router.push('/dashboard')
        } else {
          this.$message.error(response.message || '登录失败')
        }
      } catch (error) {
        console.error('登录失败:', error)
        this.$message.error('登录失败，请检查网络连接或用户名密码是否正确')
      } finally {
        this.loginLoading = false
      }
    },
    handleRegister() {
      // 跳转到注册页面
      this.$message.info('注册功能暂未开放')
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
}

.login-form {
  width: 400px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-form__title {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.login-icon {
  font-size: 24px;
  margin-right: 10px;
  color: #409EFF;
}

.form-container {
  margin-top: 20px;
}

.login-btn {
  width: 100%;
  margin-bottom: 10px;
}

.register-btn {
  width: 100%;
}

.captcha-image {
  width: 100%;
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
}
</style>