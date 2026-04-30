<template>
  <div class="account-welcome">
    <!-- 账号头部 -->
    <div class="account-header">
      <h1 class="account-title">
        <t-icon name="user" class="title-icon" />
        账号管理
      </h1>
      <p class="account-desc">
        角色分配、子账号管理、安全设置……<br />
        让每一位团队伙伴都能安心在自己的位置发光
      </p>

      <p class="account-poem">
        当您准备好时，登录即可管理您的店铺账号<br />
        让您的员工们各司其职，共同守护您的店铺
      </p>
    </div>

    <!-- 账号功能介绍卡片（三张小花，温柔并排） -->
    <div class="account-grid">
      <!-- 角色与权限 -->
      <div class="account-card">
        <div class="card-icon role">
          <t-icon name="user-1-filled" />
        </div>
        <h3>角色与权限</h3>
        <p>
          超级管理员、运营、客服、财务、只读查看<br />
          让伙伴们只做自己擅长的事，安心又高效
        </p>
      </div>

      <!-- 子账号管理 -->
      <div class="account-card">
        <div class="card-icon sub">
          <t-icon name="user-add" />
        </div>
        <h3>子账号管理</h3>
        <p>轻松添加、编辑、禁用团队伙伴<br /></p>
      </div>

      <!-- 安全设置 -->
      <div class="account-card">
        <div class="card-icon security">
          <t-icon name="lock-on" />
        </div>
        <h3>安全设置</h3>
        <p>
          登录IP限制、二次验证、密码策略<br />
          守护小店与每一位伙伴的安全
        </p>
      </div>
    </div>

    <!-- 页脚温柔结语 -->
    <div class="footer-poem">
      <p>愿您的团队，每一天都被安全守护</p>
    </div>
  </div>
</template>

<script setup>
// 无需任何逻辑～最纯净的静态页面～超级安静温柔
</script>

<style scoped>
.account-welcome {
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

.account-header {
  max-width: 1200px;
  margin: 0 auto 120px;
  padding: 0 24px;
  text-align: center;
}

.account-title {
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
  margin-left: -80px;
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

.account-desc {
  font-size: 1.5rem;
  color: var(--td-text-color-secondary);
  max-width: 800px;
  margin: 0 auto 48px;
  line-height: 2.2;
}

.account-poem {
  font-size: 1.3rem;
  color: var(--td-text-color-placeholder);
  max-width: 700px;
  margin: 0 auto;
  line-height: 2;
  font-style: italic;
}

/* 三个卡片并排～超级和谐 */
.account-grid {
  max-width: 1400px;
  margin: 0 auto 100px;
  padding: 0 24px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 48px;
}

/* 中屏两列，小屏一列～温柔适配 */
@media (max-width: 1200px) {
  .account-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 40px;
  }
}

@media (max-width: 768px) {
  .account-grid {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .account-title {
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
  .account-desc {
    font-size: 1.3rem;
  }
}

.account-card {
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

.account-card:hover {
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

.card-icon.role {
  background: linear-gradient(135deg, #722ed1, #9254de);
}
.card-icon.sub {
  background: linear-gradient(135deg, #13c2c2, #08979c);
}
.card-icon.security {
  background: linear-gradient(135deg, #ff6b6b, #ee5a52);
}

.account-card h3 {
  font-size: 2rem;
  font-weight: 600;
  margin: 0 0 24px 0;
  color: var(--td-text-color-primary);
}

.account-card p {
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