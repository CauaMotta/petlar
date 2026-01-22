import { api } from './api'

export const getRequestsForMyAnimals = async (filter: string) => {
  const response = await api.get<ApiResponse<Adoption>>(
    '/adoptions/me/animals' + filter
  )
  return response.data
}

export const getMyRequests = async (filter: string) => {
  const response = await api.get<ApiResponse<Adoption>>(
    '/adoptions/me/requests' + filter
  )
  return response.data
}

export const statusUpdate = async (id: string, status: string) => {
  const response = await api.patch<Adoption>(`/adoptions/${id}/${status}`)
  return response.data
}

export const initAdoption = async (data: AdoptionRequest) => {
  const response = await api.post<Adoption>('/adoptions', data)
  return response.data
}

export const updateReason = async (id: string, reason: string) => {
  const data = {
    reason
  }
  const response = await api.patch<Adoption>('/adoptions/' + id, data)
  return response.data
}
