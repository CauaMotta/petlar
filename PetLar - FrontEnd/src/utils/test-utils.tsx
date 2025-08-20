import React, { type ReactNode } from 'react'
import { render, type RenderOptions } from '@testing-library/react'
import { ThemeProvider } from 'styled-components'
import { dogTheme } from '../themes'
import '@testing-library/jest-dom'

type Props = {
  children: ReactNode
}

// eslint-disable-next-line react-refresh/only-export-components
const AllProviders = ({ children }: Props) => (
  <ThemeProvider theme={dogTheme}>{children}</ThemeProvider>
)

const customRender = (ui: React.ReactElement, options?: RenderOptions) =>
  render(ui, { wrapper: AllProviders, ...options })

// eslint-disable-next-line react-refresh/only-export-components
export * from '@testing-library/react'
export { customRender as render }
