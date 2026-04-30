<template>
  <DefaultLayout>
    <div v-if="detail" class="order-detail">
      <h2>订单详情</h2>
      <div class="card">
        <p><strong>订单号:</strong> {{ detail.order.id }}</p>
        <p><strong>状态:</strong> {{ statusMap[detail.order.status] }}</p>
        <p><strong>收货人:</strong> {{ detail.order.receiver }} {{ detail.order.phone }}</p>
        <p><strong>地址:</strong> {{ detail.order.address }}</p>
        <p><strong>金额:</strong> ¥{{ (detail.order.payAmount / 100).toFixed(2) }}</p>
        <p><strong>下单时间:</strong> {{ detail.order.createTime?.substr(0, 16) }}</p>
      </div>
      <h3>商品列表</h3>
      <div class="card" v-for="item in detail.items" :key="item.id">
        <div class="item-row">
          <img :src="item.image || '/placeholder.png'" class="item-img" />
          <div>
            <p>{{ item.name }}</p>
            <p class="specs" v-if="item.specs">{{ item.specs }}</p>
            <p>¥{{ (item.price / 100).toFixed(2) }} x {{ item.quantity }}</p>
          </div>
        </div>
      </div>
      <div class="actions" v-if="detail.order.status === 1">
        <button class="btn btn-danger" @click="handleRefund">申请退款</button>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import DefaultLayout from '../../components/layout/DefaultLayout.vue'
import request from '../../utils/request'

const route = useRoute()
const detail = ref(null)
const statusMap = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消', 5: '退款中', 6: '已退款' }

onMounted(async () => {
  const res = await request.get('/order/' + route.params.id)
  detail.value = res.data
})

async function handleRefund() {
  if (!confirm('确认申请退款？')) return
  try {
    await request.put('/order/refund/' + route.params.id)
    detail.value.order.status = 6
    alert('退款成功')
  } catch (e) { alert(e.message || '退款失败') }
}
</script>

<style scoped>
.order-detail { max-width: 800px; margin: 0 auto; }
.item-row { display: flex; gap: 12px; align-items: center; }
.item-img { width: 60px; height: 60px; object-fit: cover; border-radius: 4px; }
.specs { font-size: 12px; color: #999; }
.actions { margin-top: 16px; text-align: right; }
</style>
