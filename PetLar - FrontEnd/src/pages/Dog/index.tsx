import { useTheme } from 'styled-components'

import Card from '../../components/Card'

import { useApi } from '../../hooks/useApi'

import { Container, CardContainer } from './styles'
import { Line, StyledClipLoader } from '../../styles'
import AdoptionCallSection from '../../components/AdoptionCallSection'

const Dog = () => {
  const {
    data: dogAvailable,
    loading,
    error
  } = useApi('/dogs?status=Disponível')
  const { data: dogAdopted } = useApi('/dogs?status=Adotado')
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
          <p className="text">
            Ops... Ocorreu um erro, tente novamente mais tarde!
          </p>
        </div>
      </Container>
    )

  return (
    <>
      <Container>
        {dogAvailable.length == 0 && (
          <div className="box">
            <p className="text">
              Parece que não temos nenhum doguinho para adoção no momento, volte
              outra hora!
            </p>
          </div>
        )}
        {dogAvailable.length > 0 && (
          <>
            <p className="text">
              Então você está em busca de um AUmigo? De uma olhada nessas
              fofuras que estão a espera de um lar:
            </p>
            <CardContainer>
              {dogAvailable.map((dog) => (
                <Card key={dog.name} animal={dog} />
              ))}
            </CardContainer>
          </>
        )}

        {dogAdopted.length > 0 && (
          <>
            <p className="text">
              De uma olhada nestes amiguinhos que já conseguiram um lar:
            </p>
            <CardContainer>
              {dogAdopted.map((dog) => (
                <Card key={dog.name} animal={dog} />
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

export default Dog
