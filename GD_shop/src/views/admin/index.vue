<template>
  <div class="dashboard-container">
    <div class="welcome-section">
      <div class="welcome-info">
        <h2>您好，管理员</h2>
        <p>欢迎回到贵大电商管理系统</p>
      </div>
      <div class="date-info">
        <t-icon name="calendar" class="date-icon" />
        {{ currentDate }}
      </div>
    </div>

    <div class="stats-cards">
      <t-card v-for="(card, index) in statsCards" :key="index" hover bordered class="stats-card">
        <div class="stats-card-inner">
          <div class="stats-card-icon" :class="`bg-${card.color}`">
            <t-icon :name="card.icon" />
          </div>
          <div class="stats-card-content">
            <div class="stats-title">{{ card.title }}</div>
            <div class="stats-value">{{ card.value }}</div>
            <div class="stats-trend">{{ card.subtitle }}</div>
          </div>
        </div>
      </t-card>
    </div>

    <div class="dashboard-main">
      <t-card title="本周订单趋势" class="chart-card">
        <div class="bar-chart-container">
          <div class="bar-chart">
            <div v-for="(d, i) in weekData" :key="i" class="bar-wrapper">
              <div class="bar-hover-value">{{ d.orderCount }}单</div>
              <div class="bar" :style="{ height: Math.max(4, d.orderCount * 3) + 'px' }"></div>
              <div class="bar-day">{{ d.day }}</div>
            </div>
          </div>
        </div>
        <template #footer>
          <div class="view-more">
            <t-button theme="primary" variant="text" @click="$router.push('/admin/shop_data_overview')">
              查看更多 <template #suffix><t-icon name="chevron-right" /></template>
            </t-button>
          </div>
        </template>
      </t-card>

      <t-card title="快捷操作" class="shortcuts-card">
        <div class="shortcuts-container">
          <t-button v-for="s in shortcuts" :key="s.name" variant="outline" class="shortcut-btn" @click="$router.push(s.route)">
            <template #icon><t-icon :name="s.icon" /></template>
            {{ s.name }}
          </t-button>
        </div>
      </t-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue"
import http from "@/request/api/http"

const currentDate = computed(() => {
  const now = new Date()
  const week = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"][now.getDay()]
  return `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日 ${week}`
})

const todayStats = ref({ orderCount: 0, totalAmount: 0 })
const weekData = ref([])

const statsCards = computed(() => [
  { title: "今日订单", value: todayStats.value.orderCount, icon: "shop", color: "blue", subtitle: "当日下单数" },
  { title: "今日销售额", value: "¥" + (todayStats.value.totalAmount || 0).toLocaleString(), icon: "money", color: "green", subtitle: "当日交易额" },
  { title: "本周订单", value: weekData.value.reduce((s, d) => s + d.orderCount, 0), icon: "chart-bar", color: "orange", subtitle: "近7天累计" },
  { title: "本周销售额", value: "¥" + weekData.value.reduce((s, d) => s + (d.totalAmount || 0), 0).toLocaleString(), icon: "wallet", color: "purple", subtitle: "近7天累计" },
])

const shortcuts = [
  { name: "商品管理", icon: "shop", route: "/admin/product" },
  { name: "订单管理", icon: "order", route: "/admin/order_list" },
  { name: "数据概览", icon: "chart-pie", route: "/admin/shop_data_overview" },
]

onMounted(async () => {
  try {
    const dailyRes = await http.get("/stats/daily")
    if (dailyRes.data) todayStats.value = dailyRes.data

    const recentRes = await http.get("/stats/recent", { days: 7 })
    if (recentRes.data) {
      weekData.value = recentRes.data.map(d => ({
        ...d,
        day: d.date ? d.date.substring(5) : "",
      }))
    }
  } catch (e) {
    // error handled by interceptor
  }
})
</script>

<style scoped>
.dashboard-container { padding: 0 0 24px 0; }
.welcome-section { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.welcome-info h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.welcome-info p { margin: 8px 0 0; color: var(--td-text-color-secondary); font-size: 15px; }
.date-info { color: var(--td-text-color-secondary); font-size: 14px; background-color: var(--td-bg-color-container); padding: 6px 16px; border-radius: 20px; display: flex; align-items: center; }
.date-icon { margin-right: 8px; color: var(--td-brand-color); }
.stats-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.stats-card { transition: all 0.3s; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); border: none !important; }
.stats-card:hover { transform: translateY(-5px); box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08); }
.stats-card-inner { display: flex; align-items: center; padding: 16px; }
.stats-card-icon { width: 64px; height: 64px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-right: 16px; color: white; font-size: 24px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); }
.bg-blue { background: linear-gradient(135deg, #1890ff, #40a9ff); }
.bg-green { background: linear-gradient(135deg, #52c41a, #73d13d); }
.bg-orange { background: linear-gradient(135deg, #fa8c16, #ffa940); }
.bg-purple { background: linear-gradient(135deg, #722ed1, #9254de); }
.stats-card-content { flex: 1; }
.stats-title { font-size: 15px; color: var(--td-text-color-secondary); margin-bottom: 8px; }
.stats-value { font-size: 28px; font-weight: 600; line-height: 1.2; margin-bottom: 4px; }
.stats-trend { font-size: 13px; color: var(--td-text-color-secondary); }
.dashboard-main { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-bottom: 24px; }
.chart-card, .shortcuts-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
.bar-chart-container { height: 240px; padding: 20px; }
.bar-chart { display: flex; align-items: flex-end; justify-content: space-around; height: 100%; }
.bar-wrapper { display: flex; flex-direction: column; align-items: center; position: relative; }
.bar { width: 36px; background: linear-gradient(180deg, var(--td-brand-color), var(--td-brand-color-hover)); border-radius: 4px 4px 0 0; transition: all 0.3s; }
.bar-wrapper:hover .bar { filter: brightness(1.1); }
.bar-hover-value { position: absolute; top: -24px; font-size: 12px; background: var(--td-brand-color); color: white; padding: 2px 8px; border-radius: 4px; opacity: 0; transition: all 0.3s; white-space: nowrap; }
.bar-wrapper:hover .bar-hover-value { opacity: 1; }
.bar-day { margin-top: 8px; font-size: 13px; color: var(--td-text-color-secondary); }
.view-more { text-align: center; }
.shortcuts-container { display: flex; flex-wrap: wrap; gap: 12px; padding: 8px; }
.shortcut-btn { flex: 1; min-width: 100px; height: 48px; border-radius: 10px; transition: all 0.3s; }
.shortcut-btn:hover { transform: translateY(-3px); box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08); }
@media (max-width: 1200px) { .stats-cards { grid-template-columns: repeat(2, 1fr); } .dashboard-main { grid-template-columns: 1fr; } }
@media (max-width: 768px) { .stats-cards { grid-template-columns: 1fr; } }
</style>
