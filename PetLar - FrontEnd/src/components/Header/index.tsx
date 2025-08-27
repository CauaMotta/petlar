import { useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'

import PetlarLogo from '../PetlarLogo'

import { changeTheme } from '../../store/reducers/Theme'

import * as S from './styles'

const Header = () => {
  const navigate = useNavigate()
  const dispatch = useDispatch()

  const buttonAction = (params: string) => {
    dispatch(changeTheme(params))
    navigate(`/${params}`)
  }

  return (
    <S.Header>
      <S.Container>
        <div className="logo">
          <PetlarLogo width={40} height={40} />
          <h1>PetLar</h1>
        </div>
        <nav className="nav">
          <button onClick={() => buttonAction('dogs')}>Cachorros</button>
          <button onClick={() => buttonAction('cats')}>Gatos</button>
          <button onClick={() => buttonAction('birds')}>Aves</button>
          <button onClick={() => buttonAction('others')}>Outros</button>
        </nav>
      </S.Container>
    </S.Header>
  )
}

export default Header
