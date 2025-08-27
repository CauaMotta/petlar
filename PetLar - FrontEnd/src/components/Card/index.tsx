import { useNavigate } from 'react-router-dom'

import * as S from './styles'
import { Button, Line } from '../../styles'

type Props = {
  animal: Animal
}

const Card = ({ animal }: Props) => {
  const navigate = useNavigate()

  return (
    <S.Card>
      <div className="image">
        {animal.urlImage != null ? (
          <img src={animal.urlImage} alt={animal.name} />
        ) : (
          <div data-testid="noImage" className="noImage">
            <i className="fa-solid fa-image"></i>
          </div>
        )}
      </div>
      <div className="content">
        <h2>{animal.name}</h2>
        <Line />
        <p className="text--small">
          <b>Idade:</b> {animal.age} {animal.age > 1 ? 'anos' : 'ano'}
        </p>
        <p className="text--small">
          <b>Sexo:</b> {animal.sex}
        </p>
        <p className="text--small">
          <b>Porte:</b> {animal.size}
        </p>
        <Button
          disabled={animal.status === 'Adotado' ? true : false}
          onClick={() => navigate(`/details/${animal.id}`)}
        >
          {animal.status === 'Adotado' ? (
            <>Adotado!</>
          ) : (
            <>
              Ver mais <i className="fa-solid fa-arrow-right"></i>
            </>
          )}
        </Button>
      </div>
    </S.Card>
  )
}

export default Card
