<template>
  <div class="change-password-container">
    <div class="password-card">
      <div class="card-header">
        <h2>修改密码</h2>
      </div>

      <t-form ref="formRef" :data="formData" :rules="rules" label-align="top" @submit="handleSubmit">
        <t-form-item label="当前密码" name="oldPassword">
          <t-input v-model="formData.oldPassword" type="password" placeholder="请输入当前密码" size="large" clearable>
            <template #prefix-icon><t-icon name="lock-on" /></template>
          </t-input>
        </t-form-item>
        <t-form-item label="新密码" name="newPassword">
          <t-input v-model="formData.newPassword" type="password" placeholder="请输入新密码（至少8位，包含字母和数字）" size="large" clearable>
            <template #prefix-icon><t-icon name="lock-off" /></template>
          </t-input>
        </t-form-item>
        <t-form-item label="确认新密码" name="confirmPassword">
          <t-input v-model="formData.confirmPassword" type="password" placeholder="请再次输入新密码" size="large" clearable>
            <template #prefix-icon><t-icon name="check-circle-filled" /></template>
          </t-input>
        </t-form-item>
        <t-form-item>
          <t-button theme="primary" size="large" block type="submit" :loading="loading" class="submit-btn">
            确认修改
          </t-button>
        </t-form-item>
      </t-form>

      <div v-if="success" class="success-tip">
        <t-icon name="check-circle-filled" class="success-icon" />
        <p>密码修改成功</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import { MessagePlugin } from "tdesign-vue-next"
import http from "@/request/api/http"

const loading = ref(false)
const success = ref(false)
const formRef = ref(null)

const formData = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
})

const rules = {
  oldPassword: [{ required: true, message: "请输入当前密码" }],
  newPassword: [
    { required: true, message: "请输入新密码" },
    { min: 8, message: "新密码至少8位" },
    { validator: (val) => /(?=.*[a-zA-Z])(?=.*\d)/.test(val), message: "需包含字母和数字" },
  ],
  confirmPassword: [
    { required: true, message: "请确认新密码" },
    { validator: (val) => val === formData.newPassword, message: "两次输入的密码不一致" },
  ],
}

const handleSubmit = async ({ validateResult, firstError }) => {
  if (validateResult !== true) {
    MessagePlugin.warning(firstError || "请检查表单内容")
    return
  }
  loading.value = true
  try {
    await http.put("/user/password", {
      oldPassword: formData.oldPassword,
      newPassword: formData.newPassword,
    })
    success.value = true
    MessagePlugin.success("密码修改成功")
    formData.oldPassword = ""
    formData.newPassword = ""
    formData.confirmPassword = ""
  } catch (e) { /* handled by interceptor */ }
  finally { loading.value = false }
}
</script>

<style scoped>
.change-password-container { min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 24px; background: linear-gradient(135deg, var(--td-bg-color-page), rgba(var(--td-brand-color-rgb), 0.05)); }
.password-card { width: 100%; max-width: 480px; background: var(--td-bg-color-container); border-radius: 20px; padding: 40px; box-shadow: 0 20px 60px rgba(0, 0, 0, 0.12); }
.card-header { text-align: center; margin-bottom: 32px; }
.card-header h2 { margin: 0; font-size: 28px; font-weight: 700; background-image: linear-gradient(45deg, var(--td-brand-color), var(--td-brand-color-hover)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.submit-btn { height: 52px; border-radius: 14px; margin-top: 20px; }
.success-tip { text-align: center; margin-top: 32px; padding: 24px; background: var(--td-success-color-light); border-radius: 12px; }
.success-icon { font-size: 48px; color: var(--td-success-color); margin-bottom: 16px; }
.success-tip p { font-size: 18px; color: var(--td-success-color); }
@media (max-width: 768px) { .password-card { padding: 32px 24px; } }
</style>
