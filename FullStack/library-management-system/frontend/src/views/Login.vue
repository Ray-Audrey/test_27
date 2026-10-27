<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const username = ref('admin')
const password = ref('admin123')
const error = ref('')
const auth = useAuthStore()
const router = useRouter()

async function submit() {
  error.value = ''
  try {
    await auth.login(username.value, password.value)
    router.push('/')
  } catch {
    error.value = 'Login failed. Check your username and password.'
  }
}
</script>

<template>
  <main class="login-page">
    <form class="login-card" @submit.prevent="submit">
      <div class="book-mark">LM</div>
      <h1>Library Manager</h1>
      <p class="muted">Sign in to manage books</p>
      <label>Username</label>
      <input v-model="username" autocomplete="username" required>
      <label>Password</label>
      <input v-model="password" type="password" autocomplete="current-password" required>
      <p v-if="error" class="error">{{ error }}</p>
      <button class="primary full" type="submit">Sign in</button>
      <p class="hint">Demo: admin / admin123</p>
    </form>
  </main>
</template>
