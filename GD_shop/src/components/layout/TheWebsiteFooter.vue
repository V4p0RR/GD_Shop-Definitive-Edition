<template>
  <footer class="website-footer-wrapper">
    <!-- 主要内容区域 -->
    <div class="footer-main">
      <div class="footer-container">
        <!-- 品牌信息区域 -->
        <div class="footer-brand">
          <div class="brand-section">
            <div class="brand-logo">
              <div class="logo-container">
                <img src="/logo.png" alt="Star Vue" class="logo-image" />
                <div class="logo-glow"></div>
              </div>
              <div class="brand-text">
                <h3 class="brand-name">贵大电商</h3>
                <p class="brand-tagline">商家管理平台</p>
              </div>
            </div>
            <p class="brand-description">帮助您快速、便捷地管理您的店铺</p>
            <!-- <div class="social-links">
              <a href="#" class="social-link" title="GitHub">
                <t-icon name="logo-github" />
              </a>
              <a href="#" class="social-link" title="Twitter">
                <t-icon name="logo-twitter" />
              </a>
              <a href="#" class="social-link" title="Discord">
                <t-icon name="logo-discord" />
              </a>
              <a href="#" class="social-link" title="LinkedIn">
                <t-icon name="logo-linkedin" />
              </a>
            </div> -->
          </div>
        </div>

        <!-- 链接区域 -->
        <div class="footer-links">
          <div class="link-section">
            <!-- <h4 class="section-title">产品</h4>
            <ul class="link-list">
              <li><a href="#" class="footer-link">功能特性</a></li>
              <li><a href="#" class="footer-link">价格方案</a></li>
              <li><a href="#" class="footer-link">更新日志</a></li>
              <li><a href="#" class="footer-link">API 文档</a></li>
              <li><a href="#" class="footer-link">集成指南</a></li>
            </ul> -->
          </div>

          <div class="link-section">
            <!-- <h4 class="section-title">解决方案</h4>
            <ul class="link-list">
              <li><a href="#" class="footer-link">企业级应用</a></li>
              <li><a href="#" class="footer-link">电商平台</a></li>
              <li><a href="#" class="footer-link">内容管理</a></li>
              <li><a href="#" class="footer-link">数据分析</a></li>
              <li><a href="#" class="footer-link">移动应用</a></li>
            </ul> -->
          </div>

          <div class="link-section">
            <h4 class="section-title">资源</h4>
            <ul class="link-list">
              <li><a href="#" class="footer-link">帮助中心</a></li>
              <li><a href="#" class="footer-link">文档</a></li>
              <!-- <li><a href="#" class="footer-link">社区论坛</a></li>
              <li><a href="#" class="footer-link">视频教程</a></li>
              <li><a href="#" class="footer-link">最佳实践</a></li> -->
            </ul>
          </div>

          <div class="link-section">
            <h4 class="section-title">公司</h4>
            <ul class="link-list">
              <li><a href="#" class="footer-link">关于我们</a></li>
              <li><a href="#" class="footer-link">联系我们</a></li>
              <li><a href="#" class="footer-link">加入我们</a></li>
              <!-- <li><a href="#" class="footer-link">新闻动态</a></li>
              <li><a href="#" class="footer-link">合作伙伴</a></li> -->
            </ul>
          </div>
        </div>

        <!-- 订阅区域 -->
        <!-- <div class="footer-subscribe">
          <div class="subscribe-section">
            <h4 class="section-title">订阅更新</h4>
            <p class="subscribe-description">
              获取最新的产品更新、技术资讯和行业动态
            </p>
            <div class="subscribe-form">
              <t-input
                v-model="email"
                placeholder="输入您的邮箱地址"
                class="subscribe-input"
                @keyup.enter="handleSubscribe"
              >
                <template #suffix>
                  <t-button
                    theme="primary"
                    size="small"
                    @click="handleSubscribe"
                    :loading="isSubscribing"
                  >
                    订阅
                  </t-button>
                </template>
              </t-input>
            </div>
            <p class="subscribe-tip">
              我们承诺保护您的隐私，不会向第三方分享您的邮箱地址
            </p>
          </div>
        </div> -->
      </div>
    </div>

    <!-- 底部版权区域 -->
    <div class="footer-bottom">
      <div class="footer-bottom-container">
        <div class="copyright-section">
          <p class="copyright-text">
            © 2025 <span class="brand-highlight">贵大电商</span>. 保留所有权利。
          </p>
          <div class="legal-links">
            <a href="#" class="legal-link">隐私政策</a>
            <a href="#" class="legal-link">服务条款</a>
            <a href="#" class="legal-link">Cookie 政策</a>
            <a href="#" class="legal-link">安全声明</a>
          </div>
        </div>

        <div class="footer-actions">
          <div class="language-selector">
            <t-select v-model="currentLanguage" class="language-select">
              <t-option value="zh-CN" label="简体中文" />
              <t-option value="en-US" label="English" />
              <t-option value="ja-JP" label="日本語" />
            </t-select>
          </div>
          <div class="back-to-top">
            <t-button
              theme="default"
              variant="outline"
              shape="circle"
              @click="scrollToTop"
              class="back-top-btn"
            >
              <template #icon>
                <t-icon name="arrow-up" />
              </template>
            </t-button>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref } from "vue";

