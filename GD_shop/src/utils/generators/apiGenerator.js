import http from '../../request/api/http.js'

/**
 * API生成器工具类
 * 用于自动生成标准的增删改查接口，支持扩展特殊接口
 */
class ApiGenerator {
  constructor() {
    this.apis = {}
  }

  /**
   * 创建模型API
   * @param {string} name - API名称（如：user, product等）
   * @param {string} baseUrl - 基础路由（如：/users, /products等）
   * @param {Object} customApis - 自定义接口配置
   * @returns {Object} 生成的API对象
   */ 
  createModelApi(name, baseUrl, customApis = {}) {
    const api = {
      // 获取列表
      [`get${this.capitalize(name)}List`]: (params) => http.get(baseUrl, params),
      
      // 获取单个详情
      [`get${this.capitalize(name)}Detail`]: (id, params) => http.get(`${baseUrl}${id}/`, params),
      
      // 创建
      [`create${this.capitalize(name)}`]: (data) => http.post(baseUrl, data),
      
      // 更新
      [`update${this.capitalize(name)}`]: (id, data) => http.put(`${baseUrl}${id}/`, data),
      
      // 删除
      [`delete${this.capitalize(name)}`]: (id) => http.delete(`${baseUrl}${id}/`),
      
      
    }

    // 合并自定义接口
    Object.assign(api, customApis)

    return api
  }

  /**
   * 首字母大写
   * @param {string} str 
   * @returns {string}
   */
  capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1)
  }

  /**
   * 注册模型API
   * @param {string} name - API名称
   * @param {string} baseUrl - 基础路由
   * @param {Object} customApis - 自定义接口
   */
  registerModel(name, baseUrl, customApis = {}) {
    this.apis[name] = this.createModelApi(name, baseUrl, customApis)
  }

  /**
   * 获取所有API
   * @returns {Object}
   */
  getAllApis() {
    return this.apis
  }

  /**
   * 获取特定模型的API
   * @param {string} name 
   * @returns {Object}
   */
  getModelApi(name) {
    return this.apis[name] || {}
  }
}

// 创建API生成器实例
const apiGenerator = new ApiGenerator()

// 预定义一些常用的模型API
// apiGenerator.registerModel('user', '/users', {
//   // 用户特殊接口
//   getUserProfile: (id) => http.get(`/users/${id}/profile`),
//   updateUserAvatar: (id, data) => http.post(`/users/${id}/avatar`, data),
//   resetUserPassword: (id, data) => http.post(`/users/${id}/reset-password`, data),
// })


// 导出API生成器和预定义的API
export { apiGenerator }

// 导出所有API
// export default {

// } 