import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi } from 'vitest'
import { useDispatch } from 'react-redux'
import * as themeReducer from '../../store/reducers/Theme'

import Header from '.'

vi.mock('react-redux', async () => {
  const actual = await vi.importActual<typeof import('react-redux')>(
    'react-redux'
  )
  return {
    ...actual,
    useDispatch: vi.fn()
  }
})

describe('Header', () => {
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

  test('Should call changeTheme with the correct theme when clicking the button', async () => {
    const mockDispatch = vi.fn()
    ;(useDispatch as unknown as vi.Mock).mockReturnValue(mockDispatch)

    const mockChangeTheme = vi.spyOn(themeReducer, 'changeTheme')
    mockChangeTheme.mockImplementation((theme) => theme as any)

    render(<Header />)

    await userEvent.click(screen.getByText('Cachorros'))
    expect(mockDispatch).toHaveBeenCalledWith('dog')

    await userEvent.click(screen.getByText('Gatos'))
    expect(mockDispatch).toHaveBeenCalledWith('cat')

    await userEvent.click(screen.getByText('Aves'))
    expect(mockDispatch).toHaveBeenCalledWith('bird')

    await userEvent.click(screen.getByText('Outros'))
    expect(mockDispatch).toHaveBeenCalledWith('other')

    expect(mockDispatch).toHaveBeenCalledTimes(4)
  })
})