// 响应式数据
const email = ref("");
const isSubscribing = ref(false);
const currentLanguage = ref("zh-CN");

// 事件处理
const handleSubscribe = async () => {
  if (!email.value.trim()) {
    return;
  }

  isSubscribing.value = true;

  try {
    // 模拟订阅请求
    await new Promise((resolve) => setTimeout(resolve, 1000));
    console.log("订阅邮箱:", email.value);
    email.value = "";
    // 这里可以添加成功提示
  } catch (error) {
    console.error("订阅失败:", error);
  } finally {
    isSubscribing.value = false;
  }
};

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};
</script>

<style scoped>
.website-footer-wrapper {
  background: linear-gradient(
    135deg,
    var(--td-bg-color-container) 0%,
    rgba(var(--td-brand-color-rgb), 0.02) 100%
  );
  border-top: 1px solid rgba(var(--td-brand-color-rgb), 0.1);
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.website-footer-wrapper::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: radial-gradient(
      circle at 20% 20%,
      rgba(var(--td-brand-color-rgb), 0.03) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 80% 80%,
      rgba(var(--td-brand-color-rgb), 0.02) 0%,
      transparent 50%
    );
  pointer-events: none;
  z-index: 0;
}

.footer-main {
  position: relative;
  z-index: 1;
  padding: 80px 0 60px;
}

.footer-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: grid;
  grid-template-columns: 1.5fr 2fr 1fr;
  gap: 60px;
  align-items: start;
}

/* 品牌区域 */
.footer-brand {
  grid-column: 1;
}

.brand-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo-container {
  position: relative;
  width: 248px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  /* background: linear-gradient(
    135deg,
    var(--td-brand-color) 0%,
    var(--td-brand-color-hover) 100%
  ); */
  background: white;
  box-shadow: 0 4px 16px rgba(var(--td-brand-color-rgb), 0.3);
}

.logo-image {
  width: 228px;
  height: 90px;
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
  border-radius: 12px;
  background: linear-gradient(
    135deg,
    rgba(var(--td-brand-color-rgb), 0.3) 0%,
    transparent 50%
  );
  animation: footerGlow 3s ease-in-out infinite alternate;
}

@keyframes footerGlow {
  from {
    opacity: 0.3;
  }
  to {
    opacity: 0.6;
  }
}

