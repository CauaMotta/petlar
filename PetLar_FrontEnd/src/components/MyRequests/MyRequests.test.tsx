/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen, waitFor } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import MyRequests from '.'

const mockGetMyRequests = vi.fn()
const mockMutateStatus = vi.fn()
const mockMutateEdit = vi.fn()
const mockResetEdit = vi.fn()

vi.mock('../../hooks/useAdoption', () => ({
  useGetMyRequests: () => mockGetMyRequests(),
  useStatusUpdate: () => ({
    mutate: mockMutateStatus
  }),
  useEditReason: () => ({
    mutate: mockMutateEdit,
    reset: mockResetEdit,
    isSuccess: false,
    error: null
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
    animalOwner: { id: 'u1', name: 'João' }
  },
  {
    id: '2',
    status: 'aprovado',
    reason: 'Tenho quintal grande',
    animal: { id: 'a2', name: 'Mimi' },
    animalOwner: { id: 'u2', name: 'Maria' }
  }
]

describe('MyRequests', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('Should render empty state message when there are no requests', () => {
    mockGetMyRequests.mockReturnValue({ data: [], totalPages: 0 })

    render(<MyRequests />)

    expect(
      screen.getByText('Você ainda não solicitou nenhum animal!')
    ).toBeInTheDocument()
  })

  test('Should render cards and prioritize "pendente" status', () => {
    mockGetMyRequests.mockReturnValue({ data: mockAdoptions, totalPages: 1 })

    render(<MyRequests />)

    expect(screen.getByText('Rex')).toBeInTheDocument()
    expect(screen.getByText('Mimi')).toBeInTheDocument()

    const viewButtons = screen.getAllByRole('button', { name: /Visualizar/i })
    expect(viewButtons).toHaveLength(1)

    const disabledButton = screen.getByRole('button', { name: /Aprovado!/i })
    expect(disabledButton).toBeDisabled()
  })

  test('Should open modal and allow canceling a request', async () => {
    mockGetMyRequests.mockReturnValue({ data: mockAdoptions, totalPages: 1 })

    render(<MyRequests />)

    const viewButton = screen.getByRole('button', { name: /Visualizar/i })
    await userEvent.click(viewButton)

    expect(screen.getByText('Solicitação enviada')).toBeInTheDocument()
    expect(screen.getByText(/Pedido enviado para João/i)).toBeInTheDocument()
    expect(screen.getByText(/Quero muito um amigo/i)).toBeInTheDocument()

    const cancelButton = screen.getByRole('button', { name: /Cancelar/i })
    await userEvent.click(cancelButton)

    expect(mockMutateStatus).toHaveBeenCalledWith({
      id: '1',
      status: 'cancel'
    })
  })

  test('Should open edit modal and update reason', async () => {
    mockGetMyRequests.mockReturnValue({ data: mockAdoptions, totalPages: 1 })

    render(<MyRequests />)

    await userEvent.click(screen.getByRole('button', { name: /Visualizar/i }))

    await userEvent.click(screen.getByRole('button', { name: /Editar/i }))

    const textarea = screen.getByLabelText(/Texto/i)
    expect(textarea).toHaveValue('Quero muito um amigo')

    await userEvent.clear(textarea)
    await userEvent.type(textarea, 'Novo motivo válido')
    await userEvent.tab()

    await userEvent.click(screen.getByRole('button', { name: /Salvar/i }))

    await waitFor(() => {
      expect(mockMutateEdit).toHaveBeenCalledWith({
        id: '1',
        reason: 'Novo motivo válido'
      })
    })
  })

  test('Should show validation error for short reason', async () => {
    mockGetMyRequests.mockReturnValue({ data: mockAdoptions, totalPages: 1 })

    render(<MyRequests />)

    await userEvent.click(screen.getByRole('button', { name: /Visualizar/i }))
    await userEvent.click(screen.getByRole('button', { name: /Editar/i }))

    const textarea = screen.getByLabelText(/Texto/i)
    await userEvent.clear(textarea)
    await userEvent.type(textarea, 'oi')
    await userEvent.tab()

    expect(
      await screen.findByText(/Deve conter no minímo 3 caracteres/i)
    ).toBeInTheDocument()
  })
})
