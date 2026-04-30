<template>
  <div class="shop-marketing-container">
    <div class="page-header">
      <div class="page-title">
        <h2>秒杀活动管理</h2>
        <p>创建和管理限时秒杀活动</p>
      </div>
      <div class="header-actions">
        <t-button theme="primary" @click="showCreateDialog">
          <template #icon><t-icon name="add-circle" /></template>
          新建活动
        </t-button>
      </div>
    </div>

    <t-card class="table-card">
      <t-table :data="activities" :columns="columns" stripe bordered hover row-key="id">
        <template #status="{ row }">
          <t-tag :theme="getStatusTheme(row)" variant="light" shape="round">
            {{ getStatusText(row) }}
          </t-tag>
        </template>
        <template #seckillPrice="{ row }"> ¥{{ (row.seckillPrice / 100).toFixed(2) }} </template>
        <template #beginTime="{ row }"> {{ formatTime(row.beginTime) }} </template>
        <template #endTime="{ row }"> {{ formatTime(row.endTime) }} </template>
        <template #op="{ row }">
          <t-space size="small">
            <t-button theme="danger" variant="text" size="small" @click="deleteActivity(row)">删除</t-button>
          </t-space>
        </template>
      </t-table>
    </t-card>

    <!-- 创建活动弹窗 -->
    <t-dialog v-model:visible="dialogVisible" header="新建秒杀活动" width="550px" @confirm="createActivity">
      <t-form :data="form" labelWidth="100px">
        <t-form-item label="活动名称" name="spuName">
          <t-input v-model="form.spuName" placeholder="请输入活动名称" />
        </t-form-item>
        <t-form-item label="SKU ID" name="skuId">
          <t-input-number v-model="form.skuId" placeholder="参与秒杀的SKU ID" :min="1" />
        </t-form-item>
        <t-form-item label="SPU ID" name="spuId">
          <t-input-number v-model="form.spuId" placeholder="对应SPU ID" :min="1" />
        </t-form-item>
        <t-form-item label="秒杀价格(分)" name="seckillPrice">
          <t-input-number v-model="form.seckillPrice" placeholder="秒杀价格，单位：分" :min="0" />
        </t-form-item>
        <t-form-item label="秒杀库存" name="stock">
          <t-input-number v-model="form.stock" placeholder="秒杀活动库存" :min="1" />
        </t-form-item>
        <t-form-item label="开始时间" name="beginTime">
          <t-date-picker v-model="form.beginTime" enable-time-picker placeholder="选择开始时间" />
        </t-form-item>
        <t-form-item label="结束时间" name="endTime">
          <t-date-picker v-model="form.endTime" enable-time-picker placeholder="选择结束时间" />
        </t-form-item>
      </t-form>
    </t-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { MessagePlugin, DialogPlugin } from "tdesign-vue-next"
import http from "@/request/api/http"

const activities = ref([])
const dialogVisible = ref(false)
const form = ref({ spuName: "", skuId: undefined, spuId: undefined, seckillPrice: 0, stock: 0, beginTime: "", endTime: "" })

const columns = [
  { colKey: "id", title: "ID", width: 80 },
  { colKey: "spuName", title: "活动名称", width: 180 },
  { colKey: "skuId", title: "SKU ID", width: 100 },
  { colKey: "spuId", title: "SPU ID", width: 100 },
  { colKey: "seckillPrice", title: "秒杀价", width: 110, cell: "seckillPrice" },
  { colKey: "stock", title: "库存", width: 70 },
  { colKey: "beginTime", title: "开始时间", width: 170, cell: "beginTime" },
  { colKey: "endTime", title: "结束时间", width: 170, cell: "endTime" },
  { colKey: "status", title: "状态", width: 100, cell: "status" },
  { colKey: "op", title: "操作", width: 80, cell: "op" },
]

const formatTime = (t) => {
  if (!t) return ""
  return t.replace("T", " ").substring(0, 19)
}

const getStatusText = (row) => {
  const now = Date.now()
  const start = row.beginTime ? new Date(row.beginTime).getTime() : 0
  const end = row.endTime ? new Date(row.endTime).getTime() : 0
  if (now < start) return "未开始"
  if (now >= start && now <= end) return "进行中"
  return "已结束"
}

const getStatusTheme = (row) => {
  const text = getStatusText(row)
  if (text === "进行中") return "success"
  if (text === "未开始") return "warning"
  return "default"
}

const fetchActivities = async () => {
  try {
    const res = await http.get("/seckill/list")
    if (res.data) activities.value = res.data
  } catch (e) { /* handled */ }
}

const showCreateDialog = () => {
  form.value = { spuName: "", skuId: undefined, spuId: undefined, seckillPrice: 0, stock: 0, beginTime: "", endTime: "" }
  dialogVisible.value = true
}

const createActivity = async () => {
  if (!form.value.spuName || !form.value.skuId || !form.value.spuId || !form.value.seckillPrice || !form.value.stock) {
    MessagePlugin.warning("请填写完整信息")
    return
  }
  try {
    await http.post("/seckill/activity", form.value)
    MessagePlugin.success("创建成功")
    dialogVisible.value = false
    fetchActivities()
  } catch (e) { /* handled */ }
}

const deleteActivity = (row) => {
  DialogPlugin.confirm({
    header: "确认删除？",
    body: `删除活动 ${row.spuName} 后将不可恢复`,
    theme: "warning",
    confirmBtn: { theme: "danger" },
    onConfirm: async () => {
      try {
        await http.delete(`/seckill/activity/${row.id}`)
        MessagePlugin.success("已删除")
        fetchActivities()
      } catch (e) { /* handled */ }
    },
  })
}

onMounted(() => fetchActivities())
</script>

<style scoped>
.shop-marketing-container { padding: 0 0 24px 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.page-title h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.page-title p { margin: 8px 0 0; color: var(--td-text-color-secondary); font-size: 15px; }
.table-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); overflow: hidden; }
</style>
