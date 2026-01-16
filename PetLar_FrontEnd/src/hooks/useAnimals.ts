import { useQuery } from '@tanstack/react-query'

import { getAllAnimals, getAnimalById } from '../services/animalService'

type Filter = {
  status?: string
  type?: string
}

const buildQueryString = (filter?: Filter) => {
  if (!filter) return ''

  const params = new URLSearchParams()

  if (filter.status) params.append('status', filter.status)
  if (filter.type) params.append('type', filter.type)

  const query = params.toString()
  return query ? `?${query}` : ''
}

export const useGetAllAnimals = (filter: Filter) => {
  const query = useQuery({
    queryFn: () => getAllAnimals(buildQueryString(filter)),
    queryKey: ['animals-data', filter],
    refetchInterval: 60 * 5 * 1000
  })

  return {
    ...query,
    data: query.data?.content ?? []
  }
}

export const useGetAnimalById = (id: string) => {
  const query = useQuery({
    queryFn: () => getAnimalById(id),
    queryKey: ['animals-data', id]
  })

  return {
    ...query,
    data: query.data ?? null
  }
}
