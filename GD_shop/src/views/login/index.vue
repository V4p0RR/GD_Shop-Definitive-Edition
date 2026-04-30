<template>
  <div class="auth-bg">
    <div class="auth-card">
      <aside class="auth-hero">
        <div class="hero-badge">
          <svg xmlns="http://www.w3.org/2000/svg" width="452" height="447">
            <image href="../../../public/logo.png" width="100%" height="100%" />
          </svg>
        </div>
        <h1 class="hero-title">贵大电商</h1>
        <p class="hero-sub">商家管理平台</p>
        <ul class="hero-features">
          <li><t-icon name="check-circle-filled" /> 店铺管理</li>
          <li><t-icon name="check-circle-filled" /> 发布营销活动</li>
          <li><t-icon name="check-circle-filled" /> 管理您的数据与权限</li>
        </ul>
        <div class="hero-blob"></div>
      </aside>

      <section class="auth-form">
        <header class="form-header">
          <div class="title">欢迎登录</div>
          <div class="subtitle">使用您的账号开始工作</div>
        </header>

        <div v-if="isLoggedIn" class="logged-in-panel">
          <div class="logged-in-head">
            <div class="logged-in-avatar">
              <t-icon name="user-circle" />
            </div>
            <div class="logged-in-texts">
              <div class="logged-in-title">已登录</div>
              <div class="logged-in-sub">
                当前账号：<span class="strong">{{ currentUsername || "已登录用户" }}</span>
              </div>
            </div>
          </div>
          <div class="logged-in-actions">
            <t-button theme="primary" size="large" @click="enterHome">
              进入首页
              <template #suffix><t-icon name="arrow-right" /></template>
            </t-button>
            <t-button theme="default" variant="outline" size="large" @click="switchAccount">
              切换账号
            </t-button>
          </div>
        </div>

        <t-form
          v-else
          layout="vertical"
          preventSubmitDefault
          showErrorMessage
          labelAlign="top"
          :data="form"
          :rules="rules"
          @submit="onSubmit"
        >
          <t-form-item label="手机号" name="phone">
            <t-input v-model="form.phone" placeholder="请输入手机号" size="large" clearable>
              <template #prefix-icon><t-icon name="mobile" /></template>
            </t-input>
          </t-form-item>

          <t-form-item :label="loginMode === 'code' ? '验证码' : '密码'" name="password">
            <div class="captcha-row">
              <t-input
                v-model="form.password"
                :placeholder="loginMode === 'code' ? '请输入验证码' : '请输入密码'"
                size="large"
                clearable
              >
                <template #prefix-icon><t-icon name="lock-on" /></template>
              </t-input>
              <t-button
                v-if="loginMode === 'code'"
                theme="primary"
                variant="outline"
                size="large"
                :disabled="smsCountdown > 0"
                @click="sendSmsCode"
                class="sms-btn"
              >
                {{ smsCountdown > 0 ? smsCountdown + 's' : '获取验证码' }}
              </t-button>
            </div>
          </t-form-item>

          <t-form-item>
            <t-button variant="text" @click="toggleLoginMode">
              {{ loginMode === 'code' ? '使用密码登录' : '使用验证码登录' }}
            </t-button>
          </t-form-item>

          <t-form-item>
            <t-button
              class="submit-btn"
              theme="primary"
              size="large"
              type="submit"
              :loading="loading"
              block
            >
              立即登录
            </t-button>
          </t-form-item>
        </t-form>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue"
import { useRouter } from "vue-router"
import { MessagePlugin, DialogPlugin } from "tdesign-vue-next"
import { useAuthStore } from "@/stores/auth"
import { useUserStore } from "@/stores/user"
import http from "@/request/api/http"

const router = useRouter()
const authStore = useAuthStore()
const userStore = useUserStore()

const loading = ref(false)
const isLoggedIn = ref(false)
const currentUsername = ref("")
const loginMode = ref("code")
const smsCountdown = ref(0)

