import Card from '../../components/Card'

import { useApi } from '../../hooks/useApi'

import { Container, CardContainer } from './styles'
import { StyledClipLoader } from '../../styles'
import { useTheme } from 'styled-components'

const Dog = () => {
  const { data, loading, error } = useApi('/dogs')
  const theme = useTheme()

  return (
    <Container>
      <p className="text">
        Então você está em busca de um AUmigo? De uma olhada nessas fofuras que
        estão a espera de um lar:
      </p>
      <CardContainer>
        {loading && <StyledClipLoader color={theme.colors.primaryColor} />}
        {error && (
          <div className="error">
            <i className="fa-solid fa-file-circle-xmark"></i>
            <p className="text">
              Ops... Ocorreu um erro, tente novamente mais tarde!
            </p>
          </div>
        )}
        {data.map((dog) => (
          <Card animal={dog} />
        ))}
      </CardContainer>
    </Container>
  )
}

export default Dog
