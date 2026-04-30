import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const token = ref(localStorage.getItem('token') || '')

  async function login(phone, code, password) {
    const res = await request.post('/user/login', { phone, code, password })
    token.value = res.data
    localStorage.setItem('token', res.data)
    await fetchUser()
    return res
  }

  async function fetchUser() {
    const res = await request.get('/user/me')
    user.value = res.data
    localStorage.setItem('user', JSON.stringify(res.data))
  }

  async function logout() {
    try { await request.post('/user/logout') } catch (e) { /* ignore */ }
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  function isAdmin() {
    return user.value && user.value.role === 1
  }

  function isLogin() {
    return !!token.value && !!user.value
  }

  return { user, token, login, fetchUser, logout, isAdmin, isLogin }
})
