import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import AdoptionCallSection from '.'

const mockNavigate = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>(
    'react-router-dom'
  )
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

describe('AdoptionCallSection', () => {
  beforeEach(() => {
    mockNavigate.mockClear()
  })

  test('Should render title, text and button', () => {
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

    expect(screen.getByAltText('Desenho de um cão e um gato')).toHaveAttribute(
      'src',
      '/assets/dog-cat.svg'
    )
  })

  test('Should navigate to register page when button is clicked', async () => {
    render(<AdoptionCallSection />)

    const button = screen.getByRole('button', {
      name: /Cadastrar um animalzinho/i
    })
    await userEvent.click(button)

    expect(mockNavigate).toHaveBeenCalledWith('/register')
  })
})
