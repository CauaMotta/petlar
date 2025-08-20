import PetlarLogo from '../PetlarLogo'
import * as S from './styles'

type Props = {
  changeTheme: (themeName: string) => void
}

const Header = ({ changeTheme }: Props) => (
  <S.Header>
    <S.Container>
      <div className="logo">
        <PetlarLogo width={40} height={40} />
        <h1>PetLar</h1>
      </div>
      <nav className="nav">
        <button onClick={() => changeTheme('dog')}>Cachorros</button>
        <button onClick={() => changeTheme('cat')}>Gatos</button>
        <button onClick={() => changeTheme('bird')}>Aves</button>
        <button onClick={() => changeTheme('other')}>Outros</button>
      </nav>
    </S.Container>
  </S.Header>
)

export default Header
