/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import AdoptionRequests from '.'

const mockGetRequests = vi.fn()
const mockMutate = vi.fn()

vi.mock('../../hooks/useAdoption', () => ({
  useGetRequestsForMyAnimals: () => mockGetRequests(),
  useStatusUpdate: () => ({
    mutate: mockMutate
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

const mockAdoptions = [
  {
    id: '1',
    status: 'pendente',
    reason: 'Quero muito um amigo',
    animal: { id: 'a1', name: 'Rex' },
    adopter: { id: 'u1', name: 'João' }
  },
  {
    id: '2',
    status: 'aprovado',
    reason: 'Tenho quintal grande',
    animal: { id: 'a2', name: 'Mimi' },
    adopter: { id: 'u2', name: 'Maria' }
  }
]

describe('AdoptionRequests', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('Should render empty state message when there are no requests', () => {
    mockGetRequests.mockReturnValue({ data: [], totalPages: 0 })

    render(<AdoptionRequests />)

    expect(
      screen.getByText('Você ainda não cadastrou nenhum animal!')
    ).toBeInTheDocument()
  })

  test('Should render cards and prioritize "pendente" status', () => {
    mockGetRequests.mockReturnValue({ data: mockAdoptions, totalPages: 1 })

    render(<AdoptionRequests />)

    expect(screen.getByText('Rex')).toBeInTheDocument()
    expect(screen.getByText('Mimi')).toBeInTheDocument()

    const viewButtons = screen.getAllByRole('button', { name: /Visualizar/i })
    expect(viewButtons).toHaveLength(1)

    const disabledButton = screen.getByRole('button', { name: /aprovado!/i })
    expect(disabledButton).toBeDisabled()
  })

  test('Should open modal and approve adoption request', async () => {
    mockGetRequests.mockReturnValue({ data: mockAdoptions, totalPages: 1 })

    render(<AdoptionRequests />)

    const viewButton = screen.getByRole('button', { name: /Visualizar/i })
    await userEvent.click(viewButton)

    expect(screen.getByText('Solicitação de adoção')).toBeInTheDocument()
    expect(screen.getByText(/João quer adotar Rex/i)).toBeInTheDocument()
    expect(screen.getByText(/Quero muito um amigo/i)).toBeInTheDocument()

    const approveButton = screen.getByRole('button', { name: /Aprovar/i })
    await userEvent.click(approveButton)

    expect(mockMutate).toHaveBeenCalledWith({
      id: '1',
      status: 'accept'
    })
  })

  test('Should open modal and deny adoption request', async () => {
    mockGetRequests.mockReturnValue({ data: mockAdoptions, totalPages: 1 })

    render(<AdoptionRequests />)

    await userEvent.click(screen.getByRole('button', { name: /Visualizar/i }))

    const denyButton = screen.getByRole('button', { name: /Recusar/i })
    await userEvent.click(denyButton)

    expect(mockMutate).toHaveBeenCalledWith({
      id: '1',
      status: 'deny'
    })
  })
})
