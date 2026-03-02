<template>
  <div class="menu-children-container">
    <el-card class="card-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">子菜单查询</span>
        </div>
      </template>
      <el-form :model="searchForm" :rules="searchRules" ref="searchFormRef" label-width="100px" class="form-container">
        <el-form-item label="父菜单ID" prop="parentId">
          <el-input v-model="searchForm.parentId" placeholder="请输入父菜单ID" type="number" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :loading="searchLoading">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="table-container">
        <el-table v-if="childrenData.length > 0" :data="childrenData" stripe style="width: 100%">
          <el-table-column prop="id" label="菜单ID" width="100" />
          <el-table-column prop="menuName" label="菜单名称" />
          <el-table-column prop="parentId" label="父菜单ID" width="100" />
          <el-table-column prop="path" label="路径" />
          <el-table-column prop="component" label="组件" />
          <el-table-column prop="menuType" label="菜单类型" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.menuType === 0" size="small">目录</el-tag>
              <el-tag v-else-if="scope.row.menuType === 1" size="small" type="success">菜单</el-tag>
              <el-tag v-else-if="scope.row.menuType === 2" size="small" type="warning">按钮</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
              <el-tag v-else size="small" type="success">启用</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无子菜单数据" class="empty-container" />
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'MenuChildren',
  data() {
    return {
      searchForm: {
        parentId: ''
      },
      searchRules: {
        parentId: [
          { required: true, message: '请输入父菜单ID', trigger: 'blur' },
          { type: 'number', message: '父菜单ID必须是数字', trigger: 'blur' }
        ]
      },
      childrenData: [],
      searchLoading: false
    }
  },
  methods: {
    async handleSearch() {
      try {
        // 表单验证
        await this.$refs.searchFormRef.validate()
        
        this.searchLoading = true
        
        const response = await axios.get('/system/menu/children', {
          params: {
            parentId: this.searchForm.parentId
          }
        })
        
        if (response.code === 200 && response.data) {
          this.childrenData = response.data
        } else {
          this.childrenData = []
          this.$message.error(response.message || '查询子菜单失败')
        }
      } catch (error) {
        console.error('查询子菜单失败:', error)
        this.$message.error('查询子菜单失败，请检查网络连接')
      } finally {
        this.searchLoading = false
      }
    },
    resetSearch() {
      this.$refs.searchFormRef.resetFields()
      this.childrenData = []
    },
    handleView(row) {
      this.$message.info(`查看菜单：${row.menuName}`)
      console.log('查看的菜单数据:', row)
    }
  }
}
</script>

<style scoped>
.menu-children-container {
  height: 100%;
}

.empty-container {
  margin: 40px 0;
}
</style>