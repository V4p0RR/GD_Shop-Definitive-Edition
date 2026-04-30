<template>
  <div class="theme-toggle">
    <t-tooltip :content="isDarkMode ? '切换亮色模式' : '切换暗色模式'">
      <div class="theme-switch" @click="handleThemeChange">
        <div class="icon-container">
          <t-icon v-if="isDarkMode" name="moon" class="theme-icon moon" />
          <t-icon v-else name="sunny" class="theme-icon sun" />
        </div> 
        <!-- 设置不能选择 -->
        <span class="theme-text" v-if="showText" :class="{ 'dark-text': isDarkMode }" style="user-select: none;">
          {{ isDarkMode ? '暗色' : '亮色' }}
        </span>
      </div>
    </t-tooltip>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { getThemeMode, setThemeMode, toggleThemeMode, THEME_MODE } from '@/utils/theme';

// 是否为暗色模式
const isDarkMode = ref(false);

// 是否显示文本（在较大屏幕上显示）
const showText = computed(() => {
  return window.innerWidth > 768;
});

// 处理主题切换
const handleThemeChange = () => {
  isDarkMode.value = !isDarkMode.value;
  const mode = isDarkMode.value ? THEME_MODE.DARK : THEME_MODE.LIGHT;
  setThemeMode(mode);
};

// 组件挂载时，初始化主题模式
onMounted(() => {
  const currentMode = getThemeMode();
  isDarkMode.value = currentMode === THEME_MODE.DARK;
  
  // 监听窗口大小变化以更新showText值
  window.addEventListener('resize', () => {
    // 此行不实际执行任何操作，但会触发showText的重新计算
    showText.value;
  });
});
</script>

<style scoped>
.theme-toggle {
  display: inline-flex;
  align-items: center;
}
.dark-text {
  color: #ffffff;
}

.theme-switch {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: var(--td-bg-color-page);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.theme-switch:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
}

.icon-container {
  width: 20px;
  height: 20px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-icon {
  transition: all 0.5s;
  position: absolute;
}

.theme-icon.sun {
  color: #ffa940;
  animation: rotate 10s linear infinite;
}

.theme-icon.moon {
  color: #9254de;
  animation: pulse 4s ease-in-out infinite;
}

.theme-text {
  margin-left: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@media (max-width: 768px) {
  .theme-switch {
    padding: 8px;
  }
}
</style> 