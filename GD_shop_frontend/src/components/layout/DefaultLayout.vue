<template>
  <div class="layout">
    <header class="header">
      <div class="container header-inner">
        <router-link to="/" class="logo">贵大电商</router-link>
        <div class="nav">
          <router-link to="/">首页</router-link>
          <router-link to="/seckill">秒杀</router-link>
          <router-link to="/cart">购物车</router-link>
          <template v-if="auth.isLogin()">
            <router-link to="/order/list">我的订单</router-link>
            <router-link to="/profile">{{ auth.user?.nickName }}</router-link>
            <a v-if="auth.isAdmin()" href="/admin" @click.prevent="goAdmin">管理后台</a>
            <a href="#" @click.prevent="handleLogout">退出</a>
          </template>
          <router-link v-else to="/login">登录</router-link>
        </div>
      </div>
    </header>
    <main class="main container"><slot /></main>
  </div>
</template>

<script setup>
import { useAuthStore } from '../../stores/auth'
import { useRouter } from 'vue-router'
const auth = useAuthStore()
const router = useRouter()

async function handleLogout() {
  await auth.logout()
  router.push('/login')
}

function goAdmin() {
  window.location.href = '/admin'
}
</script>

<style scoped>
.header { background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,0.08); position: sticky; top: 0; z-index: 100; }
.header-inner { display: flex; align-items: center; height: 56px; justify-content: space-between; }
.logo { font-size: 22px; font-weight: bold; color: #e4393c; }
.nav { display: flex; gap: 20px; align-items: center; }
.nav a { font-size: 14px; color: #666; }
.nav a:hover, .nav a.router-link-active { color: #e4393c; }
.main { min-height: calc(100vh - 56px); padding-top: 20px; padding-bottom: 40px; }
</style>
