import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'

import Profile from '.'

const mockNavigate = vi.fn()

vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-router-dom')>()
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

vi.mock('../../components/ProfileInfo', () => ({
  default: () => <div data-testid="profile-info">Profile Info</div>
}))

vi.mock('../../components/MyAnimals', () => ({
  default: () => <div data-testid="my-animals">Meus Animais</div>
}))

vi.mock('../../components/AdoptionRequests', () => ({
  default: () => <div data-testid="adoption-requests">Solicitações</div>
}))

vi.mock('../../components/MyRequests', () => ({
  default: () => <div data-testid="my-requests">Minhas Solicitações</div>
}))

describe('Profile Page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('should render MyAnimals by default', () => {
    render(<Profile />)

    expect(screen.getByTestId('profile-info')).toBeInTheDocument()
    expect(screen.getByTestId('my-animals')).toBeInTheDocument()
    expect(screen.queryByTestId('adoption-requests')).not.toBeInTheDocument()
  })

  test('should switch to Adoption Requests tab when clicked', async () => {
    const user = userEvent.setup()
    render(<Profile />)

    const tabBtn = screen.getByRole('button', {
      name: /solicitações de adoção/i
    })
    await user.click(tabBtn)

    expect(screen.getByTestId('adoption-requests')).toBeInTheDocument()
    expect(screen.queryByTestId('my-animals')).not.toBeInTheDocument()
    expect(tabBtn).toHaveClass('active')
  })

  test('should switch to My Requests tab when clicked', async () => {
    const user = userEvent.setup()
    render(<Profile />)

    const tabBtn = screen.getByRole('button', { name: /minhas solicitações/i })
    await user.click(tabBtn)

    expect(screen.getByTestId('my-requests')).toBeInTheDocument()
    expect(screen.queryByTestId('my-animals')).not.toBeInTheDocument()
  })

  test('should navigate to register animal page when clicking the register button', async () => {
    const user = userEvent.setup()
    render(<Profile />)

    const registerBtn = screen.getByRole('button', {
      name: /cadastrar novo animal/i
    })
    await user.click(registerBtn)

    expect(mockNavigate).toHaveBeenCalledWith('/registerAnimal')
  })
})
