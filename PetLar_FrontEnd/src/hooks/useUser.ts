import { useQuery } from '@tanstack/react-query'
import { getMeRequest } from '../services/authService'

export const useGetMe = () => {
  const query = useQuery({
    queryFn: getMeRequest,
    queryKey: ['user-data']
  })

  return {
    ...query,
    data: query.data ?? null
  }
}
