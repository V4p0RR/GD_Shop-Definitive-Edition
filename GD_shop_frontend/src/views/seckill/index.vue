<template>
  <DefaultLayout>
    <h2>限时秒杀</h2>
    <div v-if="activities.length === 0" class="empty">暂无秒杀活动</div>
    <div class="seckill-grid">
      <div v-for="act in activities" :key="act.id" class="card seckill-card">
        <div class="spu-name">{{ act.spuName }}</div>
        <div class="price-row">
          <span class="seckill-price">¥{{ (act.seckillPrice / 100).toFixed(2) }}</span>
          <span class="stock">库存: {{ act.stock }}</span>
        </div>
        <div class="time-row">
          <span>{{ act.beginTime?.substr(0, 16) }} - {{ act.endTime?.substr(0, 16) }}</span>
        </div>
        <button class="btn btn-primary btn-full" @click="doSeckill(act)">立即秒杀</button>
      </div>
    </div>
    <p class="msg" v-if="msg">{{ msg }}</p>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import DefaultLayout from '../../components/layout/DefaultLayout.vue'
import request from '../../utils/request'

const router = useRouter()
const activities = ref([])
const msg = ref('')

onMounted(async () => {
  const res = await request.get('/seckill/list')
  activities.value = res.data || []
})

async function doSeckill(act) {
  const token = localStorage.getItem('token')
  if (!token) { router.push('/login'); return }
  try {
    const res = await request.post('/seckill/order/' + act.id)
    msg.value = '秒杀成功！订单号: ' + res.data
  } catch (e) { msg.value = e.message || '秒杀失败' }
}
</script>

<style scoped>
.seckill-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-top: 16px; }
.seckill-card { text-align: center; }
.spu-name { font-size: 16px; font-weight: bold; margin-bottom: 8px; }
.price-row { margin: 8px 0; }
.seckill-price { font-size: 24px; color: #e4393c; font-weight: bold; }
.stock { font-size: 12px; color: #999; margin-left: 8px; }
.time-row { font-size: 12px; color: #666; margin-bottom: 12px; }
.btn-full { width: 100%; padding: 10px; }
.msg { text-align: center; margin-top: 16px; color: #e4393c; }
.empty { text-align: center; padding: 60px; color: #999; }
@media (max-width: 768px) { .seckill-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
