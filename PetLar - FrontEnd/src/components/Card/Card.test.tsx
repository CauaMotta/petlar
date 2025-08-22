import { render, screen } from '../../utils/test-utils'
import { describe, expect, test } from 'vitest'

import Card from '.'

const animal: Animal = {
  id: '1',
  name: 'Nome de teste',
  age: 2,
  type: 'Cachorro',
  breed: 'Vira-Lata',
  sex: 'Macho',
  weight: 100,
  size: 'Pequeno',
  date: '22/08/2025',
  status: 'Disponível'
}

describe('Card', () => {
  test('Should render the card component when not have image', () => {
    render(<Card animal={animal} />)

    expect(screen.getByTestId('noImage')).toBeInTheDocument()
    expect(screen.getByText('Nome de teste')).toBeInTheDocument()
    expect(screen.getByText('Idade: 2')).toBeInTheDocument()
    expect(screen.getByText('Sexo: Macho')).toBeInTheDocument()
    expect(screen.getByText('Porte: Pequeno')).toBeInTheDocument()
    expect(screen.getByText(/Ver mais/i)).toBeInTheDocument()
  })

  test('Should render the card component when have image', () => {
    const animalWithImage: Animal = {
      ...animal,
      urlImage: 'urlTeste'
    }

    render(<Card animal={animalWithImage} />)

    expect(screen.getByAltText('Nome de teste')).toHaveAttribute(
      'src',
      'urlTeste'
    )
    expect(screen.getByText('Nome de teste')).toBeInTheDocument()
    expect(screen.getByText('Idade: 2')).toBeInTheDocument()
    expect(screen.getByText('Sexo: Macho')).toBeInTheDocument()
    expect(screen.getByText('Porte: Pequeno')).toBeInTheDocument()
    expect(screen.getByText(/Ver mais/i)).toBeInTheDocument()
  })
})
