<template>
  <header class="website-header-wrapper">
    <div class="header-container">
      <!-- Logo区域 -->
      <div class="header-logo-section">
        <div class="logo-container" @click="handleLogoClick">
          <div class="logo-icon">
            <img
              src="../../../public/guizhou_university_emblem_fixed.png"
              alt="贵大电商"
              class="logo-image"
            />
            <div class="logo-glow"></div>
          </div>
          <div class="logo-text">
            <span class="brand-name">贵大</span>
            <span class="brand-subtitle">电商</span>
          </div>
        </div>
      </div>

      <!-- 导航菜单 -->
      <nav class="header-navigation">
        <t-head-menu
          :value="activeMenu"
          @change="handleMenuChange"
          class="header-menu"
          theme="light"
          :expanded="['features']"
        >
          <t-menu-item
            v-for="item in menuItems"
            :key="item.value"
            :value="item.value"
            :to="item.path"
          >
            <template #icon>
              <t-icon :name="item.icon" />
            </template>
            {{ item.label }}
          </t-menu-item>
        </t-head-menu>
      </nav>

      <!-- 操作区域 -->
      <div class="header-actions">
        <!-- 搜索按钮 -->
        <!-- <div class="search-container">
          <button class="search-btn" @click="toggleSearch">
            <t-icon name="search" class="search-icon" />
          </button>
          <div class="search-overlay" :class="{ active: isSearchActive }">
            <div class="search-input-wrapper">
              <t-input
                v-model="searchQuery"
                placeholder="搜索内容..."
                class="search-input"
                @keyup.enter="handleSearch"
              >
                <template #prefix-icon>
                  <t-icon name="search" />
                </template>
              </t-input>
            </div>
            <button class="search-close" @click="closeSearch">
              <t-icon name="close" />
            </button>
          </div>
        </div> -->

        <!-- 主题切换 -->
        <ThemeToggle class="theme-toggle" />

        <!-- 认证按钮 -->
        <!-- <div class="auth-buttons">
          <t-button
            theme="default"
            variant="outline"
            class="login-btn"
            @click="handleLogin"
          >
            登录
          </t-button>
        </div> -->

        <!-- 移动端菜单按钮 -->
        <button class="mobile-menu-btn" @click="toggleMobileMenu">
          <div class="hamburger" :class="{ active: isMobileMenuOpen }">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </button>
      </div>
    </div>

    <!-- 移动端菜单 -->
    <div class="mobile-menu" :class="{ active: isMobileMenuOpen }">
      <div class="mobile-menu-content">
        <t-menu
          :value="activeMenu"
          @change="handleMobileMenuChange"
          class="mobile-header-menu"
          theme="light"
          layout="vertical"
        >
          <t-menu-item
            v-for="item in menuItems"
            :key="item.value"
            :value="item.value"
          >
            <template #icon>
              <t-icon :name="item.icon" />
            </template>
            {{ item.label }}
          </t-menu-item>
        </t-menu>
        <!-- <div class="mobile-auth-buttons">
          <t-button theme="default" variant="outline" block>登录</t-button>
          <t-button theme="primary" block>立即注册</t-button>
        </div> -->
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import ThemeToggle from "./ThemeToggle.vue";
import { useRouter } from "vue-router";

// 响应式数据
const activeMenu = ref("home");
const isSearchActive = ref(false);
const isMobileMenuOpen = ref(false);
const searchQuery = ref("");
const router = useRouter();
// 菜单项配置
const menuItems = [
  { value: "home", label: "首页", icon: "home", path: "/" },
  { value: "shop", label: "店铺管理", icon: "star", path: "/shop" },
  {
    value: "sell_intro",
    label: "营销活动",
    icon: "money",
    path: "/sell_intro",
  },
  { value: "about", label: "店铺数据", icon: "help-circle", path: "/about" },
  { value: "user_intro", label: "账号管理", icon: "user", path: "/user_intro" },
];

// 事件处理
const handleLogoClick = () => {
  router.push("/");
};

const handleMenuChange = (value) => {
  activeMenu.value = value;
};

const handleMobileMenuChange = (value) => {
  activeMenu.value = value;
  isMobileMenuOpen.value = false;
};

const toggleSearch = () => {
  isSearchActive.value = !isSearchActive.value;
  if (isSearchActive.value) {
    setTimeout(() => {
      document.querySelector(".search-input input")?.focus();
    }, 100);
  }
};

const closeSearch = () => {
  isSearchActive.value = false;
  searchQuery.value = "";
};

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    console.log("搜索:", searchQuery.value);
    closeSearch();
  }
};

const handleLogin = () => {
  console.log("登录");
  router.push("/login");
};

const handleRegister = () => {
  console.log("注册");
  router.push("/register");
};

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
  document.body.style.overflow = isMobileMenuOpen.value ? "hidden" : "";
};

