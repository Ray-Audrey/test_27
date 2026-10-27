<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const username = ref('admin')
const password = ref('password123')
const error = ref('')
const loading = ref(false)
const auth = useAuthStore()
const router = useRouter()

async function submit() {
  error.value = ''
  loading.value = true
  try {
    await auth.login(username.value, password.value)
    router.push('/')
  } catch {
    error.value = 'Login failed. Check your username and password, and make sure the backend is running.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="login-wrap">
    <form class="panel login-panel" @submit.prevent="submit">
      <p class="eyebrow">STUDENT SERVICES</p>
      <h1>Scholarship Manager</h1>
      <p class="muted">Sign in to manage students and scholarship selections.</p>
      <label>Username</label>
      <input v-model="username" required autocomplete="username" />
      <label>Password</label>
      <input v-model="password" type="password" required autocomplete="current-password" />
      <p v-if="error" class="error">{{ error }}</p>
      <button class="primary full" :disabled="loading">{{ loading ? 'Signing in…' : 'Sign in' }}</button>
      <p class="hint">Demo: admin / password123</p>
    </form>
  </main>
</template>
