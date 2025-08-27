import { useNavigate } from 'react-router-dom'
import { Button } from '../../styles'

import { CallSection } from './styles'

const AdoptionCallSection = () => {
  const navigate = useNavigate()

  return (
    <CallSection>
      <div className="image">
        <img
          src="/public/assets/dog-cat.svg"
          alt="Desenho de um cão e um gato"
        />
      </div>
      <div className="call">
        <h2>
          <b>Compartilhe amor:</b> <br /> anuncie um pet para adoção!
        </h2>
        <p className="text">
          Se você conhece um cão, gato ou outro bichinho que precisa de uma nova
          família, cadastre-o aqui e ajude a transformar vidas! É rápido,
          gratuito e pode mudar o destino de um animal.
        </p>
        <div className="btnContainer">
          <Button onClick={() => navigate('/register')}>
            Cadastrar um animalzinho <i className="fa-solid fa-paw"></i>
          </Button>
        </div>
      </div>
    </CallSection>
  )
}

export default AdoptionCallSection
