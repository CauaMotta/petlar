import { api } from './api'

export const getMeRequest = async () => {
  const response = await api.get<User>('/users/me')
  return response.data
}

export const registerRequest = async (data: RegisterPayload) => {
  const response = await api.post('/users/cadastrar', data)
  return response.data
}

export const updateUser = async (data: User) => {
  const response = await api.put('/users/me', data)
  return response
}

export const changePassword = async (data: changePasswordPayload) => {
  const response = await api.put('/users/me/changePassword', data)
  return response.data
}

export const deleteAccount = async () => {
  const response = await api.delete('/users/me')
  return response
}
