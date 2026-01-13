import { useQuery } from '@tanstack/react-query'
import axios, { type AxiosPromise } from 'axios'

import { API_URL } from '../main'

const fetchData = async (filter?: string): AxiosPromise<ApiResponse> => {
  const response = await axios.get<ApiResponse>(
    API_URL + '/api/animals' + (filter ? filter : '')
  )
  return response
}

export function useAnimalsApi(filter?: string) {
  const query = useQuery({
    queryFn: () => fetchData(filter),
    queryKey: ['animals-data', filter]
  })

  return {
    ...query,
    data: query.data?.data.content
  }
}
