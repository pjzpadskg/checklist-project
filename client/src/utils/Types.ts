export interface User {
  email: string
  name: string
}

export interface Task {
  createdAt: number
  taskId: string
  parentId: string | undefined | null
  groupName: string
  taskName: string
  taskDescription?: string
  taskDuration?: string
  completed: boolean
  subtasks?: Task[]
}

export interface Group {
  id: string
  name: string
  ownerId: string
  ownerName: string
}

export interface Member {
  idGroup: string
  emailuser: string
  viewing: boolean
}

export interface Invite {
  id: string
  created: number
  receiver: string
  idGroup: string
  nameGroup: string
  owner: string
}
