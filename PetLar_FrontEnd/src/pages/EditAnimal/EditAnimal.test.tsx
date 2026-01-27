/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen, waitFor } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'
import { useSelector } from 'react-redux'
import { useParams } from 'react-router-dom'

import { useUpdateAnimal, useGetAnimalById } from '../../hooks/useAnimals'
import EditAnimal from '.'

const mockMutate = vi.fn()
const mockReset = vi.fn()
const mockNavigate = vi.fn()

vi.mock('../../hooks/useAnimals', () => ({
  useGetAnimalById: vi.fn(),
  useUpdateAnimal: vi.fn()
}))

vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-router-dom')>()
  return {
    ...actual,
    useParams: vi.fn(),
    useNavigate: () => mockNavigate
  }
})

vi.mock('react-redux', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-redux')>()
  return { ...actual, useSelector: vi.fn() }
})

const mockUser = { id: 'user-123', name: 'Dono do Pet' }

const mockAnimal = {
  id: '1',
  name: 'Rex',
  birthDate: '2020-01-01',
  weight: 5500,
  type: 'CACHORRO',
  sex: 'MACHO',
  size: 'MEDIO',
  imagePath: null,
  description: 'Um cachorro legal.',
  author: { id: 'user-123' }
}

describe('EditAnimal Page', () => {
  beforeEach(async () => {
    vi.clearAllMocks()

    vi.mocked(useParams).mockReturnValue({ id: '1' })
    vi.mocked(useSelector).mockReturnValue({ user: mockUser })

    vi.mocked(useGetAnimalById).mockReturnValue({
      data: mockAnimal,
      isLoading: false
    } as any)

    vi.mocked(useUpdateAnimal).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      error: null,
      isSuccess: false,
      reset: mockReset
    } as any)
  })

  const waitForLoadingToFinish = async () => {
    await waitFor(
      () => {
        expect(screen.getByText(/Editando um Pet/i)).toBeInTheDocument()
      },
      { timeout: 150 }
    )
  }

  test('should redirect if user is not the author of the animal', async () => {
    vi.mocked(useSelector).mockReturnValue({ user: { id: 'impostor-999' } })

    render(<EditAnimal />)
    await waitForLoadingToFinish()

    expect(mockNavigate).toHaveBeenCalledWith(-1)
  })

  test('should render form with populated data correctly', async () => {
    render(<EditAnimal />)
    await waitForLoadingToFinish()

    expect(screen.getByDisplayValue('Rex')).toBeInTheDocument()
    expect(screen.getByDisplayValue('01/01/2020')).toBeInTheDocument()
    expect(screen.getByDisplayValue('5.5')).toBeInTheDocument()
    expect(screen.getByDisplayValue('Um cachorro legal.')).toBeInTheDocument()

    expect(screen.getByText('Cachorro')).toBeInTheDocument()
    expect(screen.getByText('Macho')).toBeInTheDocument()
    expect(screen.getByText('Médio')).toBeInTheDocument()
  })

  test('should show validation errors for required fields', async () => {
    const user = userEvent.setup()
    render(<EditAnimal />)
    await waitForLoadingToFinish()

    const nameInput = screen.getByLabelText(/Nome/i)
    await user.clear(nameInput)
    await user.tab()

    expect(
      await screen.findByText(/Este campo é obrigatório/i)
    ).toBeInTheDocument()
  })

  test('should validate logic for future dates', async () => {
    const user = userEvent.setup()
    render(<EditAnimal />)
    await waitForLoadingToFinish()

    const dateInput = screen.getByLabelText(/Data de nascimento/i)
    await user.clear(dateInput)
    await user.type(dateInput, '01/01/2099')
    await user.tab()

    expect(
      await screen.findByText(/A data não pode estar no futuro/i)
    ).toBeInTheDocument()
  })

  test('should handle file upload correctly', async () => {
    const user = userEvent.setup()
    render(<EditAnimal />)
    await waitForLoadingToFinish()

    const file = new File(['hello'], 'new-photo.png', { type: 'image/png' })
    const input = screen.getByLabelText(
      /Adicionar uma imagem|Substituir a imagem/i
    )

    await user.upload(input, file)

    expect(screen.getByText('new-photo.png')).toBeInTheDocument()
  })

  test('should submit the form with correct FormData structure', async () => {
    const user = userEvent.setup()
    render(<EditAnimal />)
    await waitForLoadingToFinish()

    const nameInput = screen.getByLabelText(/Nome/i)
    await user.clear(nameInput)
    await user.type(nameInput, 'Rex Atualizado')

    const submitBtn = screen.getByRole('button', { name: /Atualizar/i })
    await user.click(submitBtn)

    await waitFor(() => {
      expect(mockMutate).toHaveBeenCalledTimes(1)
    })

    const callArg = mockMutate.mock.calls[0][0]
    expect(callArg.id).toBe('1')
    expect(callArg.formData).toBeInstanceOf(FormData)
  })

  test('should show success modal and navigate back on close', async () => {
    let isSuccess = false
    vi.mocked(useUpdateAnimal).mockImplementation(
      () =>
        ({
          mutate: () => {
            isSuccess = true
          },
          isPending: false,
          error: null,
          isSuccess,
          reset: mockReset
        }) as any
    )

    const user = userEvent.setup()
    render(<EditAnimal />)
    await waitForLoadingToFinish()

    const nameInput = await screen.findByLabelText(/Nome/i)
    await user.clear(nameInput)
    await user.type(nameInput, 'Rex Atualizado')

    await user.click(screen.getByRole('button', { name: /Atualizar/i }))

    expect(
      await screen.getByText(/Seu bixinho foi atualizado com sucesso!/i)
    ).toBeInTheDocument()

    await user.click(screen.getByTestId('overlay'))

    expect(mockReset).toHaveBeenCalled()
    expect(mockNavigate).toHaveBeenCalledWith(-1)
  })

  test('should show error modal if mutation fails', async () => {
    vi.mocked(useUpdateAnimal).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      error: { message: 'Erro na API' },
      isSuccess: false,
      reset: mockReset
    } as any)

    render(<EditAnimal />)
    await waitForLoadingToFinish()

    expect(
      screen.getByText(/Parece que tivemos um problema/i)
    ).toBeInTheDocument()
  })
})
