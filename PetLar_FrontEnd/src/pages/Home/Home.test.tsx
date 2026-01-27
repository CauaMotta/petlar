/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'

import Home from '.'
import { useGetAllAnimals } from '../../hooks/useAnimals'

vi.mock('../../hooks/useAnimals', () => ({
  useGetAllAnimals: vi.fn()
}))

vi.mock('../../components/Card', () => ({
  default: ({ animal }: any) => <div data-testid="mock-card">{animal.name}</div>
}))

const mockAvailableAnimals = [
  { id: '1', name: 'Rex', type: 'cachorro', status: 'disponivel' },
  { id: '2', name: 'Miau', type: 'gato', status: 'disponivel' }
]

const mockAdoptedAnimals = [
  { id: '3', name: 'Pipoca', type: 'passaro', status: 'adotado' }
]

describe('Home Page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  const setupMocks = ({
    isLoading = false,
    isError = false,
    available = mockAvailableAnimals,
    adopted = mockAdoptedAnimals
  } = {}) => {
    vi.mocked(useGetAllAnimals).mockImplementation((params: any) => {
      if (params.status === 'disponivel') {
        return {
          data: available,
          isLoading,
          isError,
          totalPages: 1
        } as any
      }
      if (params.status === 'adotado') {
        return {
          data: adopted,
          isLoading: false,
          isError: false,
          totalPages: 1
        } as any
      }
      return {
        data: [],
        isLoading: false,
        isError: false,
        totalPages: 0
      } as any
    })
  }

  test('should show loader when isLoading is true', () => {
    setupMocks({ isLoading: true })
    render(<Home />)

    expect(screen.getByTestId('loader')).toBeInTheDocument()
  })

  test('should show error message when isError is true', () => {
    setupMocks({ isError: true })
    render(<Home />)

    expect(screen.getByText(/Ops...Ocorreu um erro/i)).toBeInTheDocument()
  })

  test('should render available and adopted animals correctly', () => {
    setupMocks()
    render(<Home />)

    expect(screen.getByText('Rex')).toBeInTheDocument()
    expect(screen.getByText('Miau')).toBeInTheDocument()
    expect(screen.getByText('Pipoca')).toBeInTheDocument()
    expect(screen.getByText('Bem-vindo ao PetLar!')).toBeInTheDocument()
  })

  test('should toggle adopted animals section when clicking the button', async () => {
    const user = userEvent.setup()
    setupMocks()
    render(<Home />)

    expect(screen.getByText('Pipoca')).toBeInTheDocument()

    const toggleBtn = screen.getByRole('button', {
      name: /esconder animais adotados/i
    })
    await user.click(toggleBtn)

    expect(screen.queryByText('Pipoca')).not.toBeInTheDocument()
    expect(screen.getByText(/mostrar animais adotados/i)).toBeInTheDocument()
  })

  test('should show empty message when no available animals are found', () => {
    setupMocks({ available: [] })
    render(<Home />)

    expect(
      screen.getByText(/Parece que não temos nenhum pet para adoção/i)
    ).toBeInTheDocument()
  })

  test('should update filter when select value changes', async () => {
    const user = userEvent.setup()
    setupMocks()
    render(<Home />)

    const select = screen.getByText('Todos')
    await user.click(select)

    const option = screen.getByText('Gato')
    await user.click(option)

    expect(useGetAllAnimals).toHaveBeenCalledWith(
      expect.objectContaining({
        type: 'gato',
        status: 'disponivel'
      })
    )
  })

  test('should reset pages when filter changes', async () => {
    const user = userEvent.setup()
    setupMocks()
    render(<Home />)

    const select = screen.getByText('Todos')
    await user.click(select)
    const option = screen.getByText('Cachorro')
    await user.click(option)

    expect(useGetAllAnimals).toHaveBeenCalledWith(
      expect.objectContaining({
        page: 0,
        type: 'cachorro'
      })
    )
  })
})
