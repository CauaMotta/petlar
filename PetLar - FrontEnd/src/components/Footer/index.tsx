import CauaLogo from '../CauaLogo'
import PetlarLogo from '../PetlarLogo'

import * as S from './styles'

const Footer = () => (
  <S.Footer>
    <S.Container>
      <div className="logo">
        <div className="petlar">
          <PetlarLogo width={40} height={40} />
          <h3>PetLar</h3>
        </div>
        <hr />
        <div className="ocauamotta">
          <CauaLogo width={40} height={40} />
          <h3>Cauã Motta</h3>
        </div>
      </div>
      <p className="text--small">
        &copy; {new Date().getFullYear()} - Cauã Motta. Todos os direitos
        reservados.
      </p>
    </S.Container>
  </S.Footer>
)

export default Footer
