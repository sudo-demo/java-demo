<template>
  <div class="menu-tree-container">
    <el-card class="card-container" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="card-title">菜单树形结构</span>
          <div class="header-actions">
            <el-button 
              type="primary" 
              size="small" 
              @click="handleAddRootMenu"
              icon="el-icon-plus"
            >
              添加根菜单
            </el-button>
            <el-button 
              type="default" 
              size="small" 
              @click="toggleExpandAll"
              :icon="expandAll ? 'el-icon-s-fold' : 'el-icon-s-unfold'"
            >
              {{ expandAll ? '折叠全部' : '展开全部' }}
            </el-button>
            <el-button 
              type="default" 
              size="small" 
              @click="refreshMenuTree"
              :loading="loading"
              icon="el-icon-refresh"
            >
              刷新
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索框 -->
      <div class="search-bar">
        <el-input
          v-model="filterText"
          placeholder="输入菜单名称搜索"
          prefix-icon="el-icon-search"
          size="small"
          clearable
          class="search-input"
        />
      </div>
      
      <!-- 菜单树 -->
      <div class="tree-container" v-loading="loading">
        <el-tree
          v-if="menuTreeData.length > 0"
          ref="menuTree"
          :data="menuTreeData"
          :props="treeProps"
          :filter-node-method="filterNode"
          node-key="id"
          :default-expand-all="expandAll"
          class="menu-tree"
          @node-click="handleNodeClick"
          @node-contextmenu="handleNodeContextMenu"
          :expand-on-click-node="false"
        >
          <template #default="{ node, data }">
            <span class="tree-node-content">
              <i class="node-icon" :class="data.icon || getNodeIconClass(data)"></i>
              <span class="node-text" :class="{ 'node-text-disabled': data.status === '1' }">{{ data.menuName }}</span>
              <span class="node-type" :class="getTypeClass(data)">{{ getTypeText(data) }}</span>
              <span class="node-status" v-if="data.status === '1'">禁用</span>
              <span class="node-actions">
                <el-button 
                  type="text" 
                  size="mini" 
                  icon="el-icon-plus" 
                  @click.stop="handleAddSubMenu(data)"
                  title="添加子菜单"
                />
                <el-button 
                  type="text" 
                  size="mini" 
                  icon="el-icon-edit" 
                  @click.stop="handleEditMenu(data)"
                  title="编辑"
                />
                <el-button 
                  type="text" 
                  size="mini" 
                  icon="el-icon-delete" 
                  @click.stop="handleDeleteMenu(data)"
                  title="删除"
                  class="delete-btn"
                />
              </span>
            </span>
          </template>
        </el-tree>
        <el-empty 
          v-else 
          description="暂无菜单数据" 
          class="empty-container"
          image-size="120"
        />
      </div>
    </el-card>
    
    <!-- 添加/编辑菜单对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="menuForm"
        :model="menuForm"
        :rules="menuRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="上级菜单" prop="parentId">
          <el-cascader
            v-model="menuForm.parentId"
            :options="menuTreeOptions"
            :props="cascaderProps"
            placeholder="选择上级菜单(不选则为根菜单)"
            clearable
            filterable
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="menuForm.menuType">
            <el-radio label="M">目录</el-radio>
            <el-radio label="C">菜单</el-radio>
            <el-radio label="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="menuForm.menuName" placeholder="请输入菜单名称" maxlength="50" show-word-limit />
        </el-form-item>
        
        <el-form-item label="菜单图标" prop="icon" v-if="menuForm.menuType !== 'F'">
          <icon-selector v-model="menuForm.icon" placeholder="选择菜单图标" />
        </el-form-item>
        
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input-number v-model="menuForm.orderNum" :min="0" :max="9999" controls-position="right" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="路由地址" prop="path" v-if="menuForm.menuType !== 'F'">
          <el-input v-model="menuForm.path" placeholder="请输入路由地址" maxlength="200" />
        </el-form-item>
        
        <el-form-item label="组件路径" prop="component" v-if="menuForm.menuType === 'C'">
          <el-input v-model="menuForm.component" placeholder="请输入组件路径" maxlength="255" />
        </el-form-item>
        
        <el-form-item label="权限标识" prop="perms" v-if="menuForm.menuType !== 'M'">
          <el-input v-model="menuForm.perms" placeholder="请输入权限标识,如:system:user:list" maxlength="100" />
        </el-form-item>
        
        <el-form-item label="路由参数" prop="query" v-if="menuForm.menuType === 'C'">
          <el-input v-model="menuForm.query" placeholder="请输入路由参数" maxlength="255" />
        </el-form-item>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否外链" prop="isFrame">
              <el-radio-group v-model="menuForm.isFrame">
                <el-radio :label="0">是</el-radio>
                <el-radio :label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否缓存" prop="isCache">
              <el-radio-group v-model="menuForm.isCache">
                <el-radio :label="0">缓存</el-radio>
                <el-radio :label="1">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单状态" prop="status">
              <el-radio-group v-model="menuForm.status">
                <el-radio label="0">显示</el-radio>
                <el-radio label="1">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示状态" prop="visible">
              <el-radio-group v-model="menuForm.visible">
                <el-radio label="0">显示</el-radio>
                <el-radio label="1">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注" prop="remark">
          <el-input v-model="menuForm.remark" type="textarea" :rows="3" placeholder="请输入备注" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading" size="small">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/menu'
