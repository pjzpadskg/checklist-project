import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Gate',
      component: () => import('@/views/UserGate.vue'),
    },
    {
      path: '/home',
      name: 'Home',
      component: () => import('@/views/UserBoard.vue'),
    },
  ],
})

export default router
