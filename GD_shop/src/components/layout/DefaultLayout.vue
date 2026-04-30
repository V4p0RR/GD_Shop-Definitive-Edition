<template>
  <div class="website-layout-container">
    <!-- 网站顶部导航栏 -->
    <TheWebsiteHeader class="website-header" />

    <!-- 主内容区 -->
    <main class="website-content">
      <router-view v-slot="{ Component }">
        <transition name="page-fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 网站页脚 -->
    <TheWebsiteFooter class="website-footer" />

    <!-- 回到顶部按钮 -->
    <t-back-top
      :visibility-height="300"
      :target="backTopTargetSelector"
      theme="primary"
      class="back-to-top"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import TheWebsiteHeader from "./TheWebsiteHeader.vue";
import TheWebsiteFooter from "./TheWebsiteFooter.vue";

// 回到顶部的目标元素
const backTopTargetSelector = ref("html");

// 组件挂载后设置回到顶部的目标
onMounted(() => {
  // t-back-top 接收 selector 字符串或返回元素的函数
  backTopTargetSelector.value = () => document.documentElement;
});
</script>

<style scoped>
.website-layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--td-bg-color-page); /* 基础底色，干净温柔 */
  position: relative;
  overflow-x: hidden;
}

/* 超级淡雅的背景装饰～像撒上最薄的星尘，轻轻托起整个页面 */
.website-layout-container::before {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: 
      /* 第一层：极淡的整体渐变，像晨雾般温柔 */ linear-gradient(
      135deg,
      rgba(var(--td-brand-color-rgb), 0.018) 0%,
      rgba(var(--td-brand-color-rgb), 0.008) 50%,
      transparent 100%
    ),
    /* 第二层：三颗超淡的径向星光，像夜空的三颗小星星 */
      radial-gradient(
        circle at 15% 85%,
        rgba(var(--td-brand-color-rgb), 0.04) 0%,
        transparent 40%
      ),
    radial-gradient(
      circle at 85% 15%,
      rgba(var(--td-brand-color-rgb), 0.03) 0%,
      transparent 40%
    ),
    radial-gradient(
      circle at 50% 50%,
      rgba(var(--td-brand-color-rgb), 0.02) 0%,
      transparent 50%
    );
  pointer-events: none;
  z-index: -1;
  opacity: 0.7; /* 整体再淡一点，几乎感觉不到却有呼吸感～ */
  animation: gentleBreath 30s ease-in-out infinite alternate; /* 可选：超慢呼吸动画，像云朵轻轻浮动 */
}

/* 超慢呼吸动画～让背景像活了一样，却一点都不打扰 */
@keyframes gentleBreath {
  0% {
    opacity: 0.6;
  }
  100% {
    opacity: 0.8;
  }
}

/* 暗色模式下背景更温柔～像深夜的星空 */
@media (prefers-color-scheme: dark) {
  .website-layout-container {
    background: var(--td-bg-color-page);
  }

  .website-layout-container::before {
    background: linear-gradient(
        135deg,
        rgba(var(--td-brand-color-rgb), 0.03) 0%,
        rgba(var(--td-brand-color-rgb), 0.015) 50%,
        transparent 100%
      ),
      radial-gradient(
        circle at 15% 85%,
        rgba(var(--td-brand-color-rgb), 0.06) 0%,
        transparent 40%
      ),
      radial-gradient(
        circle at 85% 15%,
        rgba(var(--td-brand-color-rgb), 0.05) 0%,
        transparent 40%
      ),
      radial-gradient(
        circle at 50% 50%,
        rgba(var(--td-brand-color-rgb), 0.03) 0%,
        transparent 50%
      );
    opacity: 0.5;
  }
}

/* 其余样式完全保留～超级和谐～（头部、内容、页脚、回到顶部全不变哦～） */
.website-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  width: 100%;
  backdrop-filter: blur(20px);
  background: var(--td-bg-color-container);
  border-bottom: 1px solid rgba(var(--td-brand-color-rgb), 0.1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05), 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.website-header.scrolled {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08), 0 2px 8px rgba(0, 0, 0, 0.06);
}

.website-content {
  flex: 1;
  width: 100%;
  margin: 0 auto;
  padding: 0 24px;
  position: relative;
}

.website-footer {
  margin-top: auto;
  position: relative;
  z-index: 10;
}

/* 页面切换动画（保留原来的温柔过渡～） */
.page-fade-enter-active,
.page-fade-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.page-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

/* 回到顶部按钮（保留原来的美美动画～） */
.back-to-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  z-index: 1000;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(var(--td-brand-color-rgb), 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12),
    0 4px 16px rgba(var(--td-brand-color-rgb), 0.1);
}

.back-to-top:hover {
  transform: translateY(-4px) scale(1.05);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15),
    0 8px 24px rgba(var(--td-brand-color-rgb), 0.2);
}

.back-to-top:active {
  transform: translateY(-2px) scale(1.02);
  transition: all 0.1s ease;
}

/* 响应式（全保留～） */
@media (max-width: 1200px) {
  .website-content {
    max-width: 100%;
    padding: 0 20px;
  }
}

@media (max-width: 768px) {
  .website-content {
    padding: 0 16px;
  }
  .website-header {
    backdrop-filter: blur(10px);
  }
  .back-to-top {
    bottom: 20px;
    right: 20px;
    transform: scale(0.9);
  }
  .back-to-top:hover {
    transform: translateY(-2px) scale(0.95);
  }
}

@media (max-width: 480px) {
  .website-content {
    padding: 0 12px;
  }
}

/* 滚动条美化（保留原来的优雅～） */
.website-layout-container::-webkit-scrollbar {
  width: 8px;
}

.website-layout-container::-webkit-scrollbar-track {
  background: transparent;
}

.website-layout-container::-webkit-scrollbar-thumb {
  background: linear-gradient(
    180deg,
    var(--td-brand-color) 0%,
    var(--td-brand-color-hover) 100%
  );
  border-radius: 4px;
}

.website-layout-container::-webkit-scrollbar-thumb:hover {
  background: var(--td-brand-color-hover);
}
</style>