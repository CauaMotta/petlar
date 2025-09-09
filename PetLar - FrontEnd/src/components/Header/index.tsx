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
        <div className="logo">
          <PetlarLogo width={40} height={40} />
          <h1>PetLar</h1>
        </div>
        <button
          className="menu"
          onClick={() => setIsActive(!isActive)}
        ></button>
        <div className="navBackground"></div>
        <nav className="nav">
          <button
            onClick={() => {
              setIsActive(false)
              navigate('/dogs')
            }}
          >
            Cachorros
          </button>
          <button
            onClick={() => {
              setIsActive(false)
              navigate('/cats')
            }}
          >
            Gatos
          </button>
          <button
            onClick={() => {
              setIsActive(false)
              navigate('/birds')
            }}
          >
            Aves
          </button>
          <button
            onClick={() => {
              setIsActive(false)
              navigate('/others')
            }}
          >
            Outros
          </button>
        </nav>
      </S.Container>
    </S.Header>
  )
}

export default Header
