<template>
  <div class="role-list-container">
    <el-card class="card-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">角色列表</span>
          <el-button type="primary" size="small" @click="refreshRoleList">刷新</el-button>
        </div>
      </template>
      <div class="table-container">
        <el-table v-if="roleList.length > 0" :data="roleList" stripe style="width: 100%">
          <el-table-column prop="id" label="角色ID" width="100" />
          <el-table-column prop="roleName" label="角色名称" />
          <el-table-column prop="roleCode" label="角色编码" />
          <el-table-column prop="description" label="角色描述" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
              <el-tag v-else size="small" type="success">启用</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
              <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无角色数据" class="empty-container" />
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'RoleList',
  data() {
    return {
      roleList: []
    }
  },
  mounted() {
    this.loadRoleList()
  },
  methods: {
    async loadRoleList() {
      try {
        const response = await axios.get('/role/list')
        if (response && response.code === 0) {
          this.roleList = response.data
        } else {
          this.$message.error(response.message || '获取角色列表失败')
        }
      } catch (error) {
        console.error('获取角色列表失败:', error)
        this.$message.error('获取角色列表失败，请检查网络连接')
      }
    },
    refreshRoleList() {
      this.loadRoleList()
    },
    handleView(row) {
      this.$message.info(`查看角色：${row.roleName}`)
      console.log('查看的角色数据:', row)
    },
    handleEdit(row) {
      this.$message.info(`编辑角色：${row.roleName}`)
      console.log('编辑的角色数据:', row)
    }
  }
}
</script>

<style scoped>
.role-list-container {
  height: 100%;
}

.empty-container {
  margin: 40px 0;
}
</style>