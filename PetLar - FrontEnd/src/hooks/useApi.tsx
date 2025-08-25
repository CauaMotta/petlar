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

export function useApi(endpoint: string) {
  const [data, setData] = useState<Animal[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let active = true

    const fetchData = async () => {
      setData([])
      setLoading(true)
      setError(null)

      try {
        const response = await apiRequest<ApiResponse>(endpoint)

        await new Promise((resolve) => setTimeout(resolve, 300))

        if (active) {
          setData(response.content)
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
