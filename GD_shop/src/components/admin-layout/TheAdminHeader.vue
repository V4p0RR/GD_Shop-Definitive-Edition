<template>
  <div class="header-container">
    <!-- 左侧Logo和标题 -->
    <div class="logo-container">
      <div class="logo" @click="handleLogoClick">贵大电商</div>
      <div class="title">后台管理</div>
    </div>

    <!-- 中间面包屑（保持不变，可后续动态绑定） -->
    <t-breadcrumb class="breadcrumb">
      <t-breadcrumb-item>首页</t-breadcrumb-item>
      <t-breadcrumb-item>控制台</t-breadcrumb-item>
    </t-breadcrumb>

    <!-- 右侧操作区 -->
    <div class="action-area">
      <!-- 搜索按钮（加了 @keyup.enter 实现回车搜索） -->
      <div class="search-box">
        <t-input
          placeholder="搜索..."
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #prefix-icon>
            <t-icon
              name="search"
              @click="handleSearch"
              style="cursor: pointer"
            />
          </template>
        </t-input>
      </div>

      <!-- 通知（填充真实内容 + 点击处理） -->
      <t-dropdown trigger="click">
        <t-tooltip content="通知">
          <t-badge
            :count="notificationCount"
            :dot="false"
            class="notification-badge"
          >
            <t-button variant="text" shape="square" class="action-btn">
              <template #icon><t-icon name="notification" /></template>
            </t-button>
          </t-badge>
        </t-tooltip>
        <template #dropdown>
          <t-dropdown-menu class="notification-dropdown">
            <!-- <t-dropdown-item value="1">
              <t-icon
                name="notification-filled"
                style="margin-right: 8px; color: var(--td-brand-color)"
              />
              系统更新：V1.0.1 已发布
            </t-dropdown-item>
            <t-dropdown-item value="2">
              <t-icon
                name="user-add"
                style="margin-right: 8px; color: var(--td-success-color)"
              />
              新增用户：张三已注册
            </t-dropdown-item>
            <t-dropdown-item value="3">
              <t-icon
                name="mail"
                style="margin-right: 8px; color: var(--td-warning-color)"
              />
              您有新的消息待查看
            </t-dropdown-item> -->
            <t-dropdown-item
              value="all"
              class="view-all-btn"
              @click="handleNotificationAction('all')"
            >
              查看全部通知
            </t-dropdown-item>
          </t-dropdown-menu>
        </template>
      </t-dropdown>

      <!-- 主题切换（保持不变） -->
      <ThemeToggle />

      <!-- 用户信息（绑定 value + 点击处理） -->
      <t-dropdown trigger="click">
        <div class="user-info">
          <t-avatar
            image="https://tdesign.gtimg.com/starter/avatar1.jpg"
            size="small"
          />
          <span class="user-name">管理员</span>
          <t-icon name="chevron-down" size="small" />
        </div>
        <template #dropdown>
          <t-dropdown-menu>
            <!-- <t-dropdown-item
              value="user-info"
              @click="handleUserAction('user-info')"
            >
              <t-icon name="user-circle" style="margin-right: 8px" />
              个人信息
            </t-dropdown-item> -->
            <t-dropdown-item
              value="change-password"
              @click="handleUserAction('change-password')"
            >
              <t-icon name="lock-on" style="margin-right: 8px" />
              修改密码
            </t-dropdown-item>
            <!-- <t-dropdown-item
              value="profile"
              @click="handleUserAction('profile')"
            >
              <t-icon name="user" style="margin-right: 8px" />
              个人中心
            </t-dropdown-item> -->
            <t-dropdown-item
              value="settings"
              @click="handleUserAction('settings')"
            >
              <t-icon name="setting" style="margin-right: 8px" />
              设置
            </t-dropdown-item>
            <t-dropdown-item
              value="logout"
              divider
              @click="handleUserAction('logout')"
            >
              <t-icon name="poweroff" style="margin-right: 8px" />
              退出登录
            </t-dropdown-item>
          </t-dropdown-menu>
        </template>
      </t-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import ThemeToggle from "../layout/ThemeToggle.vue";
import { useAuthStore } from "@/stores/auth"; // 你的 authStore
import { MessagePlugin, DialogPlugin } from "tdesign-vue-next";

const router = useRouter();
const authStore = useAuthStore();

// 通知数量（可后续从接口取）
const notificationCount = ref(3);

// Logo 点击跳首页
const handleLogoClick = () => {
  MessagePlugin.info("正在跳转到控制台...");
  router.push("/admin"); // 或 '/dashboard'，改成你的后台首页
};

// 搜索功能（演示）
const handleSearch = (e) => {
  const keyword = e.target.value.trim();
  if (!keyword) {
    MessagePlugin.warning("请输入搜索内容");
    return;
  }
  MessagePlugin.success(`搜索 "${keyword}" 的结果正在加载...`);
  // router.push(`/search?q=${keyword}`); // 后续接真实搜索页
};

// 通知菜单点击
const handleNotificationAction = (value) => {
  if (value === "all") {
    MessagePlugin.info("跳转到通知中心...");
    router.push("/system_log");
    return;
  }
  MessagePlugin.success("已查看此通知");
};

