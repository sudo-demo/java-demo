<template>
  <div class="user-list-container">
    <el-card class="card-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">用户列表</span>
          <el-button type="primary" size="small" @click="refreshUserList">刷新</el-button>
        </div>
      </template>
      <div class="table-container">
        <el-table v-if="userList.length > 0" :data="userList" stripe style="width: 100%">
          <el-table-column prop="id" label="用户ID" width="100" />
          <el-table-column prop="user_id" label="用户ID" />
          <el-table-column prop="user_name" label="用户姓名" />
          <el-table-column prop="phone" label="手机号" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
              <el-tag v-else size="small" type="success">启用</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastLoginTime" label="最后登录时间" width="180" />
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
              <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无用户数据" class="empty-container" />
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'UserList',
  data() {
    return {
      userList: []
    }
  },
  mounted() {
    this.loadUserList()
  },
  methods: {
    async loadUserList() {
      try {
        const response = await axios.get('/user/list')
        if (response && response.code === 0) {
          this.userList = response.data
        } else {
          this.$message.error(response.message || '获取用户列表失败')
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
        this.$message.error('获取用户列表失败，请检查网络连接')
      }
    },
    refreshUserList() {
      this.loadUserList()
    },
    handleView(row) {
      this.$message.info(`查看用户：${row.user_id}`)
      console.log('查看的用户数据:', row)
    },
    handleEdit(row) {
      this.$message.info(`编辑用户：${row.user_id}`)
      console.log('编辑的用户数据:', row)
    }
  }
}
</script>

<style scoped>
.user-list-container {
  height: 100%;
}

.empty-container {
  margin: 40px 0;
}
</style>