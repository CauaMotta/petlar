import { useNavigate } from 'react-router-dom'

import { birdTheme, catTheme, dogTheme, otherTheme } from '../../themes'

import { Container, HomeButton } from './styles'
import { Line } from '../../styles'

const Home = () => {
  const navigate = useNavigate()

  return (
    <Container>
      <div className="welcome">
        <div className="image">
          <img src="/assets/home-image.svg" alt="Garoto rodeado de animais" />
        </div>
        <h1>Bem-vindo ao PetLar!</h1>
        <p className="text">
          Encontre um novo amigo e dê um lar cheio de carinho.
        </p>
      </div>
      <div className="btnGroup">
        <HomeButton color={dogTheme} onClick={() => navigate('/dogs')}>
          <i className="fa-solid fa-dog"></i> Cachorros
        </HomeButton>
        <HomeButton color={catTheme} onClick={() => navigate('/cats')}>
          <i className="fa-solid fa-cat"></i> Gatos
        </HomeButton>
        <HomeButton color={birdTheme} onClick={() => navigate('/birds')}>
          <i className="fa-solid fa-dove"></i> Aves
        </HomeButton>
        <HomeButton color={otherTheme} onClick={() => navigate('/others')}>
          <i className="fa-solid fa-fish-fins"></i> Outros
        </HomeButton>
      </div>
      <div className="info">
        <h2 className="title--small">Sobre este projeto</h2>
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
      </div>
    </Container>
  )
}

export default Home
