/* eslint-disable @typescript-eslint/no-explicit-any */
import { useMutation, useQuery } from '@tanstack/react-query'
import Cookies from 'js-cookie'
import { useDispatch } from 'react-redux'
import type { AxiosResponse } from 'axios'

import { logout, setToken, setUser } from '../store/reducers/authSlice'

import type { ApiError } from '../types/errorType'
import {
  deleteAccount,
  registerRequest,
  updateUser,
  getMeRequest,
  changePassword
} from '../services/userService'

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

export const useUpdateUser = () => {
  const dispatch = useDispatch()

  return useMutation<User, ApiError, User>({
    mutationFn: async (credentials: User) => {
      const response: AxiosResponse = await updateUser(credentials)

      const newToken = response.headers['authorization']

      if (newToken) {
        const cleanToken = newToken.replace('Bearer ', '')
        dispatch(setToken(cleanToken))
      }

      return response.data
    },
    onSuccess: (data) => {
      dispatch(setUser(data))
    }
  })
}

export const useChangePassword = () => {
  return useMutation<any, ApiError, changePasswordPayload>({
    mutationFn: changePassword
  })
}

export const useDeleteUser = () => {
  const dispatch = useDispatch()

  return useMutation<AxiosResponse, ApiError>({
    mutationFn: deleteAccount,
    onSuccess: (data) => {
      if (data.status === 204) dispatch(logout())
    }
  })
}
