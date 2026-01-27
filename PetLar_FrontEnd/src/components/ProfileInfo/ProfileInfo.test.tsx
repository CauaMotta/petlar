/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '../../utils/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import userEvent from '@testing-library/user-event'
import { useSelector } from 'react-redux'

import ProfileInfo from '.'

vi.mock('react-redux', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-redux')>()
  return {
    ...actual,
    useSelector: vi.fn()
  }
})

vi.mock('../Modal', () => ({
  default: ({ isOpen, title, children }: any) =>
    isOpen ? (
      <div>
        {title && <h2>{title}</h2>}
        {children}
      </div>
    ) : null
}))

const updateUserMock = vi.fn()
const changePasswordMock = vi.fn()
const deleteUserMock = vi.fn()
const resetMock = vi.fn()

vi.mock('../../hooks/useUser', () => ({
  useUpdateUser: () => ({
    mutate: updateUserMock,
    isSuccess: false,
    error: null,
    reset: resetMock
  }),
  useChangePassword: () => ({
    mutate: changePasswordMock,
    isSuccess: false,
    error: null,
    reset: resetMock
  }),
  useDeleteUser: () => ({
    mutate: deleteUserMock
  })
}))

const mockUser = {
  name: 'Teste',
  email: 'teste@email.com'
}

describe('ProfileInfo', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useSelector).mockReturnValue({ user: mockUser })
  })

  it('should render user name and email', () => {
    render(<ProfileInfo />)

    expect(screen.getByText('Teste')).toBeInTheDocument()
    expect(screen.getByText('teste@email.com')).toBeInTheDocument()
  })

  it('should open edit profile modal', async () => {
    const user = userEvent.setup()
    render(<ProfileInfo />)

    await user.click(screen.getByText(/editar perfil/i))

    expect(
      screen.getByRole('heading', { name: 'Editar perfil' })
    ).toBeInTheDocument()

    expect(screen.getByLabelText(/nome/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/email/i)).toBeInTheDocument()
  })

  it('should call updateUser with correct values', async () => {
    const user = userEvent.setup()
    render(<ProfileInfo />)

    await user.click(screen.getByText(/editar perfil/i))

    const nameInput = screen.getByLabelText(/nome/i)
    await user.clear(nameInput)
    await user.type(nameInput, 'Novo Nome')

    await user.click(screen.getByText(/salvar/i))

    expect(updateUserMock).toHaveBeenCalledWith({
      name: 'Novo Nome',
      email: 'teste@email.com'
    })
  })

  it('should open change password modal', async () => {
    const user = userEvent.setup()

    render(<ProfileInfo />)

    await user.click(screen.getByText(/trocar senha/i))

    expect(
      screen.getByRole('heading', { name: 'Editar senha' })
    ).toBeInTheDocument()

    expect(screen.getByLabelText(/senha/i)).toBeInTheDocument()
  })

  it('should call changePassword with password', async () => {
    const user = userEvent.setup()

    render(<ProfileInfo />)

    await user.click(screen.getByText(/trocar senha/i))

    const passwordInput = screen.getByLabelText(/senha/i)
    await user.type(passwordInput, '123456')

    await user.click(screen.getByText(/salvar/i))

    expect(changePasswordMock).toHaveBeenCalledWith({
      password: '123456'
    })
  })

  it('should call deleteUser when clicking delete account', async () => {
    const user = userEvent.setup()

    render(<ProfileInfo />)

    await user.click(screen.getByText(/editar perfil/i))
    await user.click(screen.getByText(/apagar conta/i))

    expect(deleteUserMock).toHaveBeenCalled()
  })
})
