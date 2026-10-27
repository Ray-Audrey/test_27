import { defineStore } from 'pinia'
import api from '../api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    username: '',
    checked: false,
    loggedIn: false
  }),
  actions: {
    async checkSession() {
      try {
        const response = await api.get('/auth/me')
        this.username = response.data.username
        this.loggedIn = true
      } catch {
        this.username = ''
        this.loggedIn = false
      } finally {
        this.checked = true
      }
      return this.loggedIn
    },
    async login(username, password) {
      const response = await api.post('/auth/login', { username, password })
      this.username = response.data.username
      this.loggedIn = true
      this.checked = true
    },
    async logout() {
      try {
        await api.post('/auth/logout')
      } finally {
        this.username = ''
        this.loggedIn = false
        this.checked = true
      }
    }
  }
})
