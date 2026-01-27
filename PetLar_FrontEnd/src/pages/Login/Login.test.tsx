/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen, waitFor } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'
import * as reactRedux from 'react-redux'

import { useLogin } from '../../hooks/useLogin'
import Login from '.'

const mockNavigate = vi.fn()
const mockMutate = vi.fn()

vi.mock('../../hooks/useLogin', () => ({
  useLogin: vi.fn()
}))

vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-router-dom')>()
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

vi.mock('react-redux', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-redux')>()
  return {
    ...actual,
    useSelector: vi.fn()
  }
})

describe('Login Page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useLogin).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: false,
      error: null
    } as any)

    vi.mocked(reactRedux.useSelector).mockReturnValue({
      isAuthenticated: false
    })
  })

  test('should show validation errors when fields are empty and blurred', async () => {
    const user = userEvent.setup()
    render(<Login />)

    const emailInput = screen.getByLabelText(/email/i)
    const passwordInput = screen.getByLabelText(/senha/i)

    // Tocar nos campos e sair para disparar o validation do Formik
    await user.click(emailInput)
    await user.click(passwordInput)
    await user.click(document.body)

    expect(
      await screen.findAllByText(/este campo é obrigatório/i)
    ).toHaveLength(2)
  })

  test('should call mutate with form values when submit button is clicked', async () => {
    const user = userEvent.setup()
    render(<Login />)

    await user.type(screen.getByLabelText(/email/i), 'test@example.com')
    await user.type(screen.getByLabelText(/senha/i), 'password123')

    await user.click(screen.getByRole('button', { name: /login/i }))

    expect(mockMutate).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password123'
    })
  })

  test('should show loading spinner when login is pending', () => {
    vi.mocked(useLogin).mockReturnValue({
      mutate: mockMutate,
      isPending: true,
      isSuccess: false,
      error: null
    } as any)

    render(<Login />)

    expect(
      screen.queryByRole('button', { name: /login/i })
    ).not.toBeInTheDocument()
  })

  test('should display API error message if login fails', () => {
    vi.mocked(useLogin).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: false,
      error: { response: { data: { message: 'Credenciais inválidas' } } }
    } as any)

    render(<Login />)

    expect(screen.getByText(/credenciais inválidas/i)).toBeInTheDocument()
  })

  test('should redirect to home if isSuccess is true', async () => {
    vi.mocked(useLogin).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: true,
      error: null
    } as any)

    render(<Login />)

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/', { replace: true })
    })
  })

  test('should redirect automatically if already authenticated', () => {
    vi.mocked(reactRedux.useSelector).mockReturnValue({ isAuthenticated: true })

    render(<Login />)

    expect(screen.queryByText(/Faça o login/i)).not.toBeInTheDocument()
  })
})
