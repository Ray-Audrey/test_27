import { defineStore } from 'pinia'
import api from '../api'

export const useAuthStore = defineStore('auth', {
  state: () => ({ username: sessionStorage.getItem('username') || '' }),
  getters: { isLoggedIn: state => Boolean(state.username) },
  actions: {
    async login(username, password) {
      const { data } = await api.post('/auth/login', { username, password })
      this.username = data.username
      sessionStorage.setItem('username', data.username)
    },
    async checkSession() {
      try {
        const { data } = await api.get('/auth/me')
        this.username = data.username
        sessionStorage.setItem('username', data.username)
        return true
      } catch {
        this.username = ''
        sessionStorage.removeItem('username')
        return false
      }
    },
    async logout() {
      try { await api.post('/auth/logout') } finally {
        this.username = ''
        sessionStorage.removeItem('username')
      }
    }
  }
})
