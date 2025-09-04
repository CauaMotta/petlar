import { useEffect } from 'react'
import { useDispatch } from 'react-redux'

import Card from '../../components/Card'
import AdoptionCallSection from '../../components/AdoptionCallSection'
import Loader from '../../components/Loader'

import { useApi } from '../../hooks/useApi'
import { changeTheme } from '../../store/reducers/Theme'

import { Container, CardContainer } from './styles'
import { Line } from '../../styles'

const Bird = () => {
  const { data: available, loading, error } = useApi('/birds?status=Disponível')
  const { data: adopted } = useApi('/birds?status=Adotado')
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(changeTheme('birds'))
  }, [dispatch])

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
        <div className="box">
          <i className="fa-solid fa-file-circle-xmark"></i>
          <p className="text">
            Ops... Ocorreu um erro, tente novamente mais tarde!
          </p>
        </div>
      </Container>
    )

  return (
    <>
      <Container>
        {available.length == 0 && (
          <div className="box">
            <p className="text">
              Parece que não temos nenhum pássaro para adoção no momento, volte
              outra hora!
            </p>
          </div>
        )}
        {available.length > 0 && (
          <>
            <p className="text">
              Quer adotar um novo maestro de penas? Essas aves já estão prontas
              para comandar o show de cantoria na sua casa:
            </p>
            <CardContainer>
              {available.map((entity) => (
                <Card key={entity.name} animal={entity} />
              ))}
            </CardContainer>
          </>
        )}

        {adopted.length > 0 && (
          <>
            <p className="text">
              De uma olhada nessas estrelas aladas que já encontraram humanos
              para serem acordados todos os dias com seus cantos:
            </p>
            <CardContainer>
              {adopted.map((entity) => (
                <Card key={entity.name} animal={entity} />
              ))}
            </CardContainer>
          </>
        )}
        <Line />
      </Container>
      <AdoptionCallSection />
    </>
  )
}

export default Bird
