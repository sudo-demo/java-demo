<template>
  <div class="card-container">
    <div class="card-header">
      <h2 class="card-title">用户联系信息</h2>
    </div>
    
    <div class="form-container">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        
        <div class="btn-group">
          <el-button type="primary" @click="getContactInfo">获取联系信息</el-button>
          <el-button @click="resetForm">重置</el-button>
        </div>
      </el-form>
    </div>
    
    <div v-if="contactInfo" class="table-container">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <span>联系信息详情</span>
          </div>
        </template>
        
        <el-table :data="[contactInfo]" style="width: 100%">
          <el-table-column prop="userId" label="用户ID" width="180" />
          <el-table-column prop="username" label="用户名" width="180" />
          <el-table-column prop="nickname" label="昵称" width="180" />
          <el-table-column prop="phone" label="手机号" width="180" />
          <el-table-column prop="email" label="邮箱" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ContactInfo',
  data() {
    return {
      form: {
        userId: ''
      },
      contactInfo: null,
      loading: false
    }
  },
  methods: {
    getContactInfo() {
      if (!this.form.userId) {
        this.$message.error('请输入用户ID')
        return
      }
      
      this.loading = true
      
      // 调用正式的敏感数据接口，需要认证
      axios.get(`/system/api/sensitive/user/contact`, {
        params: {
          userId: this.form.userId
        }
      })
        .then(response => {
          if (response.code === 0) {
            this.contactInfo = response.data
            this.$message.success('获取联系信息成功')
          } else {
            this.$message.error(response.message || '获取联系信息失败')
          }
        })
        .catch(error => {
          console.error('获取联系信息失败:', error)
          this.$message.error('获取联系信息失败: ' + (error.message || '网络错误'))
        })
        .finally(() => {
          this.loading = false
        })
    },
    resetForm() {
      this.form.userId = ''
      this.contactInfo = null
    }
  }
}
</script>

<style scoped>
.card-container {
  margin: 0;
}

.form-container {
  margin-top: 0;
}

.btn-group {
  margin-top: 20px;
}

.table-container {
  margin-top: 30px;
}
</style>
