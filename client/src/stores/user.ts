import { ref, watch } from 'vue'
import { defineStore } from 'pinia'
import { useStorage } from '@vueuse/core'
import { fetchApi } from '@/utils/Helpers.ts'
import type { Group, Member, Invite, Task } from '@/utils/Types.ts'

export const useUserStore = defineStore('user', () => {
  const name = useStorage<string>('name', '')
  const isAuth = useStorage<boolean>('isAuth', false)
  const isSidebar = ref(true)
  const isMembers = ref(false)
  const currentView = useStorage<Group | {}>('currentView', {})
  const currentMembers = useStorage<Member[]>('currentMembers', [])
  const groups = useStorage<Group[]>('groups', [])
  const invites = useStorage<Invite[]>('invites', [])
  const tasks = useStorage<Task[]>('tasks', [])

  function reset() {
    name.value = ''
    isAuth.value = false
    currentView.value = {}
    groups.value = []
    tasks.value = []
  }

  // adding a member to the group
  const pLodAdd = ref({ idGroup: '', emailUser: '' })
  const { execute: addExe, error: addErr } = fetchApi(`/members`).post(pLodAdd)
  async function addMember(idGroup: string, emailUser?: string) {
    pLodAdd.value = { idGroup, emailUser }
    await addExe()
    if (addErr.value) console.log(addErr.value)
  }

  // creating a group -- also adds a member for successful creation
  const pLodCreate = ref({ name: '', owner: '' })
  const {
    execute: createExe,
    error: createErr,
    data: createData,
  } = fetchApi(`/groups`).post(pLodCreate)
  async function createGroup(name: string, owner?: string): boolean {
    pLodCreate.value = { name, owner }
    await createExe()
    if (!createErr.value) {
      const idGroup = JSON.parse(createData.value).id
      await addMember(idGroup, owner)
      return true
    } else {
      console.log(createErr.value)
      return false
    }
  }

  // get all groups associated by user
  // should only be called when user is authenticated
  const { execute: getgExe, error: getgErr, data: getgData } = fetchApi(`/groups`).get()
  async function getGroups() {
    await getgExe()
    if (!getgErr.value) {
      groups.value = JSON.parse(getgData.value)
    } else {
      console.log(getgErr.value)
    }
  }

  // get members of a given group id
  // should only be called when navigating other groups besides personal
  const {
    execute: getmExe,
    error: getmErr,
    data: getmData,
  } = fetchApi(`/members/${currentView.value.id}`)
  async function getMembersOfGroup(): void {
    await getmExe()
    if (!getmErr.value) {
      currentMembers.value = JSON.parse(getmData.value)
    } else {
      console.log(getmErr.value)
    }
  }

  watch(
    () => currentView.value.name,
    async (newView, oldView) => {
      if (isAuth.value) {
        if (newView !== 'Personal Tasks') {
          await getMembersOfGroup()
        } else {
          currentMembers.value = {}
        }
      }
    },
  )

  // get all tasks based on the current view
  // should only be called when current group exists
  const {
    execute: gettExe,
    error: gettErr,
    data: gettData,
  } = fetchApi(`/tasks/${currentView.value.id}`).get()
  async function getTasks(): void {
    await gettExe()
    if (!gettErr.value) {
      tasks.value = JSON.parse(gettData.value)
    } else {
      console.log(gettErr.value)
    }
  }

  // delete member in a group based on the current view
  // should only be called for owner of group
  const pLoadRem = ref({ id: '', emailUser: '' })
  const { execute: remExe, error: remErr } = fetchApi(`/members`).delete(pLoadRem)
  async function removeMember(emailUser: string) {
    pLoadRem.value = { id: currentView.value.id, emailUser }
    await remExe()
    if (remErr.value) console.log(remErr.value)
  }

  return {
    name,
    isAuth,
    isSidebar,
    isMembers,
    currentView,
    groups,
    invites,
    tasks,
    currentMembers,
    reset,
    createGroup,
    addMember,
    getGroups,
    getTasks,
    removeMember,
    getMembersOfGroup,
  }
})
