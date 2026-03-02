<template>
  <div class="menu-container">
    <el-card class="card-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">菜单管理</span>
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
  name: 'Menu'
}
</script>

<style scoped>
.menu-container {
  height: 100%;
}
</style>