import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import BackButton from '.'

const mockNavigate = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>(
    'react-router-dom'
  )
  return {
    ...actual,
    useNavigate: () => mockNavigate
  }
})

describe('BackButton', () => {
  beforeEach(() => {
    mockNavigate.mockClear()
  })

  test('Should navigate with string path', async () => {
    render(<BackButton path="/home" />)

    await userEvent.click(
      screen.getByRole('button', { name: /Voltar a página anterior/i })
    )
    expect(mockNavigate).toHaveBeenCalledWith('/home')
  })

  test('Should navigate with number path', async () => {
    render(<BackButton path={-1} />)

    await userEvent.click(
      screen.getByRole('button', { name: /Voltar a página anterior/i })
    )
    expect(mockNavigate).toHaveBeenCalledWith(-1)
  })
})
