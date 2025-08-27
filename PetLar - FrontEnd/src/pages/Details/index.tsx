import { useParams } from 'react-router-dom'
import { useTheme } from 'styled-components'

import BackButton from '../../components/BackButton'

import { useApi } from '../../hooks/useApi'
import { formatDate, formatWeight } from '../../utils'

import { Card, Container, Description } from './styles'
import { StyledClipLoader, Line, Button } from '../../styles'

type idParams = {
  id: string
}

const Details = () => {
  const { id } = useParams() as idParams
  const { data, loading, error } = useApi<Animal>(`/dogs/${id}`)
  const theme = useTheme()

  if (loading)
    return (
      <Container>
        <div className="box">
          <StyledClipLoader
            data-testid="clipLoader"
            color={theme.colors.primaryColor}
          />
        </div>
      </Container>
    )

  if (error)
    return (
      <Container>
        <div className="box">
          <i className="fa-solid fa-file-circle-xmark"></i>
          <p className="text">Não encontramos este registro em nosso banco!</p>
        </div>
      </Container>
    )

  return (
    <Container>
      <BackButton path={'/'} />
      <Card>
        <div className="image">
          {data.urlImage != null ? (
            <img src={data.urlImage} alt={data.name} />
          ) : (
            <span className="noImage">
              <i className="fa-solid fa-image"></i>
            </span>
          )}
        </div>
        <div className="content">
          <h2>{data.name}</h2>
          <Line />
          <div className="info">
            <p className="text">
              <b>Idade:</b> {data.age} {data.age > 1 ? 'anos' : 'ano'}
            </p>
            <p className="text">
              <b>Sexo:</b> {data.sex}
            </p>
            <p className="text">
              <b>Raça:</b> {data.breed}
            </p>
            <p className="text">
              <b>Peso:</b> {formatWeight(data.weight)} kg
            </p>
            <p className="text">
              <b>Porte:</b> {data.size}
            </p>
            <p className="text">
              <b>Data de cadastro:</b> {formatDate(data.registrationDate)}
            </p>
          </div>
          <div className="contact">
            <p className="text">
              <b>Registrado por:</b>
            </p>
            <Button>
              Entrar em contato <i className="fa-brands fa-whatsapp"></i>
            </Button>
          </div>
        </div>
      </Card>
      {data.description != null && (
        <Description>
          <h3>Descrição</h3>
          <Line />
          <p>{data.description}</p>
        </Description>
      )}
    </Container>
  )
}

export default Details
