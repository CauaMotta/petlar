/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from 'react'

import { apiRequest } from '../services/api'

type ApiResponse = {
  content: Animal[]
  totalPages: number
  totalElements: number
  number: number
  size: number
  first: boolean
  last: boolean
}

export function useApi<T extends ApiResponse | Animal = ApiResponse>(
  endpoint: string
) {
  const [data, setData] = useState<T extends ApiResponse ? Animal[] : Animal>(
    [] as unknown as T extends ApiResponse ? Animal[] : Animal
  )
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let active = true

    const fetchData = async () => {
      setData([] as unknown as T extends ApiResponse ? Animal[] : Animal)
      setLoading(true)
      setError(null)

      try {
        const response = await apiRequest<T>(endpoint)

        await new Promise((resolve) => setTimeout(resolve, 300))

        if (active) {
          if ('content' in (response as ApiResponse)) {
            setData((response as ApiResponse).content as any)
          } else {
            setData(response as any)
          }
        }
      } catch (err: any) {
        if (active) {
          setError(err.message || 'Erro ao buscar dados')
        }
      } finally {
        if (active) {
          setLoading(false)
        }
      }
    }

    fetchData()

    return () => {
      active = false
    }
  }, [endpoint])

  return { data, loading, error }
}
