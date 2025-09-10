import { render, screen } from '../../utils/test-utils'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import { useApi } from '../../hooks/useApi'

import Other from '.'

vi.mock('../../hooks/useApi', () => ({
  useApi: vi.fn()
}))

describe('Other page', () => {
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

    render(<Other />)

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

    render(<Other />)

    expect(
      screen.getByText(/Ops... Ocorreu um erro, tente novamente mais tarde!/i)
    ).toBeInTheDocument()
  })

  test('Should show empty message when there are no others available', () => {
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

    render(<Other />)

    expect(
      screen.getByText(/Parece que não temos nenhum bixinho/i)
    ).toBeInTheDocument()
  })

  test('Should render available and adopted others', () => {
    const available = [
      { id: 1, name: 'Bolt', status: 'Disponível', age: 2 },
      { id: 2, name: 'Pernalonga', status: 'Disponível', age: 1 }
    ]
    const adopted = [{ id: 3, name: 'Sansão', status: 'Adotado', age: 4 }]

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

    render(<Other />)

    expect(
      screen.getByText(
        /Que tal adotar um novo amigo para dividir momentos especiais/i
      )
    ).toBeInTheDocument()

    expect(screen.getByText(/Bolt/i)).toBeInTheDocument()
    expect(screen.getByText(/Pernalonga/i)).toBeInTheDocument()

    expect(
      screen.getByText(/Espia só esses fofos que já encontraram um lar/i)
    ).toBeInTheDocument()
    expect(screen.getByText(/Sansão/i)).toBeInTheDocument()
  })
})
