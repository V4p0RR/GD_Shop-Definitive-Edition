<template>
  <DefaultLayout>
    <div class="detail" v-if="spu">
      <div class="images"><img :src="spu.images?.split(',')[0] || '/placeholder.png'" :alt="spu.name" /></div>
      <div class="info">
        <h1>{{ spu.name }}</h1>
        <p class="desc">{{ spu.description }}</p>
        <div class="skus">
          <h3>规格选择</h3>
          <div class="sku-list">
            <div v-for="sku in skus" :key="sku.id"
                 :class="['sku-item', { active: selectedSku?.id === sku.id }]"
                 @click="selectSku(sku)">
              <span>{{ sku.name || '默认' }}</span>
              <span class="price">¥{{ (sku.price / 100).toFixed(2) }}</span>
              <span class="stock">库存: {{ sku.stock }}</span>
            </div>
          </div>
        </div>
        <div class="actions">
          <span class="price-tag">¥{{ ((selectedSku?.price || 0) / 100).toFixed(2) }}</span>
          <button class="btn btn-primary" @click="addToCart" :disabled="!selectedSku">加入购物车</button>
          <button class="btn btn-primary" @click="buyNow" :disabled="!selectedSku" style="background:#ff9f00">立即购买</button>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import DefaultLayout from '../../components/layout/DefaultLayout.vue'
import request from '../../utils/request'

const route = useRoute()
const router = useRouter()
const spu = ref(null)
const skus = ref([])
const selectedSku = ref(null)

onMounted(async () => {
  const res = await request.get('/spu/' + route.params.id)
  spu.value = res.data
  const skuRes = await request.get('/sku/list/' + route.params.id)
  skus.value = skuRes.data || []
  if (skus.value.length > 0) selectedSku.value = skus.value[0]
})

function selectSku(sku) { selectedSku.value = sku }

async function addToCart() {
  if (!selectedSku.value) return
  const sessionId = localStorage.getItem('cartSessionId') || generateSessionId()
  localStorage.setItem('cartSessionId', sessionId)
  await request.post('/cart/add', { sessionId, skuId: selectedSku.value.id, quantity: 1 })
  alert('已加入购物车')
}

function buyNow() {
  if (!selectedSku.value) return
  // 简单实现：跳转到订单页
  router.push('/cart')
}

function generateSessionId() {
  const id = 'sess_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
  localStorage.setItem('cartSessionId', id)
  return id
}
</script>

<style scoped>
.detail { display: flex; gap: 30px; }
.images { flex: 1; }
.images img { width: 100%; border-radius: 8px; }
.info { flex: 1; }
.info h1 { font-size: 24px; margin-bottom: 12px; }
.desc { color: #666; margin-bottom: 20px; line-height: 1.6; }
.sku-list { display: flex; flex-wrap: wrap; gap: 10px; margin: 10px 0; }
.sku-item { padding: 10px 16px; border: 1px solid #ddd; border-radius: 4px; cursor: pointer; }
.sku-item.active { border-color: #e4393c; background: #fff4f4; }
.sku-item .price { color: #e4393c; font-weight: bold; margin-left: 8px; }
.sku-item .stock { font-size: 12px; color: #999; margin-left: 8px; }
.actions { margin-top: 24px; display: flex; gap: 12px; align-items: center; }
.price-tag { font-size: 28px; color: #e4393c; font-weight: bold; }
@media (max-width: 768px) { .detail { flex-direction: column; } }
</style>
