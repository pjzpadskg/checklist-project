<script setup lang="ts">
import { ref } from 'vue'
import { fetchApi, ssoConfig } from '@/utils/Helpers'
import { decodeCredential } from 'vue3-google-login'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.ts'

const userStore = useUserStore()

const payload = ref({ email: '', password: '' })
const { execute, error, data } = fetchApi('/users/sessions').post(payload)
async function login() {
  // await execute()
  if (!error.value) {
    // userStore.name = JSON.parse(data.value).name
    // userStore.isAuth = true
    // fetch personal group
    payload.value = { email: '', password: '' }
    useRouter().push('/home')
  } else {
    console.log(error.value)
  }
}

async function googleLogin(response: any): void {
  try {
    const { email, sub } = decodeCredential(response.credential)
    payload.value = { email, password: sub }
    await login()
  } catch (err) {
    payload.value = { email: '', password: '' }
  }
}
</script>

<template>
  <h2 class="text-2xl font-semibold text-center">Login</h2>
  <form @submit.prevent="login">
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

    <br />
    <div class="flex flex-col px-4">
      <button class="bg-gray-200 border-2 rounded-lg border-black mx-4 p-1">Login</button>
    </div>
  </form>

  <div class="flex flex-row items-center justify-center my-4">
    <GoogleLogin :callback="googleLogin" :button-config="ssoConfig" />
    <span class="ml-2">Login with Google</span>
  </div>
</template>
