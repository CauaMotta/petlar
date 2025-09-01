import { render, screen, waitFor } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import { useApiMutation } from '../../hooks/useApiMutation'

import NewAnimal from '.'

const mockNavigate = vi.fn()

vi.mock('../../hooks/useApiMutation', () => ({
  useApiMutation: vi.fn()
}))
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>(
    'react-router-dom'
  )
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

describe('NewAnimal Page', () => {
  beforeEach(() => {
    ;(useApiMutation as unknown as vi.Mock).mockReturnValue({
      mutate: vi.fn(),
      data: null,
      loading: false,
      error: null
    })
  })

  test('Should render the component', async () => {
    render(<NewAnimal />)

    await waitFor(() => {
      expect(screen.getByText('Cadastre um Pet')).toBeInTheDocument()
      expect(screen.getAllByLabelText(/Nome:/i)).toHaveLength(2)
      expect(screen.getByLabelText(/Idade:/i)).toBeInTheDocument()
      expect(screen.getByLabelText(/Raça:/i)).toBeInTheDocument()
      expect(screen.getByLabelText(/Peso:/i)).toBeInTheDocument()
      expect(
        screen.getByLabelText(/Whatsapp para contato:/i)
      ).toBeInTheDocument()
    })
  })

  test('Should not allow submit if required fields are empty', async () => {
    render(<NewAnimal />)

    const submitBtn = screen.getByRole('button', { name: /Cadastrar/i })
    await waitFor(() => {
      expect(submitBtn).toBeDisabled()
    })
  })

  test('Should display error when age is 0', async () => {
    render(<NewAnimal />)

    const ageInput = screen.getByLabelText(/Idade:/i)
    await userEvent.type(ageInput, '0')
    await userEvent.tab()

    await waitFor(() => {
      expect(
        screen.getByText(/A idade deve ser maior que 0/i)
      ).toBeInTheDocument()
    })
  })

  test('Should call mutate with correct payload', async () => {
    const mutateMock = vi.fn()

    ;(useApiMutation as unknown as vi.Mock).mockReturnValue({
      mutate: mutateMock,
      data: null,
      loading: false,
      error: null
    })

    render(<NewAnimal />)

    const typeSelect = screen.getByText(/Selecione um animal/i)
    await userEvent.click(typeSelect)
    const dogOption = await screen.findByText('Cachorro')
    await userEvent.click(dogOption)

    await userEvent.type(screen.getByPlaceholderText('Bob'), 'Bob')
    await userEvent.type(screen.getByLabelText(/Idade:/i), '2')
    await userEvent.type(screen.getByLabelText(/Raça:/i), 'Vira-lata')
    await userEvent.type(screen.getByLabelText(/Peso:/i), '5')
    await userEvent.type(
      screen.getByPlaceholderText('Digite seu nome'),
      'Teste'
    )
    await userEvent.type(
      screen.getByLabelText(/Whatsapp para contato:/i),
      '11999999999'
    )

    const submitBtn = screen.getByRole('button', { name: /Cadastrar/i })
    await userEvent.click(submitBtn)

    await waitFor(() => {
      expect(mutateMock).toHaveBeenCalledWith({
        method: 'POST',
        endpoint: expect.any(String),
        body: expect.objectContaining({
          name: 'Bob',
          age: 24,
          breed: 'Vira-lata',
          sex: 'macho',
          weight: 500,
          size: 'pequeno',
          urlImage: '',
          description: '',
          author: 'Teste',
          phone: '11999999999'
        })
      })
    })
  })

  test('Should render the loader when loading = true', async () => {
    ;(useApiMutation as unknown as vi.Mock).mockReturnValue({
      mutate: vi.fn(),
      data: null,
      loading: true,
      error: null
    })

    render(<NewAnimal />)
    await waitFor(() => {
      expect(
        screen.getByText(/Estamos cadastrando o seu bixinho/i)
      ).toBeInTheDocument()
    })
  })

  test('Should render error message when error = true', async () => {
    ;(useApiMutation as unknown as vi.Mock).mockReturnValue({
      mutate: vi.fn(),
      data: null,
      loading: false,
      error: true
    })

    render(<NewAnimal />)
    await waitFor(() => {
      expect(
        screen.getByText(/problema ao fazer o cadastro/i)
      ).toBeInTheDocument()
    })
  })

  test('Should render success message when data exists', async () => {
    ;(useApiMutation as unknown as vi.Mock).mockReturnValue({
      mutate: vi.fn(),
      data: [],
      loading: false,
      error: null
    })

    render(<NewAnimal />)
    await waitFor(() => {
      expect(screen.getByText(/cadastrado com sucesso/i)).toBeInTheDocument()
    })
  })
})
