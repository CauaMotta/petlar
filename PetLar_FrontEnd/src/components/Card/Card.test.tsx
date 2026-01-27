import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'

import Card from '.'

const mockNavigate = vi.fn()

vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-router-dom')>()
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

vi.mock('../../utils', () => ({
  formatDateBr: vi.fn((date) => `formatted-${date}`)
}))

vi.mock('../../main', () => ({
  API_URL: 'http://api.test/'
}))

const mockAnimal = {
  id: '1',
  name: 'Rex',
  imagePath: 'rex.jpg',
  birthDate: '2022-01-01',
  type: 'Cachorro',
  size: 'Médio',
  sex: 'Macho',
  status: 'DISPONIVEL'
}

describe('Card Component', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('should render basic animal information correctly', () => {
    render(<Card animal={mockAnimal} />)

    expect(screen.getByText('Rex')).toBeInTheDocument()
    expect(screen.getByText(/cachorro/i)).toBeInTheDocument()
    expect(screen.getByText(/médio/i)).toBeInTheDocument()
    expect(screen.getByText(/macho/i)).toBeInTheDocument()
    expect(screen.getByText('formatted-2022-01-01')).toBeInTheDocument()
  })

  test('should display the image when imagePath is provided', () => {
    render(<Card animal={mockAnimal} />)

    const img = screen.getByRole('img', { name: /rex/i })
    expect(img).toHaveAttribute('src', 'http://api.test/rex.jpg')
  })

  test('should display the placeholder icon when imagePath is null', () => {
    const animalWithoutImg = { ...mockAnimal, imagePath: null }
    render(<Card animal={animalWithoutImg} />)

    expect(screen.getByTestId('noImage')).toBeInTheDocument()
    expect(screen.queryByRole('img')).not.toBeInTheDocument()
  })

  test('should render children instead of default info when provided', () => {
    render(
      <Card animal={mockAnimal}>
        <div data-testid="child-content">Extra Content</div>
      </Card>
    )

    expect(screen.getByText('Rex')).toBeInTheDocument()
    expect(screen.getByTestId('child-content')).toBeInTheDocument()
    expect(screen.queryByText(/espécie:/i)).not.toBeInTheDocument()
  })

  test('should navigate to details page when "Ver mais" button is clicked', async () => {
    const user = userEvent.setup()
    render(<Card animal={mockAnimal} />)

    const button = screen.getByRole('button', { name: /ver mais/i })
    await user.click(button)

    expect(mockNavigate).toHaveBeenCalledWith('/details/1')
  })

  test('should disable button and show "Adotado!" when status is ADOTADO', () => {
    const adoptedAnimal = { ...mockAnimal, status: 'ADOTADO' }
    render(<Card animal={adoptedAnimal} />)

    const button = screen.getByRole('button', { name: /adotado!/i })
    expect(button).toBeDisabled()
    expect(button).toHaveTextContent(/adotado!/i)
  })
})
