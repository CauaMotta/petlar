import { useState } from 'react'

import Card from '../../components/Card'
import AdoptionCallSection from '../../components/AdoptionCallSection'
import Loader from '../../components/Loader'
import StyledSelectWrapper from '../../components/StyledSelectWrapper'

import { useGetAllAnimals } from '../../hooks/useAnimals'

import { Container, CardContainer, CardInfo } from './styles'
import { Line } from '../../styles'

const options = [
  { value: '', label: 'Todos' },
  { value: 'cachorro', label: 'Cachorro' },
  { value: 'gato', label: 'Gato' },
  { value: 'passaro', label: 'Pássaro' },
  { value: 'outro', label: 'Outro' }
]

const Home = () => {
  const [typeFilter, setTypeFilter] = useState<string>()
  const [showAdopted, setShowAdopted] = useState<boolean>(true)
  const [currentPage, setCurrentPage] = useState<number>(1)
  const [currentAdoptedPage, setCurrentAdoptedPage] = useState<number>(1)
  const {
    data: available,
    isLoading,
    isError,
    totalPages
  } = useGetAllAnimals({
    status: 'disponivel',
    type: typeFilter,
    size: 10,
    page: currentPage - 1
  })
  const { data: adopted, totalPages: totalAdoptedPages } = useGetAllAnimals({
    status: 'adotado',
    type: typeFilter,
    size: 4,
    page: currentAdoptedPage - 1
  })

  const handlePageClick = (pageNumber: number) => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
    setCurrentPage(pageNumber)
  }

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
    <div>
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
        <Line />
        <div className="filterBox">
          <p className="text--small">
            <i className="fa-solid fa-filter"></i> Filtrar
          </p>
          <StyledSelectWrapper
            placeholder="Selecione..."
            value={
              options.find((option) => option.value === typeFilter) ??
              options[0]
            }
            onChange={(option) => {
              setTypeFilter((option?.value as string) ?? '')
              setCurrentPage(1)
              setCurrentAdoptedPage(1)
            }}
            options={options}
            fontSize={12}
          />
          <button
            onClick={() => setShowAdopted(!showAdopted)}
            className="btnFilter"
            type="button"
          >
            {showAdopted ? (
              <>
                <i className="fa-solid fa-eye-slash"></i> esconder animais
                adotados
              </>
            ) : (
              <>
                <i className="fa-solid fa-eye"></i> mostrar animais adotados
              </>
            )}
          </button>
        </div>
        {available.length == 0 && (
          <div className="box">
            <p className="text">
              Parece que não temos nenhum pet para adoção no momento, volte
              outra hora!
            </p>
          </div>
        )}
        {available.length > 0 && (
          <>
            <p className="text">
              Você está em busca de um AUmigo para dividir momentos especiais?
              Esses bichinhos estão esperando por você
            </p>
            <CardContainer>
              {available.map((entity) => (
                <Card key={entity.id} animal={entity} />
              ))}
            </CardContainer>
            {totalPages && totalPages > 1 && (
              <div className="pagesContainer">
                <ul>
                  <li>
                    <button
                      disabled={currentPage === 1}
                      onClick={() => handlePageClick(currentPage - 1)}
                      className="navBtn"
                    >
                      <i className="fa-solid fa-chevron-left"></i>
                    </button>
                  </li>
                  {Array.from({ length: totalPages }, (_, i) => (
                    <li key={i + 1}>
                      <button
                        className={currentPage === i + 1 ? 'active' : ''}
                        onClick={() => handlePageClick(i + 1)}
                      >
                        {i + 1}
                      </button>
                    </li>
                  ))}
                  <li>
                    <button
                      disabled={currentPage === totalPages}
                      onClick={() => handlePageClick(currentPage + 1)}
                      className="navBtn"
                    >
                      <i className="fa-solid fa-chevron-right"></i>
                    </button>
                  </li>
                </ul>
              </div>
            )}
          </>
        )}

        {showAdopted && adopted.length > 0 && (
          <>
            <p className="text">
              De uma olhada também nestes amiguinhos que já conseguiram um lar
            </p>
            <CardContainer>
              {adopted.map((entity) => (
                <Card key={entity.id} animal={entity} />
              ))}
            </CardContainer>
            {totalAdoptedPages && totalAdoptedPages > 1 && (
              <div className="pagesContainer">
                <ul>
                  <li>
                    <button
                      disabled={currentAdoptedPage === 1}
                      onClick={() => handlePageClick(currentAdoptedPage - 1)}
                      className="navBtn"
                    >
                      <i className="fa-solid fa-chevron-left"></i>
                    </button>
                  </li>
                  {Array.from({ length: totalAdoptedPages }, (_, i) => (
                    <li key={i + 1}>
                      <button
                        className={currentAdoptedPage === i + 1 ? 'active' : ''}
                        onClick={() => handlePageClick(i + 1)}
                      >
                        {i + 1}
                      </button>
                    </li>
                  ))}
                  <li>
                    <button
                      disabled={currentAdoptedPage === totalAdoptedPages}
                      onClick={() => handlePageClick(currentAdoptedPage + 1)}
                      className="navBtn"
                    >
                      <i className="fa-solid fa-chevron-right"></i>
                    </button>
                  </li>
                </ul>
              </div>
            )}
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
    </div>
  )
}

export default Home
