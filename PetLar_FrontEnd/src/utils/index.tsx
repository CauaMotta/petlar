export const formatWeight = (weight: number) => {
  return weight / 100
}

export const formatDate = (dateString: string) => {
  if (dateString) {
    const [year, month, day] = dateString.split('-')
    return `${day}/${month}/${year}`
  }
}

export const formatAge = (ageInMonths: number) => {
  if (ageInMonths < 12) {
    return `${ageInMonths} ${ageInMonths === 1 ? 'mês' : 'meses'}`
  }
  const years = Math.floor(ageInMonths / 12)
  return `${years} ${years === 1 ? 'ano' : 'anos'}`
}

export const convertType = (type: string) => {
  switch (type) {
    case 'CACHORRO':
      return 'dogs'
    case 'GATO':
      return 'cats'
    case 'PASSARO':
      return 'birds'
    case 'OUTRO':
      return 'others'
  }
}