const enterHome = () => router.push("/admin")
const toggleLoginMode = () => {
  loginMode.value = loginMode.value === "code" ? "password" : "code"
  form.password = ""
}

const switchAccount = () => {
  DialogPlugin.confirm({
    header: "切换账号",
    body: "确认要切换其他账号吗？这将清除当前登录状态。",
    theme: "warning",
    confirmBtn: { content: "确认", theme: "danger" },
    cancelBtn: "取消",
    onConfirm: () => {
      authStore.clear()
      userStore.clear()
      isLoggedIn.value = false
      currentUsername.value = ""
      form.phone = ""
      form.password = ""
      MessagePlugin.success("已切换账号")
    },
  })
}

const rules = {
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "手机号格式错误", trigger: "blur" },
  ],
  password: [
    { required: true, message: loginMode.value === "code" ? "请输入验证码" : "请输入密码", trigger: "blur" },
    { min: loginMode.value === "code" ? 6 : 6, message: "至少 6 位", type: "warning", trigger: "blur" },
  ],
}

const form = reactive({
  phone: "",
  password: "",
})

const sendSmsCode = async () => {
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    MessagePlugin.warning("请先输入正确的手机号")
    return
  }
  try {
    await http.post("/user/code?phone=" + form.phone)
    MessagePlugin.success("验证码已发送")
    smsCountdown.value = 60
    const timer = setInterval(() => {
      smsCountdown.value--
      if (smsCountdown.value <= 0) clearInterval(timer)
    }, 1000)
  } catch (e) {
    // error already handled by interceptor
  }
}

const onSubmit = async (e) => {
  if (e && e.preventDefault) e.preventDefault()

  loading.value = true
  try {
    const loginData = { phone: form.phone }
    if (loginMode.value === "code") {
      loginData.code = form.password
    } else {
      loginData.password = form.password
    }

    const res = await http.post("/user/login", loginData)
    const token = res.data
    if (!token) {
      MessagePlugin.error("登录失败，未获取到token")
      loading.value = false
      return
    }

    authStore.setToken(token)

    // 获取用户信息
    try {
      const userRes = await http.get("/user/me")
      if (userRes.data) {
        authStore.setUsername(userRes.data.nickName || form.phone)
        authStore.setRole(userRes.data.role || 0)
        userStore.setUserinfo(userRes.data)
        currentUsername.value = userRes.data.nickName || form.phone
      }
    } catch (e) {
      // fallback
      authStore.setUsername(form.phone)
      currentUsername.value = form.phone
    }

    // 检查是否为管理员或运营
    if (authStore.role !== 1 && authStore.role !== 2) {
      MessagePlugin.warning("该账号无管理后台权限")
      authStore.clear()
      userStore.clear()
      loading.value = false
      return
    }

    isLoggedIn.value = true
    MessagePlugin.success(`登录成功！欢迎回来，${currentUsername.value}`)
    router.push("/admin")
  } catch (e) {
    // error already handled by interceptor
  }
  loading.value = false
}

onMounted(() => {
  if (authStore.token) {
    isLoggedIn.value = true
    currentUsername.value = authStore.username
  }
})
</script>


<style scoped>
.auth-bg {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 56px 24px;
  background: radial-gradient(
      1200px 700px at 10% 15%,
      var(--td-brand-color-light) 0%,
      transparent 60%
    ),
    radial-gradient(
      900px 500px at 90% 85%,
      var(--td-brand-color-focus) 0%,
      transparent 60%
    ),
    linear-gradient(135deg, var(--td-brand-color-light), var(--td-brand-color));
}

.auth-card {
  width: 100%;
  max-width: 1040px;
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  border-radius: 20px;
  overflow: hidden;
  background: transparent;
  box-shadow: 0 28px 68px rgba(0, 0, 0, 0.12), 0 6px 20px rgba(0, 0, 0, 0.06);
}

.auth-hero {
  position: relative;
  padding: 56px 44px;
  background: red;
  color: var(--td-text-color-anti);
}

