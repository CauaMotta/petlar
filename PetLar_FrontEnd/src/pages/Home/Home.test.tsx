import { render, screen } from '../../utils/test-utils'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import { useApi } from '../../hooks/useApi'

import Dog from '.'

vi.mock('../../hooks/useApi', () => ({
  useApi: vi.fn()
}))

describe('Dog page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('Should render loader when loading', () => {
    ;(useApi as vi.Mock)
      .mockReturnValueOnce({
        data: [],
        loading: true,
        error: false
      })
      .mockReturnValueOnce({})

    render(<Dog />)

    expect(screen.getByTestId('loader')).toBeInTheDocument()
  })

  test('Should render error message when API fails', () => {
    ;(useApi as vi.Mock)
      .mockReturnValueOnce({
        data: [],
        loading: false,
        error: true
      })
      .mockReturnValueOnce({})

    render(<Dog />)

    expect(
      screen.getByText(/Ops... Ocorreu um erro, tente novamente mais tarde!/i)
    ).toBeInTheDocument()
  })

  test('Should show empty message when there are no dogs available', () => {
    ;(useApi as vi.Mock)
      .mockReturnValueOnce({
        data: [],
        loading: false,
        error: false
      })
      .mockReturnValueOnce({
        data: [],
        loading: false,
        error: false
      })

    render(<Dog />)

    expect(
      screen.getByText(/Parece que não temos nenhum doguinho/i)
    ).toBeInTheDocument()
  })

  test('Should render available and adopted dogs', () => {
    const available = [
      { id: 1, name: 'Rex', status: 'Disponível', age: 2 },
      { id: 2, name: 'Luna', status: 'Disponível', age: 1 }
    ]
    const adopted = [{ id: 3, name: 'Bolt', status: 'Adotado', age: 4 }]

    ;(useApi as vi.Mock)
      .mockReturnValueOnce({
        data: available,
        loading: false,
        error: false
      })
      .mockReturnValueOnce({
        data: adopted,
        loading: false,
        error: false
      })

    render(<Dog />)

    expect(
      screen.getByText(/Então você está em busca de um AUmigo/i)
    ).toBeInTheDocument()

    expect(screen.getByText(/Rex/i)).toBeInTheDocument()
    expect(screen.getByText(/Luna/i)).toBeInTheDocument()

    expect(
      screen.getByText(
        /De uma olhada nestes amiguinhos que já conseguiram um lar:/i
      )
    ).toBeInTheDocument()
    expect(screen.getByText(/Bolt/i)).toBeInTheDocument()
  })
})
