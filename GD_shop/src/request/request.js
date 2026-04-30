import axios from 'axios'
import { MessagePlugin } from 'tdesign-vue-next'
import { useAuthStore } from '@/stores/auth'

const service = axios.create({
  baseURL: import.meta.env.VUE_APP_API_URL || '/api',
  timeout: 15 * 1000,
})

service.interceptors.request.use((config) => {
  if (!(config.data instanceof FormData)) {
    config.data = JSON.stringify(config.data)
    config.headers = {
      'Content-Type': 'application/json',
    }
  }
  const auth = useAuthStore()
  if (auth.token) {
    config.headers['authorization'] = auth.token
  }
  return config
}, (error) => {
  return Promise.reject(error)
})

service.interceptors.response.use((response) => {
  const body = response.data
  if (body && body.success === false) {
    MessagePlugin.error(body.errorMsg || '操作失败')
    return Promise.reject(new Error(body.errorMsg || '操作失败'))
  }
  return body
}, (error) => {
  if (error && error.response) {
    const statusCode = error.response.status
    switch (statusCode) {
      case 401:
        const auth = useAuthStore()
        auth.clear()
        window.location.href = '/login'
        break
      case 403:
        MessagePlugin.error('无权限访问')
        break
      case 500:
        MessagePlugin.error('服务器内部错误')
        break
      default:
        MessagePlugin.error(error.response.data?.errorMsg || '请求失败')
    }
  } else {
    MessagePlugin.error('连接服务器失败')
  }
  return Promise.reject(error)
})

export default service
