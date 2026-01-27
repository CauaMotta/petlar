/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import AdoptionCallSection from '.'

const mockNavigate = vi.fn()
const mockUseSelector = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual =
    await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

vi.mock('react-redux', async () => {
  const actual =
    await vi.importActual<typeof import('react-redux')>('react-redux')
  return {
    ...actual,
    useSelector: (selector: any) => mockUseSelector(selector)
  }
})

describe('AdoptionCallSection', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('Should render title, text and button correctly', () => {
    mockUseSelector.mockReturnValue({ isAuthenticated: false })

    render(<AdoptionCallSection />)

    expect(
      screen.getByRole('heading', {
        name: /Compartilhe amor: anuncie um pet para adoção!/i
      })
    ).toBeInTheDocument()

    expect(
      screen.getByText(/Se você conhece um cão, gato ou outro bichinho/i)
    ).toBeInTheDocument()

    expect(
      screen.getByRole('button', { name: /Cadastrar um animalzinho/i })
    ).toBeInTheDocument()

    expect(screen.queryByText('Necessário login!')).not.toBeInTheDocument()
  })

  test('Should navigate to /registerAnimal when button is clicked AND user is authenticated', async () => {
    mockUseSelector.mockReturnValue({ isAuthenticated: true })

    render(<AdoptionCallSection />)

    const button = screen.getByRole('button', {
      name: /Cadastrar um animalzinho/i
    })

    await userEvent.click(button)

    expect(mockNavigate).toHaveBeenCalledWith('/registerAnimal')
    expect(screen.queryByText('Necessário login!')).not.toBeInTheDocument()
  })

  test('Should open Modal when button is clicked AND user is NOT authenticated', async () => {
    mockUseSelector.mockReturnValue({ isAuthenticated: false })

    render(<AdoptionCallSection />)

    const button = screen.getByRole('button', {
      name: /Cadastrar um animalzinho/i
    })

    await userEvent.click(button)

    expect(mockNavigate).not.toHaveBeenCalled()

    expect(screen.getByText('Necessário login!')).toBeInTheDocument()
    expect(screen.getByText(/Para acessar está página/i)).toBeInTheDocument()
  })

  test('Should navigate to /login when closing the Modal', async () => {
    mockUseSelector.mockReturnValue({ isAuthenticated: false })

    render(<AdoptionCallSection />)

    await userEvent.click(
      screen.getByRole('button', { name: /Cadastrar um animalzinho/i })
    )

    const closeButton = screen.getByLabelText(/Close/i)
    await userEvent.click(closeButton)

    expect(mockNavigate).toHaveBeenCalledWith('/login')
  })
})
