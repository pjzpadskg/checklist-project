import { createFetch } from '@vueuse/core'
import { ref } from 'vue'

export const fetchApi = createFetch({
  baseUrl: import.meta.env.VITE_BASE_URL,
  fetchOptions: {
    credentials: 'include',
  },
  options: {
    immediate: false,
  },
})

export function validateTitle(): boolean {
  return false
}

export const ssoConfig = ref({
  type: `icon`,
  theme: `filled_black`,
  size: `large`,
  shape: `pill`,
})

export function validate(input: string): boolean {
  const s = input.trim()
  if (s.length > 0) {
    if (s.length === 1 && /(^[-!.?;,:'"}{)(*&^%$#@~><|/\\=+])/.test(s)) {
      return false
    }
    return true
  } else {
    return false
  }
}

export function buildTasks(tasks: Task[]): Task[] {
  const taskMap: { [taskId: string]: Task } = {}
  tasks.forEach((task) => {
    task.subtasks = []
    taskMap[task.taskId] = task
  })

  const rootTasks: Task[] = []

  tasks.forEach((task) => {
    if (!task.parentId) {
      rootTasks.push(task)
    } else {
      const parentTask = taskMap[task.parentId]
      if (parentTask) {
        parentTask.subtasks?.push(task)
      }
    }
  })

  return rootTasks
}
