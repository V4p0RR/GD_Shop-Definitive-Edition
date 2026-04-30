<template>
  <div class="sidebar-container" :class="{ collapsed: isCollapsed }">
    <!-- 顶部Logo区域 -->
    <div class="sidebar-logo">
      <div class="logo-image-container">
        <img
          src="../../../public/guizhou_university_emblem_fixed.png"
          alt="logo"
          class="logo-image"
        />
      </div>
      <span v-show="!isCollapsed" class="logo-text">贵大电商</span>
    </div>

    <!-- 导航菜单 -->
    <div class="menu-wrapper">
      <t-menu
        :value="activeMenu"
        :collapsed="isCollapsed"
        :width="isCollapsed ? '64px' : '240px'"
        theme="light"
        @change="handleMenuSelect"
      >
        <!-- 动态渲染分类和菜单 -->
        <template v-for="category in categories" :key="category">
          <!-- 分类标题（折叠时隐藏） -->
          <div class="menu-category" v-show="!isCollapsed">
            <span class="category-title">{{ category }}</span>
          </div>

          <!-- 该分类下的菜单项 -->
          <template v-for="item in getItemsByCategory(category)" :key="item.id">
            <!-- 有子菜单 -->
            <t-submenu
              v-if="item.children?.length"
              :value="item.id"
              :title="item.title"
            >
              <template #icon>
                <t-icon :name="item.icon" />
              </template>
              <t-menu-item
                v-for="child in item.children"
                :key="child.id"
                :value="child.id"
                @click="navigateTo(child.path)"
              >
                <template #icon>
                  <t-icon :name="child.icon" />
                </template>
                {{ child.title }}
              </t-menu-item>
            </t-submenu>

            <!-- 无子菜单 -->
            <t-menu-item v-else :value="item.id" @click="navigateTo(item.path)">
              <template #icon>
                <t-icon :name="item.icon" />
              </template>
              {{ item.title }}
            </t-menu-item>
          </template>

          <!-- 分类分隔线（折叠时隐藏） -->
          <div class="menu-divider" v-show="!isCollapsed"></div>
        </template>
      </t-menu>
    </div>

    <!-- 底部折叠按钮 -->
    <div class="collapse-btn-wrapper">
      <t-button
        block
        class="collapse-btn"
        variant="text"
        shape="square"
        @click="toggleCollapse"
      >
        <template #icon>
          <t-icon :name="isCollapsed ? 'chevron-right' : 'chevron-left'" />
        </template>
      </t-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from "vue-router";
import { ref, computed, watch, watchEffect } from "vue";
import { useAuthStore } from "@/stores/auth";
const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const isCollapsed = ref(false);
const activeMenu = ref("dashboard");

// ================== 菜单数据 ==================
const allMenuData = [
  {
    id: "dashboard",
    title: "控制台",
    icon: "dashboard",
    path: "/admin",
    category: "主导航",
    order: 1,
  },
  {
    id: "shop-data",
    title: "店铺数据",
    icon: "chart-analytics",
    path: "/shop_data_overview",
    category: "主导航",
    order: 2,
  },
  {
    id: "order",
    title: "订单管理",
    icon: "article-filled",
    path: "/order_list",
    category: "业务管理",
    order: 3,
  },
  {
    id: "product",
    title: "商品管理",
    icon: "shop",
    path: "/product",
    category: "业务管理",
    order: 5,
  },
  {
    id: "sell",
    title: "营销活动",
    icon: "shop",
    path: "/sell",
    category: "业务管理",
    order: 6,
  },
  {
    id: "user",
    title: "用户管理",
    icon: "user",
    path: "/user_list",
    category: "系统管理",
    order: 7,
    requiresSuperAdmin: true,
  },
  {
    id: "system",
    title: "系统设置",
    icon: "setting",
    path: "/system",
    category: "系统管理",
    order: 8,
    children: [
      {
        id: "system-config",
        title: "系统配置",
        icon: "tools",
        path: "/system_config",
      },
      {
        id: "system-log",
        title: "操作日志",
        icon: "history",
        path: "/system_log",
      },
    ],
  },
];

const menuData = computed(() => {
  if (authStore.role === 1) return allMenuData;
  // 运营看不到用户管理
  return allMenuData.filter(item => !item.requiresSuperAdmin);
});

// ================== 计算属性 ==================
const categories = computed(() => {
  const set = new Set(
    menuData.value.map((item) => item.category).filter(Boolean)
  );
  return Array.from(set);
});

const getItemsByCategory = (category) => {
  return menuData.value
    .filter((item) => item.category === category)
    .sort((a, b) => a.order - b.order);
};

// ================== 方法 ==================
const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value;
};

const navigateTo = (path) => {
  if (path && route.path !== path) {
    router.push(path);
  }
};

