import { render, screen } from '../../utils/test-utils'
import { describe, expect, test, vi } from 'vitest'

import Home from '.'
import userEvent from '@testing-library/user-event'

const mockNavigate = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

describe('Home page', () => {
  test('Should render title, image and main texts', () => {
    render(<Home />)

    expect(
      screen.getByRole('heading', { name: /bem-vindo ao petlar!/i })
    ).toBeInTheDocument()

    expect(
      screen.getByAltText(/garoto rodeado de animais/i)
    ).toBeInTheDocument()

    expect(screen.getByText(/encontre um novo amigo/i)).toBeInTheDocument()
  })

  test('should render navigation buttons with icons and text', () => {
    render(<Home />)

    expect(
      screen.getByRole('button', { name: /cachorros/i })
    ).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /gatos/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /aves/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /outros/i })).toBeInTheDocument()
  })

  test('Should navigate correctly when clicking on buttons', async () => {
    render(<Home />)

    await userEvent.click(screen.getByRole('button', { name: /cachorros/i }))
    expect(mockNavigate).toHaveBeenCalledWith('/dogs')

    await userEvent.click(screen.getByRole('button', { name: /gatos/i }))
    expect(mockNavigate).toHaveBeenCalledWith('/cats')

    await userEvent.click(screen.getByRole('button', { name: /aves/i }))
    expect(mockNavigate).toHaveBeenCalledWith('/birds')

    await userEvent.click(screen.getByRole('button', { name: /outros/i }))
    expect(mockNavigate).toHaveBeenCalledWith('/others')

    expect(mockNavigate).toHaveBeenCalledTimes(4)
  })

  test('Should render project information at the bottom of the page', () => {
    render(<Home />)

    expect(
      screen.getByRole('heading', { name: /sobre este projeto/i })
    ).toBeInTheDocument()
    expect(
      screen.getByText(
        /projeto fictício, desenvolvido inteiramente por cauã motta/i
      )
    ).toBeInTheDocument()
  })
})
