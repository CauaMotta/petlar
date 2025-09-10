declare type Animal = {
  id: string
  name: string
  age: number
  type: string
  breed: string
  sex: string
  weight: number
  size: string
  registrationDate: string
  status: string
  description?: string
  urlImage?: string
  author: string
  phone: string
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
