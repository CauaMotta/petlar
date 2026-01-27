/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen, waitFor } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'
import * as Formik from 'formik'

import { usePostAnimal } from '../../hooks/useAnimals'
import RegisterAnimal from '.'

const mockMutate = vi.fn()
const mockReset = vi.fn()

vi.mock('../../hooks/useAnimals', () => ({
  usePostAnimal: vi.fn()
}))

vi.mock('../../components/BackButton', () => ({
  default: () => <button>Voltar</button>
}))

describe('RegisterAnimal Page', () => {
  beforeEach(async () => {
    vi.clearAllMocks()

    vi.mocked(usePostAnimal).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      error: null,
      isSuccess: false,
      reset: mockReset
    } as any)
  })

  test('should render the form with empty fields correctly', () => {
    render(<RegisterAnimal />)

    expect(screen.getByText('Cadastre um Pet')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Bob')).toHaveValue('')
    expect(screen.getByText('Cadastrar')).toBeDisabled()
  })

  test('should show validation errors when submitting empty form', async () => {
    const user = userEvent.setup()
    render(<RegisterAnimal />)

    const nameInput = screen.getByLabelText(/Nome/i)
    await user.click(nameInput)
    await user.tab()

    expect(
      await screen.findByText(/Este campo é obrigatório/i)
    ).toBeInTheDocument()
  })

  test('should validate logic for future dates', async () => {
    const user = userEvent.setup()
    render(<RegisterAnimal />)

    const dateInput = screen.getByLabelText(/Data de nascimento/i)
    await user.type(dateInput, '01/01/2099')
    await user.tab()

    expect(
      await screen.findByText(/A data não pode estar no futuro/i)
    ).toBeInTheDocument()
  })

  test('should validate weight must be greater than zero', async () => {
    const user = userEvent.setup()
    render(<RegisterAnimal />)

    const weightInput = screen.getByLabelText(/Peso/i)
    await user.type(weightInput, '0')
    await user.tab()

    expect(
      await screen.findByText(/O peso deve ser maior que 0/i)
    ).toBeInTheDocument()
  })

  test('should fill the form correctly and submit data', async () => {
    const user = userEvent.setup()
    render(<RegisterAnimal />)

    await user.click(screen.getByText('Selecione um animal'))
    await user.click(screen.getByText('Cachorro'))

    await user.type(screen.getByLabelText(/Nome/i), 'Toto')

    await user.type(screen.getByLabelText(/Data de nascimento/i), '01/01/2023')

    const weightInput = screen.getByLabelText(/Peso/i)
    await user.type(weightInput, '5')

    const file = new File(['(⌐□_□)'], 'toto.png', { type: 'image/png' })
    const fileInput = screen.getByLabelText(/Adicionar uma imagem/i)
    await user.upload(fileInput, file)

    await user.type(
      screen.getByLabelText(/Adicionar uma descrição/i),
      'Muito bravo'
    )

    const submitBtn = screen.getByRole('button', { name: /Cadastrar/i })
    expect(submitBtn).toBeEnabled()
    await user.click(submitBtn)

    await waitFor(() => {
      expect(mockMutate).toHaveBeenCalledTimes(1)
    })

    const formDataArg = mockMutate.mock.calls[0][0]
    expect(formDataArg).toBeInstanceOf(FormData)
    expect(formDataArg.has('data')).toBe(true)
    expect(formDataArg.has('image')).toBe(true)
  })

  test('should display success modal when registration works', async () => {
    const useFormikSpy = vi.spyOn(Formik, 'useFormik').mockReturnValue({
      values: {},
      errors: {},
      touched: {},
      resetForm: vi.fn(),
      setFieldValue: vi.fn(),
      handleBlur: vi.fn(),
      handleChange: vi.fn(),
      handleSubmit: vi.fn(),
      isValid: true,
      dirty: true
    } as any)

    vi.mocked(usePostAnimal).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      error: null,
      isSuccess: true,
      reset: mockReset
    } as any)

    render(<RegisterAnimal />)

    expect(
      screen.getByText(/Seu bixinho foi cadastrado com sucesso/i)
    ).toBeInTheDocument()

    const closeBtn = screen.getByRole('button', { name: /Close/i })
    await userEvent.click(closeBtn)

    expect(mockReset).toHaveBeenCalled()

    useFormikSpy.mockRestore()
  })

  test('should display loading modal', async () => {
    vi.mocked(usePostAnimal).mockReturnValue({
      mutate: mockMutate,
      isPending: true,
      error: null,
      isSuccess: false,
      reset: mockReset
    } as any)

    render(<RegisterAnimal />)
    expect(
      screen.getByText(/Estamos cadastrando o seu bixinho/i)
    ).toBeInTheDocument()
  })

  test('should display error modal on failure', async () => {
    vi.mocked(usePostAnimal).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      error: { message: 'Erro' },
      isSuccess: false,
      reset: mockReset
    } as any)

    render(<RegisterAnimal />)
    expect(
      screen.getByText(/Parece que tivemos um problema/i)
    ).toBeInTheDocument()
  })

  test('should reset form when reset button is clicked', async () => {
    const user = userEvent.setup()
    render(<RegisterAnimal />)

    const nameInput = screen.getByLabelText(/Nome/i)
    await user.type(nameInput, 'Nome Errado')

    const resetBtn = screen.getByRole('button', { name: /Resetar/i })
    await user.click(resetBtn)

    expect(nameInput).toHaveValue('')
  })
})
