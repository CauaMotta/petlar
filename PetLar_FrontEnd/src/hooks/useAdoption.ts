import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import {
  getMyRequests,
  getRequestsForMyAnimals,
  initAdoption,
  statusUpdate,
  updateReason
} from '../services/adoptionService'
import { buildQueryString } from '../utils'

import type { ApiError } from '../types/errorType'

export const useGetRequestsForMyAnimals = (filter: Filter) => {
  const query = useQuery({
    queryFn: () => getRequestsForMyAnimals(buildQueryString(filter)),
    queryKey: ['requests', filter],
    refetchInterval: 60 * 5 * 1000
  })

  return {
    ...query,
    data: query.data?.content ?? [],
    totalPages: query.data?.totalPages
  }
}

export const useGetMyRequests = (filter: Filter) => {
  const query = useQuery({
    queryFn: () => getMyRequests(buildQueryString(filter)),
    queryKey: ['my-requests', filter],
    refetchInterval: 60 * 5 * 1000
  })

  return {
    ...query,
    data: query.data?.content ?? [],
    totalPages: query.data?.totalPages
  }
}

export const useStatusUpdate = () => {
  const queryClient = useQueryClient()

  return useMutation<Adoption, ApiError, { id: string; status: string }>({
    mutationFn: ({ id, status }) => statusUpdate(id, status),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['animals-data']
      })
      queryClient.invalidateQueries({ queryKey: ['my-animals-data'] })
      queryClient.invalidateQueries({ queryKey: ['requests'] })
      queryClient.invalidateQueries({ queryKey: ['my-requests'] })
    }
  })
}

export const useInitAdoption = () => {
  const queryClient = useQueryClient()

  return useMutation<Adoption, ApiError, AdoptionRequest>({
    mutationFn: initAdoption,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['animals-data']
      })
      queryClient.invalidateQueries({ queryKey: ['my-animals-data'] })
      queryClient.invalidateQueries({ queryKey: ['requests'] })
      queryClient.invalidateQueries({ queryKey: ['my-requests'] })
    }
  })
}

export const useEditReason = () => {
  const queryClient = useQueryClient()

  return useMutation<Adoption, ApiError, { id: string; reason: string }>({
    mutationFn: ({ id, reason }) => updateReason(id, reason),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['my-requests'] })
    }
  })
}
