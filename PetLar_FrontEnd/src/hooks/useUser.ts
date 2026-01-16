import { useQuery } from '@tanstack/react-query'
import Cookies from 'js-cookie'

import { getMeRequest } from '../services/authService'

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
