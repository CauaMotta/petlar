import { useNavigate } from 'react-router-dom'

import PetlarLogo from '../PetlarLogo'

import * as S from './styles'
import { useState } from 'react'

const Header = () => {
  const navigate = useNavigate()
  const [isActive, setIsActive] = useState(false)

  return (
    <S.Header>
      <S.Container className={isActive ? 'active' : ''}>
        <div onClick={() => navigate('/')} className="logo">
          <PetlarLogo width={40} height={40} />
          <h1>PetLar</h1>
        </div>
        <button className="menu" onClick={() => setIsActive(!isActive)}>
          <span className="line"></span>
          <span className="line"></span>
          <span className="line"></span>
        </button>
        <div className="navBackground"></div>
        <nav className="nav">
          <button
            onClick={() => {
              setIsActive(false)
              navigate('/login')
            }}
          >
            Login/Cadastro{' '}
            <i className="fa-solid fa-arrow-right-to-bracket"></i>
          </button>
        </nav>
      </S.Container>
    </S.Header>
  )
}

export default Header
