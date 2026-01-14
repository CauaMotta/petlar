import { useParams } from 'react-router-dom'

import BackButton from '../../components/BackButton'
import Loader from '../../components/Loader'

import { API_URL } from '../../main'
import { useGetAnimalById } from '../../hooks/useGetAnimalById'
import { formatDate, formatWeight } from '../../utils'

import { Card, Container, Description } from './styles'
import { Line, Button } from '../../styles'

type Params = {
  id: string
}

const Details = () => {
  const { id } = useParams() as Params
  const { data, isLoading, isError } = useGetAnimalById({ id })

  if (isLoading)
    return (
      <Container>
        <div className="box">
          <Loader />
        </div>
      </Container>
    )

  if (isError || !data)
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
          {data.imagePath != null ? (
            <img src={API_URL + data.imagePath} alt={data.name} />
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
              <b>Espécie:</b> {data.type}
            </p>
            <p className="text">
              <b>Porte:</b> {data.size}
            </p>
            <p className="text">
              <b>Sexo:</b> {data.sex}
            </p>
            <p className="text">
              <b>Peso:</b> {formatWeight(data.weight)} kg
            </p>
            <p className="text">
              <b>Data de nascimento:</b> {formatDate(data.birthDate)}
            </p>
          </div>
          <div className="contact">
            <p className="text">
              <b>Registrado por:</b> {data.author.name}
            </p>
            <Button>
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
