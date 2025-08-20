import { useState } from 'react'
import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'

import Dog from './pages/Dog'
import Header from './components/Header'

import { themesMap } from './themes'
import { Container, GlobalStyle } from './styles'

function App() {
  const [theme, setTheme] = useState(themesMap['dog'])
  const routes = createBrowserRouter([
    {
      path: '/',
      element: <Navigate to="/dogs" replace />
    },
    {
      path: '/dogs',
      element: <Dog />
    }
  ])

  const changeTheme = (themeName: string) => {
    const selectedTheme = themesMap[themeName]
    if (selectedTheme) setTheme(selectedTheme)
  }

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Header changeTheme={changeTheme} />
      <Container>
        <RouterProvider router={routes} />
      </Container>
    </ThemeProvider>
  )
}

export default App
