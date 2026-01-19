import { api } from './api'

export const loginRequest = async (data: LoginPayload) => {
  const response = await api.post('/login', data)
  return response.data
}

export const getMeRequest = async () => {
  const response = await api.get<User>('/users/me')
  return response.data
}
