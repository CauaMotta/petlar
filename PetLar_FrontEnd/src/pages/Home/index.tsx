import Card from '../../components/Card'
import AdoptionCallSection from '../../components/AdoptionCallSection'
import Loader from '../../components/Loader'

import { useAnimalsApi } from '../../hooks/useAnimalsApi'

import { Container, CardContainer, CardInfo } from './styles'
import { Line } from '../../styles'

const Home = () => {
  const {
    data: available,
    isLoading,
    isError
  } = useAnimalsApi('?status=disponivel')
  const { data: adopted } = useAnimalsApi('?status=adotado')

  if (isLoading)
    return (
      <Container>
        <div className="box">
          <Loader />
        </div>
      </Container>
    )

  if (isError)
    return (
      <Container>
        <div className="box">
          <i className="fa-solid fa-file-circle-xmark"></i>
          <p className="text">
            Ops...Ocorreu um erro, tente novamente mais tarde!
          </p>
        </div>
      </Container>
    )

  return (
    <>
      <Container>
        {available?.length == 0 && (
          <div className="box">
            <p className="text">
              Parece que não temos nenhum pet para adoção no momento, volte
              outra hora!
            </p>
          </div>
        )}
        {available && available.length > 0 && (
          <>
            <p className="text">
              Então você está em busca de um AUmigo? De uma olhada nessas
              fofuras que estão a espera de um lar:
            </p>
            <CardContainer>
              {available.map((entity) => (
                <Card key={entity.name} animal={entity} />
              ))}
            </CardContainer>
          </>
        )}

        {adopted && adopted.length > 0 && (
          <>
            <p className="text">
              De uma olhada nestes amiguinhos que já conseguiram um lar:
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
      <CardInfo>
        <h2 className="title--small">
          <i className="fa-solid fa-triangle-exclamation"></i> Sobre este
          projeto
        </h2>
        <Line />
        <p className="text">
          Este site não representa uma instituição de adoção real. Trata-se de
          um projeto fictício, desenvolvido inteiramente por Cauã Motta com o
          objetivo de aplicar na prática os conhecimentos adquiridos durante a
          formação como Desenvolvedor Full Stack Java. O projeto foi construído
          do zero, contemplando tanto a parte frontend quanto o backend, e serve
          como vitrine para demonstrar habilidades em programação, arquitetura
          de software e boas práticas de desenvolvimento.
        </p>
      </CardInfo>
    </>
  )
}

export default Home