import IconSelector from '@/components/IconSelector.vue'

export default {
  name: 'MenuTree',
  components: {
    IconSelector
  },
  data() {
    return {
      menuTreeData: [],
      menuTreeOptions: [],
      treeProps: {
        children: 'children',
        label: 'menuName'
      },
      cascaderProps: {
        checkStrictly: true,
        value: 'id',
        label: 'menuName',
        children: 'children',
        emitPath: false
      },
      loading: false,
      submitLoading: false,
      filterText: '',
      expandAll: true,
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      menuForm: {
        id: null,
        parentId: 0,
        menuName: '',
        menuType: 'M',
        icon: '',
        orderNum: 0,
        path: '',
        component: '',
        perms: '',
        query: '',
        isFrame: 1,
        isCache: 0,
        status: '0',
        visible: '0',
        remark: ''
      },
      menuRules: {
        menuName: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        menuType: [
          { required: true, message: '请选择菜单类型', trigger: 'change' }
        ],
        orderNum: [
          { required: true, message: '请输入显示顺序', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入路由地址', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.menuTree.filter(val)
    }
  },
  mounted() {
    this.loadMenuTree()
  },
  methods: {
    async loadMenuTree() {
      this.loading = true
      try {
        const response = await getMenuTree()
        if (response && response.code === 0) {
          this.menuTreeData = response.data || []
          this.menuTreeOptions = this.buildMenuTreeOptions(this.menuTreeData)
        } else {
          this.$message.error(response.message || '获取菜单树形结构失败')
        }
      } catch (error) {
        console.error('获取菜单树形结构失败:', error)
        this.$message.error('获取菜单树形结构失败,请检查网络连接')
      } finally {
        this.loading = false
      }
    },
    
    buildMenuTreeOptions(tree, result = []) {
      tree.forEach(item => {
        const node = {
          id: item.id,
          menuName: item.menuName,
          children: item.children && item.children.length > 0 ? [] : undefined
        }
        if (node.children) {
          this.buildMenuTreeOptions(item.children, node.children)
        }
        result.push(node)
      })
      return result
    },
    
    refreshMenuTree() {
      this.loadMenuTree()
    },
    
    toggleExpandAll() {
      this.expandAll = !this.expandAll
      this.toggleTreeExpand(this.menuTreeData, this.expandAll)
    },
    
    toggleTreeExpand(data, expand) {
      data.forEach(item => {
        this.$refs.menuTree.store.nodesMap[item.id].expanded = expand
        if (item.children && item.children.length > 0) {
          this.toggleTreeExpand(item.children, expand)
        }
      })
    },
    
    filterNode(value, data) {
      if (!value) return true
      return data.menuName.indexOf(value) !== -1
    },
    
    handleNodeClick(data) {
      console.log('点击的菜单数据:', data)
    },
    
    handleNodeContextMenu(event, node, data) {
      event.preventDefault()
    },
    
    handleAddRootMenu() {
      this.resetForm()
      this.dialogTitle = '添加根菜单'
      this.isEdit = false
      this.menuForm.parentId = 0
      this.dialogVisible = true
    },
    
    handleAddSubMenu(data) {
      this.resetForm()
      this.dialogTitle = `添加子菜单 - ${data.menuName}`
      this.isEdit = false
      this.menuForm.parentId = data.id
      this.dialogVisible = true
    },
    
    handleEditMenu(data) {
      this.resetForm()
      this.dialogTitle = '编辑菜单'
      this.isEdit = true
      this.menuForm = {
        id: data.id,
        parentId: data.parentId || 0,
        menuName: data.menuName,
        menuType: data.menuType,
        icon: data.icon || '',
        orderNum: data.orderNum || 0,
        path: data.path || '',
        component: data.component || '',
        perms: data.perms || '',
        query: data.query || '',
        isFrame: data.isFrame !== undefined ? data.isFrame : 1,
        isCache: data.isCache !== undefined ? data.isCache : 0,
        status: data.status || '0',
        visible: data.visible || '0',
        remark: data.remark || ''
      }
      this.dialogVisible = true
    },
    
    handleDeleteMenu(data) {
      if (data.children && data.children.length > 0) {
        this.$message.warning('该菜单包含子菜单,请先删除子菜单')
        return
      }
      
      this.$confirm(`确定要删除菜单 "${data.menuName}" 吗?`, '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteMenu(data.id)
          if (response && response.code === 0) {
            this.$message.success('删除菜单成功')
            this.loadMenuTree()
          } else {
            this.$message.error(response.message || '删除菜单失败')
          }
        } catch (error) {
          console.error('删除菜单失败:', error)
          this.$message.error('删除菜单失败')
        }
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    
    submitForm() {
      this.$refs.menuForm.validate(async (valid) => {
        if (!valid) {
          return false
        }
        
        this.submitLoading = true
        try {
          const formData = { ...this.menuForm }
          let response
          
          if (this.isEdit) {
            response = await updateMenu(formData.id, formData)
          } else {
            response = await addMenu(formData)
          }
          
          if (response && response.code === 0) {
            this.$message.success(this.isEdit ? '编辑菜单成功' : '添加菜单成功')
            this.dialogVisible = false
            this.loadMenuTree()
          } else {
            this.$message.error(response.message || (this.isEdit ? '编辑菜单失败' : '添加菜单失败'))
          }
        } catch (error) {
          console.error('提交菜单失败:', error)
          this.$message.error(this.isEdit ? '编辑菜单失败' : '添加菜单失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    
    resetForm() {
      this.menuForm = {
        id: null,
        parentId: 0,
        menuName: '',
        menuType: 'M',
        icon: '',
        orderNum: 0,
        path: '',
        component: '',
        perms: '',
        query: '',
        isFrame: 1,
        isCache: 0,
        status: '0',
        visible: '0',
        remark: ''
      }
      if (this.$refs.menuForm) {
        this.$refs.menuForm.clearValidate()
      }
    },
    
    handleDialogClose() {
      this.resetForm()
    },
    
    getNodeIconClass(data) {
      switch (data.menuType) {
        case 'M':
          return 'el-icon-menu'
        case 'C':
          return 'el-icon-document'
        case 'F':
          return 'el-icon-position'
        default:
          return 'el-icon-menu'
      }
    },
    
    getTypeText(data) {
      switch (data.menuType) {
        case 'M':
          return '目录'
        case 'C':
          return '菜单'
        case 'F':
          return '按钮'
        default:
          return ''
      }
    },
    
    getTypeClass(data) {
      switch (data.menuType) {
        case 'M':
          return 'type-directory'
        case 'C':
          return 'type-menu'
        case 'F':
          return 'type-button'
        default:
          return ''
      }
    }
  }
}
</script>

<style scoped>
.menu-tree-container {
  height: 100%;
  padding: 20px;
}

.card-container {
  transition: all 0.3s ease;
}

.card-container:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-bar {
  margin-bottom: 15px;
}

.search-input {
  max-width: 300px;
}

.tree-container {
  margin-top: 10px;
  max-height: 700px;
  overflow-y: auto;
  border-radius: 8px;
  background-color: #fafafa;
  padding: 10px;
  min-height: 400px;
}

.menu-tree {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background-color: #ffffff;
  transition: all 0.3s ease;
  padding: 10px;
}

.menu-tree:hover {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.tree-node-content {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
  flex: 1;
}

.tree-node-content:hover {
  background-color: #ecf5ff;
}

.tree-node-content:hover .node-actions {
  opacity: 1;
}

.node-icon {
  font-size: 16px;
  transition: all 0.2s ease;
  color: #409eff;
}

.node-text {
  flex: 1;
  font-size: 14px;
  color: #303133;
  transition: all 0.2s ease;
}

.node-text-disabled {
  color: #909399;
  text-decoration: line-through;
}

.node-type {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  transition: all 0.2s ease;
}

.type-directory {
  background-color: #ecf5ff;
  color: #409eff;
}

.type-menu {
  background-color: #f0f9eb;
  color: #67c23a;
}

.type-button {
  background-color: #fdf6ec;
  color: #e6a23c;
}

.node-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  background-color: #f5f7fa;
  color: #909399;
  transition: all 0.2s ease;
}

.node-actions {
  opacity: 0;
  transition: opacity 0.2s ease;
  display: flex;
  gap: 5px;
}

.node-actions .el-button {
  padding: 5px;
}

.node-actions .delete-btn {
  color: #f56c6c;
}

.node-actions .delete-btn:hover {
  color: #f56c6c;
  background-color: #fef0f0;
}

.empty-container {
  margin: 60px 0;
  text-align: center;
}

.dialog-footer {
  text-align: right;
}

/* 滚动条样式 */
.tree-container::-webkit-scrollbar {
  width: 8px;
}

.tree-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.tree-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.tree-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>