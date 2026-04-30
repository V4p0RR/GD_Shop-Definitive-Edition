<template>
  <div class="marketing-welcome">
    <!-- 营销头部 -->
    <div class="marketing-header">
      <h1 class="marketing-title">
        <t-icon name="money" class="title-icon" />
        店铺营销活动
      </h1>
      <p class="marketing-desc">
        在这里，每一场活动都是一份用心准备的礼物<br />
        满减、优惠券、限时折扣、买赠、新品首发……<br />
        让每一位顾客都感受到您的爱意与惊喜
      </p>

      <!-- <p class="marketing-poem">
        没有喧闹的促销，只有最温柔的邀请<br />
        当您准备好时，登录即可开启属于您的营销魔法<br />
        我们一直在这里，静静等您为顾客点亮烟花～
      </p> -->
    </div>

    <!-- 营销功能介绍卡片（三张小花，温柔并排） -->
    <div class="marketing-grid">
      <!-- 满减与折扣 -->
      <div class="marketing-card">
        <div class="card-icon discount">
          <t-icon name="discount" />
        </div>
        <h3>满减与限时折扣</h3>
        <p>
          满99减20、限时8折、秒杀专场<br />
          把每一份优惠都轻轻放在顾客心上<br />
        </p>
      </div>

      <!-- 优惠券管理 -->
      <div class="marketing-card">
        <div class="card-icon coupon">
          <t-icon name="coupon" />
        </div>
        <h3>优惠券发放</h3>
        <p>新客券、满额券、生日券、专属券<br /></p>
      </div>

      <!-- 买赠与新品活动 -->
      <div class="marketing-card">
        <div class="card-icon gift">
          <t-icon name="gift" />
        </div>
        <h3>买赠与新品首发</h3>
        <p>
          买一送一、加购赠品、新品尝鲜<br />
          每一场活动都像一场小派对<br />
        </p>
      </div>
    </div>

    <!-- 页脚温柔结语 -->
    <div class="footer-poem">
      <p>愿您的每一次活动，都盛满爱与欢笑</p>
    </div>
  </div>
</template>

<script setup>
// 无需任何逻辑～最纯净的静态页面～超级安静温柔
</script>

<style scoped>
.marketing-welcome {
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

.marketing-header {
  max-width: 1200px;
  margin: 0 auto 120px;
  padding: 0 24px;
  text-align: center;
}

.marketing-title {
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

.marketing-desc {
  font-size: 1.5rem;
  color: var(--td-text-color-secondary);
  max-width: 800px;
  margin: 0 auto 48px;
  line-height: 2.2;
}

.marketing-poem {
  font-size: 1.3rem;
  color: var(--td-text-color-placeholder);
  max-width: 700px;
  margin: 0 auto;
  line-height: 2;
  font-style: italic;
}

/* 三个卡片并排～超级和谐 */
.marketing-grid {
  max-width: 1400px;
  margin: 0 auto 100px;
  padding: 0 24px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 48px;
}

/* 中屏两列，小屏一列～温柔适配 */
@media (max-width: 1200px) {
  .marketing-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
  }
}

@media (max-width: 768px) {
  .marketing-grid {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .marketing-title {
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
  .marketing-desc {
    font-size: 1.3rem;
  }
}

.marketing-card {
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

.marketing-card:hover {
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

.card-icon.discount {
  background: linear-gradient(135deg, #722ed1, #9254de);
}
.card-icon.coupon {
  background: linear-gradient(135deg, #13c2c2, #08979c);
}
.card-icon.gift {
  background: linear-gradient(135deg, #faad14, #d48806);
}

.marketing-card h3 {
  font-size: 2rem;
  font-weight: 600;
  margin: 0 0 24px 0;
  color: var(--td-text-color-primary);
}

.marketing-card p {
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