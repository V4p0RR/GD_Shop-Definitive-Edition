import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'login', component: () => import('../views/login/index.vue'), meta: { guest: true } },
  { path: '/', name: 'home', component: () => import('../views/home/index.vue') },
  { path: '/product/:id', name: 'product', component: () => import('../views/product/index.vue') },
  { path: '/cart', name: 'cart', component: () => import('../views/cart/index.vue') },
  { path: '/seckill', name: 'seckill', component: () => import('../views/seckill/index.vue') },
  { path: '/order/list', name: 'orders', component: () => import('../views/order/list.vue'), meta: { requiresAuth: true } },
  { path: '/order/:id', name: 'orderDetail', component: () => import('../views/order/detail.vue'), meta: { requiresAuth: true } },
  { path: '/profile', name: 'profile', component: () => import('../views/profile/index.vue'), meta: { requiresAuth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    return next('/login')
  }
  next()
})

export default router
