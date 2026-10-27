import { createRouter, createWebHistory } from 'vue-router'
import EntityView from './views/EntityView.vue'
import ReportsView from './views/ReportsView.vue'

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: EntityView, props: { which: 'main' } },
    { path: '/second', component: EntityView, props: { which: 'second' } },
    { path: '/reports', component: ReportsView },
  ],
})
