import { useNavigate } from 'react-router-dom'

import PetlarLogo from '../PetlarLogo'

import * as S from './styles'

const Header = () => {
  const navigate = useNavigate()

  return (
    <S.Header>
      <S.Container>
        <div className="logo">
          <PetlarLogo width={40} height={40} />
          <h1>PetLar</h1>
        </div>
        <nav className="nav">
          <button onClick={() => navigate('/dogs')}>Cachorros</button>
          <button onClick={() => navigate('/cats')}>Gatos</button>
          <button onClick={() => navigate('/birds')}>Aves</button>
          <button onClick={() => navigate('/others')}>Outros</button>
        </nav>
      </S.Container>
    </S.Header>
  )
}

export default Header
