/* eslint-disable @typescript-eslint/no-explicit-any */
import { render } from '../../utils/test-utils'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import { useDispatch } from 'react-redux'
import Cookies from 'js-cookie'

import { AuthProvider } from './index'
import { useGetMe } from '../../hooks/useUser'
import { setUser, logout } from '../../store/reducers/authSlice'

vi.mock('react-redux', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-redux')>()
  return {
    ...actual,
    useDispatch: vi.fn()
  }
})

vi.mock('js-cookie', () => ({
  default: {
    get: vi.fn()
  }
}))

vi.mock('../../hooks/useUser', () => ({
  useGetMe: vi.fn()
}))

vi.mock('../../store/reducers/authSlice', () => {
  return {
    setUser: vi.fn((user) => ({ type: 'auth/setUser', payload: user })),
    logout: vi.fn(() => ({ type: 'auth/logout' })),
    default: (state = {}) => state
  }
})

describe('AuthProvider', () => {
  const mockDispatch = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useDispatch).mockReturnValue(mockDispatch)
  })

  test('Should dispatch setUser when token and user data exist', () => {
    vi.mocked(
      Cookies.get as (name: string) => string | undefined
    ).mockReturnValue('valid-token')
    const mockUser = { id: 1, name: 'John Doe', email: 'john@example.com' }
    vi.mocked(useGetMe).mockReturnValue({
      data: mockUser,
      isError: false,
      isLoading: false
    } as any)

    render(
      <AuthProvider>
        <div>Children</div>
      </AuthProvider>
    )

    expect(setUser).toHaveBeenCalledWith(mockUser)
    expect(mockDispatch).toHaveBeenCalledWith(setUser(mockUser))
  })

  test('Should dispatch logout when token does not exist', () => {
    vi.mocked(
      Cookies.get as (name: string) => string | undefined
    ).mockReturnValue(undefined)
    vi.mocked(useGetMe).mockReturnValue({
      data: null,
      isError: false
    } as any)

    render(
      <AuthProvider>
        <div />
      </AuthProvider>
    )

    expect(logout).toHaveBeenCalled()
    expect(mockDispatch).toHaveBeenCalledWith(logout())
  })

  test('Should dispatch logout when useGetMe returns an error', () => {
    vi.mocked(
      Cookies.get as (name: string) => string | undefined
    ).mockReturnValue('valid-token')
    vi.mocked(useGetMe).mockReturnValue({
      data: null,
      isError: true
    } as any)

    render(
      <AuthProvider>
        <div />
      </AuthProvider>
    )

    expect(logout).toHaveBeenCalled()
    expect(mockDispatch).toHaveBeenCalledWith(logout())
  })

  test('Should render children correctly', () => {
    vi.mocked(
      Cookies.get as (name: string) => string | undefined
    ).mockReturnValue(undefined)
    vi.mocked(useGetMe).mockReturnValue({ data: null, isError: false } as any)

    const { getByText } = render(
      <AuthProvider>
        <span>Test Child Content</span>
      </AuthProvider>
    )

    expect(getByText('Test Child Content')).toBeInTheDocument()
  })
})
