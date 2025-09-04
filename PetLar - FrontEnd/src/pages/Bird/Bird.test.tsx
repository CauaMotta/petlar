import { render, screen } from '../../utils/test-utils'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import { useApi } from '../../hooks/useApi'

import Bird from '.'

vi.mock('../../hooks/useApi', () => ({
  useApi: vi.fn()
}))

describe('Bird page', () => {
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

    render(<Bird />)

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

    render(<Bird />)

    expect(
      screen.getByText(/Ops... Ocorreu um erro, tente novamente mais tarde!/i)
    ).toBeInTheDocument()
  })

  test('Should show empty message when there are no birds available', () => {
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

    render(<Bird />)

    expect(
      screen.getByText(/Parece que não temos nenhum pássaro/i)
    ).toBeInTheDocument()
  })

  test('Should render available and adopted birds', () => {
    const available = [
      { id: 1, name: 'Loro', status: 'Disponível', age: 2 },
      { id: 2, name: 'José', status: 'Disponível', age: 1 }
    ]
    const adopted = [{ id: 3, name: 'Paraguaio', status: 'Adotado', age: 4 }]

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

    render(<Bird />)

    expect(
      screen.getByText(/Quer adotar um novo maestro de penas/i)
    ).toBeInTheDocument()

    expect(screen.getByText(/Loro/i)).toBeInTheDocument()
    expect(screen.getByText(/José/i)).toBeInTheDocument()

    expect(
      screen.getByText(/De uma olhada nessas estrelas aladas/i)
    ).toBeInTheDocument()
    expect(screen.getByText(/Paraguaio/i)).toBeInTheDocument()
  })
})
