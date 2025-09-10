import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'
import { useSelector } from 'react-redux'

import Layout from './pages/Layout'
import Home from './pages/Home'
import Dog from './pages/Dog'
import Cat from './pages/Cat'
import Bird from './pages/Bird'
import Other from './pages/Other'
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
          element: <Home />
        },
        {
          path: '/dogs',
          element: <Dog />
        },
        {
          path: '/cats',
          element: <Cat />
        },
        {
          path: '/birds',
          element: <Bird />
        },
        {
          path: '/others',
          element: <Other />
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
