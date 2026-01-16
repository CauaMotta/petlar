import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'

import Layout from './pages/Layout'
import Home from './pages/Home'
import Details from './pages/Details'
// import NewAnimal from './pages/NewAnimal'

import { GlobalStyle } from './styles'
import { principalTheme } from './themes'
import Login from './pages/Login'

function App() {
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
          path: '/details/:id',
          element: <Details />
        }
        // {
        //   path: '/register',
        //   element: <NewAnimal />
        // }
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
