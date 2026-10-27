import { createRouter, createWebHistory } from 'vue-router'
import Login from './views/Login.vue'
import Dashboard from './views/Dashboard.vue'
import { useAuthStore } from './stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login },
    { path: '/', component: Dashboard, meta: { requiresAuth: true } }
  ]
})

router.beforeEach(async to => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth) {
    const valid = await auth.checkSession()
    if (!valid) return '/login'
  }
  if (to.path === '/login' && auth.isLoggedIn) {
    const valid = await auth.checkSession()
    if (valid) return '/'
  }
})
export default router
