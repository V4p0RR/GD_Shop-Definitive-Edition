import http from './http.js'
import { apiGenerator } from '../../utils/generators/apiGenerator.js'

// 使用案例
// import api from '@/request/api/api';

// const handleLogoClick = () => {
//     // 测试接口
//     api.testPost({
//       name: 'test',
//       age: 18
//     }).then(res => {
//       console.log(res);
//     });
//   }

// 注册新的模型API示例
// apiGenerator.registerModel('article', '/articles', {
//   // 文章特殊接口
//   publishArticle: (id) => http.post(`/articles/${id}/publish`),
//   getArticleComments: (id) => http.get(`/articles/${id}/comments`),
//   addArticleComment: (id, data) => http.post(`/articles/${id}/comments`, data),
// })

// -----------------------------
// 注册模型与自定义接口
// -----------------------------


// 工具：查看所有已注册的接口（按模型分组返回方法名列表）
const listRegisteredApis = () => {
  const models = apiGenerator.getAllApis();
  const summary = {};
  for (const [modelName, apiObj] of Object.entries(models)) {
    summary[modelName] = Object.keys(apiObj);
  }
  return summary;
};


// -----------------------------
// 导出统一 API
// -----------------------------
export default {
  // 账户与认证
  getCaptcha: (params) => http.get('/accounts/captcha/', params),
  login: (data) => http.post('/accounts/token/', data),
  refreshToken: (data) => http.post('/accounts/token/refresh/', data),
  verifyToken: (data) => http.post('/accounts/token/verify/', data),



  // 使用案例
  // ...apiGenerator.getModelApi('article'),

  // 工具方法
  listRegisteredApis,
}