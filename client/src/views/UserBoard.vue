<script setup lang="ts">
import { ref } from 'vue'
import USideBar from '@/components/USideBar.vue'
import UNavBar from '@/components/UNavBar.vue'
import { useUserStore } from '@/stores/user.ts'

const userStore = useUserStore()
</script>

<template>
  <div class="flex h-screen">
    <USideBar />
    <div class="flex flex-col w-screen transition-all duration-300">
      <header>
        <UNavBar />
      </header>
      <main>...</main>
    </div>
  </div>
</template>