.hero-badge {
  width: 476px;
  height: 176px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  margin-bottom: 18px;
  box-shadow: inset 0 0 0 1px
    color-mix(in oklab, var(--td-text-color-anti) 25%, transparent);
}

.hero-title {
  font-size: 38px;
  font-weight: 800;
}

.hero-sub {
  opacity: 0.9;
  margin-bottom: 18px;
}

.hero-features {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  gap: 12px;
}

.hero-features li {
  display: flex;
  align-items: center;
  gap: 8px;
  opacity: 0.96;
}

.hero-blob {
  position: absolute;
  right: -80px;
  bottom: -80px;
  width: 380px;
  height: 380px;
  background: radial-gradient(
    circle at 30% 30%,
    color-mix(in oklab, var(--td-text-color-anti) 35%, transparent),
    transparent 60%
  );
  filter: blur(8px);
  opacity: 0.6;
  pointer-events: none;
}

.auth-form {
  background: color-mix(in srgb, var(--td-bg-color-container) 95%, transparent);
  padding: 44px 40px 36px;
}

.form-header {
  text-align: left;
  margin-bottom: 20px;
}

.title {
  font-size: 24px;
  font-weight: 700;
  color: var(--td-text-color-primary);
}

.subtitle {
  color: var(--td-text-color-secondary);
  margin-top: 2px;
}

:deep(.t-form__label) {
  color: var(--td-text-color-secondary);
  font-weight: 600;
  font-size: 14px;
}

:deep(.t-form--label-top .t-form__label) {
  margin-bottom: 8px;
}

:deep(.t-input__wrap) {
  height: 52px;
  border-radius: 14px;
  background: var(--td-bg-color-secondarycontainer);
  transition: 0.2s;
}

:deep(.t-input__inner) {
  font-size: 16px;
}

:deep(.t-input__wrap:hover) {
  border-color: var(--td-brand-color-hover);
}

:deep(.t-input__wrap.t-is-focused) {
  border-color: var(--td-brand-color);
  box-shadow: 0 0 0 3px var(--td-brand-color-light);
  background: var(--td-bg-color-container);
}

.captcha-row {
  display: grid;
  grid-template-columns: 1fr 140px;
  gap: 16px;
  align-items: center;
}

.sms-btn {
  height: 52px;
  border-radius: 14px;
}

.submit-btn {
  margin-top: 10px;
  height: 54px;
  border-radius: 14px;
  background: linear-gradient(
    135deg,
    var(--td-brand-color),
    var(--td-brand-color-active)
  );
  box-shadow: 0 8px 22px rgba(0, 0, 0, 0.08), 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: 0.18s;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12), 0 4px 10px rgba(0, 0, 0, 0.08);
}

.logged-in-panel {
  background: linear-gradient(
    180deg,
    rgba(var(--td-brand-color-rgb), 0.04),
    rgba(var(--td-brand-color-rgb), 0.02)
  );
  border: 1px solid rgba(var(--td-brand-color-rgb), 0.12);
  border-radius: 14px;
  padding: 14px 16px;
  margin-bottom: 12px;
}

.logged-in-head {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logged-in-avatar {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(var(--td-brand-color-rgb), 0.12);
  color: var(--td-brand-color);
  font-size: 22px;
}

.logged-in-texts {
  line-height: 1.2;
}

.logged-in-title {
  font-weight: 700;
  color: var(--td-text-color-primary);
}

.logged-in-sub {
  color: var(--td-text-color-secondary);
}

.logged-in-sub .strong {
  color: var(--td-text-color-primary);
  font-weight: 600;
}

.logged-in-actions {
  display: flex;
  gap: 10px;
  margin-top: 12px;
}

@media (max-width: 1040px) {
  .auth-card {
    max-width: 880px;
  }
}

@media (max-width: 960px) {
  .auth-card {
    grid-template-columns: 1fr;
    max-width: 720px;
  }
  .auth-hero {
    padding: 36px 28px;
  }
  .auth-form {
    padding: 28px 24px;
  }
}
</style>
