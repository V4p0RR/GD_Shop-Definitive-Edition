<template>
  <DefaultLayout>
    <h2>我的订单</h2>
    <div class="status-tabs">
      <span v-for="s in statusList" :key="s.value"
            :class="{ active: activeStatus === s.value }"
            @click="activeStatus = s.value; loadOrders()">{{ s.label }}</span>
    </div>
    <div v-if="orders.length === 0" class="empty">暂无订单</div>
    <div v-for="order in orders" :key="order.id" class="card order-item" @click="$router.push(`/order/${order.id}`)">
      <div class="order-header">
        <span>订单号: {{ order.id }}</span>
        <span class="status">{{ statusMap[order.status] }}</span>
      </div>
      <div class="order-body">
        <span>¥{{ (order.payAmount / 100).toFixed(2) }}</span>
        <span>{{ order.createTime?.substr(0, 16) }}</span>
      </div>
    </div>
    <p class="loading" v-if="loading">加载中...</p>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import DefaultLayout from '../../components/layout/DefaultLayout.vue'
import request from '../../utils/request'

const statusMap = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消', 5: '退款中', 6: '已退款' }
const statusList = [
  { label: '全部', value: null },
  { label: '待支付', value: 0 }, { label: '已支付', value: 1 }, { label: '已发货', value: 2 },
  { label: '已完成', value: 3 }, { label: '已退款', value: 6 }
]
const activeStatus = ref(null)
const orders = ref([])
const loading = ref(false)

onMounted(() => loadOrders())

async function loadOrders() {
  loading.value = true
  const params = { current: 1 }
  if (activeStatus.value !== null) params.status = activeStatus.value
  const res = await request.get('/order/list', { params })
  orders.value = res.data || []
  loading.value = false
}
</script>

<style scoped>
.status-tabs { display: flex; gap: 12px; margin: 16px 0; flex-wrap: wrap; }
.status-tabs span { padding: 6px 14px; background: #fff; border-radius: 16px; cursor: pointer; font-size: 13px; }
.status-tabs span.active { background: #e4393c; color: #fff; }
.order-item { cursor: pointer; }
.order-header { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 14px; }
.status { color: #e4393c; }
.order-body { display: flex; justify-content: space-between; color: #666; font-size: 13px; }
.empty { text-align: center; padding: 60px; color: #999; }
</style>
