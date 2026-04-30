<template>
  <div class="product-list-container">
    <div class="page-header">
      <div class="page-title">
        <h2>商品管理</h2>
        <p>管理SPU商品</p>
      </div>
      <div class="header-actions">
        <t-button theme="primary" @click="showAddDialog">
          <template #icon><t-icon name="add-circle" /></template>
          添加SPU
        </t-button>
      </div>
    </div>

    <t-card class="filter-card">
      <div class="filter-toolbar">
        <t-input v-model="searchKeyword" placeholder="搜索商品名" clearable class="search-input" @keyup.enter="fetchProducts">
          <template #prefix-icon><t-icon name="search" /></template>
        </t-input>
        <t-select v-model="categoryFilter" placeholder="商品分类" clearable class="filter-select">
          <t-option :value="undefined" label="全部分类" />
          <t-option v-for="cat in categories" :key="cat.id" :value="cat.id" :label="cat.name" />
        </t-select>
        <t-button theme="primary" @click="fetchProducts">查询</t-button>
      </div>
    </t-card>

    <t-card class="table-card">
      <t-table :data="products" :columns="columns" stripe bordered hover row-key="id" :pagination="pagination" @page-change="handlePageChange">
        <template #images="{ row }">
          <img v-if="row.images" :src="row.images.split(',')[0]" style="width:60px;height:60px;object-fit:cover;border-radius:6px" />
        </template>
        <template #status="{ row }">
          <t-tag :theme="row.status === 1 ? 'success' : 'default'" variant="light" shape="round">
            {{ row.status === 1 ? '上架' : '下架' }}
          </t-tag>
        </template>
        <template #sold="{ row }"> {{ row.sold || 0 }} </template>
        <template #createTime="{ row }"> {{ formatTime(row.createTime) }} </template>
        <template #op="{ row }">
          <t-space size="small">
            <t-button theme="primary" variant="text" size="small" @click="manageSkus(row)">SKU</t-button>
            <t-button theme="default" variant="text" size="small" @click="showEditDialog(row)">编辑</t-button>
            <t-button theme="danger" variant="text" size="small" @click="deleteProduct(row)">删除</t-button>
          </t-space>
        </template>
      </t-table>
    </t-card>

    <!-- SPU 编辑弹窗 -->
    <t-dialog v-model:visible="dialogVisible" :header="editingSpu.id ? '编辑SPU' : '添加SPU'" width="600px" @confirm="saveSpu">
      <t-form :data="editingSpu" labelWidth="80px">
        <t-form-item label="商品名称" name="name">
          <t-input v-model="editingSpu.name" placeholder="请输入商品名称" />
        </t-form-item>
        <t-form-item label="商品描述" name="description">
          <t-textarea v-model="editingSpu.description" placeholder="请输入商品描述" />
        </t-form-item>
        <t-form-item label="分类" name="categoryId">
          <t-select v-model="editingSpu.categoryId" placeholder="选择分类">
            <t-option v-for="cat in categories" :key="cat.id" :value="cat.id" :label="cat.name" />
          </t-select>
        </t-form-item>
        <t-form-item label="商品图片" name="images">
          <t-input v-model="editingSpu.images" placeholder="图片URL，多张逗号分隔" />
        </t-form-item>
        <t-form-item label="上架状态" name="status">
          <t-switch v-model="editingSpu.status" :custom-value="[1, 0]" />
        </t-form-item>
      </t-form>
    </t-dialog>

    <!-- SKU 管理弹窗 -->
    <t-dialog v-model:visible="skuDialogVisible" :header="`管理SKU - ${currentSpu.name}`" width="800px">
      <div class="sku-toolbar">
        <t-button theme="primary" size="small" @click="showSkuAddDialog">
          <template #icon><t-icon name="add" /></template>
          添加SKU
        </t-button>
      </div>
      <t-table :data="skus" :columns="skuColumns" stripe bordered hover row-key="id" size="small">
        <template #image="{ row }">
          <img v-if="row.image" :src="row.image" style="width:40px;height:40px;object-fit:cover;border-radius:4px" />
        </template>
        <template #price="{ row }"> ¥{{ (row.price / 100).toFixed(2) }} </template>
        <template #status="{ row }">
          <t-tag :theme="row.status === 1 ? 'success' : 'default'" variant="light" shape="round" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </t-tag>
        </template>
        <template #skuOp="{ row }">
          <t-space size="small">
            <t-button theme="default" variant="text" size="small" @click="showSkuEditDialog(row)">编辑</t-button>
            <t-button theme="danger" variant="text" size="small" @click="deleteSku(row)">删除</t-button>
          </t-space>
        </template>
      </t-table>
    </t-dialog>

    <!-- SKU 编辑弹窗 -->
    <t-dialog v-model:visible="skuFormVisible" :header="editingSku.id ? '编辑SKU' : '添加SKU'" width="500px" @confirm="saveSku">
      <t-form :data="editingSku" labelWidth="80px">
        <t-form-item label="规格名称" name="name">
          <t-input v-model="editingSku.name" placeholder="如：红色-L码" />
        </t-form-item>
        <t-form-item label="规格参数" name="specs">
          <t-input v-model="editingSku.specs" placeholder="如：颜色:红,尺寸:L" />
        </t-form-item>
        <t-form-item label="价格(分)" name="price">
          <t-input-number v-model="editingSku.price" placeholder="价格，单位：分" :min="0" />
        </t-form-item>
        <t-form-item label="库存" name="stock">
          <t-input-number v-model="editingSku.stock" placeholder="库存数量" :min="0" />
        </t-form-item>
        <t-form-item label="图片URL" name="image">
          <t-input v-model="editingSku.image" placeholder="SKU图片URL" />
        </t-form-item>
        <t-form-item label="启用" name="status">
          <t-switch v-model="editingSku.status" :custom-value="[1, 0]" />
        </t-form-item>
      </t-form>
    </t-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { MessagePlugin, DialogPlugin } from "tdesign-vue-next"
