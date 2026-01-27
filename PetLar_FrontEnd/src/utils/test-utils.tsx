import '@testing-library/jest-dom'
import React, { type ReactNode } from 'react'
import { render, type RenderOptions } from '@testing-library/react'
import { Provider } from 'react-redux'
import { MemoryRouter } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'
import { principalTheme } from '../themes'

import store from '../store'

type Props = {
  children: ReactNode
}

// eslint-disable-next-line react-refresh/only-export-components
const ReduxThemeProvider = ({ children }: Props) => {
  return <ThemeProvider theme={principalTheme}>{children}</ThemeProvider>
}

// eslint-disable-next-line react-refresh/only-export-components
const AllProviders = ({ children }: Props) => (
  <MemoryRouter>
    <Provider store={store}>
      <ReduxThemeProvider>{children}</ReduxThemeProvider>
    </Provider>
  </MemoryRouter>
)

const customRender = (ui: React.ReactElement, options?: RenderOptions) =>
  render(ui, { wrapper: AllProviders, ...options })

// eslint-disable-next-line react-refresh/only-export-components
export * from '@testing-library/react'
export { customRender as render }
