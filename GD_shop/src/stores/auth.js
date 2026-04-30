import { defineStore } from 'pinia'

const AUTH_PERSIST_KEY = 'auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: '',
    username: '',
    role: 0,
  }),

  getters: {
    isAdmin: (state) => state.role === 1,
  },

  actions: {
    setToken(token) {
      this.token = token || ''
    },
    setUsername(name) {
      this.username = name || ''
    },
    setRole(role) {
      this.role = role
    },
    setAuth({ token, username, role }) {
      if (token) this.token = token
      if (username) this.username = username
      if (role !== undefined) this.role = role
    },
    clear() {
      this.token = ''
      this.username = ''
      this.role = 0
    },
  },

  persist: {
    key: AUTH_PERSIST_KEY,
    paths: ['token', 'username', 'role'],
  },
})

export default useAuthStore
