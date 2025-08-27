import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import { useDispatch } from 'react-redux'
import * as themeReducer from '../../store/reducers/Theme'

import Header from '.'

const mockNavigate = vi.fn()

vi.mock('react-redux', async () => {
  const actual = await vi.importActual<typeof import('react-redux')>(
    'react-redux'
  )
  return {
    ...actual,
    useDispatch: vi.fn()
  }
})
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

  test('Should call changeTheme with the correct theme when clicking the button', async () => {
    const mockDispatch = vi.fn()
    ;(useDispatch as unknown as vi.Mock).mockReturnValue(mockDispatch)

    const mockChangeTheme = vi.spyOn(themeReducer, 'changeTheme')
    mockChangeTheme.mockImplementation((theme) => theme as any)

    render(<Header />)

    await userEvent.click(screen.getByText('Cachorros'))
    expect(mockDispatch).toHaveBeenCalledWith('dogs')

    await userEvent.click(screen.getByText('Gatos'))
    expect(mockDispatch).toHaveBeenCalledWith('cats')

    await userEvent.click(screen.getByText('Aves'))
    expect(mockDispatch).toHaveBeenCalledWith('birds')

    await userEvent.click(screen.getByText('Outros'))
    expect(mockDispatch).toHaveBeenCalledWith('others')

    expect(mockDispatch).toHaveBeenCalledTimes(4)
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
