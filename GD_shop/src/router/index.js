import { createRouter, createWebHistory } from 'vue-router'
import { createRoutesGenerator } from '../utils/generators/routerGenerator.js'
import AdminLayout from '../components/admin-layout/AdminLayout.vue'
import WebsiteLayout from '../components/layout/DefaultLayout.vue'
import { useAuthStore } from '../stores/auth.js'

const pages = import.meta.glob('../views/**/page.js', {
  eager: true,
  import: 'default',
})

const components = import.meta.glob('../views/**/index.vue')

const generateRoutes = createRoutesGenerator({
  pages,
  components,
  basePath: '../views',
})

const routes = generateRoutes({
  layoutComponents: {
    'admin': AdminLayout,
    'default': WebsiteLayout,
  },
})

const customRoutes = [
  {
    path: '',
    redirect: '/home',
  },
]

const router = createRouter({
  history: createWebHistory(''),
  routes: [...routes, ...customRoutes],
})

function isLoggedIn() {
  try {
    const authStore = useAuthStore()
    return !!(authStore.token && authStore.token.trim() !== '')
  } catch (error) {
    return false
  }
}

function isAdminOrOperator() {
  try {
    const authStore = useAuthStore()
    return authStore.role === 1 || authStore.role === 2
  } catch (error) {
    return false
  }
}

router.beforeEach(async (to, from, next) => {
  const loggedIn = isLoggedIn()

  if (loggedIn && to.path === '/login') {
    next('/admin')
    return
  }

  if (to.meta.requiresAuth !== false && !loggedIn) {
    next({
      path: '/login',
      query: { redirect: to.fullPath },
    })
    return
  }

  // Admin route protection: check role >= 1
  if (to.meta.layout === 'admin' && loggedIn && !isAdminOrOperator()) {
    next('/home')
    return
  }

  next()
})

export default router
