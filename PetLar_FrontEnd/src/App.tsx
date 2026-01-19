import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'

import Layout from './layouts/DefaultLayout'
import Home from './pages/Home'
import Login from './pages/Login'
import RegisterUser from './pages/RegisterUser'
import Details from './pages/Details'
import RegisterAnimal from './pages/RegisterAnimal'

import { GlobalStyle } from './styles'
import { principalTheme } from './themes'
import ProtectedLayout from './layouts/ProtectedLayout'
import { useSelector } from 'react-redux'
import type { RootReducer } from './store'

function App() {
  const { isAuthenticated } = useSelector((state: RootReducer) => state.auth)

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
          path: '/login',
          element: <Login />
        },
        {
          path: '/register',
          element: <RegisterUser />
        },
        {
          path: '/details/:id',
          element: <Details />
        },
        {
          element: <ProtectedLayout isAuthenticated={isAuthenticated} />,
          children: [
            {
              path: '/registerAnimal',
              element: <RegisterAnimal />
            }
          ]
        }
      ]
    }
  ])

  return (
    <ThemeProvider theme={principalTheme}>
      <GlobalStyle />
      <RouterProvider router={routes} />
    </ThemeProvider>
  )
}

export default App
