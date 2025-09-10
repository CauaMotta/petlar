import { render, screen } from '../../utils/test-utils'
import { describe, expect, test } from 'vitest'

import Loader from '.'

describe('Loader', () => {
  test('Should render the loader', () => {
    render(<Loader />)

    const loader = screen.getByTestId('loader')
    expect(loader).toBeInTheDocument()
  })
})
