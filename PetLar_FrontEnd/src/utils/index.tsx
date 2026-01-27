export const formatWeight = (weight: number) => {
  return weight / 1000
}

export const formatDateBr = (dateString?: string) => {
  if (!dateString) return null

  const [year, month, day] = dateString.split('-')
  return `${day}/${month}/${year}`
}

export const formatDateIso = (dateString?: string) => {
  if (!dateString) return null

  const [day, month, year] = dateString.split('/')
  return `${year}-${month}-${day}`
}

export const buildQueryString = (filter?: Filter) => {
  if (!filter) return ''

  const params = new URLSearchParams()

  if (filter.status) params.append('status', filter.status)
  if (filter.type) params.append('type', filter.type)
  if (filter.page) params.append('page', filter.page.toString())
  if (filter.size) params.append('size', filter.size.toString())
  if (filter.sort) params.append('sort', filter.sort)

  const query = params.toString()
  return query ? `?${query}` : ''
}
