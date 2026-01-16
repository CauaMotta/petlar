import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'

import PetlarLogo from '../PetlarLogo'

import type { RootReducer } from '../../store'
import { logout } from '../../store/reducers/authSlice'

import * as S from './styles'

const Header = () => {
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const [isActive, setIsActive] = useState(false)
  const { user, isAuthenticated } = useSelector(
    (state: RootReducer) => state.auth
  )

  if (isAuthenticated)
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
            <div className="profileBtn">
              <div>
                <p className="text">
                  <span>Olá,</span> <br /> {user?.name}
                </p>
              </div>
            </div>
            <hr />
            <button
              onClick={() => {
                setIsActive(false)
                navigate('/profile')
              }}
            >
              Meu Perfil <i className="fa-solid fa-circle-user"></i>
            </button>
            <button
              onClick={() => {
                setIsActive(false)
                dispatch(logout())
                navigate('/')
              }}
            >
              Sair <i className="fa-solid fa-arrow-right-to-bracket"></i>
            </button>
          </nav>
        </S.Container>
      </S.Header>
    )

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
