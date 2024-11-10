<script setup lang="ts">
import { ref } from 'vue'
import { fetchApi, ssoConfig } from '@/utils/Helpers'
import { decodeCredential } from 'vue3-google-login'
import { useUserStore } from '@/stores/user.ts'

const success = ref(false)
const userStore = useUserStore()

const payload = ref({ email: '', password: '', name: '' })
const { isFetching, execute, error } = fetchApi('/users').post(payload)
async function register(): void {
  await execute()
  if (!error.value) {
    success.value = true
    userStore.createGroup('Personal Tasks', payload.value.email)
  } else {
    success.value = false
    console.log(error.value)
  }
  payload.value = { email: '', password: '', name: '' }
}

async function googleRegister(response: { credential: string }): void {
  console.log(typeof response)
  try {
    const { email, name, sub } = decodeCredential(response.credential)
    payload.value = { email, password: sub, name }
    await register()
  } catch (err) {
    console.log(err)
    payload.value = { email: '', password: '', name: '' }
  }
}
</script>

<template>
  <h2 class="text-3xl mb-4 font-semibold text-center">Register</h2>
  <h3 v-if="isFetching" class="text-center mb-4">Hang on, we're setting things up for you.</h3>
  <h3
    v-if="!error && !payload.email && success"
    class="text-l text-green-700 font-semibold text-center mb-4"
  >
    Succesfully registered! <br />
    Proceed to Login.
  </h3>
  <h3 v-if="error && !payload.email" class="text-l text-red-500 font-semibold text-center mb-4">
    Email is already registered.
  </h3>
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
        v-model="payload.password"
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
