<template>
  <div class="dashboard-container">
    <el-card class="dashboard-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="card-title">仪表盘</span>
          <el-button type="primary" size="small" @click="refreshData">刷新数据</el-button>
        </div>
      </template>
      
      <div class="dashboard-stats">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="stat-card">
              <template #header>
                <div class="stat-header">
                  <el-icon class="stat-icon"><i-ep-user /></el-icon>
                  <span>用户总数</span>
                </div>
              </template>
              <div class="stat-value">{{ userCount }}</div>
              <div class="stat-desc">较昨日 <span class="stat-change positive">+2.5%</span></div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <template #header>
                <div class="stat-header">
                  <el-icon class="stat-icon"><i-ep-position /></el-icon>
                  <span>角色总数</span>
                </div>
              </template>
              <div class="stat-value">{{ roleCount }}</div>
              <div class="stat-desc">较昨日 <span class="stat-change positive">+1.2%</span></div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <template #header>
                <div class="stat-header">
                  <el-icon class="stat-icon"><i-ep-menu /></el-icon>
                  <span>菜单总数</span>
                </div>
              </template>
              <div class="stat-value">{{ menuCount }}</div>
              <div class="stat-desc">较昨日 <span class="stat-change positive">+0.8%</span></div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <template #header>
                <div class="stat-header">
                  <el-icon class="stat-icon"><i-ep-view /></el-icon>
                  <span>登录次数</span>
                </div>
              </template>
              <div class="stat-value">{{ loginCount }}</div>
              <div class="stat-desc">今日 <span class="stat-change positive">+15</span></div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <div class="dashboard-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="content-card" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>最近登录记录</span>
                </div>
              </template>
              <el-table :data="loginLogs" style="width: 100%">
                <el-table-column prop="username" label="用户名" width="120" />
                <el-table-column prop="ip" label="登录IP" width="150" />
                <el-table-column prop="loginTime" label="登录时间" />
                <el-table-column prop="status" label="状态" width="80">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.status === 'success' ? 'success' : 'danger'">
                      {{ scope.row.status === 'success' ? '成功' : '失败' }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="content-card" shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>系统信息</span>
                </div>
              </template>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="系统名称">系统管理平台</el-descriptions-item>
                <el-descriptions-item label="版本">v1.0.0</el-descriptions-item>
                <el-descriptions-item label="前端框架">Vue 2.6.14</el-descriptions-item>
                <el-descriptions-item label="UI框架">Element UI 2.15.14</el-descriptions-item>
                <el-descriptions-item label="后端框架">Spring Boot 2.7.9</el-descriptions-item>
                <el-descriptions-item label="数据库">MySQL 8.0</el-descriptions-item>
                <el-descriptions-item label="服务器时间">{{ serverTime }}</el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Dashboard',
  data() {
    return {
      userCount: 0,
      roleCount: 0,
      menuCount: 0,
      loginCount: 0,
      loginLogs: [],
      serverTime: ''
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    async initData() {
      await this.loadDashboardData()
      this.refreshServerTime()
    },
    async refreshData() {
      try {
        await this.loadDashboardData()
        this.refreshServerTime()
        this.$message.success('数据刷新成功')
      } catch (error) {
        this.$message.error('数据刷新失败')
        console.error('数据刷新失败:', error)
      }
    },
    async loadDashboardData() {
      try {
        // 获取用户总数
        const userResponse = await axios.get('/system/user/list')
        if (userResponse && userResponse.code === 0) {
          this.userCount = userResponse.data.length
        }

        // 获取角色总数
        const roleResponse = await axios.get('/system/role/list')
        if (roleResponse && roleResponse.code === 0) {
          this.roleCount = roleResponse.data.length
        }

        // 获取菜单总数
        const menuResponse = await axios.get('/system/menu/tree')
        if (menuResponse && menuResponse.code === 0) {
          this.menuCount = this.countMenus(menuResponse.data)
        }

        // 获取登录日志（这里使用模拟数据，因为后端可能没有提供登录日志接口）
        this.loginLogs = [
          {
            username: 'admin',
            ip: '192.168.1.1',
            loginTime: new Date().toLocaleString('zh-CN'),
            status: 'success'
          },
          {
            username: 'test',
            ip: '192.168.1.2',
            loginTime: new Date(Date.now() - 3600000).toLocaleString('zh-CN'),
            status: 'success'
          },
          {
            username: 'user1',
            ip: '192.168.1.3',
            loginTime: new Date(Date.now() - 7200000).toLocaleString('zh-CN'),
            status: 'success'
          }
        ]

        // 模拟登录次数
        this.loginCount = Math.floor(Math.random() * 100) + 100
      } catch (error) {
        console.error('加载仪表盘数据失败:', error)
        // 使用默认值
        this.userCount = 0
        this.roleCount = 0
        this.menuCount = 0
        this.loginCount = 0
      }
    },
    countMenus(menuList) {
      let count = 0
      const countRecursive = (menus) => {
        menus.forEach(menu => {
          count++
          if (menu.children && menu.children.length > 0) {
            countRecursive(menu.children)
          }
        })
      }
      countRecursive(menuList)
      return count
    },
    refreshServerTime() {
      this.serverTime = new Date().toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 0;
}

.dashboard-card {
  margin-bottom: 20px;
}

.dashboard-stats {
  margin-bottom: 30px;
}

.stat-card {
  height: 100%;
}

.stat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-icon {
  font-size: 18px;
  color: #409EFF;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin: 15px 0;
  color: #303133;
}

.stat-desc {
  font-size: 14px;
  color: #909399;
}

.stat-change {
  font-size: 12px;
  font-weight: bold;
}

.stat-change.positive {
  color: #67C23A;
}

.stat-change.negative {
  color: #F56C6C;
}

.dashboard-content {
  margin-top: 20px;
}

.content-card {
  margin-bottom: 20px;
}

.refresh-btn {
  margin-left: 10px;
}
</style>
