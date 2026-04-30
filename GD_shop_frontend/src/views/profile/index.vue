<template>
  <DefaultLayout>
    <div class="profile">
      <div class="card">
        <h2>{{ auth.user?.nickName }}</h2>
        <p>手机号: {{ auth.user?.phone || '未绑定' }}</p>
        <p>角色: {{ auth.isAdmin() ? '管理员' : '普通用户' }}</p>
      </div>
      <div class="card">
        <h3>收货信息</h3>
        <div class="form-group">
          <label>收货人</label>
          <input v-model="form.defaultReceiver" placeholder="请输入收货人姓名" />
        </div>
        <div class="form-group">
          <label>联系电话</label>
          <input v-model="form.defaultPhone" placeholder="请输入收货电话" />
        </div>
        <div class="form-group">
          <label>收货地址</label>
          <input v-model="form.defaultAddress" placeholder="请输入收货地址" />
        </div>
        <button class="btn btn-primary" @click="saveAddress">保存收货信息</button>
        <span v-if="addressMsg" class="msg">{{ addressMsg }}</span>
      </div>
      <div class="card">
        <h3>修改密码</h3>
        <div class="form-group">
          <label>旧密码</label>
          <input v-model="pwd.oldPassword" type="password" placeholder="请输入旧密码" />
        </div>
        <div class="form-group">
          <label>新密码</label>
          <input v-model="pwd.newPassword" type="password" placeholder="请输入新密码" />
        </div>
        <button class="btn btn-primary" @click="changePassword">修改密码</button>
        <span v-if="pwdMsg" class="msg">{{ pwdMsg }}</span>
      </div>
      <div class="card">
        <button class="btn btn-primary" @click="handleSign">签到</button>
        <span class="sign-count">连续签到: {{ signCount }} 天</span>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import DefaultLayout from '../../components/layout/DefaultLayout.vue'
import { useAuthStore } from '../../stores/auth'
import request from '../../utils/request'

const auth = useAuthStore()
const signCount = ref(0)
const addressMsg = ref('')
const pwdMsg = ref('')

const form = reactive({
  defaultReceiver: '',
  defaultPhone: '',
  defaultAddress: '',
})

const pwd = reactive({
  oldPassword: '',
  newPassword: '',
})

onMounted(async () => {
  try {
    const res = await request.get('/user/sign/count')
    signCount.value = res.data || 0
  } catch (e) { /* ignore */ }
  try {
    const res = await request.get('/user/me')
    if (res.data) {
      form.defaultReceiver = res.data.defaultReceiver || ''
      form.defaultPhone = res.data.defaultPhone || ''
      form.defaultAddress = res.data.defaultAddress || ''
    }
  } catch (e) { /* ignore */ }
})

async function handleSign() {
  try {
    await request.post('/user/sign')
    signCount.value++
  } catch (e) { /* ignore */ }
}

async function saveAddress() {
  try {
    await request.put('/user/address', {
      defaultReceiver: form.defaultReceiver,
      defaultPhone: form.defaultPhone,
      defaultAddress: form.defaultAddress,
    })
    addressMsg.value = '保存成功'
  } catch (e) { addressMsg.value = '保存失败' }
}

async function changePassword() {
  if (!pwd.oldPassword || !pwd.newPassword) {
    pwdMsg.value = '请填写完整'
    return
  }
  try {
    await request.put('/user/password', { oldPassword: pwd.oldPassword, newPassword: pwd.newPassword })
    pwdMsg.value = '密码修改成功'
    pwd.oldPassword = ''
    pwd.newPassword = ''
  } catch (e) { pwdMsg.value = e.message || '密码修改失败' }
}
</script>

<style scoped>
.profile { max-width: 600px; margin: 0 auto; }
.sign-count { margin-left: 16px; color: #e4393c; }
.card h3 { margin-bottom: 12px; font-size: 16px; }
.form-group { margin-bottom: 12px; }
.form-group label { display: block; margin-bottom: 4px; font-size: 13px; color: #666; }
.form-group input { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.msg { margin-left: 12px; font-size: 13px; color: #e4393c; }
.btn { margin-top: 8px; }
</style>
