import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi, beforeEach } from 'vitest'

import PageCounter from '.'

const mockSetCurrentPage = vi.fn()

describe('PageCounter', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    window.scrollTo = vi.fn()
  })

  test('should not render anything if totalPages is less than or equal to 1', () => {
    const { container } = render(
      <PageCounter
        totalPages={1}
        currentPage={1}
        setCurrentPage={mockSetCurrentPage}
      />
    )

    expect(container.firstChild).toBeNull()
  })

  test('should render the correct number of page buttons', () => {
    render(
      <PageCounter
        totalPages={5}
        currentPage={1}
        setCurrentPage={mockSetCurrentPage}
      />
    )

    const buttons = screen.getAllByRole('button')
    expect(buttons).toHaveLength(7)

    expect(screen.getByText('1')).toBeInTheDocument()
    expect(screen.getByText('5')).toBeInTheDocument()
  })

  test('should call setCurrentPage and scrollTo top when a page number is clicked', async () => {
    render(
      <PageCounter
        totalPages={3}
        currentPage={1}
        setCurrentPage={mockSetCurrentPage}
      />
    )

    const secondPageButton = screen.getByText('2')
    await userEvent.click(secondPageButton)

    expect(mockSetCurrentPage).toHaveBeenCalledWith(2)
    expect(window.scrollTo).toHaveBeenCalledWith({ top: 0, behavior: 'smooth' })
  })

  test('should disable "previous" button on the first page', () => {
    render(
      <PageCounter
        totalPages={3}
        currentPage={1}
        setCurrentPage={mockSetCurrentPage}
      />
    )

    const prevButton = screen.getByLabelText(/previous-page/i)

    expect(prevButton).toBeDisabled()
  })

  test('should disable "next" button on the last page', () => {
    render(
      <PageCounter
        totalPages={3}
        currentPage={3}
        setCurrentPage={mockSetCurrentPage}
      />
    )

    const nextButton = screen.getByLabelText(/next-page/i)

    expect(nextButton).toBeDisabled()
  })

  test('should navigate to next page when clicking the right arrow', async () => {
    render(
      <PageCounter
        totalPages={3}
        currentPage={1}
        setCurrentPage={mockSetCurrentPage}
      />
    )

    const nextButton = screen.getByLabelText(/next-page/i)

    await userEvent.click(nextButton)
    expect(mockSetCurrentPage).toHaveBeenCalledWith(2)
  })

  test('should apply "active" class to the current page button', () => {
    render(
      <PageCounter
        totalPages={3}
        currentPage={2}
        setCurrentPage={mockSetCurrentPage}
      />
    )

    const activeButton = screen.getByText('2')
    expect(activeButton).toHaveClass('active')

    const inactiveButton = screen.getByText('1')
    expect(inactiveButton).not.toHaveClass('active')
  })
})
