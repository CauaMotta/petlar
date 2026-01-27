/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import MyAnimals from '.'

const mockNavigate = vi.fn()
const mockGetMyAnimals = vi.fn()
const mockDeleteMutate = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual =
    await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

vi.mock('../../hooks/useAnimals', () => ({
  useGetMyAnimals: () => mockGetMyAnimals(),
  useDeleteAnimal: () => ({
    mutate: mockDeleteMutate
  })
}))

vi.mock('../Card', () => ({
  default: ({ children, animal }: any) => (
    <div data-testid="mock-card">
      {animal.name}
      {children}
    </div>
  )
}))

const mockAnimals = [
  {
    id: '101',
    name: 'Totó',
    status: 'disponivel',
    photo: 'toto.jpg'
  },
  {
    id: '102',
    name: 'Garfield',
    status: 'adotado',
    photo: 'garfield.jpg'
  }
]

describe('MyAnimals', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('Should render empty state correctly', () => {
    mockGetMyAnimals.mockReturnValue({ data: [], totalPages: 0 })

    render(<MyAnimals />)

    expect(
      screen.getByText('Você ainda não cadastrou nenhum animal!')
    ).toBeInTheDocument()
  })

  test('Should render animal cards with correct buttons based on status', () => {
    mockGetMyAnimals.mockReturnValue({ data: mockAnimals, totalPages: 1 })

    render(<MyAnimals />)

    expect(screen.getByText('Totó')).toBeInTheDocument()
    expect(screen.getByText('Garfield')).toBeInTheDocument()

    expect(screen.getByRole('button', { name: /Editar/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /Deletar/i })).toBeInTheDocument()

    const adoptedButton = screen.getByRole('button', { name: /Adotado!/i })
    expect(adoptedButton).toBeInTheDocument()
    expect(adoptedButton).toBeDisabled()

    expect(screen.getAllByRole('button', { name: /Editar/i })).toHaveLength(1)
  })

  test('Should navigate to edit page when "Editar" is clicked', async () => {
    mockGetMyAnimals.mockReturnValue({ data: [mockAnimals[0]], totalPages: 1 })

    render(<MyAnimals />)

    const editButton = screen.getByRole('button', { name: /Editar/i })
    await userEvent.click(editButton)

    expect(mockNavigate).toHaveBeenCalledWith('/editAnimal/101')
  })

  test('Should open delete modal and confirm deletion', async () => {
    mockGetMyAnimals.mockReturnValue({ data: [mockAnimals[0]], totalPages: 1 })

    render(<MyAnimals />)

    const deleteButton = screen.getByRole('button', { name: /Deletar/i })
    await userEvent.click(deleteButton)

    expect(
      screen.getByText('Você quer excluir este animal?')
    ).toBeInTheDocument()
    expect(screen.getByRole('heading', { name: 'Totó' })).toBeInTheDocument()

    const confirmButton = screen.getByRole('button', { name: /Sim/i })
    await userEvent.click(confirmButton)

    expect(mockDeleteMutate).toHaveBeenCalledWith('101')
  })

  test('Should close delete modal when clicking "Não"', async () => {
    mockGetMyAnimals.mockReturnValue({ data: [mockAnimals[0]], totalPages: 1 })

    render(<MyAnimals />)

    await userEvent.click(screen.getByRole('button', { name: /Deletar/i }))

    const cancelButton = screen.getByRole('button', { name: /Não/i })
    await userEvent.click(cancelButton)

    expect(
      screen.queryByText('Você quer excluir este animal?')
    ).not.toBeInTheDocument()
    expect(mockDeleteMutate).not.toHaveBeenCalled()
  })
})
