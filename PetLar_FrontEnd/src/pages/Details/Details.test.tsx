/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'
import { useSelector } from 'react-redux'
import { useParams } from 'react-router-dom'

import { useGetAnimalById } from '../../hooks/useAnimals'
import Details from '.'

const mockMutate = vi.fn()
const mockReset = vi.fn()
const mockAnimal = {
  id: '123',
  name: 'Pipoca',
  type: 'Cachorro',
  size: 'Pequeno',
  sex: 'Fêmea',
  weight: 5.5,
  birthDate: '2020-01-01',
  imagePath: '/path/to/img.jpg',
  description: 'Uma cadelinha muito dócil.',
  author: { name: 'Teste' }
}

vi.mock('../../main', () => ({
  API_URL: 'http://localhost'
}))

vi.mock('../../hooks/useAnimals', () => ({
  useGetAnimalById: vi.fn()
}))

vi.mock('../../hooks/useAdoption', () => ({
  useInitAdoption: () => ({
    mutate: mockMutate,
    reset: mockReset,
    isSuccess: false,
    error: null
  })
}))

vi.mock('react-router-dom', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-router-dom')>()
  return {
    ...actual,
    useParams: vi.fn(),
    useNavigate: () => vi.fn()
  }
})

vi.mock('react-redux', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-redux')>()
  return { ...actual, useSelector: vi.fn() }
})

describe('Details Page', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useParams).mockReturnValue({ id: '123' })
    vi.mocked(useGetAnimalById).mockReturnValue({
      data: mockAnimal,
      isLoading: false,
      isError: false
    } as any)
  })

  test('should render loader when data is loading', () => {
    vi.mocked(useSelector).mockReturnValue({ isAuthenticated: true })
    vi.mocked(useGetAnimalById).mockReturnValue({ isLoading: true } as any)

    render(<Details />)
    expect(screen.getByTestId('loader')).toBeInTheDocument()
  })

  test('should render error message when animal is not found', () => {
    vi.mocked(useSelector).mockReturnValue({ isAuthenticated: true })
    vi.mocked(useGetAnimalById).mockReturnValue({
      isError: true,
      data: null,
      isLoading: false
    } as any)

    render(<Details />)
    expect(
      screen.getByText(/Não encontramos este registro/i)
    ).toBeInTheDocument()
  })

  test('should display animal details correctly', () => {
    vi.mocked(useSelector).mockReturnValue({ isAuthenticated: true })

    render(<Details />)

    expect(screen.getByText('Pipoca')).toBeInTheDocument()
    expect(screen.getByText(/Cachorro/i)).toBeInTheDocument()
    expect(screen.getByText(/Uma cadelinha muito dócil/i)).toBeInTheDocument()
    expect(screen.getByText(/Teste/i)).toBeInTheDocument()
  })

  test('should show "Unauthorized" modal if clicking adopt while not authenticated', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({ isAuthenticated: false })

    render(<Details />)

    const adoptBtn = screen.getByRole('button', { name: /Quero Adotar!/i })
    await user.click(adoptBtn)

    expect(screen.getByText(/Necessário autenticação/i)).toBeInTheDocument()
  })

  test('should open adoption modal and submit form when authenticated', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({ isAuthenticated: true })

    render(<Details />)

    const adoptBtn = screen.getByRole('button', { name: /Quero Adotar!/i })
    await user.click(adoptBtn)

    expect(
      screen.getByRole('heading', { name: /Solicitar adoção/i })
    ).toBeInTheDocument()

    const textarea = screen.getByLabelText(/Motivo/i)
    await user.type(textarea, 'Tenho muito espaço e amor.')

    const sendBtn = screen.getByRole('button', { name: /Enviar/i })
    await user.click(sendBtn)

    expect(mockMutate).toHaveBeenCalledWith({
      animalId: '123',
      reason: 'Tenho muito espaço e amor.'
    })
  })

  test('should show validation error if reason is too short', async () => {
    const user = userEvent.setup()
    vi.mocked(useSelector).mockReturnValue({ isAuthenticated: true })

    render(<Details />)

    await user.click(screen.getByRole('button', { name: /Quero Adotar!/i }))
    const textarea = screen.getByLabelText(/Motivo/i)

    await user.type(textarea, 'ab')
    await user.tab()

    expect(
      await screen.findByText(/Deve conter no minímo 3 caracteres/i)
    ).toBeInTheDocument()
  })
})
