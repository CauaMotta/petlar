import { Outlet } from 'react-router-dom'

import Header from '../../components/Header'
import Footer from '../../components/Footer'

import { Content, PageWrapper } from './styles'

const Layout = () => (
  <PageWrapper>
    <Header />
    <Content>
      <Outlet />
    </Content>
    <Footer />
  </PageWrapper>
)

export default Layout