.brand-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.brand-name {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(
    135deg,
    var(--td-brand-color) 0%,
    var(--td-brand-color-hover) 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: red;
}

.brand-tagline {
  font-size: 14px;
  color: var(--td-text-color-secondary);
  margin: 0;
  font-weight: 500;
}

.brand-description {
  font-size: 15px;
  line-height: 1.6;
  color: var(--td-text-color-secondary);
  margin: 0;
  margin-left: 70px;
}

.social-links {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.social-link {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: var(--td-bg-color-page);
  color: var(--td-text-color-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.social-link:hover {
  background: var(--td-brand-color-hover-light);
  color: var(--td-brand-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(var(--td-brand-color-rgb), 0.2);
}

.social-link :deep(.t-icon) {
  font-size: 18px;
}

/* 链接区域 */
.footer-links {
  grid-column: 2;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40px;
}

.link-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--td-text-color-primary);
  margin: 0;
  position: relative;
  padding-bottom: 8px;
}

.section-title::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 24px;
  height: 2px;
  background: linear-gradient(
    90deg,
    var(--td-brand-color) 0%,
    var(--td-brand-color-hover) 100%
  );
  border-radius: 1px;
}

.link-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.footer-link {
  color: var(--td-text-color-secondary);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
  position: relative;
  padding-left: 0;
}

.footer-link::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 1px;
  background: var(--td-brand-color);
  transition: width 0.3s ease;
}

.footer-link:hover {
  color: var(--td-brand-color);
  padding-left: 12px;
}

.footer-link:hover::before {
  width: 8px;
}

/* 订阅区域 */
.footer-subscribe {
  grid-column: 3;
}

.subscribe-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.subscribe-description {
  font-size: 14px;
  line-height: 1.5;
  color: var(--td-text-color-secondary);
  margin: 0;
}

.subscribe-form {
  position: relative;
}

.subscribe-input {
  width: 100%;
}

.subscribe-input :deep(.t-input) {
  background: var(--td-bg-color-page);
  border-radius: 12px;
  border: 1px solid rgba(var(--td-brand-color-rgb), 0.1);
  transition: all 0.3s ease;
}

.subscribe-input :deep(.t-input:focus) {
  border-color: var(--td-brand-color);
  box-shadow: 0 0 0 3px rgba(var(--td-brand-color-rgb), 0.1);
}

.subscribe-input :deep(.t-input__suffix) {
  padding-right: 8px;
}

.subscribe-tip {
  font-size: 12px;
  color: var(--td-text-color-placeholder);
  margin: 0;
  line-height: 1.4;
}

/* 底部版权区域 */
.footer-bottom {
  position: relative;
  z-index: 1;
  padding: 24px 0;
  border-top: 1px solid rgba(var(--td-brand-color-rgb), 0.1);
  background: rgba(var(--td-brand-color-rgb), 0.02);
}

.footer-bottom-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.copyright-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.copyright-text {
  font-size: 14px;
  color: var(--td-text-color-secondary);
  margin: 0;
}

.brand-highlight {
  color: red;
  font-weight: 600;
}

.legal-links {
  display: flex;
  gap: 24px;
}

.legal-link {
  font-size: 13px;
  color: var(--td-text-color-placeholder);
  text-decoration: none;
  transition: color 0.3s ease;
}

.legal-link:hover {
  color: var(--td-brand-color);
}

.footer-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.language-selector {
  min-width: 120px;
}

.language-select :deep(.t-select) {
  background: var(--td-bg-color-page);
  border-radius: 8px;
}

.back-to-top {
  display: flex;
  align-items: center;
}

.back-top-btn {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.back-top-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(var(--td-brand-color-rgb), 0.2);
  border-color: var(--td-brand-color);
  color: var(--td-brand-color);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .footer-container {
    grid-template-columns: 1fr 1.5fr 1fr;
    gap: 40px;
  }
}

@media (max-width: 1024px) {
  .footer-container {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .footer-links {
    grid-template-columns: repeat(2, 1fr);
  }

  .footer-subscribe {
    max-width: 400px;
  }
}

@media (max-width: 768px) {
  .footer-main {
    padding: 60px 0 40px;
  }

  .footer-container {
    padding: 0 16px;
    gap: 32px;
  }

  .footer-links {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .footer-bottom-container {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }

  .legal-links {
    justify-content: center;
    flex-wrap: wrap;
    gap: 16px;
  }

  .footer-actions {
    flex-direction: column;
    gap: 12px;
  }

  .brand-logo {
    justify-content: center;
  }

  .social-links {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .footer-container {
    padding: 0 12px;
  }

  .footer-bottom-container {
    padding: 0 12px;
  }

  .legal-links {
    gap: 12px;
  }

  .legal-link {
    font-size: 12px;
  }
}

/* 暗色主题适配 */
@media (prefers-color-scheme: dark) {
  .website-footer-wrapper {
    background: linear-gradient(
      135deg,
      var(--td-bg-color-container) 0%,
      rgba(var(--td-brand-color-rgb), 0.05) 100%
    );
  }

  .footer-bottom {
    background: rgba(var(--td-brand-color-rgb), 0.03);
  }
}
</style>
