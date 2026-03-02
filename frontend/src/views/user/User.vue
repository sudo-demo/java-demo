<template>
  <div class="user-container">
    <el-card class="card-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">用户管理</span>
        </div>
      </template>
      <el-container>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'User'
}
</script>

<style scoped>
.user-container {
  height: 100%;
}
</style>