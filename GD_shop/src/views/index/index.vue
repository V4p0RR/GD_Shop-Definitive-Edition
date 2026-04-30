<template>
  <div class="home-page">
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">
          欢迎使用 <span class="brand-highlight">贵大电商商家管理平台</span>
        </h1>
        <!-- <p class="hero-subtitle">
          基于 Vue 3 和 TDesign 构建的现代化网站解决方案
        </p> -->
        <div class="hero-actions">
          <t-button theme="primary" size="large" class="cta-button">
            开始使用
            <template #suffix>
              <t-icon name="arrow-right" />
            </template>
          </t-button>
          <!-- <t-button theme="default" variant="outline" size="large">
            了解更多
          </t-button> -->
        </div>
      </div>

      <div class="hero-features">
        <div class="feature-card">
          <div class="feature-icon">
            <t-icon name="building" />
          </div>
          <h3>店铺管理</h3>
          <p>管理您的店铺、商品，并处理您的订单</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <t-icon name="cart-filled" />
          </div>
          <h3>营销活动</h3>
          <p>支持创建优惠券、满减折扣等优惠活动</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <t-icon name="chart-analytics" />
          </div>
          <h3>数据与权限</h3>
          <p>查看您的店铺收益数据，支持为不同账号分配不同权限</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 首页组件逻辑
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  padding: 40px 0;
  background: linear-gradient(
    135deg,
    var(--td-bg-color-page) 0%,
    rgba(var(--td-brand-color-rgb), 0.02) 100%
  );
}

.hero-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  text-align: center;
}

.hero-content {
  margin-bottom: 80px;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 24px;
  background: linear-gradient(
    135deg,
    var(--td-text-color-primary) 0%,
    var(--td-brand-color) 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  line-height: 1.2;
}

.brand-highlight {
  color: var(--td-brand-color);
  background: none;
  -webkit-background-clip: unset;
  background-clip: unset;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: var(--td-text-color-secondary);
  margin-bottom: 40px;
  line-height: 1.6;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}

.cta-button {
  padding: 0 32px;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  box-shadow: 0 4px 16px rgba(var(--td-brand-color-rgb), 0.3);
  transition: all 0.3s ease;
}

.cta-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 24px rgba(var(--td-brand-color-rgb), 0.4);
}

.cta-button :deep(.t-icon) {
  margin-left: 8px;
  transition: transform 0.3s ease;
}

.cta-button:hover :deep(.t-icon) {
  transform: translateX(4px);
}

.hero-features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 32px;
  margin-top: 80px;
  padding: 40px;
  background: rgba(var(--td-brand-color-rgb), 0.02);
  border-radius: 24px;
  border: 1px solid rgba(var(--td-brand-color-rgb), 0.05);
}

.feature-card {
  padding: 32px 24px;
  border-radius: 16px;
  background: var(--td-bg-color-secondarycontainer);
  border: 1px solid rgba(var(--td-brand-color-rgb), 0.15);
  transition: all 0.3s ease;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.feature-card:hover {
  transform: translateY(-4px);
  background: var(--td-bg-color-container);
  box-shadow: 0 8px 32px rgba(var(--td-brand-color-rgb), 0.15);
  border-color: rgba(var(--td-brand-color-rgb), 0.25);
}

.feature-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 24px;
  border-radius: 16px;
  background: linear-gradient(
    135deg,
    var(--td-brand-color-light) 0%,
    var(--td-brand-color-hover-light) 100%
  );
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--td-brand-color);
  font-size: 28px;
  box-shadow: 0 4px 12px rgba(var(--td-brand-color-rgb), 0.2);
}

.feature-card h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 16px;
  color: var(--td-text-color-primary);
  background: linear-gradient(
    135deg,
    var(--td-text-color-primary) 0%,
    var(--td-brand-color) 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.feature-card p {
  color: var(--td-text-color-secondary);
  line-height: 1.6;
  margin: 0;
  font-size: 15px;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.1rem;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .cta-button {
    width: 100%;
    max-width: 300px;
  }

  .hero-features {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .feature-card {
    padding: 24px 20px;
  }
}

@media (max-width: 480px) {
  .hero-section {
    padding: 0 16px;
  }

  .hero-title {
    font-size: 2rem;
  }

  .hero-subtitle {
    font-size: 1rem;
  }
}
</style>