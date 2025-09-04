import { useEffect } from 'react'
import { useDispatch } from 'react-redux'

import Card from '../../components/Card'
import AdoptionCallSection from '../../components/AdoptionCallSection'
import Loader from '../../components/Loader'

import { useApi } from '../../hooks/useApi'
import { changeTheme } from '../../store/reducers/Theme'

import { Container, CardContainer } from './styles'
import { Line } from '../../styles'

const Cat = () => {
  const { data: available, loading, error } = useApi('/cats?status=Disponível')
  const { data: adopted } = useApi('/cats?status=Adotado')
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(changeTheme('cats'))
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
              Parece que não temos nenhum bixano para adoção no momento, volte
              outra hora!
            </p>
          </div>
        )}
        {available.length > 0 && (
          <>
            <p className="text">
              Quer adotar um novo soberano de quatro patas? Esses gatinhos já
              estão prontos para assumir o comando da sua casa:
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
              E olha só esses chefes felinos que já recrutaram humanos dedicados
              para servi-los:
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

export default Cat
