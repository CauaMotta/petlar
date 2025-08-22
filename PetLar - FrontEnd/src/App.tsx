import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'
import { useSelector } from 'react-redux'

import Dog from './pages/Dog'
import Header from './components/Header'
import type { RootReducer } from './store'

import { Container, GlobalStyle } from './styles'

function App() {
  const { theme } = useSelector((state: RootReducer) => state.theme)
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

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Header />
      <Container>
        <RouterProvider router={routes} />
      </Container>
    </ThemeProvider>
  )
}

export default App
