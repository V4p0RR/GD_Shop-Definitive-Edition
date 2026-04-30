<template>
  <div class="order-list-container">
    <div class="page-header">
      <div class="page-title">
        <h2>全部订单</h2>
        <p>查看和管理所有客户订单</p>
      </div>
    </div>

    <t-card class="filter-card">
      <div class="filter-toolbar">
        <t-select v-model="statusFilter" placeholder="订单状态" clearable class="filter-select">
          <t-option :value="undefined" label="全部状态" />
          <t-option :value="0" label="待付款" />
          <t-option :value="1" label="待发货" />
          <t-option :value="2" label="已发货" />
          <t-option :value="3" label="已完成" />
          <t-option :value="5" label="已退款" />
          <t-option :value="6" label="退款中" />
        </t-select>
        <t-button theme="primary" @click="fetchOrders">查询</t-button>
      </div>
    </t-card>

    <t-card class="table-card">
      <t-table
        :data="orders"
        :columns="columns"
        stripe bordered hover
        row-key="id"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #status="{ row }">
          <t-tag :theme="statusTheme[row.status]" variant="light" shape="round">
            {{ statusText[row.status] }}
          </t-tag>
        </template>
        <template #totalAmount="{ row }"> ¥{{ (row.totalAmount / 100).toFixed(2) }} </template>
        <template #createTime="{ row }"> {{ formatTime(row.createTime) }} </template>
        <template #op="{ row }">
          <t-space size="small">
            <t-button theme="primary" variant="text" size="small" @click="viewDetail(row)">查看</t-button>
            <t-button v-if="row.status === 1" theme="success" variant="text" size="small" @click="handleShip(row)">发货</t-button>
            <t-button v-if="[1, 2].includes(row.status)" theme="danger" variant="text" size="small" @click="handleRefund(row)">退款</t-button>
          </t-space>
        </template>
      </t-table>
    </t-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { MessagePlugin, DialogPlugin } from "tdesign-vue-next"
import http from "@/request/api/http"

const statusFilter = ref(undefined)
const orders = ref([])

const statusText = { 0: "待付款", 1: "待发货", 2: "已发货", 3: "已完成", 4: "已取消", 5: "已退款", 6: "退款中" }
const statusTheme = { 0: "warning", 1: "primary", 2: "default", 3: "success", 4: "default", 5: "danger", 6: "warning" }

const columns = [
  { colKey: "id", title: "订单号", width: 180 },
  { colKey: "receiver", title: "收货人", width: 100 },
  { colKey: "phone", title: "电话", width: 140 },
  { colKey: "totalAmount", title: "金额", width: 120, cell: "totalAmount" },
  { colKey: "status", title: "状态", width: 100, cell: "status" },
  { colKey: "createTime", title: "下单时间", width: 180, cell: "createTime" },
  { colKey: "op", title: "操作", width: 180, cell: "op", fixed: "right" },
]

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
})

const fetchOrders = async () => {
  try {
    const params = { current: pagination.value.current }
    if (statusFilter.value !== undefined && statusFilter.value !== null) {
      params.status = statusFilter.value
    }
    const res = await http.get("/order/admin/list", params)
    if (res.data) {
      orders.value = res.data
    }
    if (res.total !== undefined && res.total !== null) {
      pagination.value.total = res.total
    }
  } catch (e) { /* handled by interceptor */ }
}

const handlePageChange = (pageInfo) => {
  pagination.value.current = pageInfo.current
  fetchOrders()
}

const formatTime = (t) => {
  if (!t) return ""
  return t.replace("T", " ").substring(0, 19)
}

const viewDetail = (row) => {
  MessagePlugin.info(`订单 ${row.id} - 收货人: ${row.receiver}, 地址: ${row.address}`)
}

const handleShip = (row) => {
  DialogPlugin.confirm({
    header: "确认发货？",
    body: `确认将订单 ${row.id} 标记为已发货？`,
    onConfirm: async () => {
      try {
        await http.put(`/order/ship/${row.id}`)
        MessagePlugin.success("发货成功")
        fetchOrders()
      } catch (e) { /* handled */ }
    },
  })
}

const handleRefund = (row) => {
  DialogPlugin.confirm({
    header: "确认退款？",
    body: `确认为订单 ${row.id} 办理退款？`,
    theme: "warning",
    confirmBtn: { theme: "danger" },
    onConfirm: async () => {
      try {
        await http.put(`/order/refund/${row.id}`)
        MessagePlugin.success("退款成功")
        fetchOrders()
      } catch (e) { /* handled */ }
    },
  })
}

onMounted(() => fetchOrders())
</script>

<style scoped>
.order-list-container { padding: 0 0 24px 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.page-title h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.page-title p { margin: 8px 0 0; color: var(--td-text-color-secondary); font-size: 15px; }
.filter-card { margin-bottom: 20px; border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
.filter-toolbar { display: flex; align-items: center; padding: 16px 20px; gap: 16px; }
.filter-select { width: 200px; }
.table-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); overflow: hidden; }
</style>
