/* eslint-disable @typescript-eslint/no-explicit-any */
import { useMutation, useQuery } from '@tanstack/react-query'

import {
  getAllAnimals,
  getAnimalById,
  postAnimal
} from '../services/animalService'

import type { ApiError } from '../types/errorType'

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

export const usePostAnimal = () => {
  return useMutation<any, ApiError, FormData>({
    mutationFn: postAnimal,
    onSuccess: () => console.log('Animal registrado com sucesso'),
    onError: (error) => console.error(error)
  })
}
