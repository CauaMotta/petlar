import { api } from './api'

export const getAllAnimals = async (filter: string) => {
  const response = await api.get<ApiResponse>('/animals' + filter)
  return response.data
}

export const getAnimalById = async (id: string) => {
  const response = await api.get<Animal>('/animals/' + id)
  return response.data
}

export const postAnimal = async (formData: FormData) => {
  const response = await api.post('/animals', formData)
  return response.data
}
