import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTheme } from 'styled-components'

import CardForProfile from '../CardForProfile'
import Modal from '../Modal'

import { useDeleteAnimal, useGetMyAnimals } from '../../hooks/useAnimals'

import { Container } from './styles'
import { StyledButton } from '../../styles'

const MyAnimals = () => {
  const theme = useTheme()
  const navigate = useNavigate()
  const [showDeleteModal, setShowDeleteModal] = useState<boolean>(false)
  const [currentPage, setCurrentPage] = useState<number>(1)
  const { data, totalPages } = useGetMyAnimals({
    page: currentPage - 1,
    size: 4
  })
  const [animal, setAnimal] = useState<Animal>()
  const { mutate } = useDeleteAnimal()

  const handlePageClick = (pageNumber: number) => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
    setCurrentPage(pageNumber)
  }

  return (
    <Container>
      <div className="animals">
        {data.length == 0 && (
          <p className="text">Você ainda não cadastrou nenhum animal!</p>
        )}
        {data.map((animal) => (
          <CardForProfile key={animal.id} animal={animal}>
            <div className="btnGroup">
              {animal.status.toLowerCase() !== 'adotado' ? (
                <>
                  <StyledButton
                    $fontSize="14px"
                    onClick={() => navigate('/editAnimal/' + animal.id)}
                  >
                    <i className="fa-solid fa-pen-to-square"></i> Editar
                  </StyledButton>
                  <StyledButton
                    $fontSize="14px"
                    $backgroundColor="#E3C1A3"
                    onClick={() => {
                      setShowDeleteModal(true)
                      setAnimal(animal)
                    }}
                  >
                    <i className="fa-solid fa-ban"></i> Deletar
                  </StyledButton>
                </>
              ) : (
                <>
                  <StyledButton $fontSize="14px" disabled>
                    Adotado!
                  </StyledButton>
                </>
              )}
            </div>
          </CardForProfile>
        ))}
      </div>
      {!!totalPages && totalPages > 1 && (
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

      <Modal
        onClose={() => setShowDeleteModal(false)}
        isOpen={showDeleteModal}
        title={animal?.name ? animal.name : 'Deletar'}
      >
        <div className="modalContainer">
          <p>Você quer excluir este animal?</p>
          <div className="btnGroup">
            <StyledButton
              $fontSize="14px"
              $backgroundColor={theme.colors.highlightColor}
              $maxWidth="fit-content"
              onClick={() => {
                mutate(animal!.id)
                setShowDeleteModal(false)
              }}
            >
              Sim
            </StyledButton>
            <StyledButton
              $fontSize="14px"
              $backgroundColor={theme.colors.highlightColor}
              $maxWidth="fit-content"
              onClick={() => setShowDeleteModal(false)}
            >
              Não
            </StyledButton>
          </div>
        </div>
      </Modal>
    </Container>
  )
}

export default MyAnimals
