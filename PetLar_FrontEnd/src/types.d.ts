// Login

declare type LoginPayload = {
  email: string
  password: string
}

declare type LoginResponse = {
  token: string
  user: User
}

// User

declare type RegisterPayload = {
  name: string
  email: string
  password: string
}

declare type User = {
  id?: string
  name: string
  email: string
}

type changePasswordPayload = {
  password: string
}

// Adoption

declare type Adoption = {
  id: string
  status: string
  animal: Animal
  animalOwner: Author
  adopter: Author
  reason: string
}

// Animal

declare type Author = {
  id: string
  name: string
}

declare type Animal = {
  id: string
  name: string
  birthDate: string
  weight: number
  type: string
  sex: string
  size: string
  status: string
  author: Author
  imagePath?: string
  description?: string
}

declare type ApiResponse<T extends Animal | Adoption> = {
  content: T[]
  totalPages: number
  totalElements: number
  number: number
  size: number
  first: boolean
  last: boolean
}

// Filter

type Filter = {
  status?: string
  type?: string
  page?: number
  size?: number
}
