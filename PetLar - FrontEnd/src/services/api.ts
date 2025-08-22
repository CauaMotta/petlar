const API_URL = 'http://localhost:8080/api'

type RequestOptions = {
  method?: string
  headers?: Record<string, string>
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  body?: any
}

export async function apiRequest<T>(
  endpoint: string,
  options: RequestOptions = {}
): Promise<T> {
  const { method = 'GET', headers = {}, body } = options

  const response = await fetch(`${API_URL}${endpoint}`, {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...headers
    },
    body: body ? JSON.stringify(body) : undefined
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || 'Erro ao fazer a requisição')
  }

  return response.json()
}
