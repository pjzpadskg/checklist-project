<script setup lang="ts">
import { fetchApi } from '@/utils/Helpers'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.ts'

const userStore = useUserStore()
const router = useRouter()

const { execute, error } = fetchApi('/users/sessions').delete()
async function logout() {
  await execute()
  if (!error.value) {
    userStore.reset()
    router.push('/')
  } else {
    console.log(error.value)
  }
}
</script>

<template>
  <div class="modal-content text-center px-8 py-4">
    <h3 class="text-xl font-semibold text-center">Are you sure you want to logout?</h3>
    <div class="flex flex-row mt-4 justify-center">
      <button @click="logout" class="text-white px-4 py-2 bg-red-500 rounded-xl">Logout</button>
    </div>
  </div>
</template>

<style scoped>
.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
