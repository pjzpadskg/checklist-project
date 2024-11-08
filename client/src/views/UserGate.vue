<script setup lang="ts">
import ULoginForm from '@/components/ULoginForm.vue'
import URegisterForm from '@/components/URegisterForm.vue'
import { ref } from 'vue'

const isLogin = ref<boolean>(false)
</script>

<template>
  <main class="h-screen w-screen flex flex-row justify-center items-center">
    <div class="flex flex-col w-1/3 items-center">
      <img src="/checkme.gif" class="w-64 py-8" />
      <h1 class="text-3xl text-center">CheckMe</h1>
      <p class="text-sm mt-2 text-center">
        Organize your tasks. <br />
        Real time.
      </p>
    </div>

    <div v-if="isLogin" class="flex flex-col w-1/3 py-8">
      <ULoginForm />
      <p @click="isLogin = !isLogin" class="text-center hover:cursor-pointer">
        Don't have an account yet? <strong>Register</strong> here.
      </p>
    </div>

    <div v-else class="flex flex-col w-1/3">
      <URegisterForm v-if="!isLogin" />
      <p @click="isLogin = !isLogin" class="text-center hover:cursor-pointer">
        Already have an account? <strong>Login</strong> here.
      </p>
    </div>
  </main>
</template>