import http from "@/request/api/http"

const searchKeyword = ref("")
const categoryFilter = ref(undefined)
const products = ref([])
const categories = ref([])
const dialogVisible = ref(false)
const editingSpu = ref({ status: 1 })

const skuDialogVisible = ref(false)
const currentSpu = ref({})
const skus = ref([])

const skuFormVisible = ref(false)
const editingSku = ref({ status: 1 })

const pagination = ref({ current: 1, pageSize: 10, total: 0 })

const columns = [
  { colKey: "id", title: "ID", width: 80 },
  { colKey: "images", title: "图片", width: 80, cell: "images" },
  { colKey: "name", title: "商品名称", width: 180 },
  { colKey: "sold", title: "销量", width: 80, cell: "sold" },
  { colKey: "status", title: "状态", width: 80, cell: "status" },
  { colKey: "createTime", title: "创建时间", width: 170, cell: "createTime" },
  { colKey: "op", title: "操作", width: 180, cell: "op", fixed: "right" },
]

const skuColumns = [
  { colKey: "id", title: "ID", width: 80 },
  { colKey: "image", title: "图片", width: 60, cell: "image" },
  { colKey: "name", title: "名称", width: 130 },
  { colKey: "specs", title: "规格", width: 150 },
  { colKey: "price", title: "价格", width: 100, cell: "price" },
  { colKey: "stock", title: "库存", width: 70 },
  { colKey: "status", title: "状态", width: 70, cell: "status" },
  { colKey: "skuOp", title: "操作", width: 140, cell: "skuOp" },
]

const fetchProducts = async () => {
  try {
    const params = { current: pagination.value.current }
    if (categoryFilter.value) params.categoryId = categoryFilter.value
    if (searchKeyword.value) params.keyword = searchKeyword.value
    const res = await http.get("/spu/admin/list", params)
    if (res.data) products.value = res.data
    if (res.total !== undefined) pagination.value.total = res.total
  } catch (e) { /* handled */ }
}

const fetchCategories = async () => {
  try {
    const res = await http.get("/category/list")
    if (res.data) categories.value = res.data
  } catch (e) { /* handled */ }
}

const handlePageChange = (pi) => {
  pagination.value.current = pi.current
  fetchProducts()
}

const formatTime = (t) => {
  if (!t) return ""
  return t.replace("T", " ").substring(0, 19)
}

const showAddDialog = () => {
  editingSpu.value = { name: "", description: "", categoryId: undefined, images: "", status: 1 }
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  editingSpu.value = { ...row, status: row.status ?? 1 }
  dialogVisible.value = true
}

const saveSpu = async () => {
  try {
    if (editingSpu.value.id) {
      await http.put("/spu", editingSpu.value)
    } else {
      await http.post("/spu", editingSpu.value)
    }
    MessagePlugin.success("保存成功")
    dialogVisible.value = false
    fetchProducts()
  } catch (e) { /* handled */ }
}

const deleteProduct = (row) => {
  DialogPlugin.confirm({
    header: "确认删除？",
    body: `删除 ${row.name} 后将不可恢复`,
    theme: "warning",
    confirmBtn: { theme: "danger" },
    onConfirm: async () => {
      try {
        await http.delete(`/spu/${row.id}`)
        MessagePlugin.success("已删除")
        fetchProducts()
      } catch (e) { /* handled */ }
    },
  })
}

const manageSkus = async (row) => {
  currentSpu.value = row
  skuDialogVisible.value = true
  try {
    const res = await http.get(`/sku/list/${row.id}`)
    if (res.data) skus.value = res.data
  } catch (e) { skus.value = [] }
}

const showSkuAddDialog = () => {
  editingSku.value = { spuId: currentSpu.value.id, name: "", specs: "", price: 0, stock: 0, image: "", status: 1 }
  skuFormVisible.value = true
}

const showSkuEditDialog = (row) => {
  editingSku.value = { ...row, status: row.status ?? 1 }
  skuFormVisible.value = true
}

const saveSku = async () => {
  try {
    if (editingSku.value.id) {
      await http.put("/sku", editingSku.value)
    } else {
      await http.post("/sku", editingSku.value)
    }
    MessagePlugin.success("保存成功")
    skuFormVisible.value = false
    manageSkus(currentSpu.value)
  } catch (e) { /* handled */ }
}

const deleteSku = (row) => {
  DialogPlugin.confirm({
    header: "确认删除？",
    body: `删除SKU ${row.name} 后将不可恢复`,
    theme: "warning",
    confirmBtn: { theme: "danger" },
    onConfirm: async () => {
      try {
        await http.delete(`/sku/${row.id}`)
        MessagePlugin.success("已删除")
        manageSkus(currentSpu.value)
      } catch (e) { /* handled */ }
    },
  })
}

onMounted(() => {
  fetchCategories()
  fetchProducts()
})
</script>

<style scoped>
.product-list-container { padding: 0 0 24px 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.page-title h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.page-title p { margin: 8px 0 0; color: var(--td-text-color-secondary); font-size: 15px; }
.filter-card { margin-bottom: 20px; border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
.filter-toolbar { display: flex; align-items: center; padding: 16px 20px; gap: 16px; }
.search-input { width: 280px; }
.filter-select { width: 200px; }
.table-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); overflow: hidden; }
.sku-toolbar { margin-bottom: 12px; }
</style>
