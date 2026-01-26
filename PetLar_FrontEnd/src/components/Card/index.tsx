import { useNavigate } from 'react-router-dom'

import { formatDateBr } from '../../utils'
import { API_URL } from '../../main'

import * as S from './styles'
import { StyledButton, Line } from '../../styles'

type Props = {
  animal: Animal
}

const Card = ({ animal }: Props) => {
  const navigate = useNavigate()

  return (
    <S.Card>
      <div className="image">
        {animal.imagePath != null ? (
          <img src={API_URL + animal.imagePath} alt={animal.name} />
        ) : (
          <div data-testid="noImage" className="noImage">
            <i className="fa-solid fa-image"></i>
          </div>
        )}
      </div>
      <div className="content">
        <h2 className="title textCenter">{animal.name}</h2>
        <Line />
        <div className="animalInfo">
          <p className="text--small">
            <b>data de nascimento:</b> {formatDateBr(animal.birthDate)}
          </p>
          <p className="text--small">
            <b>espécie:</b> {animal.type}
          </p>
          <p className="text--small">
            <b>Porte:</b> {animal.size}
          </p>
          <p className="text--small">
            <b>Sexo:</b> {animal.sex}
          </p>
        </div>
        <StyledButton
          disabled={animal.status === 'ADOTADO' ? true : false}
          onClick={() => navigate(`/details/${animal.id}`)}
        >
          {animal.status === 'ADOTADO' ? (
            <>Adotado!</>
          ) : (
            <>
              Ver mais <i className="fa-solid fa-eye"></i>
            </>
          )}
        </StyledButton>
      </div>
    </S.Card>
  )
}

export default Card
