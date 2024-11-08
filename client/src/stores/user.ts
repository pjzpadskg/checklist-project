import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useStorage } from '@vueuse/core'

export const useUserStore = defineStore('user', () => {
  const name = useStorage<string>('name', '')
  const isAuth = useStorage<boolean>('isAuth', false)

  async function createGroup(name: string, owner: string) {}

  async function addMember(idGroup: string, emailUser: string) {}

  async function removeMember(idGroup: string, emailUser: string) {}

  return { name, isAuth, createGroup, addMember, removeMember }
})
