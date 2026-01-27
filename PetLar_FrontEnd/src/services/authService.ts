import { api } from './api'

export const loginRequest = async (data: LoginPayload) => {
  const response = await api.post('/login', data)
  return response.data
}
