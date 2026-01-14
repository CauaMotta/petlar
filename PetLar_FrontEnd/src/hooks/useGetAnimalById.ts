import { useQuery } from '@tanstack/react-query'
import axios, { type AxiosPromise } from 'axios'

import { API_URL } from '../main'

type Filter = {
  id: string
}

const fetchData = async (filter?: string): AxiosPromise<Animal> => {
  const response = await axios.get<Animal>(API_URL + '/api/animals/' + filter)
  return response
}

export function useGetAnimalById(filter: Filter) {
  const query = useQuery({
    queryFn: () => fetchData(filter.id),
    queryKey: ['animals-data', filter.id]
  })

  return {
    ...query,
    data: query.data?.data ?? null
  }
}