// 滚动监听
const handleScroll = () => {
  const header = document.querySelector(".website-header-wrapper");
  if (window.scrollY > 50) {
    header?.classList.add("scrolled");
  } else {
    header?.classList.remove("scrolled");
  }
};

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
  document.body.style.overflow = "";
});
</script>

<style scoped>
.website-header-wrapper {
  width: 100%;
  height: 80px;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
  background-color: var(--td-bg-color-container);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(var(--td-brand-color-rgb), 0.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.header-container {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

/* Logo 区域 */
.header-logo-section {
  flex-shrink: 0;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.logo-container:hover {
  transform: translateY(-2px);
  background: rgba(var(--td-brand-color-rgb), 0.05);
}

.logo-icon {
  position: relative;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  /* background: linear-gradient(
    135deg,
    var(--td-brand-color) 0%,
    var(--td-brand-color-hover) 100%
  ); */
  background: white;
  box-shadow: 0 4px 12px rgba(var(--td-brand-color-rgb), 0.3);
}

.logo-image {
  width: 24px;
  height: 24px;
  border-radius: 6px;
  z-index: 2;
  position: relative;
}

.logo-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 10px;
  background: linear-gradient(
    135deg,
    rgba(var(--td-brand-color-rgb), 0.3) 0%,
    transparent 50%
  );
  animation: glow 2s ease-in-out infinite alternate;
}

@keyframes glow {
  from {
    opacity: 0.5;
  }

  to {
    opacity: 1;
  }
}

.logo-text {
  display: flex;
  flex-direction: column;
  line-height: 1;
}

.brand-name {
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(
    135deg,
    var(--td-brand-color) 0%,
    var(--td-brand-color-hover) 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: red;
}

.brand-subtitle {
  font-size: 14px;
  font-weight: 500;
  color: red;
  margin-top: 0px;
}

/* 导航菜单 */
.header-navigation {
  flex: 1;
  display: flex;
  justify-content: center;
  margin: 0 48px;
}

.header-menu {
  background: transparent;
  border: none;
}

.header-menu :deep(.t-menu) {
  background: transparent;
  border: none;
}

.header-menu :deep(.t-menu__item) {
  border-radius: 12px;
  margin: 0 4px;
  font-weight: 500;
  font-size: 15px;
  transition: all 0.3s ease;
}

.header-menu :deep(.t-menu__item:hover) {
  background: rgba(var(--td-brand-color-rgb), 0.05);
  color: var(--td-brand-color);
  transform: translateY(-1px);
}

.header-menu :deep(.t-menu__item--active) {
  background: rgba(var(--td-brand-color-rgb), 0.08);
  color: var(--td-brand-color);
}

.header-menu :deep(.t-menu__item .t-icon) {
  margin-right: 8px;
  font-size: 16px;
}

/* 操作区域 */
.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 搜索功能 */
.search-container {
  position: relative;
}

.search-btn {
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 12px;
  background-color: var(--td-bg-color-page);
  color: var(--td-text-color-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.search-btn:hover {
  background: var(--td-brand-color-hover-light);
  color: var(--td-brand-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.search-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--td-mask-active);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
}

.search-overlay.active {
  opacity: 1;
  visibility: visible;
}

.search-input-wrapper {
  width: 90%;
  max-width: 600px;
  position: relative;
  transform: translateY(-20px);
  transition: transform 0.3s ease;
}

.search-overlay.active .search-input-wrapper {
  transform: translateY(0);
}

.search-input {
  width: 100%;
}

.search-input :deep(.t-input) {
  background-color: var(--td-bg-color-container);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 2px solid transparent;
}

.search-input :deep(.t-input:focus) {
  border-color: var(--td-brand-color);
  box-shadow: 0 8px 32px rgba(var(--td-brand-color-rgb), 0.2);
}

.search-close {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 8px;
  background-color: var(--td-bg-color-page);
  color: var(--td-text-color-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.search-close:hover {
  background: var(--td-error-color-light);
  color: var(--td-error-color);
}

/* 主题切换 */
.theme-toggle {
  margin: 0 8px;
}

/* 认证按钮 */
.auth-buttons {
  display: flex;
  gap: 12px;
}

.login-btn {
  border-radius: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.register-btn {
  border-radius: 12px;
  font-weight: 500;
  padding: 0 20px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(var(--td-brand-color-rgb), 0.3);
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(var(--td-brand-color-rgb), 0.4);
}

.register-btn :deep(.t-icon) {
  margin-left: 8px;
  transition: transform 0.3s ease;
}

.register-btn:hover :deep(.t-icon) {
  transform: translateX(4px);
}

/* 移动端菜单按钮 */
.mobile-menu-btn {
  display: none;
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 12px;
  background-color: var(--td-bg-color-page);
  cursor: pointer;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.hamburger {
  width: 20px;
  height: 16px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.hamburger span {
  width: 100%;
  height: 2px;
  background: var(--td-text-color-primary);
  border-radius: 2px;
  transition: all 0.3s ease;
  transform-origin: center;
}

.hamburger.active span:nth-child(1) {
  transform: rotate(45deg) translate(6px, 6px);
}

.hamburger.active span:nth-child(2) {
  opacity: 0;
}

.hamburger.active span:nth-child(3) {
  transform: rotate(-45deg) translate(6px, -6px);
}

/* 移动端菜单 */
.mobile-menu {
  position: fixed;
  top: 80px;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--td-bg-color-container);
  z-index: 999;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  overflow-y: auto;
}

.mobile-menu.active {
  transform: translateX(0);
}

.mobile-menu-content {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.mobile-header-menu {
  background: transparent;
  border: none;
  margin-bottom: 24px;
}

.mobile-header-menu :deep(.t-menu) {
  background: transparent;
  border: none;
}

.mobile-header-menu :deep(.t-menu__item) {
  border-radius: 12px;
  margin: 4px 0;
  font-weight: 500;
  font-size: 16px;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.mobile-header-menu :deep(.t-menu__item:hover) {
  background: rgba(var(--td-brand-color-rgb), 0.05);
  border-color: rgba(var(--td-brand-color-rgb), 0.1);
}

.mobile-header-menu :deep(.t-menu__item--active) {
  background: rgba(var(--td-brand-color-rgb), 0.08);
  color: var(--td-brand-color);
  border-color: var(--td-brand-color);
}

.mobile-header-menu :deep(.t-menu__item .t-icon) {
  margin-right: 12px;
  font-size: 18px;
}

.mobile-auth-buttons {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .header-navigation {
    margin: 0 24px;
  }

  .header-menu :deep(.t-menu__item) {
    padding: 10px 16px;
    font-size: 14px;
  }
}

@media (max-width: 768px) {
  .website-header-wrapper {
    height: 70px;
  }

  .header-container {
    padding: 0 16px;
  }

  .header-navigation {
    display: none;
  }

  .auth-buttons {
    display: none;
  }

  .mobile-menu-btn {
    display: flex;
  }

  .brand-subtitle {
    display: none;
  }

  .logo-container {
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .header-container {
    padding: 0 12px;
  }

  .logo-icon {
    width: 36px;
    height: 36px;
  }

  .logo-image {
    width: 20px;
    height: 20px;
  }

  .brand-name {
    font-size: 20px;
  }
}

/* 滚动状态样式 */
.website-header-wrapper.scrolled {
  height: 70px;
  background-color: var(--td-bg-color-container);
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08), 0 2px 8px rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid rgba(var(--td-brand-color-rgb), 0.15);
}

/* 暗色主题适配 */
@media (prefers-color-scheme: dark) {
  .website-header-wrapper {
    background-color: var(--td-bg-color-container);
    border-bottom: 1px solid rgba(var(--td-brand-color-rgb), 0.2);
  }

  .website-header-wrapper.scrolled {
    background-color: var(--td-bg-color-container);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3), 0 2px 8px rgba(0, 0, 0, 0.2);
  }

  .search-overlay {
    background-color: var(--td-mask-active);
  }

  .search-btn {
    background-color: var(--td-bg-color-page);
    color: var(--td-text-color-primary);
  }

  .search-btn:hover {
    background: rgba(var(--td-brand-color-rgb), 0.2);
    color: var(--td-brand-color);
  }

  .mobile-menu-btn {
    background-color: var(--td-bg-color-page);
  }

  .mobile-menu {
    background-color: var(--td-bg-color-container);
    backdrop-filter: blur(20px);
  }

  .header-menu :deep(.t-menu__item) {
    color: var(--td-text-color-primary);
  }

  .header-menu :deep(.t-menu__item:hover) {
    background: rgba(var(--td-brand-color-rgb), 0.15);
  }

  .header-menu :deep(.t-menu__item--active) {
    background: rgba(var(--td-brand-color-rgb), 0.2);
  }

  .mobile-header-menu :deep(.t-menu__item) {
    color: var(--td-text-color-primary);
  }

  .mobile-header-menu :deep(.t-menu__item:hover) {
    background: rgba(var(--td-brand-color-rgb), 0.15);
  }

  .mobile-header-menu :deep(.t-menu__item--active) {
    background: rgba(var(--td-brand-color-rgb), 0.2);
  }

  .logo-container:hover {
    background: rgba(var(--td-brand-color-rgb), 0.15);
  }

  .brand-subtitle {
    color: var(--td-text-color-secondary);
  }

  .search-close {
    background-color: var(--td-bg-color-page);
    color: var(--td-text-color-secondary);
  }

  .search-close:hover {
    background: var(--td-error-color-light);
    color: var(--td-error-color);
  }

  .search-input :deep(.t-input) {
    background-color: var(--td-bg-color-container);
    color: var(--td-text-color-primary);
  }

  .search-input :deep(.t-input:focus) {
    border-color: var(--td-brand-color);
    box-shadow: 0 8px 32px rgba(var(--td-brand-color-rgb), 0.2);
  }
}
</style>
