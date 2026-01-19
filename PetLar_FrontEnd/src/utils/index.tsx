export const formatWeight = (weight: number) => {
  return weight / 1000
}

export const formatDateBr = (dateString: string) => {
  if (dateString) {
    const [year, month, day] = dateString.split('-')
    return `${day}/${month}/${year}`
  }
}

export const formatDateIso = (dateString: string) => {
  if (dateString) {
    const [day, month, year] = dateString.split('/')
    return `${year}-${month}-${day}`
  }
}
