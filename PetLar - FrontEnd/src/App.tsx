import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'

import Dog from './pages/Dog'

import { dogTheme } from './themes'
import { Container, GlobalStyle } from './styles'

function App() {
  const routes = createBrowserRouter([
    {
      path: '/',
      element: <Dog />
    }
  ])

  return (
    <ThemeProvider theme={dogTheme}>
      <GlobalStyle />
      <Container>
        <RouterProvider router={routes} />
      </Container>
    </ThemeProvider>
  )
}

export default App
