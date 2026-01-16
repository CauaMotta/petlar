import { api } from './api'

export const loginRequest = async (data: LoginPayload) => {
  const response = await api.post('/api/login', data)
  return response.data
}

export const getMeRequest = async () => {
  const response = await api.get<User>('/api/users/me')
  return response.data
}
