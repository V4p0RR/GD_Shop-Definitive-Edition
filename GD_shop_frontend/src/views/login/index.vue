<template>
  <div class="login-page">
    <div class="login-card">
      <h2>贵大电商 - 登录</h2>
      <div class="form">
        <input v-model="phone" placeholder="手机号" class="input" />
        <div class="tab-btns">
          <span :class="{ active: mode === 'code' }" @click="mode = 'code'">验证码登录</span>
          <span :class="{ active: mode === 'password' }" @click="mode = 'password'">密码登录</span>
        </div>
        <input v-if="mode === 'code'" v-model="code" placeholder="验证码" class="input" />
        <button v-if="mode === 'code'" class="btn btn-default" @click="sendCode" :disabled="countdown > 0">
          {{ countdown > 0 ? countdown + 's' : '发送验证码' }}
        </button>
        <input v-if="mode === 'password'" v-model="password" type="password" placeholder="密码" class="input" />
        <button class="btn btn-primary login-btn" @click="handleLogin">登录</button>
        <p class="msg" v-if="msg">{{ msg }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import request from '../../utils/request'

const router = useRouter()
const auth = useAuthStore()
const phone = ref('')
const code = ref('')
const password = ref('')
const mode = ref('code')
const countdown = ref(0)
const msg = ref('')

async function sendCode() {
  if (!phone.value) { msg.value = '请输入手机号'; return }
  try {
    await request.post('/user/code?phone=' + phone.value)
    countdown.value = 60
    const timer = setInterval(() => { countdown.value--; if (countdown.value <= 0) clearInterval(timer) }, 1000)
    msg.value = '验证码已发送'
  } catch (e) { msg.value = '发送失败' }
}

async function handleLogin() {
  try {
    await auth.login(phone.value,
      mode.value === 'code' ? code.value : null,
      mode.value === 'password' ? password.value : null)
    router.push('/')
  } catch (e) { msg.value = e.message || '登录失败' }
}
</script>

<style scoped>
.login-page { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f5f5f5; }
.login-card { background: #fff; padding: 40px; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.1); width: 380px; }
.login-card h2 { text-align: center; margin-bottom: 24px; color: #e4393c; }
.form { display: flex; flex-direction: column; gap: 12px; }
.input { padding: 10px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
.tab-btns { display: flex; gap: 16px; font-size: 13px; }
.tab-btns span { cursor: pointer; color: #999; }
.tab-btns span.active { color: #e4393c; font-weight: bold; }
.login-btn { width: 100%; padding: 12px; font-size: 16px; }
.msg { color: #e4393c; font-size: 13px; text-align: center; }
</style>
