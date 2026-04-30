<template>
  <DefaultLayout>
    <h2>购物车</h2>
    <div v-if="items.length === 0" class="empty">购物车是空的</div>
    <div v-else>
      <div v-for="item in items" :key="item.skuId" class="card cart-item">
        <img :src="item.image || '/placeholder.png'" :alt="item.name" class="item-img" />
        <div class="item-info">
          <h4>{{ item.name }}</h4>
          <p class="specs" v-if="item.specs">{{ item.specs }}</p>
          <p class="price">¥{{ (item.price / 100).toFixed(2) }}</p>
        </div>
        <div class="qty-ctrl">
          <button @click="changeQty(item, -1)">-</button>
          <span>{{ item.quantity }}</span>
          <button @click="changeQty(item, 1)">+</button>
        </div>
        <span class="subtotal">¥{{ (item.price * item.quantity / 100).toFixed(2) }}</span>
        <button class="btn-remove" @click="removeItem(item.skuId)">删除</button>
      </div>
      <div class="address-card">
        <h3>收货信息</h3>
        <div class="address-row">
          <input v-model="addr.receiver" placeholder="收货人" class="addr-input" />
          <input v-model="addr.phone" placeholder="联系电话" class="addr-input" />
        </div>
        <input v-model="addr.address" placeholder="收货地址" class="addr-input addr-full" />
      </div>
      <div class="cart-footer">
        <span class="total">合计: ¥{{ totalAmount.toFixed(2) }}</span>
        <button class="btn btn-primary" @click="checkout" :disabled="items.length === 0">结算</button>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import DefaultLayout from '../../components/layout/DefaultLayout.vue'
import request from '../../utils/request'

const router = useRouter()
const items = ref([])
const sessionId = localStorage.getItem('cartSessionId') || ''

const addr = reactive({
  receiver: '',
  phone: '',
  address: '',
})

onMounted(async () => {
  if (sessionId) {
    const res = await request.get('/cart/list', { params: { sessionId } })
    items.value = res.data || []
  }
  // 加载用户收货信息
  const token = localStorage.getItem('token')
  if (token) {
    try {
      const res = await request.get('/user/me')
      if (res.data) {
        addr.receiver = res.data.defaultReceiver || ''
        addr.phone = res.data.defaultPhone || ''
        addr.address = res.data.defaultAddress || ''
      }
    } catch (e) { /* ignore */ }
  }
})

const totalAmount = computed(() => {
  return items.value.reduce((sum, i) => sum + (i.price * i.quantity / 100), 0)
})

async function changeQty(item, delta) {
  const newQty = item.quantity + delta
  if (newQty <= 0) return removeItem(item.skuId)
  await request.put('/cart/update', { sessionId, skuId: item.skuId, quantity: newQty })
  item.quantity = newQty
}

async function removeItem(skuId) {
  await request.delete('/cart/remove', { params: { sessionId, skuId } })
  items.value = items.value.filter(i => i.skuId !== skuId)
}

async function checkout() {
  const token = localStorage.getItem('token')
  if (!token) { router.push('/login'); return }
  const skuIds = items.value.map(i => i.skuId)
  const quantities = items.value.map(i => i.quantity)
  if (!addr.receiver || !addr.phone || !addr.address) {
    alert('请填写完整的收货信息')
    return
  }
  try {
    await request.post('/order', { address: addr.address, phone: addr.phone, receiver: addr.receiver, skuIds, quantities })
    await request.delete('/cart/clear', { params: { sessionId } })
    alert('下单成功')
    router.push('/order/list')
  } catch (e) { alert(e.message || '下单失败') }
}
</script>

<style scoped>
.cart-item { display: flex; align-items: center; gap: 16px; }
.item-img { width: 80px; height: 80px; object-fit: cover; border-radius: 4px; }
.item-info { flex: 1; }
.item-info h4 { font-size: 14px; }
.price { color: #e4393c; font-weight: bold; margin-top: 4px; }
.qty-ctrl { display: flex; align-items: center; gap: 8px; }
.qty-ctrl button { width: 28px; height: 28px; border: 1px solid #ddd; background: #fff; cursor: pointer; border-radius: 4px; }
.subtotal { font-weight: bold; color: #e4393c; min-width: 80px; text-align: right; }
.btn-remove { border: none; background: none; color: #999; cursor: pointer; }
.cart-footer { display: flex; justify-content: flex-end; align-items: center; gap: 16px; margin-top: 20px; padding: 16px; background: #fff; border-radius: 8px; }
.total { font-size: 18px; font-weight: bold; color: #e4393c; }
.address-card { background: #fff; border-radius: 8px; padding: 16px; margin-bottom: 16px; }
.address-card h3 { font-size: 15px; margin-bottom: 12px; }
.address-row { display: flex; gap: 12px; margin-bottom: 8px; }
.addr-input { padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
.addr-full { width: 100%; box-sizing: border-box; }
.empty { text-align: center; padding: 60px; color: #999; }
</style>
