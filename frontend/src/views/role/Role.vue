<template>
  <div class="role-container">
    <el-card class="card-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">角色管理</span>
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
  name: 'Role'
}
</script>

<style scoped>
.role-container {
  height: 100%;
}
</style>