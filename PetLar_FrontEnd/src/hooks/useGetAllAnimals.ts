import { useQuery } from '@tanstack/react-query'
import axios, { type AxiosPromise } from 'axios'

import { API_URL } from '../main'

const fetchData = async (filter?: string): AxiosPromise<ApiResponse> => {
  const response = await axios.get<ApiResponse>(
    API_URL + '/api/animals' + filter
  )
  return response
}

const buildQueryString = (filter?: Filter) => {
  if (!filter) return ''

  const params = new URLSearchParams()

  if (filter.status) params.append('status', filter.status)
  if (filter.type) params.append('type', filter.type)

  const query = params.toString()
  return query ? `?${query}` : ''
}

export function useGetAllAnimals(filter: Filter) {
  const query = useQuery({
    queryFn: () => fetchData(buildQueryString(filter)),
    queryKey: ['animals-data', filter]
  })

  return {
    ...query,
    data: query.data?.data.content ?? []
  }
}
