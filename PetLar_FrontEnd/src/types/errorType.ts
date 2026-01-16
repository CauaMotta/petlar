import type { AxiosError } from 'axios'

type ErrorResponse = {
  timestamp: string
  path: string
  status: number
  message: string
}

export type ApiError = AxiosError<ErrorResponse>
