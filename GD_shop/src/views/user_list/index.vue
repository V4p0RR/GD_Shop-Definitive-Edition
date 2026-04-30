<template>
  <div class="user-list-container">
    <div class="page-header">
      <div class="page-title">
        <h2>用户管理</h2>
        <p>管理平台注册用户</p>
      </div>
    </div>

    <t-card class="filter-card">
      <div class="filter-toolbar">
        <t-input v-model="searchKeyword" placeholder="搜索昵称/手机号" clearable class="search-input" @keyup.enter="fetchUsers">
          <template #prefix-icon><t-icon name="search" /></template>
        </t-input>
        <t-button theme="primary" @click="fetchUsers">查询</t-button>
      </div>
    </t-card>

    <t-card class="table-card">
      <t-table :data="users" :columns="columns" stripe bordered hover row-key="id" :pagination="pagination" @page-change="handlePageChange">
        <template #role="{ row }">
          <t-tag :theme="row.role === 1 ? 'danger' : row.role === 2 ? 'warning' : 'default'" variant="light" shape="round">
            {{ row.role === 1 ? '超级管理员' : row.role === 2 ? '运营' : '普通用户' }}
          </t-tag>
        </template>
        <template #createTime="{ row }"> {{ formatTime(row.createTime) }} </template>
        <template #op="{ row }">
          <t-space size="small">
            <t-button v-if="row.id !== 1" theme="default" variant="text" size="small" @click="toggleRole(row)">
              {{ row.role === 1 ? '降为运营' : row.role === 2 ? '降为用户' : '升为运营' }}
            </t-button>
            <t-button v-if="row.id !== 1" theme="danger" variant="text" size="small" @click="deleteUser(row)">删除</t-button>
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

const searchKeyword = ref("")
const users = ref([])

const pagination = ref({ current: 1, pageSize: 10, total: 0 })

const columns = [
  { colKey: "id", title: "ID", width: 80 },
  { colKey: "nickName", title: "昵称", width: 140 },
  { colKey: "phone", title: "手机号", width: 160 },
  { colKey: "role", title: "角色", width: 120, cell: "role" },
  { colKey: "createTime", title: "注册时间", width: 180, cell: "createTime" },
  { colKey: "op", title: "操作", width: 200, cell: "op", fixed: "right" },
]

const fetchUsers = async () => {
  try {
    const params = { current: pagination.value.current }
    if (searchKeyword.value) params.keyword = searchKeyword.value
    const res = await http.get("/user/admin/list", params)
    if (res.data) users.value = res.data
    if (res.total !== undefined) pagination.value.total = res.total
  } catch (e) { /* handled */ }
}

const handlePageChange = (pi) => {
  pagination.value.current = pi.current
  fetchUsers()
}

const formatTime = (t) => {
  if (!t) return ""
  return t.replace("T", " ").substring(0, 19)
}

const toggleRole = async (row) => {
  // 管理员→运营→用户 循环
  const newRole = row.role === 1 ? 2 : row.role === 2 ? 0 : 1
  const labels = { 0: '普通用户', 1: '超级管理员', 2: '运营' }
  DialogPlugin.confirm({
    header: "确认操作？",
    body: `将 ${row.nickName} 的角色改为"${labels[newRole]}"？`,
    onConfirm: async () => {
      try {
        await http.put(`/user/${row.id}/role`, { role: newRole })
        MessagePlugin.success("已更新")
        fetchUsers()
      } catch (e) { /* handled */ }
    },
  })
}

const deleteUser = (row) => {
  DialogPlugin.confirm({
    header: "确认删除？",
    body: `删除用户 ${row.nickName}（${row.phone}）后将不可恢复`,
    theme: "warning",
    confirmBtn: { theme: "danger" },
    onConfirm: async () => {
      try {
        await http.delete(`/user/${row.id}`)
        MessagePlugin.success("已删除")
        fetchUsers()
      } catch (e) { /* handled */ }
    },
  })
}

onMounted(() => fetchUsers())
</script>

<style scoped>
.user-list-container { padding: 0 0 24px 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.page-title h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.page-title p { margin: 8px 0 0; color: var(--td-text-color-secondary); font-size: 15px; }
.filter-card { margin-bottom: 20px; border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
.filter-toolbar { display: flex; align-items: center; padding: 16px 20px; gap: 16px; }
.search-input { width: 280px; }
.table-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); overflow: hidden; }
</style>
