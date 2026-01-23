import { useState } from 'react'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'

import Modal from '../Modal'

import type { RootReducer } from '../../store'

import { CallSection } from './styles'
import { StyledButton } from '../../styles'

const AdoptionCallSection = () => {
  const [activeModal, setActiveModal] = useState<boolean>(false)
  const navigate = useNavigate()
  const { isAuthenticated } = useSelector((state: RootReducer) => state.auth)

  const handleRedirect = () => {
    if (isAuthenticated) {
      navigate('/registerAnimal')
    } else {
      setActiveModal(true)
    }
  }

  return (
    <CallSection>
      <div className="image">
        <img src="/assets/dog-cat.svg" alt="Desenho de um cão e um gato" />
      </div>
      <div className="call">
        <h2 className="title">
          <b className="title--small">Compartilhe amor:</b> <br /> anuncie um
          pet para adoção!
        </h2>
        <p className="text">
          Se você conhece um cão, gato ou outro bichinho que precisa de uma nova
          família, cadastre-o aqui e ajude a transformar vidas! É rápido,
          gratuito e pode mudar o destino de um animal.
        </p>
        <div className="btnContainer">
          <StyledButton $maxWidth="fit-content" onClick={handleRedirect}>
            Cadastrar um animalzinho <i className="fa-solid fa-paw"></i>
          </StyledButton>
        </div>
      </div>

      <Modal
        title="Necessário login!"
        onClose={() => {
          setActiveModal(false)
          navigate('/login')
        }}
        isOpen={activeModal}
      >
        <p className="text">
          Para acessar está página <br /> você precisa estar autenticado
        </p>
      </Modal>
    </CallSection>
  )
}

export default AdoptionCallSection
