import { createFetch } from '@vueuse/core'
import { ref } from 'vue'

export const fetchApi = createFetch({
  baseUrl: import.meta.env.VITE_BASE_URL,
  fetchOptions: {
    credentials: 'include',
  },
  options: {
    immediate: false,
  },
})

export function validateTitle(): boolean {
  return false
}

export const ssoConfig = ref({
  type: `icon`,
  theme: `filled_black`,
  size: `large`,
  shape: `pill`,
})
