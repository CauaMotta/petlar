import { useDispatch } from 'react-redux'

import PetlarLogo from '../PetlarLogo'

import { changeTheme } from '../../store/reducers/Theme'

import * as S from './styles'

const Header = () => {
  const dispatch = useDispatch()

  const buttonAction = (theme: string) => {
    dispatch(changeTheme(theme))
  }

  return (
    <S.Header>
      <S.Container>
        <div className="logo">
          <PetlarLogo width={40} height={40} />
          <h1>PetLar</h1>
        </div>
        <nav className="nav">
          <button onClick={() => buttonAction('dog')}>Cachorros</button>
          <button onClick={() => buttonAction('cat')}>Gatos</button>
          <button onClick={() => buttonAction('bird')}>Aves</button>
          <button onClick={() => buttonAction('other')}>Outros</button>
        </nav>
      </S.Container>
    </S.Header>
  )
}

export default Header
