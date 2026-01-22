import { api } from './api'

export const getAllAnimals = async (filter: string) => {
  const response = await api.get<ApiResponse<Animal>>('/animals' + filter)
  return response.data
}

export const getAnimalById = async (id: string) => {
  const response = await api.get<Animal>('/animals/' + id)
  return response.data
}

export const getMyAnimals = async (filter: string) => {
  const response = await api.get<ApiResponse<Animal>>('/animals/my' + filter)
  return response.data
}

export const postAnimal = async (formData: FormData) => {
  const response = await api.post('/animals', formData)
  return response.data
}

export const putAnimal = async (id: string, formData: FormData) => {
  const response = await api.put('/animals/' + id, formData)
  return response.data
}

export const deleteAnimal = async (id: string) => {
  const response = await api.delete('/animals/' + id)
  return response
}
