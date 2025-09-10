/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState } from 'react'
import { apiRequest } from '../services/api'

type MutationOptions<T> = {
  method: 'POST' | 'PUT'
  endpoint: string
  body: T
}

export function useApiMutation<TBody = unknown, TResponse = unknown>() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [data, setData] = useState<TResponse | null>(null)

  const mutate = async (options: MutationOptions<TBody>) => {
    setLoading(true)
    setError(null)
    setData(null)

    try {
      const response = await apiRequest<TResponse>(options.endpoint, {
        method: options.method,
        body: options.body
      })

      await new Promise((resolve) => setTimeout(resolve, 300))

      setData(response)
      return response
    } catch (err: any) {
      setError(err.message || 'Erro na requisição')
      throw err
    } finally {
      setLoading(false)
    }
  }

  return { mutate, data, loading, error }
}
