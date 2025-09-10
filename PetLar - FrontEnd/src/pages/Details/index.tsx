import { useParams } from 'react-router-dom'

import BackButton from '../../components/BackButton'
import Loader from '../../components/Loader'

import { useApi } from '../../hooks/useApi'
import { formatAge, formatDate, formatWeight } from '../../utils'

import { Card, Container, Description } from './styles'
import { Line, Button } from '../../styles'

type idParams = {
  id: string
}

const Details = () => {
  const { id } = useParams() as idParams
  const { data, loading, error } = useApi<Animal>(`/dogs/${id}`)

  const contact = () => {
    window.open(
      `https://wa.me/55${data.phone}?text=Olá, vim pelo anuncio do ${data.name}!`,
      '_blank'
    )
  }

  if (loading)
    return (
      <Container>
        <div className="box">
          <Loader />
        </div>
      </Container>
    )

  if (error)
    return (
      <Container>
        <BackButton path={-1} />
        <div className="box">
          <i className="fa-solid fa-file-circle-xmark"></i>
          <p className="text">Não encontramos este registro em nosso banco!</p>
        </div>
      </Container>
    )

  return (
    <Container>
      <BackButton path={-1} />
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
          <h2 className="title">{data.name}</h2>
          <Line />
          <div className="info">
            <p className="text">
              <b>Idade:</b> {formatAge(data.age)}
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
              <b>Registrado por:</b> {data.author}
            </p>
            <Button onClick={contact}>
              Entrar em contato <i className="fa-brands fa-whatsapp"></i>
            </Button>
          </div>
        </div>
      </Card>
      {data.description != null && (
        <Description>
          <h3 className="title--small">Descrição</h3>
          <Line />
          <p className="text">{data.description}</p>
        </Description>
      )}
    </Container>
  )
}

export default Details
