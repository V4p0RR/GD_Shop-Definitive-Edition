<template>
  <DefaultLayout>
    <div class="home">
      <div class="categories">
        <span v-for="cat in categories" :key="cat.id"
              :class="{ active: activeCat === cat.id }"
              @click="activeCat = cat.id; loadProducts()">{{ cat.name }}</span>
      </div>
      <div class="search-bar">
        <input v-model="keyword" placeholder="搜索商品..." @keyup.enter="loadProducts" />
        <button class="btn btn-primary" @click="loadProducts">搜索</button>
      </div>
      <div class="product-grid">
        <div v-for="spu in products" :key="spu.id" class="product-card" @click="$router.push(`/product/${spu.id}`)">
          <img :src="spu.images?.split(',')[0] || '/placeholder.png'" :alt="spu.name" />
          <div class="info">
            <h3>{{ spu.name }}</h3>
            <p class="sold">已售 {{ spu.sold || 0 }}</p>
          </div>
        </div>
      </div>
      <p class="empty" v-if="products.length === 0">暂无商品</p>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import DefaultLayout from '../../components/layout/DefaultLayout.vue'
import request from '../../utils/request'

const categories = ref([])
const activeCat = ref(null)
const keyword = ref('')
const products = ref([])

onMounted(async () => {
  const res = await request.get('/category/list')
  categories.value = res.data || []
  loadProducts()
})

async function loadProducts() {
  const params = { current: 1 }
  if (activeCat.value) params.categoryId = activeCat.value
  if (keyword.value) params.keyword = keyword.value
  const res = await request.get('/spu/list', { params })
  products.value = res.data || []
}
</script>

<style scoped>
.home { max-width: 1200px; margin: 0 auto; }
.categories { display: flex; gap: 12px; padding: 12px 0; flex-wrap: wrap; }
.categories span { padding: 6px 16px; background: #fff; border-radius: 20px; cursor: pointer; font-size: 14px; }
.categories span.active { background: #e4393c; color: #fff; }
.search-bar { display: flex; gap: 8px; margin: 16px 0; }
.search-bar input { flex: 1; padding: 10px; border: 1px solid #ddd; border-radius: 4px; }
.product-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 16px; }
.product-card { background: #fff; border-radius: 8px; overflow: hidden; cursor: pointer; transition: box-shadow .2s; }
.product-card:hover { box-shadow: 0 2px 12px rgba(0,0,0,0.12); }
.product-card img { width: 100%; height: 200px; object-fit: cover; }
.product-card .info { padding: 10px; }
.product-card h3 { font-size: 14px; margin-bottom: 4px; }
.sold { font-size: 12px; color: #999; }
.empty { text-align: center; padding: 60px; color: #999; }
@media (max-width: 768px) { .product-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
