import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'
import { useSelector } from 'react-redux'

import Layout from './pages/Layout'
import Dog from './pages/Dog'
import Details from './pages/Details'
import NewAnimal from './pages/NewAnimal'

import type { RootReducer } from './store'

import { GlobalStyle } from './styles'

function App() {
  const { theme } = useSelector((state: RootReducer) => state.theme)
  const routes = createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      children: [
        {
          path: '/',
          element: <Navigate to="/dogs" replace />
        },
        {
          path: '/dogs',
          element: <Dog />
        },
        {
          path: '/details/:id',
          element: <Details />
        },
        {
          path: '/register',
          element: <NewAnimal />
        }
      ]
    }
  ])

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <RouterProvider router={routes} />
    </ThemeProvider>
  )
}

export default App
