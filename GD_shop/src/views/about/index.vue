<template>
  <div class="data-welcome">
    <!-- 数据头部 -->
    <div class="data-header">
      <h1 class="data-title">
        <t-icon name="chart-analytics" class="title-icon" />
        店铺数据
      </h1>
      <p class="data-desc">
        销量、访客、转化、热销宝贝……<br />
        每一份数据都像一颗小星星，照亮您的经营之路
      </p>

      <p class="data-poem">
        当您准备好时，登录即可看见属于您的星河<br />
        静静等您发现每一份成长
      </p>
    </div>

    <!-- 数据功能介绍卡片（三张小花，温柔并排） -->
    <div class="data-grid">
      <!-- 销量洞察 -->
      <div class="data-card">
        <div class="card-icon sales">
          <t-icon name="chart-bar" />
        </div>
        <h3>销量洞察</h3>
        <p>每日、每周、每月的销售曲线<br /></p>
      </div>

      <!-- 访客分析 -->
      <div class="data-card">
        <div class="card-icon visitor">
          <t-icon name="user-1-filled" />
        </div>
        <h3>访客分析</h3>
        <p>
          来自哪里、停留多久、喜欢什么<br />
          每一位访客都是一道温柔的光<br />
        </p>
      </div>

      <!-- 转化趋势 -->
      <div class="data-card">
        <div class="card-icon conversion">
          <t-icon name="trending-up" />
        </div>
        <h3>转化趋势</h3>
        <p>
          浏览到下单的每一步旅程<br />
          热销商品、爆款排行、库存预警<br />
        </p>
      </div>
    </div>

    <!-- 页脚温柔结语 -->
    <div class="footer-poem">
      <p>让您每一天，都能获取到店铺的最新数据</p>
    </div>
  </div>
</template>

<script setup>
// 无需任何逻辑～最纯净的静态页面～超级安静温柔
</script>

<style scoped>
.data-welcome {
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

.data-header {
  max-width: 1200px;
  margin: 0 auto 120px;
  padding: 0 24px;
  text-align: center;
}

.data-title {
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

.data-desc {
  font-size: 1.5rem;
  color: var(--td-text-color-secondary);
  max-width: 800px;
  margin: 0 auto 48px;
  line-height: 2.2;
}

.data-poem {
  font-size: 1.3rem;
  color: var(--td-text-color-placeholder);
  max-width: 700px;
  margin: 0 auto;
  line-height: 2;
  font-style: italic;
}

/* 三个卡片并排～超级和谐 */
.data-grid {
  max-width: 1400px;
  margin: 0 auto 100px;
  padding: 0 24px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 48px;
}

/* 中屏两列，小屏一列～温柔适配 */
@media (max-width: 1200px) {
  .data-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
  }
}

@media (max-width: 768px) {
  .data-grid {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .data-title {
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
  .data-desc {
    font-size: 1.3rem;
  }
}

.data-card {
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

.data-card:hover {
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

.card-icon.sales {
  background: linear-gradient(135deg, #1890ff, #40a9ff);
}
.card-icon.visitor {
  background: linear-gradient(135deg, #52c41a, #73d13d);
}
.card-icon.conversion {
  background: linear-gradient(135deg, #fa8c16, #ffa940);
}

.data-card h3 {
  font-size: 2rem;
  font-weight: 600;
  margin: 0 0 24px 0;
  color: var(--td-text-color-primary);
}

.data-card p {
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