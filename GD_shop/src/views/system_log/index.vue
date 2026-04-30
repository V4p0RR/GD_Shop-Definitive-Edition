<template>
  <div class="operation-log-container">
    <div class="page-header">
      <div class="page-title">
        <h2>操作日志</h2>
      </div>
    </div>

    <t-card class="filter-card">
      <div class="filter-toolbar">
        <div class="filter-left">
          <t-input v-model="searchKeyword" placeholder="搜索内容/操作人" clearable class="search-input" @keyup.enter="fetchLogs">
            <template #prefix-icon><t-icon name="search" /></template>
          </t-input>
          <t-select v-model="typeFilter" placeholder="日志类型" clearable class="filter-select">
            <t-option value="all" label="全部类型" />
            <t-option value="admin" label="管理员操作" />
            <t-option value="customer" label="顾客行为" />
            <t-option value="system" label="系统自动" />
          </t-select>
        </div>
        <div class="filter-right">
          <t-button theme="default" @click="handleReset">重置</t-button>
          <t-button theme="primary" @click="fetchLogs">查询</t-button>
        </div>
      </div>
    </t-card>

    <t-card class="log-card">
      <t-timeline v-if="logs.length > 0">
        <t-timeline-item v-for="log in logs" :key="log.id" :label="formatTime(log.createTime)">
          <t-card class="log-item-card" hover>
            <div class="log-header">
              <t-tag :theme="logTypeTheme[log.type]" variant="light" size="small">
                {{ logTypeText[log.type] || log.type }}
              </t-tag>
              <span class="log-operator">{{ log.operator }}</span>
              <span class="log-action">{{ log.action }}</span>
            </div>
            <div class="log-content">{{ log.content }}</div>
            <div class="log-footer" v-if="log.related">
              <t-tag theme="default" variant="light" size="small">关联：{{ log.related }}</t-tag>
            </div>
          </t-card>
        </t-timeline-item>
      </t-timeline>

      <div v-else class="empty-state">
        <p>暂无日志记录</p>
      </div>

      <t-pagination v-if="pagination.total > 0" :total="pagination.total" :page-size="pagination.pageSize" :current="pagination.current" @change="handlePageChange" class="pagination" />
    </t-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import http from "@/request/api/http"

const searchKeyword = ref("")
const typeFilter = ref("all")
const logs = ref([])

const logTypeText = { admin: "管理员操作", customer: "顾客行为", system: "系统自动" }
const logTypeTheme = { admin: "primary", customer: "success", system: "default" }

const pagination = ref({ current: 1, pageSize: 15, total: 0 })

const fetchLogs = async () => {
  try {
    const params = { current: pagination.value.current }
    if (searchKeyword.value) params.keyword = searchKeyword.value
    if (typeFilter.value && typeFilter.value !== "all") params.type = typeFilter.value
    const res = await http.get("/log/admin/list", params)
    if (res.data) logs.value = res.data
    if (res.total !== undefined) pagination.value.total = res.total
  } catch (e) { logs.value = [] }
}

const handlePageChange = (pageInfo) => {
  pagination.value.current = pageInfo.current
  fetchLogs()
}

const handleReset = () => {
  searchKeyword.value = ""
  typeFilter.value = "all"
  fetchLogs()
}

const formatTime = (t) => {
  if (!t) return ""
  return t.replace("T", " ").substring(0, 19)
}

onMounted(() => fetchLogs())
</script>

<style scoped>
.operation-log-container { padding: 0 0 24px 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.page-title h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.filter-card { margin-bottom: 20px; border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
.filter-toolbar { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; flex-wrap: wrap; gap: 16px; }
.filter-left { display: flex; gap: 16px; flex: 1; min-width: 300px; flex-wrap: wrap; }
.search-input { width: 300px; }
.filter-select { width: 200px; }
.filter-right { display: flex; gap: 12px; }
.log-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); padding: 20px; }
.log-item-card { margin-bottom: 8px; }
.log-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.log-operator { font-weight: 600; color: var(--td-text-color-primary); }
.log-action { color: var(--td-text-color-secondary); }
.log-content { color: var(--td-text-color-primary); line-height: 1.6; }
.log-footer { margin-top: 8px; }
.empty-state { padding: 60px 20px; text-align: center; color: var(--td-text-color-secondary); }
.pagination { margin-top: 20px; }
@media (max-width: 1200px) { .filter-left { flex-direction: column; } .search-input, .filter-select { width: 100%; } }
</style>
