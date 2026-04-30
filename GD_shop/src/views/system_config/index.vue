<template>
  <div class="system-settings-container">
    <div class="page-header">
      <div class="page-title">
        <h2>系统设置</h2>
        <p>店铺基础配置</p>
      </div>
      <div class="header-actions">
        <t-button theme="primary" @click="handleSave" :loading="saving">
          <template #icon><t-icon name="save" /></template>
          保存设置
        </t-button>
      </div>
    </div>

    <div class="settings-groups">
      <t-card title="店铺基础信息" class="setting-card">
        <t-form :data="config" label-align="top">
          <t-row :gutter="24">
            <t-col :span="6">
              <t-form-item label="店铺名称">
                <t-input v-model="config.shopName" placeholder="请输入店铺名称" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="Logo URL">
                <t-input v-model="config.logo" placeholder="Logo图片URL" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="客服电话">
                <t-input v-model="config.servicePhone" placeholder="请输入客服电话" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="店铺公告">
                <t-textarea v-model="config.announcement" placeholder="店铺顶部公告内容" :autosize="{ minRows: 2 }" />
              </t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-card>

      <t-card title="支付设置" class="setting-card">
        <t-form :data="config">
          <t-row :gutter="24">
            <t-col :span="6">
              <t-form-item label="微信支付">
                <t-switch v-model="config.wechatPay" :custom-value="[1, 0]" size="large" />
              </t-form-item>
            </t-col>
            <t-col :span="6">
              <t-form-item label="支付宝">
                <t-switch v-model="config.alipay" :custom-value="[1, 0]" size="large" />
              </t-form-item>
            </t-col>
          </t-row>
        </t-form>
      </t-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { MessagePlugin } from "tdesign-vue-next"
import http from "@/request/api/http"

const config = ref({
  shopName: '',
  logo: '',
  servicePhone: '',
  announcement: '',
  wechatPay: 1,
  alipay: 1,
})
const saving = ref(false)

onMounted(async () => {
  try {
    const res = await http.get("/config")
    if (res.data) config.value = res.data
  } catch (e) { /* handled */ }
})

const handleSave = async () => {
  saving.value = true
  try {
    await http.put("/config", config.value)
    MessagePlugin.success("设置已保存")
  } catch (e) { /* handled */ }
  finally { saving.value = false }
}
</script>

<style scoped>
.system-settings-container { padding: 0 0 40px 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px; padding: 16px 0; border-bottom: 1px dashed var(--td-component-stroke); }
.page-title h2 { margin: 0; font-size: 24px; font-weight: 600; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.page-title p { margin: 8px 0 0; color: var(--td-text-color-secondary); font-size: 15px; }
.header-actions { display: flex; gap: 16px; }
.settings-groups { display: flex; flex-direction: column; gap: 24px; }
.setting-card { border-radius: 12px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
@media (max-width: 1200px) { .t-row { flex-direction: column; } .t-col { width: 100% !important; } }
</style>
