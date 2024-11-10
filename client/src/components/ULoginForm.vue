<script setup lang="ts">
import { ref } from 'vue'
import { fetchApi, ssoConfig } from '@/utils/Helpers'
import { decodeCredential } from 'vue3-google-login'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.ts'

const userStore = useUserStore()
const router = useRouter()

// When Authenticated
// 1. Get all groups associated with user
// 2. Make current view = “Personal Tasks”
// 3. Get all tasks for “Personal Tasks”
// 4. Go to User Board View

const payload = ref({ email: '', password: '' })
const { isFetching, execute, error, data } = fetchApi('/users/sessions').post(payload)
async function login() {
  await execute()
  if (!error.value) {
    userStore.name = JSON.parse(data.value).name
    userStore.isAuth = true
    payload.value = { email: '', password: '' }
    /* 1 */ await userStore.getGroups()
    /* 2 */ userStore.currentView =
      userStore.groups[userStore.groups.findIndex((g) => g.name === 'Personal Tasks')]
    /* 3 */ // userStore.getTasks()
    /* 4 */ router.push('/home')
  } else {
    console.log(error.value)
  }
  payload.value.password = ''
}

async function googleLogin(response: { credential: string }): void {
  try {
    const { email, sub } = decodeCredential(response.credential)
    payload.value = { email, password: sub }
    await login()
  } catch (err) {
    console.log(err)
    payload.value = { email: '', password: '' }
  }
}
</script>

<template>
  <h2 class="text-3xl mb-4 font-semibold text-center">Login</h2>
  <h3 v-if="isFetching" class="text-center mb-4">Hang on, we're setting things up for you.</h3>
  <h3 v-if="error && !payload.password" class="text-l text-red-500 font-semibold text-center mb-4">
    Incorrect email/password.
  </h3>
  <form @submit.prevent="login">
    <div class="flex flex-col px-4 mb-4">
      <label for="email"> Email </label>
      <input
        v-model="payload.email"
        type="email"
        id="email"
        class="border-2 border-black rounded-lg h-8 p-2"
        placeholder=""
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
