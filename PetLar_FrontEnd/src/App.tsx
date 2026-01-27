import { useSelector } from 'react-redux'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'

import Layout from './layouts/DefaultLayout'
import ProtectedLayout from './layouts/ProtectedLayout'
import Home from './pages/Home'
import Login from './pages/Login'
import RegisterUser from './pages/RegisterUser'
import Details from './pages/Details'
import EditAnimal from './pages/EditAnimal'
import RegisterAnimal from './pages/RegisterAnimal'
import Profile from './pages/Profile'

import type { RootReducer } from './store'

import { GlobalStyle } from './styles'
import { principalTheme } from './themes'

function App() {
  const { isAuthenticated } = useSelector((state: RootReducer) => state.auth)

  const routes = createBrowserRouter([
    {
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
              path: '/profile',
              element: <Profile />
            },
            {
              path: '/registerAnimal',
              element: <RegisterAnimal />
            },
            {
              path: '/editAnimal/:id',
              element: <EditAnimal />
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
