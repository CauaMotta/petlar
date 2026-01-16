import { api } from './api'

export const registerRequest = async (data: RegisterPayload) => {
  const response = await api.post('/api/users/cadastrar', data)
  return response.data
}
