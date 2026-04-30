<template>
  <div class="data-overview-container">
    <div class="page-header">
      <div class="page-title">
        <h2>店铺数据概览</h2>
      </div>
      <div class="date-info">
        <t-icon name="calendar" class="date-icon" />
        {{ currentDate }}
      </div>
    </div>

    <div class="key-metrics">
      <t-card v-for="(metric, index) in keyMetrics" :key="index" hover class="metric-card">
        <div class="metric-inner">
          <div class="metric-icon" :class="`bg-${metric.color}`">
            <t-icon :name="metric.icon" />
          </div>
          <div class="metric-content">
            <div class="metric-label">{{ metric.label }}</div>
            <div class="metric-value">{{ metric.value }}</div>
          </div>
        </div>
      </t-card>
    </div>

    <div class="overview-main">
      <t-card title="近7日订单趋势" class="trend-card">
        <div class="bar-chart-container">
          <div class="bar-chart">
            <div v-for="(d, index) in weekData" :key="index" class="bar-item">
              <div class="bar-value">{{ d.orderCount }}</div>
              <div class="bar" :style="{ height: Math.max(4, orderMax > 0 ? (d.orderCount / orderMax) * 200 : 4) + 'px' }"></div>
              <div class="bar-label">{{ d.day }}</div>
            </div>
          </div>
        </div>
      </t-card>

      <t-card title="近7日销售额趋势" class="trend-card">
        <div class="bar-chart-container">
          <div class="bar-chart">
            <div v-for="(d, index) in weekData" :key="index" class="bar-item">
              <div class="bar-value">¥{{ (d.totalAmount / 100).toFixed(0) }}</div>
              <div class="bar" :style="{ height: Math.max(4, amountMax > 0 ? (d.totalAmount / amountMax) * 200 : 4) + 'px' }"></div>
              <div class="bar-label">{{ d.day }}</div>
            </div>
          </div>
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

const orderMax = computed(() => weekData.value.reduce((m, d) => Math.max(m, d.orderCount || 0), 0))
const amountMax = computed(() => weekData.value.reduce((m, d) => Math.max(m, d.totalAmount || 0), 0))

const keyMetrics = computed(() => [
  { label: "今日订单", value: todayStats.value.orderCount, icon: "shop", color: "blue" },
  { label: "今日销售额", value: `¥${((todayStats.value.totalAmount || 0) / 100).toFixed(2)}`, icon: "money", color: "green" },
  { label: "7日总订单", value: weekData.value.reduce((s, d) => s + d.orderCount, 0), icon: "chart-bar", color: "orange" },
  { label: "7日总销售额", value: `¥${(weekData.value.reduce((s, d) => s + (d.totalAmount || 0), 0) / 100).toFixed(2)}`, icon: "wallet", color: "purple" },
])

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
  } catch (e) { /* handled */ }
})
</script>

<style scoped>
.data-overview-container { padding: 0 0 24px 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.page-title h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.date-info { color: var(--td-text-color-secondary); font-size: 14px; background-color: var(--td-bg-color-container); padding: 6px 16px; border-radius: 20px; display: flex; align-items: center; }
.date-icon { margin-right: 8px; color: var(--td-brand-color); }
.key-metrics { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.metric-card { transition: all 0.3s; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); border: none !important; }
.metric-card:hover { transform: translateY(-5px); box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08); }
.metric-inner { display: flex; align-items: center; padding: 16px; }
.metric-icon { width: 64px; height: 64px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-right: 16px; color: white; font-size: 24px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); }
.bg-blue { background: linear-gradient(135deg, #1890ff, #40a9ff); }
.bg-green { background: linear-gradient(135deg, #52c41a, #73d13d); }
.bg-orange { background: linear-gradient(135deg, #fa8c16, #ffa940); }
.bg-purple { background: linear-gradient(135deg, #722ed1, #9254de); }
.metric-content { flex: 1; }
.metric-label { font-size: 15px; color: var(--td-text-color-secondary); margin-bottom: 8px; }
.metric-value { font-size: 28px; font-weight: 600; line-height: 1.2; }
.overview-main { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 24px; }
.trend-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
.bar-chart-container { height: 300px; padding: 20px; }
.bar-chart { display: flex; align-items: flex-end; justify-content: space-around; height: 100%; }
.bar-item { display: flex; flex-direction: column; align-items: center; width: 100%; }
.bar { width: 40px; background: linear-gradient(180deg, var(--td-brand-color), var(--td-brand-color-hover)); border-radius: 8px 8px 0 0; transition: all 0.4s; min-height: 4px; }
.bar:hover { filter: brightness(1.1); transform: translateY(-4px); }
.bar-value { font-size: 14px; font-weight: 600; margin-bottom: 4px; color: var(--td-text-color-primary); }
.bar-label { font-size: 13px; color: var(--td-text-color-secondary); margin-top: 8px; }
@media (max-width: 1200px) { .key-metrics { grid-template-columns: repeat(2, 1fr); } .overview-main { grid-template-columns: 1fr; } }
@media (max-width: 768px) { .key-metrics { grid-template-columns: 1fr; } }
</style>
