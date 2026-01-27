/* eslint-disable @typescript-eslint/no-explicit-any */
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import {
  deleteAnimal,
  getAllAnimals,
  getAnimalById,
  getMyAnimals,
  postAnimal,
  putAnimal
} from '../services/animalService'
import { buildQueryString } from '../utils'

import type { ApiError } from '../types/errorType'

export const useGetAllAnimals = (filter: Filter) => {
  const query = useQuery({
    queryFn: () => getAllAnimals(buildQueryString(filter)),
    queryKey: ['animals-data', filter],
    refetchInterval: 60 * 5 * 1000
  })

  return {
    ...query,
    data: query.data?.content ?? [],
    totalPages: query.data?.totalPages
  }
}

export const useGetMyAnimals = (filter: Filter) => {
  const query = useQuery({
    queryFn: () => getMyAnimals(buildQueryString(filter)),
    queryKey: ['my-animals-data', filter],
    refetchInterval: 60 * 5 * 1000
  })

  return {
    ...query,
    data: query.data?.content ?? [],
    totalPages: query.data?.totalPages
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
  const queryClient = useQueryClient()

  return useMutation<any, ApiError, FormData>({
    mutationFn: postAnimal,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['animals-data']
      })
      queryClient.invalidateQueries({ queryKey: ['my-animals-data'] })
    }
  })
}

export const useUpdateAnimal = () => {
  const queryClient = useQueryClient()

  return useMutation<Animal, ApiError, { id: string; formData: FormData }>({
    mutationFn: ({ id, formData }) => putAnimal(id, formData),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['animals-data']
      })
      queryClient.invalidateQueries({ queryKey: ['my-animals-data'] })
    }
  })
}

export const useDeleteAnimal = () => {
  const queryClient = useQueryClient()

  return useMutation<any, ApiError, string>({
    mutationFn: deleteAnimal,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['animals-data']
      })
      queryClient.invalidateQueries({ queryKey: ['my-animals-data'] })
    }
  })
}
