import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/dashboard'
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('../views/dashboard/Index.vue'),
      meta: {
        title: '仪表盘'
      }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
      meta: {
        title: '登录'
      }
    },
    {
      path: '/menu',
      name: 'menu',
      component: () => import('../views/menu/Menu.vue'),
      meta: {
        title: '菜单管理'
      },
      children: [
        {
          path: 'tree',
          name: 'menuTree',
          component: () => import('../views/menu/MenuTree.vue'),
          meta: {
            title: '菜单树形结构'
          }
        },
        {
          path: 'children',
          name: 'menuChildren',
          component: () => import('../views/menu/MenuChildren.vue'),
          meta: {
            title: '子菜单查询'
          }
        }
      ]
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('../views/user/User.vue'),
      meta: {
        title: '用户管理'
      },
      children: [
        {
          path: 'list',
          name: 'userList',
          component: () => import('../views/user/UserList.vue'),
          meta: {
            title: '用户列表'
          }
        }
      ]
    },
    {
      path: '/role',
      name: 'role',
      component: () => import('../views/role/Role.vue'),
      meta: {
        title: '角色管理'
      },
      children: [
        {
          path: 'list',
          name: 'roleList',
          component: () => import('../views/role/RoleList.vue'),
          meta: {
            title: '角色列表'
          }
        },
        {
          path: 'page',
          name: 'rolePage',
          component: () => import('../views/role/RolePage.vue'),
          meta: {
            title: '角色分页'
          }
        }
      ]
    },
    {
      path: '/sensitive',
      name: 'sensitive',
      component: () => import('../views/sensitive/Sensitive.vue'),
      meta: {
        title: '敏感数据管理'
      },
      children: [
        {
          path: 'contact',
          name: 'sensitiveContact',
          component: () => import('../views/sensitive/ContactInfo.vue'),
          meta: {
            title: '用户联系信息'
          }
        },
        {
          path: 'test',
          name: 'sensitiveTest',
          component: () => import('../views/sensitive/TestContactInfo.vue'),
          meta: {
            title: '测试联系信息'
          }
        }
      ]
    }
  ]
})

// 路由前置守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title || '系统管理平台'
  
  // 检查是否需要登录
  const token = localStorage.getItem('token')
  if (to.name !== 'login' && !token) {
    // 未登录，跳转到登录页面
    next('/login')
  } else {
    next()
  }
})

export default router