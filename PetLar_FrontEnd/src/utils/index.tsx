export const formatWeight = (weight: number) => {
  return weight / 1000
}

export const formatDate = (dateString: string) => {
  if (dateString) {
    const [year, month, day] = dateString.split('-')
    return `${day}/${month}/${year}`
  }
}
