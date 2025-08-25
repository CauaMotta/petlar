import * as S from './styles'

type Props = {
  animal: Animal
}

const Card = ({ animal }: Props) => {
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
        <hr />
        <p className="text--small">Idade: {animal.age}</p>
        <p className="text--small">Sexo: {animal.sex}</p>
        <p className="text--small">Porte: {animal.size}</p>
        <button disabled={animal.status === 'Adotado' ? true : false}>
          {animal.status === 'Adotado' ? (
            <>Adotado!</>
          ) : (
            <>
              Ver mais <i className="fa-solid fa-arrow-right"></i>
            </>
          )}
        </button>
      </div>
    </S.Card>
  )
}

export default Card
