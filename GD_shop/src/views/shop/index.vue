<template>
  <div class="welcome-page">
    <!-- 店铺头部 -->
    <div class="shop-header">
      <h1 class="shop-title">
        <t-icon name="shop" class="title-icon" />
        贵大电商 <span class="subtitle">商家管理平台</span>
      </h1>
      <p class="shop-desc">
        商品管理 · 订单管理 · 客服聊天<br />
        让每一位商家都能轻松上手店铺经营
      </p>

      <p class="welcome-poem">
        提供方便、快捷的可视化操作<br />
        让您一站式完成店铺管理<br />
      </p>
    </div>

    <!-- 功能介绍卡片区域（只留三朵小花～纯文字，温柔如诗） -->
    <div class="features-grid">
      <!-- 商品管理 -->
      <div class="feature-card">
        <div class="card-icon product">
          <t-icon name="cart-filled" />
        </div>
        <h3>商品管理</h3>
        <p>编辑详情、设置库存、调整价格<br /></p>
      </div>

      <!-- 订单管理 -->
      <div class="feature-card">
        <div class="card-icon order">
          <t-icon name="assignment-filled" />
        </div>
        <h3>订单管理</h3>
        <p>查看、处理、发货，支持物流追踪<br /></p>
      </div>

      <!-- 客服聊天 -->
      <div class="feature-card">
        <div class="card-icon chat">
          <t-icon name="chat-bubble" />
        </div>
        <h3>客服聊天</h3>
        <p>实时交流，快速回复<br /></p>
      </div>
    </div>

    <!-- 页脚温柔结语 -->
    <div class="footer-poem">
      <p>
        贵大电商商家管理平台<br />
        愿您的每一天，都充满收获。
      </p>
    </div>
  </div>
</template>

<script setup>
// 无需任何逻辑～最纯净的静态页面～超级安静温柔
</script>
<style scoped>
.welcome-page {
  min-height: 100vh;
  padding: 100px 0 120px;
  background: linear-gradient(
    135deg,
    var(--td-bg-color-page) 0%,
    rgba(var(--td-brand-color-rgb), 0.05) 100%
  );
  display: flex;
  flex-direction: column;
  align-items: center;
}

.shop-header {
  max-width: 1200px;
  margin: 0 auto 120px;
  padding: 0 24px;
  text-align: center;
}

.shop-title {
  font-size: 3.8rem;
  font-weight: 700;
  margin-bottom: 32px;
  background: linear-gradient(
    135deg,
    var(--td-text-color-primary) 0%,
    var(--td-brand-color) 100%
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
  flex-wrap: wrap;
}

.title-icon {
  font-size: 4.4rem;
  color: var(--td-brand-color);
}

.subtitle {
  font-size: 2.2rem;
  font-weight: 500;
  background: none;
  color: var(--td-brand-color);
}

.shop-desc {
  font-size: 1.5rem;
  color: var(--td-text-color-secondary);
  max-width: 800px;
  margin: 0 auto 48px;
  line-height: 2.2;
}

.welcome-poem {
  font-size: 1.3rem;
  color: var(--td-text-color-placeholder);
  max-width: 700px;
  margin: 0 auto;
  line-height: 2;
  font-style: italic;
}

/* 关键调整：三个卡片在大屏完美并排～超级和谐 */
.features-grid {
  max-width: 1400px;
  margin: 0 auto 100px;
  padding: 0 24px;
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 大屏固定三列，并排站好～ */
  gap: 48px; /* 间距更大一点，更呼吸顺畅 */
}

/* 中屏自动两列，小屏一列～温柔适配 */
@media (max-width: 1200px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
  }
}

@media (max-width: 768px) {
  .features-grid {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .shop-title {
    font-size: 3rem;
    flex-direction: column;
    gap: 16px;
  }
  .title-icon {
    font-size: 3.8rem;
  }
  .subtitle {
    font-size: 1.8rem;
  }
  .shop-desc {
    font-size: 1.3rem;
  }
}

@media (max-width: 480px) {
  .shop-header {
    padding: 0 16px;
  }
  .shop-title {
    font-size: 2.6rem;
  }
}

.feature-card {
  padding: 48px 40px;
  border-radius: 24px;
  background: var(--td-bg-color-secondarycontainer);
  border: 1px solid rgba(var(--td-brand-color-rgb), 0.12);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  transition: all 0.4s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.feature-card:hover {
  transform: translateY(-12px);
  box-shadow: 0 20px 50px rgba(var(--td-brand-color-rgb), 0.18);
  border-color: rgba(var(--td-brand-color-rgb), 0.3);
  background: var(--td-bg-color-container);
}

.card-icon {
  width: 88px;
  height: 88px;
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: white;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  margin-bottom: 40px;
}

.card-icon.product {
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
}
.card-icon.order {
  background: linear-gradient(135deg, #ff6b6b, #ee5a52);
}
.card-icon.chat {
  background: linear-gradient(135deg, #a8e6cf, #88d8b0);
}

.feature-card h3 {
  font-size: 2rem;
  font-weight: 600;
  margin: 0 0 24px 0;
  color: var(--td-text-color-primary);
}

.feature-card p {
  flex: 1;
  color: var(--td-text-color-secondary);
  line-height: 2;
  font-size: 1.1rem;
}

.footer-poem {
  text-align: center;
  color: var(--td-text-color-placeholder);
  font-size: 1.2rem;
  line-height: 2;
  margin-top: auto;
  padding-top: 60px;
}
</style>