import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import Header from '.'

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

describe('Header', () => {
  beforeEach(() => {
    mockNavigate.mockClear()
  })

  test('Should render logo and title', () => {
    render(<Header />)

    expect(screen.getByText('PetLar')).toBeInTheDocument()
    expect(screen.getByTestId('petlar-logo')).toBeInTheDocument()
  })

  test('Should render the navigation buttons', () => {
    render(<Header />)

    expect(screen.getByText('Cachorros')).toBeInTheDocument()
    expect(screen.getByText('Gatos')).toBeInTheDocument()
    expect(screen.getByText('Aves')).toBeInTheDocument()
    expect(screen.getByText('Outros')).toBeInTheDocument()
  })

  test('Should call navigate when nav button is clicked', async () => {
    render(<Header />)

    await userEvent.click(screen.getByText('Cachorros'))
    expect(mockNavigate).toHaveBeenCalledWith('/dogs')

    await userEvent.click(screen.getByText('Gatos'))
    expect(mockNavigate).toHaveBeenCalledWith('/cats')

    await userEvent.click(screen.getByText('Aves'))
    expect(mockNavigate).toHaveBeenCalledWith('/birds')

    await userEvent.click(screen.getByText('Outros'))
    expect(mockNavigate).toHaveBeenCalledWith('/others')

    expect(mockNavigate).toHaveBeenCalledTimes(4)
  })
})
