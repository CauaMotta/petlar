import { useMutation } from '@tanstack/react-query'
import { useDispatch } from 'react-redux'
import Cookies from 'js-cookie'

import { loginRequest } from '../services/authService'
import { getMeRequest } from '../services/userService'
import { loginSuccess } from '../store/reducers/authSlice'

import type { ApiError } from '../types/errorType'

export const useLogin = () => {
  const dispatch = useDispatch()

  return useMutation<LoginResponse, ApiError, LoginPayload>({
    mutationFn: async (credentials: LoginPayload) => {
      const { token } = await loginRequest(credentials)

      Cookies.set('token', token)

      const user = await getMeRequest()

      return { token, user }
    },
    onSuccess: (data) => dispatch(loginSuccess(data)),
    onError: () => Cookies.remove('token')
  })
}
