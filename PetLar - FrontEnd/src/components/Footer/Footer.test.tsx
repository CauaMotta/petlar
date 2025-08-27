import { describe, expect, test } from 'vitest'

import { render, screen } from '../../utils/test-utils'

import Footer from '.'

describe('Footer', () => {
  test('Should render the PetLar Logo', () => {
    render(<Footer />)

    expect(screen.getByText(/PetLar/i)).toBeInTheDocument()
  })

  test('Should render the Cauã Logo', () => {
    render(<Footer />)

    expect(
      screen.getByRole('heading', { name: /Cauã Motta/i })
    ).toBeInTheDocument()
  })

  test('Should render the copyright with the current year', () => {
    render(<Footer />)

    const currentYear = new Date().getFullYear()
    expect(
      screen.getByText(
        `© ${currentYear} - Cauã Motta. Todos os direitos reservados.`
      )
    ).toBeInTheDocument()
  })

  test('Should render the SVG logotype', () => {
    render(<Footer />)

    expect(screen.getByTestId('petlar-logo')).toBeInTheDocument()
    expect(screen.getByTestId('caua-logo')).toBeInTheDocument()
  })
})
