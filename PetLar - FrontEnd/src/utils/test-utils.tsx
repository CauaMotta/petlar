import React, { type ReactNode } from 'react'
import { render, type RenderOptions } from '@testing-library/react'
import { Provider, useSelector } from 'react-redux'
import { ThemeProvider } from 'styled-components'
import '@testing-library/jest-dom'

import store, { type RootReducer } from '../store'

type Props = {
  children: ReactNode
}

// eslint-disable-next-line react-refresh/only-export-components
const ReduxThemeProvider = ({ children }: Props) => {
  const { theme } = useSelector((state: RootReducer) => state.theme)
  return <ThemeProvider theme={theme}>{children}</ThemeProvider>
}

// eslint-disable-next-line react-refresh/only-export-components
const AllProviders = ({ children }: Props) => (
  <Provider store={store}>
    <ReduxThemeProvider>{children}</ReduxThemeProvider>
  </Provider>
)

const customRender = (ui: React.ReactElement, options?: RenderOptions) =>
  render(ui, { wrapper: AllProviders, ...options })

// eslint-disable-next-line react-refresh/only-export-components
export * from '@testing-library/react'
export { customRender as render }
