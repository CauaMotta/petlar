import { Outlet, useLocation } from 'react-router-dom'

import Header from '../../components/Header'
import Footer from '../../components/Footer'

import { Content, PageWrapper } from './styles'

const Layout = () => {
  const location = useLocation()
  const isHome = location.pathname === '/'

  return (
    <PageWrapper>
      {!isHome && <Header />}
      <Content>
        <Outlet />
      </Content>
      {!isHome && <Footer />}
    </PageWrapper>
  )
}

export default Layout
