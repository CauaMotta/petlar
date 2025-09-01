export const formatWeight = (weight: number) => {
  return weight / 100
}

export const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  })
}

export const formatAge = (ageInMonths: number) => {
  if (ageInMonths < 12) {
    return `${ageInMonths} ${ageInMonths === 1 ? 'mês' : 'meses'}`
  }
  const years = Math.floor(ageInMonths / 12)
  return `${years} ${years === 1 ? 'ano' : 'anos'}`
}
