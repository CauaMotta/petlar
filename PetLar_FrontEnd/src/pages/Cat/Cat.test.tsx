import { render, screen } from '../../utils/test-utils'
import { beforeEach, describe, expect, test, vi } from 'vitest'
import { useApi } from '../../hooks/useApi'

import Cat from '.'

vi.mock('../../hooks/useApi', () => ({
  useApi: vi.fn()
}))

describe('Cat page', () => {
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

    render(<Cat />)

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

    render(<Cat />)

    expect(
      screen.getByText(/Ops... Ocorreu um erro, tente novamente mais tarde!/i)
    ).toBeInTheDocument()
  })

  test('Should show empty message when there are no cats available', () => {
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

    render(<Cat />)

    expect(
      screen.getByText(/Parece que não temos nenhum bixano/i)
    ).toBeInTheDocument()
  })

  test('Should render available and adopted cats', () => {
    const available = [
      { id: 1, name: 'Mimi', status: 'Disponível', age: 2 },
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

    render(<Cat />)

    expect(
      screen.getByText(/Quer adotar um novo soberano de quatro patas/i)
    ).toBeInTheDocument()

    expect(screen.getByText(/Mimi/i)).toBeInTheDocument()
    expect(screen.getByText(/Luna/i)).toBeInTheDocument()

    expect(
      screen.getByText(/E olha só esses chefes felinos/i)
    ).toBeInTheDocument()
    expect(screen.getByText(/Bolt/i)).toBeInTheDocument()
  })
})
