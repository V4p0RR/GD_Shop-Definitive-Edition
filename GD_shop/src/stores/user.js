import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userinfo: {},
  }),
  actions: {
    setUserinfo(info) {
      this.userinfo = info || {}
    },
    clear() {
      this.userinfo = {}
    },
  },
})

export default useUserStore
