import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { describe, expect, test, vi } from 'vitest'

import StyledSelectWrapper from '.'

const mockOptions = [
  { value: 'dog', label: 'Dog' },
  { value: 'cat', label: 'Cat' }
]
const mockOnChange = vi.fn()
const placeholder = 'Select an animal'

describe('StyledSelectWrapper', () => {
  test('should render with the correct placeholder', () => {
    render(
      <StyledSelectWrapper
        options={mockOptions}
        value={null}
        onChange={mockOnChange}
        placeholder={placeholder}
      />
    )

    expect(screen.getByText(placeholder)).toBeInTheDocument()
  })

  test('should show options when clicked', async () => {
    const user = userEvent.setup()
    render(
      <StyledSelectWrapper
        options={mockOptions}
        value={null}
        onChange={mockOnChange}
        placeholder={placeholder}
      />
    )

    const selectControl = screen.getByText(placeholder)
    await user.click(selectControl)

    expect(screen.getByText('Dog')).toBeInTheDocument()
    expect(screen.getByText('Cat')).toBeInTheDocument()
  })

  test('should call onChange when an option is selected', async () => {
    const user = userEvent.setup()
    render(
      <StyledSelectWrapper
        options={mockOptions}
        value={null}
        onChange={mockOnChange}
        placeholder={placeholder}
      />
    )

    await user.click(screen.getByText(placeholder))

    const option = screen.getByText('Dog')
    await user.click(option)

    expect(mockOnChange).toHaveBeenCalledWith(
      expect.objectContaining({ value: 'dog', label: 'Dog' }),
      expect.anything()
    )
  })

  test('should display the currently selected value', () => {
    const selectedOption = mockOptions[1]

    render(
      <StyledSelectWrapper
        options={mockOptions}
        value={selectedOption}
        onChange={mockOnChange}
        placeholder={placeholder}
      />
    )

    expect(screen.getByText('Cat')).toBeInTheDocument()
    expect(screen.queryByText(placeholder)).not.toBeInTheDocument()
  })
})
