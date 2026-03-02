<template>
  <div class="app-container">
    <template v-if="$route.name !== 'login'">
      <el-container class="main-container">
        <el-header class="app-header">
          <div class="header-left">
            <el-icon class="logo-icon"><i-ep-sunny /></el-icon>
            <span class="logo-text">系统管理平台</span>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" :src="userAvatar || defaultAvatar" />
                <span class="user-name">{{ userName || '未登录' }}</span>
                <el-icon class="el-icon--right"><i-ep-arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-container>
          <el-aside width="220px" class="app-sidebar">
            <el-menu
              :default-active="activeMenu"
              class="sidebar-menu"
              @select="handleMenuSelect"
              background-color="#001529"
              text-color="#fff"
              active-text-color="#409EFF"
              router
            >
              <!-- 仪表盘 -->
              <el-menu-item index="/dashboard">
                <el-icon><i-ep-data-line /></el-icon>
                <span>仪表盘</span>
              </el-menu-item>
              
              <!-- 动态菜单 -->
              <el-sub-menu 
                v-for="menu in menus" 
                v-if="menu.menuType === 'M' && menu.children && menu.children.length > 0" 
                :key="menu.id" 
                :index="menu.id.toString()"
              >
                <template #title>
                  <el-icon v-if="menu.icon"><component :is="menu.icon" /></el-icon>
                  <span>{{ menu.menuName }}</span>
                </template>
                <el-menu-item 
                  v-for="child in menu.children" 
                  :key="child.id" 
                  :index="child.path || '#'"
                >
                  <span>{{ child.menuName }}</span>
                </el-menu-item>
              </el-sub-menu>
              <el-menu-item 
                v-for="menu in menus" 
                v-else-if="menu.menuType === 'C'" 
                :key="menu.id" 
                :index="menu.path || '#'"
              >
                <el-icon v-if="menu.icon"><component :is="menu.icon" /></el-icon>
                <span>{{ menu.menuName }}</span>
              </el-menu-item>
            </el-menu>
          </el-aside>
          <el-main class="app-main">
            <router-view />
          </el-main>
        </el-container>
      </el-container>
    </template>
    <router-view v-else />
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'App',
  data() {
    return {
      userAvatar: '',
      userName: '',
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      menus: []
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    }
  },
  mounted() {
    this.loadUserInfo()
    this.loadMenus()
  },
  watch: {
    '$route': {
      handler() {
        this.loadUserInfo()
      },
      immediate: true
    }
  },
  methods: {
    loadUserInfo() {
      // 从 localStorage 获取用户信息
      const userInfo = localStorage.getItem('userInfo')
      if (userInfo) {
        const info = JSON.parse(userInfo)
        this.userName = info.username || info.nickname || info.realName
        this.userAvatar = info.avatar
      }
    },
    async loadMenus() {
      try {
        const response = await axios.get('/menu/tree')
        if (response && response.code === 0) {
          this.menus = response.data
        }
      } catch (error) {
        console.error('获取菜单失败:', error)
      }
    },
    handleMenuSelect(key, keyPath) {
      this.$router.push(key)
    },
    handleLogout() {
      // 清除 localStorage 中的用户信息和 token
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
      // 跳转到登录页面
      this.$router.push('/login')
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, 'Microsoft YaHei', Arial, sans-serif;
  font-size: 14px;
  line-height: 1.5;
  color: #333;
  background-color: #f0f2f5;
}

.app-container {
  height: 100vh;
  overflow: hidden;
}

.main-container {
  height: 100%;
}

.app-header {
  height: 64px;
  background-color: #fff;
  color: #303133;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-bottom: 1px solid #f0f0f0;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo-icon {
  font-size: 24px;
  margin-right: 10px;
  color: #409EFF;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #303133;
}

.user-name {
  margin: 0 10px;
  color: #303133;
}

.app-sidebar {
  background-color: #001529;
  height: calc(100vh - 64px);
  overflow-y: auto;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.sidebar-menu {
  height: 100%;
  border-right: none;
}

.app-main {
  padding: 24px;
  height: calc(100vh - 64px);
  overflow-y: auto;
  background-color: #f0f2f5;
}

/* 登录页面样式 */
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
}

.login-form {
  width: 400px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-form__title {
  text-align: center;
  margin-bottom: 30px;
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

/* 通用卡片样式 */
.card-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

/* 通用按钮样式 */
.btn-group {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

/* 通用表格样式 */
.table-container {
  margin-top: 20px;
}

/* 通用表单样式 */
.form-container {
  margin-top: 20px;
}

.form-item {
  margin-bottom: 20px;
}
</style>