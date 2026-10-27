import { createRouter, createWebHistory } from 'vue-router'
import Login from './views/Login.vue'
import Dashboard from './views/Dashboard.vue'
import { useAuthStore } from './stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login, meta: { guestOnly: true } },
    { path: '/', component: Dashboard, meta: { requiresAuth: true } }
  ]
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()
  if (!auth.checked) await auth.checkSession()

  if (to.meta.requiresAuth && !auth.loggedIn) return '/login'
  if (to.meta.guestOnly && auth.loggedIn) return '/'
})

export default router