const handleMenuSelect = (val) => {
  activeMenu.value = val;
  // 找到对应 path 并跳转（子菜单已在 @click 处理）
  const item = menuData.value.find((i) => i.id === val);
  if (item && !item.children?.length && item.path) {
    navigateTo(item.path);
  }
};

// 根据当前路由高亮菜单
const updateActiveFromRoute = () => {
  const currentPath = route.path;
  let foundId = null;

  for (const item of menuData.value) {
    if (item.path === currentPath) {
      foundId = item.id;
      break;
    }
    if (item.children) {
      const child = item.children.find((c) => c.path === currentPath);
      if (child) {
        foundId = child.id;
        break;
      }
    }
  }

  if (foundId) {
    activeMenu.value = foundId;
  } else if (currentPath === "/dashboard" || currentPath === "/") {
    activeMenu.value = "dashboard";
  }
};

// 初始化 + 监听路由变化
updateActiveFromRoute();
watchEffect(() => {
  updateActiveFromRoute();
});

// ================== expose（如果父组件需要） ==================
defineExpose({
  menuData,
  isCollapsed,
  toggleCollapse,
});
</script>
<style scoped>
.sidebar-container {
  height: 100vh;
  background-color: var(--td-bg-color-container);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  width: 240px;
  overflow: hidden;
  position: relative;
}

.sidebar-container.collapsed {
  width: 64px;
}

.sidebar-logo {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  margin-bottom: 12px;
  color: var(--td-brand-color);
  cursor: pointer;
  border-bottom: 1px solid var(--td-component-stroke);
  position: relative;
}

.sidebar-logo::after {
  content: "";
  position: absolute;
  bottom: -1px;
  left: 20px;
  right: 20px;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    var(--td-brand-color-hover),
    transparent
  );
  opacity: 0.6;
}

.logo-image-container {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.logo-image {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
  transition: all 0.3s;
}

.logo-text {
  margin-left: 12px;
  font-size: 20px;
  font-weight: 600;
  white-space: nowrap;
  background-image: linear-gradient(
    45deg,
    var(--td-brand-color),
    var(--td-brand-color-hover)
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: red;
}

.menu-wrapper {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 4px 0;
  scrollbar-width: thin;
}

/* 自定义滚动条 */
.menu-wrapper::-webkit-scrollbar {
  width: 4px;
}

.menu-wrapper::-webkit-scrollbar-track {
  background: transparent;
}

.menu-wrapper::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.menu-category {
  padding: 8px 16px 4px;
  margin-top: 8px;
}

.category-title {
  font-size: 12px;
  color: var(--td-text-color-secondary);
  text-transform: uppercase;
  letter-spacing: 1px;
  display: flex;
  align-items: center;
}

.category-title::before,
.category-title::after {
  content: "";
  height: 1px;
  background-color: var(--td-component-stroke);
  opacity: 0.4;
}

.category-title::before {
  width: 16px;
  margin-right: 8px;
}

.category-title::after {
  flex: 1;
  margin-left: 8px;
}

.menu-divider {
  height: 1px;
  margin: 8px 16px;
  background-color: var(--td-component-stroke);
  opacity: 0.4;
}

.menu-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 6px;
  transition: all 0.3s;
}

.collapse-btn-wrapper {
  display: flex;
  justify-content: center;
  padding: 12px 0;
  margin-top: auto;
  border-top: 1px solid var(--td-component-stroke);
  height: 57px;
}

.collapse-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  cursor: pointer;
  color: var(--td-text-color-secondary);
  background-color: var(--td-bg-color-page);
  transition: all 0.3s;
}

.collapse-btn:hover {
  color: var(--td-brand-color);
  background-color: var(--td-brand-color-hover-light);
  transform: scale(1.05);
}

/* 覆盖菜单样式 */
:deep(.t-menu__operations) {
  display: none;
}

:deep(.t-submenu) {
  margin-bottom: 4px;
}

:deep(.t-menu__item) {
  margin: 4px 12px;
  border-radius: 8px;
  font-weight: 500;
  padding: 10px 8px;
  transition: all 0.3s ease;
}

:deep(.t-menu__item.t-is-active) {
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: linear-gradient(
    90deg,
    var(--td-brand-color-light),
    transparent
  ) !important;
}

:deep(.t-menu__item.t-is-active) .menu-icon-wrapper {
  color: white;
}

:deep(.t-menu__item:hover) {
  transform: translateX(2px);
}

:deep(.t-menu__item.t-is-active:hover) {
  transform: translateX(0);
}

:deep(.t-menu__operations) {
  padding: 0 16px;
}

:deep(.t-menu__item--plain) {
  margin-bottom: 4px;
}

:deep(.t-submenu__icon) {
  margin-right: 8px;
}

:deep(.t-submenu__title) {
  margin: 4px 12px;
  border-radius: 8px;
  padding: 10px 8px;
}

:deep(.t-submenu__title.t-is-active) .menu-icon-wrapper {
  background-color: var(--td-brand-color);
  color: white;
}
</style>
