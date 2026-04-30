import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.authorization = token
  }
  return config
}, error => Promise.reject(error))

request.interceptors.response.use(response => {
  const res = response.data
  if (res.success === false) {
    return Promise.reject(new Error(res.errorMsg || '请求失败'))
  }
  return res
}, error => {
  if (error.response && error.response.status === 401) {
    const token = localStorage.getItem('token')
    if (token) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
  }
  return Promise.reject(error)
})

export default request
