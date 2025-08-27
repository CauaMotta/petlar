import { render, screen } from '../../utils/test-utils'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import userEvent from '@testing-library/user-event'

import Card from '.'

const mockAnimal: Animal = {
  id: '1',
  name: 'Nome de teste',
  age: 2,
  type: 'Cachorro',
  breed: 'Vira-Lata',
  sex: 'Macho',
  weight: 100,
  size: 'Pequeno',
  registrationDate: '22-08-2025',
  status: 'Disponível'
}
const mockNavigate = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>(
    'react-router-dom'
  )
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

describe('Card', () => {
  beforeEach(() => {
    mockNavigate.mockClear()
  })

  test('Should render the card component when not have image', () => {
    render(<Card animal={mockAnimal} />)

    expect(screen.getByTestId('noImage')).toBeInTheDocument()
    expect(screen.getByText(/Nome de teste/i)).toBeInTheDocument()
    expect(screen.getByText(/2 anos/i)).toBeInTheDocument()
    expect(screen.getByText(/Macho/i)).toBeInTheDocument()
    expect(screen.getByText(/Pequeno/i)).toBeInTheDocument()
    expect(screen.getByText(/Ver mais/i)).toBeInTheDocument()
  })

  test('Should render the card component when have image', () => {
    const mockAnimalWithImage: Animal = {
      ...mockAnimal,
      urlImage: 'urlTeste'
    }

    render(<Card animal={mockAnimalWithImage} />)

    expect(screen.getByAltText('Nome de teste')).toHaveAttribute(
      'src',
      'urlTeste'
    )
    expect(screen.getByText(/Nome de teste/i)).toBeInTheDocument()
    expect(screen.getByText(/2 anos/i)).toBeInTheDocument()
    expect(screen.getByText(/Macho/i)).toBeInTheDocument()
    expect(screen.getByText(/Pequeno/i)).toBeInTheDocument()
    expect(screen.getByText(/Ver mais/i)).toBeInTheDocument()
  })

  test('Should call navigate when view more button is clicked', async () => {
    render(<Card animal={mockAnimal} />)

    const button = screen.getByRole('button', { name: /Ver mais/i })
    await userEvent.click(button)

    expect(mockNavigate).toHaveBeenCalledWith('/details/1')
  })

  test('Should render adopted button when status is adopted', async () => {
    const mockAnimalAdopted: Animal = {
      ...mockAnimal,
      status: 'Adotado'
    }

    render(<Card animal={mockAnimalAdopted} />)

    const button = screen.getByRole('button', { name: /Adotado/i })
    expect(button).toBeDisabled()

    await userEvent.click(button)

    expect(mockNavigate).not.toHaveBeenCalled()
  })
})
