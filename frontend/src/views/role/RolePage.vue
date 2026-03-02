<template>
  <div class="role-page-container">
    <el-card class="card-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">角色分页查询</span>
        </div>
      </template>
      <el-form :model="searchForm" :rules="searchRules" ref="searchFormRef" label-width="100px" class="form-container">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="searchForm.roleName" placeholder="请输入角色名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="禁用" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="页码" prop="page">
              <el-input v-model="searchForm.page" type="number" placeholder="请输入页码" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="每页大小" prop="pageSize">
              <el-input v-model="searchForm.pageSize" type="number" placeholder="请输入每页大小" :min="1" :max="100" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item>
              <el-button type="primary" @click="handleSearch" :loading="searchLoading">查询</el-button>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="table-container">
        <el-table v-if="rolePage.records && rolePage.records.length > 0" :data="rolePage.records" stripe style="width: 100%">
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
      <div class="pagination-container" v-if="rolePage.records && rolePage.records.length > 0">
        <el-pagination
          v-model:current-page="searchForm.page"
          v-model:page-size="searchForm.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="rolePage.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'RolePage',
  data() {
    return {
      searchForm: {
        page: 1,
        pageSize: 10,
        roleName: '',
        status: ''
      },
      searchRules: {
        page: [
          { required: true, message: '请输入页码', trigger: 'blur' },
          { type: 'number', message: '页码必须是数字', trigger: 'blur' },
          { min: 1, message: '页码必须大于等于1', trigger: 'blur' }
        ],
        pageSize: [
          { required: true, message: '请输入每页大小', trigger: 'blur' },
          { type: 'number', message: '每页大小必须是数字', trigger: 'blur' },
          { min: 1, message: '每页大小必须大于等于1', trigger: 'blur' },
          { max: 100, message: '每页大小必须小于等于100', trigger: 'blur' }
        ]
      },
      rolePage: {
        records: [],
        total: 0
      },
      searchLoading: false
    }
  },
  mounted() {
    this.handleSearch()
  },
  methods: {
    async handleSearch() {
      try {
        // 表单验证
        await this.$refs.searchFormRef.validate()
        
        this.searchLoading = true
        
        const response = await axios.get('/system/role/page', {
          params: this.searchForm
        })
        
        if (response.code === 200 && response.data) {
          this.rolePage = response.data
        } else {
          this.rolePage = { records: [], total: 0 }
          this.$message.error(response.message || '查询角色分页失败')
        }
      } catch (error) {
        console.error('查询角色分页失败:', error)
        this.$message.error('查询角色分页失败，请检查网络连接')
      } finally {
        this.searchLoading = false
      }
    },
    resetSearch() {
      this.$refs.searchFormRef.resetFields()
      this.rolePage = { records: [], total: 0 }
    },
    handleSizeChange(size) {
      this.searchForm.pageSize = size
      this.handleSearch()
    },
    handleCurrentChange(current) {
      this.searchForm.page = current
      this.handleSearch()
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
.role-page-container {
  height: 100%;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.empty-container {
  margin: 40px 0;
}
</style>