// 用户菜单点击（重点修复：退出登录用 DialogPlugin.confirm）
const handleUserAction = (value) => {
  switch (value) {
    case "user-info":
      router.push("/user/info");
      break;
    case "change-password":
      router.push("/user_password");
      break;
    case "profile":
      router.push("/user_profile");
      break;
    case "settings":
      router.push("/system_config");
      break;
    case "logout":
      DialogPlugin.confirm({
        header: "退出登录",
        body: "确认退出登录吗？",
        theme: "warning",
        confirmBtn: { content: "确认退出", theme: "danger" },
        cancelBtn: { content: "取消", theme: "default" },
        onConfirm: () => {
          authStore.clear(); // 安全清空状态
          MessagePlugin.success("已退出登录");
          router.push("/login");
        },
        onCancel: () => {},
      });
      break;
    default:
      MessagePlugin.info("功能开发中");
  }
};
</script>
<style scoped>
.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 24px;
  background-color: var(--td-bg-color-container);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(8px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.03);
  position: relative;
  z-index: 10;
}

.logo-container {
  display: flex;
  align-items: center;
  flex-shrink: 0; /* 防止被挤压 */
}

.logo {
  font-size: 22px;
  font-weight: 700;
  background-image: linear-gradient(
    45deg,
    var(--td-brand-color),
    var(--td-brand-color-hover)
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent; /* 修复了原来的 red 覆盖 */
  margin-right: 8px;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: var(--td-text-color-primary);
  white-space: nowrap;
}

.breadcrumb {
  margin-left: 24px;
  flex: 1;
  min-width: 0; /* 允许 flex 收缩 */
}

.action-area {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

.search-box {
  width: 200px;
  transition: width 0.3s ease;
}

.search-box:focus-within {
  width: 280px;
}

.search-input :deep(.t-input) {
  background-color: var(--td-bg-color-page);
  border-radius: 20px;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.search-input :deep(.t-input:focus) {
  border-color: var(--td-brand-color-hover);
  box-shadow: 0 0 0 2px rgba(var(--td-brand-color-rgb), 0.1),
    inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.action-btn {
  position: relative;
  opacity: 0.85;
  transition: all 0.3s;
  background-color: var(--td-bg-color-page);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
}

.action-btn:hover {
  opacity: 1;
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.notification-badge :deep(.t-badge--count) {
  box-shadow: 0 0 0 1px var(--td-bg-color-container);
}

.user-info {
  display: flex;
  align-items: center;
  padding: 4px 12px;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: var(--td-bg-color-page);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.user-info:hover {
  background-color: var(--td-bg-color-container-hover);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.user-name {
  margin-left: 8px;
  margin-right: 4px;
  font-size: 14px;
  font-weight: 500;
  color: var(--td-text-color-primary);
  white-space: nowrap;
}

/* ==================== 温柔响应式调整 ==================== */

/* 大屏（>1100px）：一切优雅完整～ */
@media (max-width: 1100px) {
  .search-box {
    width: 180px;
  }
  .search-box:focus-within {
    width: 240px;
  }
}

/* 中屏（≤900px）：隐藏标题，保持搜索和面包屑 */
@media (max-width: 900px) {
  .title {
    display: none;
  }
  .search-box {
    width: 160px;
  }
  .search-box:focus-within {
    width: 220px;
  }
}

/* 小屏（≤768px）：核心保留，其余温柔隐藏，顶栏永远清爽～ */
@media (max-width: 768px) {
  .header-container {
    padding: 0 16px;
  }

  .breadcrumb,
  .search-box,
  .user-name {
    display: none; /* 隐藏非核心元素 */
  }

  .action-area {
    gap: 8px;
  }

  /* 用户头像变成圆形小图标，更节省空间又可爱～ */
  .user-info {
    padding: 4px;
    border-radius: 50%;
    min-width: 40px;
    justify-content: center;
  }

  /* 只保留头像，不显示名字 */
  .user-info .t-avatar {
    margin: 0;
  }
}

/* 超小屏（≤480px）额外保险～ */
@media (max-width: 480px) {
  .logo {
    font-size: 20px;
  }
  .action-area {
    gap: 6px;
  }
}

/* 下拉菜单样式（完全保留原来的美美动画～） */
:deep(.t-dropdown__item) {
  transition: all 0.3s;
  padding: 10px 16px;
  position: relative;
  overflow: hidden;
}

:deep(.t-dropdown__item::before) {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 3px;
  background-color: var(--td-brand-color);
  transform: translateX(-100%);
  transition: transform 0.3s;
}

:deep(.t-dropdown__item:hover::before) {
  transform: translateX(0);
}

:deep(.t-dropdown__item:hover) {
  color: var(--td-brand-color);
  background-color: var(--td-brand-color-hover-light);
  padding-left: 20px;
}

:deep(.t-dropdown__item:active) {
  background-color: rgba(var(--td-brand-color-rgb), 0.2);
  transform: scale(0.98);
}

:deep(.t-dropdown__item .t-icon) {
  margin-right: 8px;
  transition: all 0.3s;
}

:deep(.t-dropdown__item:hover .t-icon) {
  color: var(--td-brand-color);
  transform: translateX(2px);
}

:deep(.t-dropdown__content) {
  border-radius: 10px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  border: 1px solid rgba(var(--td-brand-color-rgb), 0.1);
}

:deep(.t-dropdown__menu) {
  padding: 6px;
}

.view-all-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px 20px !important;
  margin: 0 !important;
  font-size: 14px;
  font-weight: 500;
  color: var(--td-brand-color) !important;
  border-radius: 0 !important;
  transition: all 0.2s ease;
}

.view-all-btn:hover {
  background-color: var(--td-brand-color-focus) !important;
  color: var(--td-brand-color) !important;
}
</style>