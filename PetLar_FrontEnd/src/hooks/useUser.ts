import { useMutation, useQuery } from '@tanstack/react-query'
import Cookies from 'js-cookie'

import { getMeRequest } from '../services/authService'

import type { ApiError } from '../types/errorType'
import { registerRequest } from '../services/userService'

export const useRegisterUser = () => {
  return useMutation<User, ApiError, RegisterPayload>({
    mutationFn: registerRequest
  })
}

export const useGetMe = () => {
  const token = Cookies.get('token')

  const query = useQuery({
    queryFn: getMeRequest,
    queryKey: ['user-data'],
    enabled: !!token,
    retry: false
  })

  return {
    ...query,
    data: query.data ?? null
  }
}
