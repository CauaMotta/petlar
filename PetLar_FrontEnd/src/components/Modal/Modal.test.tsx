import { render, screen } from '../../utils/test-utils'
import userEvent from '@testing-library/user-event'
import { beforeEach, describe, expect, test, vi } from 'vitest'

import Modal from '.'

const mockOnClose = vi.fn()
const modalTitle = 'Test Modal Title'
const modalContent = 'Modal Child Content'

describe('Modal', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('should not render anything when isOpen is false', () => {
    const { container } = render(
      <Modal isOpen={false} onClose={mockOnClose} title={modalTitle}>
        <div>{modalContent}</div>
      </Modal>
    )

    expect(container.firstChild).toBeNull()
    expect(screen.queryByText(modalTitle)).not.toBeInTheDocument()
  })

  test('should render title and children correctly when isOpen is true', () => {
    render(
      <Modal isOpen={true} onClose={mockOnClose} title={modalTitle}>
        <div data-testid="child-content">{modalContent}</div>
      </Modal>
    )

    expect(screen.getByText(modalTitle)).toBeInTheDocument()
    expect(screen.getByTestId('child-content')).toHaveTextContent(modalContent)
  })

  test('should call onClose when close button is clicked', async () => {
    render(
      <Modal isOpen={true} onClose={mockOnClose} title={modalTitle}>
        <div>{modalContent}</div>
      </Modal>
    )

    const closeButton = screen.getByRole('button', { name: /close/i })
    await userEvent.click(closeButton)

    expect(mockOnClose).toHaveBeenCalledTimes(1)
  })

  test('should call onClose when overlay is clicked', async () => {
    render(
      <Modal isOpen={true} onClose={mockOnClose} title={modalTitle}>
        <div>{modalContent}</div>
      </Modal>
    )

    const overlay = screen.getByTestId('overlay')
    await userEvent.click(overlay)

    expect(mockOnClose).toHaveBeenCalledTimes(1)
  })

  test('should render the Line component inside the modal', () => {
    // Note: Line is a styled component, we check for its presence in the DOM
    const { container } = render(
      <Modal isOpen={true} onClose={mockOnClose} title={modalTitle}>
        <div>{modalContent}</div>
      </Modal>
    )

    // Se o Line for renderizado como uma <hr> ou div específica pelo estilo,
    // podemos buscar pela estrutura ou classe.
    // Como ele está entre o header e o conteúdo:
    const hr =
      container.querySelector('hr') || container.querySelector('.sc-hvigdm')
    expect(hr).toBeInTheDocument()
  })
})
