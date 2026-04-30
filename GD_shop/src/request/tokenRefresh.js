// Token自动刷新管理
import { MessagePlugin } from 'tdesign-vue-next';
import { useAuthStore } from '@/stores/auth';

// Token配置
const TOKEN_KEY = 'access_token'; // 存储的token字段
const REFRESH_TOKEN_KEY = 'refresh_token'; // 存储的刷新token字段
const TOKEN_HEADER = 'Authorization';
const TOKEN_PREFIX = 'Bearer ';
const RESPONSE_TOKEN_FIELD = 'access'; // 响应的token字段
const RESPONSE_REFRESH_TOKEN_FIELD = 'refresh'; // 响应的刷新token字段


const REFRESH_TOKEN_URL = '/accounts/token/refresh/';
// 新增：登录获取token接口 & 需要排除刷新逻辑的鉴权接口
const LOGIN_TOKEN_URL = '/accounts/token/';
const AUTH_EXCLUDE_URLS = [
  REFRESH_TOKEN_URL,
  LOGIN_TOKEN_URL,
  '/accounts/login/',
  '/accounts/register/',
  '/accounts/password/reset/',
];

// 刷新token接口的请求变量名称
const REFRESH_TOKEN_REQUEST_TOKEN_FIELD = 'refresh';

// 新增：判断某个请求是否属于需要排除自动刷新的鉴权接口
const isAuthExcluded = (url) => {
  try {
    const a = document.createElement('a');
    a.href = url;
    const path = a.pathname || url;
    return AUTH_EXCLUDE_URLS.some((ex) => path.endsWith(ex));
  } catch (_) {
    return AUTH_EXCLUDE_URLS.some((ex) => url.includes(ex));
  }
};

// 刷新状态管理
let isRefreshing = false;
let failedQueue = [];

// 处理队列中的请求
const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

// 清除所有token（统一通过 Pinia store）
const clearTokens = () => {
  try {
    const auth = useAuthStore();
    auth.clear();
  } catch (_) {
    // 兜底清理
    try { localStorage.removeItem(TOKEN_KEY); } catch (_) {}
    try { localStorage.removeItem(REFRESH_TOKEN_KEY); } catch (_) {}
  }
};

// 跳转登录页
const redirectToLogin = () => {
  window.location.href = '/login';
};

// 自动刷新token的核心逻辑
export const handleTokenRefresh = (error, service) => {
  const originalRequest = error?.config || {};

  // 如果没有原始请求信息，或该请求属于鉴权接口（如登录、刷新、注册等），不进行自动刷新
  if (!originalRequest.url) {
    
    return Promise.reject(new Error(error?.response?.data?.message || '请求未授权'));
  }

  // 刷新token接口失败：清除并报错
  if (originalRequest.url === REFRESH_TOKEN_URL) {
    clearTokens();
    return Promise.reject(new Error('登录已过期，请重新登录'));
  }

  // 登录/注册等鉴权接口的401，不应触发刷新逻辑，直接透传错误
  if (isAuthExcluded(originalRequest.url)) {

    return Promise.reject(new Error(error?.response?.data?.message || '请求未授权'));
  }

  // 避免同一请求重复触发刷新逻辑
  if (originalRequest._retry) {
    return Promise.reject(error);
  }

  // 如果正在刷新token，将请求加入队列
  if (isRefreshing) {
    return new Promise((resolve, reject) => {
      failedQueue.push({ resolve, reject });
    })
      .then((token) => {
        originalRequest.headers[TOKEN_HEADER] = TOKEN_PREFIX + token;
        return service(originalRequest);
      })
      .catch((err) => {
        return Promise.reject(err);
      });
  }

  // 开始刷新token
  originalRequest._retry = true;
  isRefreshing = true;

  // 从 Pinia store 读取 refresh token
  const auth = useAuthStore();
  const refreshToken = auth.refresh || localStorage.getItem(REFRESH_TOKEN_KEY);
  if (!refreshToken) {
    clearTokens();
    return Promise.reject(new Error('登录已过期，请重新登录'));
  }

  // 调用刷新token接口
  return service({
    method: 'post',
    url: REFRESH_TOKEN_URL,
    data: { [REFRESH_TOKEN_REQUEST_TOKEN_FIELD]: refreshToken },
  })
    .then((response) => {
      // 刷新成功，保存新的token
      try {
        const auth = useAuthStore();
        if (response[RESPONSE_TOKEN_FIELD]) {
          auth.setTokens({ access: response[RESPONSE_TOKEN_FIELD] });
        }
        if (response[RESPONSE_REFRESH_TOKEN_FIELD]) {
          auth.setTokens({ refresh: response[RESPONSE_REFRESH_TOKEN_FIELD] });
        }
      } catch (_) {
        // 兜底写回本地，防止极端情况下丢失
        try { if (response[RESPONSE_TOKEN_FIELD]) localStorage.setItem(TOKEN_KEY, response[RESPONSE_TOKEN_FIELD]); } catch (_) {}
        try { if (response[RESPONSE_REFRESH_TOKEN_FIELD]) localStorage.setItem(REFRESH_TOKEN_KEY, response[RESPONSE_REFRESH_TOKEN_FIELD]); } catch (_) {}
      }

      // 更新原请求的token
      originalRequest.headers[TOKEN_HEADER] = TOKEN_PREFIX + response[RESPONSE_TOKEN_FIELD];

      // 处理队列中的请求
      processQueue(null, response[RESPONSE_TOKEN_FIELD]);

      // 重新发送原请求
      return service(originalRequest);
    })
    .catch((refreshError) => {
      // 刷新token失败，清除所有token
      processQueue(refreshError, null);
      clearTokens();
      return Promise.reject(new Error('登录已过期，请重新登录'));
    })
    .finally(() => {
      isRefreshing = false;
    });
};

// 导出配置常量，供其他地方使用
export const TOKEN_CONFIG = {
  TOKEN_KEY,
  REFRESH_TOKEN_KEY,
  TOKEN_HEADER,
  TOKEN_PREFIX,
  RESPONSE_TOKEN_FIELD,
  RESPONSE_REFRESH_TOKEN_FIELD,
};