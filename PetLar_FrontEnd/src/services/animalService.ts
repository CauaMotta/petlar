import { api } from './api'

export const getAllAnimals = async (filter: string) => {
  const response = await api.get<ApiResponse>('/api/animals' + filter)
  return response.data
}

export const getAnimalById = async (id: string) => {
  const response = await api.get<Animal>('/api/animals/' + id)
  return response.data
}
