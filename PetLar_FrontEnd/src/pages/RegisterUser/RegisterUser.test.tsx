/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'
import * as reactRedux from 'react-redux'

import { useRegisterUser } from '../../hooks/useUser'
import RegisterUser from '.'

const mockNavigate = vi.fn()
const mockMutate = vi.fn()

vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-router-dom')>()
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    Navigate: ({ to }: { to: string }) => (
      <div data-testid="navigate-mock">Redirecting to {to}</div>
    )
  }
})

vi.mock('../../hooks/useUser', () => ({
  useRegisterUser: vi.fn()
}))

vi.mock('react-redux', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-redux')>()
  return { ...actual, useSelector: vi.fn() }
})

describe('RegisterUser Page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useRegisterUser).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: false,
      error: null
    } as any)
    vi.mocked(reactRedux.useSelector).mockReturnValue({
      isAuthenticated: false
    })
  })

  test('should validate field constraints (min/max/required)', async () => {
    const user = userEvent.setup()
    render(<RegisterUser />)

    const nameInput = screen.getByLabelText(/nome/i)

    await user.type(nameInput, 'ab')
    await user.tab()

    expect(
      await screen.findByText(/Deve conter no minímo 3 caracteres/i)
    ).toBeInTheDocument()
  })

  test('should trim and clean name input on blur', async () => {
    const user = userEvent.setup()
    render(<RegisterUser />)

    const nameInput = screen.getByLabelText(/nome/i) as HTMLInputElement

    await user.type(nameInput, '  João    Silva  ')
    await user.tab()

    expect(nameInput.value).toBe('João Silva')
  })

  test('should call mutate with correct values when form is valid', async () => {
    const user = userEvent.setup()
    render(<RegisterUser />)

    await user.type(screen.getByLabelText(/nome/i), 'Fulano de Tal')
    await user.type(screen.getByLabelText(/email/i), 'fulano@teste.com')
    await user.type(screen.getByLabelText(/senha/i), '123456')

    await user.click(screen.getByRole('button', { name: /cadastrar/i }))

    expect(mockMutate).toHaveBeenCalledWith({
      name: 'Fulano de Tal',
      email: 'fulano@teste.com',
      password: '123456'
    })
  })

  test('should show success modal and navigate to login on close', async () => {
    const user = userEvent.setup()

    vi.mocked(useRegisterUser).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: true,
      error: null
    } as any)

    render(<RegisterUser />)

    expect(
      screen.getByText(/Cadastro realizado com sucesso/i)
    ).toBeInTheDocument()

    const closeBtn =
      screen.getByRole('button', { name: /close/i }) ||
      screen.getByText(/fechar/i)
    await user.click(closeBtn)

    expect(mockNavigate).toHaveBeenCalledWith('/login')
  })

  test('should redirect if already authenticated', () => {
    vi.mocked(reactRedux.useSelector).mockReturnValue({ isAuthenticated: true })
    render(<RegisterUser />)
    expect(screen.getByTestId('navigate-mock')).toBeInTheDocument()
  })
})
