import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'
import { useSelector, useDispatch } from 'react-redux'
import Header from '.'
import { logout } from '../../store/reducers/authSlice'

const mockNavigate = vi.fn()
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
    useSelector: vi.fn(),
    useDispatch: vi.fn()
  }
})

vi.mock('../../store/reducers/authSlice', () => ({
  logout: vi.fn(() => ({ type: 'auth/logout' })),
  default: (state = { user: null, isAuthenticated: false }) => state
}))

vi.mock('../PetlarLogo', () => ({
  default: () => <div data-testid="mock-logo">Logo</div>
}))

describe('Header', () => {
  const mockDispatch = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useDispatch).mockReturnValue(mockDispatch)
  })

  test('should render login button when user is not authenticated', () => {
    vi.mocked(useSelector).mockReturnValue({
      isAuthenticated: false,
      user: null
    })

    render(<Header />)

    expect(screen.getByText(/Login\/Cadastro/i)).toBeInTheDocument()
    expect(screen.queryByText(/Meu Perfil/i)).not.toBeInTheDocument()
  })

  test('should navigate to login page when clicking login button', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({
      isAuthenticated: false,
      user: null
    })

    render(<Header />)

    const loginBtn = screen.getByText(/Login\/Cadastro/i)
    await user.click(loginBtn)

    expect(mockNavigate).toHaveBeenCalledWith('/login')
  })

  test('should render user profile info when authenticated', () => {
    vi.mocked(useSelector).mockReturnValue({
      isAuthenticated: true,
      user: { name: 'João Silva' }
    })

    render(<Header />)

    expect(screen.getByText(/Olá,/i)).toBeInTheDocument()
    expect(screen.getByText(/João Silva/i)).toBeInTheDocument()
    expect(screen.getByText(/Meu Perfil/i)).toBeInTheDocument()
    expect(screen.getByText(/Sair/i)).toBeInTheDocument()
  })

  test('should toggle "active" class when menu button is clicked', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({
      isAuthenticated: false,
      user: null
    })

    render(<Header />)

    const containerElement = screen.getByTestId('header-container')
    const menuBtn = screen.getByLabelText(/Abrir menu/i)

    expect(containerElement).not.toHaveClass('active')

    await user.click(menuBtn)
    expect(containerElement).toHaveClass('active')

    await user.click(menuBtn)
    expect(containerElement).not.toHaveClass('active')
  })

  test('should navigate to home when logo is clicked', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({
      isAuthenticated: false,
      user: null
    })

    render(<Header />)

    const logoArea = screen.getByText('PetLar')
    await user.click(logoArea)

    expect(mockNavigate).toHaveBeenCalledWith('/')
  })

  test('should navigate to profile and close menu when clicking "Meu Perfil"', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({
      isAuthenticated: true,
      user: { name: 'João Silva' }
    })

    render(<Header />)

    const profileBtn = screen.getByText(/Meu Perfil/i)
    await user.click(profileBtn)

    expect(mockNavigate).toHaveBeenCalledWith('/profile')
  })

  test('should dispatch logout action and navigate to home when clicking logout button', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({
      isAuthenticated: true,
      user: { name: 'João Silva' }
    })

    render(<Header />)

    const logoutBtn = screen.getByText(/Sair/i)
    await user.click(logoutBtn)

    expect(mockDispatch).toHaveBeenCalledWith(logout())
    expect(mockNavigate).toHaveBeenCalledWith('/')
  })
})
