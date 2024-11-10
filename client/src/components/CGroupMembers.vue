<script setup lang="ts">
import { useUserStore } from '@/stores/user.ts'

const userStore = useUserStore()
</script>

<template>
  <!-- Members Modal -->
  <div class="modal flex-col">
    <div class="text-right">
      <span @click="userStore.isMembers = !userStore.isMembers">❌</span>
    </div>
    <div class="modal-content text-center px-8 py-4">
      <h2>Members</h2>
      <ul class="mx-px space-y-2">
        <li
          v-for="(members, index) in userStore.currentMembers"
          :key="index"
          class="hover:cursor-pointer hover:bg-blue-100 hover:text-black mx-2"
        >
          <pre class="text-xl p-2">{{ members }}</pre>
        </li>
      </ul>
    </div>
  </div>
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

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
