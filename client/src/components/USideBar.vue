<script setup lang="ts">
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user.ts'
import { validate } from '@/utils/Helpers.ts'

const userStore = useUserStore()

const showAddModal = ref(false)
const checklistName = ref('')
const checklistErr = ref(false)
const disabled = computed(() => !validate(checklistName.value))
const loading = ref(false)

async function onChecklistAdd() {
  loading.value = true
  const successful = await userStore.createGroup(checklistName.value)
  if (successful) {
    checklistErr.value = false
    await userStore.getGroups()
    showAddModal.value = !showAddModal.value
  } else {
    checklistErr.value = true
  }
  checklistName.value = ''
  loading.value = false
}
</script>

<template>
  <div
    class="'transition-all duration-300 border-r-2 border-purple-900 rounded-r-lg bg-black"
    :class="[userStore.isSidebar ? 'w-80' : 'w-0 overflow-hidden']"
  >
    <!-- Name and Logo -->
    <div class="flex flex-row justify-center items-center h-16 mb-2">
      <img src="/checkme.gif" class="w-10" />
      <span class="ml-2 text-2xl font-bold text-white"> CheckMe </span>
    </div>

    <!-- Checklists -->
    <div class="flex flex-row border-b-2 border-gray-600 mx-2 mb-2 justify-between items-center">
      <span class="text-gray-300 text-l mx-3">Checklists</span>
      <span
        @click="showAddModal = !showAddModal"
        class="text-gray-300 text-xl text-right mr-4 hover:cursor-pointer"
        >+</span
      >
    </div>
    <ul class="mx-px space-y-2">
      <li
        v-for="group in userStore.groups"
        :key="group.id"
        @click="userStore.currentView = group"
        class="hover:cursor-pointer hover:bg-blue-100 text-white hover:text-black mx-2"
      >
        <span class="text-xl p-2">{{ group.name }}</span>
      </li>
    </ul>
  </div>

  <!-- Adding Checklist Modal -->
  <Teleport to="body">
    <div v-if="showAddModal" class="modal flex-col align-center justify-center items-center">
      <div class="modal-content text-center pb-6">
        <div class="text-right pr-1 pt-1 mb-1">
          <span
            class="hover:cursor-pointer"
            @click="
              showAddModal = !showAddModal
              checklistName = ``
              checklistErr = false
            "
          >
            ❌
          </span>
        </div>
        <h3 class="text-xl font-semibold text-center py-2 px-16">Create a New Checklist</h3>
        <div class="flex-col mt-2 px-6">
          <form @submit.prevent="onChecklistAdd" class="flex flex-col">
            <p
              v-if="checklistErr && !checklistName"
              class="text-l text-red-500 font-semibold text-center"
            >
              This checklist name already exists.
            </p>
            <p v-if="loading" class="text-l text-black text-center">
              Hang on, we're setting things up for you.
            </p>
            <label for="checklistId" class="text-left px-1 font-semibold">Checklist Name</label>
            <input
              v-model="checklistName"
              id="checklistId"
              class="border-black border-2 rounded-lg mb-4 px-2"
              required
              autofocus
            />
            <button
              @click="logout"
              :disabled="disabled"
              :class="[disabled ? 'bg-gray-500' : 'bg-blue-500']"
              class="text-white px-4 py-2 rounded-xl mx-12"
            >
              Create
            </button>
          </form>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  background-color: rgba(0, 0, 0, 0.8);
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
