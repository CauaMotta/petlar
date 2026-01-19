import { api } from './api'

export const registerRequest = async (data: RegisterPayload) => {
  const response = await api.post('/users/cadastrar', data)
  return response.data
}
