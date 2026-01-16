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

declare type User = {
  id: string
  name: string
  email: string
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

declare type ApiResponse = {
  content: Animal[]
  totalPages: number
  totalElements: number
  number: number
  size: number
  first: boolean
  last: boolean
}

declare type CreateAnimal = {
  name: string
  age: number
  breed: string
  sex: string
  weight: number | null
  size: string
  description?: string
  urlImage?: string
  author: string
  phone: string
}
