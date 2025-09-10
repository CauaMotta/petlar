import { render, screen } from '../../utils/test-utils'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import { useApi } from '../../hooks/useApi'

import Details from '.'
import userEvent from '@testing-library/user-event'

vi.mock('../../hooks/useApi', () => ({
  useApi: vi.fn()
}))

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>(
    'react-router-dom'
  )
  return {
    ...actual,
    useParams: () => ({ id: '1' })
  }
})

describe('Details Page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('Should render loader when loading', () => {
    ;(useApi as vi.Mock).mockReturnValue({
      data: null,
      loading: true,
      error: false
    })

    render(<Details />)

    expect(screen.getByTestId('loader')).toBeInTheDocument()
  })

  test('Should render error message when API fails', () => {
    ;(useApi as vi.Mock).mockReturnValue({
      data: null,
      loading: false,
      error: true
    })

    render(<Details />)

    expect(
      screen.getByText(/Não encontramos este registro em nosso banco!/i)
    ).toBeInTheDocument()
  })

  test('Should render details when API succeeds', () => {
    ;(useApi as vi.Mock).mockReturnValue({
      data: {
        id: 1,
        name: 'Rex',
        age: 2,
        sex: 'Macho',
        breed: 'Vira-lata',
        weight: 12,
        size: 'Médio',
        registrationDate: '2025-01-01',
        urlImage: '/rex.png',
        description: 'Muito brincalhão',
        author: 'Teste',
        phone: '11999999999'
      },
      loading: false,
      error: false
    })

    render(<Details />)

    expect(screen.getByRole('heading', { name: /Rex/i })).toBeInTheDocument()
    expect(screen.getByRole('img', { name: /Rex/i })).toHaveAttribute(
      'src',
      '/rex.png'
    )
    expect(screen.getByText(/Muito brincalhão/i)).toBeInTheDocument()
    expect(
      screen.getByRole('button', { name: /Entrar em contato/i })
    ).toBeInTheDocument()
  })

  test('should open WhatsApp with the correct number and message when clicking the button', async () => {
    ;(useApi as vi.Mock).mockReturnValue({
      data: {
        id: 1,
        name: 'Rex',
        age: 2,
        sex: 'Macho',
        breed: 'Vira-lata',
        weight: 12,
        size: 'Médio',
        registrationDate: '2025-01-01',
        urlImage: '/rex.png',
        description: 'Muito brincalhão',
        author: 'Teste',
        phone: '11999999999'
      },
      loading: false,
      error: false
    })
    const openSpy = vi.spyOn(window, 'open').mockImplementation(() => null)

    render(<Details />)

    const button = screen.getByRole('button', { name: /entrar em contato/i })
    await userEvent.click(button)

    expect(openSpy).toHaveBeenCalledWith(
      'https://wa.me/5511999999999?text=Olá, vim pelo anuncio do Rex!',
      '_blank'
    )

    openSpy.mockRestore()
  })
})
