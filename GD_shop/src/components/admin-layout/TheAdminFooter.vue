<template>
  <div class="footer">
    <div class="footer-copyright">
      © 2025 <span class="brand-text">贵大电商</span>. 版权所有
    </div>
    <div class="footer-links">
      <a href="#">帮助</a>
      <a href="#">隐私</a>
      <a href="#">条款</a>
    </div>
  </div>
</template>

<script setup>
// 页脚组件
// 可以通过props来配置版权信息、链接等
</script>

<style scoped>
.footer {
  height: 57px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid var(--td-component-stroke);
  color: var(--td-text-color-secondary);
  font-size: 14px;
  background-color: var(--td-bg-color-container);
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 5;
}

.brand-text {
  color: var(--td-brand-color);
  font-weight: 500;
}

.footer-links {
  display: flex;
  gap: 24px;
}

.footer-links a {
  color: var(--td-text-color-secondary);
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: var(--td-brand-color);
}

@media (max-width: 768px) {
  .footer {
    flex-direction: column;
    height: auto;
    padding: 16px;
    gap: 8px;
    text-align: center;
  }
}
</style>
