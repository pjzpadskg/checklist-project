<script setup lang="ts">
import { ref } from 'vue'
import { fetchApi, ssoConfig } from '@/utils/Helpers'
import { decodeCredential } from 'vue3-google-login'
import { useUserStore } from '@/stores/user.ts'

const userStore = useUserStore()

const payload = ref({ email: '', password: '', name: '' })
const { execute, error, data } = fetchApi('/users').post(payload)
async function register(): void {
  // await execute()
  if (!error.value) {
    // make a personal group for user
    // create group
  } else {
    console.log(error.value)
  }
  payload.value = { email: '', password: '', name: '' }
}

async function googleRegister(response: any): void {
  try {
    const { email, name, sub } = decodeCredential(response.credential)
    payload.value = { email, password: sub, name }
    await register()
  } catch (err) {
    payload.value = { email: '', password: '', name: '' }
  }
}
</script>

<template>
  <h2 class="text-2xl font-semibold text-center">Register</h2>
  <form @submit.prevent="register">
    <div class="flex flex-col px-4 mb-4">
      <label for="email"> Email </label>
      <input
        v-model="payload.email"
        type="email"
        id="email"
        class="border-2 border-black rounded-lg h-8 p-2"
        required
        autofocus
      />
    </div>

    <div class="flex flex-col px-4 mb-4">
      <label for="pass"> Password </label>
      <input
        v-model="payload.pass"
        type="password"
        id="pass"
        class="border-2 border-black rounded-lg h-8 p-2"
        required
      />
    </div>

    <div class="flex flex-col px-4 mb-4">
      <label for="name"> Name </label>
      <input
        v-model="payload.name"
        id="name"
        class="border-2 border-black rounded-lg h-8 p-2"
        required
      />
    </div>

    <br />
    <div class="flex flex-col px-4">
      <button class="bg-gray-200 border-2 rounded-lg border-black mx-4 p-1">Register</button>
    </div>
  </form>

  <div class="flex flex-row items-center justify-center my-4">
    <GoogleLogin :callback="googleRegister" :button-config="ssoConfig" />
    <span class="ml-2">Register with Google</span>
  </div>
</template>
