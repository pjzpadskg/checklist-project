<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.ts'
import ULogoutModal from '@/components/ULogoutModal.vue'
import CGroupMembers from '@/components/CGroupMembers.vue'
const isLoggingOut = ref(false)

const userStore = useUserStore()
const router = useRouter()

onMounted(() => {
  if (!userStore.isAuth) {
    userStore.reset()
    router.push('/')
  }
})
</script>

<template>
  <!-- Top Nav -->
  <div class="flex flex-row items-center justify-between w-full h-16 mb-2 bg-white">
    <div class="flex flex-row items-center ml-4">
      <img
        src="/menu.png"
        @click="userStore.isSidebar = !userStore.isSidebar"
        :class="[userStore.isSidebar ? 'border-black bg-gray-300' : 'border-purple-900']"
        class="w-10 mx-2 cursor-pointer border-2 rounded-md"
      />
      <img v-if="!userStore.isSidebar" src="/checkme.gif" class="w-10" />
      <span class="text-xl font-semibold ml-4">{{ userStore.currentView.name }}</span>
      <img
        src="/group.jpg"
        v-if="userStore.currentView.name !== `Personal Tasks`"
        @click="userStore.isMembers = !userStore.isMembers"
        class="ml-4 w-8 hover:cursor-pointer"
      />
    </div>

    <div class="mr-8">
      <RouterLink to="/notifications" class="hover:cursor-pointer mr-4 mt-2"> 🔔 </RouterLink>
      <span @click="isLoggingOut = !isLoggingOut" class="hover:cursor-pointer mt-2"> Logout </span>
    </div>
  </div>

  <!-- Logout Modal -->
  <div
    v-if="isLoggingOut"
    @click="isLoggingOut = !isLoggingOut"
    class="modal flex-col cursor-pointer"
  >
    <span class="text-xl text-white"> Click elsewhere to cancel. </span>
    <ULogoutModal />
  </div>

  <!-- Group Members List -->

  <CGroupMembers v-if="userStore.isMembers" />
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.8);
  z-index: 1000;
}
</style>
