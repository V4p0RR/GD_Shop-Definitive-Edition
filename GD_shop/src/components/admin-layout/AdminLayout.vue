<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <TheSidebar ref="sidebarRef" class="sidebar" />
    
    <!-- 主内容区 -->
    <div class="layout-content">
      <!-- 顶部导航 -->
      <TheHeader class="header" />
      
      <!-- 页面内容 -->
      <div class="page-container">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
      
      <!-- 页脚 -->
      <!-- <TheFooter /> -->
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import TheSidebar from './TheAdminSidebar.vue';
import TheHeader from './TheAdminHeader.vue';
import TheFooter from './TheAdminFooter.vue';

// 侧边栏引用，可用于操作侧边栏组件
const sidebarRef = ref(null);
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  width: 100%;
  background-color: var(--td-bg-color-page);
  position: relative;
  overflow: hidden;
}

.sidebar {
  height: 100%;
  z-index: 100;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
}

.layout-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin-left: 240px;
  transition: margin 0.3s ease;
}

.header {
  position: sticky;
  top: 0;
  width: 100%;
  z-index: 99;
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.07);
}

.page-container {
  flex: 1;
  padding: 24px;
  overflow: auto;
  min-height: calc(100vh - 64px - 60px); /* 100vh减去头部高度和底部高度 */
  background-position: center;
  background-size: cover;
  border-radius: 16px 0 0 0;
  margin-top: 4px;
  box-shadow: inset 0 4px 10px rgba(0, 0, 0, 0.05);
  background-image: 
    radial-gradient(
      circle at 50% 50%, 
      rgba(var(--td-brand-color-rgb), 0.03),
      transparent 400px
    );
}



/* 页面切换过渡效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 侧边栏折叠时的样式调整 */
:deep(.sidebar-container.collapsed) + .layout-content {
  margin-left: 64px;
}

@media (max-width: 768px) {
  .layout-content {
    margin-left: 0;
  }
  
  .page-container {
    padding: 16px;
  }
  

}
</style>