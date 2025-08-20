import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi } from 'vitest'

import Header from '.'

const mockChangeTheme = vi.fn()

describe('Header', () => {
  test('Should render logo and title', () => {
    render(<Header changeTheme={mockChangeTheme} />)

    expect(screen.getByText('PetLar')).toBeInTheDocument()
    expect(screen.getByTestId('petlar-logo')).toBeInTheDocument()
  })

  test('Should render the navigation buttons', () => {
    render(<Header changeTheme={mockChangeTheme} />)

    expect(screen.getByText('Cachorros')).toBeInTheDocument()
    expect(screen.getByText('Gatos')).toBeInTheDocument()
    expect(screen.getByText('Aves')).toBeInTheDocument()
    expect(screen.getByText('Outros')).toBeInTheDocument()
  })

  test('Should call changeTheme with the correct theme when clicking the button', async () => {
    render(<Header changeTheme={mockChangeTheme} />)

    await userEvent.click(screen.getByText('Cachorros'))
    expect(mockChangeTheme).toHaveBeenCalledWith('dog')

    await userEvent.click(screen.getByText('Gatos'))
    expect(mockChangeTheme).toHaveBeenCalledWith('cat')

    await userEvent.click(screen.getByText('Aves'))
    expect(mockChangeTheme).toHaveBeenCalledWith('bird')

    await userEvent.click(screen.getByText('Outros'))
    expect(mockChangeTheme).toHaveBeenCalledWith('other')

    expect(mockChangeTheme).toHaveBeenCalledTimes(4)
  })
})
