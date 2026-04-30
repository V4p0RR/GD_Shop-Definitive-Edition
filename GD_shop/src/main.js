import { createApp } from 'vue';
import App from './App.vue';
import router from "./router/index.js";
import {createPinia} from "pinia"
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import TDesign from 'tdesign-vue-next';
// import TDesignChat from '@tdesign-vue-next/chat'; // 引入chat组件
// 引入组件库的少量全局样式变量
import 'tdesign-vue-next/es/style/index.css';
// 引入自定义样式
import './style/theme.css';

// 导入主题工具，初始化主题
import { getThemeMode, setThemeMode } from './utils/theme';

// 在应用启动前初始化主题模式
const initTheme = () => {
  const currentTheme = getThemeMode();
  setThemeMode(currentTheme); // 这将同时更新localStorage和HTML属性
};

// 立即调用初始化函数
initTheme();

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App);
app.use(router);
app.use(pinia)

app.use(TDesign);
// app.use(TDesignChat);

app.mount('#app')

